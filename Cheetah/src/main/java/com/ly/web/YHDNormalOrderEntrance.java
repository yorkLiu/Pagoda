package com.ly.web;

import com.ly.config.WebDriverProperties;
import com.ly.web.writer.OrderWriter;
import com.ly.web.yhd.NormalOrderNG;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.TestNG;

/**
 * Created by yongliu on 11/2/16.
 */
public class YHDNormalOrderEntrance {

  private String applicationContext = "applicationConfig.xml";
  
  public static void main(String[] args) {

    new YHDNormalOrderEntrance().start(args);
    
  }
  
  void start(String[] parameters){
    ApplicationContext context = new ClassPathXmlApplicationContext(applicationContext);

    context.getAutowireCapableBeanFactory().autowireBeanProperties(this,
      AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    
    OrderWriter orderWriter = context.getBean(OrderWriter.class);
    WebDriverProperties webDriverProperties = context.getBean(WebDriverProperties.class);

   
    // start get beans
    NormalOrderNG normalOrderNG = new NormalOrderNG();
    normalOrderNG.setOrderWriter(orderWriter);
    normalOrderNG.setWebDriverProperties(webDriverProperties);
    normalOrderNG.setVoiceFilePath(webDriverProperties.getWarningVoiceFile());
    // end get beans

    TestNG testng = new TestNG();
    Class[] classes = new Class[]{normalOrderNG.getClass()};
    testng.setTestClasses(classes);
    testng.run();
  }
}
