package ImageObject;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import GUI.AppFrame;
import Interface.ImageObjectInterface;

public class ImageObject implements ImageObjectInterface {

	private String imgName;
	private File imgPath;
	private BufferedImage imgData;
	private Point[] envelopePoint = new Point[4];
	private AppFrame frame;
	public static ImageObject instance = null;

	public static ImageObject getInstance() {
		if (instance == null) {
			instance = new ImageObject();
		}

		return instance;
	}

	private ImageObject() {

	}

	public void loadImage() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selectFile = fileChooser.getSelectedFile();
			if (selectFile != null) {
				setFilePath(selectFile);
			}
		}

		File filePath = getFilePath();
		if (filePath != null) {
			BufferedImage image = loadImage(filePath);
			setBufferedImage(image);
			
		}

	}

	@Override
	public void setName(String imgName) {

		this.imgName = imgName;

	}

	@Override
	public String getName() {

		if (imgName == null) {
			System.err.println(" getName() : imgName was null.");
		}

		return imgName;
	}

	@Override
	public void setFilePath(File imgPath) {

		this.imgPath = imgPath;

	}

	@Override
	public File getFilePath() {

		if (imgPath == null) {
			System.err.println("getFilePath() : imgPath was null.");
		}

		return imgPath;
	}

	@Override
	public void setBufferedImage(BufferedImage imgData) {

		this.imgData = imgData;

	}

	@Override
	public BufferedImage getBufferedImage() {

		if (imgPath == null) {
			System.err.println("getBufferedImage() : imgPath was null");
			return null;
		}

		return imgData;

	}

	public BufferedImage loadImage(File inputFile) {
		if (inputFile != null) {
			String fileName = inputFile.getName();
			setName(fileName);
			try {
				BufferedImage img = ImageIO.read(inputFile);

				return img;

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return null;
	}

	public Point[] getMouseEnvelope() {

		return this.envelopePoint;

	}

	
	public void setMousePosition(Point[] envelopePoint) {
		this.envelopePoint = envelopePoint;

	}

	public void setFrame(AppFrame frame) {
		this.frame = frame;
	}
	



}
