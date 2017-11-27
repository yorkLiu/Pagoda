# -*- coding: UTF-8 -*-
# coding:utf-8
from __future__ import unicode_literals
import os
import random
import time


from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By

import constants


# JD order steps
# 1. login
# 2. search production by keyword
# 3. from the search result find the correct production (by sku)
# 4. browser the production with 30~60 seconds
# 5. add it to shopping car
# 6. confirm order, input the receiver information (full name, address and mobile phone)
# 7. pay the order



def initWebDriver():
    """
    Init web driver
    :return: webdriver
    """

    # chrome driver path
    chromedriver = "/Users/yongliu/Project/webDriver/chromedriver"
    os.environ["webdriver.chrome.driver"] = chromedriver

    # set user-agent random
    opts = Options()
    opts.add_argument("user-agent=%s" % random.choice(constants.USER_AGENT))

    driver = webdriver.Chrome(chromedriver, chrome_options=opts)

    return driver

def login(username, password):
    """
    Login with account login (not phone sms login)
    :param username: required
    :param password: required
    :return: true if login success, else return false.
    """
    pass


def search(driver, keyword):
    """
    search production by @keyword
    :param driver:
    :param keyword:
    :return:
    """

    # driver = webdriver.Chrome()

    search_field_id='index_newkeyword'
    WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, search_field_id)))

    search_text_field = driver.find_element_by_id(search_field_id)
    search_btn = driver.find_element_by_id('index_search_submit')
    search_text_field.click()
    time.sleep(2)
    search_text_field.clear()
    search_text_field.send_keys(keyword)
    time.sleep(3)
    search_btn.click()

    search_list_id='seach_list'
    WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, search_list_id)))
    lis = driver.find_elements(By.XPATH, "//div[@id='seach_list']//ul[not(contains(@class, 'displayNone'))]/li")
    for li in lis:
        a = li.find_element_by_tag_name("a")

        product_name = li.find_element(By.CLASS_NAME,"product-name")
        product_price = li.find_element(By.XPATH,"/div[contains(@class, )]")
        print a.get_attribute('href'), product_name.text, product_price.text




if __name__ == "__main__":
    driver = initWebDriver()
    driver.get(constants.JD_INDEX_URL)
    time.sleep(1)
    agent = driver.execute_script("return navigator.userAgent")
    print agent

    search(driver, '刹车片')




