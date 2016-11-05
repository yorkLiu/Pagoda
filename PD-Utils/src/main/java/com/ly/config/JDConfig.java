package com.ly.config;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by yongliu on 11/4/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/04/2016 10:19
 */
public class JDConfig extends WebDriverProperties {
  //~ Instance fields --------------------------------------------------------------------------------------------------
  
  private static final String SPACE=" ";

  /** for comment select the tag, @exclusiveTags will be exclude (not select). */
  private String exclusiveTags;
  

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.config.WebDriverProperties#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();
    logger.info("exclusiveTags: " + exclusiveTags);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for exclusive tags.
   *
   * @return  String
   */
  public String getExclusiveTags() {
    return exclusiveTags;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for exclusive tags.
   *
   * @param  exclusiveTags  String
   */
  public void setExclusiveTags(String exclusiveTags) {
    byte[] bytes = exclusiveTags.getBytes();
    try {
      exclusiveTags = new String(bytes, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    this.exclusiveTags = exclusiveTags;
  }
  
  public String[] getExcludeTags(){
    return StringUtils.delimitedListToStringArray(exclusiveTags, ",", SPACE);
  }
} // end class JDConfig
