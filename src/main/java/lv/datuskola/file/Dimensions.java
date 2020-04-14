package lv.datuskola.file;

import java.awt.image.BufferedImage;

class Dimensions {
    int scaledWidth;
    int scaledHeight;
    double scale;

    public Dimensions(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        if (height > width) {
            scale = (float) 500 / (float) width;
        } else {
            scale = (float) 500 / (float) height;
        }

        scaledWidth = (int) (width * scale);
        scaledHeight = (int) (height * scale);
    }
}