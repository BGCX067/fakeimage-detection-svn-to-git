package GUI;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ImageObject.ImageObject;
import Interface.AlgorithmInterface;

public class AppDrawRectanglePanel extends JPanel implements MouseListener,
		MouseMotionListener {
	BufferedImage img;
	private Point pressPoint;
	private Point mousePoint;
	private JFrame frame;
	private AlgorithmInterface algorithm;

	public AppDrawRectanglePanel(BufferedImage image, JFrame frame,
			AlgorithmInterface algorithm) {
		addMouseListener(this);
		addMouseMotionListener(this);
		this.frame = frame;
		this.algorithm = algorithm;
		BufferedImage draw = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
		paintComponent(draw.createGraphics());
		setBufferedImage(image);
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		paintImage(g);
		if (pressPoint != null && mousePoint != null) {

			g.drawLine(pressPoint.x, pressPoint.y, pressPoint.x, mousePoint.y);
			g.drawLine(pressPoint.x, pressPoint.y, mousePoint.x, pressPoint.y);
			g.drawLine(pressPoint.x, mousePoint.y, mousePoint.x, mousePoint.y);
			g.drawLine(mousePoint.x, pressPoint.y, mousePoint.x, mousePoint.y);

		}
		repaint();

	}

	public void setBufferedImage(BufferedImage img) {

		this.img = img;

	}

	private void paintImage(Graphics g) {

		if (img == null) {

			return;
		}
		g.drawImage(img, 0, 0, null);
	}

	public Point[] getEnvelopePoint() {
		Point[] envelope = new Point[4];
		if (pressPoint == null && mousePoint == null) {
			return null;
		}

		if (pressPoint.x < mousePoint.x) {

			if (pressPoint.y < mousePoint.y) {
				envelope[0] = pressPoint;
				envelope[1] = new Point(mousePoint.x, pressPoint.y);
				envelope[2] = new Point(pressPoint.x, mousePoint.y);
				envelope[3] = mousePoint;
			} else {
				envelope[2] = pressPoint;
				envelope[0] = new Point(pressPoint.x, mousePoint.y);
				envelope[3] = new Point(mousePoint.x, pressPoint.y);
				envelope[1] = mousePoint;
			}

		} else {
			if (pressPoint.y < mousePoint.y) {
				envelope[1] = pressPoint;
				envelope[3] = new Point(pressPoint.x, mousePoint.y);
				envelope[0] = new Point(mousePoint.x, pressPoint.y);
				envelope[2] = mousePoint;
			} else {
				envelope[3] = pressPoint;
				envelope[1] = new Point(pressPoint.x, mousePoint.y);
				envelope[2] = new Point(mousePoint.x, pressPoint.y);
				envelope[0] = mousePoint;
			}
		}

		return envelope;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressPoint = e.getPoint();
		mousePoint = null;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (getEnvelopePoint() != null) {
			ImageObject instance = ImageObject.getInstance();
			instance.setMousePosition(getEnvelopePoint());
			this.frame.dispose();
			new AppDisplayResult(algorithm.getResultImage(),
					algorithm.getName());
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

		mousePoint = e.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
