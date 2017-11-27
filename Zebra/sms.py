# -*- coding: UTF-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('UTF-8')

import logging
import requests
import re
import time

LOGGER = logging.getLogger(__name__)


class UserInfo:
    def __init__(self, uid, pwd, author_id=None, pid=None):
        self.uid = uid
        self.pwd = pwd
        self.author_id=author_id
        self.token=None
        self.pid = pid

    def get_token(self):
        return self.token

    def set_token(self, token):
        self.token = token

    def get_pid(self):
        return self.pid

    def set_pid(self, pid):
        self.pid = pid

    def is_login(self):
        if not self.uid or not self.pwd or not self.token:
            return False
        else:
            return True

class NotFoundMobilePhoneException(Exception):
    def __init__(self, msg=None, stacktrace=None):
        self.message = msg
        self.stacktrace = stacktrace

    def __str__(self):
        exception_msg = "Message: %s\n" % self.message
        if self.stacktrace is not None:
            stacktrace = "\n".join(self.stacktrace)
            exception_msg += "Stacktrace:\n%s" % stacktrace
        return exception_msg

class SMSMSAPIServer:

    baseUrl = "http://api.eobzz.com/httpApi.do?action="
    loginUrl = baseUrl + "loginIn&"
    getUserInfos = baseUrl + "getUserInfos&"
    getMobilenum = baseUrl + "getMobilenum&"
    addIgnoreList = baseUrl + "addIgnoreList&"
    getVcodeAndReleaseMobile = baseUrl + "getVcodeAndReleaseMobile&"
    getVcodeAndHoldMobilenum = baseUrl + "getVcodeAndHoldMobilenum&"

    def __init__(self):
        pass

    @staticmethod
    def get_html(url, params=None):
        urlNameString = url
        if params:
            if url.find("?") > -1:
                if "".rfind('?') > -1:
                    urlNameString += params
                else:
                    urlNameString += "&" + params
            else:
                urlNameString += "?" + params

        r = requests.get(urlNameString)
        return r.content

    @staticmethod
    def login(self, userInfo):
        if userInfo.uid and userInfo.pwd:
            try:
                return SMSMSAPIServer.get_html(SMSMSAPIServer.loginUrl, 'uid=%s&pwd=%s' % (userInfo.uid, userInfo.pwd))
            except Exception as e:
                return ""

        return ""

    @staticmethod
    def login(username, password):
        if username and password:
            try:
                result = SMSMSAPIServer.get_html(SMSMSAPIServer.loginUrl, 'uid=%s&pwd=%s' % (username, password))
                if result.lower().startswith(username):
                    strings = result.split("|")
                    return strings[1]
            except Exception as e:
                LOGGER.error(e.message)
                return ""

        return ""

    @staticmethod
    def get_user_info(username, token):
        try:
            return SMSMSAPIServer.get_html(SMSMSAPIServer.getUserInfos, "uid=%s&token=%s" %(username, token))
        except Exception as e:
            LOGGER.error(e.message)
            return ""

    @staticmethod
    def get_mobile_phone_number(pid, uid, token, size, mobile=None):
        try:
            size = size if size else 1
            para_tpl = 'pid=%s&uid=%s&token=%s&mobile=%s&size=%s'
            para = para_tpl % (pid, uid, token, '', size)
            if mobile:
                para = para_tpl % (pid, uid, token, mobile, size)

            url = SMSMSAPIServer.getMobilenum + para
            return SMSMSAPIServer.get_html(url)
        except Exception as e:
            return ""

    @staticmethod
    def get_mobile_phone_numbers(pid, uid, token, size, mobile=None):
        mobiles = SMSMSAPIServer.get_mobile_phone_number(pid, uid, token, size, mobile)

        if mobiles.__contains__("no_data"):
            LOGGER.info('系统暂时没有可用号码了')
        elif mobiles.__contains__("parameter_error"):
            LOGGER.info( '传入参数错误')
        elif mobiles.__contains__("not_login"):
            LOGGER.info('没有登录,在没有登录下去访问需要登录的资源，忘记传入uid,token\n')
        elif mobiles.__contains__("account_is_locked"):
            LOGGER.info('账号被锁定')
        elif mobiles.__contains__("account_is_stoped"):
            LOGGER.info('账号被停用')
        elif mobiles.__contains__("account_is_question_locked"):
            LOGGER.info('账号已关闭')
        elif mobiles.__contains__("account_is_ip_stoped"):
            LOGGER.info('账号ip锁定')
        elif mobiles.__contains__("message"):
            LOGGER.info(mobiles)
        elif mobiles.__contains__("not_found_project"):
            LOGGER.info('没有找到项目,项目ID不正确')
            print '没有找到项目,项目ID不正确'
        elif mobiles.__contains__("max_count_disable"):
            LOGGER.info('已经达到了当前等级可以获取手机号的最大数量，请先处理完您手上的号码再获取新的号码（处理方式：能用的号码就获取验证码，不能用的号码就加黑）')
            print '已经达到了当前等级可以获取手机号的最大数量，请先处理完您手上的号码再获取新的号码（处理方式：能用的号码就获取验证码，不能用的号码就加黑）'
        elif mobiles.__contains__("unknow_error"):
            LOGGER.info( '未知错误,再次请求就会正确返回')
        else:
            mobile_phone_numbers = mobiles.split("|")[0].split(";")
            return mobile_phone_numbers

        return None


    @staticmethod
    def ignore_mobile_phone(mobile, uid, token, pid):
        try:
            para = 'uid=%s&token=%s&mobiles=%s&pid=%s' % ( uid, token, mobile, pid)
            url = SMSMSAPIServer.addIgnoreList + para
            return SMSMSAPIServer.get_html(url)
        except Exception as e:
            LOGGER.info(e.message)
        return ''

    @staticmethod
    def get_vcode_and_release_mobile(uid, token, mobile_phone, author_uid):
        try:
            para = 'uid=%s&token=%s&mobile=%s' % (uid, token, mobile_phone)
            if author_uid:
                para += '&author_uid='+author_uid

            url = SMSMSAPIServer.getVcodeAndReleaseMobile + para
            return SMSMSAPIServer.get_html(url)
        except Exception as e:
            LOGGER.info(e.message)

        return ''

    @staticmethod
    def get_vcode_from_sms(sms_content):
        sms_content = str(sms_content).decode('utf-8')
        patt = '[0-9]{4,}'
        m = re.search(patt, sms_content)
        if m:
            group = m.group()
            if group and len(group) >=4:
                return group

        return ''

class SmsUtils:

    def __init__(self):
        pass

    @staticmethod
    def login_sms(userInfo):
        if not userInfo.is_login():
            token = SMSMSAPIServer.login(userInfo.uid, userInfo.pwd)
            if token:
                userInfo.set_token(token)
            else:
                LOGGER.error('Sms Platform login error')
                return False

        return True

    @staticmethod
    def print_user_info(userInfo):
        if userInfo.is_login:
            info = SMSMSAPIServer.get_user_info(userInfo.uid, userInfo.get_token())
            information = info.split(";")
            score = information[1]
            balance = int(information[2])
            max_get_count=information[3]
            LOGGER.debug('****************************INFO****************************')
            LOGGER.debug(('积分: %s, 余额: %s, 可同时获取号码数: %s' % (score, (balance / 100.00), max_get_count)).decode('utf-8'))
            LOGGER.debug( '************************************************************')
        else:
            LOGGER.error("Please login first")


    @staticmethod
    def login_and_print_info(userInfo):
        SmsUtils.login_sms(userInfo)
        SmsUtils.print_user_info(userInfo)

    @staticmethod
    def get_phone_numbers(userInfo, phone_number=None):
        SmsUtils.login_and_print_info(userInfo)

        return SMSMSAPIServer.get_mobile_phone_numbers(userInfo.get_pid(), userInfo.uid, userInfo.token, 5, phone_number)

    @staticmethod
    def get_vcode_from_phone_number(userInfo, phone_number):
        return SmsUtils.get_vcode_from_phone_number_with_times(userInfo, phone_number, 0)

    @staticmethod
    def get_vcode_from_phone_number_with_times(userInfo, phone_number, index):
        vcode = None
        retMsg = None
        message = SMSMSAPIServer.get_vcode_and_release_mobile(userInfo.uid, userInfo.get_token(), phone_number, userInfo.author_id).strip()
        message = str(message).decode('utf-8')
        if message.startswith(phone_number):
            LOGGER.debug('Sms was received, message: %s', message)
            retMsg = message.split("|")[1]
            LOGGER.debug( 'retMsg:%s', retMsg)
        elif 'not_receive' in message or 'try again later' in message:
            if index <= 30:
                LOGGER.debug('SMS receive error, will wait 2 seconds and try again. - %s times' % index)
                if 'try again later' in message:
                    time.sleep(5)
                else:
                    time.sleep(2)
                return SmsUtils.get_vcode_from_phone_number_with_times(userInfo, phone_number, (index+1))
        else:
            LOGGER.debug("The phone [%s] was not received message." % phone_number)
            SMSMSAPIServer.ignore_mobile_phone(phone_number, userInfo.uid, userInfo.get_token, userInfo.get_pid())

        if retMsg:
            vcode = SMSMSAPIServer.get_vcode_from_sms(retMsg)
            LOGGER.debug('vcode:%s', vcode)

        return vcode

    @staticmethod
    def ignore(userInfo, phone_numbers):
        if len(phone_numbers) > 0:
            SMSMSAPIServer.ignore_mobile_phone(','.join(phone_numbers), userInfo.uid, userInfo.get_token(), userInfo.get_pid())
