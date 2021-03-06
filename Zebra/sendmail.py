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

dateFormat = '%Y-%m-%d'
now = datetime.datetime.now()
today = now.strftime(dateFormat)

### START config Email Sender
email_username = 'pagodasupport@sina.com'
email_password = 'UFByckAjJDQ1Njck'
email_from = 'pagodasupport@sina.com'
email_subject = 'Test for RWD Email'

# config the email address here
email_tos = '441553179@qq.com,879165403@qq.com'.split(',')
email_ccs = 'yong.liu@ozstrategy.com'.split(',')
### END config Email Sender


log = logging.getLogger(__name__)

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
        if (not email_file_name or email_file_name == None or email_file_name == '') or not os.path.isfile(
                email_file_name):
            log.warn('No file to read, ignore send email')
            return

        need_converter_email_content = not str(email_file_name).upper().endswith("HTML")

        with open(email_file_name) as fp:
            text_content = fp.read()
            if need_converter_email_content:
                text_content = text_content.replace('\n', '</br>')
                text_content = html_template % text_content
            mail_content = MIMEText(text_content, 'html')

        log.info("Ready send email.....")

        de_email_pwd = base64.decodestring(email_password)

        mail_content['Subject'] = email_subject
        mail_content['From'] = email_from
        mail_content['To'] = ','.join(email_tos)
        mail_content['Cc'] = ','.join(email_ccs)

        server = smtplib.SMTP('smtp.sina.com:25')
        server.ehlo()
        server.starttls()
        server.login(email_username, de_email_pwd)
        server.sendmail(email_from, email_tos, mail_content.as_string())
        server.quit()
        log.info('Send email successfully')

    except Exception as e:
        log.error("Send Email Error")
        log.error(e)


if __name__ == '__main__':
    parameters = sys.argv[1:]

    email_file_name = None
    if parameters.__len__() > 0:
        email_file_name = parameters[0]
        send_email()

    exit()