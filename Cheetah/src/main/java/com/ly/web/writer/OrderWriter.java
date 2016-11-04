package com.ly.web.writer;

import java.io.File;

import java.util.Date;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.InitializingBean;

import com.ly.utils.DateUtil;
import com.ly.utils.FileUtils;

import com.ly.web.command.OrderCategory;
import com.ly.web.command.OrderResultInfo;
import org.springframework.util.StringUtils;


/**
 * Created by yongliu on 11/1/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/01/2016 16:18
 */
public class OrderWriter implements InitializingBean {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 3561817317629600898L;

  private static final String TXT_FILE = ".txt";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String workDir;
  
  private String delimiter;

  private Logger logger = Logger.getLogger(getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    logger.info("The file dir is: " + workDir);
    logger.info("The delimiter is: " + delimiter);
  }
  
  public OrderWriter(){}

  //~ ------------------------------------------------------------------------------------------------------------------


  public void setWorkDir(String workDir) {
    if(workDir != null && StringUtils.hasText(workDir)){
      this.workDir = StringUtils.cleanPath(workDir);
    }
  }

  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * writeOrderInfo.
   *
   * @param  orderCategory        OrderCategory (YHD, JD)
   * @param  orderResultInfo      OrderResultInfo
   * @param  appendOrderDateTime  Boolean
   */
  public void writeOrderInfo(OrderCategory orderCategory, OrderResultInfo orderResultInfo,
    Boolean appendOrderDateTime) {
    File   dir = new File(workDir);
    String now = DateUtil.formatDate(new Date(), DateUtil.DATETIME_FORMATTER);

    if (!dir.exists()) {
      dir.mkdirs();
    }

    if (!workDir.endsWith(File.separator)) {
      workDir = workDir + File.separator;
    }

    String filePath = workDir + generateFileName(orderCategory.toString());

    FileUtils.writeContentToFile(filePath,
      new Object[] {
        // username
        orderResultInfo.getUsername(),
        
        // password
        orderResultInfo.getPassword(),
        
        // payment url
        orderResultInfo.getPaymentUrl(),

        // orderNo
        orderResultInfo.getOrderNo(),

        // full name
        orderResultInfo.getAddressInfo().getFullName(),

        // full address
        orderResultInfo.getAddressInfo().getFullAddress(),

        // telephone number
        orderResultInfo.getAddressInfo().getTelephoneNum(),

        // order price
        orderResultInfo.getPrice(),

        // append order date
        ((appendOrderDateTime != null) && appendOrderDateTime) ? now : ""
      }, delimiter);

  } // end method writeOrderInfo

  //~ ------------------------------------------------------------------------------------------------------------------

  private String generateFileName(String prefix) {
    StringBuilder fileName  = new StringBuilder(prefix).append("-");
    String        dateStamp = DateUtil.formatDateOnly(new Date());
    fileName.append(dateStamp).append(TXT_FILE);

    return fileName.toString();
  }

} // end class OrderWriter
