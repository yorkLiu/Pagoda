package com.ly.web.dp;

import java.lang.reflect.Method;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import org.springframework.util.Assert;

import org.testng.annotations.DataProvider;

import com.ly.utils.FileUtils;

import com.ly.web.command.OrderCommand;
import com.ly.web.reader.ExcelReader;


/**
 * Created by yongliu on 11/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/10/2016 09:50
 */
public class YHDOrderDataProvider {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Logger logger = Logger.getLogger(JDDataProvider.class);

  /** DOCUMENT ME! */
  public static String normalOrderPath = null;

  /** TODO: DOCUMENT ME! */
  public static String yhtOrderPath = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * createNormalOrderDataProvider.
   *
   * @param   m  Method
   *
   * @return  Iterator
   */
  @DataProvider(name = "dp-yhd-normal-order")
  public static Iterator<Object[]> createNormalOrderDataProvider(Method m) {
    if (logger.isDebugEnabled()) {
      logger.debug("Load data from excel path: " + normalOrderPath);
    }

    Assert.notNull(normalOrderPath);

    List<OrderCommand> orderInfoList   = new LinkedList<>();
    List<Object[]>     sortedOrderList = new LinkedList<>();

    Set<String> files = FileUtils.getExcelFileFromPath(normalOrderPath);

    for (String file : files) {
      orderInfoList.addAll(new ExcelReader().readYHDNormalOrderExcelToObject(file));
    }


    // TODO sort
    // Map<String, List<OrderCommand>> groups = orderInfoList.stream().collect(Collectors.groupingBy(OrderCommand::getGroupName));


    for (OrderCommand orderCommand : orderInfoList) {
      sortedOrderList.add(new Object[] { orderCommand });
    }


    return sortedOrderList.iterator();
  } // end method createNormalOrderDataProvider

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * createYHTOrderDataProvider.
   *
   * @param   m  Method
   *
   * @return  Iterator
   */
  @DataProvider(name = "dp-yhd-t-order")
  public static Iterator<Object[]> createYHTOrderDataProvider(Method m) {
    if (logger.isDebugEnabled()) {
      logger.debug("Load data from excel path: " + yhtOrderPath);
    }

    Assert.notNull(yhtOrderPath);

    List<OrderCommand> orderInfoList   = new LinkedList<>();
    List<Object[]>     sortedOrderList = new LinkedList<>();

    Set<String> files = FileUtils.getExcelFileFromPath(yhtOrderPath);

    for (String file : files) {
      orderInfoList.addAll(new ExcelReader().readYHTOrderExcelToObject(file));
    }


    // TODO sort
    // Map<String, List<OrderCommand>> groups = orderInfoList.stream().collect(Collectors.groupingBy(OrderCommand::getGroupName));


    for (OrderCommand orderCommand : orderInfoList) {
      sortedOrderList.add(new Object[] { orderCommand });
    }


    return sortedOrderList.iterator();
  } // end method createYHTOrderDataProvider

} // end class YHDOrderDataProvider
