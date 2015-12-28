/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package achromaticAberration;

/**
 *
 * @author Ruj
 */
public class ImageMath {

    /**
     * Will normalized the array centering the 0 at 128 and the positive above
     * and the negative under it.
     */
    public static void normalizeNeg(float[] array, int length) {
        float max = array[0];
        float min = array[0];
        // Find max
        for (int i = 0; i < length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
            if (array[i] < min) {
                min = array[i];
            }
        }

        // Normalize
        float factorPos = 128f / max;
        float factorNeg = 128f / min;
        for (int i = 0; i < length; i++) {
            if (array[i] >= 0) {
                array[i] = array[i] * factorPos + 128f;
            } else {
                array[i] = 128f - array[i] * factorNeg;
            }
        }
    }

    // Will normalized the array using the positive only
    public static void normalizePos(float[] array, int length) {
        float max = array[0];
        float min = array[0];
        // Find max
        for (int i = 0; i < length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        // Normalize
        float factorPos = 255f / max;
        for (int i = 0; i < length; i++) {
            array[i] = array[i] * factorPos;
        }
    }

    // Will normalized the array
    public static void brighten(float[] array, int length) {
        float max = 0f;
        // Find max
        for (int i = 0; i < length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        // Normalize
        float factor = 255 - max;
        for (int i = 0; i < length; i++) {
            array[i] += factor;
        }
    }

    public static void zeroOnly(float[] array, int length) {
        float zeroThreshold = 0.0000001f;
        float max = 0f;
        // Find max
        for (int i = 0; i < length; i++) {
            if (array[i] < zeroThreshold) {//&& array[i] > -zeroThreshold){
                array[i] = 255f;
            } else {
                array[i] = 0f;
            }
        }
    }
    
    public static void countSign(float[] array, int length){
        int pos = 0, zero = 0, neg = 0;
        for (int i = 0; i < length; i++) {
            if(array[i] < 0){
                neg++;
            }else if(array[i] < Float.MIN_NORMAL){
                zero++;
            }else{
                pos++;
            }
        }
        
        System.out.println(pos+","+zero+","+neg);
    }
    
    // Convert Negative to Red, Zero to Greeen and Positive to blue
    public static int[] nzpToRGB(float[] src){
        int[] result = new int[src.length];
        int p = 0;
        for (int i = 0; i < src.length; i++) {
            if(src[i] < 0){
                result[i] = 0xff0000;
            }else if(src[i] < Float.MIN_NORMAL){
                result[i] = 0x00ff00;
            }else{
                result[i] = 0x0000ff;
            }
            p+=1;
        }
        
        return result;
    }
}
