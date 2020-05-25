package lv.datuskola.data.export;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TextCellData implements CellData {

    private String text;

    public TextCellData(String text) {
        this.text = text;
    }

    public void render(XSSFWorkbook workbook, XSSFSheet sheet, XSSFRow columnRow, int rowNum, int colNum) throws Exception {
        XSSFCell cell = columnRow.createCell(colNum);
        cell.setCellValue(text);
    }
}
