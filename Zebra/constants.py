# -*- coding: UTF-8 -*-
# coding:utf-8


JD_LOGIN_URL='https://plogin.m.jd.com/user/login.action'
JD_INDEX_URL='https://m.jd.com'



##### PDD Config START######
PDD_INDEX_URL="http://mobile.yangkeduo.com/index.html"

PDD_SEARCH_URL="http://mobile.yangkeduo.com/search_result.html?search_key={key_word}&search_src=new&search_met_track=manual&refer_page_name=search&refer_page_id={refer_page_id}"

PDD_GOODS_PAGE="http://mobile.yangkeduo.com/goods.html?goods_id={goods_id}&is_spike=0&refer_page_name=search_result&refer_page_id={refer_page_id}"

PDD_JOIN_GROUP_URL="http://mobile.yangkeduo.com/group8.html?group_order_id={group_order_id}&refer_page_name=goods_detail&refer_page_element=local_group&refer_page_id={refer_page_id}&refer_page_sn={refer_page_sn}"

PDD_PERSONAL_PAGE_URL="http://mobile.yangkeduo.com/personal.html"

PDD_LOGIN_PAGE_URL="http://mobile.yangkeduo.com/login.html"

# http://mobile.yangkeduo.com/chat_detail.html?mall_id=340007&refer_page_name=chat_list&refer_page_id=chat_list_1512443429657_vRepbNFHc1&refer_page_sn=10036
PDD_CHAT_BASE_URL="http://mobile.yangkeduo.com/chat_detail.html?mall_id={mall_id}&refer_page_name=chat_list&refer_page_id={refer_page_id}&refer_page_sn=10036"

### API [START]###
PDD_GOODS_DETAILS_API='http://apiv4.yangkeduo.com/v5/goods/{goods_id}?pdduid='
PDD_GOODS_GROUPS_API='http://apiv4.yangkeduo.com/v2/goods/{goods_id}/local_group?pdduid='
PDD_MALL_INFO_API='http://apiv4.yangkeduo.com/mall/{mall_id}/info?pdduid='




# Get PDD 所有的一级目录 (一级目录所返回的数据 包含了其所对应的的二级目录)
PDD_GET_ALL_CATEGORY='http://apiv4.yangkeduo.com/operations?pdduid=&is_back=1'
# http://apiv2.yangkeduo.com/v4/operation/1135/groups?opt_type=2&offset=900&size=1000&sort_type=DEFAULT&pdduid=
PDD_GET_GOODS_BY_CATEGORY_ID='http://apiv2.yangkeduo.com/v4/operation/{opt_id}/groups?opt_type={opt_type}&offset={start}&size={end}&sort_type=DEFAULT&pdduid='

### API [END]#####

##### PDD Config END########



############################# Constant variables [START] #############################
order_finished_status_text='FINISHED'
order_not_finished_status_text='NOT_FINISHED'
############################# Constant variables [E N D] #############################









# USER AGENT
# Android and IPhone
# FROM: 1. https://segmentfault.com/a/1190000002532679
#       2. http://www.aiezu.com/code/server_http_user-agent_uc.html
#       3. http://www.fynas.com/ua
USER_AGENT=[
    'Mozilla/5.0 (Linux; Android 4.4.4; HTC D820u Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.89 Mobile Safari/537.36',
    'Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; HTC_D820u Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30',
    'Mozilla/5.0 (Linux; Android 4.4.4; HTC D820u Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 ACHEETAHI/2100501044',
    'Mozilla/5.0 (Linux; Android 4.4.4; HTC D820u Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 bdbrowser_i18n/4.6.0.7',
    'Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; HTC D820u Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.1.0.527 U3/0.8.0 Mobile Safari/534.30',
    'Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; HTC D820u Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 SogouMSE,SogouMobileBrowser/3.5.1',
    'Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; HTC D820u Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.6 Mobile Safari/537.36',
    'Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; HTC D820u Build/KTU84P) AppleWebKit/534.24 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.24 T5/2.0 baidubrowser/5.3.4.0 (Baidu; P1 4.4.4)',
    # 'Mozilla/5.0 (Linux; U; Android 2.3.6; zh-cn; GT-S5660 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 MicroMessenger/4.5.255',
    'Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; HUAWEI MT7-TL00 Build/HuaweiMT7-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.3.8.909 Mobile Safari/537.36',
    'Mozilla/5.0 (Linux; U; Android 6.0.1; zh-cn; Redmi 3X Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/7.2 Mobile Safari/537.36',
    'Mozilla/5.0 (Linux; U; Android 6.0.1; zh-cn; vivo Xplay6 Build/MXB48T) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/7.6 Mobile Safari/537.36',
    'Mozilla/5.0 (Linux; Android 5.1.1; vivo X7 Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 baiduboxapp/8.6.5 (Baidu; P1 5.1.1)',
    # 'android Mozilla/5.0 (Linux; Android 5.1.1; ATH-AL00 Build/HONORATH-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.121 Mobile Safari/537.36  phh_android_version/3.40.1 phh_android_build/228842 phh_android_channel/hw',

    'Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12A365 Safari/600.1.4',
    'Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OS X; zh-CN) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/12A365 UCBrowser/10.2.5.551 Mobile',
    'Mozilla/5.0 (iPhone 5SGLOBAL; CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/6.0 MQQBrowser/5.6 Mobile/12A365 Safari/8536.25',
    'Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/7.0 Mobile/12A365 Safari/9537.53',
    'Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Coast/4.01.88243 Mobile/12A365 Safari/7534.48.3',
    'Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) CriOS/40.0.2214.69 Mobile/12A365 Safari/600.1.4',
    # 'Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2',
    'Mozilla/5.0 (iPad; U; CPU OS 6_0 like Mac OS X; zh-CN; iPad2) AppleWebKit/534.13 (KHTML, like Gecko) UCBrowser/8.6.0.199 U3/0.8.0 Safari/534.13',
    'Mozilla/5.0 (iPhone 6s; CPU iPhone OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 MQQBrowser/7.5.1 Mobile/13G36 Safari/8536.25 MttCustomUA/2 QBWebViewType/1 WKType/1',
    # 'Mozilla/5.0 (iPhone; CPU iPhone OS 10_2 like Mac OS X) AppleWebKit/602.3.12 (KHTML, like Gecko) Mobile/14C92 MicroMessenger/6.5.9 NetType/WIFI Language/zh_CN',
    'Mozilla/5.0 (iPhone 6s; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 MQQBrowser/7.6.0 Mobile/14E304 Safari/8536.25 MttCustomUA/2 QBWebViewType/1 WKType/1'
]