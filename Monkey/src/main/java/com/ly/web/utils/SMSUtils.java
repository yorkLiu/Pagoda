package com.ly.web.utils;

import com.ly.exceptions.NotReceiveMessageException;
import com.ly.model.SMSReceiverInfo;
import com.ly.suma.api.ApiService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by yongliu on 10/31/17.
 */
public class SMSUtils {

  private static final Log logger = LogFactory.getLog(SMSUtils.class);
  
  public static Boolean loginSms(SMSReceiverInfo smsReceiverInfo){
    if(!smsReceiverInfo.isLogin()){
      String token = ApiService.login(smsReceiverInfo.getUsername(), smsReceiverInfo.getPassword());
      if(token != null && org.springframework.util.StringUtils.hasText(token)){
        smsReceiverInfo.setToken(token);
      } else {
        logger.error("SMS Platform login failed....");
        return Boolean.FALSE;
      }
    }
    return Boolean.TRUE;
  }
  
  public static void printUserInfo(SMSReceiverInfo smsReceiverInfo){
    if(smsReceiverInfo.isLogin()) {
      // get user infomation
      String userInfo = ApiService.getUserInfo(smsReceiverInfo.getUsername(), smsReceiverInfo.getToken());
      logger.info("SMS Platform user info: " + userInfo);
      String[] infos = userInfo.split(";");
      String score = infos[1];
      Integer balance = new Integer(infos[2]);
      String maxGetCount = infos[3];

      System.out.println("****************************INFO****************************");
      System.out.println(String.format("积分: %s, 余额: %s, 可同时获取号码数: %s", score, (balance / 100.00), maxGetCount));
      System.out.println("************************************************************");
    } else {
      logger.error("Please login sms platform first.");
    }
  }
  
  public static String[] getPhoneNumbers(SMSReceiverInfo smsReceiverInfo){
    loginSms(smsReceiverInfo);
    printUserInfo(smsReceiverInfo);

    String[] phoneNumbers = ApiService.getMobileNums(smsReceiverInfo.getPid(), smsReceiverInfo.getUsername(), smsReceiverInfo.getToken(), 5);
    
    return phoneNumbers;
  }

  public static String getVCodeFromPhoneNum(SMSReceiverInfo smsReceiverInfo, String phoneNumber){
    return getVCodeFromPhoneNum(smsReceiverInfo, phoneNumber, 0);
  }
  
  public static String getVCodeFromPhoneNum(SMSReceiverInfo smsReceiverInfo, String phoneNumber, int index){
    String vcode = null;
    String retMsg = null;
    String message = ApiService.getVcodeAndReleaseMobile(smsReceiverInfo.getUsername(), smsReceiverInfo.getToken(), phoneNumber, smsReceiverInfo.getAuthorID());
    if (message.startsWith(phoneNumber)) {
      logger.info("SMS was received, message: " + message);
      retMsg = message.split("\\|")[1];
    } else if (message.contains("not_receive") || message.contains("try again later")) {
      if (index <= 20) {
        logger.warn("SMS receive error, will wait 2 seconds and try again." + index);
        if(message.contains("try again later")){
          delay(5);
        } else {
          delay(2);
        }
        return getVCodeFromPhoneNum(smsReceiverInfo, phoneNumber, ++index);
      } else {
        String errorMsg = "The phone [" + phoneNumber + "] was not received message.";
        logger.warn(errorMsg);
        ApiService.addIgnore(phoneNumber, smsReceiverInfo.getUsername(), smsReceiverInfo.getToken(), smsReceiverInfo.getPid());
        throw new NotReceiveMessageException(System.currentTimeMillis(), errorMsg);
      }
    }
    
    if(retMsg != null && StringUtils.hasText(retMsg)){
      vcode = ApiService.getVCodeFromSms(retMsg);
    }

    return vcode;
  }
  
  public static String ignorePhones(String[] ignorePhones, SMSReceiverInfo smsReceiverInfo){
    String mobiles = org.springframework.util.StringUtils.arrayToDelimitedString(ignorePhones, ",");
    logger.info("Ignore phones: " + mobiles);
    String result = ApiService.addIgnore(mobiles, smsReceiverInfo.getUsername(), smsReceiverInfo.getToken(), smsReceiverInfo.getPid());
    logger.info("Result: " + result);
    return result;
  }

  protected static void delay(int seconds) {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug(">>>Starting delay " + seconds + " second(s).>>>");
      }

// Thread.sleep(seconds * 1000);
      TimeUnit.SECONDS.sleep(seconds);

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<End delay " + seconds + " second(s).<<<<");
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }


}
