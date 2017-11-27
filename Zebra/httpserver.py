# -*- coding: UTF-8 -*-
from __future__ import print_function
import time
from flask import Flask, render_template, request
import os



app = Flask(__name__, static_folder="html/static", template_folder="html/templates")


@app.route("/")
def hello():
    # print (os.getcwd())
    # file_name = '%s/%s.txt' % (os.path.join(os.getcwd(), 'Merchant-Orders'), time.strftime('%Y-%m-%d'))
    # with open(file_name, 'a') as file:
    #     file.writelines('100001')

    return render_template('index.html')
    # return render_template('AddMerchantOrder.html')

@app.route("/addorder", methods=['POST','GET'])
def addorder():
    print ('-----post')
    print (request.form['orders'])

    return render_template('AddMerchantOrder.html')


if __name__ == "__main__":
    print('oh hello')
    app.run(host='127.0.0.1', port=5000)