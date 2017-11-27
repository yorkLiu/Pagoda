# -*- coding: UTF-8 -*-
# coding:UTF-8
import shlex, subprocess
import sys

# from pddsysconfig import logger as log
reload(sys)
sys.setdefaultencoding('UTF-8')


class ADSL_dial:
    def __init__(self, uname, pass_, conn_name=u'宽带连接'):
        self._uname = uname
        self._pass = pass_
        self._conn_name = conn_name

    def connect(self, u=None, pass_=None):
        """
        宽带连接, 如果不提供用户名与密码, 那么将使用初始化时的用户名和密码
        :param u:
        :param pass_:
        :return:
        """

        if u and pass_:
            self._uname = u
            self._pass = pass_

        # log.debug('ADSL connect with username: %s', self._uname)

        cp = subprocess.Popen(
            shlex.split('rasdial "%s" "%s" "%s"' % (self._conn_name, self._uname, self._pass)),
            stdout=subprocess.PIPE,
            shell=False
        )

        # 等待命令返回
        cp.wait()
        print 'adsl connect: %s', cp.returncode
        # log.info('ADSL connect return code:%s', cp.returncode)
        if cp.returncode == 0:
            # log.info("ADSL connect successfully")
            return True

        return cp.returncode

    def disconnect(self):
        """
        断开宽带连接
        :return:
        """

        # log.debug('ADSL disconnect ......')

        cp = subprocess.Popen(
            shlex.split('rasdial /DISCONNECT'),
            stdout=subprocess.PIPE,
            shell=False
        )

        cp.wait()

        print 'disconnect adsl: %s', cp.returncode
        # log.debug('ADSL disconnect successfully ......')
        return cp.returncode

if __name__ == "__main__":
    adsl = ADSL_dial('hun3414022', '30808042')
    adsl.disconnect()
    adsl.connect()