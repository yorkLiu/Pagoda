import urllib
import re

def getHtml(url):
    page = urllib.urlopen(url)
    htmlContent = page.read()
    return htmlContent


def getImage(html):
    reg = '<img src="?\'?([^"\'>]*.jpg)'
    imgReg = re.compile(reg)
    imglist = re.findall(imgReg, html)
    idx =0
    filename="/Users/yongliu/Downloads/getimages/%s.jpg"
    for img in imglist:
        print filename % idx
        urllib.urlretrieve(img, filename % idx)
        idx+=1
    return imglist

html = getHtml("http://tieba.baidu.com/p/4975164598?red_tag=j0247572055")
print getImage(html)
