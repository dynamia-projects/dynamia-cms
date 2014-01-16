package com.dynamia.cms.site.core;

import com.mortennobel.imagescaling.ResampleOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import javax.imageio.ImageIO;

public class ImageUtil {

    public static void resizeJPEGImage(File input, File output, int thumbWidth, int thumbHeight) {
        try {

            BufferedImage image = ImageIO.read(input);

            double thumbRatio = (double) thumbWidth / (double) thumbHeight;
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);
            double imageRatio = (double) imageWidth / (double) imageHeight;
            if (thumbRatio < imageRatio) {
                thumbHeight = (int) (thumbWidth / imageRatio);
            } else {
                thumbWidth = (int) (thumbHeight * imageRatio);
            }

            ResampleOp op = new ResampleOp(thumbWidth, thumbHeight);
            BufferedImage newImage = op.filter(image, null);
            output.getParentFile().mkdirs();
            output.createNewFile();
            ImageIO.write(newImage, "jpeg", output);
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public static boolean isImage(File file) {
        if (!file.exists()) {
            return file.getName().endsWith("jpg") || file.getName().endsWith("png");
        }

        String mimetype = getMimetype(file);
        return mimetype.contains("image");
    }

    public static String getMimetype(File file) {

        return URLConnection.guessContentTypeFromName(file.getName());
    }

}
