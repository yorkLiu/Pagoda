package com.ly.sort.yhd;

import com.ly.sort.constant.Constant;
import com.ly.sort.util.Utils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yongliu on 8/1/16.
 */
public class YHDSearch {
  protected final Log logger = LogFactory.getLog(getClass());

  private int currentPage = 0;
  private String currentKeyWord = null;

  // 每加载一次,加载多少个商品出来,JD一次加载30条
  private int pageOffsetSize = 30;

  //how many columns for every line(每一行显示多少个商品)
  private int numberOfLine = 5;

  // 最多找200页,200页之类没有找到,则说明该商品还没有排名
  private int maxFoundPage = 200;
  List<String> results = new LinkedList<String>();
  private DefaultHttpClient httpclient = null;
  
 public YHDSearch(){
   httpclient = new DefaultHttpClient();

   logger.info("Page Offset Size: " + pageOffsetSize);
   logger.info("Number of Every Line: " + numberOfLine);
   logger.info("Max Search Pages: " + maxFoundPage);
 }


  public List<String> yhdSort(String keyword, String sku, String productionName, String searchType) {
    this.currentKeyWord = keyword;

    logger.info("Search Type: " + searchType);
    logger.info("Key Word: " + keyword);

    if ("SKU".equalsIgnoreCase(searchType)) {
      logger.info("SKU:" + sku);

      return searchBySKU(sku);
    }
    else if ("PRODUCTIONNAME".equalsIgnoreCase(searchType)) {
    }
    else if ("ALL".equals(searchType)) {

      return searchByKeyWord(Utils.convertUTF8(productionName));
    }

    return null;
  }

  public List<String> searchBySKU(String sku) {
    // TODO search by sku
    String searchUrl = getPageContent();
    logger.info("URL:" + searchUrl);
    
    return null;
  }


  public List<String> searchByKeyWord(String productionName) {
    
    return null;
  }
  
  
  public String getPageContent(){
    this.currentPage++;

    if (this.currentPage > maxFoundPage) {

      return null;
    }
    
    //一个页面会加载两页的内容
    //第一部分
    String partOneUrl = String.format(Constant.BASE_SEARCH_URL_EVEN_NUM_PAGING_FIRST, currentPage, currentKeyWord);
    //第二部分
    String partTwoUrl = String.format(Constant.BASE_SEARCH_URL_EVEN_NUM_PAGING_SECOND, currentPage, currentKeyWord);
    
    logger.info("Part one url: " + partOneUrl);
    logger.info("Part two url: " + partTwoUrl);
    
    String partOneContent = getContentFromURL(partOneUrl);
    String partTwoContent = getContentFromURL(partTwoUrl);
    
    String pageContent = null;
    
    if(partOneContent != null){
      if(partOneContent.startsWith("{\"value\":\"")){
        partOneContent = partOneContent.replace("{\"value\":\"", "");
      }
      int idx = partOneContent.lastIndexOf("\"}");
      logger.info("idx:" + idx);
      if(idx > 0){
        partOneContent = partOneContent.substring(0, partOneContent.trim().length()-2);
      }
      pageContent = partOneContent;
    }
    if(partTwoContent != null){
      pageContent += partTwoContent;
    }

    
    
//    logger.info(Utils.convertUTF8(pageContent));

    partOneContent = StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeHtml(partOneContent));
    partTwoContent = StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeHtml(partTwoContent));
    pageContent = StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeHtml(pageContent));
    partOneContent = partOneContent.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\\\"");
    System.out.println(pageContent);
    
    Document document = Jsoup.parse(partOneContent);
//    System.out.println(document.html());
    Elements item = document.select("div[class=mod_search_pro]");
    System.out.println(item.size());
    
    return null;
    
  }


  public String getContentFromURL(String url) {
    try {
      HttpGet httpGet = new HttpGet(url);
      httpGet.setHeader("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
//      httpGet.setHeader("Accept-Encoding", "gzip, deflate");
      httpGet.setHeader("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4");
      httpGet.setHeader("Host", "search.yhd.com");
      httpGet.setHeader("Content-Type", "text/html; charset=UTF-8");
      httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");
      ResponseHandler<String> responseHandler = new BasicResponseHandler();
//      String responseBody = httpclient.execute(httpGet, responseHandler);
      String responseBody = null;
      HttpResponse response = httpclient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        String charset = Utils.getContentCharSet(entity);
//        // 使用EntityUtils的toString方法，传递编码，默认编码是ISO-8859-1   
        responseBody = EntityUtils.toString(entity, charset);
      }

      httpGet.releaseConnection();
      
      return responseBody;
    }catch (Exception e){
      logger.error(e.getMessage(), e);
    }

    return null;
  }


  public static void main(String[] args) {
    YHDSearch search = new YHDSearch();
    search.yhdSort("红酒", null, null, "SKU");
  }
}
