package com.ly.web;

import java.util.Properties;

import com.ly.utils.TimeUtils;
import com.ly.web.jd.JDOrderCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;

import org.testng.TestNG;

import com.ly.web.yhd.NormalOrderCase;
import com.ly.web.yhd.YHTOrderCase;


/**
 * Created by yongliu on 11/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/14/2016 15:29
 */
public class OrderEntrance {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog(OrderEntrance.class);

  private static final String JOB_YHD = "YHD";
  private static final String JOB_YHT = "YHT";
  private static final String JOB_JD = "JD";

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * main.
   *
   * @param  args  String[]
   */
  public static void main(String[] args) {
    String[] parameters = new String[args.length];
    logger.warn(parameters);
    System.arraycopy(args, 0, parameters, 0, args.length);

    OrderEntrance command = new OrderEntrance();
    command.start(parameters);

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * start.
   *
   * @param   parameters  String[]
   *
   * @throws  IllegalArgumentException  exception
   */
  void start(String[] parameters) {
    Properties properties = StringUtils.splitArrayElementsIntoProperties(
        parameters, "=");

    if (properties == null) {
      throw new IllegalArgumentException("Comment job name is required.");
    }

    String jobName = (String) properties.get("job");

    if (jobName == null) {
      throw new IllegalArgumentException("Comment job name is required.");
    }

    properties.setProperty("job", jobName);

    long startTime = System.currentTimeMillis();

    if (jobName.equalsIgnoreCase(JOB_YHD)) {
      TestNG  testng  = new TestNG();
      Class[] classes = new Class[] { NormalOrderCase.class };
      testng.setTestClasses(classes);
      testng.run();

    } else if (jobName.equalsIgnoreCase(JOB_YHT)) {
      TestNG  testng  = new TestNG();
      Class[] classes = new Class[] { YHTOrderCase.class };

      testng.setTestClasses(classes);
      testng.run();
    } else if (jobName.equalsIgnoreCase(JOB_JD)){
      TestNG  testng  = new TestNG();
      Class[] classes = new Class[] { JDOrderCase.class };

      testng.setTestClasses(classes);
      testng.run();
    }

    long endTime   = System.currentTimeMillis();
    long spendTime = endTime - startTime;
    logger.info(">>>>>>> Start: " + startTime + ", End: " + endTime + ", Spent Time: " + TimeUtils.toTime(spendTime));
  } // end method start
} // end class OrderEntrance
