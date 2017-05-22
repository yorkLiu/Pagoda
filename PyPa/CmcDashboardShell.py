#!/usr/bin/env python
# -*- coding: UTF-8 -*- 
import os
import sys
import logging
import requests
import time
import datetime
from jira import JIRA
import base64
import smtplib
from email.mime.text import MIMEText
import re
import collections

reload(sys)
sys.setdefaultencoding("utf8")

###################### tools #####################
# pip install requests
# pip install request[socks]
# pip install JIRA
###################### tools #####################

###################### Shell ENV [START] #################
# export OUTPUT_DIR='~/Downloads/JIRA/Dashboard'
# export EMAIL_FROM='Dashboard Observer <dashboard_observer@mail.com>'
# export EMAIL_TO_TECH_LEADS='mail@mai.com'

###################### Shell ENV [END] ###################

current_milli_time = lambda: int(round(time.time() * 1000))
dateFormat='%Y-%m-%d'
today = datetime.datetime.now().strftime(dateFormat)


######################## Configuration [Start] ###############################
output_dir = os.environ.get('OUTPUT_DIR')

### START config cmc jira username & password
cmc_jira_username='ozintel'
cmc_jira_password='0zPa$$123'
### END config cmc jira username & password


### START config Email Sender
email_username=os.environ.get('EMAIL_USER_NAME')
email_password=os.environ.get('EMAIL_PWD')
email_from= os.environ.get('EMAIL_FROM')
email_subject='OZStrategy Dashboard on CMC Side on %s' % today

# config the email address here
email_to_tech_leads=os.environ.get('EMAIL_TO_TECH_LEADS').split(',')
email_ccs=os.environ.get('EMAIL_CCS').split(',')

### END config Email Sender

proxies = {
            'http': 'socks5://192.168.100.3:1083',
            'https': 'socks5://192.168.100.3:1083'
          }
######################## Configuration [End] #################################

### START CMC Dashboard Config
dashboard_real_Url = "https://jira.cmcassist.com/secure/RapidBoard.jspa?rapidView=129"
dashboardUrl = "https://jira.cmcassist.com/rest/greenhopper/1.0/xboard/work/allData.json?rapidViewId=129&_=%s"
cmc_jira_url_prefix = 'https://jira.cmcassist.com/browse/%s'
### END  CMC Dashboard Config

### START OZ JIRA Config
oz_jira_server = 'http://192.168.168.21:8091'
oz_jira_username='builder'
oz_jira_password = "builder$123"

oz_jira_search_query = 'project = "CMC JIRA Tickets" AND summary ~ %s'
oz_jira_url_prefix = 'http://192.168.168.21:8091/browse/%s'
### END OZ JIRA Config

cmc_dashboard_extra_fields_defined= {
    'customfield_11263': '<span style="font-weight: bold;">Sprint:</span> %s',
    'customfield_11152': '<span style="font-weight: bold;">QA Date:</span> %s'
}

oz_jira_status_map = {
    '已解决': '<span style="color:green">Resolved</span>',
    '开始': 'Open',
    '进行中': 'In Progress',
    '关闭': '<span style="color:green">Closed</span>',
    '重新打开': '<span style="color:red;font-weight: bold;;">Reopened</span>'
}



log = logging.getLogger('Dashboard')
log.setLevel(logging.INFO)
fmt = logging.Formatter('%(levelname)s:%(name)s:%(message)s')
h = logging.StreamHandler()
h.setFormatter(fmt)
log.addHandler(h)



def getDashboardContent():
    resultMap={}
    group_by_extra_map = {}
    url = dashboardUrl % current_milli_time()
    log.info("Visit URL %s", url)

    response = requests.get(url,  auth=(cmc_jira_username, cmc_jira_password), proxies=proxies)
    if response.status_code == requests.codes.ok:
        log.info("Data was returned.")
        jsonObj = response.json()
        issues = jsonObj['issuesData']['issues']
        for issueObj in issues:
            key = issueObj['key']
            summary = issueObj['summary']
            # type is 'defect', 'story'...
            type = issueObj['typeName']
            # status is 'Open (Dev)', 'In Progress (Dev)' ...
            status = issueObj['statusName']
            # Sprint & QA Date
            extra_text = get_extra_fields_info(issueObj['extraFields'],  key, group_by_extra_map)

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
                        'extra_text': extra_text,
                        'url': cmc_jira_url_prefix % key
                    })


        html_template = """\
            <html>
              <head>
                <meta charset="UTF-8">
                <style type="text/css">
              		body{margin: 20px;}
              		ol>li{line-height: 25px;}
              		.extra-field-cls{font-size: 9px; line-height: 20px; border-bottom: 1px dotted #999 }
              		.extra-field-cls > div { border-bottom: 1px dotted #999}
              	</style>
              </head>
              <body>
                 <div style='font-weight: bold;'>The following data got from: <a href='%s'>%s</a></div>
                 %s


                </br>
                </br>
                <div class='extra-field-cls'>
                    %s
                </div>

                </br></br>
                <div style='color:white'>
                    %s
                </div>

                <div>
                  <hr/>
                  <div style="font-size: 10px; color:gray;">
                  <div>This email template generated by Dashboard Observer</div>
                  <div>Dashboard Observer job will trigger at 8:30/10:30 am on Mon - Fri</div>
                  </div>
                </div>
              </body>
            </html>
        """

        resultText = ''
        notCreatedTicketsText = ''
        group_by_extra_map_html_info = ''
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
                                text_info_tpl = '<li>' + '%s - %s <div style="font-size:10px;line-height: 15px;">%s</div></li>'
                                resultText = resultText+ text_info_tpl % (cmc_ticket_with_oz_jira_info, issue['summary'], issue['extra_text'])
                            resultText = resultText + "</ol>"

                resultText  = resultText + '</ul>'

            group_by_extra_map = collections.OrderedDict(sorted(group_by_extra_map.items()))
            for extra_text in group_by_extra_map:
                tpl = '<div><span style="font-weight:bold">%s:</span> <span style="font-style: italic;">%s</span></div>'
                group_by_extra_map_html_info = group_by_extra_map_html_info + tpl % (extra_text, ', '.join(group_by_extra_map[extra_text]))
                

            for ticketType in notFoundTicketsMap:
                notCreatedTicketsText = notCreatedTicketsText + '<div>Not created ticket for %s: %s</div>' % (ticketType, '/'.join(notFoundTicketsMap[ticketType]))

            # write to file
            filename = os.path.join(get_work_path(output_dir), 'dashboard_%s.html' % today)
            fout = open(filename, 'wr')
            fout.write(html_template % (dashboard_real_Url, dashboard_real_Url,resultText, group_by_extra_map_html_info, notCreatedTicketsText))
            fout.flush()
            log.info("Get dashboard successfully. The result was in %s", filename)

            # call send email
            send_email(filename)


def get_extra_fields_info(extraFields, cmc_ticket_key, group_by_extra_map):
    date_pattern = re.compile(r'\d+\-\d+\-\d+')
    none_text = '<span style="color: #999; font-style: italic;">None</span>'
    html_info = []
    for ext_field in extraFields:
        field_label = ext_field['label']
        field_text = ext_field['html'].strip() or None
        # customfield_11263 is QA Date
        if ext_field['id'] == 'customfield_11152' and field_text:
            field_text = re.findall(date_pattern, field_text)[0]

        ext_field_html = field_text or none_text
        html_info.append(cmc_dashboard_extra_fields_defined[ext_field['id']] % ext_field_html)

        map_key = '%s %s' % (field_label, field_text)
        if not group_by_extra_map.has_key(map_key):
            group_by_extra_map[map_key] = []

        group_by_extra_map[map_key].append('<a href="%s">%s</a>' % (cmc_jira_url_prefix % cmc_ticket_key, cmc_ticket_key))
            

    return '<br/>'.join(html_info)

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

        issue_status_en = issue_status
        if oz_jira_status_map.has_key(issue_status):
            issue_status_en = oz_jira_status_map[issue_status]

        log.info("found the ticket %s [%s]", issue.key, cmc_ticket_no)

        oz_ticket_info = "<a href='{oz_issue_url}'>{oz_issue_key}</a>, {status}, <span style='color:green'>{owner}</span>".format(
            oz_issue_url=oz_jira_url_prefix % issue.key, 
            oz_issue_key=issue.key, 
            status=issue_status_en, 
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

        mail_content['Subject'] = email_subject
        mail_content['From'] = email_from
        mail_content['To'] = ','.join(email_to_tech_leads)
        mail_content['Cc'] = ','.join(email_ccs)

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
