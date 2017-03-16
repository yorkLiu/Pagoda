import json
import urllib
import os
import re
import htmllib
from datetime import date, timedelta


json_string = '{"expand":"renderedFields,names,schema,operations,editmeta,changelog,versionedRepresentations","id":"58105","self":"https://jira.cmcassist.com/rest/api/2/issue/58105","key":"CA-4886","fields":{"summary":"CredAgility - Data Export Action (PRODUCT)","issuetype":{"self":"https://jira.cmcassist.com/rest/api/2/issuetype/30","id":"30","description":"gh.issue.story.desc","iconUrl":"https://jira.cmcassist.com/secure/viewavatar?size=xsmall&avatarId=11535&avatarType=issuetype","name":"Story","subtask":false,"avatarId":11535}}}'

data = json.loads(json_string)

print data.get('fields').get('issuetype').get('name')

str = ", master, mme_uc_7.2_13621_20160119_ga"
print str[1:].strip()

attachmentName = "https://jira.cmcassist.com/secure/attachment/82669/1.png"
print os.path.basename(attachmentName)

print os.path.relpath("~/Download")
print os.path.expanduser("~/Download/")
print os.path.realpath(".")

# p = os.path.join(os.path.expanduser("~/Downloads/"), "CA-4323/aaa")
# print p
# if not os.path.exists(p):
#     os.makedirs(p)

download_url = 'image/jpeg:MomPortal3.jpg:https://jira.cmcassist.com/secure/attachment/83293/MomPortal3.jpg'
ulr_reg = 'http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\(\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+'
regex = re.compile(ulr_reg)
print regex.findall(download_url)[0]


def unescape(s):
         p = htmllib.HTMLParser(None)
         p.save_bgn()
         p.feed(s)
         return p.save_end()

print urllib.unquote(unescape("CA-4886%20batch%20export%20design&amp;.docx"))


software_branches = [
    {'value': 'a', 'id': 1}, 
    {'value': 'b', 'id': 2}, 
    {'value': 'c', 'id': 3}, 
    {'value': 'd', 'id': 4}
]

branch_names = []
for software_branch in software_branches:
    branch_names.append(software_branch['value'])
    
print branch_names
print ','.join(branch_names)

print  os.path.basename('/sdf/sss/tx.txt')

fout = open("/Users/yongliu/Downloads/JIRA/Tickets/aaa.txt", 'a')
fout.write("aaaa\n")


status_map = {
    'ApproveDesign': 711,
    'ApproveDev': 741,
    'OpenDev': 761
}

print status_map['ApproveDesign']

print os.path.expanduser("~")

print date.today().isoweekday()