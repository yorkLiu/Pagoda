#!/usr/bin/env python
# -*- coding: UTF-8 -*- 
from jira import JIRA
import logging
# from datetime import date, timedelta
import sys, getpass
import datetime
import re
import requests
from jira.client import GreenHopper


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
        jira_option = {'server': jira_server, 'verify': True}
        jira = JIRA(jira_option, basic_auth={jira_user, jira_password})
        log.info("Connected server: %s successfully!", jira_server)
        return jira
    except Exception, e:
        log.error("Failed connect JIRA server (%s)", jira_server)
        return None



def create_oz_versions(cookies):
    r = requests.post('http://192.168.168.21:8091/rest/api/2/version', json={'name': 'test_create_version_01', 'project': 'CMC'}, cookies=cookies)
    print r.status_code == 201
    print r.text


def get_oz_versions(oz_jira_username, oz_jira_password):
    response = requests.get('http://192.168.168.21:8091/rest/api/2/project/CMC/versions', auth=(oz_jira_username, oz_jira_password))
    cookies = response.cookies

    jsonArray =  response.json()
    versions = []
    for json in jsonArray:
        versions.append(json['name'])


    create_oz_versions(cookies)
    
    print versions

# def get_active_srpint(oz_jira_username, oz_jira_password, board_id):
#     response = requests.get('http://192.168.168.21:8091/rest/greenhopper/latest/sprintquery/6?includeHistoricSprints=false&includeFutureSprints=true', auth=(oz_jira_username, oz_jira_password))
#     jsonArray =  response.json()['sprints']
#     current_sprint={}
#     for json in jsonArray:
#         if json['state'] == u'ACTIVE':
#             current_sprint['id'] = json['id']
#             current_sprint['name'] = json['name']
#     return current_sprint


def get_active_srpint(oz_jira_username, oz_jira_pwd, board_id):
    api_url = oz_jira_server + '/rest/greenhopper/latest/sprintquery/%s?includeHistoricSprints=false&includeFutureSprints=true' % str(board_id)
    print api_url
    response = requests.get(api_url, auth=(oz_jira_username, oz_jira_pwd))
    json =  response.json()
    sprints = json['sprints'] if json['sprints'] else None
    current_sprint={}
    print sprints
    if sprints:
        for sprint in sprints:
            if sprint['state']== u'ACTIVE':
                print sprint['id'], sprint['name']
                current_sprint['id'] = sprint['id']
                current_sprint['name'] = sprint['name']
                break
    log.info("Current Active Sprint: '%s'", current_sprint)
    return current_sprint
    
def get_sprint_name_from_ticket(sprint_text):
    sprint_map={}
    if sprint_text and not str(sprint_text).isspace():
        pattern = re.compile(r'\[.{0,}\]$')
        r2 = re.findall(pattern, sprint_text)[0]
        r2 = r2.replace('[', '').replace(']', '')
        items = r2.split(',')
        for item in items:
            key_value=item.split('=')
            key = str(key_value[0]).strip()
            val = str(key_value[1]).strip()
            if key:
                sprint_map[key] = val
    print sprint_map               
    return sprint_map['name'] if sprint_map.has_key('name') else None


if __name__ == '__main__':
    username = "builder"
    password = "builder$123"   
    
    oz_jira_search_query = 'project = "CMC JIRA Tickets" AND summary ~ %s AND NOT issuetype=Sub-task'
    oz_jira = connect_jira(oz_jira_server, username, password)
    
    
    issue = oz_jira.issue('CMC-3436')

    # issue2 = oz_jira.issue('CMC-3442')


    # oz_jira.create_issue_link('Relates', 'CMC-3436', 'CMC-3442')

    oz_jira.add_issues_to_epic('CMC-3436',['CMC-3442'] )




    # print oz_jira.issue_type_by_name('relates to')
    # for i in oz_jira.create_issue_link():
    # print oz_jira.issue_link_types()
    # for type in oz_jira.issue_link_types():
    #     print type
    # for i in oz_jira.issue_link_types():
    #     print i.name, i.id

    # active_sprint = get_active_srpint(username, password, 6)
    # print active_sprint
    # issue.update(fields={"customfield_10021": str(active_sprint['id'])})
    #
    # for i in issue.fields.customfield_10021:
    #     print i
    #     print get_sprint_name_from_ticket(i)

    # create_oz_versions(username, password)
    # get_oz_versions(username, password)

    

    # options = {
    #     'server': oz_jira_server}
    # gh = GreenHopper(options, basic_auth=(username, password))
    # boards = gh.boards()
    # for board in boards:
    #     print board.name, board.id
    # 
    # 
    # sprints = gh.sprints(6, state='ACTIVE')
    # print "------sprints", sprints.__len__()
    # for sprint in sprints:
    #     print sprint.name, sprint.id, sprint.raw['state']
    #     # if sprint.raw['state'] == u'ACTIVE':
    #     #     print sprint.name, sprint.id
    # print sprints
    # 
    # oz_sprint = get_sprint_name_from_ticket(issue.fields.customfield_10021[0] if issue.fields.customfield_10021 else None)        
    # if oz_sprint == None or str(oz_sprint).isspace():
    #     log.info("---update...")
    #     issue.update(fields={"customfield_10021": '3'})
    # # issue.set_field_value('customfield_10021', '3')
    #     
    # print issue.fields.customfield_10321       
    # print issue.fields.customfield_10322

    # for sprint in sorted([s for s in sprints
    #                       if s.raw[u'state'] == u'ACTIVE'],
    #                      key = lambda x: x.raw[u'sequence']):
    #     milestone_str = str(sprint)
    #     
    #     print milestone_str
        # issues = sprint_issues(gh, board.id, sprint.id)
        # for issue in issues:
        #     issues_str += fmt_issues.format(issue.key, issue.summary)
    
    
    # issue.update(fields={'versions': [{'name': 'nv_test_branch'}]})
    
    # print issue.fields.comment.comments

    # print issue.fields.assignee.emailAddress
    # print issue.fields.assignee.raw
    # 
    # # cmcagile.com
    # al_comment = [comment for comment in issue.fields.comment.comments
    #  if re.search(r'@ozstrategy.com$', comment.author.emailAddress)]
    # 
    # print al_comment[al_comment.__len__()-1].body
    # 
    # for c in al_comment:
    #     print c.author
    #     print c.body

    # for attachment in issue.fields.attachment:
    #     print attachment, '---', attachment.get().__len__()/1024
    
    # print issue.fields
    # 
    # print issue.fields.assignee, issue.fields.status.name
    # oz_jira.add_comment(issue, 'Test comment \n new line')
    
    # status = issue.fields.status.name
    # print status
    # print issue.fields.customfield_10228
    # trans = oz_jira.transitions(issue)
    # print [(t['id'], t['name']) for t in trans]
    # 
    # for t in trans:
    #     print t['id'], t['name']
        
        
    # oz_jira.transition_issue(issue, 3)   
    # 
    # 
    # print issue.fields.issuetype.name
    # issue.update(fields={"duedate": str(date.today())})
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
    
    
    