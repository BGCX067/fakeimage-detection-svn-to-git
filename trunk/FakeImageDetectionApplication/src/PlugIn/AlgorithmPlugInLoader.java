package PlugIn;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JMenuItem;

import Interface.AlgorithmInterface;

public class AlgorithmPlugInLoader {

	Vector<AlgorithmInterface> algorithmVector = new Vector<AlgorithmInterface>();
	JMenuItem[] algorithmMenuItem;
	Logger logger = Logger.getLogger("MyLog");
	FileHandler fh;

	public AlgorithmPlugInLoader(File[] className) {
		String folderPath = System.getProperty("user.dir");
		try {
			fh = new FileHandler(new File(new File(folderPath),
					"pluginLogFile.log").getPath());
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
		loadClass(className);
		algorithmMenuItem = new JMenuItem[algorithmVector.size()];

	}

	public JMenuItem[] getMenuItem() {

		for (int i = 0; i < algorithmMenuItem.length; i++) {
			algorithmMenuItem[i] = new JMenuItem(algorithmVector.get(i)
					.getName());

		}
		return algorithmMenuItem;

	}

	public AlgorithmInterface getAlgorithm(int number) {

		return algorithmVector.get(number);

	}

	public void loadClass(File[] algorithmName) {

		for (int i = 0; i < algorithmName.length; i++) {

			try {
				URL classURL = new File(algorithmName[i].getPath()).toURI().toURL() ;
				ClassLoader loader = URLClassLoader.newInstance(
					    new URL[] { classURL },
					    getClass().getClassLoader()
					);
				String className = new String("algorithmsPackage."+algorithmName[i].getName());
				className = className.replaceAll(".jar","");
				Class<?> calledClass = Class.forName(className,true,loader);
				try {
					addAlgorithm((AlgorithmInterface) calledClass.newInstance());
					logger.info("Loaded Class");
				} catch (InstantiationException | IllegalAccessException e) {
					logger.info("1 " + e.getStackTrace());
				}
			} catch (ClassNotFoundException e) {
				logger.info("Class forname ");
			} catch (MalformedURLException e1) {
				logger.info("URL error ");
			}
		}

	}

	public void addAlgorithm(AlgorithmInterface algorithm) {

		algorithmVector.addElement(algorithm);

	}

}
