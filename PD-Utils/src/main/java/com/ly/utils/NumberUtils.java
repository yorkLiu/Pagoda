package com.ly.utils;

import java.math.BigDecimal;


/**
 * Created by yongliu on 1/4/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  01/04/2017 16:10
 */
public class NumberUtils {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for mod ten number.
   *
   * @param   intValue  Integer
   *
   * @return  Integer
   */
  public static Integer getModTenNumber(Integer intValue) {
    if (intValue != null) {
      int mod = intValue % 10;

      return intValue - mod;

    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Integer[].
   *
   * @Description  calculate the search prices
   *
   * @param        price        $param.type$
   * @param        startOffset  $param.type$
   * @param        endOffset
   *                            <pre>

         Case One: startOffset and endOffset is "0", means @searchByPriceOffset = 0 or searchByPriceOffset = 0, 0
         Then prices = [floor(price), ceil(price)]
            I.E:
                price = 189.9 then prices = [189, 190]
                price = 146   then prices = [146, 146]

        Case Two: endOffset is not null, means config as @searchByPriceOffset = 0, 10 or @searchByPriceOffset = 10, 20
        Then prices = [floor(price) - startOffset, floor(price) + endOffset]
            I.E:
              price = 100, searchByPriceOffset = 0, 10  then prices = [100, 110]
              price = 132, searchByPriceOffset = 10, 20 then prices = [122, 152]
              price = 148.6, searchByPriceOffset = 10, 20 then prices = [138, 168]

        Case Three: endOffset is null, means config as @searchByPriceOffset = 20
        Then offset = maxPrice - minPrice
            I.E:
              price = 141, searchByPriceOffset=20, then prices = [140, 160]
              price = 149, searchByPriceOffset=20, then prices = [140, 160]
              price = 132.9, searchByPriceOffset=20, then prices = [130, 150]
  
   * </pre>
   *
   * @return       Integer[].
   */
  public static Integer[] getSearchPrices(BigDecimal price, Integer startOffset, Integer endOffset) {
    Integer[] prices       = null;
    Integer   tmpEndOffset = (endOffset == null) ? 0 : endOffset;

    if (price == null || (startOffset == null && endOffset == null)) {
      return null;
    }

    // case startOffset and endOffset is "0"
    // config as @searchByPriceOffset = 0 or searchByPriceOffset = 0, 0
    if (startOffset.equals(0) && tmpEndOffset.equals(0)) {
      // if price = 189.9 then prices = [189, 190]
      // if price = 146   then prices = [146, 146]
      prices = new Integer[] { price.intValue(), new Double(Math.ceil(price.doubleValue())).intValue() };
    } else if (endOffset != null) {
      // endOffset is not null
      // config as @searchByPriceOffset = 0, 10 or @searchByPriceOffset = 10, 20
      // if price = 100, searchByPriceOffset = 0, 10  then prices = [100, 110]
      // if price = 132, searchByPriceOffset = 10, 20 then prices = [122, 152]
      // if price = 148.6, searchByPriceOffset = 10, 20 then prices = [138, 168]
      Integer floorPrice = new Double(Math.floor(price.doubleValue())).intValue();
      prices = new Integer[] { floorPrice - startOffset, floorPrice + tmpEndOffset };
    } else if ((endOffset == null) && (startOffset != null)) {
      // endOffset is null
      // config as @searchByPriceOffset = 20
      // if price = 141, searchByPriceOffset=20, then prices = [140, 160]
      // if price = 149, searchByPriceOffset=20, then prices = [140, 160]
      // if price = 132.9, searchByPriceOffset=20, then prices = [130, 150]
      Integer intPrice   = price.intValue();
      Integer startPrice = getModTenNumber(intPrice);
      Integer endPrice   = startPrice + startOffset;

      prices = new Integer[] { startPrice, endPrice };
    }

    return prices;
  } // end method getSearchPrices

  public static void main(String[] args) {
    Integer prices1[] = getSearchPrices(new BigDecimal("132.5"), 0, 0);
    Integer prices2[] = getSearchPrices(new BigDecimal("89"), 0, 0);
    printArray(prices1);
    printArray(prices2);

    Integer prices3[] = getSearchPrices(new BigDecimal("89"), 0, 10);
    Integer prices4[] = getSearchPrices(new BigDecimal("99.98"), 5, 10);
    printArray(prices3);
    printArray(prices4);

    Integer prices5[] = getSearchPrices(new BigDecimal("99.98"), 10, null);
    Integer prices6[] = getSearchPrices(new BigDecimal("146"), 10, null);
    printArray(prices5);
    printArray(prices6);

    Integer prices7[] = getSearchPrices(new BigDecimal("146"), null, null);
    printArray(prices7);
  }
  
  public static void printArray(Object[] objs){
    if(objs != null){
      for (Object obj : objs) {
        System.out.println(obj);
      }
    } else {
      System.out.println("NULL");
    }
    
  }


} // end class NumberUtils
