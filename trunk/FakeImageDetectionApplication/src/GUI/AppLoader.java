package GUI;

import ImageObject.ImageObject;


public class AppLoader {

	public AppLoader() {

		AppFrame frame = new AppFrame();
		ImageObject imageObj = ImageObject.getInstance();
		imageObj.setFrame(frame);
		frame.setSize(1024, 768);
		frame.setVisible(true);
		
	}
	
	
	public static void main(String[] args) {
		new AppLoader();

	}

}
