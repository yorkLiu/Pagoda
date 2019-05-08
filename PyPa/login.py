import requests
import re

r = requests.get('https://jira.katabat.com/browse/CA-4886', auth=('dannilmao', 'dan$56789'))

if r.status_code == requests.codes.ok:
    htmlContent = r.content
    if htmlContent and not htmlContent.isspace():
        regexString = '<div id="description-val"*?>/<div*>'
        regex = re.compile(regexString)
        result = regex.match(regex, htmlContent)

print r.content




