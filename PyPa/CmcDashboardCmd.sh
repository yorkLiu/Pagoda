#!/usr/bin/env bash

###################### Shell ENV [START] #################
# export OUTPUT_DIR='~/Downloads/JIRA/Dashboard'
# export EMAIL_FROM='Dashboard Observer <dashboard_observer@ozstrategy.com>'
# export EMAIL_TO_TECH_LEADS='yong.liu@ozstrategy.com'

###################### Shell ENV [END] ###################

export OUTPUT_DIR='~/Downloads/JIRA/Dashboard'
export NONE_CREATED_TICKET_OUTPUT_DIR='~/Downloads/JIRA/DashboardNoneCreatedTicket'
export EMAIL_USER_NAME='notify@ozstrategy.com'
export EMAIL_PWD='MHptcDBvZCRvejg4OGNvbUAm'
export EMAIL_FROM='Dashboard Observer <notify@ozstrategy.com>'
export EMAIL_SUBJECT='$(date +"%Y-%m-%d") Automatic Created/Updated Tickets <notify@ozstrategy.com>'

export OZ_JIRA_USERNAME='builder'
export OZ_JIRA_PASSWORD='builder$123'
export CMC_JIRA_USERNAME='ozintel'
export CMC_JIRA_PASSWORD='0zPa$$123'

## Config this
export EMAIL_TO_TECH_LEADS='yong.liu@ozstrategy.com'
export EMAIL_CCS='yong.liu@ozstrategy.com'


export USE_PROXY='YES'
export PROXY_SERVER='socks5://192.168.168.3:1083'


## Check the cmc dashbaord tickets but not created on oz jira server
#python CmcDashboardShell.py true
python CmcWorkLog.py
#python CmcDashboard.py
#python DailyJira.py -f $NONE_CREATED_TICKET_OUTPUT_DIR/2019-05-08.txt -a -U -L -E
#
### create the dashboard ticket which not created on oz jira server
#python DailyJira.pyc -f $NONE_CREATED_TICKET_OUTPUT_DIR/$(date +"%Y-%m-%d").txt -a -U -L -P
##
#### run dashboard
##python CmcDashboardShell.pyc
#
### email the created tickets to tech_leads
##python sendemail.pyc $NONE_CREATED_TICKET_OUTPUT_DIR/$(date +"%Y-%m-%d").txt
#python sendemail.py ~/Downloads/JIRA/Tickets/$(date +"%Y%m%d").txt
