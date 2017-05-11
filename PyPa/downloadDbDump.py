#!/usr/bin/env python
# -*- coding: UTF-8 -*-
from fabric.api import *
import os
import sys

offshore = 'usser@ip:port'
env.passwords = {offshore: 'password'}

db_dump_file_names = None

@hosts(offshore)
def download(files):
    db_dump_file_names = files.replace(',','/').split('/')
    print 'You want to download [%s]' % ','.join(db_dump_file_names)
    if db_dump_file_names.__len__() > 0:
        for file in db_dump_file_names:
            filename = file.strip()
            get("/tmp/%s" % filename, os.path.expanduser('~/DBDump/'))
