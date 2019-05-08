import os
import sys
import requests
import logging
from jira import JIRA
from jira import JIRAError
import xml.etree.ElementTree as ET
# import datetime
import pandas as pd
import base64
import smtplib
from email.mime.text import MIMEText

############################### Install Tools###################
# pip install pandas --user
# pip install python-dateutil pytz --force-reinstall --upgrade --user
############################### Install Tools###################

reload(sys)
sys.setdefaultencoding("utf8")

dateFormat='%Y-%m-%d'
now = pd.datetime.now()
today = now.strftime(dateFormat)

### START config Email Sender
email_username=os.environ.get('EMAIL_USER_NAME')
email_password=os.environ.get('EMAIL_PWD')
email_from= os.environ.get('EMAIL_FROM')
email_subject='%s CMC Tickets WorkLogged Tracking' % today

# config the email address here
email_to_tech_leads=os.environ.get('EMAIL_TO_TECH_LEADS').split(',')
email_ccs=os.environ.get('EMAIL_CCS').split(',')

### END config Email Sender

#### JIRA Account Config [Start]######
oz_jira_username=os.environ.get('OZ_JIRA_USERNAME')
oz_jira_password =os.environ.get('OZ_JIRA_PASSWORD')

cmc_jira_username=os.environ.get('CMC_JIRA_USERNAME')
cmc_jira_password=os.environ.get('CMC_JIRA_PASSWORD')

use_proxy=os.environ.get('USE_PROXY')
proxy_ip = os.environ.get('PROXY_SERVER')
#### JIRA Account Config [End]########

cmc_jira_server = 'https://jira.katabat.com'
oz_jira_server = 'http://192.168.168.21:8091'

oz_jira_ticket_link = 'http://192.168.168.21:8091/browse/{ticketNo}'
oz_jira_cmc_jira_link = 'https://jira.katabat.com/browse/{cmcTicketNo}'


oz_jira_search_all_tickets_monthly = 'project = "CMC JIRA Tickets" AND labels in(%s)'
oz_jira_search_all_tickets_monthly_by_ticket_number_jql = 'project = "CMC JIRA Tickets" AND (summary ~ %s)'
oz_jira_search_all_labels_montly_api = oz_jira_server+'/rest/api/1.0/labels/23175/suggest?query=%s'

proxies = None
if use_proxy and str(use_proxy).upper() in ('YES', 'TRUE'):
    proxies = {
                'http': str(proxy_ip).strip(),
                'https': str(proxy_ip).strip()
              }

log = logging.getLogger('CmcWorkLog')
log.setLevel(logging.INFO)
fmt = logging.Formatter('%(levelname)s:%(name)s:%(message)s')
h = logging.StreamHandler()
h.setFormatter(fmt)
log.addHandler(h)

keys = {
    'key': 'key',
    'summary': 'summary',
    'type': 'type',
    'totalEstimate': 'total_estimate',
    'totalLoggedHours': 'total_logged_hours',
    'totalLoggedHoursCurrentMonth': 'current_month_total_logged_hours'
}

# monthly_tickets_map
# key: cmc ticket number, value: refer oz ticket number
monthly_tickets_map={}

# statistics by ticket by ticket type
# CA, SMW, INC, ... Defect, Story, Task
logged_hours_by_type={}


templates = {
    'ticket_info_li': '<li><a href="{ticketurl}"><b>{key}</b></a>[<a href="{ozticketurl}">{ozissuekey}</a>] - <span class="summary">{summary}</span> {loggedinfo} <div style="width: 500px; padding: 3px; ">{logdetails}</div></li>',
    'ticket_logged_info': '<span style="font-weight: bold;"><span>Estimate/Work Logged: </span>{estimate}/<span style="color:blue">{worklogged}</span></span>',
    'ticket_logged_info_no_completed': '<span style="font-weight: bold; color:red"><span>Estimate/Work Logged: </span>{estimate}/<span style="color:blue">{worklogged}</span></span>',
    'ticket_worklog_table': '<table><thead><tr><th>Date</th><th>Description</th><th>Worked</th></tr></thead><tbody>%s</tbody></table>',
    'ticket_worklog_details_tr': '<tr><td class="col1">{date}</td><td class="col2">{comment}</td><td class="col3">{hours}</td></tr>',
    'logged_hours_by_ticket_type': '<div><b>{typename}: {hours}</b></div>'
}

html_template = """\
            <html>
              <head>
                <style type="text/css">
              		body{margin: 20px; font-size: 14px;font-family: Cambria, "Hoefler Text", Utopia, "Liberation Serif", "Nimbus Roman No9 L Regular", Times, "Times New Roman", serif;}
              		ol>li{line-height: 25px;}
              		.summary{font-size:12px;color:gray;}
              		table>thead>tr>th, table>tbody>tr>td {font-size:10px; text-align:left;border-bottom: 1px solid #ccc;overflow: hidden;padding: 5px 7px;vertical-align: top}
              		.col1{width:100px}
              		.col2{width:350px}
              		.col3{width:50px; text-align: right;}
              	</style>
              </head>
              <body>
                <div class='header-info'>
                    <p>
                       This email just notice us how many hours with <b>ozintel</b> logged in CMC JIRA 
                    </p>
                    <p>
                        There are <a href='%s'> <b>%s</b> CMC tickets</a> on %s but only <b>%s</b> CMC Tickets logged hours 
                    </p>
                    <p>
                        <div>
                            <div><label>Total Logged Hours on current month: </label> <span style="color:blue;"><b>%s</b></span></div>
                            <div style="padding-left: 10px">%s</div>
                        </div>
                        </p>
                </div>

                <p>
                    <div>
                        <ol>
                            %s
                        </ol>
                    </div>
                </p>

                <div>
                  <hr/>
                  <div style="font-size: 10px; color:gray;">
                  <div>This email template generated by Estimation Observer</div>
                  <div>Estimation Observer job will trigger at 7:30 am on Mon - Fri and the last work day of a month</div>
                  <div>If today is not the last work day of current month, then the email content contains tickets is the Dashboard tickets</div>
                  <div>Else if today is the last work day of current month, then the email content is 'All tickets which handled by current month'</div>
                  </div>
                </div>
              </body>
            </html>
        """

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
    except JIRAError as e:
        log.error("Failed connect JIRA server (%s)", jira_server)
        log.error(e.message)
        return None

def get_cmc_ticket_number(oz_ticket):
    """
    Find the CMC ticket number from OZ Ticket
    There are two ways to find it
        # 1: get the cmc ticket from OZ ticket summary
        # 2: get the cmc ticket from OZ ticket 'customfield_10227' fields (CMC JIRA Link)
    If "CMC JIRA Link" value not None, the as the priority, get the CMC Ticket Number from "CMC JIRA Link" firstly
    Else if "CMC JIRA Link" was not value, get the CMC Ticket Number from "Summary"
    ::param oz_ticket the ticket object
    """

    cmc_ticket_num = None
    if oz_ticket:
        summary = oz_ticket.fields.summary
        cmc_jira_link_url = oz_ticket.fields.customfield_10227 or ''

        cmc_ticket_no_in_summary = str(summary).strip().split(" ")[0]
        cmc_ticket_no_in_link_url = str(cmc_jira_link_url).strip().replace(oz_jira_cmc_jira_link.format(cmcTicketNo='') , '')

        if cmc_ticket_no_in_link_url:
            cmc_ticket_num = cmc_ticket_no_in_link_url
        else:
            cmc_ticket_num = cmc_ticket_no_in_summary

    return cmc_ticket_num.strip()

def statistics_hours_by_ticket_type(ticketNum, loggedHours):
    ticket_key_prefix=ticketNum.split('-')[0]
    if not logged_hours_by_type.has_key(ticket_key_prefix):
        logged_hours_by_type[ticket_key_prefix] = float(0.0)

    logged_hours_by_type[ticket_key_prefix] = logged_hours_by_type[ticket_key_prefix] + loggedHours            

def generate_html(logged_tickets, totalFoundTickets, url):
    ticket_info_strs = ''
    hours_type_ticket_type_strs = ''
    total_estimate_hours=float(0.0)
    total_logged_hours = float(0.0)
    total_logged_hours_current_month = float(0.0)
    
    if logged_tickets.__len__() == 0:
        log.info("No work logged tickets, return.")

    for logged_ticket_obj in logged_tickets:
        key = logged_ticket_obj[keys['key']]
        summary = logged_ticket_obj[keys['summary']]
        estimate = float(logged_ticket_obj[keys['totalEstimate']])
        worklogged = float(logged_ticket_obj[keys['totalLoggedHours']])
        current_month_worklogged = float(logged_ticket_obj[keys['totalLoggedHoursCurrentMonth']])
        total_logged_hours = total_logged_hours + worklogged
        total_logged_hours_current_month = total_logged_hours_current_month + current_month_worklogged
        total_estimate_hours = total_estimate_hours + estimate

        # calculate the work log by ticket type
        statistics_hours_by_ticket_type(key, current_month_worklogged)

        print 'key:', key

        log_details_str=''
        worklog_details = logged_ticket_obj[key] if logged_ticket_obj.has_key(key) else None

        if worklog_details == None:
            continue

        for worklog in worklog_details:
            date = worklog['log_date']
            # comment = (u' '.join(worklog['log_comment'])).encode("utf-8").strip()
            comment = worklog['log_comment'].replace(u'\uff0c', ',').replace("\n", " ").replace("\r", " ")
            hours = worklog['log_hours']
            print date, '\t', comment, '\t', hours
            log_details_str = log_details_str + templates['ticket_worklog_details_tr'].format(date=date, comment=comment, hours=hours)

        if log_details_str.strip() != '':
            log_details_str = templates['ticket_worklog_table'] % log_details_str


        ticket_logged_info_str=templates['ticket_logged_info']
        if worklogged < estimate:
            ticket_logged_info_str = templates['ticket_logged_info_no_completed']
        ticket_logged_info_str=ticket_logged_info_str.format(estimate = estimate, worklogged=worklogged)

        ticket_info_strs = ticket_info_strs + templates['ticket_info_li'].format(
            ticketurl=oz_jira_cmc_jira_link.format(cmcTicketNo=key),
            key=key,
            ozticketurl=oz_jira_ticket_link.format(ticketNo=monthly_tickets_map[key]),
            ozissuekey=monthly_tickets_map[key],
            summary=summary,
            loggedinfo=ticket_logged_info_str,
            logdetails=log_details_str)

    if logged_hours_by_type.keys().__len__() > 0:
        for key in logged_hours_by_type:
            typename =  key
            hours = logged_hours_by_type[key]
            hours_type_ticket_type_strs = hours_type_ticket_type_strs + templates['logged_hours_by_ticket_type'].format(typename=typename, hours=hours)

    cur_month_str = now.strftime('%B %Y')
    html = html_template % (url, totalFoundTickets, cur_month_str, logged_tickets.__len__(),
                            total_logged_hours_current_month, hours_type_ticket_type_strs,
                            ticket_info_strs)
    
    print html
    send_email(html)

def get_search_url(jql):
    return '%s/issues/?jql=%s' % (oz_jira_server, jql)

def get_worklog_monthly():
    """
        This method will called only once per month
        Only the last work day of a month will call this method
        Statics all tickets works logged of current month 
    """
    labels_for_current_month=[]
    cmc_tickets_for_current_month=[]

    current_month_label_prefix = now.strftime('%Y%m')
    log.info("Query issue on %s", current_month_label_prefix)
    cookies = None
    for i in range(0, 4):
        current_month_label_prefix_idx= '%s%s' % (current_month_label_prefix, i)
        if not cookies:
            r = requests.get(oz_jira_search_all_labels_montly_api % current_month_label_prefix_idx, auth=(oz_jira_username, oz_jira_password))
            cookies = r.cookies
        else:
            r = requests.get(oz_jira_search_all_labels_montly_api % current_month_label_prefix_idx, cookies=cookies)

        root = ET.fromstring(r.content)
        for label in root.iter('label'):
            if label.text:
                labels_for_current_month.append(label.text)


    # clear the monthly_tickets_map
    monthly_tickets_map.clear()
    if labels_for_current_month.__len__() > 0:
        jql = oz_jira_search_all_tickets_monthly % ','.join(labels_for_current_month)
        print jql
        issues_for_monthly = oz_jira.search_issues(jql, maxResults=1000)
        log.info("Find %s issues for current month", issues_for_monthly.__len__())
        for issue in issues_for_monthly:
            cmc_ticket_num = get_cmc_ticket_number(issue)
            cmc_tickets_for_current_month.append(cmc_ticket_num)
            monthly_tickets_map[cmc_ticket_num] = issue.key

        if cmc_tickets_for_current_month.__len__() > 0:
            logged_tickets_monthly =  get_worklog_by_tickets(cmc_tickets_for_current_month)
            if logged_tickets_monthly.__len__() > 0:
                generate_html(logged_tickets_monthly, issues_for_monthly.__len__(), get_search_url(jql))


def get_worklog_daily(tickets):
    # clear the monthly_tickets_map
    monthly_tickets_map.clear()
    jql = oz_jira_search_all_tickets_monthly_by_ticket_number_jql % ' or summary ~'.join(tickets)
    issues_for_monthly = oz_jira.search_issues(jql, maxResults=1000)
    log.info("Find %s issues for current month", issues_for_monthly.__len__())
    for issue in issues_for_monthly:
        cmc_ticket_num = get_cmc_ticket_number(issue)
        monthly_tickets_map[cmc_ticket_num] = issue.key

    logged_tickets_monthly =  get_worklog_by_tickets(tickets)
    if logged_tickets_monthly.__len__() > 0:
        generate_html(logged_tickets_monthly, tickets.__len__(), get_search_url(jql))    
        
def get_worklog_by_tickets(tickets):
    """
        Find out the @tickets how many hours were logged by oz
        ::param tickets tickets array
        return array object [{log_date: '', log_comment: '', oz_logged_hours: ''}...]
    """
    oz_work_logs = []
    current_mont_prefix=now.strftime('%Y-%m')
    if tickets and tickets.__len__() > 0:
        for ticketNo in tickets:
            if not ticketNo:
                continue

            log.info("Find the WorkLog for ticket No.[%s]", ticketNo)
            issue = cmc_jira.issue(ticketNo)
            ###### get the total estimate hours START##################
            total_oz_estimate_hours= float(issue.fields.customfield_12751) if issue.fields.customfield_12751 else 0
            reminder_oz_hours = float(issue.fields.customfield_13253) if issue.fields.customfield_13253 else 0
            reminder_dev_oz_hours = float(issue.fields.customfield_13252) if issue.fields.customfield_13252 else 0
            total_oz_estimate_hours = total_oz_estimate_hours or reminder_oz_hours or reminder_dev_oz_hours
            ###### get the total estimate hours END###################
            oz_logged_hours=0.0
            oz_current_month_logged_hours=0.0
            ticket_work_logs = []
            worklogs = cmc_jira.worklogs(ticketNo)
            for worklog in worklogs:
                logged_author_name = worklog.raw['author']['name']
                if logged_author_name in ('ozintel', 'ozdev'):
                    log_comment = worklog.raw['comment']
                    log_date = worklog.raw['started'].split('T')[0]
                    log_time_seconds=worklog.raw['timeSpentSeconds']
                    log_hour = long(log_time_seconds)/3600.0
                    oz_logged_hours = oz_logged_hours + log_hour
                    if str(log_date).startswith(current_mont_prefix):
                        oz_current_month_logged_hours = oz_current_month_logged_hours + log_hour

                    ticket_work_logs.append({'log_date': log_date, 'log_comment': log_comment, 'log_hours': log_hour})
                    print log_date, '\t', log_comment, '\t', log_hour

            if ticket_work_logs.__len__() > 0:
                oz_work_logs.append({
                    keys['key']: issue.key, 
                    keys['summary']: issue.fields.summary, 
                    keys['type']: issue.fields.issuetype.name,
                    keys['totalEstimate']:total_oz_estimate_hours, 
                    keys['totalLoggedHours']: oz_logged_hours, 
                    keys['totalLoggedHoursCurrentMonth']: oz_current_month_logged_hours, 
                    ticketNo: ticket_work_logs})

            log.info("Original Estimate Oz Hours/OZ Logged: %s/%s", total_oz_estimate_hours, oz_logged_hours)
            if total_oz_estimate_hours > 0 or  oz_logged_hours > 0:
                print 'Total/Logged: %s/%s' % (total_oz_estimate_hours,oz_logged_hours)
    return oz_work_logs


def send_email(html_content):
    try:
        if not html_content or html_content == None or html_content == '':
            log.warn('No email content to send, ignore send email')
            return

        log.info("Ready send email.....")

        email_tos = list(email_to_tech_leads)
        de_email_pwd = base64.decodestring(email_password)

        mail_content = MIMEText(html_content, 'html')

        mail_content['Subject'] = email_subject
        mail_content['From'] = email_from
        mail_content['To'] = ','.join(email_tos)
        mail_content['Cc'] = ','.join(email_ccs)

        server = smtplib.SMTP('smtp.gmail.com:587')
        server.ehlo()
        server.starttls()
        server.login(email_username, de_email_pwd)
        server.sendmail(email_from, email_tos, mail_content.as_string())
        server.quit()
        log.info('Send email successfully')

    except:
        log.error("Send Email Error")

if __name__ == '__main__':
    parameters = sys.argv[1:]
    cmc_jira = connect_jira(cmc_jira_server, cmc_jira_username, cmc_jira_password, use_proxy=True)
    oz_jira = connect_jira(oz_jira_server, oz_jira_username, oz_jira_password)

    lastWorkDate=pd.date_range(pd.datetime.today(), periods=1, freq='BM')
    lastWorkDateStr=lastWorkDate.strftime(dateFormat)[0]
    print lastWorkDateStr, today, today == lastWorkDateStr
    # if today is the last work date of a month
    # then run the method to statistics all cmc ticket logged hours which in oz jira refered
    # if today == lastWorkDateStr:
    #     get_worklog_monthly()
    # else:
    #     # else will statistics the dashboard which assigned ozintel or ozdev tickets
    #     # within logged hours
    #     if parameters.__len__() > 0:
    #         filename = parameters[0]
    #         if os.path.isfile(filename):
    #             file = open(filename, 'r')
    #             ticket_file_content = file.read()
    #             tickets = ticket_file_content.replace("/", ",").replace("\\", ",").replace("\n", ",").replace(" ", "").replace("\t", "").split(',')
    #             print tickets
    #             if tickets:
    #                 get_worklog_daily(tickets)

    get_worklog_monthly()
