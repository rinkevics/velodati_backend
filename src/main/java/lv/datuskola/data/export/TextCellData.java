package lv.datuskola.data.export;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

class TextCellData implements CellData {

    private String text;

    public TextCellData(String text) {
        this.text = text;
    }

    public void render(SXSSFWorkbook workbook, SXSSFSheet sheet, SXSSFRow columnRow, int rowNum, int colNum) {
        SXSSFCell cell = columnRow.createCell(colNum);
        cell.setCellValue(text);
    }
}
