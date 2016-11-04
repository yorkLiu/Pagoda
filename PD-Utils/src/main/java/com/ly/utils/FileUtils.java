package com.ly.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;

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
   * @param   array     Object[]
   *
   * @return  boolean
   */
  public static boolean writeContentToFile(String filePath, Object[] array, String delimiter) {
    delimiter = delimiter != null ? delimiter : DEFAULT_DELIMITER;
    String  content = StringUtils.arrayToDelimitedString(array, delimiter);
    Boolean success = writeContentToFile(filePath, content);

    return success;
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
} // end class FileUtils
