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
public class JDOrderDataProvider {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Logger logger = Logger.getLogger(JDOrderDataProvider.class);

  /** DOCUMENT ME! */
  public static String jdOrderPath = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * createNormalOrderDataProvider.
   *
   * @param   m  Method
   *
   * @return  Iterator
   */
  @DataProvider(name = "dp-jd-normal-order")
  public static Iterator<Object[]> createNormalOrderDataProvider(Method m) {
    if (logger.isDebugEnabled()) {
      logger.debug("Load data from excel path: " + jdOrderPath);
    }

    Assert.notNull(jdOrderPath);

    List<OrderCommand> orderInfoList   = new LinkedList<>();
    List<Object[]>     sortedOrderList = new LinkedList<>();

    Set<String> files = FileUtils.getExcelFileFromPath(jdOrderPath);

    try {
      for (String file : files) {
        orderInfoList.addAll(new ExcelReader().readJDOrdersFromExcelToObject(file));
      }
    }catch (Exception e){
      logger.error(e.getMessage(), e);
    }


    // TODO sort
    // Map<String, List<OrderCommand>> groups = orderInfoList.stream().collect(Collectors.groupingBy(OrderCommand::getGroupName));


    for (OrderCommand orderCommand : orderInfoList) {
      sortedOrderList.add(new Object[] { orderCommand });
    }


    return sortedOrderList.iterator();
  } // end method createNormalOrderDataProvider


} // end class JDOrderDataProvider
