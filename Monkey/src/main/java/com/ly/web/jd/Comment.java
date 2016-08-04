package com.ly.web.jd;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ly.web.base.AbstractObject;
import com.ly.web.constant.Constant;


/**
 * Created by yongliu on 8/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/03/2016 13:12
 */
public class Comment extends AbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String ORDER_TR_ID_PREFIX = "track%s";

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new Comment object.
   */
  public Comment() { }

  /**
   * Creates a new Comment object.
   *
   * @param  driver  WebDriver
   */
  public Comment(WebDriver driver) {
    Assert.notNull(driver);
    this.webDriver = driver;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * comment.
   *
   * @param   orderId     String
   * @param   commentMap  Map
   *
   * @throws  Exception  exception
   */
  public void comment(String orderId, Map<String, String> commentMap) throws Exception {
    String myOrderUrl = getCommentUrl(orderId);
    Assert.notNull(orderId);


    if (logger.isDebugEnabled()) {
      logger.debug("Confirm receipt the order#" + orderId);
    }

    if (!webDriver.getCurrentUrl().contains("order")) {
      navigateTo(myOrderUrl);
    }


  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * String.
   *
   * @param         orderId  $param.type$
   *
   * @return        String.
   *
   * @Description:  <pre>
       find the tr attr oty
        i.e: oty='22, 4, 70'
        oty[0] _otype     in comment url is 'ot'
        oty[1] _paytype   in comment url is 'payid'
        oty[2] _shipType  in comment url is 'shipmentid'
   * </pre>
   */
  private String getCommentUrl(String orderId) {
    String     url       = null;
    String     orderTrId = String.format(ORDER_TR_ID_PREFIX, orderId);
    WebElement trEle     = webDriver.findElement(By.id(orderTrId));

    if (trEle != null) {
      String oty = trEle.getAttribute("oty");

      if ((oty != null) && StringUtils.hasText(oty)) {
        String[] otys = oty.trim().split(",");

        if (otys.length >= 3) {
          url = String.format(Constant.JD_ORDER_WRITE_COMMENT_URL, orderId, otys[0], otys[1], otys[2]);
        }
      }
    }

    return url;

  }
} // end class Comment
