from fastapi import FastAPI
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup
import urllib.parse
import time
import redis
import json
import asyncio

app = FastAPI()

# Redis 연결
redis_client = redis.StrictRedis(host="localhost", port=6379, db=0, decode_responses=True)

chrome_options = Options()
chrome_options.add_argument("--no-sandbox")
chrome_options.add_argument("--disable-dev-shm-usage")

def run_selenium(location: str):
    """야놀자 크롤링 (숙소 정보 전체 가져오기)"""
    try:
        service = Service(ChromeDriverManager().install())
        driver = webdriver.Chrome(service=service, options=chrome_options)

        keyword = urllib.parse.quote(location)
        url = f"https://www.yanolja.com/search/{keyword}?keyword={keyword}&searchKeyword={keyword}"
        driver.get(url)
        time.sleep(5)  # 페이지 로딩 대기

        # HTML 파싱
        page_source = driver.page_source
        soup = BeautifulSoup(page_source, "html.parser")

        # 숙소 리스트 가져오기
        elements = soup.select("div.PlaceListItemText_contents__2GR73.place-content")
        results = {}

        for element in elements:
            try:
                name_tag = element.select_one(".PlaceListTitle_text__2511B")
                if not name_tag:
                    continue  # 숙소 이름이 없으면 무시
                name = name_tag.get_text(strip=True)

                data = {}

                # 각 정보 크롤링
                rating_tag = element.select_one(".PlaceListScore_rating__3Glxf")
                review_count_tag = element.select_one(".PlaceListScore_reviewInfo__3QSCU")
                category_tag = element.select_one(".PlaceListTitle_text__2511B")
                rental_time_tag = element.select_one(".PlacePriceInfoV2_topInfo__mDkFX")
                rental_price_tag = element.select_one(".PlacePriceInfoV2_bottomInfo__2h62q")
                checkin_time_tag = element.select_one(".PlacePriceInfoV2_topInfo__mDkFX")
                checkin_price_tag = element.select_one(".PlacePriceInfoV2_bottomInfo__2h62q")

                # 값이 존재하면 추가
                if rating_tag:
                    data["rating"] = rating_tag.get_text(strip=True)
                if review_count_tag:
                    data["review_count"] = review_count_tag.get_text(strip=True)
                if category_tag:
                    data["category"] = category_tag.get_text(strip=True)
                if rental_time_tag:
                    data["rental_time"] = rental_time_tag.get_text(strip=True)
                if rental_price_tag:
                    data["rental_price"] = rental_price_tag.get_text(strip=True)
                if checkin_time_tag:
                    data["checkin_time"] = checkin_time_tag.get_text(strip=True)
                if checkin_price_tag:
                    data["checkin_price"] = checkin_price_tag.get_text(strip=True)

                results[name] = data

                # Redis에 JSON 형태로 저장
                redis_key = f"yanolja:{name}"
                redis_client.set(redis_key, json.dumps(data, ensure_ascii=False))

            except Exception as e:
                print(f"Error processing element: {e}")

        driver.quit()

        if not results:
            raise ValueError("크롤링 결과가 없습니다.")

        return {"status": "success", "location": location, "results": results}

    except Exception as e:
        return {"status": "fail", "location": location, "error": str(e)}

@app.get("/scrape/{location}")
async def scrape_endpoint(location: str):
    """비동기 크롤링 실행 후 Redis에 저장"""
    loop = asyncio.get_running_loop()
    response = await loop.run_in_executor(None, run_selenium, location)
    return response
