from fastapi import FastAPI, Depends, BackgroundTasks
from sqlalchemy.orm import Session
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup
import urllib.parse
import time
from datetime import datetime
from database import get_db
from model import Accommodation
import asyncio
from concurrent.futures import ThreadPoolExecutor
import logging

app = FastAPI()

# 로깅 설정 (파일로도 저장하고 싶다면, filename="scraper.log" 추가)
logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

executor = ThreadPoolExecutor(max_workers=5)  # 병렬 작업을 위한 스레드 풀


def extract_lent_price_info(price_elements):
    """ 가격 및 시간 정보 파싱 """
    price_info = {"discount_rate": None, "time": None, "origin_price": None, "price": None, "status": True}

    texts = [elem.strip() for elem in price_elements if elem.strip()]  # ✅ .text 제거
    logging.info(f"[STEP 1] 추출된 렌트 가격 요소: {texts}")

    if len(texts) == 1:
        if "판매가" in texts[0]:  # 예약 마감
            text = texts[0].split(' ')
            price_info["price"] = int(text[1].replace(',', '').replace('원', ''))
            price_info["status"] = False
        elif "숙소에 문의" in texts[0]: # 숙소에 문의
            return price_info
        else:
            price_info["time"] = texts[0].replace("시간", '')
        if len(texts) == 3:
            price_info["time"] = texts[0].replace("시간", '')
            price_info["discount_rate"] = int(texts[1].replace('%', ''))
            price_info["origin_price"] = int(texts[2].replace(',', '').replace('원', ''))

    logging.info(f"[STEP 2] 파싱된 가격 정보: {price_info}")
    return price_info


def extract_lodgment_price_info(price_elements):
    """ 가격 및 시간 정보 파싱 """
    price_info = {"discount_rate": None, "time": None, "origin_price": None, "price": None, "status": True}

    texts = [elem.strip() for elem in price_elements if elem.strip()]  # ✅ .text 제거
    logging.info(f"[STEP 1] 추출된 숙박 가격 요소: {texts}")

    if len(texts) == 1:
        if "판매가" in texts[0]:  # 예약 마감
            text = texts[0].split(' ')
            price_info["price"] = int(text[1].replace(',', '').replace('원', ''))
            price_info["status"] = False
        elif "숙소에 문의" in texts[0]: # 숙소에 문의
            return price_info
        else:
            price_info["time"] = texts[0].replace('~', '')
    if len(texts) == 3:
        price_info["time"] = texts[0].replace('~', '')
        price_info["discount_rate"] = int(texts[1].replace('%', ''))
        price_info["origin_price"] = int(texts[2].replace(',', '').replace('원', ''))

    logging.info(f"[STEP 2] 파싱된 가격 정보: {price_info}")
    return price_info


def sync_selenium_task(location: str, db: Session):
    """ 야놀자 크롤링 & MySQL 저장 """
    try:
        logging.info(f"[START] 크롤링 시작 - 지역: {location}")
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")

        service = Service(ChromeDriverManager().install())
        driver = webdriver.Chrome(service=service, options=chrome_options)

        keyword = urllib.parse.quote(location)
        url = f"https://www.yanolja.com/search/{keyword}?keyword={keyword}&searchKeyword={keyword}"
        logging.info(f"[INFO] 요청 URL: {url}")

        driver.get(url)
        time.sleep(5)  # 페이지 로딩 대기

        # 30번 스크롤 다운
        for _ in range(30):
            driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
            time.sleep(2)  # 데이터 로딩 대기

        soup = BeautifulSoup(driver.page_source, "html.parser")
        driver.quit()

        accommodations = []
        elements = soup.select("div.PlaceListItemText_contents__2GR73.place-content")
        logging.info(f"[INFO] 총 {len(elements)}개의 숙소 정보 발견")

        for element in elements:
            name = element.select_one("div.PlaceListTitle_container__qe7XH").text.strip() if element.select_one(
                "div.PlaceListTitle_container__qe7XH") else None
            score = float(element.select_one("span.PlaceListScore_rating__3Glxf").text.strip()) if element.select_one(
                "span.PlaceListScore_rating__3Glxf") else None
            category = element.select_one("div.PlaceListGrade_container__1oIhJ").text.strip() if element.select_one(
                "div.PlaceListGrade_container__1oIhJ") else None

            logging.info(f"[INFO] 숙소 정보: 이름={name}, 평점={score}, 카테고리={category}")

            price_info_elements = element.select("div.PlacePriceInfoV2_topInfo__mDkFX span")
            price_info_texts = [BeautifulSoup(str(e), "html.parser").text.strip() for e in price_info_elements if
                                BeautifulSoup(str(e), "html.parser").text.strip()]
            logging.info(f"{price_info_texts}")

            lent_info_elements = price_info_texts[1:price_info_texts.index("숙박")]
            lodgment_info_elements = price_info_texts[price_info_texts.index("숙박") + 1:]

            lent_parsed_info = extract_lent_price_info(lent_info_elements)
            lodgment_parsed_info = extract_lodgment_price_info(lodgment_info_elements)

            # 모든 가격 정보를 가져오기
            prices = element.select("div.PlacePriceInfoV2_bottomInfo__2h62q")
            logging.info(f"[DEBUG] 추출된 가격 정보: {prices}")

            # 텍스트만 추출하여 리스트로 변환
            price_texts = [price.text.strip() for price in prices if price.text.strip()]
            logging.info(f"[DEBUG] 추출된 가격 텍스트 리스트: {price_texts}")

            if len(price_texts) == 1:
                price_texts.insert(0, "0")

            accommodation = {
                "local": location,
                "name": name,
                "score": score,
                "category": category,
                "lent_discount_rate": lent_parsed_info.get("discount_rate") if lent_parsed_info["discount_rate"] else 0,
                "lent_time": int(lent_parsed_info["time"]) if lent_parsed_info["time"] else 0,
                "lent_origin_price": lent_parsed_info.get("origin_price") if lent_parsed_info["origin_price"] else 0,
                "lent_price": int(price_texts[0].replace(',', '').replace('원~', '').replace('최대할인가', ''))
                if price_texts[0] and lent_parsed_info.get("status", True) and price_texts[0] != "예약마감" and price_texts[0] != "숙소에 문의" else 0,
                "lent_status": lent_parsed_info.get("status", True),
                "enter_time": datetime.strptime(lodgment_parsed_info["time"], "%H:%M").time() if lodgment_parsed_info[
                    "time"] else datetime.strptime("00:00", "%H:%M").time(),
                "lodgment_discount_rate": lodgment_parsed_info.get("discount_rate") if lent_parsed_info[
                    "discount_rate"] else 0,
                "lodgment_origin_price": lodgment_parsed_info.get("origin_price") if lent_parsed_info[
                    "origin_price"] else 0,
                "lodgment_price": int(price_texts[1].replace(',', '').replace('원~', '').replace('최대할인가', ''))
                if price_texts[1] and lodgment_parsed_info.get("status", True) and price_texts[1] != "예약마감" and price_texts[1] != "숙소에 문의" else 0,
                "lodgment_status": lodgment_parsed_info.get("status", True),
            }

            logging.info(f"[STEP 3] DB 저장할 데이터: {accommodation}")

            accommodations.append(accommodation)

        for data in accommodations:
            existing = db.query(Accommodation).filter_by(name=data["name"]).first()
            if existing:
                logging.info(f"[UPDATE] 기존 데이터 업데이트: {data['name']}")
                for key, value in data.items():
                    setattr(existing, key, value if value is not None else getattr(existing, key))
            else:
                logging.info(f"[INSERT] 새로운 데이터 추가: {data['name']}")
                db.add(Accommodation(**data))

        db.commit()
        logging.info("[SUCCESS] 데이터 저장 완료!")
        return {"status": "success", "location": location, "count": len(accommodations)}

    except Exception as e:
        logging.error(f"[ERROR] 크롤링 및 DB 저장 중 에러 발생: {str(e)}", exc_info=True)
        return {"status": "fail", "error": str(e)}


async def run_selenium(location: str, db: Session):
    """야놀자 크롤링 & MySQL 저장 (비동기)"""
    loop = asyncio.get_event_loop()
    return await loop.run_in_executor(executor, sync_selenium_task, location, db)


@app.get("/scrape/{location}")
async def scrape_endpoint(location: str, background_tasks: BackgroundTasks, db: Session = Depends(get_db)):
    """비동기 크롤링 실행"""
    logging.info(f"크롤링 요청 받음: {location}")
    background_tasks.add_task(run_selenium, location, db)
    return {"status": "scraping started", "location": location}