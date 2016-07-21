package com.ly.web.utils;

import org.apache.log4j.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.springframework.util.StringUtils;

import com.ly.web.command.CommentsInfo;


/**
 * Created by yongliu on 7/19/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/19/2016 15:19
 */
public class YHDUtils {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  // http://item.yhd.com/item/63789609?tp=222.43222_0.212.2_105.54.LMsd^9b-10-EnPos&ti=RGU7NA
  private static final String P_URL_PREFIX           = "http://item.yhd.com/item/";
  private static final int    ORDER_ID_COLUMN        = 0;
  private static final int    USER_NAME_COLUMN       = 1;
  private static final int    PASSWORD_COLUMN        = 2;
  private static final int    SKU_COLUMN             = 3;
  private static final int    COMMENT_CONTENT_COLUMN = 4;

  private static final Logger logger = Logger.getLogger(YHDUtils.class);

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * assembleCommentInfoByCell.
   *
   * @param  commentsInfo  CommentsInfo
   * @param  row           Row
   * @param  cell          Cell
   */
  public static void assembleCommentInfoByCell(CommentsInfo commentsInfo, Row row, Cell cell) {
    // set all cell type is 'String'
    cell.setCellType(cell.CELL_TYPE_STRING);

    int colIdx = cell.getColumnIndex();

    String cellValue = getCellValue(cell);

    if (logger.isDebugEnabled()) {
      logger.debug("Read the cellIndex: " + colIdx + " in row: " + row.getRowNum() + " and value: " + cellValue);
    }

    switch (colIdx) {
      case ORDER_ID_COLUMN: {
        // column index is 0, it's order id
        commentsInfo.setOrderId(cellValue);

        break;
      }

      case USER_NAME_COLUMN: {
        // column index is 1, it's username
        if (cellValue != null && StringUtils.hasText(cellValue)) {
          commentsInfo.setUsername(cellValue);
        }

        break;
      }

      case PASSWORD_COLUMN: {
        // column index is 2, it's password
        if (cellValue != null && StringUtils.hasText(cellValue)) {
          commentsInfo.setPassword(cellValue);
        }

        break;
      }

      case SKU_COLUMN: {
        // column index is 3, it's sku
        String sku = YHDUtils.getSku(cellValue);

        if (sku != null) {
          commentsInfo.getCommentsMap().put(sku, null);
        }

        break;
      }

      case COMMENT_CONTENT_COLUMN: {
        // column index is 4, it's comment content
        String sku = YHDUtils.getSku(getCellValue(row.getCell(SKU_COLUMN)));

        if (sku != null) {
          commentsInfo.getCommentsMap().put(sku, cellValue);
        }

        break;
      }
    } // end switch
  } // end method assembleCommentInfoByCell

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
   * getter method for sku.
   *
   * @param   skuOrUrl  String
   *
   * @return  String
   */
  public static String getSku(String skuOrUrl) {
    String sku = null;

    if (logger.isDebugEnabled()) {
      logger.debug("Ready analysis SKU form: " + skuOrUrl);
    }

    if ((skuOrUrl != null) && StringUtils.hasText(skuOrUrl)) {
      if (skuOrUrl.contains(P_URL_PREFIX)) {
        if (logger.isDebugEnabled()) {
          logger.debug(skuOrUrl + " is a url, will get sku NO. from this url.");
        }

        String urlWithoutParams = skuOrUrl.split("\\?")[0];

        if (logger.isDebugEnabled()) {
          logger.debug("Analysis the url without parameters is: " + urlWithoutParams);
        }

        sku = urlWithoutParams.replace(P_URL_PREFIX, "");

        if (logger.isDebugEnabled()) {
          logger.debug("Got the SKU: " + sku + " from the url: " + skuOrUrl);
        }
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug(skuOrUrl + " is not a url, it is a actual sku.");
        }

        sku = skuOrUrl;

        if (logger.isDebugEnabled()) {
          logger.debug("So the sku is: " + sku);
        }
      } // end if-else
    }   // end if

    if (logger.isDebugEnabled()) {
      logger.debug("Return sku[" + sku + "]");
    }

    return sku;
  } // end method getSku


} // end class YHDUtils