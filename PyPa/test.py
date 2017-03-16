from lxml import etree
from io import StringIO, BytesIO

node=etree.fromstring('''<a>
a-text <b>b-text</b> b-tail <c>c-text</c> c-tail
</a>''')

alltext = node.xpath('descendant-or-self::text()')

print alltext

