package com.ly.web.reader;

import java.io.File;
import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.springframework.util.StringUtils;

import com.ly.excel.BaseExcelReader;

import com.ly.utils.ExcelUtils;
import com.ly.utils.FileUtils;

import com.ly.web.command.OrderCommand;
import com.ly.web.utils.YHDNormalOrderColumns;
import com.ly.web.utils.YHDOrderUtils;


/**
 * Created by yongliu on 11/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/09/2016 16:27
 */
public class ExcelReader extends BaseExcelReader {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -3417859052742663138L;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * readYHDNormalOrderExcelToObject.
   *
   * @param   path  String
   *
   * @return  List
   */
  public List<OrderCommand> readYHDNormalOrderExcelToObject(String path) {
    return readYHDOrderInfoFromExcel(path, Boolean.FALSE);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * readYHTOrderExcelToObject.
   *
   * @param   path  String
   *
   * @return  List
   */
  public List<OrderCommand> readYHTOrderExcelToObject(String path) {
    return readYHDOrderInfoFromExcel(path, Boolean.TRUE);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private List<OrderCommand> readYHDNormalOrderExcel(Workbook wb, int sheetIndex, int startReadLine, int tailLine,
    String groupName, boolean isGroupBuy) {
    List<OrderCommand> ordersInfoList = new LinkedList<>();
    Sheet              sheet          = wb.getSheetAt(sheetIndex);
    Row                row            = null;

    Map<String, OrderCommand> map = new LinkedHashMap<>();

    for (int i = startReadLine; i < (sheet.getLastRowNum() - tailLine + 1); i++) {
      row = sheet.getRow(i);

      OrderCommand orderInfo    = null;
      String       username     = null;
      Cell         userNameCell = null;

      // read i line 0 column orderNum
      boolean isMergeForFirstCol = isMergedRegion(sheet, i, 0);

      if (isMergeForFirstCol) {
        userNameCell = getMergeCell(sheet, row.getRowNum(), 0);
      } else {
        userNameCell = row.getCell(0);
      }

      if (userNameCell != null) {
        username = ExcelUtils.getStringCell(userNameCell);

        if (logger.isDebugEnabled()) {
          logger.debug("username: " + username);
        }

        if ((username != null) && StringUtils.hasText(username)) {
          if (map.get(username) == null) {
            orderInfo = new OrderCommand(Boolean.TRUE);
            orderInfo.setGroupName(groupName);
            map.put(username, orderInfo);
          } else {
            orderInfo = map.get(username);
          }
        }
      }

      if ((username == null) || !StringUtils.hasText(username.trim())) {
        if (logger.isDebugEnabled()) {
          logger.debug("No username, skip this row.");
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

        YHDOrderUtils.assembleNormalOrderExclusiveProductionInfo(orderInfo, row, cell);

        if (cell.getColumnIndex() >= YHDNormalOrderColumns.PRODUCTION_URL_COLUMN) {
          YHDOrderUtils.assembleNormalOrderProductionInfo(orderInfo, row, isGroupBuy);

          break;
        }
      }
    } // end for

    for (OrderCommand orderInfo : map.values()) {
      ordersInfoList.add(orderInfo);
    }

    logger.info("Found " + ordersInfoList.size() + " order(s) for group: " + groupName);

    return ordersInfoList;
  } // end method readYHDNormalOrderExcel

  //~ ------------------------------------------------------------------------------------------------------------------

  private List<OrderCommand> readYHDOrderInfoFromExcel(String path, boolean isGroupBuy) {
    Workbook wb = null;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("<<<<<<<<<<Start process excel from path: " + path);
      }

      String groupName = FileUtils.getFileNameWithoutExtension(path);
      wb = WorkbookFactory.create(new File(path));

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<<<<<<<End processed excel");
      }

      return readYHDNormalOrderExcel(wb, 0, 2, 0, groupName, isGroupBuy);
    } catch (InvalidFormatException e) {
      logger.error(e.getMessage(), e);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }

    return null;
  }

} // end class ExcelReader
