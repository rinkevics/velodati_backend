package lv.datuskola.file;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

@Component
@Scope("prototype")
public class ImageTransformer implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Path inputFile;
    private Path outFile;
    private Path squareFile;

    public static final  String PNG = "png";
    public static final String JPG = "jpg";
    public static final String JPEG = "jpeg";

    public void init(Path inputFile, Path outFile, Path squareFile) {
        this.inputFile = inputFile;
        this.outFile = outFile;
        this.squareFile = squareFile;
    }

    @Override
    public void run() {
        try {
            transformImage(inputFile, outFile, squareFile);
        } catch (ImageProcessingException | IOException e) {
            logger.warn("Exception while transforming image", e);
        }
    }

    private void transformImage(Path inputFile, Path outFile, Path squareFile) throws ImageProcessingException, IOException {
        var fileExtension = getFileExtension(inputFile);
        fileExtension = fileExtension.toLowerCase();
        if ( !(PNG.equals(fileExtension)
                || JPG.equals(fileExtension)
                || JPEG.equals(fileExtension))) {
            logger.error("Unsupported file type for file "+ inputFile.toString());
            return;
        }

        BufferedImage destinationImage = transform(inputFile);
        ImageIO.write(destinationImage, fileExtension, outFile.toFile());
        BufferedImage croppedImage = cropRectangle(destinationImage);
        ImageIO.write(croppedImage, fileExtension, squareFile.toFile());
        inputFile.toFile().delete();
    }

    private String getFileExtension(Path inputFile) {
        var fileName = inputFile.getFileName().toString();
        var fileExtensionStart = fileName.lastIndexOf(".");
        if(fileExtensionStart < 0) {
            return "";
        }
        fileExtensionStart += 1;
        var fileExtension = fileName.substring(fileExtensionStart);
        return fileExtension;
    }


    private BufferedImage transform(Path inputFile) throws ImageProcessingException, IOException {
        BufferedImage originalImage = ImageIO.read(inputFile.toFile());

        var fileExtension = getFileExtension(inputFile);
        int orientation = 1;
        if(JPG.equals(fileExtension) || JPEG.equals(fileExtension)) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputFile.toFile());
            orientation = getOrientation(metadata);
        }
        var dimensions = new Dimensions(originalImage);
        int destinationWidth;
        int destinationHeight;

        var affineTransform = new AffineTransform();
        switch(orientation) {
            case 1:
                // do not rotate, this is handled in default case

            case 6:
                // rotate 90 clockwise
                destinationWidth = dimensions.scaledHeight;
                destinationHeight = dimensions.scaledWidth;
                affineTransform.translate(destinationWidth, 0);
                affineTransform.rotate(Math.PI / 2);
                affineTransform.scale(dimensions.scale, dimensions.scale);
                break;
            case 8:
                // rotate 90 counter clockwise
                destinationWidth = dimensions.scaledHeight;
                destinationHeight = dimensions.scaledWidth;
                affineTransform.translate(0, destinationHeight);
                affineTransform.rotate(- Math.PI / 2);
                affineTransform.scale(dimensions.scale, dimensions.scale);
                break;
            case 3:
                // rotate 180 degrees
                destinationWidth = dimensions.scaledWidth;
                destinationHeight = dimensions.scaledHeight;
                affineTransform.translate(destinationWidth, destinationHeight);
                affineTransform.rotate(Math.PI);
                affineTransform.scale(dimensions.scale, dimensions.scale);
                break;

            default:
                // do not rotate
                destinationWidth = dimensions.scaledWidth;
                destinationHeight = dimensions.scaledHeight;
                affineTransform.scale(dimensions.scale, dimensions.scale);
                break;

        }

        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage destinationImage = new BufferedImage(destinationWidth, destinationHeight, originalImage.getType());
        affineTransformOp.filter(originalImage, destinationImage);
        return destinationImage;
    }

    private int getOrientation(Metadata metadata) {

        var propertiesExif = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

        int orientation = 1;
        try {
            if(propertiesExif != null && propertiesExif.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                orientation = propertiesExif.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return orientation;
    }

    private BufferedImage cropRectangle(BufferedImage originalImage) {
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        int targetWidth = 0;
        int targetHeight = 0;
        int xc = 0;
        int yc = 0;

        if(height > width) {
            targetWidth = width;
            targetHeight = width;
            yc = (height - width) / 2;
        } else {
            targetWidth = height;
            targetHeight = height;
            xc = (width - height) / 2;
        }

        // Crop
        var croppedImage = originalImage.getSubimage(
                xc,
                yc,
                targetWidth, // widht
                targetHeight // height
        );
        return croppedImage;
    }
}
