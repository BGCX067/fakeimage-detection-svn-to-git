package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ImageObject.ImageObject;

public class AppMenu extends JMenu implements ActionListener {

	private JMenuItem loadImageMenuItem;
	private AppPanel panel;
	private JMenuItem exitMenuItem;
	private AppFrame frame;

	public AppMenu(String name, AppFrame frame) {
		super(name);
		this.frame = frame;
		this.panel = frame.getpanel();
		
		loadImageMenuItem = new JMenuItem("Load Image");
		loadImageMenuItem.addActionListener(this);
		add(loadImageMenuItem);

		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(this);
		add(exitMenuItem);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadImageMenuItem) {
			panel.setText("");
			ImageObject imageObj = ImageObject.getInstance();
			imageObj.loadImage();
			BufferedImage image = imageObj.getBufferedImage();
			panel.setBufferedImage(image);
			BufferedImage draw = new BufferedImage(image.getWidth(),
					image.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
			panel.paintComponent(draw.createGraphics());
		}

		if (e.getSource() == exitMenuItem) {

			System.exit(1);

		}

	}

}
