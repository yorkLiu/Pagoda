from lxml import html

visitUrl = "http://www.ifeng.com/"
xpath = "//div[@id='headLineDefault']/ul/li"
textTemplate = "[{0}|{1}]"

data = html.parse(visitUrl)
items = data.xpath(xpath)

for item in items:
    lineText = ""
    atags = item.xpath("a")
    for atag in atags:
        text = atag.text
        href = atag.attrib['href']
        lineText += textTemplate.replace("{0}", text).replace("{1}", href)
    print lineText
