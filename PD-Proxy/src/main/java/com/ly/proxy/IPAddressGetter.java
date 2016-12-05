package com.ly.proxy;

/**
 * Created by yongliu on 12/2/16.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Description Find the IP associated address
 *  i.e:
 *     IP = 113.96.121.110   ==> 广东省广州市  电信
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  12/02/2016 15:02
 */
public class IPAddressGetter {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for ip address info.
   *
   * @param   ip  String
   *
   * @return  String
   *
   * @throws  IOException  exception
   */
  public static String getIpAddressInfo(String ip) throws IOException {
    String            info         = getInfo(ip);
    ArrayList<String> addressArray = IPAddressGetter.getAddresses(info);

    return ((addressArray != null) && (addressArray.size() > 0)) ? addressArray.get(0) : null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * main.
   *
   * @param   args  String[]
   *
   * @throws  IOException  exception
   */
  public static void main(String[] args) throws IOException {
    IPAddressGetter ipgetter = new IPAddressGetter();

    String info = new IPAddressGetter().getInfo("113.96.121.110");
// System.out.println("不规则结果:" + info);


    ArrayList<String> addressarray = IPAddressGetter.getAddresses(info);
    System.out.println("规范化结果：" + addressarray.toString());
    System.out.println("规范化结果：" + getIpAddressInfo("113.96.121.110"));

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 抓取某个网页的源代码.
   *
   * @param   urlstr   要抓取网页的地址
   * @param   charset  网页所使用的编码 如"utf-8","gbk"
   *
   * @return  抓取某个网页的源代码.
   *
   * @throws  IOException
   */
  private static String fetchHtml(String urlstr, String charset) throws IOException {
    URL               url    = new URL(urlstr);
    HttpURLConnection con    = (HttpURLConnection) url.openConnection();
    InputStream       is     = con.getInputStream();
    InputStreamReader isr    = new InputStreamReader(is, charset);
    String            result = "";
    int               read;

    while ((read = isr.read()) != -1) {
      result += (char) read;
    }

    isr.close();

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 从不规则字符串中(getInfo()的结果),抽取出地址信息数组.
   *
   * @param   info  $param.type$
   *
   * @return  从不规则字符串中(getInfo()的结果),抽取出地址信息数组.
   */
  private static ArrayList<String> getAddresses(String info) {
    ArrayList<String> addressarray = new ArrayList<String>();
    String            regex        = "：(.*?)</li>";
    Pattern           p            = Pattern.compile(regex);
    Matcher           matcher      = p.matcher(info);

    while (matcher.find()) {
      addressarray.add(matcher.group(1));
    }

    return addressarray;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 取出html源码中.
   *
   * <ul class=\"ul1\">
   *   <li>和 之间的部分，即为我们要的结果(不规则字符串)</li>
   * </ul>
   *
   * @param   ip  String
   *
   * @return  取出html源码中.
   *
   * @throws  IOException
   */
  private static String getInfo(String ip) throws IOException {
    String info   = fetchHtml("http://www.ip138.com/ips138.asp?ip=" + ip
        + "&action=2", "gb2312");
    int    index1 = info.indexOf("<ul class=\"ul1\">") + 16;
    int    index2 = info.indexOf("</ul></td>");
    info = info.substring(index1, index2);

    return info;
  }

} // end class IPAddressGetter
