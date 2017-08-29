package com.ly.config;

import java.math.BigDecimal;

import com.ly.utils.ExcelUtils;
import org.springframework.util.StringUtils;

import com.ly.utils.Constants;


/**
 * Created by yongliu on 11/6/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/07/2016 11:12
 */
public class BaseOrderConfig extends WebDriverProperties {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** The goal production page, stay @browserTime seconds. */
  protected Integer browserTime;


  /** goods compare count, default is 3. */
  protected Integer compareGoodsCount = 3;

  /**
   * The max search page num, after current page num greater than @maxSearchPageNum, will search by price,.
   *
   * @maxSearchPageNum  property value could be set on config file. If @maxSearchPageNum value equal or less than 0,
   *                    means would not fine production by price.
   */
  protected Integer maxSearchPageNum;

  /**
   * The generated order file name prefix i.e: orderFileNamePrefix=YHD-example the the order file name is:
   * YHD-example-todayDate(2016-10-10)
   */
  protected String orderFileNamePrefix;

  /** The search price offset: start, end */
  protected String searchByPriceOffset;

  /**
   * The flag of turn on or off to get IP Proxy. If @useIpProxy=TRUE, then will get ip proxy from proxy server. else
   * will not. By default is 'FALSE'
   * <code>Value: TRUE, Y, FALSE, N</code>
   */
  protected String useIpProxy;

  /**
   * How many orders for per instance
   * i.e:
   *  instance 1: orderRange=1-10  (means instance 1 will only process 1-10 orders)
   *  instance 2: orderRange=11-20 (means instance 2 will only process 2-20 orders)
   */
  protected String orderRange;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.config.WebDriverProperties#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();
    logger.info("orderFileNamePrefix: " + orderFileNamePrefix);
    logger.info("compareGoodsCount: " + compareGoodsCount);
    logger.info("browserTime: " + browserTime + " second(s)");
    logger.info("maxSearchPageNum: " + maxSearchPageNum);
    logger.info("searchByPriceOffset: " + searchByPriceOffset);
    logger.info("useIpProxy: " + getUseIpProxy());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for browser time.
   *
   * @return  Integer
   */
  public Integer getBrowserTime() {
    if (browserTime == null) {
      return 0;
    }

    return browserTime;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for compare goods count.
   *
   * @return  Integer
   */
  public Integer getCompareGoodsCount() {
    if (compareGoodsCount == null) {
      return 3;
    }

    return compareGoodsCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for max search page num.
   *
   * @return  Integer
   */
  public Integer getMaxSearchPageNum() {
    return maxSearchPageNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for order file name prefix.
   *
   * @return  String
   */
  public String getOrderFileNamePrefix() {
    return orderFileNamePrefix;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price offsets.
   *
   * @return  Integer[]
   */
  public Integer[] getPriceOffsets() {
    Integer[] priceOffsets = new Integer[] { 0, 0 };

    if ((searchByPriceOffset != null) && StringUtils.hasText(searchByPriceOffset)) {
      try {
        String[] offsets = StringUtils.delimitedListToStringArray(StringUtils.deleteAny(searchByPriceOffset,
              Constants.SPACER),
            ",");

        switch (offsets.length) {
          case 1: {
            int startOffset = parseIntSafe(offsets[0].trim());
            priceOffsets[0] = startOffset;
            priceOffsets[1] = null;

            break;
          }

          default: {
            int startOffset = parseIntSafe(offsets[0].trim());
            int endOffset   = parseIntSafe(offsets[1].trim());
            priceOffsets[0] = startOffset;
            priceOffsets[1] = endOffset;
          }
        }
      } catch (Exception e) {
        logger.error(e.getMessage());
      }
    } // end if

    return priceOffsets;
  } // end method getPriceOffsets
  
  public Integer[] getRanges(){
    Integer[] ranges = null;
    
    if(orderRange != null && StringUtils.hasText(orderRange)){
      try {
        String[] tmpRanges = StringUtils.delimitedListToStringArray(StringUtils.deleteAny(orderRange,
          Constants.SPACER), "-");
        if (tmpRanges.length > 0) {
          ranges = new Integer[2];
        }

        switch (tmpRanges.length) {
          case 2: {
            ranges[0] = parseIntSafe(tmpRanges[0].trim());
            ranges[1] = parseIntSafe(tmpRanges[1].trim());

            break;
          }
          case 1: {
            ranges[0] = parseIntSafe(tmpRanges[0].trim());
            ranges[1] = null;

            break;
          }
        }
      }catch (Exception e){
        logger.error(e.getMessage());
      }
    }
    
    return ranges;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for search by price offset.
   *
   * @return  String
   */
  public String getSearchByPriceOffset() {
    return searchByPriceOffset;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for search end price.
   *
   * @param   price  BigDecimal
   *
   * @return  Integer
   */
  public Integer getSearchEndPrice(BigDecimal price) {
    if (price != null) {
      Integer[] offsets = getPriceOffsets();

      return price.add(new BigDecimal(offsets[1])).intValue();
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for search start price.
   *
   * @param   price  BigDecimal
   *
   * @return  Integer
   */
  public Integer getSearchStartPrice(BigDecimal price) {
    if (price != null) {
      Integer[] offsets = getPriceOffsets();

      return price.add(new BigDecimal(offsets[0])).intValue();
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for use ip proxy.
   *
   * @return  Boolean
   */
  public Boolean getUseIpProxy() {
    if (null == useIpProxy) {
      return Boolean.FALSE;
    }

    return ExcelUtils.getBooleanValue(useIpProxy);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for browser time.
   *
   * @param  browserTime  Integer
   */
  public void setBrowserTime(Integer browserTime) {
    this.browserTime = browserTime;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for compare goods count.
   *
   * @param  compareGoodsCount  Integer
   */
  public void setCompareGoodsCount(Integer compareGoodsCount) {
    this.compareGoodsCount = compareGoodsCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for max search page num.
   *
   * @param  maxSearchPageNum  Integer
   */
  public void setMaxSearchPageNum(Integer maxSearchPageNum) {
    this.maxSearchPageNum = maxSearchPageNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for order file name prefix.
   *
   * @param  orderFileNamePrefix  String
   */
  public void setOrderFileNamePrefix(String orderFileNamePrefix) {
    this.orderFileNamePrefix = orderFileNamePrefix;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for search by price offset.
   *
   * @param  searchByPriceOffset  String
   */
  public void setSearchByPriceOffset(String searchByPriceOffset) {
    this.searchByPriceOffset = searchByPriceOffset;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for use ip proxy.
   *
   * @param  useIpProxy  Boolean
   */
  public void setUseIpProxy(String useIpProxy) {
    this.useIpProxy = useIpProxy;
  }

  public String getOrderRange() {
    return orderRange;
  }

  public void setOrderRange(String orderRange) {
    this.orderRange = orderRange;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private int parseIntSafe(String intVal) {
    int result = 0;

    try {
      if ((intVal != null) && StringUtils.hasText(intVal)) {
        result = Integer.parseInt(intVal.trim());
      }
    } catch (NumberFormatException e) {
      logger.error(e.getMessage());
    }

    return result;
  }
} // end class BaseOrderConfig
