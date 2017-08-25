import os
import os.path
import base64
import re
import datetime

def get_array_hash(array):
    result = 0
    if array:
        for v in array:
            result = result + hash(str(v))
    return result


if __name__ == '__main__':

    today = datetime.datetime.now()
    label = today.strftime('%Y%m')
    print label
    
    a = ['a', 'b']
    b = ['b', 'a']
    print get_array_hash(a)
    print get_array_hash(b)
    print os.environ.get('MYSQL_HOME')
    
    print 'aaa'.split(",")
    
    e1 = base64.b64encode('data to be encoded')
    e2 =  base64.b64encode('ss')
    d1 = base64.decodestring(e1)
    d2 = base64.decodestring(e2)
    print e1, e2
    print d1, d2
    
    print base64.b64encode('ss', 'uu')
    
    print os.path.expanduser('~/Downloads')
    
    c = ''
    
    print c or 'abcdd...'
    
    times = '<span title="03/May/17"><time datetime="2017-05-03">03/May/17</time></span>'

    pattern = re.compile(r'\d+\-\d+\-\d+')
    print re.findall(pattern, times)[0]
    
    today = datetime.datetime.now()
    print today.isoweekday()
    
    aset = set(a)
    aset.add('b')
    aset.add('e')
    
    bset = set(['c','d','a','b'])
    print 'A:', ','.join(aset)
    print 'B:', ','.join(bset)
    
    print aset - bset
    print bset - aset
    
    print list(bset)
    
    e1 = ['yong.liu@ozstrategy.com', 'jianping.wang@ozstrategy.com']
    cset = set(e1)
    cset.add('yong.liu@ozstrategy.com')
    cset.add('yong.liu2@ozstrategy.com')
    
    print ', '.join(cset)
    print list(cset)
    
    print datetime.datetime.now().strftime('%Y%m')
    
    
    bb = 'com.atlassian.greenhopper.service.sprint.Sprint@2c2c8dc9[id=463,rapidViewId=125,state=CLOSED,name=SF 44 2017-04-27,startDate=2017-04-27T09:44:49.799-04:00,endDate=2017-05-10T02:06:00.000-04:00,completeDate=2017-05-18T09:34:27.676-04:00,sequence=463]'

    p2 = re.compile(r'\[.{0,}\]$')
    r2 = re.findall(p2, bb)[0]
    r2 = r2.replace('[', '').replace(']', '')
    d2 = r2.split(',')
    rm = {}
    for s in d2:
        ss = s.split('=')
        rm[ss[0]] = ss[1]
    
    print rm
    print rm['name']
    
    
    array2 = ['a','b','c']
    array3 = ['d', 'f', 'c']
    # array3.append(array2+array3)
    print array3+array2

    print os.path.isfile('~/sdfsf/ss.txt')
    
    
    
    
    