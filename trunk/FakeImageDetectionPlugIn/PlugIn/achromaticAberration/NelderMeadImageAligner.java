/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package achromaticAberration;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;

import achromaticAberration.ImageAlignment.Result;
import achromaticAberration.Extractor;

/**
 * Compute the error by expanding target using equation and compare to input
 *
 * @author Ruj
 */
public class NelderMeadImageAligner implements ImageAlignment {

    class MyMultivariateFunction implements MultivariateFunction {

        private final float[] input;
        private final float[] target;
        private final int width;
        private final int height;
        private Rectangle rect;
        private int chk;
        private float minA;
        private float maxA;
        
        // r is rectangle that we choose to check, ck is flag to choose that calculate in rectangle or outside if 
        //ck=1 calculate out side ck!=1 calculate in side
        public MyMultivariateFunction(float[] input, float[] target, int width, 
                int height, Rectangle r,int ck, float minA, float maxA) {
            this.input = input;
            this.target = target;
            this.width = width;
            this.height = height;
            this.rect=r;
            this.minA = minA;
            this.maxA = maxA;
            chk=ck;
          
        }

        @Override
        public double value(double[] point) {
            double a = point[0];
            double x0 = point[1];
            double y0 = point[2];

            // Check bound.  If a is outside of bound we give -1
            if(a > this.maxA || a < this.minA){
                return -1;
            }
            
            int total = 0;
            int pos = 0;
            int x, y;
            // chk=1 calculate a,x0,y0 in Rectangle
            if(this.chk==1){
                for (int i = 0; i < this.height; i++) {
                    for (int j = 0; j < this.width; j++) {

                        if(j>=this.rect.getMinX()&&j<=this.rect.getMaxX()&&i>=this.rect.getMinY()&&i<=this.rect.getMaxY()){
                            //if be in rect
                            x = (int) ((a * ((double) j - x0))+x0);
                            y = (int) ((a * ((double) i - y0))+y0);
                            if (x > 0 && x < width && y > 0 && y < height) {
                                if (target[y * this.height + x] == input[pos]) {
                                    total++;
                                }
                            }
                        }
                        else{
                            
                        }
                    }
                }
            }
            // chk !=1 calculate a,x0,y0 out side Rectangle
            else{
                for (int i = 0; i < this.height; i++) {
                    for (int j = 0; j < this.width; j++) {

                        if(j>=this.rect.getMinX()&&j<=this.rect.getMaxX()&&i>=this.rect.getMinY()&&i<=this.rect.getMaxY()){
                            //if be in rect

                        }
                        else{
                            //if out rect
                            x = (int) ((a * ((double) j - x0))+x0);
                            y = (int) ((a * ((double) i - y0))+y0);
                            if (x > 0 && x < width && y > 0 && y < height) {
                                if (target[y * this.height + x] == input[pos]) {
                                    total++;
                                }
                            }

                        }
                    }
                }    
            }
            return total;
        }
    }

    class MyComparator implements Comparator<PointValuePair>{

        @Override
        public int compare(PointValuePair o1, PointValuePair o2) {
            if(o1.getValue() < o2.getValue()){
                return 1;
            }else if(o1.getValue() == o2.getValue()){
                return 0;
            }
            
            return -1;
        }
    }
    
    @Override
    public Result align(float[] input, float[] target, int width, int height,Rectangle r,int ck) {
        Result result = new Result();

        // Define range of multiplying factor
        float minA = 1.001f;
        float maxA = 1.0078f;
        
        // Set nelder mead starting step
        double[] step = {.001,10,10};
        
        // Create Nelder Mead 
        NelderMeadSimplex nelderMeadSimplex = new NelderMeadSimplex(step);
        MyMultivariateFunction function = 
                new MyMultivariateFunction(input, target, width, height,r,ck, minA, maxA);
        MyComparator comparator = new MyComparator();
       
        // Starting point at middle of image
        double[] startPoint = new double[]{1.001,width/2,height/2};
        nelderMeadSimplex.build(startPoint);
        nelderMeadSimplex.evaluate(function, comparator);
        for (int i = 0; i < 1000; i++) {
            
            nelderMeadSimplex.iterate(function, comparator);
        
        }
        double[] p = nelderMeadSimplex.getPoint(0).getPoint();
        result.a = p[0];
        result.x0 = p[1];
        result.y0 = p[2];
        
        return result;
    }
    public static void main(String []args){
        try {
            NelderMeadImageAligner g=new NelderMeadImageAligner();
            BufferedImage img = ImageIO.read(new File("filter1.png"));
            BufferedImage img1 = ImageIO.read(new File("filter2.png"));
            Rectangle rec=new Rectangle(0,0,img.getWidth(),img.getHeight());
           // img=img.getSubimage(10, 10, 100, 100);
           // img1=img1.getSubimage(10, 10, 100, 100);
            float[][] imageFloatArray = Extractor.toRGBFloatArray(img);
            float[][] imageFloatArray1 = Extractor.toRGBFloatArray(img1);
            Result b=g.align(imageFloatArray[0], imageFloatArray1[0], img.getWidth(), img.getHeight(),rec,1);
            System.out.print(b.toString()+":"+img.getWidth()+":"+img.getHeight());
        } catch (IOException ex) {
            Logger.getLogger(NelderMeadImageAligner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     }
        
     
}
