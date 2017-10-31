package com.ly.web.lyd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import com.ly.web.constant.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.ly.exceptions.SendSmsFrequencyException;

import com.ly.model.SMSReceiverInfo;

import com.ly.suma.api.ApiService;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.utils.SMSUtils;


/**
 * Created by yongliu on 10/31/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/31/2017 15:43
 */
public class BindPhone extends YHDAbstractObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private SMSReceiverInfo smsReceiverInfo;

  //~ Constructors -----------------------------------------------------------------------------------------------------


  /**
   * Creates a new BindPhone object.
   */
  public BindPhone() { }

  /**
   * Creates a new BindPhone object.
   *
   * @param  webDriver        WebDriver
   * @param  smsReceiverInfo  SMSReceiverInfo
   */
  public BindPhone(WebDriver webDriver, SMSReceiverInfo smsReceiverInfo) {
    this.webDriver       = webDriver;
    this.smsReceiverInfo = smsReceiverInfo;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * bindPhoneNumber.
   *
   * @return  String
   */
  public String bindPhoneNumber() {
    webDriver.get(Constant.YHD_SECURITY_PAGE_URL);

    try {
      webDriver.findElement(By.xpath("//a[@data-tpa='MYYHD_PC_SAFE_BINDMOBILE']"));
    } catch (NoSuchElementException e) {
      logger.info("This user was binded phone");
      return  Constant.PHONE_HAS_BIND;
    } // end try-catch


    // goto bind phone page
    webDriver.get(Constant.YHD_BIND_PHONE_PAGE_URL);

    try {
      WebElement phoneField = null;

      phoneField = ExpectedConditions.presenceOfElementLocated(By.id("newverifyMobile")).apply(webDriver);
      phoneField.clear();

      String foundedPhoneNum = bind();

      String bindScript =
        "function getTwo(){var retVal; $.ajaxSetup({async: false}); $.get('http://home.yhd.com/myyhdbind/successBind.do?cellphone=$phone$&smsValidateCode=$vcode$', function(data){retVal = data.result;}, 'json'); return retVal;} return getTwo();";

      if (foundedPhoneNum != null) {
        webDriver.findElement(By.id("newverifyMobile")).sendKeys(foundedPhoneNum);

        String vcode = SMSUtils.getVCodeFromPhoneNum(smsReceiverInfo, foundedPhoneNum);
        logger.info("The phone return vcode is: " + vcode);
        executeJavaScript("$('#vCode').parent().parent().parent().css('display', 'block')");

        if (vcode != null) {
          WebElement vcodeField = webDriver.findElement(By.id("vCode"));
          vcodeField.clear();
          vcodeField.sendKeys(vcode);

          String retMsg = (String) executeJavaScript(bindScript.replace("$phone$", foundedPhoneNum).replace("$vcode$",
            vcode));

          if ("success".equalsIgnoreCase(retMsg)) {
            refreshPage();

            return foundedPhoneNum;
          }
        }
      }
    }catch (NoSuchElementException e){
      logger.error(e);
    }


    return null;
  } // end method bindPhoneNumber

  //~ ------------------------------------------------------------------------------------------------------------------

  private String bind() {
    List<String> ignorePhones     = new ArrayList<>();
    String       foundPhoneNumber = null;
    String[]     phoneNumbers     = SMSUtils.getPhoneNumbers(smsReceiverInfo);
    ignorePhones.addAll(Arrays.asList(phoneNumbers));

    for (String phoneNumber : phoneNumbers) {
      Boolean sentSuccess = sendMessage(phoneNumber);

      if (sentSuccess) {
        foundPhoneNumber = phoneNumber;
        ignorePhones.remove(phoneNumber);

        break;
      }
    }

    if (ignorePhones.size() > 0) {
      String mobiles = org.springframework.util.StringUtils.arrayToDelimitedString(ignorePhones.toArray(), ",");
      logger.info("Release the phone numbers: " + mobiles);
      ApiService.addIgnore(mobiles, smsReceiverInfo.getUsername(), smsReceiverInfo.getToken(),
        smsReceiverInfo.getPid());
    }

    return foundPhoneNumber;
  } // end method bind

  //~ ------------------------------------------------------------------------------------------------------------------

  private Boolean sendMessage(String phoneNumber) {
    String sendMsgScript =
      "function getOne(){var retVal; $.ajaxSetup({async: false}); $.get('http://home.yhd.com/myyhdbind/finishBind.do?cellphone=$phone$', function(data){retVal = data.result;}, 'json'); return retVal;} return getOne();";
    String script        = sendMsgScript.replace("$phone$", phoneNumber);
    String retInfo       = (String) executeJavaScript(script);

    if ("success".equalsIgnoreCase(retInfo)) {
      return Boolean.TRUE;
    } else if (retInfo.contains("too fast")) {
      throw new SendSmsFrequencyException(System.currentTimeMillis(), retInfo);
    }

    return Boolean.FALSE;
  }


} // end class BindPhone
