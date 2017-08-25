package com.ly.web.writer;

import java.io.File;

import java.util.Date;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.util.StringUtils;

import com.ly.file.FileWriter;

import com.ly.utils.DateUtil;
import com.ly.utils.FileUtils;

import com.ly.web.command.OrderCategory;
import com.ly.web.command.OrderResultInfo;


/**
 * Created by yongliu on 11/1/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/01/2016 16:18
 */
public class OrderWriter extends FileWriter {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String fileNamePrefix;

  private String header;

  private String[] headers;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.file.FileWriter#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();
    logger.info("fileNamePrefix is: " + fileNamePrefix);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for file name prefix.
   *
   * @return  String
   */
  public String getFileNamePrefix() {
    return fileNamePrefix;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for headers.
   *
   * @return  String[]
   */
  public String[] getHeaders() {
    return headers;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for file name prefix.
   *
   * @param  fileNamePrefix  String
   */
  public void setFileNamePrefix(String fileNamePrefix) {
    this.fileNamePrefix = fileNamePrefix;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for headers.
   *
   * @param  headers  String[]
   */
  public void setHeaders(String[] headers) {
    this.headers = headers;
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
    String now      = DateUtil.formatDate(new Date(), DateUtil.DATETIME_FORMATTER);
    String filePath = getFilePath(((fileNamePrefix != null) && StringUtils.hasText(fileNamePrefix))
          ? fileNamePrefix : orderCategory.toString());

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

        // keyword
        orderResultInfo.getKeyword(),

        // full name
        orderResultInfo.getAddressInfo().getFullName(),

        // full address
        orderResultInfo.getAddressInfo().getFullAddress(),

        // telephone number
        orderResultInfo.getAddressInfo().getTelephoneNum(),

        // order price
        orderResultInfo.getPrice(),

        // append order date
        ((appendOrderDateTime != null) && appendOrderDateTime) ? now : "",

        // group name (merchant name)
        orderResultInfo.getGroupName(),

        // new line
        DEFAULT_LINE_SEPARATOR
      }, getHeader(), delimiter);

  } // end method writeOrderInfo

  public void writeOrderInfo2(OrderCategory orderCategory, OrderResultInfo orderResultInfo,
                             Boolean appendOrderDateTime) {
    String now      = DateUtil.formatDate(new Date(), DateUtil.DATETIME_FORMATTER);
    String filePath = getFilePath(((fileNamePrefix != null) && StringUtils.hasText(fileNamePrefix))
      ? fileNamePrefix : orderCategory.toString());

    FileUtils.writeContentToFile(filePath,
      new Object[] {
        // store name
        orderResultInfo.getStoreName(),
        
        // orderNo
        orderResultInfo.getOrderNo(),
        
        // username
        orderResultInfo.getUsername(),

        // password
        orderResultInfo.getPassword(),
        
        // keyword
        orderResultInfo.getKeyword(),

        // full name
        orderResultInfo.getAddressInfo().getFullName(),

        // full address
        orderResultInfo.getAddressInfo().getFullAddress(),

        // telephone number
        orderResultInfo.getAddressInfo().getTelephoneNum(),

        // order price
        orderResultInfo.getPrice(),

        // group name (merchant name)
        orderResultInfo.getGroupName(),
        
        // use ip proxy
        orderResultInfo.getUsedIpProxy(),

        // append order date
        ((appendOrderDateTime != null) && appendOrderDateTime) ? now : "",

        // new line
        DEFAULT_LINE_SEPARATOR
      } ,
//      getHeader(),
//      delimiter
      "|"
    );

  } // end method writeOrderInfo
  

  //~ ------------------------------------------------------------------------------------------------------------------

  private String getHeader() {
    if ((headers != null) && (headers.length > 0) && (header == null)) {
      header = StringUtils.arrayToDelimitedString(headers, delimiter);
    }

    return header;
  }
} // end class OrderWriter
