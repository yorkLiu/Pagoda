package com.ly.web;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;

import org.testng.TestNG;

import com.ly.utils.TimeUtils;

import com.ly.web.jd.JD;
import com.ly.web.lyd.YHD;


/**
 * Created by yongliu on 11/3/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/07/2016 17:12
 */
public class CommentManager {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog(CommentManager.class);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private final String JOB_JD  = "JD";
  private final String JOB_YHD = "YHD";

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

    CommentManager command = new CommentManager();
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
  public void start(String[] parameters) {
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

    if (jobName.equalsIgnoreCase(JOB_JD)) {
      TestNG  testng  = new TestNG();
      Class[] classes = new Class[] { JD.class };
      testng.setTestClasses(classes);
      testng.run();

    } else if (jobName.equalsIgnoreCase(JOB_YHD)) {
      TestNG  testng  = new TestNG();
      Class[] classes = new Class[] { YHD.class };

      testng.setTestClasses(classes);
      testng.run();
    }

    long endTime   = System.currentTimeMillis();
    long spendTime = endTime - startTime;
    logger.info(">>>>>>> Start: " + startTime + ", End: " + endTime + ", Spent Time: " + TimeUtils.toTime(spendTime));
  } // end method start

} // end class CommentManager
