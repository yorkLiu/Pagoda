package com.ly.file;

import com.ly.utils.DateUtil;
import com.ly.utils.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by yongliu on 11/4/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/04/2016 14:04
 */
public class FileWriter implements InitializingBean {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  public static final String TXT_FILE = ".txt";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  protected String delimiter;

  /** TODO: DOCUMENT ME! */
  protected Logger logger = Logger.getLogger(getClass());

  /** TODO: DOCUMENT ME! */
  protected String workDir;

  protected static final String DEFAULT_LINE_SEPARATOR = System.getProperty("line.separator");

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    logger.info("The file dir is: " + workDir);
    logger.info("The delimiter is: " + delimiter);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for delimiter.
   *
   * @return  String
   */
  public String getDelimiter() {
    return delimiter;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for today commented orders from file.
   *
   * @param   fileNamePrefix  String
   *
   * @return  Set
   */
  public List<String> getTodayCommentedOrdersFromFile(String fileNamePrefix) {
    List<String> orderNoList = null;

    try {
      String content = readFile(fileNamePrefix);

      if ((content != null) && StringUtils.hasText(content)) {
        String[] orderNos = StringUtils.delimitedListToStringArray(content, delimiter);
        orderNoList = CollectionUtils.arrayToList(orderNos);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    return orderNoList;
  }

  public List<String> getTodayCommentedOrdersFromFiles(String... fileNamePrefix) {
    List<String> orderNoList = new ArrayList<>();
    for (String fpx : fileNamePrefix) {
      List<String> ordersFormFile = getTodayCommentedOrdersFromFile(fpx);
      if(ordersFormFile != null && !ordersFormFile.isEmpty()){
        orderNoList.addAll(ordersFormFile);
      }
    }
    
    return orderNoList;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for work dir.
   *
   * @return  String
   */
  public String getWorkDir() {
    return workDir;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * readFile.
   *
   * @param   fileNamePrefix  String
   *
   * @return  String
   *
   * @throws  IOException  exception
   */
  public String readFile(String fileNamePrefix) throws IOException {
    String filePath = getFilePath(fileNamePrefix);

    return FileUtils.readFile(filePath);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for delimiter.
   *
   * @param  delimiter  String
   */
  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for work dir.
   *
   * @param  workDir  String
   */
  public void setWorkDir(String workDir) {
    if ((workDir != null) && StringUtils.hasText(workDir)) {
      this.workDir = StringUtils.cleanPath(workDir);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * writeToFile.
   *
   * @param  fileNamePrefix  String
   * @param  content         String
   */
  public void writeToFile(String fileNamePrefix, String content) {
//    String filePath = getFilePath(fileNamePrefix);
    String filePath = getFilePathIncludeFileName(fileNamePrefix);

    FileUtils.writeContentToFile(filePath, new Object[] { content, "" }, delimiter);
  }
  
  public void writeToFileln(String fileNamePrefix, String content) {
    writeToFile(fileNamePrefix, content, System.lineSeparator());
  }

  public void writeToFile(String fileNamePrefix, String content, String paramDelimiter) {
//    String filePath = getFilePath(fileNamePrefix);
    String filePath = getFilePathIncludeFileName(fileNamePrefix);
    paramDelimiter = paramDelimiter != null && StringUtils.hasLength(paramDelimiter)? paramDelimiter : delimiter;

    FileUtils.writeContentToFile(filePath, new Object[] { content, "" }, paramDelimiter);
  }
  
  public void writeToFileDirectly(String fileName, String content){
    FileUtils.writeContentToFile(fileName, content);
  }
  

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * generateFileName.
   *
   * @param   prefix  String
   *
   * @return  String
   */
  protected String generateFileName(String prefix) {
    StringBuilder fileName  = new StringBuilder(prefix).append("-");
    String        dateStamp = DateUtil.formatDateOnly(new Date());
    fileName.append(dateStamp).append(TXT_FILE);

    logger.info("Generated File name: " + fileName);
    return fileName.toString();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for file path.
   *
   * @param   filePrefix  String
   *
   * @return  String
   */
  protected String getFilePath(String filePrefix) {
    File dir = new File(workDir);

    if (!dir.exists()) {
      dir.mkdirs();
    }

    if (!workDir.endsWith(File.separator)) {
      workDir = workDir + File.separator;
    }

    String filePath = workDir + generateFileName(filePrefix.toString());

    logger.info("File Path:" + filePath);
    return filePath;
  }
  
  protected String getFilePathIncludeFileName(String filePrefix){
    String tmpWorkDir = workDir;
    filePrefix = StringUtils.cleanPath(filePrefix);
    String fileNameOnly = StringUtils.getFilename(filePrefix);
    String foldersIncludeFileName = filePrefix.replace(fileNameOnly, "");
    if(StringUtils.hasLength(foldersIncludeFileName)){
      tmpWorkDir = StringUtils.applyRelativePath(workDir, foldersIncludeFileName);
    }

    File dir = new File(tmpWorkDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    
    if (!tmpWorkDir.endsWith(File.separator)) {
      tmpWorkDir = tmpWorkDir + File.separator;
    }
    String filePath = tmpWorkDir + generateFileName(fileNameOnly.toString());

    logger.info("File Path:" + filePath);
    return filePath;
  }
  
  
  public File getFileByName(String fileName){
    String tmpWorkDir = workDir;
    fileName = StringUtils.cleanPath(fileName);
    String fileNameOnly = StringUtils.getFilename(fileName);
    String foldersIncludeFileName = fileName.replace(fileNameOnly, "");
    if(StringUtils.hasLength(foldersIncludeFileName)){
      tmpWorkDir = StringUtils.applyRelativePath(workDir, foldersIncludeFileName);
    }

    File dir = new File(tmpWorkDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    if (!tmpWorkDir.endsWith(File.separator)) {
      tmpWorkDir = tmpWorkDir + File.separator;
    }
    String filePath = tmpWorkDir + fileNameOnly;

    logger.info("File Path:" + filePath);
    return new File(filePath);
  }
  

} // end class FileWriter
