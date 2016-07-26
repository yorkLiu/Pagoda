package com.ly.web.constant;


import java.util.Date;

/**
 * TODO: DOCUMENT ME!
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/12/2016 14:49
 */
public class Constant {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  public static final String USER_AGENT =
    "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36 CoolNovo/2.0.9.19";

  /** TODO: DOCUMENT ME! */
  public static final String NOT_FOUND = "NOT_FOUND";

  /** TODO: DOCUMENT ME! */
  public static final String OK = "OK";


//  public static final String YHD_LOGIN_PAGE_URL = "https://passport.yhd.com/passport/login_input.do";
  public static final String YHD_LOGIN_PAGE_URL = "https://passport.yhd.com/passport/login_input.do?returnUrl=http://my.yhd.com/order/myOrder.do";

  public static final String YHD_INDEX_PAGE_URL = "http://www.yhd.com/";
  
  public static final String YHD_MY_ORDER_URL = "http://my.yhd.com/order/myOrder.do";
  
  public static final String YHD_ORDER_WRITE_COMMENT_URL="http://e.yhd.com/front-pe/pe/orderProductExperience!orderProductExperience.do?soId=%s&userId=%s&soType=0&hasCommented=false";
  
  public static final String YHD_LOGOUT_URL = "https://passport.yhd.com/passport/logoutJsonp.do?timestamp="+new Date().getTime()+"&callback=?";
}
