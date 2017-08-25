package com.ly.web.utils;

import org.apache.log4j.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.springframework.util.StringUtils;

import com.ly.utils.ExcelUtils;
import com.ly.utils.URLUtils;

import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.OrderCommand;


/**
 * Created by yongliu on 11/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/09/2016 16:06
 */
public class OrderUtils {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Logger logger = Logger.getLogger(OrderUtils.class);

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * assembleNormalOrderExclusiveProductionInfo.
   *
   * @param  orderInfo  OrderCommand
   * @param  row        Row
   * @param  cell       Cell
   */
  public static void assembleNormalOrderExclusiveProductionInfo(OrderCommand orderInfo, Row row, Cell cell) {
    // set all cell type is 'String'
    cell.setCellType(cell.CELL_TYPE_STRING);

    int colIdx = cell.getColumnIndex();

    String cellValue = ExcelUtils.getCellValue(cell);

    switch (colIdx) {
      case OrderColumns.USER_NAME_COLUMN: {
        orderInfo.setUsername(cellValue);

        break;
      }

      case OrderColumns.PASSWORD_COLUMN: {
        orderInfo.setPassword(cellValue);

        break;
      }

      case OrderColumns.RECEIVER_NAME_COLUMN: {
        orderInfo.getAddressInfo().setFullName(cellValue);

        break;
      }

      case OrderColumns.RECEIVE_ADDRESS_COLUMN: {
        orderInfo.getAddressInfo().setFullAddress(cellValue);

        break;
      }

      case OrderColumns.RECEIVE_MOBILE_PHONE_COLUMN: {
        orderInfo.getAddressInfo().setTelephoneNum(cellValue);

        break;
      }

      case OrderColumns.RECEIVE_IDENTITY_CARD_NUM_COLUMN: {
        orderInfo.getAddressInfo().setIdentityCardNum(cellValue);

        break;
      }

      case OrderColumns.RECEIVE_PROVINCE_COLUMN: {
        orderInfo.getAddressInfo().setProvince(cellValue);

        break;
      }

      case OrderColumns.RECEIVE_CITY_COLUMN: {
        orderInfo.getAddressInfo().setCity(cellValue);

        break;
      }

      case OrderColumns.RECEIVE_COUNTRY_COLUMN: {
        orderInfo.getAddressInfo().setCountry(cellValue);

        break;
      }
      case OrderColumns.RECEIVE_TOWN_COLUMN: {
        orderInfo.getAddressInfo().setTwon(cellValue);

        break;
      }

      case OrderColumns.ALLOW_OVERSEA_COLUMN: {
        orderInfo.setAllowOversea(ExcelUtils.getBooleanValue(cellValue));

        break;
      }
      case OrderColumns.RECEIVE_ORDER_COMMENT_COLUMN: {
        orderInfo.setOrderCommentText(cellValue);

        break;
      }
    } // end switch
  }   // end method assembleNormalOrderExclusiveProductionInfo

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * assembleNormalOrderProductionInfo.
   *
   * @param  orderInfo   OrderCommand
   * @param  row         Row
   * @param  isGroupBuy  boolean
   */
  public static void assembleNormalOrderProductionInfo(OrderCommand orderInfo, Row row, boolean isGroupBuy) {
    String url  = ExcelUtils.getCellValue(row.getCell(OrderColumns.PRODUCTION_URL_COLUMN));
    String name = ExcelUtils.getCellValue(row.getCell(OrderColumns.PRODUCTION_NAME_COLUMN));

    // price
    Cell priceCell = row.getCell(OrderColumns.PRODUCTION_PRICE_COLUMN);
    priceCell.setCellType(priceCell.CELL_TYPE_STRING);

    String price = ExcelUtils.getCellValue(priceCell);

    String keyword = ExcelUtils.getCellValue(row.getCell(OrderColumns.PRODUCTION_KEYWORD_COLUMN));

    // count
    Cell countCell = row.getCell(OrderColumns.PRODUCTION_COUNT_COLUMN);
    countCell.setCellType(countCell.CELL_TYPE_STRING);

    Integer count = ExcelUtils.getIntegerValue(ExcelUtils.getCellValue(countCell));

    Boolean addToFavorite = ExcelUtils.getBooleanValue(ExcelUtils.getCellValue(
          row.getCell(OrderColumns.PRODUCTION_ADD_TO_FAVORITE_COLUMN)));


    String sku = URLUtils.getSkuFromUrl(url);

    if ((sku != null) && StringUtils.hasText(sku)) {
      ItemInfoCommand itemInfo = new ItemInfoCommand(url, sku, name, price, keyword, count, addToFavorite);

      if (isGroupBuy) {
        itemInfo.setKeyword(null);
        itemInfo.setGrouponId(sku);

        // if @isGroupBuy = true, the keyword is the group buy category.
        itemInfo.setGroupBuyCategory(keyword);
      }

      orderInfo.addItem(itemInfo);

      if (logger.isDebugEnabled()) {
        logger.debug("Added itemInfo: " + itemInfo);
      }
    }
  } // end method assembleNormalOrderProductionInfo

} // end class OrderUtils
