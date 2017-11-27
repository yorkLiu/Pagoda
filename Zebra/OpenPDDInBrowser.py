#!/usr/bin/env python
# -*- coding: UTF-8 -*-
# coding:utf-8
from __future__ import unicode_literals
import sys

from PDD import PDDLogin as PDDLogin


if __name__ == '__main__':
    params =sys.argv[1:]

    if len(params) > 0:
        for phone in params:
            if phone:
                phone = phone.strip()

                pddLogin = PDDLogin()
                pddLogin.initWebDriver('chromedriver')
                pddLogin.login(phone)
    else:
        help_text =  """
            用浏览器打开拼多多,并登陆相应的账号
            使用方法:
               OpenPDDInBrowser.exe 手机号
            如果想一次打开多个账号, 账号之间请用 空格 隔开
               OpenPDDInBrowser.exe 手机号1 手机号2 手机号3 
        """.decode('UTF-8')
        print help_text