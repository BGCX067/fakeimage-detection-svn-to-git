/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package achromaticAberration;

import java.awt.Rectangle;

/**
 * For aligning image using equation a(x)+x0 and a(y)+y0 This is for aligning
 * black and white image only
 *
 * @author Ruj
 */
public interface ImageAlignment {

    public class Result {
        public double a;
        public double x0;
        public double y0;

        @Override
        public String toString() {
            return "a = "+a+",x_0 = "+x0+",y_0 = "+y0; 
        }
        
        
    }

    Result align (float[] input, float[] target, int width, int height,Rectangle r,int ck);
}
