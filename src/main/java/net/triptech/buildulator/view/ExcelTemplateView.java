/*******************************************************************************
 * Copyright (c) 2012 David Harrison, Triptech Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     David Harrison, Triptech Ltd - initial API and implementation
 ******************************************************************************/
package net.triptech.buildulator.view;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.triptech.buildulator.model.DataGrid;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.view.document.AbstractExcelView;


/**
 * The Class ExcelTemplateView.
 */
public class ExcelTemplateView extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DataGrid dataGrid = (DataGrid) model.get("dataGrid");

        String sheetName = "Sheet 1";
        if (StringUtils.isNotBlank(dataGrid.getTitle())) {
            sheetName = dataGrid.getTitle();
        }

        HSSFSheet sheet = workbook.createSheet(sheetName);

        Font font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);

        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFont(font);

        int rowNum = 0;
        int maxColumnCount = 0;

        if (dataGrid.getHeaderFields().size() > 0) {
            HSSFRow header = sheet.createRow(rowNum);
            rowNum++;

            maxColumnCount = dataGrid.getHeaderFields().size();

            int i = 0;
            for (String field : dataGrid.getHeaderFields()) {

                HSSFCell cell = header.createCell(i);
                cell.setCellValue(field);
                cell.setCellStyle(style);
                i++;
            }
        }

        for (int y = 0; y < dataGrid.getRowCount(); y++) {
            HSSFRow row = sheet.createRow(rowNum++);

            List<String> rowData = dataGrid.getRowFields(y);

            if (rowData.size() > maxColumnCount) {
                maxColumnCount = rowData.size();
            }

            int x = 0;

            for (String data : rowData) {
                HSSFCell cell = row.createCell(x);

                try {
                    double dbValue = Double.parseDouble(data);
                    cell.setCellValue(dbValue);
                } catch (NumberFormatException nfe) {
                    cell.setCellValue(data);
                }
                x++;
            }
        }

        for (int i = 0; i < maxColumnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
