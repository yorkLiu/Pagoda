#!/usr/bin/env python
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

reload(sys)
sys.setdefaultencoding("utf8")

############## pip install tools###################
# pip install JIRA
# pip install loggin
# pip install requests
# pip install requests[socks]  or pip instlal PySocks
############## pip install tools###################


oz_jira_rapid_board_id=21

'''
Log Configuration
'''
log = logging.getLogger('JIRA')
log.setLevel(logging.INFO)
fmt = logging.Formatter('%(levelname)s:%(name)s:%(message)s')
h = logging.StreamHandler()
h.setFormatter(fmt)
log.addHandler(h)

'''
Label
'''
dateFormat='%Y%m%d'
today = date.today()
label = today.strftime(dateFormat)

'''
Content file name configuration
'''
content_file_path='Tickets/'
content_file_name='{label}.txt'
file_content_template='http://192.168.168.21:8091/browse/{ozTicketNo} ({cmcTicketNo})'

'''
OZ JIRA Server
'''
oz_jira_server = 'http://192.168.168.21:8091'
cmc_jira_server = 'https://jira.cmcassist.com'

cmc_jira_epic_api=cmc_jira_server + '/rest/api/2/search?jql=cf[11264]={key}&fields=summary'

oz_jira_search_query = 'project = "CMC JIRA Tickets" AND summary ~ %s'
oz_jira_yesterday_not_resolved_query='project = "CMC JIRA Tickets" AND labels=%s AND status not in(Resolved, Closed, "Passed QA", Done)'
oz_jira_env_text = 'https://jira.cmcassist.com/browse/{cmcTicketNo}\n branch: {branch}'
oz_jira_cmc_jira_link = 'https://jira.cmcassist.com/browse/{cmcTicketNo}'
oz_jira_get_cmc_project_all_versions_api=oz_jira_server + '/rest/api/2/project/CMC/versions'
oz_jira_create_cmc_project_version_api=oz_jira_server + '/rest/api/2/version'

use_proxy=os.environ.get('USE_PROXY')
proxy_ip = os.environ.get('PROXY_SERVER')

'''
Ticket Status Map
'''
status_map = {
    'ApproveDesign': 711,
    'ApproveDev': 741,
    'OpenDev': 761,
    'ReOpen': 3,
    'ResolveIssue': 771
}

messages={
    'updated': 'Updated',
    'updateFailed': 'Update Failed',
    'synchronized': 'Cmc was updated the ticket %s, we have synchronized'
}

ticket_updated_comment = '[~%s] %s \n %s'

# proxies = {
#     'http': 'socks5://192.168.168.3:1083',
#     'https': 'socks5://192.168.168.3:1083'
# }

proxies = None
if use_proxy and str(use_proxy).upper() in ('YES', 'TRUE'):
    proxies = {
                'http': str(proxy_ip).strip(),
                'https': str(proxy_ip).strip()
              }


do_not_append_sprint_status_contains=['PM', 'PENDING']

cmc_ticket_assign_to_us_usernames=['ozdev', 'ozintel']

ignore_linked_tickets_prefix=['CTS']

# CMC ticket assign to who
# for example all CA tickets assign to 'yliu'
# all TD tickets assign to 'cldu'
tickets_automatic_assignments={
    'TD': 'njli',
    'CA': 'yliu'
    # 'CBA': 'cldu',
    # 'UCO': 'cldu',
    # 'SMW': 'wtang',
    # 'SMB': 'wtang',
    # 'GRM': 'wtang',
    # 'FLEX': 'swu',
    # 'NFS': 'swu'
}

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

def create_new_version(version_name, cookies):
    if version_name and not str(version_name).isspace() and cookies:
        r = requests.post(oz_jira_create_cmc_project_version_api, json={'name': str(version_name).strip(), 'project': 'CMC'}, cookies=cookies)
        if r.status_code != 201:
            log.error('Create version [%s] error.', version_name)
            log.error(r.text)

def check_oz_jira_versions(cmc_software_branch_names, is_create_new_version=True):
    """
    Check the OZ versions contains cmc_software_branch_names (array)
    if cmc_software_branch_names' item not in OZ Versions Then create a new version 

    ::param oz_jira_username oz jira username
    ::param oz_jira_password oz jira password
    ::param cmc_software_branch_names the cmc ticket 'Software Branches' names
    ::param is_create_new_version (True or False), if set to 'False' will not create new version
    """
    response = requests.get(oz_jira_get_cmc_project_all_versions_api, auth=(oz_jira_username, oz_jira_pwd))
    cookies = response.cookies
    jsonArray =  response.json()
    current_versions = []
    for json in jsonArray:
        current_versions.append(json['name'])

    # Check the version is in current_version
    # if not exists then create it.
    for version in cmc_software_branch_names:
        if version not in current_versions and is_create_new_version:
            create_new_version(version, cookies)


def get_current_oz_sprint():
    """
    Get current 'Active' sprint for oz jira
    """
    # current_sprint={}
    # options = {'server': oz_jira_server}
    # gh = GreenHopper(options, basic_auth=(oz_jira_username, oz_jira_pwd))
    # sprints = gh.sprints(oz_jira_rapid_board_id)
    # for sprint in sprints:
    #     if sprint.raw['state'] == u'ACTIVE':
    #         current_sprint['id'] = sprint.id
    #         current_sprint['name'] = sprint.name
    # 
    # return current_sprint

    return get_active_srpint(oz_jira_rapid_board_id)

def get_active_srpint(board_id):
    api_url = oz_jira_server + '/rest/greenhopper/latest/sprintquery/%s?includeHistoricSprints=false&includeFutureSprints=true' % str(board_id)
    response = requests.get(api_url, auth=(oz_jira_username, oz_jira_pwd))
    json =  response.json()
    sprints = json['sprints'] if json['sprints'] else None
    current_sprint={}
    if sprints:
        for sprint in sprints:
            if sprint['state']== u'ACTIVE':
                current_sprint['id'] = sprint['id']
                current_sprint['name'] = sprint['name']
                break
    log.info("Current Active Sprint: '%s'", current_sprint)
    return current_sprint

def append_label(oz_jira, issue, workDir, cmcTicketNo, oz_issue_key):
    try:
        extra_info=''
        cmc_issue = cmc_jira.issue(cmcTicketNo)
        cmc_issue_status = cmc_issue.fields.status.name
        p_cmc_ticket_status = str(cmc_issue_status).replace('(', '').replace(')', '').replace(' ', '-').strip().upper()
        # do_reopen_flag= not ("PASSED" in p_cmc_ticket_status or p_cmc_ticket_status in ('CLOSED', 'RESOLVED', 'QA-COMPLETE', 'DEV-COMPLETE', 'IN-PROGRESS-QA', 'IN-QA'))
        do_reopen_flag= "REJECTED" in p_cmc_ticket_status
        append_today_label_flag= not ("PENDING" in p_cmc_ticket_status or "PASSED" in p_cmc_ticket_status or p_cmc_ticket_status in ('CLOSED', 'RESOLVED', 'QA-COMPLETE', 'DEV-COMPLETE', 'IN-PROGRESS-QA', 'IN-QA', 'OPEN-PM'))

        # append today's label to this issue.
        ticket_status = issue.fields.status.name
        log.info("%s current status is: [%s]", oz_issue_key, ticket_status)
        if label not in issue.fields.labels:

            if do_reopen_flag and ticket_status in ('关闭', 'Closed', 'Resolved', 'Passed QA'):
                if ticket_status in ('Passed QA'):
                    oz_jira.transition_issue(issue, status_map['ResolveIssue'])
                oz_jira.transition_issue(issue, status_map['ReOpen'])
                log.info("ReOpened [%s]", oz_issue_key)

            if append_today_label_flag:
                issue.add_field_value('labels',label)
                # issue.fields.labels.append(u'%s' %label)
                # issue.update(fields={"labels": issue.fields.labels})
                log.info(".........[%s] appended label: %s successfully.........", oz_issue_key, label)
            else:
                extra_info = " but not append today's label because oz ticket status is [%s] and cmc ticket status is [%s]" % (ticket_status, cmc_issue_status)
        else:
            log.info(".........[%s] already has label: %s.........", oz_issue_key, label)

        # write to file
        write_main_content_to_file(workDir, cmcTicketNo, oz_issue_key, messages['updated'] + extra_info)
    except Exception, e:
        log.error(">>>>>>>>Append label: [%s] %s failed<<<<<<<<", oz_issue_key, label)
        # write to file
        write_main_content_to_file(workDir, cmcTicketNo, oz_issue_key, messages['updateFailed'] + ', status is "%s"' % ticket_status)
        log.error(e)

def create_update_ticket_on_oz_side_with_linked_epic(oz_jira, cmc_jira, cmcTicketNo, workDir,
                                                     isCreateNewIfNotExists=True, forceUpdate=False, createLinked=False, createEpic=False):
    linkedIssue = {}
    epicIssues = []
    if createLinked:
        linkedIssueObj = getLinkedIssue(cmcTicketNo)

        if linkedIssueObj:
            inwardIssues = []
            outwardIssues = []
            if 'inwardIssues' in linkedIssueObj:
                print 'Linked Inward:'
                for key in linkedIssueObj['inwardIssues']:
                    print key
                    inwardIssues.append(create_update_ticket_on_oz_side(oz_jira, cmc_jira, key, workDir, isCreateNewIfNotExists,
                                                    forceUpdate))

            if 'outwardIssues' in linkedIssueObj:
                print 'Linked Outward:'
                for key in linkedIssueObj['outwardIssues']:
                    print key
                    outwardIssues.append(create_update_ticket_on_oz_side(oz_jira, cmc_jira, key, workDir, isCreateNewIfNotExists,
                                                    forceUpdate))
            if inwardIssues:
                linkedIssue['inwardIssues'] = inwardIssues
            if outwardIssues:
                linkedIssue['outwardIssues'] = outwardIssues

    if createEpic:
        print 'Epic Issues:'
        for key in getEpicIssue(cmcTicketNo):
            print key
            epicIssues.append(create_update_ticket_on_oz_side(oz_jira, cmc_jira, key, workDir, isCreateNewIfNotExists,
                                            forceUpdate))

    # Create the parent issue
    create_update_ticket_on_oz_side(oz_jira, cmc_jira, cmcTicketNo, workDir, isCreateNewIfNotExists, forceUpdate, linkedIssue, epicIssues)


# Find cmc ticket NO in OZ JIRA server
# if found, then update the label to today
# else return false
def create_update_ticket_on_oz_side(oz_jira, cmc_jira, cmcTicketNo, workDir, isCreateNewIfNotExists=True, forceUpdate=False, linkedIssue=None, epicIssues=None):
    oz_issue_key = None
    log.info("Search Ticket NO. %s", cmcTicketNo)
    issues = oz_jira.search_issues(oz_jira_search_query %cmcTicketNo)
    count = issues.__len__()
    if count > 0:

        # Means this @cmcTicketNo has exists in Oz side
        # And need append today's label(I.E 20170101...)
        issue = issues[0]
        oz_issue_key = issue.key
        oz_issue_status_name = issue.fields.status.name
        log.info("Found %s --> %s" ,cmcTicketNo ,oz_issue_key)

        log.info("Ready append label %s to %s(%s)", label, oz_issue_key, cmcTicketNo)
        append_label(oz_jira, issue, workDir, cmcTicketNo, oz_issue_key)
        if forceUpdate and oz_issue_status_name not in('Closed', '关闭'):
        # if forceUpdate:
            # update oz ticket's summary, description, attachments and branches
           forceUpdateOzTicket(oz_jira, cmc_jira, oz_issue_key, cmcTicketNo, workDir, linkedIssue, epicIssues)

    else:
        # not found @cmcTicketNo in Oz Jira Server
        # If @isCreateNewIfNotExists == True
        # then create the cmc ticket in oz jira server side
        if isCreateNewIfNotExists == True:
            log.info("Not found Cmc Jira Ticket: %s, will create it.", cmcTicketNo)
            oz_issue_key = create_ticket_on_oz_side(oz_jira, cmc_jira, cmcTicketNo, workDir, linkedIssue, epicIssues)
        else:
            log.info("Not found Cmc Jira Ticket: %s and will not create it.", cmcTicketNo)

    return oz_issue_key


def get_md5(value):
    """
    Caculate the value's md5
    """
    value = str(value)
    if value and not value.isspace():
        return hashlib.md5(value).hexdigest()
    return None

def isEquals(value1, value2):
    return get_md5(value1) == get_md5(value2)


def download_diff_attachments(workDir, cmcTicketNo, cmc_attachments, oz_exists_attachment_file_names):
    attachs = []
    workPath = get_work_path(workDir, cmcTicketNo)
    for attachment in cmc_attachments:
        attachment_file_name = attachment.filename
        if attachment_file_name not in oz_exists_attachment_file_names:
            log.info("Downloading attachment %s", attachment_file_name)
            filename = os.path.join(workPath, attachment_file_name)
            fout = open(filename, "wb")
            fout.write(attachment.get())
            attachs.append(filename)
            log.info("Attachment downloaded on: %s", filename)


    return attachs

def get_array_hash(array):
    """
    get the array hash code
    """
    result = 0
    if array:
        for v in array:
            result = result + hash(str(v))
    return result

def get_diff(original_text, newest_text, cmc_ticket_num, oz_ticket_last_update_datetime):
    """
        get different text with @original_text and @newest_text
        :param original_text oz ticket description
        :param newest_text cmc ticket description
        :param oz_ticket_last_update_datetime the oz ticket last updated date time
    """
    last_update_datetime = datetime.strptime(oz_ticket_last_update_datetime.split('+')[0], '%Y-%m-%dT%H:%M:%S.%f').strftime('%Y-%m-%d %H:%M:%S')
    d = difflib.unified_diff(original_text.splitlines(), newest_text.splitlines(),
                             u'OZ Ticket Updated on {0}'.format(last_update_datetime),
                             u'{0} Description (modified)'.format('[%s|%s]' % (cmc_ticket_num, oz_jira_cmc_jira_link.format(cmcTicketNo=cmc_ticket_num) )), n=1, lineterm=u'\n')
    diffs = list(d)
    if diffs.__len__() > 0:
        diffs_processed=[]
        for diff in diffs:
            if str(diff).startswith("+") and not str(diff).startswith("++"):
                diffs_processed.append(str(diff).replace("+", "(+) ", 1))
            elif str(diff).startswith("-") and not str(diff).startswith("--"):
                diffs_processed.append(str(diff).replace("-", "(-) ", 1))
            else:
                diffs_processed.append(diff)

        diff = u'\n'.join(diffs_processed)+'\n'
        return diff

    return None

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
    return sprint_map['name'] if sprint_map.has_key('name') else None 

def get_oz_affect_versions(cmc_issue_software_branch_names):
    affected_versions = []
    if cmc_issue_software_branch_names and cmc_issue_software_branch_names.__len__() > 0:
        for branch_name in cmc_issue_software_branch_names:
            affected_versions.append({'name': str(branch_name).strip()})

    return affected_versions

def update_issue(issue, fields):
    if issue and fields:
        try:
            issue.update(fields=fields)
        except JIRAError, ex:
            log.error(ex.message, ex)

def update_oz_ticket_extra_info(oz_issue, cmc_issue, linkedIssue=None, epicIssues=None):
    ret_map = {}

    try:
        add_sprint_info = True,
        cmc_issue_status = cmc_issue.fields.status.name
        cmc_issue_type = str(cmc_issue.fields.issuetype.name).upper()
        cmc_issue_sprint = get_sprint_name_from_ticket(cmc_issue.fields.customfield_11263[0] if cmc_issue.fields.customfield_11263 else None)
        cmc_issue_software_branches = cmc_issue.fields.customfield_13450
        cmc_issue_software_branch_names = get_cmc_ticket_software_branches(cmc_issue_software_branches)

        if cmc_issue_type in ('STORY'):
            for t in do_not_append_sprint_status_contains:
                if t.upper() in cmc_issue_status.upper():
                    add_sprint_info = False
                    break

        if oz_issue:
            oz_stored_cmc_status = oz_issue.fields.customfield_10321
            oz_stored_cmc_sprint = oz_issue.fields.customfield_10322
            # oz info
            ret_map['oz_cmc_status'] = oz_stored_cmc_status
            ret_map['oz_cmc_sprint'] = oz_stored_cmc_sprint
            # cmc info
            ret_map['cmc_status'] = cmc_issue_status
            ret_map['cmc_sprint'] = cmc_issue_sprint

            # Check the cmc_issue_software_branch_names is in oz jira versions.
            check_oz_jira_versions(cmc_issue_software_branch_names)

            # "customfield_10321" -> CMC JIRA Status
            # oz_issue.update(fields={"customfield_10321": cmc_issue_status})
            update_issue(oz_issue, fields={"customfield_10321": cmc_issue_status})
            # "customfield_10322" -> CMC JIRA Sprint
            # oz_issue.update(fields={"customfield_10322": cmc_issue_sprint})
            update_issue(oz_issue, fields={"customfield_10322": cmc_issue_sprint})
            # update "Affects Version/s
            # oz_issue.update(fields={"versions": get_oz_affect_versions(cmc_issue_software_branch_names)})
            update_issue(oz_issue, fields={"versions": get_oz_affect_versions(cmc_issue_software_branch_names)})

            # update OZ Sprint [Start]
            if add_sprint_info:
                # "customfield_10021" -> OZ Sprint
                oz_sprint = get_sprint_name_from_ticket(oz_issue.fields.customfield_10021[0] if oz_issue.fields.customfield_10021 else None)
                if oz_sprint == None or str(oz_sprint).isspace():
                    current_sprint_id = current_oz_sprint['id'] if current_oz_sprint.has_key('id') else None
                    print "current_sprint_id:", current_sprint_id
                    if current_sprint_id:
                        # oz_issue.update(fields={"customfield_10021": str(current_sprint_id)})
                        update_issue(oz_issue, fields={"customfield_10021": str(current_sprint_id)})
            # update OZ Sprint [End]
    except JIRAError, e:
        log.info('>>>>Update OZ ticket extra info ERROR>>>>')
        log.error(e)


    ##### added link issue or epic issue
    try:
        if linkedIssue:
            if 'inwardIssues' in linkedIssue:

                for linkKey in linkedIssue['inwardIssues']:
                    log.info("Added the Link issue [%s] to this [%s]", linkKey, oz_issue.key)
                    oz_jira.create_issue_link('Relates', oz_issue.key, linkKey)
            if 'outwardIssues' in linkedIssue:
                for linkKey in linkedIssue['outwardIssues']:
                    log.info("Added the Link issue [%s] to this [%s]", linkKey, oz_issue.key)
                    oz_jira.create_issue_link('Relates', oz_issue.key, linkKey)

        if epicIssues:
            if str(oz_issue.fields.issuetype.name).lower() == 'epic':
                parentEpicIssueKey = oz_issue.key
                log.info("[%s] Linked Epic issues....[%s]", parentEpicIssueKey, ','.join(epicIssues))
                oz_jira.add_issues_to_epic(parentEpicIssueKey, epicIssues)

            else:
                log.warn("Ignore link the Epic issues [%s], because this issue [%s] type not 'Epic', it's '%s'", ','.join(epicIssues), oz_issue.key, oz_issue.type.name)

    except:
        pass

    return ret_map

def forceUpdateOzTicket(oz_jira, cmc_jira, oz_issue_key, cmcTicketNo, workDir,linkedIssue=None, epicIssues=None):
    """
        Update the oz ticket from cmc ticket with comamnd paramenter '-U'
        :param oz_jira: oz jira server
        :param cmc_jira: cmc jira server
        :param oz_issue_key: oz ticket number
        :param cmcTicketNo: cmc ticket number
        :param workDir: the attachment stored path
    """
    try:
        oz_issue = oz_jira.issue(oz_issue_key)
        cmc_issue = cmc_jira.issue(cmcTicketNo)
        oz_issue_exists_attachments = []
        log.info('Update Oz ticket %s [%s]', oz_issue_key, cmcTicketNo)

        for attachment in oz_issue.fields.attachment:
            oz_issue_exists_attachments.append(attachment.filename)

        # download the attachment first
        diff_attachments = download_diff_attachments(workDir, cmcTicketNo, cmc_issue.fields.attachment, oz_issue_exists_attachments)
        # cmc_issue_type = cmc_issue.fields.issuetype.name
        cmc_issue_software_branches = cmc_issue.fields.customfield_13450
        cmc_issue_software_branch_names = get_cmc_ticket_software_branches(cmc_issue_software_branches)
        cmc_issue_summary = cmc_issue.fields.summary
        cmc_issue_description = cmc_issue.fields.description

        oz_issue_software_branches = oz_issue.fields.customfield_10228
        oz_issue_summary = oz_issue.fields.summary.replace(cmcTicketNo, '').lstrip()
        oz_issue_description = oz_issue.fields.description
        oz_issue_last_updated_time=oz_issue.fields.updated

        # update oz_issue extra info
        extra_map = update_oz_ticket_extra_info(oz_issue, cmc_issue, linkedIssue, epicIssues)

        if not oz_issue_software_branches:
            oz_issue_software_branches = ['N/A']

        changed_keys = []
        changed_msgs = []

        if not isEquals(cmc_issue_summary, oz_issue_summary):
            changed_keys.append('Summary')
            changed_msgs.append('*Summary* \nOriginal: %s \n*For Now:* %s \n\n' % (oz_issue_summary, cmc_issue_summary))
            log.info("%s [%s] Cmc summary was changed", oz_issue_key, cmcTicketNo)
            # oz_issue.update(fields={"summary": '%s %s' % (cmcTicketNo, cmc_issue_summary)})
            update_issue(oz_issue,fields={"summary": '%s %s' % (cmcTicketNo, cmc_issue_summary)})

        # if not isEquals(cmc_issue_description, oz_issue_description):
        desc_diff = get_diff(oz_issue_description, cmc_issue_description, cmcTicketNo, oz_issue_last_updated_time)
        if desc_diff != None:
            changed_keys.append('Description')
            changed_msgs.append('*Description* changed, please review the *Acceptance Criteria*\n\n %s \n\n' % desc_diff)
            log.info("%s [%s] Cmc description was changed", oz_issue_key, cmcTicketNo)
            # oz_issue.update(fields={"description": cmc_issue_description})
            update_issue(oz_issue,fields={"description": cmc_issue_description})

        if get_array_hash(cmc_issue_software_branch_names) != get_array_hash(oz_issue_software_branches):
            changed_keys.append('Software Branches')
            changed_msgs.append('*Software Branches* \nOriginal: %s\n*For Now:* %s\n\n' % (','.join(oz_issue_software_branches), ','.join(cmc_issue_software_branch_names)))
            log.info("%s [%s] Cmc software branches was changed", oz_issue_key, cmcTicketNo)
            # oz_issue.update(fields={"customfield_10228": cmc_issue_software_branch_names})
            update_issue(oz_issue,fields={"customfield_10228": cmc_issue_software_branch_names})

        if extra_map.has_key('oz_cmc_status') and extra_map.has_key('cmc_status') and not isEquals(extra_map['oz_cmc_status'], extra_map['cmc_status']):
            changed_keys.append("CMC JIRA Status")
            changed_msgs.append('*CMC JIRA Status* \nOriginal: %s\n*For Now:* %s\n\n' % (extra_map['oz_cmc_status'], extra_map['cmc_status']))

        if extra_map.has_key('oz_cmc_sprint') and extra_map.has_key('cmc_sprint') and not isEquals(extra_map['oz_cmc_sprint'], extra_map['cmc_sprint']):
            changed_keys.append("CMC JIRA Sprint")
            changed_msgs.append('*CMC JIRA Sprint* \nOriginal: %s\n*For Now:* %s\n\n' % (extra_map['oz_cmc_sprint'], extra_map['cmc_sprint']))

        # upload attachment for created issue.
        if diff_attachments and diff_attachments.__len__()>0:
            changed_keys.append("Attachments")
            updated_attachments = ''
            for filename in diff_attachments:
                try:
                    updated_attachments = updated_attachments + '[^ %s]\n' % os.path.basename(filename)
                    oz_jira.add_attachment(issue=oz_issue, attachment=filename)
                except JIRAError, ex:
                    log.error(ex)

            changed_msgs.append('*Attachments Updated* \n%s\n\n' % updated_attachments)

        # write to log file
        if changed_keys and changed_keys.__len__() > 0:
            # add a comment to notice the ticket developer
            oz_jira.add_comment(oz_issue, ticket_updated_comment % (oz_issue.fields.assignee.name, messages['synchronized'] % (','.join(changed_keys)), ''.join(changed_msgs)))
            # write to file
            write_main_content_to_file(workDir, cmcTicketNo, oz_issue_key, messages['synchronized'] % (','.join(changed_keys)))
    except JIRAError as e:
        log.error(e.message, e)

def get_work_path(workDir, cmcTicketNo=None):
    workPath = None
    if workDir and not workDir.isspace():
        workPath = os.path.expanduser(workDir)
    else:
        workPath = os.path.realpath(".")

    if cmcTicketNo:
        # in workDir folder created a folder name with @cmcTicketNo
        # I.E ~/Download/CA-4323
        workPath = os.path.join(workPath, cmcTicketNo)
        if not os.path.exists(workPath):
            log.info("The directory: " + workPath + " was not exists. Will create it.")
            os.makedirs(workPath)
            log.info("Created path: " + workPath + " successfully.")

    return workPath

def download_attachments(workDir, cmcTicketNo, attachments):
    attachs = []
    workPath = get_work_path(workDir, cmcTicketNo)
    for attachment in attachments:
        log.info("Downloading attachment %s", attachment.filename)
        filename = os.path.join(workPath, attachment.filename)
        fout = open(filename, "wb")
        fout.write(attachment.get())
        attachs.append(filename)
        log.info("Attachment downloaded on: %s", filename)

    return attachs

def write_content_to_file(filename, content, writeMode='w'):
    fout = open(filename, writeMode)
    fout.write(content)

def write_main_content_to_file(workDir, cmcTicketNo, ozTicketNo, extraText=None):
    filepath = get_work_path(os.path.join(workDir, content_file_path))
    filename = os.path.join(filepath, content_file_name.format(label=label))
    log.info("Ready write to file: %s", filename)

    if not os.path.exists(filepath):
        os.makedirs(filepath)
    file_content= file_content_template.format(ozTicketNo=ozTicketNo, cmcTicketNo=cmcTicketNo)
    if extraText:
        file_content += "\t" + extraText + "\n"

    # fout = open(filename, 'a')
    # fout.write(file_content+'\n')
    write_content_to_file(filename, file_content, 'a')


def get_cmc_ticket_software_branches(software_branches):
    branch_names = []
    if software_branches and software_branches.__len__() > 0:
        for software_branch in software_branches:
            # branch_names.append(software_branch['value'])
            branch_names.append(software_branch.value)
    else:
        branch_names.append("master")


    log.info('Software Branches: %s', ', '.join(branch_names))
    return branch_names

def create_ticket_on_oz_side(oz_jira, cmc_jira, cmcTicketNo, workDir, linkedIssue=None, epicIssues=None):
    cmc_issue = cmc_jira.issue(cmcTicketNo)

    # download the attachment first
    attachments = download_attachments(workDir, cmcTicketNo, cmc_issue.fields.attachment)
    issue_type = str(cmc_issue.fields.issuetype.name).upper()
    software_branches = cmc_issue.fields.customfield_13450

    summary = "{cmcTicketNo} {summary}".format(cmcTicketNo=cmcTicketNo, summary = cmc_issue.fields.summary)
    environment_text = oz_jira_env_text.format(cmcTicketNo=cmcTicketNo, branch = software_branches)
    issue_type_name = 'Bug'
    branch_names = get_cmc_ticket_software_branches(software_branches)
    labels = []
    # process labels & issue type
    if 'STORY' == issue_type:
        labels = ['Estimate']
        issue_type_name = 'Story'
    elif 'TASK' == issue_type:
        labels = [label]
        issue_type_name = 'Task'
    elif 'EPIC' == issue_type:
        labels = [label]
        issue_type_name = 'Epic'
    else:
        labels = [label]

    assignee_and_reporter = get_assignment_by_ticket(cmcTicketNo)

    issue_dic = {
        # 'project': {'key': 'CMC JIRA Tickets'},
        'project': {'id': 10190},
        'summary': summary,
        'description': cmc_issue.fields.description,
        'issuetype': {'name': issue_type_name},
        'labels': labels,
         # customfield_10227 is CMC JIRA Link
        'customfield_10227': oz_jira_cmc_jira_link.format(cmcTicketNo=cmcTicketNo),
         # customfield_10228 is Software Branches
        'customfield_10228': branch_names,
        'assignee': {'name': assignee_and_reporter},
        'reporter': {'name': assignee_and_reporter}
        # 'assignee': {'name': oz_jira_default_assignee_reporter},
        # 'reporter': {'name': oz_jira_default_assignee_reporter}
        # 'environment': oz_jira_env_text.format(cmcTicketNo=cmcTicketNo, branch=', '.join(branch_names))
    }

    if issue_type_name == 'Epic':
        issue_dic['customfield_10023'] = 'Epic for %s' % cmcTicketNo

    new_issue = oz_jira.create_issue(fields=issue_dic)

    # update OZ ticket extra info
    update_oz_ticket_extra_info(new_issue, cmc_issue, linkedIssue, epicIssues)

    # update ticket's status (bug: Open Dev, story: Require Estimation
    new_issue_status = new_issue.fields.status.name.lower()
    if 'Bug' == issue_type_name:

        # update the duedate to 'today'
        new_issue.update(fields={"duedate": str(today)})

        if 'design' == new_issue_status:
            oz_jira.transition_issue(new_issue, status_map['ApproveDesign'])
            oz_jira.transition_issue(new_issue, status_map['ApproveDev'])
            oz_jira.transition_issue(new_issue, status_map['OpenDev'])
        elif 'require estimation' == new_issue_status:
            oz_jira.transition_issue(new_issue, status_map['ApproveDev'])
            oz_jira.transition_issue(new_issue, status_map['OpenDev'])
    elif 'Story' == issue_type_name:
        if 'design' == new_issue_status:
            oz_jira.transition_issue(new_issue, status_map['ApproveDesign'])

    # upload attachment for created issue.
    if attachments and attachments.__len__()>0:
        for filename in attachments:
            oz_jira.add_attachment(issue=new_issue, attachment=filename)

    log.info("Create Ticket successfully: %s (%s)", new_issue.key, cmcTicketNo)

    # write to file
    write_main_content_to_file(workDir, cmcTicketNo, new_issue.key, issue_type_name)

    # for field_name in cmc_issue.raw['fields']:
    #     print "Field:", field_name, "Value:", cmc_issue.raw['fields'][field_name]
    # print cmc_issue.fields.attachment

    return new_issue.key

def get_assignment_by_ticket(cmc_ticket_no):
    """
    get the assignment and report by cmc ticket no from config @tickets_automatic_assignments
    :param cmc_ticket_no:
    :return: assignment username, if not found by default return confi @oz_jira_default_assignee_reporter
    """
    ticket_prefix = str(cmc_ticket_no).split("-")[0]

    if tickets_automatic_assignments.has_key(ticket_prefix) and tickets_automatic_assignments[ticket_prefix]:
        return tickets_automatic_assignments[ticket_prefix]

    return oz_jira_default_assignee_reporter

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

# Append today's label for yesterday's tickets which status not in(Resolved, Closed)
def append_today_label_for_yesterday_unresolved_tickets(oz_jira, workDir):
    """
    Append today's label for yestaday's tickets which status not in(Resolved, Closed)
    """
    day_offset = 1
    weekday = today.isoweekday()
    if weekday == 6:
        # Saturday
        day_offset = 1
    elif weekday ==7:
        # Sunday
        day_offset=2
    elif weekday == 1:
        # monday, usually Sat and Sun were reset days
        day_offset = 3

    yesterday_label=(today - timedelta(days=day_offset)).strftime(dateFormat)
    yesterday_unresolved_tickets = oz_jira.search_issues(oz_jira_yesterday_not_resolved_query % yesterday_label)
    if yesterday_unresolved_tickets.__len__() > 0:
        log.info(">>>>>>>>> There are %i tickets will append today's label: %s", yesterday_unresolved_tickets.__len__(), label)
        for issue in yesterday_unresolved_tickets:
            cmc_ticket_num = get_cmc_ticket_number(issue)
            append_label(oz_jira, issue, workDir, cmc_ticket_num, issue.key)
            if force_update_flag:
                # update oz ticket's summary, description, attachments and branches
                forceUpdateOzTicket(oz_jira, cmc_jira, issue.key, cmc_ticket_num, workDir)


def getLinkedIssue(key):
    """
    Get the issue linked issues key
    :param issue: JIRA Issue object

    inwardIssue: the issue to link from
    outwardIssues: the issue to link to
    :return: {"inwardIssues": [key1, key2], "outwardIssues": [key3, key4...]}
    """
    inwardIssues = []
    outwardIssues = []
    results = {}

    issue = cmc_jira.issue(key)
    for link in issue.fields.issuelinks:
        if hasattr(link, "outwardIssue"):
            outwardIssue = link.outwardIssue
            if checkLinkedTicket(outwardIssue):
                outwardIssues.append(outwardIssue.key)
        if hasattr(link, "inwardIssue"):
            inwardIssue = link.inwardIssue
            if checkLinkedTicket(inwardIssue):
                inwardIssues.append(inwardIssue.key)
    if inwardIssues:
        results['inwardIssues'] = inwardIssues
    if outwardIssues:
        results['outwardIssues'] = outwardIssues

    return results

def checkLinkedTicket(linked_ticket):
    if linked_ticket:
        linked_ticket_key = linked_ticket.key
        linked_ticket_status = linked_ticket.fields.status.name
        p_cmc_ticket_status = str(linked_ticket_status).replace('(', '').replace(')', '').replace(' ', '-').strip().upper()
        ignoreStatusFlag = ("PASSED" in p_cmc_ticket_status or p_cmc_ticket_status in ('CLOSED', 'RESOLVED', 'QA-COMPLETE', 'DEV-COMPLETE', 'IN-PROGRESS-QA', 'IN-QA'))

        real_cmc_linked_ticket = cmc_jira.issue(linked_ticket_key)
        if real_cmc_linked_ticket.fields.assignee:
            linked_ticket_assignee = real_cmc_linked_ticket.fields.assignee.name if real_cmc_linked_ticket.fields.assignee else 'NotAssign'

        for prefix in ignore_linked_tickets_prefix:
            if (prefix in linked_ticket_key) or ignoreStatusFlag or not (linked_ticket_assignee in cmc_ticket_assign_to_us_usernames):
                log.info("Ignore Linked ticket %s", linked_ticket_key)
                return False
    return True

def getEpicIssue(key):
    epicIssues = []
    api_url = cmc_jira_epic_api.format(key=key)
    r = requests.get(api_url, auth=(cmc_jira_username, cmc_jira_pwd))
    jsonData = r.json()
    if jsonData and 'issues' in jsonData:
        for item in jsonData['issues']:
            epicIssues.append(item['key'])

    return epicIssues


if __name__ == '__main__':
    parameters = sys.argv[1:]

    help_text = """
            -h help
            -d <workPath> work directory, if not configured used current path
            -c config oz & cmc jira username and password
            -f <filename> load content from a file
            -t <ticketsNo> load content from transform tickets (split with ',', '/' or '\')
            -a Append today's label for yesterday not 'Resolved' and 'Closed' tickets (just in oz jira)
            -U force update the oz ticket with same as cmc ticket (if cmc ticket was updated)
            -P without proxy

            Usage:
                    python DailyJira.py -d ~/Downloads/JIRA -c
               Then:
                    python DailyJira.py -f <filename> [-a]
               OR
                    python DailyJira.py -t <cmc tickets no> [-a]

                    python DailyJira.py -h 
        """

    if parameters.__len__() == 0:
        print help_text
        sys.exit(2)

    try:
        opts, args = getopt.getopt(parameters,"hcaULEPd:f:t:",["config", "append", "dir=","file=","tickets="])
    except getopt.GetoptError, e:
        print 'DailyJira.py -d <workPath> -c '
        print 'DailyJira.py -f <filename> '
        print 'DailyJira.py -t <cmc tickets> '
        print '[-P] without proxy'
        log.error(e)
        sys.exit(2)

    ############  variables [START] ############
    tickets = []
    config_file_name=os.path.expanduser("~/.dailyJiraConfig")
    work_dir = None
    append_today_label_for_yesterday_tickets_flag = False
    force_update_flag = False
    create_linked_issue_flag=False
    create_epic_issue_falg=False
    use_proxy = True
    ############  variables [END]  #############

    for opt, arg in opts:
        if opt == '-h':
            print help_text
            sys.exit(2)

        if opt in ('-d', '--dir'):
            # Configuration the work dir, it not configured, used the current path
            print "dir:", arg
            work_dir = get_work_path(arg)

        if opt in ('-c', '--config'):
            oz_jira_username=raw_input("Please enter OZ JIRA username: ")
            oz_jira_pwd=getpass.getpass(prompt="Please enter OZ JIRA password [%s]: " %oz_jira_username)

            cmc_jira_username=raw_input("Please enter CMC JIRA username: ")
            cmc_jira_pwd=getpass.getpass(prompt="Please enter CMC JIRA password [%s]: " %cmc_jira_username)

            oz_jira_default_assigner = raw_input("Please enter OZ JIRA default Assignee/Reporter: ")
            oz_jira_default_assigner = oz_jira_default_assigner if oz_jira_default_assigner else oz_jira_username

            work_dir = get_work_path(work_dir)
            config_content = '{a}\n{b}\n{c}\n{d}\n{e}\n{workPath}'.format(a=oz_jira_username, b=oz_jira_pwd, c=cmc_jira_username, d=cmc_jira_pwd, e=oz_jira_default_assigner, workPath=work_dir)
            write_content_to_file(config_file_name, config_content)

        if opt in ('-P', '--noproxy'):
            use_proxy = False

        if opt in ('-U', '--Update'):
            force_update_flag = True

        if opt in ('-L', '--Link'):
            create_linked_issue_flag = True

        if opt in ('-E', '--Epic'):
            create_epic_issue_falg = True

        if opt in ('-a', '--append'):
            append_today_label_for_yesterday_tickets_flag = True

        elif opt in ('-f', '--file'):
            print arg
            filename = arg.strip()
            filename = os.path.expanduser(filename)
            if os.path.isfile(filename):
                file = open(filename, 'r')
                ticket_file_content = file.read()
                tickets = ticket_file_content.replace("/", ",").replace("\\", ",").replace("\n", ",").replace(" ", "").replace("\t", "").split(',')
                print tickets

        elif opt in ('-t', '--tickets'):
           print arg
           tickets=arg.replace("/", ",").replace("\\", ",").replace(" ", "").split(',')
           print tickets

    ################ read config data [Start] ####################
    config_file = open(config_file_name, 'r')
    config_data = config_file.read()
    if not config_data or config_data.isspace():
        print """
            You can not configured the username and password.
            Please execute the below command first:
            python DailyJira.py -c
        """
        sys.exit(2)
    configs = config_data.split("\n")
    oz_jira_username = configs[0].strip()
    oz_jira_pwd = configs[1].strip()
    cmc_jira_username = configs[2].strip()
    cmc_jira_pwd = configs[3].strip()
    oz_jira_default_assignee_reporter=configs[4].strip()
    configured_work_dir=configs[5].strip()
    if not work_dir and configured_work_dir and not configured_work_dir.isspace():
        work_dir =configured_work_dir

    ################ read config data [End] ######################

    ############### INIT JIRA Server [Start] ####################
    oz_jira = None
    cmc_jira = None
    if oz_jira_username and oz_jira_pwd:
        oz_jira = connect_jira(oz_jira_server, oz_jira_username, oz_jira_pwd, False)

    if cmc_jira_username and cmc_jira_pwd:
        cmc_jira = connect_jira(cmc_jira_server, cmc_jira_username, cmc_jira_pwd, use_proxy)
    ############### INIT JIRA Server [End] ######################

    ## get current active sprint####
    current_oz_sprint = get_current_oz_sprint()
    ## get current active sprint####

    if append_today_label_for_yesterday_tickets_flag == True:
        if not oz_jira:
            print "You have not configured the OZ JIRA Author"
            sys.exit(2)
        append_today_label_for_yesterday_unresolved_tickets(oz_jira, work_dir)

    if tickets.__len__() > 0:
        if not oz_jira:
            print "You have not configured the OZ JIRA Author"
            sys.exit(2)
        elif not cmc_jira:
            print "You have not configured the CMC JIRA Author"
            sys.exit(2)

        for ticket in tickets:
            if ticket and not ticket.isspace():
                try:
                    if create_linked_issue_flag or create_epic_issue_falg:
                        create_update_ticket_on_oz_side_with_linked_epic(oz_jira, cmc_jira, ticket, work_dir, True, force_update_flag, create_linked_issue_flag, create_epic_issue_falg)
                    else:
                        create_update_ticket_on_oz_side(oz_jira, cmc_jira, ticket, work_dir, True, force_update_flag)
                except Exception, ex:
                    log.error(ex)

    exit()