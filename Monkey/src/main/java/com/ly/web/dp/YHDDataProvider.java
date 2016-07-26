package com.ly.web.dp;

import java.lang.reflect.Method;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.util.Assert;

import org.testng.annotations.DataProvider;

import com.ly.web.command.CommentsInfo;
import com.ly.web.excel.ExcelReader;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/20/2016 10:45
 */
public class YHDDataProvider {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Logger logger = Logger.getLogger(YHDDataProvider.class);

  /** TODO: DOCUMENT ME! */
  public static String path = null;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new YHDDataProvider object.
   */
  public YHDDataProvider() { }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * createCommentsDataProvider.
   *
   * @param   m  Method
   *
   * @return  Iterator
   */
  @DataProvider(name = "dp-yhd-comment")
  public static Iterator<Object[]> createCommentsDataProvider(Method m) {
    if (logger.isDebugEnabled()) {
      logger.debug("Load data from excel path: " + path);
    }

    Assert.notNull(path);

    ExcelReader        excelReader      = new ExcelReader();
    List<CommentsInfo> commentsInfoList = excelReader.readExcelToObj(path);

    List<Object[]> dataToBeReturned = new LinkedList<>();

    for (CommentsInfo commentsInfo : commentsInfoList) {
      dataToBeReturned.add(new Object[] { commentsInfo });
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Found " + dataToBeReturned.size() + " results.");
    }

    return dataToBeReturned.iterator();
  }


} // end class YHDDataProvider
