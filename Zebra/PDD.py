# -*- coding: UTF-8 -*-
# coding:utf-8
from __future__ import unicode_literals
import sys
import os
import random
import time
import re


from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
# from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By

import requests
# from requests.cookies import RequestsCookieJar
import json

from sms import SmsUtils
from sms import UserInfo
from sms import NotFoundMobilePhoneException
import constants
import pddsysconfig
from pddsysconfig import logger as log

# from appconfig import ApplicationConfiguration


reload(sys)
sys.setdefaultencoding('utf-8')


####### Utils [START]######
def convertToTime(seconds):
    m, s = divmod(seconds, 60)
    h, m = divmod(m, 60)
    temp_time = "%d:%02d:%02d" % (h, m, s)
    # print 'Seconds:', seconds, ' --> time is: ', temp_time
    return temp_time
####### Utils [E N D]######



def get_order_sn(text):
    """
    get the order id from the transform text
    the text can be a text or url text
    :param text:
    :return: order id  (i.e: 171108-304365461120350)
    """
    patt = '[0-9]{6}-[0-9]{1,}'
    m = re.search(patt, text)
    if m:
        group = m.group()
        return group


class PDDLogin:
    def __init__(self):
        self.driver = None

        if not pddsysconfig.smsUserInfo:
            pddsysconfig.app_configuration = app_configuration = pddsysconfig.load_app_configurations()
            log.info('Loaded the application configurations.')

            # init the sms info
            pddsysconfig.smsUserInfo = UserInfo(pddsysconfig.decrypt(app_configuration.sms_username),
                                                pddsysconfig.decrypt(app_configuration.sms_password),
                                                pddsysconfig.sms_author_id, pddsysconfig.sms_pid)
            log.info("Initialize the SMS plat form.")
            log.info('--smsUserInfo:%s', pddsysconfig.smsUserInfo)


    def initWebDriver(self, driver_name='phantomjs'):
        """
        Init web driver
        :return: webdriver
        """
        log.debug('Start initialize web driver')
        # set user-agent random
        opts = Options()
        user_agent=random.choice(constants.USER_AGENT)
        opts.add_argument("user-agent=%s" % user_agent)
        # mobile_emulation = {"deviceName": "iPhone 6 Plus"}
        # chrome_options = webdriver.ChromeOptions()
        # chrome_options.add_experimental_option("mobileEmulation", mobile_emulation)
        log.info("set up with driver with: '%s'", driver_name)
        if driver_name == 'chromedriver':
            # os.environ["webdriver.chrome.driver"] = pddsysconfig.chrome_driver_path
            self.driver = webdriver.Chrome(executable_path= pddsysconfig.chrome_driver_path, chrome_options=opts)
        elif driver_name == 'phantomjs':
            self.driver = webdriver.PhantomJS(pddsysconfig.phantomjs_driver_path)

        self.driver.maximize_window()
        log.debug('Web Driver was set up, the user-agent is: [%s]', user_agent)

    def enter_element_value(self, element_id, value):
        """
        Execute the java scrip to set element value by element_id in front-end.
        :param element_id:
        :param value:
        :return:
        """
        enter_value_by_id_script = "document.getElementById('{element_id}').value='{value}'"
        self.driver.execute_script(enter_value_by_id_script.format(element_id=element_id, value=value))

    def login(self,username=None, password=None):
        """
        Login PDD
        :param username:
        :param password:
        :return:
        """
        log.info('Start login to PDD......')
        login_with_cookie_success = False

        if username:
            self.username = username
            user_cookies = pddsysconfig.get_cookies_by_username(username)
            if user_cookies:
                self.driver.get(constants.PDD_INDEX_URL)
                time.sleep(2)
                self.driver.delete_all_cookies()
                self.driver.execute_script(user_cookies)
                self.driver.get(constants.PDD_PERSONAL_PAGE_URL)
                time.sleep(5)
                login_with_cookie_success = constants.PDD_PERSONAL_PAGE_URL in self.driver.current_url
                if login_with_cookie_success:
                    log.debug('The username [%s] was login successfully with cookie', username)
                    # close the alert pop up window
                    self.driver.execute_script("if(document.querySelector('.close-button')){document.querySelector('.close-button').click();}")
                    time.sleep(3)
                    self.driver.execute_script("if(document.body.querySelector('.alert-goto-app-cancel')){document.body.querySelector('.alert-goto-app-cancel').click()}")
                    user_info = self.driver.execute_script("return window.rawData['userInfo'] || 'NONE';")
                    log.info('username %s info: %s', username, user_info)
                    if user_info =='NONE':
                        log.info('The User [%s] was login by other people and the cookie was expired, need login once again.', username)
                        login_with_cookie_success = False
                        self.driver.delete_all_cookies()


        if not login_with_cookie_success:
            self.driver.get(constants.PDD_LOGIN_PAGE_URL)
            time.sleep(2)

            # login by mobile phone
            self.login_by_mobile_phone(username)

    def login_by_mobile_phone(self, mobile_phone=None):

        if constants.PDD_LOGIN_PAGE_URL not in self.driver.current_url:
            self.driver.get(constants.PDD_LOGIN_PAGE_URL)
            time.sleep(2)
            print self.driver.current_url


        # check the page was shows the "手机登陆"
        # if shows, click it to show the mobile phone form
        login_icon_opacity = self.driver.execute_script("return document.getElementById('first').style.opacity;")
        if login_icon_opacity != '0':
            self.driver.execute_script("document.querySelector('.phone_login').click();")
            time.sleep(1)

        vcode = None
        if mobile_phone:
            self.enter_element_value('user_mobile', mobile_phone)
            self.driver.execute_script("document.getElementById('code_button').click();")
            phone_numbers = SmsUtils.get_phone_numbers(pddsysconfig.smsUserInfo,mobile_phone)
            log.debug('Get phone_numbers: %s', phone_numbers)
            # get the vcode from sms platform
            if phone_numbers and phone_numbers[0] == mobile_phone:
                vcode = SmsUtils.get_vcode_from_phone_number(pddsysconfig.smsUserInfo, mobile_phone)
            else:
                log.error("The phone [%s] was not found in sms platform.", mobile_phone)
                # save the mobile_phone to locked file.
                pddsysconfig.store_locked_account(mobile_phone)
                # raise an exception
                raise NotFoundMobilePhoneException(mobile_phone)
        else:
            # not provide the phone number, get it from sms platform.
            phone_numbers = SmsUtils.get_phone_numbers(pddsysconfig.smsUserInfo)
            log.debug('Get phone_numbers: %s', phone_numbers)
            ignores = set(phone_numbers)
            for phone_number in phone_numbers:
                mobile_phone = phone_number
                log.debug('try to login with phone number: %s', mobile_phone)
                self.enter_element_value('user_mobile', mobile_phone)
                # sent sms
                self.driver.execute_script("document.getElementById('code_button').click();")

                vcode = SmsUtils.get_vcode_from_phone_number(pddsysconfig.smsUserInfo, mobile_phone)
                if vcode:
                    log.debug('phone number: %s received vcode: %s', mobile_phone, vcode)
                    ignores.remove(mobile_phone)
                    break
                else:
                    log.debug('the phone number: %s not receive vcode message, will try next phone.', mobile_phone)
            if len(ignores):
                log.debug("release the mobile phone numbers, %s", ','.join(ignores))
                SmsUtils.ignore(pddsysconfig.smsUserInfo, ignores)

        if vcode:
            # set the username with mobile_phone
            self.username = mobile_phone

            self.enter_element_value('input_code', vcode)
            # login
            self.driver.execute_script("document.getElementById('submit_button').click();")

            WebDriverWait(self.driver, 60).until(EC.presence_of_element_located((By.ID, 'footer_home')))
            # save cookies
            pddsysconfig.save_cookies(mobile_phone, self.driver.get_cookies())
            log.info('Username [%s] login successfully.', mobile_phone)

        # WebDriverWait(self.driver, 60).until(EC.presence_of_element_located((By.ID, 'footer_home')))
        # # save cookies
        # pddsysconfig.save_cookies(mobile_phone, self.driver.get_cookies())

    def getPageID(self):
        """
        PDD has assigned every page a "page_id"
        can run javaScript "Navigation.querySet.page_id" to get the it
        :return: visit page_id
        """

        page_id = self.driver.execute_script("return Navigation.querySet.page_id")
        log.debug('The current page ID is: %s', page_id)
        return page_id

    def forward_to(self, page_name):
        if page_name:
            log.debug("Will redirect to %s page", page_name)
            self.driver.execute_script("Navigation.forward('%s')" % page_name)
            time.sleep(5)


class PDDOrder(PDDLogin):
    def __init__(self):
        PDDLogin.__init__(self)
        # super(PDDOrder, self).__init__()
        # self.driver = None

        self.username=None
        self.password='NO'

        # 订单号
        self.order_sn=None

        #  搜索 关键词
        self.keyword=None

        # 下单 IP 信息
        self.order_ip_info=None

        ## default is pddsysconfig.pay_type_weixin_text
        self.payment_type=pddsysconfig.pay_type_weixin_text

        # 存放 用户自定义的地址信息
        # address object format @see pddsysconfig.conv_address_from_front_end
        self.self_define_address = None

        # 该订单 下单时的收货地址信息 (在 order_checkout页面提取)
        # address object format @see pddsysconfig.conv_address_from_front_end
        self.address = None

        # 购买方式
        self.buy_type = None



    def getMallInfo(self, mallId):
        """
        Get PDD mall information by mall_id
        from request API
        :param mallId:
        :return: if founded return json object
        {mall_id: maillId, mall_name: mallName, mall_sales: mallSales}
        else None
        """
        if mallId:
            r = requests.get(constants.PDD_MALL_INFO_API.format(mall_id=mallId))
            if r.text:
                data = json.loads(r.text, encoding='UTF-8')
                log.debug("Mall Info: mall_name:%s, mall_id: %s, mall_sales: %s", data['mall_name'], data['mall_id'], data['mall_sales'])
                return {'mall_id': data['mall_id'], 'mall_name': data['mall_name'], 'mall_sales': data['mall_sales']}

        return None

    def getGroups(self, goodsId):
        """
        Get first 5 groups for associated goods_id
        from request API
        :param goodsId:
        :return:
        {
            total: total_groups
            server_time: server_time
            groups:[{groupInfo1}, {groupInfo2},...]
        }
        """

        if goodsId:
            r = requests.get(constants.PDD_GOODS_GROUPS_API.format(goods_id=goodsId))
            if r.text:
                groups = []
                data = json.loads(r.text, encoding='UTF-8')
                log.debug('Total groups: %s', data['total'])
                json_groups = data['local_group']

                for groupstr in json_groups:
                    group = json.loads(groupstr)
                    groups.append(group)

                    log.debug('group order ID: %s, City Name:%s, Nick Name: %s, Expire Time: %s, User ID: %s', group['group_order_id'], group['city_name'], group['nickname'], group['expire_time'], group['uid'])
                    log.debug('------------------------------------------')

                return {
                    'total': int(data['total']),
                    'server_time': data['server_time'],
                    'groups': groups
                }
        return None

    def getGoodsDetails(self, goodsId):
        """
        Get goods details information such as
        name, skus, prices...
        from request API
        :param goodsId:
        :return:
        {
            goods_id: goods_id,
            goods_name: goods_name,
            goods_sn: goods_sn,
            mall_id: mall_id
            mall_name: mall_name
            mall_sales: mall_sales
            sku: [{skuinfo1}, {skuinfo2}]
        }
        """

        r = requests.get(constants.PDD_GOODS_DETAILS_API.format(goods_id=goodsId))
        # print r.text
        data = json.loads(r.text, encoding='UTF-8')
        mall_id = data['mall_id']
        mall_info = self.getMallInfo(mall_id)
        log.debug('goods name: %s, goods_sn: %s', data['goods_name'],data['goods_sn'])

        json_skus = data['sku']

        ret =  {
            'goods_id': goodsId,
            'goods_name': data['goods_name'],
            'goods_sn': data['goods_sn'],
            'mall_id': mall_id,
            'mall_name': mall_info['mall_name'],
            'mall_sales': mall_info['mall_sales'],
            'sku': json_skus
        }

        return ret

    def getLowestPriceSku(self, sku_array):
        """
        Get the lowest price sku info (spec text)
        :param sku_array:
        :return: special texts ['深色', 'M']
        """

        # 选出 在售库存大于 0的
        sku_array = [item for item in sku_array if int(item['quantity']) > 0]

        sorted_sku_array = sorted(sku_array, key=lambda x: x['group_price'])
        if sorted_sku_array:
            sku = sorted_sku_array.__getitem__(0)
            specs = sku['specs']
            select_texts = []
            for spec in specs:
                select_texts.append(spec['spec_value'])

            return select_texts

        return None

    def confirmCheckoutOrder(self, specs_text):
        # 选择 颜色，型号 ...
        self.selectSpecs(specs_text)

        # submit order
        self.submitOrder()

        # checkout order
        self.checkoutOrder()

    def groupBuy(self, specs):
        """
        click '一键拼单' and select the specs
        :param specs:
        :return:
        """
        log.debug('.......Group Buy......')
        time.sleep(5)
        script = "document.querySelector('.goods-group-btn').click();"
        self.driver.execute_script(script)
        time.sleep(2)
        self.confirmCheckoutOrder(specs)

    def joinGroup(self, groupInfo, specs):
        """
        If groups exists, then join the most recently expire time group
        :param groups:
        :return:
        """
        log.debug(".......Join Group Buy .......")
        groups = groupInfo['groups']
        server_time = int(groupInfo['server_time'])
        refer_page_id = self.getPageID()

        # sort the groups order by expire_time ASC
        sorted_groups = sorted(groups, key=lambda x: x['expire_time'])
        if sorted_groups and len(sorted_groups) > 0:
            group = sorted_groups.__getitem__(0)
            group_order_id = group['group_order_id']
            group_expire_time = int(group['expire_time'])
            if group_order_id:
                log.debug('参团购买, Group Order ID: %s, expire time: %s' , group_order_id, convertToTime((group_expire_time - server_time)))
                self.driver.get(
                    constants.PDD_JOIN_GROUP_URL.format(group_order_id=group_order_id, refer_page_id=refer_page_id,
                                                        refer_page_sn='10014'))
                time.sleep(2)
                self.driver.execute_script("document.querySelector('.group-detail-buy-btn').click();")
                time.sleep(2)
                self.confirmCheckoutOrder(specs)


    def selectSpecs(self, specs_texts):
        """
        Choose sku
        :param specs_text: array (i.e: ['长袖：006深咖啡+PAR黑色', 'M'] or ['纯色白长袖+纯色白长袖', 'XL'])
        :return:
        """

        selectScript = """
            function selectOne(text){
                var specs = document.body.querySelectorAll('.sku-spec-value');
                for (var i = 0;i<specs.length;i++){
                  var spec = specs[i];
                  if(spec.innerText.indexOf(text.replace(/ +(?= )/g,''))>-1 && spec.className.indexOf('sku-spec-value-selected') < 0){
                    spec.click()
                  }
                }
            }
            selectOne('%s');
        """

        for text in specs_texts:
            print 'Select text:', text
            log.info('Select text: %s', text)
            self.driver.execute_script(selectScript % text)

    def checkAddressIsExists(self, address):
        """
        Check the address is exists
        :param address:
        :return: True or False
        """
        check_script="""
            function checkAddressIsExists(name, mobile, province, city, dis, address){
                var isExists = false;
                var addrArray = document.querySelectorAll('.address-item-use');
                for (var i =0;i<addrArray.length;i++){
                    var addr = addrArray[i];
                    if(addr){
                        var addrText = addr.innerText;
                        if(addrText.indexOf(name) > -1 && addrText.indexOf(mobile) > -1 
                        && addrText.indexOf(province) > -1 && addrText.indexOf(city) > -1
                        && addrText.indexOf(dis) > -1 && addrText.indexOf(address) > -1){
                            isExists = true;
                            addr.click();
                            break;
                        }
            
                    }
                }
                return isExists;
            }
        """
        call_script="return checkAddressIsExists({name}, {mobile}, {province}, {city}, {district}, {address});"

        exe_script = check_script + call_script.format(**address)

        is_exists = self.driver.execute_script(exe_script) or False
        print "address is exists: " , is_exists

        return is_exists

    def addAddress(self, address):
        """
        Create or Update address
        :param address: json object
                {
                    name: receive name,
                    mobile: mobile phone,
                    province: province,
                    city: city,
                    district: district,
                    address: detail address
                }
        :return:
        """

        is_create_new = True
        exist_address_count = 0
        exist_address_count_script="return document.querySelectorAll('.address-item-use').length;"

        if 'addresses' in self.driver.current_url:
            # in address page
            exist_address_count = self.driver.execute_script(exist_address_count_script)
            if exist_address_count > 0:
                # check exists address, if found update the address
                if self.checkAddressIsExists(address):
                    time.sleep(2)
                    is_create_new = False
                    print 'Update Address'
            else:
                script = "document.querySelector('.add-btn span').click();"
                self.driver.execute_script(script)
                WebDriverWait(self.driver, 10).until(EC.presence_of_element_located((By.ID, 'm-addr-mask')))

        # receive name
        self.enter_element_value('name', address['name'])
        self.enter_element_value('mobile', address['mobile'])
        self.enter_element_value('address', address['address'])
        # base script
        # enter_value_by_id_script = "document.getElementById('{element_id}').value='{value}'"
        # driver.execute_script(enter_value_by_id_script.format(element_id='name', value=receiveName))
        # driver.execute_script(enter_value_by_id_script.format(element_id='mobile', value=mobile))
        # driver.execute_script(enter_value_by_id_script.format(element_id='address', value=address))

        # choose province, city and direct
        self.driver.execute_script("document.querySelector('.m-addr-region').click();")
        time.sleep(3)

        base_choose_script = """
        function selectZone(elementId, chooseText){
            var childrenEl = document.getElementById(''+elementId).children;
            for (var i = 0;i<childrenEl.length;i++){
                var ele = childrenEl[i];
                if(ele && ele.innerText == chooseText){
                    ele.click();
                    return ele.innerText;
                }
    
            }
        }
        return selectZone('%s','%s')
        """

        # choose province
        selected_province = self.driver.execute_script(base_choose_script % ('region-selector-list-1', address['province']))
        print 'selected_province:', selected_province
        time.sleep(5)
        # choose city
        selected_city = self.driver.execute_script(base_choose_script % ('region-selector-list-2', address['city']))
        print 'selected_city:', selected_city
        time.sleep(5)
        # choose district region
        selected_district = self.driver.execute_script(base_choose_script % ('region-selector-list-3', address['district']))
        print 'selected_district:', selected_district
        time.sleep(5)

        # if '保存' button is active, then click it
        save_address_script = "if(document.querySelector('.m-addr-save').className.indexOf('active')>-1){document.querySelector('.m-addr-save').click();}"
        self.driver.execute_script(save_address_script)

        # check is save successfully or not
        now_address_count = self.driver.execute_script(exist_address_count_script)
        print "Save Success: ", now_address_count > exist_address_count if is_create_new else now_address_count == exist_address_count


    def submitOrder(self):
        """
        Click '确定' button to submit the order
        :return:
        """
        script = "document.querySelector('.sku-selector-bottom').click();"
        self.driver.execute_script(script)
        log.info('Order was submitted....')


    def checkoutOrder(self, use_self_address=False):
        """
        Check out the order
        :return: the order id
        """
        if 'order_checkout' not in self.driver.current_url:
            # do something here
            pass


        time.sleep(2)
        WebDriverWait(self.driver, 10).until(EC.presence_of_element_located((By.CSS_SELECTOR, '.oc-pay-final')))

        exist_addr = self.get_address_on_front_end()
        print 'exist_addr:', exist_addr
        log.debug('exist_addr:%s', exist_addr)
        if 'NO_ADDRESS' in exist_addr:
            address = pddsysconfig.get_address_and_save_the_address_index()
            self.driver.execute_script("document.querySelector('.oc-add-address').click();")
            time.sleep(2)
            self.addAddress(address)

        else:
            if use_self_address:
                address = pddsysconfig.get_self_address()
            else:
                address = pddsysconfig.conv_address_from_front_end(exist_addr)
                print 'address:', address
                log.debug('Receive Address: %s', address)
            full_equals = True
            for key in address:
                if address[key] not in exist_addr:
                    full_equals = False
                    break
            if not full_equals:
                # click the address info to address page to add or update the address
                self.driver.execute_script("document.querySelector('.oc-address-info').click();")
                time.sleep(2)
                self.addAddress(address)


        #click the "立即支付" to chekout the order
        self.payment_order()

    def get_address_on_front_end(self):
        """
        Ge the address info from front-end.
        :return: front-end address info
        """

        get_address_scripts="""
            function getAddress(){
                var addr = document.body.querySelector('.oc-address-info'); 
                if(addr){ 
                    return addr.innerText.replace('&nbsp;',' ').trim().split(/\s+/)
                } else {
                    return 'NO_ADDRESS';
                }
            }
            return getAddress();
        """
        return self.driver.execute_script(get_address_scripts)

    def choose_payment_type(self, pay_type_text, class_name):
        """
        Choose the payment type (支付宝 or 微信代付)
        :param class_name: front-end class name
        :param pay_type_text: payment type text
        :return: pay type text, or NO_Pay_Type|...
        """

        pay_type_text = pddsysconfig.decode_chinese(pay_type_text)
        log.info('choose_payment_type.pay_type_text:%s', pay_type_text)
        print ('choose_payment_type.pay_type_text:%s', pay_type_text)

        choose_payment_type_script = """
            function choosePayType(payTypeText, class_name){
                var payTypes = document.body.querySelectorAll('.'+class_name);
                for (var i = 0; i< payTypes.length; i++){
                    var payType = payTypes[i];
                    if (payType && payType.innerText && payType.innerText.indexOf(payTypeText) > -1){
                        text = payType.innerText
                        payType.click();
                        return text;
                    }
                }
                return 'NO_Pay_Type|'+payTypeText
            }   
            return choosePayType('%s', '%s'); 
        """

        return self.driver.execute_script(choose_payment_type_script % (pay_type_text, class_name))

    def save_order_to_file(self, order_sn, payment_url):
        """

        :param order_sn:
        :return:

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
        """

        # go to index page
        self.driver.execute_script("Navigation.home()")
        time.sleep(3)
        # go to my orders page
        self.driver.execute_script("Navigation.ordersPage()")
        WebDriverWait(self.driver, 20).until(EC.title_contains('我的订单'))
        rawData = self.driver.execute_script("return window.rawData")
        orderInfo = None
        if rawData:
            orders = rawData['orders']
            for order in orders:
                if order_sn == order['order_sn']:
                    orderInfo = {
                        'order_sn': order_sn,
                        'order_amount': order['order_amount'],
                        'order_status': order['order_status_desc'],
                        'mall_id': order['mall']['id'],
                        'mall_name': order['mall']['mall_name']
                    }
                    orderInfo['order_goods'] = []
                    for goods in order['order_goods']:
                        orderInfo['order_goods'].append({'goods_id': goods['goods_id'], 'goods_name': goods['goods_name'], 'sku_id': goods['sku_id'], 'spec': goods['spec']})

                    break
        log.info("Save order info to file")
        pddsysconfig.save_order(self.username, self.password, order_sn, self.keyword,
                                self.address, orderInfo, self.order_ip_info, payment_url)
        log.info('Order info was saved.')

    def payment_order(self):
        """
        select the payment type to pay this order.
        :return: order_id
        """
        current_url=self.driver.current_url

        if 'order_checkout' in current_url:
            log.info("Current in order checkout page, will choose the payment type to checkout the order.")
            selected_text = self.choose_payment_type(self.payment_type, 'oc-payment-item')
            log.debug('Selected payment type: %s', selected_text)

            time.sleep(2)

            receive_address_text = self.get_address_on_front_end()
            log.debug('Order receive address is: %s', receive_address_text)
            # set the class address info
            self.address = pddsysconfig.conv_address_from_front_end(receive_address_text)
            log.debug('address json object: %s', self.address)

            # click 'payment button'
            self.driver.execute_script("document.body.querySelector('.oc-pay-btn').click();")
            time.sleep(2)
            if self.payment_type == pddsysconfig.pay_type_weixin_text:
                WebDriverWait(self.driver, 20).until(EC.title_contains('好友代付'))
                current_url = self.driver.current_url
                log.debug('找微信好友代付 URL: [%s]', current_url)
            elif self.payment_type == pddsysconfig.pay_type_alipay_text:
                current_url = self.driver.current_url
                WebDriverWait(self.driver, 20).until(EC.title_contains('支付宝'))
                log.debug('支付宝 支付 URL: [%s]', current_url)

            # get order_sn from the url
            self.order_sn = order_sn = get_order_sn(current_url)
            # write order to file
            self.save_order_to_file(order_sn, current_url)

    def enter_element_value(self, element_id, value):
        """
        Execute the java scrip to set element value by element_id in front-end.
        :param element_id:
        :param value:
        :return:
        """
        enter_value_by_id_script = "document.getElementById('{element_id}').value='{value}'"
        self.driver.execute_script(enter_value_by_id_script.format(element_id=element_id, value=value))

    def buyGoods(self,goods_id, buy_type):
        """
        Buy goods by @buy_type
        :param goods_id:
        :param buy_type:
        :return: orderID
        """
        sku_info = self.getGoodsDetails(goods_id)
        groupInfo = self.getGroups(goods_id)
        lowest_sku_texts = self.getLowestPriceSku(sku_info['sku'])

        # 开团 还是参团
        buy_type = int(buy_type)
        log.debug('buy type: %s', buy_type)
        if buy_type == pddsysconfig.buy_type_by_open_group_only:
            # 开团购买
            if sku_info:
                self.groupBuy(lowest_sku_texts)
            else:
                log.info('没有找到相应的商品ID: %s',  goods_id)
        elif buy_type == pddsysconfig.buy_type_by_join_group_only:
            # 参团购买
            if groupInfo and groupInfo['total'] > 0:
                self.joinGroup(groupInfo, lowest_sku_texts)
            else:
                log.info('商品ID:%s, 已无团可参了', goods_id)

        elif buy_type == pddsysconfig.buy_type_by_open_and_join_group:
            # 开团，参团购买
            # 如果该商品已有团，先将现有的团全部拼完之后再开新团
            if groupInfo and groupInfo['total'] > 0:
                log.info('Will join the group')
                self.joinGroup(groupInfo, lowest_sku_texts)
            else:
                log.info('There are no group to join, will open a new group')
                self.groupBuy(lowest_sku_texts)

    def wait_until_element_located(self, locator):
        try:
            WebDriverWait(self.driver, 20).until(EC.presence_of_element_located(locator))
            return True
        except:
            self.driver.execute_script("window.location.reload()")
            return self.wait_until_element_located(locator)

    def search(self, keyword, goods_Id, buy_type):
        """
        Search by @keyword
        :param keyword: 搜索关键词
        :param goodsId: 商品ID (not sku)
        :param buy_type: @see pddsysconfig.buy_types
        :return:
        """

        if 'mobile.yangkeduo.com' not in self.driver.current_url:
            self.driver.get(constants.PDD_INDEX_URL)

        search_icon_id = 'footer_class'
        WebDriverWait(self.driver, 10).until(EC.presence_of_element_located((By.ID, search_icon_id)))

        self.driver.execute_script("if(document.getElementById('web-promotion-activity-popup')){document.getElementById('web-promotion-activity-popup').children[0].children[0].click()}")

        # search page
        search_icon_field = self.driver.find_element_by_id(search_icon_id)
        search_icon_field.click()
        time.sleep(3)
        page_id = self.getPageID()

        # direct to search result page (search by keyword)
        self.driver.get(constants.PDD_SEARCH_URL.format(key_word=keyword, refer_page_id=page_id))

        # wait the search result shows
        self.wait_until_element_located((By.ID, 'search-result-list'))
        time.sleep(3)

        page_id = self.getPageID()

        # direct to goods details page
        self.driver.get(constants.PDD_GOODS_PAGE.format(goods_id=goods_Id, refer_page_id=page_id))
        time.sleep(3)
        page_id = self.getPageID()
        # todo is browse this goods some seconds....

        # buy
        self.buyGoods(goods_Id, buy_type)



    def run_order(self, app_configuration, order):
        log.info('>>>>>>>>>>>> Start Run Order >>>>>>>>>>>>')
        if order:
            goods_id = order['goods_id']
            keyword = order['keyword']
            order_count = order['order_count']
            buy_type = order['buy_type']
            username = order['username']
            # ADSL change IP
            if int(app_configuration.adsl_dial_on) != 0:
                ip_info = pddsysconfig.change_ip(app_configuration.adsl_username, app_configuration.adsl_password)
                log.info('goods_id: %s, keyword: %s, buy_type: %s, IP: %s', goods_id, keyword, buy_type, ip_info)
                self.order_ip_info = ip_info

            self.keyword = keyword
            self.buy_type=buy_type

            self.initWebDriver(app_configuration.webdriver_name)
            self.login(username)

            self.search(keyword, goods_id, buy_type)
        log.info('>>>>>>>>>>>> END Run Order >>>>>>>>>>>>')


def run(queue):
    log.info('==============================================================================')
    log.info('...........Run..............Time: %s', time.strftime(pddsysconfig.date_time_formatter))
    log.info('==============================================================================')
    # read user configs
    pddsysconfig.app_configuration = app_configuration = pddsysconfig.load_app_configurations()
    log.info('Loaded the application configurations.')

    # init the sms info
    pddsysconfig.smsUserInfo = UserInfo(pddsysconfig.decrypt(app_configuration.sms_username), pddsysconfig.decrypt(app_configuration.sms_password), pddsysconfig.sms_author_id, pddsysconfig.sms_pid)
    log.info("Initialize the SMS plat form.")

    time.sleep(5)
    index = 0
    while True:
        if not queue.empty():
            order = queue.get(timeout=300)
            if order:
                goods_id = order['goods_id']
                if pddsysconfig.finished_orders.has_key(str(goods_id)):
                    goods_order_info=pddsysconfig.finished_orders[str(goods_id)]
                    if goods_order_info.has_key('status') and goods_order_info['status'] == constants.order_finished_status_text:
                        log.info('The goods_id: %s total order count is: %s, and has finished order count: %s, it is finished orders, no need to order it again.',
                                 goods_id, goods_order_info['total'], goods_order_info['total_finished'])
                        continue

                    elif goods_order_info.has_key('last_order_time'):
                        last_order_time = goods_order_info['last_order_time']
                        deadline = int(time.time()) - last_order_time

                        if deadline < int(app_configuration.delay_seconds_for_next_by_goods_id):
                            log.debug('The goods_id: %s now() - last_order_time < %s, will continue this goods after delay times',
                                      goods_id, app_configuration.delay_seconds_for_next_by_goods_id)
                            queue.put(order)
                            continue

                index +=1
                log.info('Processing the %s order>>>>>>>', index)
                try:
                    PDDOrder().run_order(app_configuration, order)
                except Exception as e:
                    print e.message
                    log.error(e.message)
                    log.error(e)


def run2():
    log.info('==============================================================================')
    log.info('...........Run..............Time: %s', time.strftime(pddsysconfig.date_time_formatter))
    log.info('==============================================================================')
    # read user configs
    pddsysconfig.app_configuration = app_configuration = pddsysconfig.load_app_configurations()
    log.info('Loaded the application configurations.')

    # init the sms info
    pddsysconfig.smsUserInfo = UserInfo(pddsysconfig.decrypt(app_configuration.sms_username), pddsysconfig.decrypt(app_configuration.sms_password), pddsysconfig.sms_author_id, pddsysconfig.sms_pid)
    log.info("Initialize the SMS plat form.")

    time.sleep(5)
    index = 0

    for order in pddsysconfig.read_today_merchant_orders():
        if order:
            index += 1
            log.info('Processing the %s order>>>>>>>', index)
            try:
                PDDOrder().run_order(app_configuration, order)
            except Exception as e:
                print e
                log.error(e.message)



if __name__ == "__main__":

    pddLogin = PDDLogin()
    pddLogin.initWebDriver('chromedriver')
    pddLogin.login("13698183542")
    time.sleep(500)
    #pass
    # run2()
    # #
    # time.sleep(500)
    # driver = initWebDriver()
    # driver.get(constants.PDD_INDEX_URL)
    #
    # driver.delete_all_cookies()
    #
    # set_cookie_script="document.cookie = '{name}={value}; path={path}; domain={domain}; expires={expires}';\n"
    # #
    # cookies = []
    #
    # cookies.append({'name': 'PDDAccessToken', 'value': '4f4b748a72251b4033872af621a478ccde803d0f', 'path': '/', 'domain': '.yangkeduo.com', 'expires': None})
    # cookies.append({'name': 'api_uid', 'value': 'rBQRnVn6ibAqi19eDQwXAg==', 'path': '/', 'domain': '.yangkeduo.com', 'expires': None})
    # cookies.append({'name': 'pdd_user_id', 'value': '8672256350', 'path': '/', 'domain': '.yangkeduo.com', 'expires': None})
    # scripts = ''
    # for cookie in cookies:
    #     scripts = scripts + set_cookie_script.format(**cookie)
    #
    # print scripts
    #
    # driver.execute_script(scripts)
    # driver.get(constants.PDD_INDEX_URL)
    #
    #
    #
    # time.sleep(1)
    # # agent = driver.execute_script("return navigator.userAgent")
    # # print agent
    #
    # # driver.get('http://mobile.yangkeduo.com/addresses.html')
    # # time.sleep(5)
    # # addAddress('小李', '13567890009', '创新大厦', '四川', '成都', '高新区')
    # search('营养保健', 15443205, pddsysconfig.buy_type_by_join_group_only)
