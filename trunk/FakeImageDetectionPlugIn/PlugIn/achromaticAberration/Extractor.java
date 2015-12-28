package achromaticAberration;

import java.awt.image.BufferedImage;

/**
 * This class extract Red Green Blue channel into 3 gray scale images
 *
 * @author Ruj
 */
public class Extractor {

    public enum Channel {

        RED, GREEN, BLUE
    };

    public static float[][] toRGBFloatArray(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        // Get RGB info for the whole image
        int[] o_rgbArray = img.getRGB(0, 0, width, height,
                null, 0, width);

        // Create gray scale 2d array of each channel
        float[][] rgbArray = new float[3][width*height];

        int pixel;
        for (int i = 0; i < o_rgbArray.length; i++) {
            int rgb = o_rgbArray[i];
            // Get blue
            pixel = (rgb) & 0x000000FF;
            rgbArray[2][i] = pixel;

            // Get green
            rgb = rgb >> 8;
            pixel = (rgb) & 0x000000FF;
            rgbArray[1][i] = pixel;

            // Get Red
            rgb = rgb >> 8;
            pixel = (rgb) & 0x000000FF;
            rgbArray[0][i] = pixel;
        }

        return rgbArray;
    }

    public static float[][][] toRGB2DFloatArray(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        // Get RGB info for the whole image
        int[] o_rgbArray = img.getRGB(0, 0, width, height,
                null, 0, width);

        // Create gray scale 2d array of each channel
        float[][][] rgbArray = new float[3][height][width];

        int pixel;
        int x = 0, y = 0;
        for (int i = 0; i < o_rgbArray.length; i++) {
            int rgb = o_rgbArray[i];
            // Get blue
            pixel = (rgb) & 0x000000FF;
            rgbArray[2][x][y] = pixel;

            // Get green
            rgb = rgb >> 8;
            pixel = (rgb) & 0x000000FF;
            rgbArray[1][x][y] = pixel;

            // Get Red
            rgb = rgb >> 8;
            pixel = (rgb) & 0x000000FF;
            rgbArray[0][x][y] = pixel;

            x++;
            if (x >= width) {
                x = 0;
                y++;
            }
        }

        return rgbArray;
    }

    public static BufferedImage[] toRGBImages(BufferedImage img) {
        BufferedImage[] imageArray = new BufferedImage[3];

        // Get RGB info for the whole image
        int[] o_rgbArray = img.getRGB(0, 0, img.getWidth(), img.getHeight(),
                null, 0, img.getWidth());

        // Create gray scale image of each channel
        int[][] rgbArray = new int[3][o_rgbArray.length];

        int pixel;
        for (int i = 0; i < o_rgbArray.length; i++) {
            int rgb = o_rgbArray[i];
            // Get blue
            pixel = (rgb) & 0x000000FF;
            rgbArray[2][i] = pixel + pixel << 8 + pixel << 16;

            // Get green
            rgb = rgb >> 8;
            pixel = (rgb) & 0x000000FF;
            rgbArray[1][i] = pixel + pixel << 8 + pixel << 16;

            // Get Red
            rgb = rgb >> 8;
            pixel = (rgb) & 0x000000FF;
            rgbArray[0][i] = pixel + pixel << 8 + pixel << 16;
        }

        // Create buffered image from data
        BufferedImage g_img;

        for (int i = 0; i < 3; i++) {
            //imageArray[i] = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
            imageArray[i] = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            //imageArray[i].getRaster().setPixels(0, 0, img.getWidth(), img.getHeight(), rgbArray[i]);
            imageArray[i].getRaster().setDataElements(0, 0, img.getWidth(), img.getHeight(), rgbArray[i]);
        }
        return imageArray;
    }
}
