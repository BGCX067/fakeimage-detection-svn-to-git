package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ImageObject.ImageObject;
import Interface.AlgorithmInterface;
import PlugIn.AlgorithmPlugInLoader;

public class AppAlgorithmMenu extends JMenu implements ActionListener {

	private AlgorithmPlugInLoader algorithmPlugin;
	private JMenuItem[] algorithmMenuItem;
	private AppPanel panel;
	private ImageObject imageObj;

	public AppAlgorithmMenu(String name, AppPanel panel,File[] classList) {
		super(name);
		imageObj = ImageObject.getInstance();
		this.panel = panel;
		algorithmPlugin = new AlgorithmPlugInLoader(classList);
		algorithmMenuItem = algorithmPlugin.getMenuItem();
		for (int i = 0; i < algorithmMenuItem.length; i++) {
			algorithmMenuItem[i].addActionListener(this);
			add(algorithmMenuItem[i]);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BufferedImage image = imageObj.getBufferedImage();
		if (image == null) {
			this.panel.setText("Please load image");
			return;
		}
		this.panel.setText("");
		for (int i = 0; i < algorithmMenuItem.length; i++) {

			if (e.getSource() == algorithmMenuItem[i]) {
				AlgorithmInterface algorithm = algorithmPlugin.getAlgorithm(i);

				if (algorithm.needEnvelope()) {
				
					JFrame frame = new JFrame("draw rectangle");
					frame.setSize(image.getWidth(), image.getHeight() + 20);
					frame.setVisible(true);
					AppDrawRectanglePanel drawPanel = new AppDrawRectanglePanel(
							image, frame, algorithm);
					frame.getContentPane().add(drawPanel);
				} else {
					new AppDisplayResult(algorithm.getResultImage(), algorithm.getName()) ;
				}
			}

		}

	}
}
