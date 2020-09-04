package lv.datuskola.data.export;

import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

interface CellBuilder {
    void render(SXSSFWorkbook workbook, SXSSFSheet sheet, SXSSFRow columnRow, int rowNum, int colNum) throws Exception;
}
