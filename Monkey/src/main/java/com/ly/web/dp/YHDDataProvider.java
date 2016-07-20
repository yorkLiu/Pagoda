package com.ly.web.dp;

import com.ly.web.command.CommentsInfo;
import com.ly.web.excel.ExcelReader;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/20/2016 10:45
 */
public class YHDDataProvider {
  
  public YHDDataProvider(){
    
  }
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * createCommentsDataProvider.
   *
   * @return  Iterator
   */
  @DataProvider(name = "dp-yhd-comment")
  public static Iterator<Object[]> createCommentsDataProvider(Method m) {
    String             excelPath        = "";
    ExcelReader        excelReader      = new ExcelReader();
    List<CommentsInfo> commentsInfoList = excelReader.readExcelToObj(excelPath);

    List<Object[]> dataToBeReturned = new LinkedList<>();
    for (CommentsInfo commentsInfo : commentsInfoList) {
      dataToBeReturned.add(new Object[]{commentsInfo});
    }
    

    return dataToBeReturned.iterator();
  }

  

} // end class YHDDataProvider
