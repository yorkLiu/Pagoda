# -*- coding: UTF-8 -*-
import os
import sys, getopt, getpass
from jira import JIRA
from jira import JIRAError
import logging
from datetime import date, timedelta, datetime
import hashlib
import difflib
import re
import requests

proxies = {
    
}

cmc_jira_server_url = 'https://jira.sss.com'


log = logging.getLogger('JIRA')
log.setLevel(logging.INFO)
fmt = logging.Formatter('%(levelname)s:%(name)s:%(message)s')
h = logging.StreamHandler()
h.setFormatter(fmt)
log.addHandler(h)

# Connect to jira server with username and password
def connect_jira(jira_server, jira_user, jira_password, use_proxy=False):
    log.info("Connecting: %s with author: %s", jira_server,jira_user)
    try:
        jira_option = {'server': jira_server}
        if use_proxy:
            log.info("Visit %s using Proxy: %s", jira_server, proxies)
            jira = JIRA(jira_option, basic_auth={jira_user, jira_password}, proxies=proxies)
        else:
            jira = JIRA(jira_option, basic_auth={jira_user, jira_password})
        log.info("Connected server: %s successfully!", jira_server)
        return jira
    except JIRAError, e:
        log.error("Failed connect JIRA server (%s)", jira_server)
        log.error(e.message)
        return None


def getEpicIssues(key):
    url = 'https://jira.katabat.com/rest/api/2/search?jql=cf[11264]={key}&fields=summary'.format(key=key)
    r = requests.get(url,auth=(cmc_username, cmc_pwd))
    jsonData = r.json()
    if jsonData and 'issues' in jsonData:
        for item in jsonData['issues']:
            print item['key']


if __name__ == "__main__":
    # cmc_username=''
    # cmc_pwd = ''
    # cmc_jira_server = connect_jira(cmc_jira_server_url, cmc_username, cmc_pwd, True)
    #
    # issue = cmc_jira_server.issue('CA-6891')
    #
    # print issue.fields.status.name



    # for link in issue.fields.issuelinks:
    #     if hasattr(link, "outwardIssue"):
    #         outwardIssue = link.outwardIssue
    #         print("\tOutward: " + outwardIssue.key)
    #     if hasattr(link, "inwardIssue"):
    #         print link
    #         inwardIssue = link.inwardIssue
    #         print("\tInward: " + inwardIssue.key)
    #
    # getEpicIssues('CA-6469')

    exclusives = ['PM', 'Pending']

    print [x in ('Pending Dev Funding') for x in exclusives]

    # print 'Pending' in ('Pending Dev Funding')



