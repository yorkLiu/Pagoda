#!/usr/bin/env python
# -*- coding: UTF-8 -*-
import requests
from bs4 import BeautifulSoup
import json

url = 'https://passport.hellobi.com/sso/login'
username='kaggle'
password='kaggle2hellobi#'
r = requests.get('https://passport.hellobi.com/sso/login')
cookies= []
for cookie in r.cookies:
    # print '%s=%s' % (cookie.name, cookie.value)
    cookies.append('%s=%s' % (cookie.name, cookie.value))


soup = BeautifulSoup(r.text, 'lxml')
token_field = soup.find(name='input', attrs={'name': '_token'})
_token=''
if token_field:
    _token = token_field.get('value')

return_url ='https://edu.hellobi.com/course/250/play/lesson/4657'
formdata = {'username': username, 'password': password, 'return_url': return_url, '_token': _token}
headers = {
    'Host': 'passport.hellobi.com',
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:52.0) Gecko/20100101 Firefox/52.0',
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': "en-US,en;q=0.5",
    'Accept-Encoding':"gzip, deflate, br",
    'Referer': 'https://passport.hellobi.com/sso/login',
    'Connection': 'keep-alive',
    'Upgrade-Insecure-Requests': "1",
    'Content-Type':'application/x-www-form-urlencoded',
    'Content-Length': "93",
    # 'Content-Type':'application/json',
    'Cookie': '%s' % ';'.join(cookies)


}
# print json.dumps(formdata)
session = requests.Session()
r = session.post("https://passport.hellobi.com/sso/login", data=json.dumps(formdata), headers=headers)
print r.status_code
