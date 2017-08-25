#!/usr/bin/env python
# -*- coding: UTF-8 -*-
from fabric.api import *
import os
import sys

############ USAGE ##########################
# fab -f DownloadDbDump.py download:files="yourdownloadfilename"
############ USAGE ##########################

offshore = 'yongliu@192.168.100.3:9721'
env.passwords = {offshore: 'Pwd$123#'}

db_dump_file_names = None

@hosts(offshore)
def download(files):
    db_dump_file_names = files.replace(',','/').split('/')
    print 'You want to download [%s]' % ','.join(db_dump_file_names)
    if db_dump_file_names.__len__() > 0:
        for file in db_dump_file_names:

            s = run('ls /tmp/%s' % file)
            print s

            # filename = file.strip()
            # get("/tmp/%s" % filename, os.path.expanduser('~/DBDump/'))
