import sys
import re
import os
import urllib
import urllib2
import cookielib
import json
import logging
import htmllib
from lxml import html, etree
import BaseJIRA

# reload(sys)
# sys.setdefaultencoding("utf8")
# 
# log = logging.getLogger('CMCJIRA')
# log.setLevel(logging.INFO)
# fmt = logging.Formatter('%(levelname)s:%(name)s:%(message)s')
# h = logging.StreamHandler()
# h.setFormatter(fmt)
# log.addHandler(h)
# 
# def html_decode(s):
#     p = htmllib.HTMLParser(None)
#     p.save_bgn()
#     p.feed(s)
#     return urllib.unquote(p.save_end())

class CMCJira(BaseJIRA.BaseJIRA):
    def __init__(self):
        BaseJIRA.BaseJIRA.__init__(self)
        self.username=''
        self.password=''
        self.downloadPath=''
        self.loginUrl = 'https://jira.katabat.com/login.jsp'
        self.jiraUrlPrefix='https://jira.katabat.com/browse/{ticketNo}'
        self.jiraSummaryAndDescriptonUrlPrefix="https://jira.katabat.com/rest/api/2/issue/{ticketId}?fields=summary,issuetype,description,customfield_13450"

        self.xpath_ticket_rel_ID="//a[@id='key-val']"
        self.xpath_attachment="//div[@id='attachmentmodule']//ol/li"

        self.cj = cookielib.LWPCookieJar()
        self.opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(self.cj))
        urllib2.install_opener(self.opener)


    def login(self):
        params = {
            "os_username" : self.username,
            "os_password" : self.password
        }
        req = urllib2.Request(self.loginUrl, urllib.urlencode(params))
        response = urllib2.urlopen(req)
        self.operate = self.opener.open(req)

    def loadSummaryAndDescription(self, ticketRelId):
        if not ticketRelId.isspace():
            requestUrl = self.jiraSummaryAndDescriptonUrlPrefix.replace("{ticketId}", ticketRelId)
            self.log.info("Request URL: " + requestUrl)
            response = self.opener.open(requestUrl)
            responseText = response.read()
            self.opener.close()
            data = json.loads(responseText)
            # data = ast.literal_eval(responseText)
            fields = data.get('fields')
            # ----------get the software branches [start]-------------
            branches = ""
            branchesObjs = fields.get('customfield_13450')
            for branch in branchesObjs:
                branches = branches + ", " + branch.get('value')
            if not branches.isspace():
                branches = branches[1:].strip()

            self.log.info("branches:" + branches)   
            # ----------get the software branches [end] ---------------   
            return {
                'ticketNo': data.get('key'),
                'ticketType': fields.get('issuetype').get('name'),
                'ticketSummary': fields.get('summary'),
                'ticketBranches': branches,
                'ticketDescription':fields.get('description')
            }

    def downloadAttachments(self, ticketNo, urls):  
        self.log.info("Ready for download attachments....")
        if not urls:
            self.log.info("No attachments need download...")
            return

        # in self.downloadPath folder created a folder name with @ticketNo
        # I.E ~/Download/CA-4323
        downloadPath = os.path.join(self.downloadPath, ticketNo)
        if not os.path.exists(downloadPath):
            self.log.info("The directory: " + downloadPath + " was not exists. Will create it.")
            os.makedirs(downloadPath)
            self.log.info("Created path: " + downloadPath + " successfully.")

        ulrReg = 'http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\(\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+'
        regex = re.compile(ulrReg)

        for durl in urls:
            url = regex.findall(durl)[0]
            self.log.info("Attachment URL: " + url)
            filename=os.path.join(downloadPath, self.html_decode(os.path.basename(url)))
            self.log.info("Download attachment save as: " + filename)
            resp = self.opener.open(url)
            attachment = resp.read()
            self.opener.close()
            fout = open(filename, 'wb')
            fout.write(attachment)
            # urllib.urlretrieve(url, filename)

    def getContent(self, ticketNo, needDownloadAttachment=None):
        if ticketNo and not ticketNo.isspace():
            ticketUrl = self.jiraUrlPrefix.replace("{ticketNo}", ticketNo)
            self.log.info("Request " + ticketNo + " with URL: " + ticketUrl)
            ticketReq = urllib2.Request(ticketUrl)
            response = self.opener.open(ticketReq)
            page = etree.HTML(response.read())
            self.opener.close()
            ticket_rel_ID = page.xpath(self.xpath_ticket_rel_ID)[0].attrib['rel']
            self.log.info("TicketNo:" + ticketNo + "rel ID is: " + ticket_rel_ID)

            # find the ticket all attachments
            attachmentUrls = []
            attachments = page.xpath(self.xpath_attachment)
            for att in attachments:
                attachmentUrls.append(att.attrib['data-downloadurl'])

            if needDownloadAttachment and needDownloadAttachment is True:
                # download the attachment    
                self.downloadAttachments(ticketNo, attachmentUrls)

            ticketMainContent = self.loadSummaryAndDescription(ticket_rel_ID)
            print ticketMainContent

if __name__ == '__main__':
    cmcJira = CMCJira()
    cmcJira.setLoginInfo("dannilmao", "dan$56789")
    cmcJira.setDownloadPath("~/Downloads/CMCJIRA/")
    cmcJira.login()
    # tickets = ["CA-4886", "CA-5607"]
    cmcJira.getContent("CA-5656", True)
    # cmcJira.getContent("CA-5607")
    # cmcJira.getContent("INC-2022")