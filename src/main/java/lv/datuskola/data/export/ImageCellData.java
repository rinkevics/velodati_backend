package lv.datuskola.data.export;

import lv.datuskola.file.Dimensions;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFShape;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;

import static lv.datuskola.file.ImageTransformer.*;

class ImageCellData implements CellData {

    private final String fileName;
    private final String fileType;

    public ImageCellData(String fileName, String fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public void render(SXSSFWorkbook workbook, SXSSFSheet sheet, SXSSFRow columnRow, int rowNum, int colNum) throws Exception {
        Path path = Path.of(fileName);
        if(!path.toFile().exists()) {
            return;
        }

        var is = getImage(path);
        int pictureIndex = workbook.addPicture(is, getWorkbookType(fileType));

        var helper = workbook.getCreationHelper();
        var anchor = helper.createClientAnchor();
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
        anchor.setCol1(colNum);
        anchor.setRow1(rowNum);
        anchor.setCol2(colNum + 10);
        anchor.setRow2(rowNum);

        Drawing<XSSFShape> drawing = sheet.createDrawingPatriarch();
        final Picture pict = drawing.createPicture(anchor, pictureIndex);
        pict.resize();
    }

    private int getWorkbookType(String fileType) {
        return switch(fileType.toLowerCase()) {
            case JPG, JPEG -> Workbook.PICTURE_TYPE_JPEG;
            case PNG -> Workbook.PICTURE_TYPE_PNG;
            default -> throw new RuntimeException("unsupported file type");
        };
    }

    private byte[] getImage(Path path) throws IOException {
        var destinationImage = scale(500, path);
        var baos = new ByteArrayOutputStream();
        ImageIO.write(destinationImage, fileType, baos);
        return baos.toByteArray();
    }

    private BufferedImage scale(int cropDimension, Path path) throws IOException {
        BufferedImage originalImage = ImageIO.read(path.toFile());
        var dimensions = Dimensions.build2(originalImage, cropDimension);

        var affineTransform = new AffineTransform();
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage destinationImage = new BufferedImage(dimensions.scaledWidth(), dimensions.scaledHeight(),
                originalImage.getType());
        affineTransformOp.filter(originalImage, destinationImage);
        return destinationImage;
    }

}