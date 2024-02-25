from selenium import webdriver
from bs4 import BeautifulSoup
import urllib.parse
import time


def scrape_yanolja(local):
    hdr = {'User-agent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/603.1.23 (KHTML, like Gecko) Version/10.0 Mobile/14E5239e Safari/602.1'}

    driver = webdriver.Chrome()
    keyword = urllib.parse.quote(local)
    driver.get('https://www.yanolja.com/search/{keyword}?keyword={keyword}&searchKeyword={keyword}')

    time.sleep(5)
    page_source = driver.page_source
    soup = BeautifulSoup(page_source, 'html.parser')

    elements = soup.select('strong.PlaceListTitle_text__2511B, span.PlacePriceInfoV2_discountPrice__1PuwK')

    results = []
    for element in elements:
        results.append(element.text.strip())

    driver.quit()

    return results