package com.ly.web.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yongliu on 7/7/16.
 */
public class HtmlUtils {
  
  public static String getHtml(InputStream in){
    BufferedReader br = null;
    try{
      br = new BufferedReader(new InputStreamReader(in, "utf-8"));

      StringBuilder sb = new StringBuilder();
      String        s  = "";

      while ((s = br.readLine()) != null) {
        sb.append(s.trim()).append("\n");
      }
    }catch (Exception e){
      e.printStackTrace();
    }
    
    return br != null ? br.toString() : null;
  }
}
