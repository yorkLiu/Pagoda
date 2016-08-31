package com.ly.sort.constant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * Created by yongliu on 8/1/16.
 */
public class Constant {

  protected final Log logger = LogFactory.getLog(getClass());


  /**0: page
   * 1: keyword
   */
  public static final String BASE_SEARCH_URL_EVEN_NUM_PAGING_FIRST ="http://search.yhd.com/searchPage/c0-0-0/b/a-s1-v4-p%s-price-d0-f0-m1-rt0-pid-mid0-k%s";
  /**0: page
   * 1: keyword
   */
  public static final String BASE_SEARCH_URL_EVEN_NUM_PAGING_SECOND ="http://search.yhd.com/searchPage/c0-0-0/b/a-s1-v4-p%s-price-d0-f0-m1-rt0-pid-mid0-k%s/?&isGetMoreProducts=1&moreProductsDefaultTemplate=0&isLargeImg=0&moreProductsFashionCateType=2&nextAdIndex=28&nextImageAdIndex=2&adProductIdListStr=&fashionCateType=2&firstPgAdSize=0&_=" + new Date().getTime();
  
  
}
