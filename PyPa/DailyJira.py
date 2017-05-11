#!/usr/bin/env python
# -*- coding: UTF-8 -*- 
import os
import sys, getopt, getpass
from jira import JIRA
import logging
from datetime import date, timedelta, datetime
import hashlib
import difflib

reload(sys)
sys.setdefaultencoding("utf8")

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

oz_jira_search_query = 'project = "CMC JIRA Tickets" AND summary ~ %s'
oz_jira_yesterday_not_resolved_query='project = "CMC JIRA Tickets" AND labels=%s AND status not in(Resolved, Closed)'
oz_jira_env_text = 'https://jira.cmcassist.com/browse/{cmcTicketNo}\n branch: {branch}'
oz_jira_cmc_jira_link = 'https://jira.cmcassist.com/browse/{cmcTicketNo}'

'''
Ticket Status Map
'''
status_map = {
    'ApproveDesign': 711,
    'ApproveDev': 741,
    'OpenDev': 761,
    'ReOpen': 3
}

messages={
    'updated': 'Updated',
    'updateFailed': 'Update Failed',
    'synchronized': 'Cmc was updated the ticket %s, we have synchronized'
}

ticket_updated_comment = '[~%s] %s \n %s'

proxies = {
    'http': 'socks5://192.168.100.3:1083',
    'https': 'socks5://192.168.100.3:1083'
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
    except Exception, e:
        log.error("Failed connect JIRA server (%s)", jira_server)
        return None

def append_label(oz_jira, issue, workDir, cmcTicketNo, oz_issue_key):
    try:
        # append today's label to this issue.
        ticket_status = issue.fields.status.name
        log.info("%s current status is: [%s]", oz_issue_key, ticket_status)
        if label not in issue.fields.labels:

            if ticket_status in ('关闭', 'Closed', 'Resolved'):
                oz_jira.transition_issue(issue, status_map['ReOpen'])
                log.info("ReOpened [%s]", oz_issue_key)

            issue.add_field_value('labels',label)
            # issue.fields.labels.append(u'%s' %label)
            # issue.update(fields={"labels": issue.fields.labels})
            log.info(".........[%s] appended label: %s successfully.........", oz_issue_key, label)
        else:
            log.info(".........[%s] already has label: %s.........", oz_issue_key, label)

        # write to file
        write_main_content_to_file(workDir, cmcTicketNo, oz_issue_key, messages['updated'])
    except Exception, e:
        log.error(">>>>>>>>Append label: [%s] %s failed<<<<<<<<", oz_issue_key, label)
        # write to file
        write_main_content_to_file(workDir, cmcTicketNo, oz_issue_key, messages['updateFailed'])
        log.error(e)


# Find cmc ticket NO in OZ JIRA server
# if found, then update the label to today
# else return false
def create_update_ticket_on_oz_side(oz_jira, cmc_jira, cmcTicketNo, workDir, isCreateNewIfNotExists=True, forceUpdate=False):
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
        if forceUpdate and oz_issue_status_name not in('Closed', '关闭', 'Resolved'):
            # update oz ticket's summary, description, attachments and branches
           forceUpdateOzTicket(oz_jira, cmc_jira, oz_issue_key, cmcTicketNo, workDir)

    else:
        # not found @cmcTicketNo in Oz Jira Server
        # If @isCreateNewIfNotExists == True
        # then create the cmc ticket in oz jira server side
        if isCreateNewIfNotExists == True:
            log.info("Not found Cmc Jira Ticket: %s, will create it.", cmcTicketNo)
            create_ticket_on_oz_side(oz_jira, cmc_jira, cmcTicketNo, workDir)
        else:
            log.info("Not found Cmc Jira Ticket: %s and will not create it.", cmcTicketNo)

    return count >0


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
        diff = u'\n'.join(diffs)+'\n'
        return diff

    return None

def forceUpdateOzTicket(oz_jira, cmc_jira, oz_issue_key, cmcTicketNo, workDir):
    """
        Update the oz ticket from cmc ticket with comamnd paramenter '-U'
        :param oz_jira: oz jira server
        :param cmc_jira: cmc jira server
        :param oz_issue_key: oz ticket number
        :param cmcTicketNo: cmc ticket number
        :param workDir: the attachment stored path
    """
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

    if not oz_issue_software_branches:
        oz_issue_software_branches = ['N/A']

    changed_keys = []
    changed_msgs = []

    if not isEquals(cmc_issue_summary, oz_issue_summary):
        changed_keys.append('Summary')
        changed_msgs.append('*Summary* \nOriginal: %s \n*For Now:* %s \n\n' % (oz_issue_summary, cmc_issue_summary))
        log.info("%s [%s] Cmc summary was changed", oz_issue_key, cmcTicketNo)
        oz_issue.update(fields={"summary": '%s %s' % (cmcTicketNo, cmc_issue_summary)})

    # if not isEquals(cmc_issue_description, oz_issue_description):
    desc_diff = get_diff(oz_issue_description, cmc_issue_description, cmcTicketNo, oz_issue_last_updated_time)
    if desc_diff != None:
        changed_keys.append('Description')
        changed_msgs.append('*Description* changed, please review the *Acceptance Criteria*\n\n %s \n\n' % desc_diff)
        log.info("%s [%s] Cmc description was changed", oz_issue_key, cmcTicketNo)
        oz_issue.update(fields={"description": cmc_issue_description})

    if get_array_hash(cmc_issue_software_branch_names) != get_array_hash(oz_issue_software_branches):
        changed_keys.append('Software Branches')
        changed_msgs.append('*Software Branches* \nOriginal: %s\n*For Now:* %s\n\n' % (','.join(oz_issue_software_branches), ','.join(cmc_issue_software_branch_names)))
        log.info("%s [%s] Cmc software branches was changed", oz_issue_key, cmcTicketNo)
        oz_issue.update(fields={"customfield_10228": cmc_issue_software_branch_names})


    # upload attachment for created issue.
    if diff_attachments and diff_attachments.__len__()>0:
        changed_keys.append("Attachments")
        updated_attachments = ''
        for filename in diff_attachments:
            updated_attachments = updated_attachments + '[^ %s]\n' % os.path.basename(filename)
            oz_jira.add_attachment(issue=oz_issue, attachment=filename)

        changed_msgs.append('*Attachments Updated* \n%s\n\n' % updated_attachments)

    # write to log file
    if changed_keys and changed_keys.__len__() > 0:
        # add a comment to notice the ticket developer
        oz_jira.add_comment(oz_issue, ticket_updated_comment % (oz_issue.fields.assignee.name, messages['synchronized'] % (','.join(changed_keys)), ''.join(changed_msgs)))
        # write to file
        write_main_content_to_file(workDir, cmcTicketNo, oz_issue_key, messages['synchronized'] % (','.join(changed_keys)))

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

def create_ticket_on_oz_side(oz_jira, cmc_jira, cmcTicketNo, workDir):
    cmc_issue = cmc_jira.issue(cmcTicketNo)

    # download the attachment first
    attachments = download_attachments(workDir, cmcTicketNo, cmc_issue.fields.attachment)
    issue_type = cmc_issue.fields.issuetype.name
    software_branches = cmc_issue.fields.customfield_13450

    summary = "{cmcTicketNo} {summary}".format(cmcTicketNo=cmcTicketNo, summary = cmc_issue.fields.summary)
    environment_text = oz_jira_env_text.format(cmcTicketNo=cmcTicketNo, branch = software_branches)
    issue_type_name = 'Bug'
    branch_names = get_cmc_ticket_software_branches(software_branches)
    labels = []
    # process labels & issue type
    if 'Story' == issue_type:
        labels = ['Estimate']
        issue_type_name = 'Story'
    else:
        labels = [label]

    # process softer branches
    # if software_branches and software_branches.__len__() > 0:
    #     for software_branch in software_branches:
    #         # branch_names.append(software_branch['value'])
    #         branch_names.append(software_branch.value)
    # else:
    #     branch_names.append("master")
    # 
    # 
    # log.info('Software Branches: %s', ', '.join(branch_names))

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
        'customfield_10228': branch_names
        # 'environment': oz_jira_env_text.format(cmcTicketNo=cmcTicketNo, branch=', '.join(branch_names))
    }

    new_issue = oz_jira.create_issue(fields=issue_dic)

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


# Append today's label for yesterday's tickets which status not in(Resolved, Closed)
def append_today_label_for_yesterday_unresolved_tickets(oz_jira, workDir):
    """
    Append today's label for yestaday's tickets which status not in(Resolved, Closed)
    """
    day_offset = 1
    weekday = today.isoweekday()
    if weekday == 1:
        # monday, usually Sat and Sun were reset days
        day_offset = 2

    yesterday_label=(today - timedelta(days=day_offset)).strftime(dateFormat)
    yesterday_unresolved_tickets = oz_jira.search_issues(oz_jira_yesterday_not_resolved_query % yesterday_label)
    if yesterday_unresolved_tickets.__len__() > 0:
        log.info(">>>>>>>>> There are %i tickets will append today's label: %s", yesterday_unresolved_tickets.__len__(), label)
        for issue in yesterday_unresolved_tickets:
            append_label(oz_jira, issue, workDir, None, issue.key)


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
        opts, args = getopt.getopt(parameters,"hcaUd:f:t:",["config", "append", "dir=","file=","tickets="])
    except getopt.GetoptError, e:
        print 'DailyJira.py -d <workPath> -c '
        print 'DailyJira.py -f <filename> '
        print 'DailyJira.py -t <cmc tickets> '
        log.error(e)
        sys.exit(2)

    ############  variables [START] ############
    tickets = []
    config_file_name=os.path.expanduser("~/.dailyJiraConfig")
    work_dir = None
    append_today_label_for_yesterday_tickets_flag = False
    force_update_flag = False
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

            work_dir = get_work_path(work_dir)
            config_content = '{a}\n{b}\n{c}\n{d}\n{workPath}'.format(a=oz_jira_username, b=oz_jira_pwd, c=cmc_jira_username, d=cmc_jira_pwd, workPath=work_dir)
            write_content_to_file(config_file_name, config_content)
        
        if opt in ('-U', '--Update'):
            force_update_flag = True

        if opt in ('-a', '--append'):
            append_today_label_for_yesterday_tickets_flag = True

        elif opt in ('-f', '--file'):
            print arg
            filename = arg.strip()
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
    configured_work_dir=configs[4].strip()
    if not work_dir and configured_work_dir and not configured_work_dir.isspace():
        work_dir =configured_work_dir

    ################ read config data [End] ######################

    ############### INIT JIRA Server [Start] ####################
    oz_jira = None
    cmc_jira = None
    if oz_jira_username and oz_jira_pwd:
        oz_jira = connect_jira(oz_jira_server, oz_jira_username, oz_jira_pwd)

    if cmc_jira_username and cmc_jira_pwd:
        cmc_jira = connect_jira(cmc_jira_server, cmc_jira_username, cmc_jira_pwd, True)
    ############### INIT JIRA Server [End] ######################

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
                create_update_ticket_on_oz_side(oz_jira, cmc_jira, ticket, work_dir, True, force_update_flag)

    exit()