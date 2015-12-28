package GUI;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import ImageObject.ImageObject;

public class AppDisplayResult{
	 JFrame resultFrame;
	 ImageObject imageObject;
	 AppPanel resultPanel;
	public AppDisplayResult(BufferedImage resultImage,String name) {
		imageObject = ImageObject.getInstance();
		resultFrame = new JFrame(name);
		resultFrame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
			

			}

			@Override
			public void windowClosed(WindowEvent e) {
			

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		resultPanel = new AppPanel();
		resultPanel.setBufferedImage(resultImage);
		BufferedImage draw = new BufferedImage(resultImage.getWidth(),
				resultImage.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
		resultPanel.paintComponent(draw.createGraphics());
	
		resultPanel.setSize(resultImage.getWidth(),resultImage.getHeight());
		resultFrame.setSize(resultImage.getWidth(),resultImage.getHeight()+20);
		resultFrame.setVisible(true);
		
		resultFrame.getContentPane().add(resultPanel);

	}
	
	

}
