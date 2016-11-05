package com.ly.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import org.apache.log4j.Logger;

import org.springframework.util.StringUtils;


/**
 * Created by yongliu on 11/1/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/01/2016 11:47
 */
public class FileUtils implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 6376376893137529891L;

  private static Logger logger = Logger.getLogger(FileUtils.class);

  private static final Boolean APPEND_CONTENT_FLAG = Boolean.TRUE;

  private static final String DEFAULT_DELIMITER = "\t\t";

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * main.
   *
   * @param  args  String[]
   */
  public static void main(String[] args) {
    String content  = "abcd\t\t234234324\t\t\n";
    String content2 = "100001\t\tBBS\t\t\n";
    String content3 = "100002\t\tDES\t\t\n";
    FileUtils.writeContentToFile("/Users/yongliu/Downloads/test.txt", content);
    FileUtils.writeContentToFile("/Users/yongliu/Downloads/test.txt", content2);
    FileUtils.writeContentToFile("/Users/yongliu/Downloads/test.txt", content3);
    FileUtils.writeContentToFile("/Users/yongliu/Downloads/test.txt", new Object[] {
        "A",
        "B",
        200,
        "C\n"
      }, DEFAULT_DELIMITER);


  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * writeContentToFile.
   *
   * @param   filePath  String
   * @param   content   String
   *
   * @return  boolean
   */
  public static boolean writeContentToFile(String filePath, String content) {
    Boolean success = Boolean.TRUE;

    if ((filePath == null) || !StringUtils.hasText(filePath)) {
      logger.error("file path is null");

      return Boolean.FALSE;
    }

    try {
      filePath = StringUtils.cleanPath(filePath);

      if (logger.isDebugEnabled()) {
        logger.debug("Append content:" + content + " to file:" + filePath);
      }

      File file = new File(filePath);

      if (!file.exists()) {
        if (logger.isDebugEnabled()) {
          logger.debug("The file: " + filePath + " was not exists, create it.");
        }

        file.createNewFile();
      }

      synchronized (file) {
        FileWriter fileWriter = new FileWriter(file, APPEND_CONTENT_FLAG);

        if (file.canWrite()) {
          fileWriter.write(content);
          fileWriter.flush();
          fileWriter.close();

          if (logger.isDebugEnabled()) {
            logger.debug("Write content to file was successfully.");
          }
        } else {
          logger.warn("The file now can not writable, fail write content: [" + content + "] to file.");
        }
      }
    } catch (Exception e) {
      success = Boolean.FALSE;

      logger.error(e.getMessage(), e);
    } // end try-catch

    return success;
  } // end method writeContentToFile

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * writeContentToFile.
   *
   * @param   filePath   String
   * @param   array      Object[]
   * @param   delimiter  String
   *
   * @return  boolean
   */
  public static boolean writeContentToFile(String filePath, Object[] array, String delimiter) {
    delimiter = (delimiter != null) ? delimiter : DEFAULT_DELIMITER;

    String  content = StringUtils.arrayToDelimitedString(array, delimiter);
    Boolean success = writeContentToFile(filePath, content);

    return success;
  }

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * readFile.
   *
   * @param   filePath  String
   *
   * @return  String
   *
   * @throws  IOException  exception
   */
  public static String readFile(String filePath) throws IOException {
    BufferedReader reader   = null;
    StringBuffer   fileData = new StringBuffer();
    reader = new BufferedReader(new FileReader(filePath));

    char[] buf     = new char[4096];
    int    numRead = 0;

    while ((numRead = reader.read(buf)) != -1) {
      String readData = String.valueOf(buf, 0, numRead);
      fileData.append(readData);
    }

    reader.close();


    return fileData.toString();
  } // end method readFile
  
} // end class FileUtils
