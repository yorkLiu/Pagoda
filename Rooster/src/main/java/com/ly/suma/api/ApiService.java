package com.ly.suma.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiService {

	public static String login(UserInfo userInfo) {
		if (!MyControl.isNull(userInfo.getUid()) && !MyControl.isNull(userInfo.getPwd())) {
			try {
				String param = String.format("uid=%s&pwd=%s", userInfo.getUid().trim(), userInfo.getPwd());
				System.out.println(param);
				return HttpHelper.getHtml(Config.loginUrl, param);
			} catch (Exception ex) {
				ex.printStackTrace();
				return "";
			}
		} else {
			return "";
		}
	}

	public static String login(String username, String password) {
		if (!MyControl.isNull(username) && !MyControl.isNull(password)) {
			try {
				String param = String.format("uid=%s&pwd=%s", username.trim(), password.trim());
				System.out.println(param);
				String result =  HttpHelper.getHtml(Config.loginUrl, param);

				if (result.toLowerCase().startsWith(username.toLowerCase())) {
					String[] strings = result.split("\\|");
					return strings[1];
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
				return "";
			}
		} else {
			return "";
		}
		
		return null;
	}

	public static String getUserInfo(String userName, String token) {
		try {
			String para = String.format("uid=%s&token=%s", userName, token);
			String url = Config.getUserInfos + para;
			System.out.println(url);
			return HttpHelper.getHtml(url);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String getMobileNum(String pid, String uid, String token) {
		return getMobileNum(pid, uid, token, 1);
	}
	
	public static String[] getMobileNums(String pid, String uid, String token, Integer size){
		String result = getMobileNum(pid, uid, token, size);
		if(result.contains("no_data")){
			System.out.println("系统暂时没有可用号码了");
		} else if(result.contains("parameter_error")){
			System.out.println("传入参数错误");
		} else if(result.contains("not_login")){
			System.out.println("没有登录,在没有登录下去访问需要登录的资源，忘记传入uid,token\n");
		} else if(result.contains("account_is_locked")){
			System.out.println("账号被锁定");
		} else if(result.contains("account_is_stoped")){
			System.out.println("账号被停用");
		} else if(result.contains("account_is_question_locked")){
			System.out.println("账号已关闭");
		} else if(result.contains("account_is_ip_stoped")){
			System.out.println("账号ip锁定");
		} else if(result.contains("message")){
			System.out.println(result);
		} else if(result.contains("not_found_project")){
			System.out.println("没有找到项目,项目ID不正确");
		} else if(result.contains("max_count_disable")){
			System.out.println("已经达到了当前等级可以获取手机号的最大数量，请先处理完您手上的号码再获取新的号码（处理方式：能用的号码就获取验证码，不能用的号码就加黑）");
		} else if(result.contains("unknow_error")){
			System.out.printf("未知错误,再次请求就会正确返回");
		} else {
			String[] phoneNumbers = result.split("\\|")[0].split(";");
			return phoneNumbers;
		}
		return null;
	}

	public static String getMobileNum(String pid, String uid, String token, Integer size) {
		try {
			size = size != null && size > 0 ? size : 1;
			String para = String.format("pid=%s&uid=%s&token=%s&mobile=&size=%s", pid, uid, token, size);
			String url = Config.getMobilenum + para;
			return HttpHelper.getHtml(url);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String addIgnore(String mobileNum, String uid, String token, String pid) {
		try {
			String para = String.format("uid=%s&token=%s&mobiles=%s&pid=%s", uid, token, mobileNum, pid);
			String url = Config.addIgnoreList + para;
			return HttpHelper.getHtml(url);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String getVcodeAndReleaseMobile(String uid, String token, String mobileNum, String author_uid) {
		try {
			String para = String.format("uid=%s&token=%s&mobile=%s", uid, token, mobileNum);
			if(author_uid != null && !"".equals(author_uid)){
				para+="&author_uid="+author_uid;
			}
			
			String url = Config.getVcodeAndReleaseMobile + para;
			return HttpHelper.getHtml(url);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String getVcodeAndHoldMobilenum(String uid, String token, String mobileNum, String nextId, String author_uid) {
		try {
//			String para = String.format("uid=%s&token=%s&mobile=%s&next_pid=%s", uid, token, mobileNum, nextId);
			String para = String.format("uid=%s&token=%s&mobile=%s", uid, token, mobileNum);
			if(author_uid != null && !"".equals(author_uid)){
				para+="&author_uid="+author_uid;
			}
			String url = Config.getVcodeAndHoldMobilenum + para;
			return HttpHelper.getHtml(url);
		} catch (Exception ex) {
			return "";
		}
	}


	public static String getVCodeFromSms(String smsContent){
		Pattern pattern = Pattern.compile("[0-9]{4,}");
		Matcher m = pattern.matcher(smsContent);
		while (m.find()) {
			String group = m.group();
			if(group != null && group.length() >= 4){
				return group;
			}
		}
		return "";
	}
}
