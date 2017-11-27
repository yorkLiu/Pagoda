# -*- coding: UTF-8 -*-

# PDD-SD User configurations

class ApplicationConfiguration:
    # User Configurations
    # read the configuration values from '.config.txt' file
    # Note: this class's properties must equals with '.config.txt' file keys.

    def __init__(self):

        # Using driver name
        # default is 'phantomjs'
        # webdriver_names ('chromedriver', 'phantomjs')
        self.webdriver_name='phantomjs'

        # 收货地址前加 符号 标记，作为暗号来识别是自己下的单
        # 如果没有配置，则收货地址不会做任何的处理
        # 比如 '*', 'A1'...
        self.mark_receive_address_symbol=None

        # 对于每一个  商品ID (goodsID) 间隔时间 (秒)
        self.delay_seconds_for_next_by_goods_id=None

        # Sms platform account information
        self.sms_username=None
        self.sms_password=None

        # address used index
        self.address_used_index=None

        # ADSL account information
        self.adsl_dial_on=None
        self.adsl_username=None
        self.adsl_password=None

        # Email
        self.email_login_username=None
        self.email_login_password=None
        self.email_sent_to=None
        self.email_cc_to=None
        self.email_bcc_to=None

        # Payment
        self.a_pay_username=None
        self.a_pay_password=None

    def __setattr__(self, key, value):
        self.__dict__[key] = value

    def set_properties(self, property_map):
        properties = self.__dict__
        for key in property_map:
            value = property_map[key]
            if properties.has_key(key):
                self.__setattr__(key, value)
            else:
                print 'No attribute %s to store the value: %s' % (key, value)
