#!/usr/bin/env python
# -*- coding: UTF-8 -*- 
import os
import sys
import logging
import requests
import datetime
from datetime import timedelta
from jira import JIRA
import base64
import smtplib
from email.mime.text import MIMEText


reload(sys)
sys.setdefaultencoding("utf8")


###################### tools #####################
# pip install requests
# pip install request[socks]
# pip install JIRA
###################### tools #####################

dateFormat='%Y-%m-%d'
labelDateFormat='%Y%m%d'
currentMonthFormat='%Y%m'
now = datetime.datetime.now()
today = now.strftime(dateFormat)
label = now.strftime(labelDateFormat)
currentMonth= now.strftime(currentMonthFormat)

######################## Configuration [Start] ###############################
output_dir = os.environ.get('OUTPUT_DIR')

### START config Email Sender
email_username=os.environ.get('EMAIL_USER_NAME')
email_password=os.environ.get('EMAIL_PWD')
email_from= os.environ.get('EMAIL_FROM')
email_subject='%s CMC Tickets Tracking' % today

# config the email address here
email_to_tech_leads=os.environ.get('EMAIL_TO_TECH_LEADS').split(',')
email_ccs=os.environ.get('EMAIL_CCS').split(',')
### END config Email Sender

#### JIRA Account Config [Start]######
oz_jira_username=os.environ.get('OZ_JIRA_USERNAME')
oz_jira_password =os.environ.get('OZ_JIRA_PASSWORD')

cmc_jira_username=os.environ.get('CMC_JIRA_USERNAME')
cmc_jira_password=os.environ.get('CMC_JIRA_PASSWORD')
#### JIRA Account Config [End]########

qa_manager_full_names=os.environ.get('OZ_QA_MANAGER_NAME')

use_proxy=os.environ.get('USE_PROXY')
proxy_ip = os.environ.get('PROXY_SERVER')

### START config  jira username & password
oz_jira_server = 'http://192.168.168.21:8091'
cmc_jira_server = 'https://jira.katabat.com'
### END config  jira username & password

cmc_jira_url_prefix = 'https://jira.katabat.com/browse/%s'
oz_jira_url_prefix = 'http://192.168.168.21:8091/browse/%s'
oz_fixed_tickets_query='project = "CMC JIRA Tickets" AND labels = %s AND status in (Resolved, Closed, "Passed QA", "In QA", Done) ORDER BY status DESC'


proxies = None
if use_proxy and str(use_proxy).upper() in ('YES', 'TRUE'):
    proxies = {
                'http': str(proxy_ip).strip(),
                'https': str(proxy_ip).strip()
              }

log = logging.getLogger('JIRA Checker')
log.setLevel(logging.INFO)
fmt = logging.Formatter('%(levelname)s:%(name)s:%(message)s')
h = logging.StreamHandler()
h.setFormatter(fmt)
log.addHandler(h)


na_text='N/A'
rejected_ticket_file_name = '%s-CMC-Tickets-Audit.txt' % currentMonth
mail_to_set = set(email_to_tech_leads)

oz_jira_status_map = {
    '已解决': 'Resolved',
    '开始': 'Open',
    '进行中': 'In Progress',
    '关闭': 'Closed',
    '重新打开': 'Reopened'
}

jira_ticket_status_styles = {
    'resolved': '<span style="color:green;">Resolved</span>',
    'closed': '<span style="color:green;">Closed</span>',
    'passed-qa': '<span style="color:green;">Passed QA</span>',
    'done': '<span style="color:green;">Done</span>',
    'qa-complete': '<span style="color:green;">QA Complete</span>',
    'In-QA': '<span style="color:blue;">In QA</span>',
    'in-progress-dev': '<span style="color:blue;">In Progress Dev</span>',
    'in-progress-qa': '<span style="color:blue;">In Progress QA</span>',
    'rejected-qa': '<span style="color:red;"><b>Rejected QA</b></span>',
    'reopen': '<span style="color:red;"><b>ReOpen</b></span><'
}

statistics_jira_status={
    'rejected': 'Rejected'
}

html_templates = {
    'item_content_tpl': '<li><span><span class="ozinfo">{ozTicketInfo}</span> <span class="cmcinfo">[{cmcTicketInfo}]</span> - <span class="summary">{ticketSummary}</span> </span>{extraInfo}</li>',
    'oz_ticket_info_tpl': "<a href='{hyperLink}'>{hyperText}</a>, {ticketStatus}, {author}, Reporter: {qaReporter})",
    'cmc_ticket_info_tpl': "<a href='{hyperLink}'>{hyperText}</a>, {ticketStatus}",
    'author_info_tpl': "<span style='color:green'>{author}</span>",
    'qa_report_info_tpl': "<span style='color:purple'>{qaReporter}</span>",
    'extra_info_tpl': "{extraInfo}",
    'not_assign_qa_reporter_tpl': "<div class='quote'><span>{qaManager} there are <b>{count}</b> ticket(s) without QA Reporter: </span> <div>{tickets}</div></div>",
    'rejected_tickets_statistics_tpl': "<div class='quote'>{ticketStatus} (<b>{count}</b>): <div>{tickets}</div></div>",
    'statistics_file_content_tpl': "{ticketStatus}|{date}|{label}: {tickets}\n",
    'email_content_tpl': """\
         <html>
              <head>
                <meta charset="UTF-8">
                <style type="text/css">
              		body{margin: 20px; font-size: 14px;font-family: Cambria, "Hoefler Text", Utopia, "Liberation Serif", "Nimbus Roman No9 L Regular", Times, "Times New Roman", serif;}
              		ol>li{line-height: 25px;}
              		.extra-field-cls{font-size: 9px; line-height: 20px; border-bottom: 1px dotted #999 }
              		.extra-field-cls > div { border-bottom: 1px dotted #999}
              		.ozinfo{font-size:12px;}
              		.cmcinfo{font-size: 14px;}
              		.summary{font-size:12px;color:gray;}
              		/*.not-assign-qa-reporter > div {font-size:12px; padding: 5px 0 0 20px;}*/
              		.quote {
                      background: #f9f9f9;
                      border-left: 3px solid #ccc;
                      margin: 1.5em 10px;
                      padding: .5em 10px;
                      /*quotes: '\\201C''\\201D''\\2018''\\2019';*/

                      -webkit-border-top-left-radius: 10px;
                      -webkit-border-top-right-radius: 3px;
                      -webkit-border-bottom-right-radius: 6px;
                      -webkit-border-bottom-left-radius: 10px;
                      -moz-border-radius-topleft: 10px;
                      -moz-border-radius-topright: 3px;
                      -moz-border-radius-bottomright: 6px;
                      -moz-border-radius-bottomleft: 10px;
                    }
                    .quote:before {
                      color: #ccc;
                      content: '\\201C';
                      font-size: 4em;
                      line-height: .1em;
                      margin-right: .25em;
                      vertical-align: -.4em;
                    }
                    .quote:after{content:'';/*content: '\\201D';*/}
                    .quote p {display: inline; }
              	</style>
              </head>
              <body>
                <p>
                  <div class='header-info'>
                    <p>
                    <div>This email just notice us CMC tickets status, developer, QA Reporter and etc which OZ handling</div>
                    </p>
                    <p>
                    <div> There are <b>%s</b> CMC ticket(s) were <b>Fixed</b> on our side within <b>%s</b> label.
                    </p>
                  </div>
                </p>

                <p>
                    <div>
                        <ol>
                            %s
                        </ol>
                    </div>
                </p>

              <p>
                  <div for='not-assign-qa-reporter'>
                     %s
                  </div>
              </p>
              <p>
                <div for='reject-ticket-statisics'>
                    %s
                </div>
              </p>

              <div>
                  <hr/>
                  <div style="font-size: 10px; color:gray;">
                  <div>This email template generated by OZ Ticket Tracking Robot</div>
                  <div>OZ Ticket Tracking job will trigger at 7:40 am on Mon - Fri</div>
                  </div>
                </div>
              </body>
         </html>
    """
}

extra_info_texts = {
    'cmc_status_not_change_to_dev_completed': "<div style='color:orange'>@{author}, why don't you change {cmcTicket} to <b>DEV COMPLETE</b> on CMC side? </div>",
    'cmc_reject_qa': "<div style='color:red'>@{author}, CMC QA was <b>Rejected</b> {cmcTicket}, please take a look at this ticket.</div>",
    'notice_qa_reporter_update_ticket_status_tpl':"<div style='color:orange'>@{qaReporter}, please change the {ticket} to refer status. CMC has changed {cmcTicket} to <b>{cmcTicketStatus}</b></div>"
}

rejected_ticket_by_cmc_qa=[]
rejected_ticket_by_cmc_qa_only_ticket_numbers=[]

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
    except Exception, e:
        print e.message
        log.error("Failed connect JIRA server (%s)", jira_server)
        return None

def get_work_path(workDir):
    workPath = None
    if workDir and not workDir.isspace():
        workPath = os.path.expanduser(workDir)
    else:
        workPath = os.path.realpath(".")

    if not os.path.exists(workPath):
        os.makedirs(workPath)
    return workPath

def get_last_work_day_label():
    """
    Get last work day label
    Usually Sat and Sun day were rest day
    This method is 
        If today is Monday, then get the previous Friday's label
        Else return today's label
    """
    will_find_label = label 
    day_offset = 1
    weekday = datetime.datetime.now().isoweekday()
    if weekday == 1:
        # monday, usually Sat and Sun were reset days
        day_offset = 3

    will_find_label = (now - timedelta(days=day_offset)).strftime(labelDateFormat)
    return will_find_label

def generate_oz_ticket_hyperlink(oz_ticket_number):
    return '<a href="%s">%s</a>' % (oz_jira_url_prefix % oz_ticket_number, oz_ticket_number) 


def generate_cmc_ticket_hyperlink(cmc_ticket_number):
    return '<a href="%s">%s</a>' % (cmc_jira_url_prefix % cmc_ticket_number, cmc_ticket_number)

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
        cmc_ticket_no_in_link_url = str(cmc_jira_link_url).strip().replace(cmc_jira_url_prefix % '', '')

        if cmc_ticket_no_in_link_url:
            cmc_ticket_num = cmc_ticket_no_in_link_url
        else:
            cmc_ticket_num = cmc_ticket_no_in_summary

    return cmc_ticket_num.strip()

def get_wraped_ticket_status(ticket_status):
    p_ticket_status = str(ticket_status).replace('(', '').replace(')', '').replace(' ', '-').strip().lower()
    return jira_ticket_status_styles[p_ticket_status] if jira_ticket_status_styles.has_key(p_ticket_status) else ticket_status

def get_extra_info(oz_ticket_status, cmc_ticket_status, author, qa_reporter, oz_ticket_number, cmc_ticket_number):
    extra_text = ''
    p_cmc_ticket_status = str(cmc_ticket_status).replace('(', '').replace(')', '').replace(' ', '-').strip().upper()

    if oz_ticket_status.upper() in ('RESOLVED', 'CLOSED', 'PASSED QA', 'DONE') and p_cmc_ticket_status in ('OPEN-DEV', 'IN-PROGRESS-DEV'):
        extra_text = extra_info_texts['cmc_status_not_change_to_dev_completed'].format(author=author, cmcTicket=cmc_ticket_number)

    elif "REJECT" in p_cmc_ticket_status and 'INC' not in cmc_ticket_number and 'PROD' not in cmc_ticket_number:
        # Append rejected cmc ticket to array ignore INC and PROD tickets.
        reject_arr_item_text= '%s[%s]' % (generate_cmc_ticket_hyperlink(cmc_ticket_number), 
                                           generate_oz_ticket_hyperlink(oz_ticket_number))
        rejected_ticket_by_cmc_qa.append(reject_arr_item_text)
        rejected_ticket_by_cmc_qa_only_ticket_numbers.append('%s[%s]' % (cmc_ticket_number, oz_ticket_number))
        # get the extra text
        extra_text = extra_info_texts['cmc_reject_qa'].format(author=author, cmcTicket=cmc_ticket_number)

    elif ("PASSED" in p_cmc_ticket_status or p_cmc_ticket_status in ('CLOSED', 'RESOLVED', 'QA-COMPLETE')) and oz_ticket_status.upper() not in ('CLOSED', 'PASSED QA', 'DONE'):
        extra_text = extra_info_texts['notice_qa_reporter_update_ticket_status_tpl'].format(qaReporter=qa_reporter, 
                                                                               ticket=generate_oz_ticket_hyperlink(oz_ticket_number),
                                                                               cmcTicket=generate_cmc_ticket_hyperlink(cmc_ticket_number),
                                                                               cmcTicketStatus=cmc_ticket_status)
    return extra_text

def generate_item_content(oz_ticket, cmc_ticket):
    oz_ticket_key = oz_ticket.key
    oz_ticket_summary = oz_ticket.fields.summary
    oz_ticket_status_name=oz_ticket.fields.status.name.encode('utf-8').strip()
    oz_ticket_status = oz_jira_status_map[oz_ticket_status_name] if oz_jira_status_map.has_key(oz_ticket_status_name) else oz_ticket_status_name
    oz_ticket_developer_full_name = oz_ticket.fields.assignee
    oz_ticket_qaReporter_full_name = oz_ticket.fields.customfield_10320 or na_text
    wraped_oz_ticket_status = get_wraped_ticket_status(oz_ticket_status)

    cmc_ticket_key=cmc_ticket.key
    cmc_ticket_status=cmc_ticket.fields.status.name
    wraped_cmc_ticket_status = get_wraped_ticket_status(cmc_ticket_status)

    author = html_templates['author_info_tpl'].format(author=oz_ticket_developer_full_name)
    qa_reporter = html_templates['qa_report_info_tpl'].format(qaReporter=oz_ticket_qaReporter_full_name)
    cmc_ticket_text = html_templates['cmc_ticket_info_tpl'].format(hyperLink=cmc_jira_url_prefix % cmc_ticket_key, 
                                                                   hyperText=cmc_ticket_key, 
                                                                   ticketStatus=wraped_cmc_ticket_status)
    oz_ticket_text = html_templates['oz_ticket_info_tpl'].format(hyperLink=oz_jira_url_prefix % oz_ticket_key,
                                                                 hyperText=oz_ticket_key, 
                                                                 ticketStatus=wraped_oz_ticket_status,
                                                                 author=author, 
                                                                 qaReporter=qa_reporter)
    extra_info_text = html_templates['extra_info_tpl'].format(extraInfo=get_extra_info(oz_ticket_status, 
                                                                                       cmc_ticket_status, 
                                                                                       author,
                                                                                       qa_reporter,
                                                                                       oz_ticket_key,
                                                                                       cmc_ticket_key))
    item_html_text = html_templates['item_content_tpl'].format(ozTicketInfo=oz_ticket_text, 
                                                               cmcTicketInfo=cmc_ticket_text, 
                                                               ticketSummary=oz_ticket_summary,
                                                               extraInfo=extra_info_text)
    return item_html_text

def get_html_content(totalCount, label, item_html_text, none_qa_reporter_html_text, tickets_status_statistics_html_text):
    return html_templates['email_content_tpl'] % (totalCount, label, item_html_text, 
                                                  none_qa_reporter_html_text, 
                                                  tickets_status_statistics_html_text)

def check_tickets_main(search_label):
    """
    Check OZ side CMC Tickets which status in (Resolved and Closed) on CMC Side status
    If OZ side ticket was Resolved but CMC Side this ticket was Rejected, ReOpen or other status
    Will send an email to developer to let the developer to known

    customfield_10320: QA Reporter
    """

    not_assign_qa_reporter_tickets = []
    all_items_html_text = ''
    none_qa_reporter_html_text=''
    tickets_status_statistics_html_text=''
    oz_jira_ins = connect_jira(oz_jira_server, oz_jira_username, oz_jira_password)
    cmc_jira_ins = connect_jira(cmc_jira_server, cmc_jira_username, cmc_jira_password, True)

    search_label = search_label if search_label else get_last_work_day_label()

    query_jql = oz_fixed_tickets_query % search_label
    oz_side_fixed_cmc_tickets = oz_jira_ins.search_issues(query_jql)
    total_tickets_count = oz_side_fixed_cmc_tickets.__len__()
    log.info("There are %s cmc tickets on %s were [Fixed]", total_tickets_count, search_label)

    idx = 0
    for oz_ticket in oz_side_fixed_cmc_tickets:
        idx = idx + 1
        log.info("Processing %s/%s.....", idx, total_tickets_count)
        oz_ticket_key = oz_ticket.key
        cmc_ticket_key = get_cmc_ticket_number(oz_ticket)
        cmc_ticket = cmc_jira_ins.issue(str(cmc_ticket_key))
        oz_developer_email_addr = oz_ticket.fields.assignee.emailAddress
        oz_qa_email_addr = oz_ticket.fields.customfield_10320.emailAddress if oz_ticket.fields.customfield_10320 else None

        # generate the item text
        all_items_html_text = all_items_html_text + generate_item_content(oz_ticket, cmc_ticket)


        # which developer and qa reporter will receive this email
        if oz_developer_email_addr:
            mail_to_set.add(oz_developer_email_addr)
        if oz_qa_email_addr:
            mail_to_set.add(oz_qa_email_addr)
        else:
            # append none assign the qa reporter to array.
            not_assign_qa_reporter_tickets.append(generate_oz_ticket_hyperlink(oz_ticket_key))


    # generate warning qa manager which tickets not assign qa reporter.
    if not_assign_qa_reporter_tickets.__len__() > 0:
        qa_manager_names = html_templates['qa_report_info_tpl'].format(qaReporter='@'+qa_manager_full_names)
        none_qa_reporter_html_text = html_templates['not_assign_qa_reporter_tpl'].format(qaManager=qa_manager_names, 
                                                            count=not_assign_qa_reporter_tickets.__len__(),
                                                            tickets=', '.join(not_assign_qa_reporter_tickets))

    if rejected_ticket_by_cmc_qa.__len__() > 0:
        tickets_status_statistics_html_text = html_templates['rejected_tickets_statistics_tpl'].format(ticketStatus=jira_ticket_status_styles['rejected-qa'],
                                                                                                       count=rejected_ticket_by_cmc_qa.__len__(),
                                                                                                       tickets=', '.join(rejected_ticket_by_cmc_qa))
        try:

            file_content = html_templates['statistics_file_content_tpl'].format(ticketStatus=statistics_jira_status['rejected'],
                                                                                date=today, 
                                                                                label=search_label, 
                                                                                tickets = ', '.join(rejected_ticket_by_cmc_qa_only_ticket_numbers))
            write_to_file(rejected_ticket_file_name, file_content)
        except:
            log.error('Write the rejected content error.')

    # Generate the html content (email content)
    html_content = get_html_content(total_tickets_count, search_label, all_items_html_text, 
                                    none_qa_reporter_html_text, tickets_status_statistics_html_text)


    # write to file
    # filename = os.path.join(get_work_path(output_dir), 'cmc_ticket_tracker_%s.html' % today)
    # fout = open(filename, 'wr')
    # fout.write(html_content)
    # fout.flush()
    # print filename

    # send email
    send_email(html_content)

def write_to_file(filename, content, write_mode='a'):
    filename_with_path = os.path.join(get_work_path(output_dir), filename)
    with open(filename_with_path, write_mode) as fp:
        fp.write(content)


def send_email(html_content):
    try:
        if not html_content or html_content == None or html_content == '':
            log.warn('No email content to send, ignore send email')
            return

        log.info("Ready send email.....")

        email_tos = list(mail_to_set)
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

    search_label = None
    if parameters.__len__() > 0:
         search_label = parameters[0]

    check_tickets_main(search_label)

    exit()