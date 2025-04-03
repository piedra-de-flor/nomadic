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

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

executor = ThreadPoolExecutor(max_workers=5)

def sync_selenium_task(location: str, db: Session):
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

            accommodation = {
                "local": location,
                "name": name,
                "score": score,
                "category": category
            }

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
    loop = asyncio.get_event_loop()
    return await loop.run_in_executor(executor, sync_selenium_task, location, db)

@app.get("/scrape/{location}")
async def scrape_endpoint(location: str, background_tasks: BackgroundTasks, db: Session = Depends(get_db)):
    logging.info(f"크롤링 요청 받음: {location}")
    background_tasks.add_task(run_selenium, location, db)
    return {"status": "scraping started", "location": location}