import requests

# url = 'https://plaqa.credagility.com/bcc/dwr/call/plaincall/userRoleController.updateUser.dwr'
# data = {
# 'callCount':'1',
# 'nextReverseAjaxIndex':'0',
# 'c0-scriptName':'userRoleController',
# 'c0-methodName':'updateUser',
# 'c0-id':'0',
# 'c0-e2':'string:9',
# 'c0-e3':'string:',
# 'c0-e4':'string:chy',
# 'c0-e5':'string:Tester',
# 'c0-e6':'string:OZStrategy',
# 'c0-e7':'string:Tester%20OZStrategy',
# 'c0-e8':'string:tester%40ozstrategy.com',
# 'c0-e9':'string:queueView%3FnewAccount2Mode%3Dtrue',
# 'c0-e10':'number:1',
# 'c0-e11':'string:99999999',
# 'c0-e12':'string:',
# 'c0-e13':'string:2',
# 'c0-e16':'number:2',
# 'c0-e17':'string:Test%20UC%20CMC%20Admin',
# 'c0-e18':'boolean:false',
# 'c0-e19':'number:0',
# 'c0-e15':'Object_Object:{id:reference:c0-e16, name:reference:c0-e17, organizationRole:reference:c0-e18, organizationLevel:reference:c0-e19}',
# 'c0-e14':'array:[reference:c0-e15]',
# 'c0-e20':'boolean:false',
# 'c0-e21':'boolean:false',
# 'c0-e22':'boolean:true',
# 'c0-e23':'string:',
# 'c0-e24':'string:',
# 'c0-e25':'string:',
# 'c0-e26':'string:',
# 'c0-e27':'date:1487814921000',
# 'c0-e28':'string:Tester%20OZStrategy',
# 'c0-e29':'date:1550552994000',
# 'c0-e1':'Object_Object:{id:reference:c0-e2, clientAgentId:reference:c0-e3, username:reference:c0-e4, firstName:reference:c0-e5, lastName:reference:c0-e6, userFullName:reference:c0-e7, email:reference:c0-e8, defaultStationView:reference:c0-e9, twilioNumberId:reference:c0-e10, phoneNumber:reference:c0-e11, extension:reference:c0-e12, defaultRoleId:reference:c0-e13, simpleRoles:reference:c0-e14, accountLocked:reference:c0-e20, newUserPasswordPending:reference:c0-e21, enabled:reference:c0-e22, creatorName:reference:c0-e23, password:reference:c0-e24, confirmPassword:reference:c0-e25, passwordHint:reference:c0-e26, createDate:reference:c0-e27, lastUpdaterName:reference:c0-e28, lastUpdateDate:reference:c0-e29}',
# 'c0-param0':'array:[reference:c0-e1]',
# 'c0-e31':'string:username',
# 'c0-e32':'string:clientAgentId',
# 'c0-e33':'string:userFullName',
# 'c0-e34':'string:newUserPasswordPending',
# 'c0-e35':'string:enabled',
# 'c0-e36':'string:accountLocked',
# 'c0-e37':'string:createDate',
# 'c0-e30':'array:[reference:c0-e31,reference:c0-e32,reference:c0-e33,reference:c0-e34,reference:c0-e35,reference:c0-e36,reference:c0-e37]',
# 'c0-e38':'string:chy',
# 'c0-param1':'Object_Object:{fields:reference:c0-e30, query:reference:c0-e38}',
# 'batchId':'20',
# 'instanceId':'0',
# 'page':'%2Fbcc%2Fhtml%2Fdesktop%3Fxsf%3DADE3078HyiuYdf',
# 'scriptSessionId':'lIMhysVvTS~AsWGtkoK9SHhqJQ108mEYnym/3RMn2Am-rg1KVO$Id'
# }
#
#
# headers = {
# 'Accept': '*/*',
# 'Accept-Encoding': 'gzip, deflate, br',
# 'Accept-Language': 'en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7',
# 'Connection': 'keep-alive',
# 'Content-Length': '2154',
# 'Content-Type': 'text/plain',
# 'Cookie': 'JSESSIONID=BA38F6C2DEB7122D55047A6EE9069C16; TS01b2f972=012b190f27ab737e6158cefb49b8590e7ae555018acaed6b65605c9129ee913a5f6615ff827b14aa7c086f881228039a5e601924ffdcde1ebd06b4174a0e6ecdb156e67ad1f8e22d5c5646752e5bc2ec50635fd228; DWRSESSIONID=lIMhysVvTS~AsWGtkoK9SHhqJQ108mEYnym; com.cmc.web.PortfolioCookie=default; localeCookie=en_US; FlexCenter_Ext4_1_wallpaper=wallpapers/Deep-Blue.jpg; JSESSIONID="A3F6375F389ACAD46D4DB2EC8EE6C52F.${jvmRoute.ClusterId}"; BIGipServerPLAQA_site_pool=!qa9zA4FpJ9MHITm3nm3mVCDkEnQdHXuS98NCWRy/CwwkITRi6qMkCO+6YvZ9loKV3+mDNIUMcKhZWQ==; ext_theme=blue; HttpOnly; BIGipServerPLAQA_agent_pool=!u+7WXW4elwQGsWS3nm3mVCDkEnQdHUF/bvkLHaWRFZDPvgM8/goW23UmjibvhuhknbNwWJVdzWEuMg==; activeTab=1550506245030; BIGipServerPLAQA_bcc_pool=!2u2CC3+A7+wC/zO3nm3mVCDkEnQdHWFusPK45n5PbL35aA52vvwAQToLU8RMX2bnfGgmwfnIhfVVlQ==; TS013e1307=012b190f27e65bbba7912433b2dfae68ed203b5bca6ec9f656a0b819a940fc0b1f79abb93bbf2562150e7951187bc6c6c3ef997d4baa0576023f34e65de6a520c33db966f1330cc5e3775d8e86f132b402b091e6146ff267bc5e76048c65b3acf6b2f28a0cc9257a013108672259c11b72cb9dff94cea248ad24c0863469406b55e82e455dda768823de7e995a20af637830692eb39a278462d44c8a34f4a75d58bf3e4dfa4087f3fd708e6fe21d8c873453bb2c4d7656b87740dbb756c394d31771e5dba6f0114b3f46d1a830ce87f97274711f5de72f0c49c29f2229a8633f4c7b01e6fd60c21c6e4f6cc97d1f2d8327ec42020ea094b4e86da3234bbbe0b2bd824b95a00a52338b4ab524f4a6a8b8ad7f5b9f0c16bc4091466595e2ba2d1581266ee31408d97f06ebdc03a0046b1265c2e1d50c5b46f2582a32d5aea20615b772643dfc2318bdbf1588e72a8f81866c5db89a4716c822c3cb9f20526718f1d001a7b9b3d527fbe25d04731adbc24d33284933bd7fcb20c2ab7d01cbc995b2aeca4ebee97ef516b04adbe8a4d6ced742282db384474c33c437ac9182cabe735cd8e334644a001988c4c020aa1badede92da9f05ff110ff1eb3e126358d57f162c6e90022',
# 'Host': 'plaqa.credagility.com',
# 'Origin': 'https://plaqa.credagility.com',
# 'Referer': 'https://plaqa.credagility.com/bcc/html/desktop?xsf=ADE3078HyiuYdf',
# 'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36'
#
# }


# url='https://plaqa.credagility.com/agent/dwr/call/plaincall/GlobalController.updateUserInfo.dwr'
#
#
# headers = {
# 'Accept': '*/*',
# 'Accept-Encoding': 'gzip, deflate, br',
# 'Accept-Language': 'en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7',
# 'Connection': 'keep-alive',
# 'Content-Length': '2154',
# 'Content-Type': 'text/plain',
# 'Cookie': 'DWRSESSIONID=0GJx~is0osYMm9VIcxEIh1~i8CtWqjIh5Am; JSESSIONID=node01uuskjvtc6wgteliga9fvjnig1.node0; localeCookie=en_US; ext_theme=blue; HttpOnly; FlexCenter_Ext4_1_wallpaper=wallpapers/Deep-Blue.jpg; activeTab=1550557187358',
# 'Host': 'plaqa.credagility.com',
# 'Origin': 'https://plaqa.credagility.com',
# 'Referer': 'https://plaqa.credagility.com/agent/html/queueView?newAccount2Mode=true&landingView=true',
# 'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36'
# }
#
# data = {
# 'callCount':'1',
# 'nextReverseAjaxIndex':'0',
# 'c0-scriptName':'GlobalController',
# 'c0-methodName':'updateUserInfo',
# 'c0-id':'0',
# 'c0-param0':'string:Tester',
# 'c0-param1':'string:OZStrategy',
# 'c0-param2':'string:666666666',
# 'c0-param3':'string:tester%40ozstrategy.com',
# 'batchId':'21',
# 'instanceId':'0',
# 'page':'%2Fagent%2Fhtml%2FqueueView%3FnewAccount2Mode%3Dtrue%26landingView%3Dtrue',
# 'scriptSessionId':'fEAFmeS2C6Bx6htSppvPuvqsNWG4IdXwyym-2/JXjt2Am-n1TBvtG*s',
# }


url = 'http://localhost:9090/Xiling/dwr/call/plaincall/userRoleController.updateUser.dwr'

headers = {
'Accept': '*/*',
'Accept-Encoding': 'gzip, deflate, br',
'Accept-Language': 'en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7',
'Connection': 'keep-alive',
'Content-Length': '2154',
'Content-Type': 'text/plain',
'Cookie': 'DWRSESSIONID=0GJx~is0osYMm9VIcxEIh1~i8CtWqjIh5Am; JSESSIONID=node01uuskjvtc6wgteliga9fvjnig1.node0; localeCookie=en_US; ext_theme=blue; HttpOnly; FlexCenter_Ext4_1_wallpaper=wallpapers/Deep-Blue.jpg; activeTab=1550557267988',
'Host': 'localhost:9090',
'Origin': 'http://localhost:9090',
'Referer': 'http://localhost:9090/Xiling/html/desktop?xsf=ADE3078HyiuYdf',
'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36'
}

data = {
'callCount':'1',
'nextReverseAjaxIndex':'0',
'c0-scriptName':'userRoleController',
'c0-methodName':'updateUser',
'c0-id':'0',
'c0-e2':'string:9',
'c0-e3':'string:',
'c0-e4':'string:chy',
'c0-e5':'string:Tester',
'c0-e6':'string:OZStrategy',
'c0-e7':'string:Tester%20OZStrategy',
'c0-e8':'string:tester%40ozstrategy.com',
'c0-e9':'string:queueView%3FnewAccount2Mode%3Dtrue',
'c0-e10':'number:1',
'c0-e11':'string:2222222222',
'c0-e12':'string:',
'c0-e13':'string:2',
'c0-e16':'number:2',
'c0-e17':'string:Test%20UC%20CMC%20Admin',
'c0-e18':'boolean:false',
'c0-e19':'number:0',
'c0-e15':'Object_Object:{id:reference:c0-e16, name:reference:c0-e17, organizationRole:reference:c0-e18, organizationLevel:reference:c0-e19}',
'c0-e14':'array:[reference:c0-e15]',
'c0-e20':'boolean:false',
'c0-e21':'boolean:false',
'c0-e22':'boolean:true',
'c0-e23':'string:',
'c0-e24':'string:',
'c0-e25':'string:',
'c0-e26':'string:',
'c0-e27':'date:1487814921000',
'c0-e28':'string:Tester%20OZStrategy',
'c0-e29':'date:1550552994000',
'c0-e1':'Object_Object:{id:reference:c0-e2, clientAgentId:reference:c0-e3, username:reference:c0-e4, firstName:reference:c0-e5, lastName:reference:c0-e6, userFullName:reference:c0-e7, email:reference:c0-e8, defaultStationView:reference:c0-e9, twilioNumberId:reference:c0-e10, phoneNumber:reference:c0-e11, extension:reference:c0-e12, defaultRoleId:reference:c0-e13, simpleRoles:reference:c0-e14, accountLocked:reference:c0-e20, newUserPasswordPending:reference:c0-e21, enabled:reference:c0-e22, creatorName:reference:c0-e23, password:reference:c0-e24, confirmPassword:reference:c0-e25, passwordHint:reference:c0-e26, createDate:reference:c0-e27, lastUpdaterName:reference:c0-e28, lastUpdateDate:reference:c0-e29}',
'c0-param0':'array:[reference:c0-e1]',
'c0-e31':'string:username',
'c0-e32':'string:clientAgentId',
'c0-e33':'string:userFullName',
'c0-e34':'string:newUserPasswordPending',
'c0-e35':'string:enabled',
'c0-e36':'string:accountLocked',
'c0-e37':'string:createDate',
'c0-e30':'array:[reference:c0-e31,reference:c0-e32,reference:c0-e33,reference:c0-e34,reference:c0-e35,reference:c0-e36,reference:c0-e37]',
'c0-e38':'string:chy',
'c0-param1':'Object_Object:{fields:reference:c0-e30, query:reference:c0-e38}',
'batchId':'20',
'instanceId':'0',
'page':'%2Fbcc%2Fhtml%2Fdesktop%3Fxsf%3DADE3078HyiuYdf',
'scriptSessionId':'~~~0GJx~is0osYMm9VIcxEIh1~i8CtWqjIh5Am/zxcq5Am-jXeUgdL7e'
}

r = requests.post(url, data=data,json=data, headers=headers)
print r.content