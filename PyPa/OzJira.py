#!/usr/bin/env python
# -*- coding: UTF-8 -*- 
from jira import JIRA
import logging
from datetime import date, timedelta
import sys, getpass


log = logging.getLogger('JIRA')
log.setLevel(logging.INFO)
fmt = logging.Formatter('%(levelname)s:%(name)s:%(message)s')
h = logging.StreamHandler()
h.setFormatter(fmt)
log.addHandler(h)

oz_jira_server = 'http://192.168.168.21:8091'

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
    
if __name__ == '__main__':
    username = ""
    password = ""
    oz_jira = connect_jira(oz_jira_server, username, password)
   
    issue = oz_jira.issue('CMC-2854')
    status = issue.fields.status.name
    trans = oz_jira.transitions(issue)
    print [(t['id'], t['name']) for t in trans]

    for t in trans:
        print t['id'], t['name']


    print issue.fields.issuetype.name
    issue.update(fields={"duedate": str(date.today())})
    # issue.update(fields={"duedate": '5/四月/17'})
    # 
    # print status
    # dateFormat='%Y-%m-%d'
    # today = datetime.datetime.now()
    # label = today.strftime(dateFormat)
    # print label
    # 
    # print issue.fields.duedate

    # for field_name in issue.raw['fields']:
    #     print "Field:", field_name, "Value:", issue.raw['fields'][field_name]
    
    # issue.add_field_value('labels', '20170315')
    
    # issue.update('duedate', label)
    # # oz_jira.transition_issue(issue, 761, fields={'duedate': label})
    # issue.update(fields={"duedate":{'value':label}})
    
    # tickets = ['CMC-2701', 'CMC-2698', 'CMC-2697', 'CMC-2696', 'CMC-2695', 'CMC-2694', 'CMC-2693']
    # 
    # for ticket in tickets:
    #     issue = oz_jira.issue(ticket)
    #     status = issue.fields.status.name
    #     print status.lower()
    #     
    #     if 'Design' == status:
    #         oz_jira.transition_issue(issue, 711)
    #         oz_jira.transition_issue(issue, 741)
    #         oz_jira.transition_issue(issue, 761)
    #     elif 'Require Estimation' == status:
    #         print '----'
    #         oz_jira.transition_issue(issue, 741)
    #         oz_jira.transition_issue(issue, 761)
    
    
    