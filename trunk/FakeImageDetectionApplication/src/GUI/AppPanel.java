package GUI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class AppPanel extends JPanel {
	BufferedImage img;
	private String text;

	public AppPanel() {

	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		paintText(g);
		paintImage(g);
		repaint();

	}

	public void setText(String text) {

		this.text = text;

	}

	public void setBufferedImage(BufferedImage img) {

		this.img = img;

	}

	private void paintText(Graphics g) {
		if (this.text != null) {
			g.drawString(this.text, (this.getWidth() / 2)
					- (this.text.length() / 2), (this.getHeight() * 8 / 9));
		}

	}

	private void paintImage(Graphics g) {

		if (img == null) {

			return;
		}
		g.drawImage(img, (this.getWidth() / 2) - (img.getWidth() / 2),
				this.getHeight() / 2 - (img.getHeight() / 2), null);
	}

}
