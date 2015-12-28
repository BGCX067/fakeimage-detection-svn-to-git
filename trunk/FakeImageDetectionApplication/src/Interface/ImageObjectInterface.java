package Interface;

import java.awt.image.BufferedImage;
import java.io.File;

public interface ImageObjectInterface {

	public void setName(String imgName);

	public String getName();

	public void setFilePath(File imgPath);

	public File getFilePath();

	public void setBufferedImage(BufferedImage imgData);

	public BufferedImage getBufferedImage();

}
