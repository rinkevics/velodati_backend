package lv.datuskola.data.export;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;

@Service
class XLSWriter {

    void write(List<Column> columns, List<CellData[]> data, String xlsFile) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            writeMainSheet(columns, data, workbook);
            FileOutputStream outputStream = new FileOutputStream(xlsFile);
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeMainSheet(List<Column> columns, List<CellData[]> data, XSSFWorkbook workbook) throws Exception {
        XSSFSheet sheet = workbook.createSheet("Main");
        sheet.createFreezePane(0, 1);
        createHeader(columns, sheet);
        renderTable(workbook, sheet, data);
    }

    private static void createHeader(List<Column> columns, XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(0);
        int colNum = 0;
            for (Column column : columns) {
            XSSFCell cell = row.createCell(colNum);
            cell.setCellValue(column.name());
            sheet.setColumnWidth(colNum, column.width());
            colNum++;
        }
    }

    private static void renderTable(XSSFWorkbook workbook, XSSFSheet sheet, List<CellData[]> tableData)
            throws Exception {
        int rowNum = 1;
        for(var rowData : tableData) {
            var row = sheet.createRow(rowNum);
            row.setHeight((short) 8000);

            int colNum = 0;
            for(var cellData : rowData) {
                cellData.render(workbook, sheet, row, rowNum, colNum);
                colNum++;
            }
            rowNum++;
        }
    }

}