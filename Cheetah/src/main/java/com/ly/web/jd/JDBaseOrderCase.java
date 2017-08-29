package com.ly.web.jd;

import com.ly.config.JDOrderConfig;
import com.ly.file.FileWriter;
import com.ly.proxy.PagodaProxyProcessor;
import com.ly.proxy.ProxyProcessor;
import com.ly.web.base.SeleniumBaseObject;
import com.ly.web.command.OrderCommand;
import com.ly.web.constant.Constant;
import com.ly.web.exceptions.AccountLockedException;
import com.ly.web.exceptions.LoginFailedException;
import com.ly.web.writer.OrderWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import org.testng.annotations.BeforeTest;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yongliu on 8/15/17.
 */
public class JDBaseOrderCase extends SeleniumBaseObject {

  /**  DOCUMENT ME! */
  protected static final String   applicationContext = "applicationConfig.xml";

  /** DOCUMENT ME! */
  protected static final String[] JD_Configs = new String[] { "JD-ApplicationConfig.xml" };

  /** DOCUMENT ME! */
  protected final Log logger = LogFactory.getLog(getClass());

  /** DOCUMENT ME! */
  protected List<OrderCommand> orderCommandList = new LinkedList<>();

  /** DOCUMENT ME! */
  @Autowired protected OrderWriter orderWriter;

  @Autowired protected JDOrderConfig jdOrderConfig;

  @Autowired  protected ProxyProcessor proxyProcessor;
  @Autowired  protected PagodaProxyProcessor pagodaProxyProcessor;
  @Autowired  protected FileWriter fileWriter;


  @BeforeTest
  public void init(){
    try {
      ApplicationContext parent = new ClassPathXmlApplicationContext(applicationContext);
      ApplicationContext context = new ClassPathXmlApplicationContext(JD_Configs, parent);
      context.getAutowireCapableBeanFactory().autowireBeanProperties(this,
        AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);

      this.webDriverProperties = jdOrderConfig;
      if (null == orderWriter) {
        orderWriter = (OrderWriter) context.getBean("orderWriter");
      }
      initProperties();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  @Override protected void initProperties() {
    super.initProperties();
  }

  protected boolean login(OrderCommand orderCommand) throws LoginFailedException {
    boolean loginSuccess = Boolean.TRUE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Starting login....");
        logger.debug("Open Login Page:" + Constant.JD_LOGIN_PAGE_URL_WITHOUT_MY_ORDER);
      }

      com.ly.web.jd.Login login = new com.ly.web.jd.Login(driver, Constant.JD_LOGIN_PAGE_URL_WITHOUT_MY_ORDER, voiceFilePath);

      loginSuccess = login.login(orderCommand.getUsername(), orderCommand.getPassword(), true);

      if (logger.isDebugEnabled()) {
        logger.debug("Login successfully for user: " + orderCommand.getUsername());
      }
      
      if(!loginSuccess){
        throw new LoginFailedException("Failed to login with user: " + orderCommand.getUsername());
      }

    } catch (AccountLockedException e){
      loginSuccess = Boolean.FALSE;
      // write this orderNo to file.
      if(orderWriter != null){
        String noneOrderId="None-Order-ID";
        String content = StringUtils.arrayToDelimitedString(new String[]{orderCommand.getUsername(), orderCommand.getPassword(), noneOrderId}, "/");
        orderWriter.writeToFileln(Constant.JD_ACCOUNT_LOCKED_FILE_NAME_PREFIX, content);
      }
      logger.error(e.getMessage(), e);
    } catch (Exception e) {
      loginSuccess = Boolean.FALSE;
      logger.error(e.getMessage());
      throw new LoginFailedException(e);
    }

    return loginSuccess;
  }

  public void setJdOrderConfig(JDOrderConfig jdOrderConfig) {
    this.jdOrderConfig = jdOrderConfig;
  }

  public void setProxyProcessor(ProxyProcessor proxyProcessor) {
    this.proxyProcessor = proxyProcessor;
  }

  public void setPagodaProxyProcessor(PagodaProxyProcessor pagodaProxyProcessor) {
    this.pagodaProxyProcessor = pagodaProxyProcessor;
  }

  public void setOrderWriter(OrderWriter orderWriter) {
    this.orderWriter = orderWriter;
  }

  public void setFileWriter(FileWriter fileWriter) {
    this.fileWriter = fileWriter;
  }
}
