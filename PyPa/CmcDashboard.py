#!/usr/bin/env python
# -*- coding: UTF-8 -*- 
import base64
import datetime
import logging
import smtplib
import sys
import time
from email.mime.text import MIMEText

import os
import requests
from jira import JIRA

reload(sys)
sys.setdefaultencoding("utf8")

###################### tools #####################
# pip install requests
# pip install request[socks]
# pip install JIRA
# pip install logging
###################### tools #####################

current_milli_time = lambda: int(round(time.time() * 1000))
dateFormat='%Y-%m-%d'
today = datetime.datetime.now().strftime(dateFormat)


######################## Configuration [Start] ###############################
output_dir = "~/Downloads/JIRA/Dashboard"

### START config cmc jira username & password
cmc_jira_username='ozintel'
cmc_jira_password='0zPa$$123'
### END config cmc jira username & password


### START config Email Sender
email_server_key = 'key-063b98a4091398b81baa16959b20d660'
email_server_api_user = 'sandbox9597735f17034d268fa4a01a3e6292d0.mailgun.org'
email_username='yong.liu@ozstrategy.com'
email_password='dXUwMDAwMDA='
email_from='Dashboard Observer <dashboard_observer@ozstrategy.com>'
email_subject='OZStrategy Dashboard in CMC Side on %s' % today

# config the email address here
email_to_addres = 'yong.liu@ozstrategy.com'
email_to_tech_leads=email_to_addres.split(',')

use_proxy=os.environ.get('USE_PROXY')
proxy_ip = os.environ.get('PROXY_SERVER')

### END config Email Sender

# proxies = {
#             'http': 'socks5://192.168.168.3:1083',
#             'https': 'socks5://192.168.168.3:1083'
#           }

proxies = None
if use_proxy and str(use_proxy).upper() in ('YES', 'TRUE'):
    proxies = {
                'http': str(proxy_ip).strip(),
                'https': str(proxy_ip).strip()
              }
######################## Configuration [End] #################################

### START CMC Dashboard Config
dashboard_real_Url = "https://jira.katabat.com/secure/RapidBoard.jspa?rapidView=129"
dashboardUrl = "https://jira.katabat.com/rest/greenhopper/1.0/xboard/work/allData.json?rapidViewId=129&_=%s"
cmc_jira_url_prefix = 'https://jira.katabat.com/browse/%s'
### END  CMC Dashboard Config

### START OZ JIRA Config
oz_jira_server = 'http://192.168.168.21:8091'
oz_jira_username='builder'
oz_jira_password = "builder$123"

oz_jira_search_query = 'project = "CMC JIRA Tickets" AND summary ~ %s'
oz_jira_url_prefix = 'http://192.168.168.21:8091/browse/%s'
### END OZ JIRA Config




log = logging.getLogger('Dashboard')
log.setLevel(logging.INFO)
fmt = logging.Formatter('%(levelname)s:%(name)s:%(message)s')
h = logging.StreamHandler()
h.setFormatter(fmt)
log.addHandler(h)



def getDashboardContent():
    resultMap={}
    url = dashboardUrl % current_milli_time()
    log.info("Visit URL %s with proxy: %s", url, proxies)

    types = {}
    statuses = {}

    print cmc_jira_username, cmc_jira_password

    response = requests.get(url,  auth=(cmc_jira_username, cmc_jira_password), proxies=proxies)
    print response.status_code
    if response.status_code == requests.codes.ok:
        log.info("Data was returned.")
        jsonObj = response.json()
        issues = jsonObj['issuesData']['issues']

        entityData = jsonObj['entityData']
        # init the types (Story, Defect, Task, Epic ...)
        typeList = entityData['types']
        for key in typeList:
            types[key] = typeList[key]['typeName']

        # init the status
        statusList = entityData['statuses']
        for key in statusList:
            statuses[key] = statusList[key]['statusName']

        for issueObj in issues:
            assignee =  issueObj['assignee']
            print issueObj['key'], assignee
            if assignee not in ('ozdev', 'ozintel'):
                continue
            key = issueObj['key']
            print key, issueObj['hidden'], issueObj['assignee']
            summary = issueObj['summary']
            # type is 'defect', 'story'...
            # type = issueObj['typeName']
            type = types[issueObj['typeId']]
            # status is 'Open (Dev)', 'In Progress (Dev)' ...
            # status = issueObj['statusName']
            status = statuses[issueObj['statusId']]
            if not issueObj['hidden']:
                log.info("Process CMC Ticket [%s]", key)
                if not resultMap.has_key(type):
                    resultMap[type] = {'total': 0}
                if not resultMap[type].has_key(status):
                    resultMap[type][status] = []

                if resultMap[type][status] != None:
                    # increasement the type's total
                    resultMap[type]['total'] = resultMap[type]['total'] +1
                    resultMap[type][status].append({
                        'key': key,
                        'summary': summary,
                        'url': cmc_jira_url_prefix % key
                    })


        html_template = """\
            <html>
              <head>
                <style type="text/css">
              		body{margin: 20px;}
              		ol>li{line-height: 25px;}
              	</style>
              </head>
              <body>
                 <div style='font-weight: bold;'>The following data got from: <a href='%s'>%s</a></div>
                 %s

                <div style='color:white'>
                    %s
                </div>
                <div>
                  <hr/>
                  <div style="font-size: 10px; color:gray;">
                  <div>This email template generated by Dashboard Observer</div>
                  <div>Dashboard Observer job will trigger at 8:30 am on Mon - Fri</div>
                  </div>
                </div>
              </body>
            </html>
        """

        resultText = ''
        notCreatedTicketsText = ''
        notFoundTicketsMap = {}
        if resultMap:
            # INIT the OZ JIRA Server
            oz_jira_server_ins = connect_jira(oz_jira_server, oz_jira_username, oz_jira_password)

            for key in resultMap.keys():
                resultText=resultText + '<h4><span style="color:blue">%s (%s)</span></h4>' % (key, resultMap[key]['total']) + '<ul>'
                for statusKey in resultMap[key].keys():
                    if statusKey !='total':
                        resultText = resultText + '<li>' + '<h5>%s (%s)</h5>' % (statusKey, resultMap[key][statusKey].__len__() )+ '</li>'
                        if resultMap[key][statusKey].__len__() > 0:
                            resultText = resultText + "<ol>"
                            for issue in resultMap[key][statusKey]:
                                cmc_issue_key = issue['key'].strip()
                                keyWithHyperLink = "<a href='%s'>%s</a>" % (issue['url'], cmc_issue_key)
                                oz_jira_tikcet_info = get_oz_jira_info(oz_jira_server_ins, cmc_issue_key)

                                if oz_jira_tikcet_info == None:
                                    # put this ticket to a map
                                    if not notFoundTicketsMap.has_key(key):
                                        notFoundTicketsMap[key] = []
                                    notFoundTicketsMap[key].append(cmc_issue_key)

                                cmc_ticket_with_oz_jira_info='%s [%s]' % (keyWithHyperLink, oz_jira_tikcet_info)
                                resultText = resultText+ '<li>' + '%s - %s' % (cmc_ticket_with_oz_jira_info, issue['summary']) + '</li>'
                            resultText = resultText + "</ol>"

                resultText  = resultText + '</ul>'

            for ticketType in notFoundTicketsMap:
                notCreatedTicketsText = notCreatedTicketsText + '<div>Not created ticket for %s: %s</div>' % (ticketType, '/'.join(notFoundTicketsMap[ticketType]))

            # write to file
            filename = os.path.join(get_work_path(output_dir), 'dashboard_%s.html' % today)
            fout = open(filename, 'wr')
            fout.write(html_template % (dashboard_real_Url, dashboard_real_Url,resultText, notCreatedTicketsText ))
            fout.flush()
            log.info("Get dashboard successfully. The result was in %s", filename)

            # call send email
            # send_email(filename)


def get_work_path(workDir):
    workPath = None
    if workDir and not workDir.isspace():
        workPath = os.path.expanduser(workDir)
    else:
        workPath = os.path.realpath(".")

    if not os.path.exists(workPath):
        os.makedirs(workPath)
    return workPath

def connect_jira(jira_server, jira_user, jira_password):
    log.info("Connecting: %s with author: %s", jira_server,jira_user)
    try:
        jira_option = {'server': jira_server}
        jira = JIRA(jira_option, basic_auth={jira_user, jira_password})
        log.info("Connected server: %s successfully!", jira_server)
        return jira
    except Exception, e:
        log.error("Failed connect JIRA server (%s)", jira_server)
        return None

def get_oz_jira_info(oz_jira_server, cmc_ticket_no):
    try:
        # log.info("=====fiind ticket: [%s]", cmc_ticket_no)
        issues = oz_jira_server.search_issues(oz_jira_search_query % cmc_ticket_no)
        issue = issues[0]
        issue_status = issue.fields.status.name.encode('utf-8').strip()
        log.info("found the ticket %s [%s]", issue.key, cmc_ticket_no)

        oz_ticket_info = "<a href='{oz_issue_url}'>{oz_issue_key}</a>, {status}, <span style='color:green'>{owner}</span>".format(
            oz_issue_url=oz_jira_url_prefix % issue.key, 
            oz_issue_key=issue.key, 
            status=issue_status, 
            owner=issue.fields.assignee)
        return oz_ticket_info
    except:
        log.warn("Not found OZ JIRA Ticket for %s", cmc_ticket_no)
        return None

def send_email(filename):
    try:
        if not filename or filename == None or filename == '':
            log.warn('No file to read, ignore send email')
            return

        log.info("Ready send email.....")
        de_email_pwd = base64.decodestring(email_password)
        with open(filename) as fp:
            # mail_content = fp.read()
            mail_content = MIMEText(fp.read(), 'html')

        # request_url = 'https://api.mailgun.net/v3/{0}/messages'.format(email_server_api_user)
        # request = requests.post(request_url, auth=('api', email_server_key), data={
        #     'from': email_from,
        #     'subject': email_subject,
        #     'to': email_to_tech_leads,
        #     'html': mail_content
        # })
        # 
        # if request.status_code == requests.codes.ok:
        #     log.info('Send email successfully')
        # else:
        #     log.error('Send email failed, return code:%s', request.status_code)
        #     log.error(request.text)

        mail_content['Subject'] = email_subject
        mail_content['From'] = email_from

        server = smtplib.SMTP('smtp.gmail.com:587')
        server.ehlo()
        server.starttls()
        server.login(email_username, de_email_pwd)
        server.sendmail(email_from, email_to_tech_leads, mail_content.as_string())
        server.quit()
        log.info('Send email successfully')

    except:
        log.error("Send Email Error")

if __name__ == '__main__':
    getDashboardContent()
