package com.ly.utils;

import org.apache.poi.ss.usermodel.Cell;

import org.springframework.util.StringUtils;


/**
 * Created by yongliu on 11/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/09/2016 15:05
 */
public class ExcelUtils {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for boolean value.
   *
   * @param   cellValue  String
   *
   * @return  Boolean
   */
  public static Boolean getBooleanValue(String cellValue) {
    if ((cellValue != null) && !StringUtils.isEmpty(cellValue)
          && ("Y".equalsIgnoreCase(cellValue) || "YES".equalsIgnoreCase(cellValue) || "true".equals(cellValue)
            || "是".equals(cellValue))) {
      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 获取单元格的值.
   *
   * @param   cell  $param.type$
   *
   * @return  获取单元格的值.
   */
  public static String getCellValue(Cell cell) {
    if (cell == null) {
      return "";
    }

    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
      return cell.getStringCellValue();

    } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
      return String.valueOf(cell.getBooleanCellValue());

    } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
      return cell.getCellFormula();

    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
      return String.valueOf(cell.getNumericCellValue());

    }

    return "";
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for integer value.
   *
   * @param   cellValue  String
   *
   * @return  Integer
   */
  public static Integer getIntegerValue(String cellValue) {
    try {
      if (cellValue != null) {
        return Integer.parseInt(cellValue);
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for string cell.
   *
   * @param   cell  Cell
   *
   * @return  String
   */
  public static String getStringCell(Cell cell) {
    if (cell != null) {
      cell.setCellType(cell.CELL_TYPE_STRING);

      return getCellValue(cell);
    }

    return null;
  }
} // end class ExcelUtils
