import sys
import os
import logging
import htmllib
import urllib

reload(sys)
sys.setdefaultencoding("utf8")

log = logging.getLogger('JIRA')
log.setLevel(logging.INFO)
fmt = logging.Formatter('%(levelname)s:%(name)s:%(message)s')
h = logging.StreamHandler()
h.setFormatter(fmt)
log.addHandler(h)

def html_decode( s):
    p = htmllib.HTMLParser(None)
    p.save_bgn()
    p.feed(s)
    return urllib.unquote(p.save_end())

class BaseJIRA(object):

    def __init__(self):
        self.username=''
        self.password=''
        self.log = log
        self.html_decode = html_decode


    def setLoginInfo(self, username, password):
        self.username = username
        self.password = password


    def setDownloadPath(self, downloadPath):
        if downloadPath and not downloadPath.isspace():
            self.downloadPath = os.path.expanduser(downloadPath)
        else:
            # current directory
            self.downloadPath = os.path.realpath(".")


