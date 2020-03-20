package lv.datuskola.file;

import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.jpeg.JpegDirectory;

class Dimensions {
    int scaledWidth;
    int scaledHeight;
    double scale;

    public Dimensions(Metadata metadata) throws MetadataException {
        var properties = metadata.getFirstDirectoryOfType(JpegDirectory.class);
        int width = properties.getImageWidth();
        int height = properties.getImageHeight();

        if (height > width) {
            scale = (float) 500 / (float) width;
        } else {
            scale = (float) 500 / (float) height;
        }

        scaledWidth = (int) (width * scale);
        scaledHeight = (int) (height * scale);
    }
}