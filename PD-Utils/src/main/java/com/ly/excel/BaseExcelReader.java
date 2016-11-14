package com.ly.excel;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.ly.utils.ExcelUtils;


/**
 * Created by yongliu on 11/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/09/2016 15:06
 */
public abstract class BaseExcelReader implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 6422975519075639064L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected Logger logger = Logger.getLogger(getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  protected Cell getMergeCell(Sheet sheet, int row, int column) {
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
  protected String getMergedRegionValue(Sheet sheet, int row, int column) {
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

          return ExcelUtils.getCellValue(fCell);
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
  protected boolean hasMerged(Sheet sheet) {
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
  protected boolean isMergedRegion(Sheet sheet, int row, int column) {
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
  protected boolean isMergedRow(Sheet sheet, int row, int column) {
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
  protected void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
    sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
  }


} // end class BaseExcelReader
