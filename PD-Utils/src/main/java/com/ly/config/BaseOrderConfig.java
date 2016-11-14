package com.ly.config;

import com.ly.utils.Constants;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;


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
   * The generated order file name prefix i.e: orderFileNamePrefix=YHD-example the the order file name is:
   * YHD-example-todayDate(2016-10-10)
   */
  protected String orderFileNamePrefix;

  /** The search price offset: start, end */
  protected String searchByPriceOffset;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.config.WebDriverProperties#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();
    logger.info("orderFileNamePrefix: " + orderFileNamePrefix);
    logger.info("compareGoodsCount: " + compareGoodsCount);
    logger.info("browserTime: " + browserTime + " second(s)");
    logger.info("searchByPriceOffset: " + searchByPriceOffset);
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
   * getter method for order file name prefix.
   *
   * @return  String
   */
  public String getOrderFileNamePrefix() {
    return orderFileNamePrefix;
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
  
  /**
   * getter method for price offsets.
   *
   * @return  Integer[]
   */
  public Integer[] getPriceOffsets() {
    Integer[] priceOffsets = new Integer[] { 0, 0 };

    if ((searchByPriceOffset != null) && StringUtils.hasText(searchByPriceOffset)) {
      if (searchByPriceOffset.contains(",")) {
        try {
          String[] offsets = StringUtils.delimitedListToStringArray(StringUtils.deleteAny(searchByPriceOffset, Constants.SPACER),
              ",");

          switch (offsets.length) {
            case 1: {
              int startOffset = Integer.parseInt(offsets[0].trim());
              priceOffsets[0] = startOffset;
              priceOffsets[1] = startOffset;

              break;
            }

            default: {
              int startOffset = Integer.parseInt(offsets[0].trim());
              int endOffset   = Integer.parseInt(offsets[1].trim());
              priceOffsets[0] = startOffset;
              priceOffsets[1] = endOffset;
            }
          }
        } catch (Exception e) {
          logger.error(e.getMessage());
        }
      } // end if
    } // end if

    return priceOffsets;
  } // end method getPriceOffsets
  
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

} // end class BaseOrderConfig
