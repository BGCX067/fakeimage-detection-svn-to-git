/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package achromaticAberration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import achromaticAberration.BoxBlur;
import achromaticAberration.DifferenceOfGaussian;
import achromaticAberration.Extractor;
import achromaticAberration.ImageMath;

/**
 * 
 * @author Ruj
 */
public class FakeImageDetection {

	/**
	 * @param args
	 *            the command line arguments
	 */
	// public static void main(String[] args) {
	public int startBuildImage(String path, String distiny) {
		try {
			// String path="C:/test/test.png";
			File inputFile = new File(path);
			String fileName = inputFile.getName();
			String temp = fileName.replaceAll(".jpg", "");
			temp = temp.replace(".png", "");
			distiny = distiny + File.separator;
			fileName = temp;
			BufferedImage img = ImageIO.read(inputFile);
			String[] names = { "R_", "G_", "B_" };

			// Split image into 3 images based on each channel
			// BufferedImage[] rgb = Extractor.toRGBImages(img);
			float[][] imageFloatArray = Extractor.toRGBFloatArray(img);
			int total = imageFloatArray[0].length;
			float[][] edgeArray = new float[3][total];

			// // Create small black image
			// int size = 400;
			// float[] blackImage = new float[size];
			// float[] result = new float[size];
			//
			// Arrays.fill(blackImage, 200, 400, 1f);
			// // Arrays.fill(blackImage, 1f);
			//
			// //BoxBlur.blur(blackImage, result, 2, 2, 20, 20, null);
			// DifferenceOfGaussian.gussianDiff(blackImage, result, 20, 20,
			// null, null, null);
			// DifferenceOfGaussian.detectEdge(result, blackImage, 20, 20);
			//
			// result[0] = result[0] * 1.5f;

			// // Try black and white image
			// Arrays.fill(imageFloatArray[0], 0, imageFloatArray[0].length / 2,
			// 0f);
			// Arrays.fill(imageFloatArray[0], imageFloatArray[0].length / 2,
			// imageFloatArray[0].length, 1f);

			// Run Boxblur 4 times with
			float[] result = new float[imageFloatArray[0].length];
			float[] result2 = new float[imageFloatArray[0].length];

			float[] buffer1 = new float[imageFloatArray[0].length];
			float[] buffer2 = new float[imageFloatArray[0].length];
			float[] buffer3 = new float[imageFloatArray[0].length];

			int r = 2;
			BoxBlur.blur(imageFloatArray[0], result, r, r, img.getWidth(),
					img.getHeight(), buffer1);
			BoxBlur.blur(result, result2, r, r, img.getWidth(),
					img.getHeight(), buffer1);
			BoxBlur.blur(result2, result, r, r, img.getWidth(),
					img.getHeight(), buffer1);
			BoxBlur.blur(result, result2, r, r, img.getWidth(),
					img.getHeight(), buffer1);
			result = result2;

			BufferedImage resultImg = new BufferedImage(img.getWidth(),
					img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
			resultImg.getRaster().setPixels(0, 0, img.getWidth(),
					img.getHeight(), result);
			ImageIO.write(resultImg, "PNG", new File(distiny + "Blur_"
					+ fileName + ".png"));

			BufferedImage rgbImg = new BufferedImage(img.getWidth(),
					img.getHeight(), BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < 3; i++) {
				DifferenceOfGaussian.gussianDiff(imageFloatArray[i],
						edgeArray[i], img.getWidth(), img.getHeight(), buffer1,
						buffer2, buffer3);
				rgbImg.setRGB(0, 0, img.getWidth(), img.getHeight(),
						ImageMath.nzpToRGB(edgeArray[i]), 0, img.getWidth());
				ImageIO.write(rgbImg, "PNG", new File(distiny + "DOG_"
						+ names[i] + fileName + ".png"));

				// Create edge image
				DifferenceOfGaussian.detectEdge(edgeArray[i], buffer1,
						img.getWidth(), img.getHeight());
				ImageMath.normalizePos(buffer1, buffer1.length);
				resultImg.getRaster().setPixels(0, 0, img.getWidth(),
						img.getHeight(), buffer1);
				ImageIO.write(resultImg, "PNG", new File(distiny + "DOG_EDGE_"
						+ names[i] + fileName + ".png"));
			}

			// Diff red from green
			for (int i = 0; i < total; i++) {
				edgeArray[0][i] -= edgeArray[1][i];
				edgeArray[2][i] -= edgeArray[1][i];
			}

			resultImg.getRaster().setPixels(0, 0, img.getWidth(),
					img.getHeight(), edgeArray[0]);
			ImageIO.write(resultImg, "PNG", new File(distiny + "RG_DOG_DIF_"
					+ fileName + ".png"));
			resultImg.getRaster().setPixels(0, 0, img.getWidth(),
					img.getHeight(), edgeArray[2]);
			ImageIO.write(resultImg, "PNG", new File(distiny + "BG_DOG_DIF_"
					+ fileName + ".png"));

			// // Create test image
			// float[] testImg = new float[900];
			// float[] testImgResult = new float[900];
			// buffer1 = new float[900];
			// buffer2 = new float[900];
			// Arrays.fill(testImg, 900 / 2, 900 - 1, 1f);
			//
			// DifferenceOfGaussian.detect(testImg, testImgResult, 30, 30,
			// buffer1, buffer2);

			// Find the edge of each image
			// DoGFilter dgf = new DoGFilter();
			// dgf.setRadius1(4);
			// dgf.setRadius2(4);

			// BufferedImage[] edgeImages = new BufferedImage[3];
			// // Extract edge of each color channel
			// for (int i = 0; i < edgeImages.length; i++) {
			// edgeImages[i] = dgf.filter(rgb[i], null);
			// }

			// for (int i = 0; i < names.length; i++) {
			// ImageIO.write(rgb[0], "PNG", new File(names[i] + fileName +
			// ".png"));
			// ImageIO.write(edgeImages[0], "PNG", new File("Edge_" + names[i] +
			// fileName + ".png"));
			// }

			// // Find difference between Green - Red and Green -Blue
			// Graphics2D g2d = edgeImages[1].createGraphics();
			// g2d.setComposite( new SubtractComposite( 1.0f ) );
			// g2d.drawImage( edgeImages[0], 0, 0, null );
			// g2d.dispose();
			//
			// ImageIO.write(edgeImages[1], "PNG", new File("greenedge.png"));
			// ImageIO.write(edgeImages[0], "PNG", new File("rededge.png"));
			return 0;
		} catch (IOException ex) {
			Logger.getLogger(FakeImageDetection.class.getName()).log(
					Level.SEVERE, null, ex);
			return 1;
		} catch (Exception ex) {
			Logger.getLogger(FakeImageDetection.class.getName()).log(
					Level.SEVERE, null, ex);
			return 1;
		}
	}
}
