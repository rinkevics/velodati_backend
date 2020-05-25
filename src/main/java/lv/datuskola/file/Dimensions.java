package lv.datuskola.file;

import java.awt.image.BufferedImage;

public record Dimensions(double scale, int scaledWidth, int scaledHeight) {

    public static Dimensions build(BufferedImage bufferedImage, int cropDimension) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        double scale = 0;

        if (height > width) {
            scale = (float) cropDimension / (float) width;
        } else {
            scale = (float) cropDimension / (float) height;
        }

        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);

        return new Dimensions(scale, scaledWidth, scaledHeight);
    }

    public static Dimensions build2(BufferedImage bufferedImage, int cropDimension) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        double scale = 0;

        if (height > cropDimension) {
            scale = (float) cropDimension / (float) height;
        } else {
            scale = 1;
        }

        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);

        return new Dimensions(scale, scaledWidth, scaledHeight);
    }
}