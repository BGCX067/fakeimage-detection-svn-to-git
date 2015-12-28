/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package achromaticAberration;

import java.util.Arrays;

/**
 * Edge detection using difference of gaussian
 *
 * @author Ruj
 */
public class DifferenceOfGaussian {

    public static final int blurIterationDefault = 4;
    public static final int radius1Default = 3;
    public static final int radius2Default = 2;

    public static void gussianDiff(float[] src, float[] result,
            int imgWidth, int imgHeight,
            float[] buffer1, float[] buffer2, float[] buffer3) {
        gussianDiff(src, result, imgWidth, imgHeight,
                DifferenceOfGaussian.radius1Default,
                DifferenceOfGaussian.radius2Default,
                buffer1, buffer2, buffer3);
    }

    public static void gussianDiff(float[] src, float[] result,
            int imgWidth, int imgHeight, int radius1, int radius2,
            float[] buffer1, float[] buffer2, float[] buffer3) {
        gussianDiff(src, result, imgWidth, imgHeight,
                DifferenceOfGaussian.blurIterationDefault,
                radius1, radius2, buffer1, buffer2, buffer3);
    }

    /**
     * Perform gaussian diff on image
     *
     * @param src
     * @param result Must not be null. Size must equal to src
     * @param imgWidth Width of the image
     * @param imgHeight Heigh of the image
     * @param radius1 Positive value only
     * @param radius2 Positive value only
     * @param blurIteration Number of iteration to estimate gaussian using
     * BoxBlur
     * @param buffer1 If provided, will be used in calculation. Must be at least
     * as big as the source. It must not be the same object as other arguments.
     * @param buffer2 If provided, will be used in calculation. Must be at least
     * as big as the source. It must not be the same object as other arguments.
     * @param buffer2 If provided, will be used in calculation. Must be at least
     * as big as the source. It must not be the same object as other arguments.
     */
    public static void gussianDiff(float[] src, float[] result,
            int imgWidth, int imgHeight, int blurIteration,
            int radius1, int radius2,
            float[] buffer1, float[] buffer2, float[] buffer3) {
        // Sanity check
        if (src == null || result == null || result.length < src.length
                || imgWidth * imgHeight != src.length
                || (buffer1 != null && buffer1.length < src.length)
                || (buffer2 != null && buffer2.length < src.length)
                || (buffer3 != null && buffer3.length < src.length)) {
            throw new IllegalArgumentException();
        }

        if (buffer1 == null) {
            buffer1 = new float[src.length];
        }


        if (buffer2 == null) {
            buffer2 = new float[src.length];
        }

        if (buffer3 == null) {
            buffer3 = new float[src.length];
        }

        int radius = radius1;
        float[] tmp;
        // Box blur 
        BoxBlur.blur(src, buffer1, radius, radius, imgWidth, imgHeight, buffer3);

        // Repeat box blur until we finish approximation
        for (int i = 0; i < blurIteration - 1; i++) {
            BoxBlur.blur(buffer1, buffer2, radius, radius, imgWidth, imgHeight, buffer3);
            // Switch buffer 1 and 2
            tmp = buffer1;
            buffer1 = buffer2;
            buffer2 = tmp;
        }

        // Save to result
        System.arraycopy(buffer1, 0, result, 0, src.length);

        // Do it for radious 2
        radius = radius2;
        // Box blur 
        BoxBlur.blur(src, buffer1, radius, radius, imgWidth, imgHeight, buffer3);
        // Repeat box blur until we finish approximation
        for (int i = 0; i < blurIteration - 1; i++) {
            BoxBlur.blur(buffer1, buffer2, radius, radius, imgWidth, imgHeight, buffer3);
            // Switch buffer 1 and 2
            tmp = buffer1;
            buffer1 = buffer2;
            buffer2 = tmp;
        }

        // Subtract blur with radius1 from blur with radius2
        int total = src.length;
        int positive = 0;
        int negative = 0;
        for (int i = 0; i < total; i++) {
            result[i] = result[i] - buffer1[i];
            if (result[i] < 0f) {
                negative++;
            } else {
                positive++;
            }
        }
    }

    /**
     * Detect edge from diff of gaussian image. This will produce binary image
     * of edge. Done by detecting zero crossing
     *
     * @param src
     * @param result Must not be null. Size must equal to src
     * @param imgWidth Width of the image
     * @param imgHeight Heigh of the image
     *
     */
    public static void detectEdge(float[] src, float[] result,
            int imgWidth, int imgHeight) {
        // Clear result
        Arrays.fill(result,0f);
        
        // Create offset
        int[] offset = {
            -imgWidth - 1, -imgWidth, -imgWidth + 1, -1,
            +1, +imgWidth - 1, +imgWidth, +imgWidth + 1};

        int px = 0, py = 0;
        int p;
        boolean foundPos, foundNeg;
        // Do the inner one first for get the edge for now
        for (int i = 1; i < imgHeight - 1; i++) {
            px = 1;
            py += imgWidth;
            for (int j = 1; j < imgWidth - 1; j++) {
                p = px+py;
                px++;
                /* Check for possible crossing.  Which can be 0 (obvious) or 
                 * a negative (incase crossing is between pixel)
                 * */
                if (src[p] < 0) {
                    // This is neg so it can mean zero crossing is between pix
                    // Just search for positive and take both values
                    for (int k = 0; k < offset.length; k++) {
                        if(src[p + offset[k]] > 0){
                            // Take both value
                            result[p] = 1;
                            result[p + offset[k]] = 1;
                        }
                    }
                } else if (src[p] <= Float.MIN_NORMAL) {
                    // This is zero.  zero crossing can be this one
                    foundPos = false;
                    foundNeg = false;
                    // Scan for positive and negative crossing
                    for (int k = 0; k < offset.length && !(foundPos && foundNeg) ; k++) {
                        if(!foundPos && src[p + offset[k]] > 0){
                            foundPos = true;
//                            result[p] = 1;
                        }
                        if(!foundNeg && src[p + offset[k]] < 0){
                            foundNeg = true;
  //                          result[p] = 1;
                        }
                    }
                    if(foundNeg && foundPos){
                        result[p] = 1;
                    }
                }
            }
        }
    }
}
