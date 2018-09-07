#!/usr/bin/env python
# -*- coding: UTF-8 -*-
from datetime import datetime
import subprocess
import cPickle

DATE_TIME_FORMAT='%Y%m%d%H%M%S'

login_cmd = 'ssh cmcdb'
dump_cmd = 'mysqldump --single-transaction -h{host} -P3306 --ignore-table={db_name}.VariableAudit --ignore-table={db_name}.PreviewRuleNodeResult --ignore-table={db_name}.Session -n -R -u ozuser1 -pTest123! {db_name} > {output_filename}.sql;'
gzip_cmd = 'gzip {output_filename}.sql'
download_cmd = 'scp cmcdb:~/{output_filename}.sql.gz /Users/yongliu/Downloads'
remove_db_cmd = 'rm -rf {output_filename}.sql.gz'
cmc_databases={}

def print_database(databases, db_host_ip):
    databases = databases.split('\n')
    for database in databases:
        if database and database not in ['Database', 'information_schema'] and 'ARCHIVE' not in database \
                and 'AUTHOR' not in database and 'PUBLIC' not in database and 'mysql' not in database:
            cmc_databases[database] = db_host_ip

def update_database():
    show_dbs_151 = 'mysql -n -h10.1.104.151 -P3306 -uozuser1 -pTest123! -e"show databases;"'
    show_dbs_21 = 'mysql -n -h10.1.104.21 -P3306 -uozuser1 -pTest123! -e"show databases;"'
    data = subprocess.check_output(login_cmd.split() + show_dbs_151.split())
    print_database(data, '10.1.104.151')
    data2 = subprocess.check_output(login_cmd.split() + show_dbs_21.split())
    print_database(data2, '10.1.104.21')
    with open('.cmc_databases.pck', 'wb') as f:
        cPickle.dump(cmc_databases, f)
import os
def load_databases():
    if not os.path.exists('.cmc_databases.pck'):
        update_database()

    with open('.cmc_databases.pck', 'rb') as f:
        cmc_databases = cPickle.load(f)

    return cmc_databases

def dump_database(database_name):
    host = cmc_databases[database_name]
    filename=datetime.now().strftime(DATE_TIME_FORMAT)
    dump_command = dump_cmd.format(host=host, db_name=database_name, output_filename=filename)
    gzip_command = gzip_cmd.format(output_filename=filename)
    download_command = download_cmd.format(output_filename=filename)
    delete_command = remove_db_cmd.format(output_filename=filename)
    print dump_command
    commands = login_cmd.split() + dump_command.split() + gzip_command.split()
    print commands
    subprocess.check_output(commands)
    print 'Downloading....'
    subprocess.check_output(download_command.split())
    print 'Done!'
    subprocess.check_output(login_cmd.split() + delete_command.split())

def search_cmc_db(prefix):
    if prefix:
        dbs = [key for key in cmc_databases.keys() if key and prefix == key]
        if len(dbs) == 0:
            dbs = [key for key in cmc_databases.keys() if key and prefix in key]
        return dbs
    else:
        return [key for key in cmc_databases.keys() if key]


from sys import argv
if __name__ == '__main__':
    cmc_databases = load_databases()
    if len(argv) == 1:
        print '\t'.join(search_cmc_db(None))
        exit(0)

    prefix = argv[1]
    dbs = search_cmc_db(prefix)
    if len(dbs) > 1:
        print '\t'.join(dbs)
        exit(0)
    download_db_name = dbs[0]
    print 'Starting download database "%s"' % download_db_name
    dump_database(download_db_name)



