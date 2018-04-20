#!/usr/bin/env python
# -*- coding: UTF-8 -*-
from __future__ import unicode_literals

import requests
import json
import time
import random

import logging
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By



import constants as CONSTANTS
from PDD import PDDLogin
from pddsysconfig import get_chat_messages_from_template
from pddsysconfig import save_malls
from pddsysconfig import remove_duplicate_mall
from pddsysconfig import get_mall_from_file
# from pddsysconfig import logger




from multiprocessing import Pool
import contextlib
import multiprocessing
from functools import partial
from contextlib import contextmanager


LOGGER = logging.getLogger(__name__)

@contextmanager
def poolcontext(*args, **kwargs):
    pool = multiprocessing.Pool(*args, **kwargs)
    yield pool
    pool.terminate()

def get_header():
    return {
        'User-Agent': random.choice(CONSTANTS.USER_AGENT),
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
        'Accept-Language': 'en-US,en;q=0.5',
        'Connection': 'keep-alive',
        'Accept-Encoding': 'gzip, deflate',
        'content-type': 'application/json'
    }


proxies = {
    'http': 'socks5://192.168.100.3:1083',
    'https': 'socks5://192.168.100.3:1083'
}

def get_all_categories(category_names=None):
    """
    Get categories if @category_names not assigned, by default get all level 1 category

    :param category_names: I.E (['男装', '鞋包',...])
    :return: json array
    [
        {
            opt_id: opt_id
            opt_name: opt_name,
            opt_type: 1/2(一级目录 为1, 二级目录为2)
        }
    ]
    """

    result = []
    r = requests.get(CONSTANTS.PDD_GET_ALL_CATEGORY)
    data = json.loads(r.text, encoding='UTF-8')
    for item in data:
        flag = item['opt_name'] in category_names if category_names else True
        if flag:
            result.append({
                'opt_id': item['opt_id'],
                'opt_name': item['opt_name'],
                'opt_type': 1
            })


        # print item['opt_id'], item['opt_name'], item['opt_desc']
        # for child in item['children']:
        #     print '\t', child['opt_id'], child['opt_name'], child['opt_desc']
    return result


def get_goods_by_category_id(category_info):
    if not category_info:
        LOGGER.error("No Category Info")
        return

    goods_id_array = []
    start = 0
    limit = 100
    end = limit
    idx = 1

    while True:
        url = CONSTANTS.PDD_GET_GOODS_BY_CATEGORY_ID.format(opt_id=category_info['opt_id'], opt_type=category_info['opt_type'], start=start, end=end)
        LOGGER.info("Visit url: %s", url)
        # r = requests.get(url)
        try:
            r = requests.get(url, headers = get_header())
        except:
            r = requests.get(url, headers=get_header())

        data = json.loads(r.text, encoding='UTF-8')
        if not data.has_key('goods_list'):
            LOGGER.info("ERROR:%s", r.status_code)
            print "ERROR:%s" % r.status_code
            break

        if len(data['goods_list']) == 0:
            break


        for item in data['goods_list']:
            goods_id_array.append(item['goods_id'])
            idx+=1

        start = end
        end = start + limit

    LOGGER.info("Found %s goods", len(goods_id_array))
    return goods_id_array


def get_mall_by_goods_id(goods_id):
    """
    Get mall info by goods_id
    :param goods_id:
    :return:
    """

    if not goods_id:
        LOGGER.error("No goods_id")
        return

    url = CONSTANTS.PDD_GOODS_DETAILS_API.format(goods_id=goods_id)

    try:
        r = requests.get(url, headers = get_header())
    except:
        r = requests.get(url, headers=get_header(), proxies=proxies)

    data = json.loads(r.text, encoding='UTF-8')
    if data and data.has_key('mall_id') and data['mall_id']:
        mall_id = data['mall_id']
        LOGGER.info("goods_id: %s --> mall_id: %s", goods_id, mall_id)
        print ("goods_id: %s --> mall_id: %s" % (goods_id, mall_id))
        return str(mall_id)

    return None

def get_malls(goods_id_array):
    """
    Get mall info by goods_id_array
    :param goods_id_array:
    :return: mall_ids array
    """

    max_pool_size = 10 if len(goods_id_array)/100 >= 10 else 2
    with contextlib.closing(Pool(processes=max_pool_size)) as pool:
        malls = pool.map(partial(get_mall_by_goods_id), goods_id_array)
        # res = pool.starmap(get_mall_by_goods_id, list(goods_id_array))

    mallSet = set([str(m) for m in malls if m])
    return mallSet


def crawl_mall(categories=None):
    crawl_categories = get_all_categories(categories)
    LOGGER.info("Found %s categories", len(crawl_categories))
    print "Found %s categories" % len(crawl_categories)
    for category in crawl_categories:
        print '----%s' % category['opt_name']
        goods_array = get_goods_by_category_id(category)
        LOGGER.info('goods_array length: %s', len(goods_array))
        save_malls(get_malls(goods_array))

    # 去掉重复的 mall_id
    remove_duplicate_mall()

class MessageTemplate:
    def __init__(self, msg_tpl_file):
        # 模板 文件
        self.template_file = msg_tpl_file

        # 模板文件中的 聊天内容 (每一行则是一条message)
        # messages 是一个list
        self.messages = get_chat_messages_from_template(msg_tpl_file)


    def getMessages(self):
        return self.messages



class ChatService(PDDLogin):
    """
    与客服聊天
    """
    def __init__(self, username, messageTemplate, malls):
        PDDLogin.__init__(self)

        self.username = username

        # message template
        self.messageTemplate=messageTemplate

        # 给哪些商家发送广告 (mall_id)
        self.malls=malls


    def chat(self):
        self.initWebDriver("chromedriver")
        self.login(self.username)

        # navigate to chat_list.html and get the page_id
        self.forward_to("chat_list.html")
        refer_page_id = self.getPageID()

        total = len(self.malls)
        idx = 0

        for mall_id in self.malls:
            if mall_id:
                idx +=1
                LOGGER.info("Send adv message to mall_id: [%s], sent: %s/%s", mall_id, idx, total)
                self.send_msg(mall_id, refer_page_id)


    def send_msg(self, mall_id, refer_page_id):
        # format the chat url with mall_id
        url = CONSTANTS.PDD_CHAT_BASE_URL.format(mall_id=mall_id, refer_page_id=refer_page_id)
        self.driver.get(url)

        input_field_id='message-input'

        WebDriverWait(self.driver, 60).until(EC.presence_of_element_located((By.ID, input_field_id)))
        input_field = self.driver.find_element_by_id(input_field_id)

        # after the chat page loaded, find the input id 'message-input' and enter the messages one-by-one
        for message in self.messageTemplate.getMessages() or []:
            input_field.send_keys(message)
            time.sleep(1)
            # click '发送'
            self.driver.execute_script("document.querySelector('.send-button').dispatchEvent(new Event('touchend'))")

            # every message will wait 3 seconds
            time.sleep(3)




# def merge_names(a, b):
#     return '{} & {}'.format(a, b)
#
# if __name__ == '__main__':
#     names = ['Brown', 'Wilson', 'Bartlett', 'Rivera', 'Molloy', 'Opie']
#     with poolcontext(processes=3) as pool:
#         results = pool.map(partial(merge_names, b='Sons'), names)
#     print(results)

if __name__ == '__main__':
    # crawl_mall()
    # remove_duplicate_mall()
    messageTemplate = MessageTemplate("template01.txt")
    username='17082294661'
    malls =  get_mall_from_file(0, 10)
    chatService = ChatService(username, messageTemplate, malls)
    chatService.chat()




