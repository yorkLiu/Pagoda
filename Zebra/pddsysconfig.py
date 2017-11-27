# -*- coding: UTF-8 -*-
# coding:UTF-8
# from __future__ import unicode_literals
import sys
import os
import ConfigParser
import time
import requests
import json
import re
import linecache
import hashlib

import logging
from appconfig import ApplicationConfiguration
import constants as Constants


reload(sys)
sys.setdefaultencoding('UTF-8')


#################### Description [START] ####################
# .configs folder stored addresses.data and config.txt files
# .cookies folder stored all user cookies info, every user has it's own cookie file (per file per user)
# .logs folder stored the logs files
# .data folder stored date-finished-order-info.data(json string)
# Merchant-Orders stored 商家下单信息
# Finished-Orders stored 已完成的订单信息
#################### Description [E N D] ####################

######################## Utils [START] ########################
date_time_formatter='%Y-%m-%d %H:%M:%S'

def decode_chinese(chinese_characters):
    return str(chinese_characters).strip().decode('utf-8')

def check_folder_exists(folder_path):
    """
    Check the folder is exists, if not exists then create the folder(s)
    :param folder_path:
    :return:
    """
    if not os.path.exists(folder_path):
        os.mkdir(folder_path)

from Crypto.Cipher import AES
import base64

secret_password='YtsDOtsButDogrew'
my_cipher = AES.new(secret_password,AES.MODE_ECB)
def encrypt(value):
    encoded = base64.b64encode(my_cipher.encrypt(value.rjust(32)))
    return encoded

def decrypt(encoded_value):
    decoded = my_cipher.decrypt(base64.b64decode(encoded_value))
    return decoded.strip()



def store_locked_account(username):
    """
    store the locked username info
    format:
       username--date,
    :param username:
    :return:
    """
    content_tpl = '%s--%s' % (username, today_text)
    with open(os.path.join(config_folder_path, 'locked-account.txt'), 'a') as f:
        f.write(content_tpl)
        f.flush()

######################## Utils [E N D] ########################

######################### INIT the log configuration [START] ############################
from selenium.webdriver.remote.remote_connection import LOGGER

# set the selenium logger level to WARNING
LOGGER.setLevel(logging.WARNING)
# set the urllib3 logger level to WARNING
logging.getLogger("urllib3").setLevel(logging.WARNING)
# set requests logger level to WARNING
logging.getLogger("requests").setLevel(logging.WARNING)

log_folder_name=os.path.join(os.getcwd(), '.logs')
check_folder_exists(log_folder_name)

log_file_name=os.path.join(log_folder_name, 'log-%s.log' % time.strftime('%Y-%m-%d'))
logging.basicConfig(filename=log_file_name, filemode='a', format='[%(name)s] %(asctime)s %(levelname)s %(filename)s - %(funcName)s(%(lineno)s) | %(message)s', level=logging.DEBUG)
logger = logging.getLogger("PDD-Auto-Order")
######################### INIT the log configuration [E N D] ############################


########################## Generate Zip File [START] ##########################
import zipfile
from os.path import basename
def zip_account_cookies(accounts_info, zip_file_name=None):
    if accounts_info:
        zip_file_name = zip_file_name if zip_file_name else decode_chinese('%s PDD %s Account-Cookies(用于免验证码登陆)' % (today_text, len(accounts_info)))
        zip_file_path = os.path.join(data_folder_path, '%s.zip' % zip_file_name)
        zip_file = zipfile.ZipFile(zip_file_path, 'w', zipfile.ZIP_STORED)
        for username in accounts_info or []:
            f = os.path.join(cookie_store_path, cookie_file_name_prefix % username)
            base_file_name = basename(f)
            zip_file.write(f, base_file_name)
            logger.debug('Added [%s] to [%s] zip file', base_file_name, zip_file_name)

        zip_file.close()
        return zip_file_path

    return None

def zipdir(path, ziph, zip_filename_prefix=None):
    # ziph is zipfile handle
    if os.path.isdir(path):
        for root, dirs, files in os.walk(path):
            for file in files:
                ziph.write(os.path.join(root, file),basename(file))
    else:
        if os.path.exists(path):
            file_name = '%s-%s' % (zip_filename_prefix, basename(path)) if zip_filename_prefix else basename(path)
            ziph.write(path, file_name)

def send_debug_email():
    attachments = list()
    zip_file_name = '%s-cookies-orders-exportFiles-logs-debug' % today_text
    zip_file_name_path = os.path.join(data_folder_path, '%s.zip' % zip_file_name)

    if os.path.exists(zip_file_name_path):
        os.remove(zip_file_name_path)

    with zipfile.ZipFile(zip_file_name_path, 'w', zipfile.ZIP_STORED) as zip_file:
        # zip all cookie files in cookies folder
        zipdir(cookie_store_path, zip_file)

        # zip today export files in export folder
        zipdir(export_to_today_folder_path, zip_file)

        # zip today log file
        zipdir(log_file_name, zip_file)

        # zip today order in finished-orders
        zipdir(today_finished_order_file_name, zip_file, decode_chinese('已完成订单'))
        # zip today order in merchant-orders folders
        zipdir(os.path.join(order_merchant_folder_path, '%s.txt' % today_text), zip_file, decode_chinese('商家计划订单'))

        # zip today finished order info (json data)
        zipdir(today_finished_order_info_file_path, zip_file)

        attachments.append(zip_file_name_path)

    content = ('<b>%s PDD 所有 Debug 信息如下:</b>' % today_text).encode('utf-8')
    sendemail.send_email(content=content,attachments=attachments, append_attach_as_email_content=False)

########################## Generate Zip File [E N D] ##########################


# This file configurations are all system config

################# 购买方式 [START] ###################
# 只开团
buy_type_by_open_group_only=1
# 只参团
buy_type_by_join_group_only=2
# 开团参团
# 如果该商品已开有团，将已有的团拼完再开新的团
# 如果该商品已经没有可以拼的团，则新开一个团
buy_type_by_open_and_join_group=3
################# 购买方式 [E N D] ###################


################# 支付方式 [START]  ###################
pay_type_alipay_text='支付宝'
pay_type_weixin_text='微信好友代付'
################# 支付方式 [E N D]  ###################

####### Config the account cookies store path [START] ######
cookie_store_path=os.path.join(os.getcwd(),'.cookies')
cookie_file_name_prefix='%s-cookie.data'
cookie_store_keys=['PDDAccessToken','api_uid', 'pdd_user_id']
####### Config the account cookies store path [E N D] ######



########### sms instance info [START]###########
sms_author_id='devPagodaUpperMe'
sms_pid='20150'
smsUserInfo = None

## appconfig instance
app_configuration=None
########### sms instance info [E N D]###########


data_folder_path=os.path.join(os.getcwd(), '.data')
config_folder_path=os.path.join(os.getcwd(), '.configs')
check_folder_exists(data_folder_path)
check_folder_exists(config_folder_path)
########## Order folders path config [START] ##########
# 商家订单 信息 存放路径
# 格式为: (split by '|')
#     商品ID|放单 数量|购买类型(开/参 团)|账号(多个账号用 "," 分隔)
#     goods_id|keyword|order_count|buy_type|mobile_phone
order_merchant_folder_path=os.path.join(os.getcwd(), 'Merchant-Orders')

# 已下单 文件存放路径 (已付款，未付款)
# 格式为: (split by '|')
#   账号|密码|订单号|关键词|收货人姓名|电话|地址|实际付款总额|订单状态|下单时间|商品ID|SKU_ID|商家ID|商家名称|下单IP|付款链接
#   username|password|order_sn|search_keyword|receive_name|receive_mobile|receive_address|payment_amount|order_status|order_time|goods_id|sku_id|mall_id|mall_name|ip_info|payment_url
order_finished_folder_path=os.path.join(os.getcwd(), 'Finished-Orders')
finished_order_file_header='账号|密码|订单号|关键词|收货人姓名|电话|地址|实际付款总额|订单状态|下单时间|商品ID|商品名称|SKU_ID|商家ID|商家名称|下单IP|付款链接'

today_text=time.strftime('%Y-%m-%d')
today_order_file_name='%s.txt' % today_text

# 今天已下定单 文件名
today_finished_order_file_name=os.path.join(order_finished_folder_path, today_order_file_name)

# 地址信息 文件路径
address_info_file_name=os.path.join(config_folder_path, 'addresses.data')

check_folder_exists(order_merchant_folder_path)
check_folder_exists(order_finished_folder_path)
########## Order folders path config [E N D] ##########


####################### Driver Configs [START] #######################
web_driver_base_path=os.path.join(config_folder_path, 'webdrivers')
check_folder_exists(web_driver_base_path)

web_driver_file_extension = ''
if sys.platform == 'win32':
    web_driver_file_extension='.exe'

chrome_driver_path = os.path.join(web_driver_base_path, 'chromedriver%s' % web_driver_file_extension)
phantomjs_driver_path =  os.path.join(web_driver_base_path, 'phantomjs%s' % web_driver_file_extension)

####################### Driver Configs [E N D] #######################


############################ Read Configuration from Config File[START]############################
Config = ConfigParser.ConfigParser()
config_file_path=os.path.join(config_folder_path,'config.txt')
Config.read(config_file_path)


def get_config(section, property_name):
    return Config.get(section, property_name)

def save_config(section, property_name, property_value):
    config_file = open(config_file_path, 'w')
    Config.set(section, property_name, property_value)
    Config.write(config_file)
    config_file.flush()
    config_file.close()

def get_configurations():
    dict = {}
    sections = Config.sections()
    for section in sections:
        options = Config.options(section)
        for option in options:
            try:
                dict[option] = Config.get(section, option)
            except:
                dict[option] = None

    return dict

def load_app_configurations():
    """
    load configurations from config file
    :return: ApplicationConfiguration instance
    """
    appConfig = ApplicationConfiguration()
    appConfig.set_properties(get_configurations())
    return appConfig


############################ Read Configuration from Config File[E N D]############################


############################ cookie functions [START]############################
def save_cookies(username, cookies):
    """
    Store the cookie to file and the file name is just like 'username-cookie.data'
    :param username:
    :param cookies:
    :return:
    """
    if not os.path.exists(cookie_store_path):
        os.mkdir(cookie_store_path)

    LINE = "document.cookie = '{name}={value}; path={path}; domain=.yangkeduo.com;'\n"
    cookie_file_name=os.path.join(cookie_store_path, cookie_file_name_prefix % username)
    with open(cookie_file_name, 'w') as file:
        for cookie in cookies:
            if cookie['name'] in cookie_store_keys:
                file.write(LINE.format(**cookie))


def get_cookies_by_username(username):
    """
    Get username cookies, if found username cookie then return the cookies content
    else if not found the username cookie file, then need login, return None
    :param username:
    :return: cookies content or None
    """
    cookie_file_name = os.path.join(cookie_store_path, cookie_file_name_prefix % username)
    if os.path.exists(cookie_file_name):
        cookie_content=''
        with open(cookie_file_name, 'r') as file:
            cookie_content +=file.read()
        return cookie_content

    return None

############################ cookie functions [END]##############################


############################ ADSL Configuration [START]##############################
# from adsl import ADSL_dial as ADSLDial
adsl_name='PDDADSL'
def ping():
    r = requests.get('http://ip.chinaz.com/getip.aspx')
    text = r.text.replace('ip', "'ip'").replace('address', "'address'").replace("'", '"')
    text = decode_chinese(text)
    logger.debug(text)
    data = json.loads(text, encoding='UTF-8')
    return '%s  %s' % (data['ip'], data['address'])

def change_ip(adsl_username, adsl_password):
    """
    Change IP using adsl connect
    only support Window System
    sys.platform: (Linux: linux, linux2, MAC OX: darwin, Windows: win32)
    :param adsl_username:
    :param adsl_password:
    :return:
    """
    print "Change IP with ADSL, current system is: ", sys.platform
    if sys.platform == 'win32':
        try:
            adsl_disconnect()
        except:
            pass

        return adsl_connect(adsl_username, adsl_password)
    else:
        print "ERROR: Current system operation not support ADSL connection."
        return ping()

def adsl_connect(username, password):
    """
    ADSL connect with username and password
    :param username:
    :param password:
    :return: True: connect successfully, False: connect failed
    """

    # adsl = ADSLDial(username, password)
    # adsl.disconnect()
    # connect_result = adsl.connect()
    # print 'connect_result:', connect_result
    # if connect_result == 0:
    #     ip = ping()
    #     logger.info('ADSL connection successfully, IP[%s]', ip)
    #     print "ADSL connection successfully."
    #     time.sleep(3)
    #     return ip
    # else:
    #     print "connect failed, will re-connect"
    #     time.sleep(5)
    #     return adsl_connect(username, password)


    cmd_str = 'Rasdial %s %s %s /phonebook:%s' % (adsl_name, username, password, os.path.join(config_folder_path, 'pddadsl.pbk'))
    print 'cmd_str:', cmd_str
    res = os.system(cmd_str)
    if int(res) == 0:
        print "connection successfully."
        time.sleep(5)
        return ping()
    else:
        print "connect failed, will re-connect"
        time.sleep(10)
        return adsl_connect(username, password)

def adsl_disconnect():
    cmdstr = 'Rasdial /disconnect'
    print 'disconnect cmdstr:', cmdstr
    os.system(cmdstr)
    print "disconnection successfully."
    time.sleep(2)
############################ ADSL Configuration [E N D]##############################


############################ Read Order file content [START] ############################
def read_order_file_content(file_path):
    content = ''
    if(os.path.exists(file_path)):
        with open(file_path, 'r') as f:
            content += f.read().decode("utf-8-sig").encode("utf-8")
            f.flush()
    return content


# 用于存放 已经下好的订单信息
# {
#   total: total_orders_count
#   total_finished: total_finished_order_count,
#   2000003(goodsId): {total: total_order, total_finished: total_finished_count, last_order_time: last_order_time, status: FINISHED/NOT_FINISHED orders:[071111-2342342423,....]}
#   .......
# }

today_finished_order_info_file_name='%s-finished-order-info.json' % time.strftime('%Y-%m-%d')
today_finished_order_info_file_path=os.path.join(data_folder_path, today_finished_order_info_file_name)
finished_orders = None

def get_finished_order_info():
    logger.info('......get_finished_order_info, finished_orders:%s', finished_orders)
    data = None
    if os.path.exists(today_finished_order_info_file_path):
        with open(today_finished_order_info_file_path, 'r') as json_file:
            try:
                data = json.load(json_file)
            except:
                print '%s file is empty' % today_finished_order_info_file_path
                logger.debug('%s file is empty' % today_finished_order_info_file_path)
    logger.info('......after get_finished_order_info, finished_orders: %s', finished_orders)
    return data or {}

if not finished_orders:
    finished_orders = get_finished_order_info()

import sendemail
def update_finished_order_info(goods_id, order_sn):
    if goods_id and order_sn:
        goods_id = str(goods_id)
        order_sn = str(order_sn)
        print 'finished_orders:', finished_orders
        logger.debug('finished_orders:%s', finished_orders)
        finished_orders['total_finished'] = finished_orders['total_finished']+1 if finished_orders.has_key('total_finished') else 1
        if not finished_orders.has_key(goods_id):
            finished_orders[goods_id] = {'total_finished': 1, 'last_order_time': int(time.time()), 'orders': [order_sn]}
        else:
            finished_orders[goods_id]['total_finished'] = finished_orders[goods_id]['total_finished']+1 if finished_orders[goods_id].has_key('total_finished') else 1
            finished_orders[goods_id]['last_order_time'] =int(time.time())
            finished_orders[goods_id]['orders'].append(order_sn) if finished_orders[goods_id].has_key('orders') else [order_sn]

        total = int(finished_orders[goods_id]['total'])
        total_finished = int(finished_orders[goods_id]['total_finished'])
        if total_finished >= total:
            finished_orders[goods_id]['status'] = Constants.order_finished_status_text
            # if this goods_id has finished the orders
            # then export to csv file
            export_file_names = export_csv_file(goods_id=goods_id)

            # send email
            if export_file_names:
                sendemail.send_email(attachments=export_file_names)

        # write the json file to file
        with open(today_finished_order_info_file_path, 'w') as outfile:
            json.dump(finished_orders, outfile)
            outfile.flush()

        # if all orders finished, send the email group by mall_id
        total_order = int(finished_orders['total'])
        total_finished_order = int(finished_orders['total_finished'])
        if total_finished_order >= total_order:
            logger.info("All merchant orders were finished [total/finished]: %s/%s", total_order, total_finished_order)
            print 'All merchant orders were finished [total/finished]: %s/%s' % (total_order, total_finished_order)
            attachments = export_csv_file()
            if attachments:
                sendemail.send_email(attachments=attachments, attach_accounts_info = True)


def update_finished_order_info_total_value(key, value, is_root=False):
    logger.debug('Update finished order info, key: %s, value: %s, isRoot: %s', key, value, is_root)
    if is_root:
        finished_orders[key] = value
    else:
        if finished_orders.has_key(key):
            finished_orders[key]['total'] = value
        else:
            finished_orders[key] = {'total': value}

def read_today_merchant_orders():
    """
    read today's merchant orders (加载今天 商家订单信息)
    :return: json array
    [{
        goods_id: goods_id,
        order_count: order_count,
        buy_type: buy_type (@pddsysconfig.buy_type_by_open_*),
        username: username
    }]
    """
    orders = list()
    file_name=os.path.join(order_merchant_folder_path, today_order_file_name)
    content = read_order_file_content(file_name).decode('utf-8')
    goodsIDSet = set()

    if content:
        items = content.split('\n')
        for item in items:
            print item
            if item:
                item = re.sub(r'\s+', '', item)
                data = item.split('|')
                goods_id=data[0].strip()
                keyword = str(data[1]).strip()
                order_count = int(data[2])
                buy_type = int(data[3])
                goodsIDSet.add(goods_id)

                users = None
                if len(data) >= 5 and data[4]:
                    users = [u.strip() for u in data[4].split(',')]

                if order_count > 0:
                    for i in range(order_count):
                        username = None
                        if users and len(users) > i:
                            username = users[i]

                        orders.append({'goods_id': goods_id, 'keyword': keyword, 'order_count': 1, 'buy_type': buy_type, 'username': username})

        return sorted_orders(orders, 'goods_id')
    return []

def read_today_merchant_orders_recycle(queue):
    original_md5=''
    wait_count=0
    file_name = os.path.join(order_merchant_folder_path, today_order_file_name)
    while True:
        new_md5=hashlib.md5(read_order_file_content(file_name)).hexdigest()
        print 'md5:', new_md5
        logger.debug('MD5(%s) = %s', file_name, new_md5)
        if original_md5 != new_md5:
            wait_count =0
            original_md5 = new_md5
            logger.info('Merchant Order file was changed, will start run')
            orders = read_today_merchant_orders()
            for order in orders:
                while True:
                    if queue.full():
                        time.sleep(30)
                    else:
                        queue.put(order)
                        break

        else:
            wait_count = wait_count+1
            if wait_count > 5:
                # wait 40 seconds
                time.sleep(40)
            else:
                # wait 1 minutes
                # time.sleep(5*60)
                time.sleep(60)

def save_order(username, password, order_sn, keyword, address, order_info, ip_info, payment_url):
    """
    Save Order info to file
    :param username:
    :param password:
    :param order_sn:
    :param keyword:
    :param address: json object
    :param order_info:
            {
            order_sn: order_sn,
            order_amount: order_amount,
            order_status: order_status_desc,
            mall_id: mall_id,
            mall_name: mall_name,
            order_goods:[
                {
                    goods_id: goods_id,
                    goods_name: goods_name,
                    sku_id: sku_id,
                    spec: spec
                }
            ]
         }
    :param ip_info:
    :param payment_url:
    :return:
    """

    try:
        append_header = not os.path.exists(today_finished_order_file_name)
        data = []
        data.append(username)
        data.append(password)
        data.append(order_sn)
        data.append(keyword)
        data.append(address['name'])
        data.append(address['mobile'])
        data.append('%s %s %s %s' % (address['province'], address['city'], address['district'], address['address']))

        data.append(order_info['order_amount'])
        data.append(order_info['order_status'])
        data.append(time.strftime(date_time_formatter))
        goods_info=order_info['order_goods'][0]
        goods_id = goods_info['goods_id']
        data.append(goods_id)
        data.append(goods_info['goods_name'])
        data.append(goods_info['sku_id'])

        data.append(order_info['mall_id'])
        data.append(order_info['mall_name'])

        data.append(ip_info)
        data.append(payment_url)

        with open(today_finished_order_file_name, 'a') as f:
            if append_header:
                f.write('%s\n' % finished_order_file_header)

            f.write('%s\n' % '|'.join(str(v) for v in data))
            f.flush()

        # update the finished order info
        update_finished_order_info(goods_id, order_sn)

    except Exception as e:
        logger.error(e.message)


def group(iterable, key):
    """
    group a array by key
    :param iterable:
    :param key:
    :return:
    """
    groups = dict()
    key_value_set = set([item[key] for item in iterable])

    for value in key_value_set:
        if value:
            groups[str(value)] = [item for item in iterable if item[key] == value]

    return groups

def sorted_orders(iterable, key):
    """
    sorted the order 将要下单的商品ID 打乱顺序
    :param iterable:
    :param key:
    :return:
    """

    logger.info('Sort orders by key:%s', key)
    sorted_items = list()
    map_data = group(iterable, key)

    # update the finished_orders object total and every goods total values [START].
    update_finished_order_info_total_value('total', sum(len(map_data[k]) for k in map_data), True)
    [update_finished_order_info_total_value(k, len(map_data[k])) for k in map_data]
    # update the finished_orders object total and every goods total values [E N D].
    items = sorted(map_data.values(), key=lambda item: len(item), reverse=True)
    max_len = len(items[0])
    for i in xrange(max_len):
        for item in items:
            if (i < len(item)):
                sorted_items.append(item[i])

    return sorted_items


############################ Read Order file content [E N D] ############################



########################### get address [START] ###########################
# total lines for address data file
total_address_count=sum(1 for line in open(address_info_file_name))

def get_address_index():
    return int(get_config('Address', 'address_used_index'))

def save_address_index(index):
    save_config('Address', 'address_used_index', str(index))

special_provinces = ['北京', '上海', '重庆', '天津']
spcial_cities = ['乌兰察布市']

def conv_province(province):
    return province.replace('市', '').replace('省', '').replace('自治区', '').replace('壮族', '').strip()

def conv_city(province, city):
    if province in special_provinces:
        # 如果是直辖市, 则City就为相应的province
        return province
    elif '乌兰察布市' in city:
        return '乌兰察布市'
    else:
        return city.replace('市', '').strip()

def conv_district(district):
    return district.strip()

def conv_address(address):
    result = []
    addr_data = re.sub(r'\s+', '#', address.strip()).split('#')
    province = conv_province(addr_data[0])
    city = ''
    district = ''
    addr = ''
    if province in special_provinces:
        if len(addr_data) == 3:
            city = province
            district = conv_district(addr_data[1])
            addr = addr_data[2].strip()
        elif len(addr_data) == 4:
            city = conv_city(province, addr_data[1])
            district = conv_district(addr_data[2])
            addr = addr_data[3].strip()
    else:
        city = conv_city(province, addr_data[1])
        district = conv_district(addr_data[2])
        addr = addr_data[3].strip()

    result.append(province)
    result.append(city)
    result.append(district)
    result.append(addr)

    return result


def conv_address_from_front_end(font_end_address):
    if len(font_end_address) >= 6:
        return {
            'name': font_end_address[0],
            'mobile': str(font_end_address[1]),
            'province': font_end_address[2],
            'city': font_end_address[3],
            'district': font_end_address[4],
            'address': font_end_address[5]
        }

    return None

def get_address():
    """
    get a address
    :return: json object
     {
            'name': name,
            'mobile': mobile,
            'province': province,
            'city': city,
            'district': district,
            'address': addr
        }
    """
    # read address used index in configuration file
    address_used_index = get_address_index()
    # ---------------- check the index is ge total_address_count [START]-------------

    if address_used_index >= total_address_count:
        address_used_index = 1
        save_address_index(address_used_index)
    # ---------------- check the index is ge total_address_count [E N D]-------------
    address = decode_chinese(linecache.getline(address_info_file_name, address_used_index+1))

    if address:
        print address
        address_data = address.split(',')
        name=address_data[0]
        mobile=address_data[1]
        addr_data = conv_address(address_data[2])

        return {
            'name': name,
            'mobile': mobile,
            'province': addr_data[0],
            'city': addr_data[1],
            'district': addr_data[2],
            'address': addr_data[3]
        }

    return None

def get_address_and_save_the_address_index():
    address = get_address()
    address_used_index = get_address_index()
    save_address_index(address_used_index+1)
    return address

def get_self_address():
    pass

########################### get address [E N D] ###########################



########################## Generate CSV files [START] ##########################
import csv

export_folder_path = os.path.join(os.getcwd(), 'export')
export_to_today_folder_path=os.path.join(export_folder_path, today_text)
# file format: # mallName-goodsId-today orderCount 单.csv
export_by_goods_file_name=os.path.join(export_to_today_folder_path, '%s-%s-%s %s单.csv')
# file format: # mallName-today orderCount 单.csv
export_by_mall_file_name=os.path.join(export_to_today_folder_path, '%s-%s %s单.csv')


check_folder_exists(export_folder_path)
check_folder_exists(export_to_today_folder_path)

def export_csv_file(goods_id=None, mall_id=None, goods_id_index=10, mall_id_index=13, mall_name_index=14):
    """
    base on the finished orders generate the csv files
    :param goods_id: if goods_id only export the goods_id orders
    :param mall_id: if mall_id only export the mall_id orders
    :param goods_id_index:
    :param mall_id_index:
    :param mall_name_index:
    :return:
    """

    logger.info("Export to csv file.....")

    if not os.path.exists(today_finished_order_file_name):
        logger.error('Export finished orders to csv file failed, the finished order file not exists.')
        return False

    content = ''
    export_file_names = list()
    with open(today_finished_order_file_name, 'r') as file:
        content += file.read()

    value_delimiter= '|'
    if content:
        contents = content.split('\n')
        headers = contents[0].split(value_delimiter)
        orders = contents[1:]
        logger.info("There are :%s finished orders can export", len(orders))

        if goods_id:
            goods_id = str(goods_id).strip()
            write_orders = [order for order in orders if (order and (order.split(value_delimiter)[goods_id_index]) == goods_id)]
            if len(write_orders) > 0:
                mall_name = write_orders[0].split(value_delimiter)[mall_name_index]
                export_csv_file_name = export_by_goods_file_name % (mall_name, goods_id, today_text, len(write_orders))
                export_file_names.append(save_csv_file(export_csv_file_name, write_orders, headers))
                logger.info("Export order by goods_id: %s for mall_name: %s, file stored path: %s", goods_id, mall_name, export_csv_file_name)
            else:
                logger.warn("Export order by goods_id: %s, no order to export", goods_id)

        elif mall_id:
            mall_id = str(mall_id).strip()
            write_orders = [order for order in orders if (order and (order.split(value_delimiter)[mall_id_index]) == mall_id)]

            if len(write_orders) > 0:
                mall_name = write_orders[0].split(value_delimiter)[mall_name_index]
                export_csv_file_name = export_by_mall_file_name % (mall_name, today_text, len(write_orders))
                export_file_names.append(save_csv_file(export_csv_file_name, write_orders, headers))
                logger.info("Exported order by mall_id: %s, mall_name: %s, file stored path: %s", mall_id, mall_name, export_csv_file_name)
            else:
                logger.warn("Export order by mall_id: %s, no order to export", mall_id)
        else:
            # by default export all finished orders group by the mall_id.
            logger.info("Export all finished orders group by the mall_id.")
            mall_id_set = set([str(order.split(value_delimiter)[mall_id_index]) for order in orders if order] )
            for mallID in mall_id_set:
                mall_orders = [order for order in orders if (order and (order.split(value_delimiter)[mall_id_index]) == str(mallID))]
                if len(mall_orders) > 0:
                    mall_name = mall_orders[0].split(value_delimiter)[mall_name_index]
                    export_csv_file_name = export_by_mall_file_name % (mall_name, today_text, len(mall_orders))
                    export_file_names.append(save_csv_file(export_csv_file_name, mall_orders, headers))
                    logger.info("Exported order mall_id: %s, mall_name: %s, file stored path: %s", mall_id, mall_name, export_csv_file_name)

    return export_file_names

def save_csv_file(csv_file_name, orders, headers):
    export_file_name=os.path.join(os.getcwd(), csv_file_name).decode('utf-8')
    logger.info("export_file_name: %s" ,export_file_name)
    value_delimiter = '|'
    if csv_file_name and len(orders) > 0:
        with open(export_file_name, 'w') as outfile:
            outfile.write(u'\ufeff'.encode('utf-8'))
            writer = csv.writer(outfile, lineterminator='\n')
            writer.writerow([header for header in headers])
            for order in orders:
                writer.writerow(order.split(value_delimiter))

            outfile.flush()
        logger.info("Export to csv file successfully.")
        return export_file_name

    return None
########################## Generate CSV files [E N D] ##########################




if __name__ == '__main__':
    # encoded =  encrypt('TEST')
    # print encoded
    # print decrypt(encoded).strip()
    #
    # export_csv_file()
    # print read_today_merchant_orders()
    send_debug_email()