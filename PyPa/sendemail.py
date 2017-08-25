#!/usr/bin/env python
# -*- coding: UTF-8 -*- 
import os
import sys
import datetime
import logging
import base64
import smtplib
from email.mime.text import MIMEText

reload(sys)
sys.setdefaultencoding("utf8")

dateFormat='%Y-%m-%d'
now = datetime.datetime.now()
today = now.strftime(dateFormat)

### START config Email Sender
email_username=os.environ.get('EMAIL_USER_NAME')
email_password=os.environ.get('EMAIL_PWD')
email_from= os.environ.get('EMAIL_FROM')
email_subject= os.environ.get('EMAIL_SUBJECT')

# config the email address here
email_tos=os.environ.get('EMAIL_TO_TECH_LEADS').split(',')
email_ccs=os.environ.get('EMAIL_CCS').split(',')
### END config Email Sender


log = logging.getLogger('JIRA')
log.setLevel(logging.INFO)
fmt = logging.Formatter('%(levelname)s:%(name)s:%(message)s')
h = logging.StreamHandler()
h.setFormatter(fmt)
log.addHandler(h)

html_template="""\
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
                %s

                <div>
                  <hr/>
                  <div style="font-size: 10px; color:gray;">
                  <div>This email template generated by Create/Update Automatic</div>
                  <div>Create/Update Automatic job will create the ticket(s) which on CMC Dashboard but not in OZ JIRA Server. </div>
                  <div>And also will update and append today's label for previous work day not Fixed ticket which on OZ JIRA Server</div>
                  <div>Create/Update Automatic job will trigger at 8:30 am on Mon - Fri</div>
                  </div>
                </div>
              </body>
    </html>

"""

def send_email():
    try:
        if (not email_file_name or email_file_name == None or email_file_name == '') or not os.path.isfile(email_file_name):
            log.warn('No file to read, ignore send email')
            return

        need_converter_email_content= not str(email_file_name).upper().endswith("HTML")

        with open(email_file_name) as fp:
            text_content = fp.read()
            if need_converter_email_content:
                text_content = text_content.replace('\n','</br>')
                text_content = html_template % text_content
            mail_content = MIMEText(text_content, 'html')

        log.info("Ready send email.....")

        de_email_pwd = base64.decodestring(email_password)

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
    
    email_file_name = None
    if parameters.__len__() > 0:
        email_file_name = parameters[0]
        send_email()

    exit()