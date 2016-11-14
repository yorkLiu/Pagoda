package com.ly.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import java.util.HashSet;
import java.util.LinkedHashSet;
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

  private static final String MULTIPLE_SEPARATOR = ",";
  
  private static final String DEFAULT_LINE_SEPARATOR = System.getProperty("line.separator");

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for excel file from path.
   *
   * @param   path  String
   *
   * @see     FileUtils#getFilesFromPath(java.lang.String)
   *
   * @return  only list the excel files in @path
   */
  public static Set<String> getExcelFileFromPath(String path) {
    Set<String> excelFiles = new LinkedHashSet<>();
    Set<String> files      = getFilesFromPath(path);

    if (files.size() > 0) {
      for (String file : files) {
        String extension = StringUtils.getFilenameExtension(file);

        if (StringUtils.hasText(extension)
              && (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls"))) {
          excelFiles.add(file);
        }
      }
    }

    return excelFiles;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for file name only.
   *
   * <p>e.g. /myFolder/test.txt -> test</p>
   *
   * @param   filePath  String (may be {@code null})
   *
   * @return  the path with stripped filename extension
   */
  public static String getFileNameWithoutExtension(String filePath) {
    String fileName = null;

    if ((filePath != null) && StringUtils.hasText(filePath)) {
      fileName = StringUtils.stripFilenameExtension(StringUtils.getFilename(StringUtils.cleanPath(filePath)));
    }

    return fileName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for files from path.
   *
   * <pre>
     i.e:
       1. path = /folder1/test.txt, /folder1/test2.txt, folder2/test3.txt  (multiple files)
             --> {/folder1/test.txt, /folder1/test2.txt, folder2/test3.txt}
       2. path = /folder1  (folder)
             --> {/folder1/test.txt, folder1/test2.txt, ...}  (all files in folder1)

       3. path = /folder1, /folder2, ... (multiple folders)
            --> {/folder1/test.txt, /folder1/test2.txt, folder2/test3.txt} (all files in folder1, folder2 ...)
   </pre>
   *
   * @param   path  String (may be {@code null}
   *
   * @return  all files
   */
  public static Set<String> getFilesFromPath(String path) {
    if (path == null) {
      return null;
    }

    Set<String> files = new LinkedHashSet<>();
    String[]    array = StringUtils.delimitedListToStringArray(path, MULTIPLE_SEPARATOR);

    for (String f : array) {
      String fileName = f.trim();
      File   file     = new File(fileName);

      if (file.isFile() && !file.isHidden()) {
        files.add(f);
      } else if (file.isDirectory()) {
        File[] filesInFolder = file.listFiles();

        if ((filesInFolder != null) && (filesInFolder.length > 0)) {
          for (File fl : filesInFolder) {
            if (fl.isFile() && !fl.isHidden()) {
              files.add(fl.getAbsolutePath());
            }
          }
        }
      }
    }

    return files;
  } // end method getFilesFromPath

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
    return writeContentToFile(filePath, content, null);
  } // end method writeContentToFile

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * writeContentToFile.
   *
   * @param   filePath  String
   * @param   content   String
   * @param   header    String
   *
   * @return  boolean
   */
  public static boolean writeContentToFile(String filePath, String content, String header) {
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

        // write file header
        if ((header != null) && StringUtils.hasText(header)) {
          // append new line separator
          header = header + DEFAULT_LINE_SEPARATOR;
          doWrite(file, header);
        }
      }

      doWrite(file, content);

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

  public static boolean writeContentToFile(String filePath, Object[] array, String header, String delimiter) {
    delimiter = (delimiter != null) ? delimiter : DEFAULT_DELIMITER;

    String  content = StringUtils.arrayToDelimitedString(array, delimiter);
    Boolean success = writeContentToFile(filePath, content, header);

    return success;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private static void doWrite(File file, String content) throws IOException {
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
  }

} // end class FileUtils
