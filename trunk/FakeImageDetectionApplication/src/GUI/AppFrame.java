package GUI;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

@SuppressWarnings("serial")
public class AppFrame extends JFrame {

	private AppPanel panel;
	private JMenuBar menuBar;
	Logger logger = Logger.getLogger("MyLog");
	FileHandler fh;

	public AppFrame() {

		initComponents();

		setTitle("Fake Image Detection Application");

		addWindowListener(new WindowListener() {

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
				System.exit(1);

			}

			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(1);

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initComponents() {
		panel = new AppPanel();
		menuBar = new JMenuBar();
		panel.setSize(this.getWidth(), this.getHeight());
		getContentPane().add(panel);

		AppMenu menu = new AppMenu("Menu", this);
		 File[] classList = loadFileList();
		AppAlgorithmMenu algorithmMenu = new AppAlgorithmMenu("Algorithm",panel, classList);
		menuBar.add(menu);
		menuBar.add(algorithmMenu);
		setJMenuBar(menuBar);

	}

	private File[] loadFileList() {

		String folderPath = System.getProperty("user.dir");
		try {
			fh = new FileHandler(
					new File(new File(folderPath), "MyLogFile.log").getPath());
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("My path :" + folderPath);

		File folder = new File(folderPath,"plugin");
		File[] listOfFiles = folder.listFiles();
	
		return listOfFiles;
	}

	public JMenuBar getJMenuBar() {
		return menuBar;
	}

	public AppPanel getpanel() {
		return this.panel;
	}

}
