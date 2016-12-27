package com.ly.spark;

import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yongliu on 12/9/16.
 */
public class YHDLog {

  /**
   * orderNum          username      index         spent time        tag
   * 46570384551 -- [skht44707] -- [1/25] -- Spent: 93216 ms. Commented Successfully.
   *
   *  orderNum          username              index                  spent time         tag
   * "(\\w+) \\-{2} \\[(\\w+)\\] \\-{2} \\[(\\d+/\\d+)\\] \\-{2} \\w+: (\\d+) \\w+. (\\w+\\s+\\w+)";
   */
  private static final String LOG_ENTRY_PATTERN = "(\\w+) \\-{2} \\[(\\w+)\\] \\-{2} \\[(\\d+/\\d+)\\] \\-{2} \\w+: (\\d+) \\w+. (\\w+\\s+\\w+)";
  private static final Pattern PATTERN = Pattern.compile(LOG_ENTRY_PATTERN);
  private static Logger logger = Logger.getLogger("YHDLogAnalyzer");

  private String orderNum;
  
  private String username;
  
  private String index;
  
  private long spentTime;
  
  private String tag;
  
  public YHDLog(){}
  
  public YHDLog(String orderNum, String username, String index, String spentTime, String tag){
    this.orderNum = orderNum;
    this.username = username;
    this.index = index;
    this.spentTime = Long.parseLong(spentTime);
    this.tag = tag;
  }
  
  public static YHDLog parseFromLogLine(String logline){
    String []loglines = logline.split("\\|");
    if(loglines.length>1){
      logline =loglines[1].trim();
    }
    
    Matcher m = PATTERN.matcher(logline);
    YHDLog yhdLog = null;
    if(m.find()){
      yhdLog = new YHDLog(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5));
    }
    
//    if (!m.find()) {
//      logger.log(Level.ALL, "Cannot parse logline{0}", logline);
//      throw new RuntimeException("Error parsing logline");
//    }
    
    return yhdLog;
    
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }

  public String getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  public static Pattern getPATTERN() {
    return PATTERN;
  }

  public long getSpentTime() {
    return spentTime;
  }

  public void setSpentTime(long spentTime) {
    this.spentTime = spentTime;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


    public static void main(String[] args) {
    String logline = "[Monkey] 2016-12-09 13:51:56,853 INFO [main] com.ly.web.jd.JD.readComment(218) | 46570384551 -- [skht44707] -- [1/25] -- Spent: 93216 ms. Commented Successfully.";
      logline = logline.split("\\|")[1];
      System.out.println("logline:" + logline);
    Matcher m = PATTERN.matcher(logline);
    if(m.find()){
      printf(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5));
    }
      
  }
  
  public static void printf(String...obj){
    StringBuffer sb = new StringBuffer();
    for (String s : obj) {
      sb.append(", " ).append(s);
    }

    System.out.println(sb.delete(0, 1));
  }
  
}
