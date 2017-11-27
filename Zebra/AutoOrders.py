# -*- coding: UTF-8 -*-
from multiprocessing import Queue, Process
import threading
from pddsysconfig import read_today_merchant_orders_recycle
from pddsysconfig import send_debug_email
from PDD import run as run_order

import datetime


if __name__ == "__main__":
    print('start time:', datetime.datetime.now())
    file_q = Queue(maxsize=50)
    # p1 = Process(target=read_today_merchant_orders_recycle, args=(file_q,))
    # p2 = Process(target=run_order, args=(file_q,))

    threads = []

    try:
        # p1 = Process(target=read_today_merchant_orders_recycle, args=(file_q,))
        # p2 = Process(target=run_order, args=(file_q,))
        # p1.start()
        # p2.start()
        # p1.join()
        # p2.join()

        t1 = threading.Thread(target=read_today_merchant_orders_recycle, args=(file_q,))
        threads.append(t1)
        t1.start()
        t2 = threading.Thread(target=run_order, args=(file_q,))
        threads.append(t2)
        t2.start()

        # t3 = threading.Thread(target=send_debug_email)
        # threads.append(t3)

        t1.join(1)
        t2.join(1)



    except (KeyboardInterrupt, SystemExit):

        # if p2.is_alive():
        #     p2.terminate()
        #
        # if p1.is_alive():
        #     p2.terminate()

        print '请稍后,正在结束该程序........'.decode('utf-8')
        send_debug_email()
        print '请稍后,正在结束该程序........'.decode('utf-8')

        for t in threads:
            if t.isAlive():
                print 'Stop Thread %s' % t.getName()
                t.exit()

        # print('end time:', datetime.datetime.now())
        # exit(0)
        # sys.exit(0)



