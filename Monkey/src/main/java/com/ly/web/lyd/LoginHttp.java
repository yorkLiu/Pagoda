package com.ly.web.lyd;

import java.io.BufferedReader;

import java.util.ArrayList;
import java.util.List;

import com.ly.utils.JSONObjectUtils;
import com.ly.web.constant.Constant;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import org.apache.log4j.Logger;

import com.ly.web.utils.HtmlUtils;


/**
 * Created by yongliu on 7/7/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/07/2016 17:20
 */
public class LoginHttp {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String YHD_LOGIN_PAGE_URL = "https://passport.yhd.com/passport/login_input.do";

  private static final String YHD_SHOW_VALID_CODE_URL = "https://passport.yhd.com/publicPassport/showValidate.do";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private DefaultHttpClient httpClient = null;

  private Logger logger = Logger.getLogger(getClass());

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new LoginHttp object.
   */
  public LoginHttp() {
    if (httpClient == null) {
      httpClient = new DefaultHttpClient();
    }
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  private String visitLoginPage() {
    BufferedReader in = null;

    try {
      httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);

      logger.info("Open capchar url: " + YHD_LOGIN_PAGE_URL);

      HttpGet httpGet = new HttpGet(YHD_LOGIN_PAGE_URL);
      httpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

      HttpResponse response  = httpClient.execute(httpGet);
      String       loginHtml = HtmlUtils.getHtml(response.getEntity().getContent());

      if (logger.isDebugEnabled()) {
        logger.debug("login html: " + loginHtml);
      }

      // 保存图片
// if(imagePath == null){
// imagePath = image_save_path + new Date().getTime() + ".png";
// }
// download(response.getEntity().getContent(), imagePath);
// logger.info("Image saved successfully and path: " + imagePath);

      List<Cookie> cookies = httpClient.getCookieStore().getCookies();
      httpGet.releaseConnection();

      StringBuilder cookiesSB = new StringBuilder();
      logger.info("The first time to set cookie");

      if (cookies.isEmpty()) {
        logger.info("None cookie.");
      } else {
        for (int i = 0; i < cookies.size(); i++) {
          // System.out.println("- " + cookies.get(i).toString());
          cookiesSB.append(cookies.get(i).getName()).append("=").append(cookies.get(i).getValue()).append("; ");
        }

        logger.info("Cookie was set. Cookie: " + cookiesSB);
      }

      return cookiesSB.toString();
    } catch (Exception e) {
      e.printStackTrace();

      return null;
    } // end try-catch-finally

  } // end method visitLoginPage
  
  private String checkShowValidCode(String cookie, String username){
    try{

      httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);

      // 验证 登陆时时否需要 validate code.
      HttpPost httppost = new HttpPost(YHD_SHOW_VALID_CODE_URL);
      httppost.setHeader("Cookie", cookie);
      httppost.setHeader("Host", "passport.yhd.com");
      httppost.setHeader("Referer", "https://passport.yhd.com/passport/login_input.do");
      httppost.setHeader("User-Agent", Constant.USER_AGENT);

      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      nvps.add(new BasicNameValuePair("credentials.username", username));

      httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
      httppost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

      HttpResponse response = httpClient.execute(httppost);
      
      if(200 == response.getStatusLine().getStatusCode()){
        String result = HtmlUtils.getHtml(response.getEntity().getContent());
        logger.info("result:"+ result);
        String showValidCode = JSONObjectUtils.getValue(result, "ShowValidCode");
        logger.info("showValidCode:" + showValidCode);
      }
      
      httppost.releaseConnection();

    }catch (Exception e){
      logger.error(e.getMessage(), e);
    }



    return null;
  }

  public static void main(String[] args) {
    LoginHttp login = new LoginHttp();
    String cookie = login.visitLoginPage();
    login.checkShowValidCode(cookie, "13620859356");
  }


} // end class LoginHttp
