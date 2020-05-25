package lv.datuskola.data.export;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

interface CellData {
    void render(XSSFWorkbook workbook, XSSFSheet sheet, XSSFRow columnRow, int rowNum, int colNum) throws Exception;
}
