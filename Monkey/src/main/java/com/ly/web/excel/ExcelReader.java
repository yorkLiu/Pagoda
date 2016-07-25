package com.ly.web.excel;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import org.springframework.util.StringUtils;

import com.ly.web.command.CommentsInfo;
import com.ly.web.utils.YHDUtils;

import com.microsoft.schemas.office.visio.x2012.main.CellType;


/**
 * Created by yongliu on 7/18/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/18/2016 19:59
 */
public class ExcelReader implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 1308826032561720907L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Logger logger = Logger.getLogger(getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

 

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 读取excel数据.
   *
   * @param   path  $param.type$
   *
   * @return  读取excel数据.
   */
  public List<CommentsInfo> readExcelToObj(String path) {
    Workbook wb = null;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("<<<<<<<<<<Start process excel from path: " + path);
      }

      wb = WorkbookFactory.create(new File(path));

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<<<<<<<End processed excel");
      }

      return readExcel(wb, 0, 1, 0);
    } catch (InvalidFormatException e) {
      logger.error(e.getMessage(), e);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private Cell getMergeCell(Sheet sheet, int row, int column) {
    int sheetMergeCount = sheet.getNumMergedRegions();

    for (int i = 0; i < sheetMergeCount; i++) {
      CellRangeAddress ca          = sheet.getMergedRegion(i);
      int              firstColumn = ca.getFirstColumn();
      int              lastColumn  = ca.getLastColumn();
      int              firstRow    = ca.getFirstRow();
      int              lastRow     = ca.getLastRow();

      if ((row >= firstRow) && (row <= lastRow)) {
        if ((column >= firstColumn) && (column <= lastColumn)) {
          Row  fRow  = sheet.getRow(firstRow);
          Cell fCell = fRow.getCell(firstColumn);
          fCell.setCellType(fCell.CELL_TYPE_STRING);

          return fCell;
        }
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 获取合并单元格的值.
   *
   * @param   sheet   $param.type$
   * @param   row     $param.type$
   * @param   column  $param.type$
   *
   * @return  获取合并单元格的值.
   */
  private String getMergedRegionValue(Sheet sheet, int row, int column) {
    int sheetMergeCount = sheet.getNumMergedRegions();

    for (int i = 0; i < sheetMergeCount; i++) {
      CellRangeAddress ca          = sheet.getMergedRegion(i);
      int              firstColumn = ca.getFirstColumn();
      int              lastColumn  = ca.getLastColumn();
      int              firstRow    = ca.getFirstRow();
      int              lastRow     = ca.getLastRow();

      if ((row >= firstRow) && (row <= lastRow)) {
        if ((column >= firstColumn) && (column <= lastColumn)) {
          Row  fRow  = sheet.getRow(firstRow);
          Cell fCell = fRow.getCell(firstColumn);

          fCell.setCellType(fCell.CELL_TYPE_STRING);

          return YHDUtils.getCellValue(fCell);
        }
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 判断sheet页中是否含有合并单元格.
   *
   * @param   sheet  $param.type$
   *
   * @return  判断sheet页中是否含有合并单元格.
   */
  private boolean hasMerged(Sheet sheet) {
    return (sheet.getNumMergedRegions() > 0) ? true : false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 判断指定的单元格是否是合并单元格.
   *
   * @param   sheet   $param.type$
   * @param   row     行下标
   * @param   column  列下标
   *
   * @return  判断指定的单元格是否是合并单元格.
   */
  private boolean isMergedRegion(Sheet sheet, int row, int column) {
    int sheetMergeCount = sheet.getNumMergedRegions();

    for (int i = 0; i < sheetMergeCount; i++) {
      CellRangeAddress range       = sheet.getMergedRegion(i);
      int              firstColumn = range.getFirstColumn();
      int              lastColumn  = range.getLastColumn();
      int              firstRow    = range.getFirstRow();
      int              lastRow     = range.getLastRow();

      if ((row >= firstRow) && (row <= lastRow)) {
        if ((column >= firstColumn) && (column <= lastColumn)) {
          return true;
        }
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 判断合并了行.
   *
   * @param   sheet   $param.type$
   * @param   row     $param.type$
   * @param   column  $param.type$
   *
   * @return  判断合并了行.
   */
  private boolean isMergedRow(Sheet sheet, int row, int column) {
    int sheetMergeCount = sheet.getNumMergedRegions();

    for (int i = 0; i < sheetMergeCount; i++) {
      CellRangeAddress range       = sheet.getMergedRegion(i);
      int              firstColumn = range.getFirstColumn();
      int              lastColumn  = range.getLastColumn();
      int              firstRow    = range.getFirstRow();
      int              lastRow     = range.getLastRow();

      if ((row == firstRow) && (row == lastRow)) {
        if ((column >= firstColumn) && (column <= lastColumn)) {
          return true;
        }
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 合并单元格.
   *
   * @param  sheet     $param.type$
   * @param  firstRow  开始行
   * @param  lastRow   结束行
   * @param  firstCol  开始列
   * @param  lastCol   结束列
   */
  private void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
    sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 读取excel文件.
   *
   * @param   wb             $param.type$
   * @param   sheetIndex     sheet页下标：从0开始
   * @param   startReadLine  开始读取的行:从0开始
   * @param   tailLine       去除最后读取的行
   *
   * @return  读取excel文件.
   */
  private List<CommentsInfo> readExcel(Workbook wb, int sheetIndex, int startReadLine, int tailLine) {
    List<CommentsInfo> commentsInfoList = new LinkedList<>();
    Sheet              sheet            = wb.getSheetAt(sheetIndex);
    Row                row              = null;

    Map<String, CommentsInfo> map = new LinkedHashMap<>();

    if (logger.isDebugEnabled()) {
      logger.debug("This excel have total rows: " + (sheet.getLastRowNum() + 1));
    }

    for (int i = startReadLine; i < (sheet.getLastRowNum() - tailLine + 1); i++) {
      row = sheet.getRow(i);

      String       orderNum     = null;
      CommentsInfo commentsInfo = null;

      if (logger.isDebugEnabled()) {
        logger.debug("Read excel row index: " + row.getRowNum());
      }

      Cell orderNumCell = null;

      // read i line 0 column orderNum
      boolean isMergeForFirstCol = isMergedRegion(sheet, i, 0);

      if (isMergeForFirstCol) {
        orderNumCell = getMergeCell(sheet, row.getRowNum(), 0);
      } else {
        orderNumCell = row.getCell(0);
      }

      if (orderNumCell != null) {
        orderNum = YHDUtils.getStringCell(orderNumCell);

        if (logger.isDebugEnabled()) {
          logger.debug("orderNum: " + orderNum);
        }

        if ((orderNum != null) && StringUtils.hasText(orderNum)) {
          if(map.get(orderNum) == null){
            commentsInfo = new CommentsInfo();
            map.put(orderNum, commentsInfo);
          } else {
            commentsInfo = map.get(orderNum);
          }
          
        }
      }

      if ((orderNum == null) || !StringUtils.hasText(orderNum.trim())) {
        if (logger.isDebugEnabled()) {
          logger.debug("No order number, skip this row.");
        }

        continue;
      }


      for (Cell cell : row) {
        boolean isMerge = isMergedRegion(sheet, i, cell.getColumnIndex());

        if (logger.isDebugEnabled()) {
          logger.debug("(Row, Cell), [" + row.getRowNum() + ", " + cell.getColumnIndex() + "], isMerge: " + isMerge);
        }

        // 判断是否具有合并单元格
        if (isMerge) {
          cell = getMergeCell(sheet, row.getRowNum(), cell.getColumnIndex());
        }

        YHDUtils.assembleCommentInfoByCell(commentsInfo, row, cell);
      }
    } // end for


    for (CommentsInfo commentsInfo : map.values()) {
      commentsInfoList.add(commentsInfo);
    }
    
    // return list
    return commentsInfoList;
  } // end method readExcel


  /**
   * main.
   *
   * @param  args  String[]
   */
  public static void main(String[] args) {
    ExcelReader        excelReader      = new ExcelReader();
    String             path             = "/Users/yongliu/Downloads/7.21TianXun.xlsx";
    List<CommentsInfo> commentsInfoList = excelReader.readExcelToObj(path);
    System.out.println(commentsInfoList);


  }

} // end class ExcelReader
