package algorithmsPackage;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import achromaticAberration.FakeImageDetection;
import achromaticAberration.filterImage;
import achromaticAberration.findVectest;
import ImageObject.ImageObject;
import Interface.AlgorithmInterface;

public class achromaticAberration implements AlgorithmInterface {

	private ImageObject instance;

	@Override
	public String getName() {
		return "Achromatic Aberration";
	}

	@Override
	public BufferedImage getResultImage() {
		instance = ImageObject.getInstance();
		instance.getBufferedImage();
		String temp=instance.getFilePath().getName().replaceAll(".jpg","");
		temp =temp.replaceAll(".pgn", "");
		String name=temp;
		String location=instance.getFilePath().getPath();
        FakeImageDetection build=new FakeImageDetection();
      //  System.out.print(name+"\n"+location);
        if(build.startBuildImage(location,instance.getFilePath().getParent())==0){
            //build.startBuildImage(path,dispath);
        	location=instance.getFilePath().getParent();
             filterImage a=new filterImage(name,location);
           // File file=new File(path);
          //   findVectest 
            System.out.print("Finish\n");
        }
        Point[] envelope = instance.getMouseEnvelope();
        Rectangle r=new Rectangle();
        r.x=envelope[0].x;
        r.y=envelope[0].y;
        r.height=envelope[3].y-envelope[0].y;
        r.width=envelope[3].x-envelope[0].x;
        BufferedImage gg=null;
        BufferedImage bb=null;
        BufferedImage real=null;
        BufferedImage br2=null;
        BufferedImage br1=null;
        BufferedImage br3=null;
        //======================================================================================================== path
        try {
        	
            real=ImageIO.read(instance.getFilePath());
            gg=ImageIO.read(new File("filter1_"+name+".png"));
            bb = ImageIO.read(new File("filter2_"+name+".png"));
        } catch (IOException ex) {
            Logger.getLogger(findVectest.class.getName()).log(Level.SEVERE, null, ex);
        }
        findVectest a=new findVectest(real,gg,bb,r,name);
        Thread t=new Thread(a);
        t.start();
        try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //
        try {
        	//choose your out put can choose 3 out put
			 br1=ImageIO.read(new File("algor2"+name+".png")); 
			 br2=ImageIO.read(new File("algor3"+name+".png"));
             br3=ImageIO.read(new File("normal"+name+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
//		envelope[0];
//		envelope[1];
//		envelope[2];
//		envelope[3];
		return br2;
        //return null;
	}

	@Override
	public boolean needEnvelope() {
		// TODO Auto-generated method stub
		return true;
	}

}
