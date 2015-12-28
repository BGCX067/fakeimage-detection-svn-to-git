/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package achromaticAberration;

import java.util.Arrays;

/**
 * BoxBlur system. This one use fast box blur method outline here
 * http://web.archive.org/web/20060718054020/http://www.acm.uiuc.edu/siggraph/workshops/wjarosz_convolution_2001.pdf
 *
 * @author Ruj
 */
public class BoxBlur {

    /**
     * Perform bluring on src image. Width has to be given. The data input is
     * assumed to be row based.
     *
     * @param src Data source. Assume one color only. Row base array
     * @param result Array storing results.
     * @param img_width Indicate length of each row
     * @param img_height Indicate height of image
     * @param buffer If not null and has equal or greater size to src, it will
     * be used to store temporary data during computation. This will help save
     * memory allocation time.
     */
    public static void blur(float[] src, float[] result,
            int blur_radious_width, int blur_radious_height,
            int img_width, int img_height, float[] buffer) {

        // Sanity check
        if (src == null || result == null || img_width < 1 || img_height < 1) {
            throw new IllegalArgumentException();
        }
        if (src.length % img_width > 0) {
            throw new IllegalArgumentException("Width mismatch with src array");
        }

        if (buffer == null || buffer.length < src.length) {
            buffer = new float[src.length];
        }

        // Init result;
        Arrays.fill(buffer, 0f);
        Arrays.fill(result, 0f);

        int direction_radius_limit = blur_radious_height;
        int total_stripes = img_width;
        int direction_limit = img_height;
        int increment = img_width;
        int stripe_start_posiion_increment = 1;

        // Blur vertically
        directional_blur(total_stripes, stripe_start_posiion_increment,
                direction_radius_limit, src, increment, buffer, direction_limit);

        // Blur horizontally
        direction_radius_limit = blur_radious_width;
        total_stripes = img_height;
        direction_limit = img_width;
        increment = 1;
        stripe_start_posiion_increment = img_width;
        directional_blur(total_stripes, stripe_start_posiion_increment,
                direction_radius_limit, buffer, increment, result, direction_limit);

        /**
         * Average all. It should be noted that the edges need different average
         * factors
         */
        // Create template for averaging
        float[][] avgTemplate = new float[blur_radious_height + 1][blur_radious_width + 1];
        for (int i = 0; i <= blur_radious_height; i++) {
            for (int j = 0; j <= blur_radious_width; j++) {
                avgTemplate[i][j] = 1f / (((blur_radious_height * 2 + 1) - i)
                        * ((blur_radious_width * 2 + 1) - j));
            }
        }

        int templateX, templateY, p = 0;
        for (int i = 0; i < img_height; i++) {
            templateY = blur_radious_height - i;
            if (templateY < 0) {
                templateY = i - (img_height - blur_radious_height) + 1;
                if (templateY < 0) {
                    templateY = 0;
                }
            }
            for (int j = 0; j < img_width; j++) {
                templateX = blur_radious_width - j;
                if (templateX < 0) {
                    templateX = j - (img_width - blur_radious_width) + 1;
                    if (templateX < 0) {
                        templateX = 0;
                    }
                }
               // System.out.println(templateX+","+templateY);
                result[p] *= avgTemplate[templateY][templateX];
                p++;
            }
        }

//        float factor = 1.0f / ((float) (2 * blur_radious_width + 1) * (2 * blur_radious_height + 1));
//        // Perform average
//        int total = result.length;
//        for (int i = 0; i < total; i++) {
//            result[i] *= factor;
//        }
    }

    /**
     * Actual working part for Blur
     */
    private static void directional_blur(int total_stripes,
            int stripe_start_posiion_increment, int direction_radius_limit, float[] src,
            int increment, float[] result, int direction_limit) {
        float v;
        int front_p, tail_p, result_pos, limit;
        int stripe_start_position = 0;

        int part1Limit = direction_radius_limit;
        int part2Limit = direction_limit - direction_radius_limit;
        // For each slot(can be column or row) performs moving window sum
        for (int j = 0; j < total_stripes; j++) {
            v = 0;
            front_p = stripe_start_position;
            tail_p = stripe_start_position;
            result_pos = stripe_start_position;

            stripe_start_position += stripe_start_posiion_increment;

            // Start summing first part (filling up window
            for (int i = 0; i < direction_radius_limit; i++) {
                v += src[front_p];
                front_p += increment;
            }
            /* To make it less complicate, we split summing into 3 parts. 1) When we
             * don't have full windows at the top, 2) when we have full windows and 
             * 3) when we don't have full windows at the bottom
             */
            // Part 1
            for (int i = 0; i <= part1Limit; i++) {
                // Add one more value in
                v += src[front_p];
                result[result_pos] += v;
                front_p += increment;
                result_pos += increment;
            }
            // Part 2
            for (int i = part1Limit + 1; i < part2Limit; i++) {
                // Add one more value in and take one out
                v += src[front_p];
                v -= src[tail_p];
                result[result_pos] += v;
                front_p += increment;
                tail_p += increment;
                result_pos += increment;
            }
            // Part 3
            for (int i = part2Limit; i < direction_limit; i++) {
                // Take one value out
                v -= src[tail_p];
                result[result_pos] += v;
                tail_p += increment;
                result_pos += increment;
            }
        }
    }
}
