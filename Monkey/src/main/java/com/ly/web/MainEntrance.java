package com.ly.web;

import com.ly.web.dp.YHDDataProvider;
import com.ly.web.lyd.YHD;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.TestNG;


/**
 * Created with IntelliJ IDEA. User: yongliu Date: 8/20/14 Time: 12:43 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author $author$
 * @version $Revision$, $Date$
 */
public class MainEntrance {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param args DOCUMENT ME!
   */
  public static void main(String[] args) {
    try {
      
      // args path
      String path = "/Users/yongliu/Downloads/7.18YHD.xlsx";
      YHDDataProvider.path = path;
      
      TestNG testng = new TestNG();
      Class[] classes = new Class[]{YHD.class};
      testng.setTestClasses(classes);
      testng.run();

//      if ((null != args) && (args.length > 0)) {
//        String filePath = args[0];
//        logger.info("filePath : " + filePath);
//
//        TestNGCase.configFilePath = filePath;
//
//        TestNG testng = new TestNG();
//        Class[] classes = new Class[]{TestNGCase.class};
//        testng.setTestClasses(classes);
//        testng.run();
//      } else {
//        if (logger.isDebugEnabled()) {
//          logger.debug("the parameter is null, run test failed");
//        }
//      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  } // end method main
} // end class MainEntrance
