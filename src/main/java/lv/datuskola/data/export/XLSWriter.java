package lv.datuskola.data.export;

import lv.datuskola.file.ImageTransformer;
import lv.datuskola.place.PlaceDataExportDTO;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Service
class XLSWriter {

    @Autowired
    public ImageTransformer imageTransformer;

    void buildXLS(List<Column> columns, Iterator<PlaceDataExportDTO> data, String xlsFile) {
        try {
            recreateXLSFile(xlsFile);

            SXSSFWorkbook workbook = new SXSSFWorkbook(10);
            try {
                buildMainSheet(columns, data, workbook);

                try (FileOutputStream outputStream = new FileOutputStream(xlsFile)) {
                    workbook.write(outputStream);
                }
            } finally {
                workbook.dispose();
            }
        } catch (Exception e) {
            //TODO logging
            e.printStackTrace();
        }
    }

    private void recreateXLSFile(String xlsFile) throws IOException {
        var xls = new File(xlsFile);
        if(xls.exists()) {
            xls.delete();
        }
        xls.createNewFile();
    }

    private void buildMainSheet(List<Column> columns, Iterator<PlaceDataExportDTO> data, SXSSFWorkbook workbook) throws Exception {
        SXSSFSheet sheet = workbook.createSheet("Main");
        sheet.createFreezePane(0, 1);
        createHeader(columns, sheet);
        renderTable(workbook, sheet, data);
    }

    private void createHeader(List<Column> columns, SXSSFSheet sheet) {
        SXSSFRow row = sheet.createRow(0);
        int colNum = 0;
            for (Column column : columns) {
            SXSSFCell cell = row.createCell(colNum);
            cell.setCellValue(column.name());
            sheet.setColumnWidth(colNum, column.width());
            colNum++;
        }
    }

    private void renderTable(SXSSFWorkbook workbook, SXSSFSheet sheet, Iterator<PlaceDataExportDTO> tableData)
            throws Exception {
        int rowNum = 1;
        while(tableData.hasNext()) {
            PlaceDataExportDTO place = tableData.next();
            var row = sheet.createRow(rowNum);
            row.setHeight((short) 8000);

            int colNum = 0;

            RowBuilder rowBuilder = new RowBuilder(imageTransformer, place);
            for(CellBuilder cellBuilder : rowBuilder.buildRowCells()) {
                cellBuilder.render(workbook, sheet, row, rowNum, colNum);
                colNum++;
            }

            rowNum++;
        }
    }

}