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

  public static void main(String[] args) {

    new YHDNormalOrderEntrance().start(args);
    
  }
  
  void start(String[] parameters){
    TestNG testng = new TestNG();
    Class[] classes = new Class[]{NormalOrderNG.class};
    testng.setTestClasses(classes);
    testng.run();
  }
}
