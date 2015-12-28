/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package achromaticAberration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import achromaticAberration.Extractor;
import achromaticAberration.NelderMeadImageAligner;
import achromaticAberration.ImageAlignment.Result;

/**
 *
 * @author rikikun
 */
public class findVectest implements Runnable {
    public BufferedImage img,img1,temp,algor2,realimg,vector,algor3,normal;
    public Rectangle rect;
    String name;
    public double lo;
    final int xsize=50,ysize=100;
    final int hz=20;
    final int scale=5;
    public findVectest(BufferedImage real,BufferedImage imgc,BufferedImage img1c,Rectangle r,String name){
        this.name=name;
        this.img=imgc;
        this.img1=img1c;
        this.realimg=real;
        temp=realimg;
        algor3=new BufferedImage(realimg.getWidth(),realimg.getHeight(),BufferedImage.TYPE_INT_RGB);
        algor2=new BufferedImage(realimg.getWidth(),realimg.getHeight(),BufferedImage.TYPE_INT_RGB);
        normal=new BufferedImage(realimg.getWidth(),realimg.getHeight(),BufferedImage.TYPE_INT_RGB);
        this.rect=r;
        lo=1.0041;
    }
    //build image
    public void buildImage(BufferedImage hh,BufferedImage hq,BufferedImage hr,String name){
        try {
            ImageIO.write(hq,"PNG", new File("algor2"+name+".png"));
            ImageIO.write(hh,"PNG", new File("algor3"+name+".png"));
            ImageIO.write(hr,"PNG", new File("normal"+name+".png"));
        } catch (IOException ex) {
            Logger.getLogger(filterImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //use aligner to plot graph
    public void plotalgor2(Result b,Result c,Rectangle r){
        BufferedImage red=img;
        BufferedImage green=img1;
        BufferedImage real=realimg;
        Graphics g=algor2.getGraphics();
        g.drawImage(real, 0, 0, null);
        g.drawOval((int)b.x0, (int)b.y0, 10, 10);
        //draw fill
        g.setColor(Color.ORANGE);
        g.drawRect(0, 0,red.getWidth()-1,red.getHeight()-1);
     //   g.fillRect(0, 0, red.getWidth(),red.getHeight());
        Color color = new Color(0,255,0,2);        
        g.setColor(Color.BLUE);
        g.drawRect(r.x, r.y,r.width ,r.height);
        g.setColor(color);
        g.fillRect(r.x, r.y,r.width ,r.height);
        g.fillRect(5, 5,100 ,100);

     //   System.out.print(recanswer+"  %\n");
        g.setColor(Color.BLACK);
        drawString(g,String.valueOf("A="+b.a+"\nX0="+b.x0+"\nY0="+b.y0),(int)(red.getWidth()/2),(int)(red.getHeight()/2));
        drawString(g,String.valueOf("A="+c.a+"\nX0="+c.x0+"\nY0="+c.y0),(int)(r.getCenterX()),(int)(r.getCenterY()));
    }
    private void drawString(Graphics g, String text, int x, int y) {
                for (String line : text.split("\n"))
                    g.drawString(line, x, y += g.getFontMetrics().getHeight());
            }
    public int[] rescale(int x,int y,int xx,int yy){
        int []a=new int[2];
        if(xx>x&&yy>y){
            xx=xx+scale;
            yy=yy+scale;
        }
        else if(xx>x&&yy<y){
            xx=xx+scale;
            yy=yy-scale;
        }
        else if(xx>x&&yy==y){
            xx=xx+scale;
        }
        else if(xx==x&&yy<y){
            yy=yy-scale;
        }
        else if(xx==x&&yy>y){
            yy=yy+scale;
        }
        else if(xx<x&&yy<y){
            yy=yy-scale;
            xx=xx-scale;
        }
        else if(xx<x&&yy==y){
            xx=xx-scale;
        }
        else if(xx<x&&yy>y){
            yy=yy+scale;
            xx=xx-scale;
        }
        a[0]=xx;
        a[1]=yy;
        return a;
    }

    public void plotalgor3(Rectangle r,Result b,Result c,int flag){
        BufferedImage real=realimg;
        BufferedImage gg=img;
        BufferedImage qq=img1;
        Graphics g=algor3.getGraphics();
        Color color = new Color(0,255, 0, 2);
        g.drawImage(real, 0, 0, null);
        g.drawRect(r.x, r.y, r.width, r.height);
        int xx,yy;
        g.setColor(color);
            for(int x=0;x<gg.getWidth();x=x+hz){
                for(int y=0;y<gg.getHeight();y=y+hz){
                    xx=(int)Math.round((b.a*(x-(b.x0)))+(b.x0));
                    yy=(int)Math.round((b.a*(y-(b.y0)))+(b.y0));
                    //change scale
                    int a[]=null;
                    a=rescale(x,y,xx,yy);
                    xx=a[0];
                    yy=a[1];
                    //if in Rectangle
                    if(x>=r.getMinX()&&x<=r.getMaxX()&&y>=r.getMinY()&&y<=r.getMaxY()){

                    }
                    // out side Rectangle
                    else{
                        g.setColor(Color.GREEN);
                        //if have point in this pixel
                        if(gg.getRGB(x, y)==-1){
                            if(xx<gg.getWidth()&&yy<gg.getHeight()&&xx>0&&yy>0){
                                if(qq.getRGB(xx, yy)==-1){
                                    g.drawLine(x, y, xx, yy);
                                    g.drawOval(xx-2, yy-2, 5,5);
                                }
                                else{
                                    g.drawLine(x, y, xx, yy);
                                    g.drawOval(xx-2, yy-2, 5,5);
                                }

                            }
                        }
                        //if have not point in this pixel
                        else{
                            if(xx<gg.getWidth()&&yy<gg.getHeight()&&xx>0&&yy>0){
                                if(qq.getRGB(xx, yy)==-1){
                                    g.drawLine(x, y, xx, yy);
                                    g.drawOval(xx-2, yy-2, 5,5);
                                }
                                else{
                                    g.drawLine(x, y, xx, yy);
                                    g.drawOval(xx-2, yy-2, 5,5);
                                }

                            }
                            
                        }
                    }
                }
            }
            g.setColor(Color.BLUE);
            //g.setColor(color1);
            for(int x=0;x<gg.getWidth();x=x+hz){
                for(int y=0;y<gg.getHeight();y=y+hz){
                    xx=(int)Math.round((c.a*(x-(c.x0)))+(c.x0));
                    yy=(int)Math.round((c.a*(y-(c.y0)))+(c.y0));
                    //change scale
                    int a[]=null;
                    a=rescale(x,y,xx,yy);
                    xx=a[0];
                    yy=a[1];
                    //in side rectangle
                    if(x>=r.getMinX()&&x<=r.getMaxX()&&y>=r.getMinY()&&y<=r.getMaxY()){
                        //if have point in this pixel
                        if(gg.getRGB(x, y)==-1){
                                if(xx<gg.getWidth()&&yy<gg.getHeight()&&xx>0&&yy>0){
                                    if(qq.getRGB(xx, yy)==-1){
                                        g.drawLine(x, y, xx, yy);
                                        g.drawOval(xx-2, yy-2, 5,5);
                                    }
                                    else{
                                        g.drawLine(x, y, xx, yy);
                                        g.drawOval(xx-2, yy-2, 5,5);
                                    }
                                }
                            }
                        //if have not point in this pixel
                        else{
                            if(xx<gg.getWidth()&&yy<gg.getHeight()&&xx>0&&yy>0){
                                    if(qq.getRGB(xx, yy)==-1){
                                        g.drawLine(x, y, xx, yy);
                                        g.drawOval(xx-2, yy-2, 5,5);
                                    }
                                    else{
                                        g.drawLine(x, y, xx, yy);
                                        g.drawOval(xx-2, yy-2, 5,5);
                                    }
                                }
                        }
                    }
                    //out side rectangle
                    else{
                      
                    }
                }
            }
    }
    //algor 3
    public void chkEq(Rectangle rr){
        Result b;
        Result c;
        Rectangle r=rr;

            NelderMeadImageAligner ge=new NelderMeadImageAligner();

    
            float[][] imageFloatArray = Extractor.toRGBFloatArray(img);
            float[][] imageFloatArray1 = Extractor.toRGBFloatArray(img1);

                    b=ge.align(imageFloatArray[0], imageFloatArray1[0], img.getWidth(), img.getHeight(),r,1);
                    System.out.print(b.toString()+"\n");
                    c=ge.align(imageFloatArray[0], imageFloatArray1[0], img.getWidth(), img.getHeight(),r,2);
                    System.out.print(c.toString()+"\n");
                    plotalgor3(r,b,c,2);
    }
    //algor 2
    public void startChkPer(String name,Rectangle re){

       Rectangle r=re;
       Result b;
       Result c;
       
            NelderMeadImageAligner ge=new NelderMeadImageAligner();

            float[][] imageFloatArray = Extractor.toRGBFloatArray(img);
            float[][] imageFloatArray1 = Extractor.toRGBFloatArray(img1);

                    b=ge.align(imageFloatArray[0], imageFloatArray1[0], img.getWidth(), img.getHeight(),r,1);
                    c=ge.align(imageFloatArray[0], imageFloatArray1[0], img.getWidth(), img.getHeight(),r,2);
                    plotalgor2(b,c,r); 
    }
    public void startVector(){
        Rectangle r=new Rectangle (0,0,0,0);
        Result b;
        int xx,yy;
        Graphics g=this.normal.createGraphics();
        g.setColor(Color.red);
        g.drawImage(this.realimg,0,0, null);
        NelderMeadImageAligner ge=new NelderMeadImageAligner();
            float[][] imageFloatArray = Extractor.toRGBFloatArray(img);
            float[][] imageFloatArray1 = Extractor.toRGBFloatArray(img1);
            b=ge.align(imageFloatArray[0], imageFloatArray1[0], img.getWidth(), img.getHeight(),r,1);
            for(int x=0;x<img.getWidth();x=x+hz){
                for(int y=0;y<img.getHeight();y=y+hz){
                    xx=(int)Math.round((b.a*(x-(b.x0)))+(b.x0));
                    yy=(int)Math.round((b.a*(y-(b.y0)))+(b.y0));
                    //change scale
                    int a[]=null;
                    a=rescale(x,y,xx,yy);
                    xx=a[0];
                    yy=a[1];
                    //if have point in this pixel
                    if(img.getRGB(x, y)==-1){
                                if(xx<img.getWidth()&&yy<img.getHeight()&&xx>0&&yy>0){
                                        g.drawLine(x, y, xx, yy);
                                        g.drawOval(xx, yy, 5,5);
                                }
                    }
                    //if have not point in this pixel
                    else{
                        if(xx<img.getWidth()&&yy<img.getHeight()&&xx>0&&yy>0){
                                        g.drawLine(x, y, xx, yy);
                                        g.drawOval(xx, yy, 5,5);
                                }
                    }
                }
            }
    }
    @Override
    public void run() {
            //call algor 2
        startChkPer(name,this.rect);
            //call algor 3
        chkEq(this.rect);
        startVector();
        buildImage(algor3,algor2,normal,name);
    }
}
