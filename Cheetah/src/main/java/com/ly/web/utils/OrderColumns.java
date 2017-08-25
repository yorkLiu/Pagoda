package com.ly.web.utils;

/**
 * Created by yongliu on 11/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/09/2016 16:50
 */
public class OrderColumns {

  /**username*/
  public static final int USER_NAME_COLUMN=0;
  /**password*/
  public static final int PASSWORD_COLUMN=1;
  /**收货人姓名*/
  public static final int RECEIVER_NAME_COLUMN=2;
  /**收货人地址*/
  public static final int RECEIVE_ADDRESS_COLUMN=3;
  /**收货人手机号*/
  public static final int RECEIVE_MOBILE_PHONE_COLUMN=4;

  /**是否是海购*/
  public static final int ALLOW_OVERSEA_COLUMN=5;
  
  /**收货人identity car number (only for 海购)*/
  public static final int RECEIVE_IDENTITY_CARD_NUM_COLUMN=6;
  /**province*/
  public static final int RECEIVE_PROVINCE_COLUMN=7;
  /**city, 市*/
  public static final int RECEIVE_CITY_COLUMN=8;
  /**country 区/县*/
  public static final int RECEIVE_COUNTRY_COLUMN=9;
  /**twon 镇/街道*/
  public static final int RECEIVE_TOWN_COLUMN=10;
  /**订单备注*/
  public static final int RECEIVE_ORDER_COMMENT_COLUMN=11;
  
  /**商品链接*/
  public static final int PRODUCTION_URL_COLUMN=12;
  /**商品名称*/
  public static final int PRODUCTION_NAME_COLUMN=13;
  /**商品价格*/
  public static final int PRODUCTION_PRICE_COLUMN=14;
  /**关键词*/
  public static final int PRODUCTION_KEYWORD_COLUMN=15;
  /**购买数量*/
  public static final int PRODUCTION_COUNT_COLUMN=16;
  /**加收藏?*/
  public static final int PRODUCTION_ADD_TO_FAVORITE_COLUMN=17;
  
}
