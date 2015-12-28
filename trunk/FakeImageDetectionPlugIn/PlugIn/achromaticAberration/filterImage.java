/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package achromaticAberration;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 *
 * @author rikikun
 */
public class filterImage {

   public filterImage(String name,String path) {
        BufferedImage img = null;
        BufferedImage img2 = null;
        BufferedImage img3;
        System.out.println(path+File.separator+"DOG_EDGE_R_"+name+".png");
        String temp=name.replaceAll(".png", "");
        temp=temp.replaceAll(".jpg", "");
        System.out.println(temp);
        name=temp;
        try {
            //======================================================================================================= path
            img=ImageIO.read(new File(path+File.separator+"DOG_EDGE_R_"+name+".png"));
            img2=ImageIO.read(new File(path+File.separator+"DOG_EDGE_G_"+name+".png"));
        } catch (IOException ex) {
            Logger.getLogger(filterImage.class.getName()).log(Level.SEVERE, null, ex);
        } 
        filter3d(img,img2,"filter1_"+name);
        filter3d(img2,img,"filter2_"+name);
    }
    private void filter3d(BufferedImage src,BufferedImage dis,String name){
        BufferedImage resultImg = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for(int q=0;q<3;q++){
            for(int x=0;x<src.getWidth();x++){
                for(int y=0;y<src.getHeight();y++){
                    if(src.getRGB(x, y)==-1){
                        if(x<src.getWidth()&&y<src.getHeight()){
                            if(dis.getRGB(x, y)==-1){
                                resultImg.setRGB(x, y, -1);
                            }
                        }
                        if((x-q)>0&&(x-q)<src.getWidth()&&(y-q)>0&&(y-q)<src.getHeight()){
                            if(dis.getRGB(x-q, y-q)==-1){
                                resultImg.setRGB(x, y, -1);
                            }
                        }
                        else if((x)>0&&(x)<src.getWidth()&&(y-q)>0&&(y-q)<src.getHeight()){
                            if(dis.getRGB(x, y-q)==-1){
                                resultImg.setRGB(x, y, -1);
                            }
                        }
                         if((x+q)>0&&(x+q)<src.getWidth()&&(y-q)>0&&(y-q)<src.getHeight()){
                            if(dis.getRGB(x+q, y-q)==-1){
                                resultImg.setRGB(x, y, -1);
                            }
                        }
                         if((x-q)>0&&(x-q)<src.getWidth()&&(y)>0&&(y)<src.getHeight()){
                            if(dis.getRGB(x-q, y)==-1){
                                resultImg.setRGB(x, y, -1);
                            }
                        }
                         if((x+q)>0&&(x+q)<src.getWidth()&&(y)>0&&(y)<src.getHeight()){
                            if(dis.getRGB(x+q, y)==-1){
                                resultImg.setRGB(x, y, -1);
                            }
                        }
                         if((x-q)>0&&(x-q)<src.getWidth()&&(y+q)>0&&(y+q)<src.getHeight()){
                            if(dis.getRGB(x-q, y+q)==-1){
                                resultImg.setRGB(x, y, -1);
                            }
                        }
                         if((x)>0&&(x)<src.getWidth()&&(y+q)>0&&(y+q)<src.getHeight()){
                            if(dis.getRGB(x, y+q)==-1){
                                resultImg.setRGB(x, y, -1);
                            }
                        }
                         if((x+q)>0&&(x+q)<src.getWidth()&&(y+q)>0&&(y+q)<src.getHeight()){
                            if(dis.getRGB(x+q, y+q)==-1){
                                resultImg.setRGB(x, y, -1);
                            }
                        }
                    }
                }
            }
        }
        try {
        	System.out.println(name);
            ImageIO.write(resultImg,"PNG", new File(name+".png"));
        } catch (IOException ex) {
            Logger.getLogger(filterImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String []args){
        filterImage a=new filterImage("fake-1","");
       
    }

}
