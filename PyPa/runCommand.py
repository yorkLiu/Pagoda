import os
from datetime import date, timedelta, datetime
# print os.popen("python /Users/yongliu/DailyJira.py -t CA-5652").read()

today = date.today()
print today.isoweekday()