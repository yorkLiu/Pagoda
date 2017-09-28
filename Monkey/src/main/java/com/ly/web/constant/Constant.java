package com.ly.web.constant;


import java.io.File;
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
  
  public static final String ACCOUNT_FOLDER_NAME="accounts";


//  public static final String YHD_LOGIN_PAGE_URL = "https://passport.yhd.com/passport/login_input.do";
  public static final String YHD_LOGIN_PAGE_URL = "https://passport.yhd.com/passport/login_input.do?returnUrl=http://my.yhd.com/order/myOrder.do";
  
  public static final String YHD_LOGIN_PAGE_URL_WITHOUT_MY_ORDER = "https://passport.yhd.com/passport/login_input.do";

  public static final String YHD_INDEX_PAGE_URL = "http://www.yhd.com";
  
  public static final String YHD_MY_ORDER_URL = "http://my.yhd.com/order/myOrder.do";
  
  public static final String YHD_ORDER_WRITE_COMMENT_URL="http://e.yhd.com/front-pe/pe/orderProductExperience!orderProductExperience.do?soId=%s&userId=%s&soType=0&hasCommented=false";
  
  public static final String YHD_LOGOUT_URL = "https://passport.yhd.com/passport/logoutJsonp.do?timestamp="+new Date().getTime()+"&callback=?";
  
  public static final String YHD_ITEM_URL_PREFIX="http://item.yhd.com/item/";
  
  public static final String YHD_SHOPPING_CAR_URL="http://cart.yhd.com/cart/cart.do?action=view";
  
  public static final String YHD_CHECKOUT_ORDER_URL="http://buy.yhd.com/checkoutV3/index.do";
  
  public static final String YHD_PAYMENT_ORDER_PAGE_URL_PREFIX="http://cashier.yhd.com/order/finishOrder.do";
  
  public static final String YHT_PAGE_URL="http://t.yhd.com";
  
  
  
  public static final String YHD_COMMENT_FILE_NAME_PREFIX="YHD-Comment";
  
  public static final String YHD_ORDER_FILE_NAME_PREFIX="YHD-Order";
  
  public static final String YHD_ACCOUNT_FOLDER_NAME= ACCOUNT_FOLDER_NAME +File.separator + "YHD-Account" + File.separator;
  public static final String YHD_ACCOUNT_LOCKED_FILE_NAME_PREFIX=YHD_ACCOUNT_FOLDER_NAME+"YHD-Account-Locked";
  public static final String YHD_ACCOUNT_COMMENT_FAILED_FILE_NAME_PREFIX=YHD_ACCOUNT_FOLDER_NAME+"YHD-Comment-Failed";
  
  
  
  public static final String JD_LOGIN_PAGE_URL="https://passport.jd.com/uc/login?ReturnUrl=http://order.jd.com/center/list.action";
  public static final String JD_LOGIN_PAGE_URL_WITHOUT_MY_ORDER="https://passport.jd.com/uc/login";
  public static final String JD_MY_ORDER_URL="http://order.jd.com/center/list.action";
  public static final String JD_MY_ORDER_RECYCLE_URL="http://order.jd.com/center/recycle.action?d=1";
  // http://club.jd.com/myJdcomments/orderVoucher.action?ruleid=20596221140&ot=22&payid=4&shipmentid=70
  public static final String JD_ORDER_WRITE_COMMENT_URL="http://club.jd.com/myJdcomments/orderVoucher.action?ruleid=%s&ot=%s&payid=%s&shipmentid=%s";
  public static final String JD_LOGOUT_URL="https://passport.jd.com/uc/login?ltype=logout";
  public static final String JD_COMMENT_SUCCESS_URL="http://club.jd.com/myJdcomments/saveCommentSuccess.action";
  public static final String JD_ACCOUNT_LOCKED_URL_PREFIX="https://safe.jd.com/dangerousVerify";
  
  public static final String NONE_SKU_KEY_PREFIX="NONE-SKU-";
  
  public static final String JD_COMMENT_FILE_NAME_PREFIX="JD-Comment";
  public static final String JD_COULD_NOT_COMMENT_FILE_NAME_PREFIX="JD-CouldNot-Comment";
  
  public static final String JD_ACCOUNT_FOLDER_NAME= ACCOUNT_FOLDER_NAME +File.separator + "JD-Account" + File.separator;
  public static final String JD_ACCOUNT_LOCKED_FILE_NAME_PREFIX=JD_ACCOUNT_FOLDER_NAME + "JD-Account-Locked";
  public static final String JD_ACCOUNT_UNLOCKED_FILE_NAME_PREFIX=JD_ACCOUNT_FOLDER_NAME + "JD-Account-UnLocked";
  public static final String JD_ACCOUNT_COMMENT_FAILED_FILE_NAME_PREFIX=JD_ACCOUNT_FOLDER_NAME + "JD-Comment-Failed";
  
  
  public static final String JD_INDEX_PAGE_URL="https://www.jd.com";
  public static final String JD_ITEM_URL_PREFIX="https://item.jd.com/%s.html";
  public static final String JD_SHOPPING_CAR_URL="https://cart.jd.com/cart.action";
  public static final String JD_CHECKOUT_ORDER_URL="https://trade.jd.com/shopping/order/getOrderInfo.action";
  public static final String JD_PAYMENT_ORDER_PAGE_URL_PREFIX="https://pcashier.jd.com/cashier/index.action";
  
  
}
