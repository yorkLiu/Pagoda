#!/usr/bin/env bash

export OUTPUT_DIR='~/Downloads/JIRA/Dashboard'
export EMAIL_USER_NAME='yong.liu@ozstrategy.com'
export EMAIL_PWD='dXUwMDAwMDA='
export EMAIL_FROM='CMC Tickets Tracking <yong.liu@ozstrategy.com>'



### Config JIRA [start]
export OZ_JIRA_USERNAME='builder'
export OZ_JIRA_PASSWORD='builder$123'
export CMC_JIRA_USERNAME='ozdev'
export CMC_JIRA_PASSWORD='Oz1tel$123$'
### Config JIRA [end]

## Config this
export OZ_QA_MANAGER_NAME='Haiying Chen, Fei Qin'
export EMAIL_TO_TECH_LEADS='yong.liu@ozstrategy.com'
export EMAIL_CCS='yong.liu@ozstrategy.com'


if [[ ($# -ge 1) ]]; then
    python OZJiraTracker.py $1
    
else
    python OZJiraTracker.py
fi
