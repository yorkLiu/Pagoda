#!/usr/bin/env python
# -*- coding: UTF-8 -*- 
import os
import sys, getopt, getpass
from jira import JIRA
import logging
from datetime import date, timedelta

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

oz_jira_search_query = 'project = "CMC JIRA Tickets" AND summary ~ %s AND NOT issuetype=Sub-task'
oz_jira_yesterday_not_resolved_query='project = "CMC JIRA Tickets" AND labels=%s AND status not in(Resolved, Closed) AND NOT issuetype=Sub-task'
oz_jira_env_text = 'https://jira.cmcassist.com/browse/{cmcTicketNo}\n branch: {branch}'

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
    'updateFailed': 'Update Failed'
}

'''
Connect to jira server with username and password
'''
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

def append_label(oz_jira, issue, workDir, cmcTicketNo, oz_issue_key):
    try:
        # append today's label to this issue.
        ticket_status = issue.fields.status.name
        log.info("%s current status is: [%s]", oz_issue_key, ticket_status)
        if label not in issue.fields.labels:

            if ticket_status in ('关闭'):
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

'''
 Find cmc ticket NO in OZ JIRA server
 if found, then update the label to today
 else return false
'''
def create_update_ticket_on_oz_side(oz_jira, cmc_jira, cmcTicketNo, workDir, isCreateNewIfNotExists=True):
    log.info("Search Ticket NO. %s", cmcTicketNo)
    issues = oz_jira.search_issues(oz_jira_search_query %cmcTicketNo)
    count = issues.__len__()
    if count > 0:

        # Means this @cmcTicketNo has exists in Oz side
        # And need append today's label(I.E 20170101...)
        issue = issues[0]
        oz_issue_key = issue.key
        log.info("Found %s --> %s" ,cmcTicketNo ,oz_issue_key)

        log.info("Ready append label %s to %s(%s)", label, oz_issue_key, cmcTicketNo)
        append_label(oz_jira, issue, workDir, cmcTicketNo, oz_issue_key)
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


def create_ticket_on_oz_side(oz_jira, cmc_jira, cmcTicketNo, workDir):
    cmc_issue = cmc_jira.issue(cmcTicketNo)

    # download the attachment first
    attachments = download_attachments(workDir, cmcTicketNo, cmc_issue.fields.attachment)
    issue_type = cmc_issue.fields.issuetype.name
    software_branches = cmc_issue.fields.customfield_13450

    summary = "{cmcTicketNo} {summary}".format(cmcTicketNo=cmcTicketNo, summary = cmc_issue.fields.summary)
    environment_text = oz_jira_env_text.format(cmcTicketNo=cmcTicketNo, branch = software_branches)
    issue_type_name = 'Bug'
    branch_names = []
    labels = []
    # process labels & issue type
    if 'Story' == issue_type:
        labels = ['Estimate']
        issue_type_name = 'Story'
    else:
        labels = [label]

    print type(software_branches)
    print software_branches
    # process softer branches
    if software_branches and software_branches.__len__() > 0:
        for software_branch in software_branches:
            print software_branch
            print type(software_branch)
            # branch_names.append(software_branch['value'])
            branch_names.append(software_branch.value)
    else:
        branch_names.append("master")

    print environment_text
    print ', '.join(branch_names)

    issue_dic = {
        # 'project': {'key': 'CMC JIRA Tickets'},
        'project': {'id': 10190},
        'summary': summary,
        'description': cmc_issue.fields.description,
        'issuetype': {'name': issue_type_name},
        'labels': labels,
        'environment': oz_jira_env_text.format(cmcTicketNo=cmcTicketNo, branch=', '.join(branch_names))
    }

    # update the duedate to 'today'
    if 'Bug' == issue_type:
        issue_dic['duedate'] = str(today)

    new_issue = oz_jira.create_issue(fields=issue_dic)

    # update ticket's status (bug: Open Dev, story: Require Estimation
    new_issue_status = new_issue.fields.status.name.lower()
    if 'Bug' == issue_type_name:

        # update the duedate to 'today'
        # new_issue.update(fields={"duedate": str(today)})

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
        opts, args = getopt.getopt(parameters,"hcad:f:t:",["config", "append", "dir=","file=","tickets="])
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
        cmc_jira = connect_jira(cmc_jira_server, cmc_jira_username, cmc_jira_pwd)
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
                create_update_ticket_on_oz_side(oz_jira, cmc_jira, ticket, work_dir)