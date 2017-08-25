import requests


if __name__ == '__main__':
    
    ip_port='61.152.81.193:9100'
    proxy_string='http://%s' % ip_port
    # session.trust_env=False
    # r = requests.get('https://www.jd.com',proxies={
    # # r = requests.get('http://ip.chinaz.com/',proxies={
    #     'http': proxy_string,
    #     'https': proxy_string
    # })


    s = requests.Session()
    s.trust_env=False
    s.proxies={'http': proxy_string,
               'https': proxy_string
               }
    
    r = s.get('https://www.jd.com/')
    
    r.encoding='utf-8'
    print r.status_code
    print r.text