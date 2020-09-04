package lv.datuskola.data.export;

import lv.datuskola.file.FilesStore;
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
import java.util.Iterator;
import java.util.List;

@Service
class XLSWriter {

    @Autowired
    public ImageTransformer imageTransformer;

    void write(List<Column> columns, Iterator<PlaceDataExportDTO> data, String xlsFile) {
        try {
            var xls = new File(xlsFile);
            if(xls.exists()) {
                xls.delete();
            }
            xls.createNewFile();

            SXSSFWorkbook workbook = new SXSSFWorkbook(10);
            writeMainSheet(columns, data, workbook);

            FileOutputStream outputStream = new FileOutputStream(xlsFile);
            workbook.write(outputStream);
            outputStream.close();

            workbook.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeMainSheet(List<Column> columns, Iterator<PlaceDataExportDTO> data, SXSSFWorkbook workbook) throws Exception {
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
        PlaceDataExportDTO place;
        while(tableData.hasNext()) {
            place = tableData.next();
            var row = sheet.createRow(rowNum);
            row.setHeight((short) 8000);

            int colNum = 0;

            new TextCellData(String.valueOf(place.id)).render(workbook, sheet, row, rowNum, colNum++);
            new TextCellData(place.placeType != null ? place.placeType.label : "").render(workbook, sheet, row, rowNum, colNum++);
            new TextCellData(String.valueOf(place.voteCount)).render(workbook, sheet, row, rowNum, colNum++);
            new TextCellData(place.townHallReplyState != null ? place.townHallReplyState.label : "").render(workbook, sheet, row, rowNum, colNum++);
            getImageCell(place).render(workbook, sheet, row, rowNum, colNum++);

            rowNum++;
        }
    }

    private CellData getImageCell(PlaceDataExportDTO place) {
        CellData imageCellData;
        if(place.img != null && !place.img.isEmpty()) {
            imageCellData = new ImageCellData(FilesStore.IMG_FOLDER + File.separator + place.img, imageTransformer.getFileExtension(place.img));
        } else {
            imageCellData = new TextCellData("");
        }
        return imageCellData;
    }

}