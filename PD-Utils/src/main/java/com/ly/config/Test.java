package com.ly.config;

import java.math.BigDecimal;

/**
 * Created by yongliu on 11/9/16.
 */
public class Test {

  public static void main(String[] args) {
    BigDecimal price = new BigDecimal("23.9");
    Integer offsets[] = new Integer[]{0, 30};
    int startPrice = price.add(new BigDecimal(offsets[0])).intValue();
    int endPrice = price.add(new BigDecimal(offsets[1])).intValue();

    System.out.println(startPrice + " ~ " + endPrice);
    
  }
}
