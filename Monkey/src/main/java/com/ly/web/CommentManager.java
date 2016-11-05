package com.ly.web;

import com.ly.config.JDConfig;
import com.ly.config.WebDriverProperties;
import com.ly.config.YHDConfig;
import com.ly.utils.TimeUtils;
import com.ly.web.dp.JDDataProvider;
import com.ly.web.dp.YHDDataProvider;
import com.ly.web.jd.JD;
import com.ly.web.lyd.YHD;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import org.testng.TestNG;

import java.util.Properties;

/**
 * Created by yongliu on 11/3/16.
 */
public class CommentManager {

  private static final Log logger = LogFactory.getLog(CommentManager.class);
  
  private String JOB_JD="JD";
  private String JOB_YHD="YHD";
  private String applicationContext = "applicationContext-resources.xml";

  public static void main(String[] args) {
    String[] parameters = new String[args.length];
    logger.warn(parameters);
    System.arraycopy(args, 0, parameters, 0, args.length);

    CommentManager command = new CommentManager();
    command.start(parameters);
  }
  
  public void start(String[] parameters){
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
    if(jobName.equalsIgnoreCase(JOB_JD)){
      TestNG testng = new TestNG();
      Class[] classes = new Class[]{JD.class};
      testng.setTestClasses(classes);
      testng.run();
      
    } else if(jobName.equalsIgnoreCase(JOB_YHD)){
      TestNG testng = new TestNG();
      Class[] classes = new Class[]{YHD.class};

      testng.setTestClasses(classes);
      testng.run();
    }
    
    long endTime = System.currentTimeMillis();
    long spendTime = endTime - startTime;
    logger.info(">>>>>>> Start: " + startTime + ", End: " + endTime + ", Spent Time: " + TimeUtils.toTime(spendTime));
  }
  
}
