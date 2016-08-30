package com.ly.web.utils;

import org.apache.log4j.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.springframework.util.StringUtils;

import com.ly.web.command.CommentsInfo;


/**
 * Created by yongliu on 7/19/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/19/2016 15:19
 */
public class YHDUtils {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  // http://item.yhd.com/item/63789609?tp=222.43222_0.212.2_105.54.LMsd^9b-10-EnPos&ti=RGU7NA
  private static final String P_YHD_URL_PREFIX          = "http://item.yhd.com/item/";
  private static final String P_JD_URL_PREFIX           = "http://item.jd.com/";
  private static final String P_JD_URL_HTTPS_PREFIX     = "https://item.jd.com/";
  private static final int    ORDER_ID_COLUMN        = 0;
  private static final int    USER_NAME_COLUMN       = 1;
  private static final int    PASSWORD_COLUMN        = 2;
  private static final int    SKU_COLUMN             = 3;
  private static final int    COMMENT_CONTENT_COLUMN = 4;
  private static final int    ONLY_RECEIPT_NOT_COMMENT_COLUMN = 5;

  private static final Logger logger = Logger.getLogger(YHDUtils.class);

  //~ Methods ----------------------------------------------------------------------------------------------------------
  
  public static String getStringCell(Cell cell){
    if(cell != null){
      cell.setCellType(cell.CELL_TYPE_STRING);
      return getCellValue(cell);
    }
    return null;
  }

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
        System.out.println("order number:" + cellValue);
        commentsInfo.setOrderId(cellValue);

        break;
      }

      case USER_NAME_COLUMN: {
        // column index is 1, it's username
        System.out.println("username:" + cellValue);
        if (cellValue != null && StringUtils.hasText(cellValue)) {
          commentsInfo.setUsername(cellValue);
        }

        break;
      }

      case PASSWORD_COLUMN: {
        // column index is 2, it's password
        System.out.println("password:" + cellValue);
        if (cellValue != null && StringUtils.hasText(cellValue)) {
          commentsInfo.setPassword(cellValue);
        }

        break;
      }

      case SKU_COLUMN: {
        // column index is 3, it's sku
        System.out.println("sku:" + cellValue);
        String sku = YHDUtils.getSku(cellValue);

        if (sku != null) {
          commentsInfo.getCommentsMap().put(sku, null);
        }

        break;
      }

      case COMMENT_CONTENT_COLUMN: {
        // column index is 4, it's comment content
        System.out.println("comment:" + cellValue);
        String sku = YHDUtils.getSku(getCellValue(row.getCell(SKU_COLUMN)));

        if (sku != null) {
          commentsInfo.getCommentsMap().put(sku, cellValue);
        }

        break;
      }
      
      case ONLY_RECEIPT_NOT_COMMENT_COLUMN: {
        System.out.println("doNotComment:" + cellValue);
        // column index is 5, it's only receipt not comment column
        // if 'Y', 'Yes', 'true', '是'
        // then commentsInfo.doNotComment is "true"
        commentsInfo.setDoNotComment(getDoNotCommentCellValue(cellValue));
      }
    } // end switch
  } // end method assembleCommentInfoByCell

  //~ ------------------------------------------------------------------------------------------------------------------
  
  private static Boolean getDoNotCommentCellValue(String cellValue) {
    if ((cellValue != null) && !StringUtils.isEmpty(cellValue)
          && ("Y".equalsIgnoreCase(cellValue) || "YES".equalsIgnoreCase(cellValue) || "true".equals(cellValue)
            || "是".equals(cellValue))) {
      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }

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
      boolean isUrl = Boolean.FALSE;
      String prefix = null;
      if(skuOrUrl.contains(P_YHD_URL_PREFIX)){
        isUrl = Boolean.TRUE;
        prefix = P_YHD_URL_PREFIX;
      } else if(skuOrUrl.contains(P_JD_URL_PREFIX)){
        isUrl = Boolean.TRUE;
        prefix = P_JD_URL_PREFIX;
      } else if(skuOrUrl.contains(P_JD_URL_HTTPS_PREFIX)){
        isUrl = Boolean.TRUE;
        prefix = P_JD_URL_HTTPS_PREFIX;
      }
      
      if(isUrl && prefix != null){
        if (logger.isDebugEnabled()) {
          logger.debug(skuOrUrl + " is a url, will get sku NO. from this url.");
        }

        String urlWithoutParams = skuOrUrl.split("\\?")[0];

        if (logger.isDebugEnabled()) {
          logger.debug("Analysis the url without parameters is: " + urlWithoutParams);
        }

        sku = urlWithoutParams.replace(prefix, "").replace(".html", "").trim();

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
