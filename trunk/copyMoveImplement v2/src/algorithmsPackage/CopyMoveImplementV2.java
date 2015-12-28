package algorithmsPackage;

import hashFunction.MurmurHash3;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ImageObject.ImageObject;
import Interface.AlgorithmInterface;

import com.google.common.collect.HashMultimap;

public class CopyMoveImplementV2 implements AlgorithmInterface {
	private byte[][][] originalMatrix;
	private int blockSize = 2;
	private int imageWidth;
	private int imageHeight;
	private HashMultimap<String, Point> allPossibleHash;
	private ArrayList<HashMultimap<String, Point>> allResultRepeated;
	private HashSet<String> repeatKey;
	private ImageObject imageInstance;

	public CopyMoveImplementV2() {
		imageInstance = ImageObject.getInstance();
		allPossibleHash = HashMultimap.create();
		allResultRepeated = new ArrayList<HashMultimap<String, Point>>();
		repeatKey = new HashSet<>();
	}

	// called by getResultImage
	private byte[][][] makeImageArray(BufferedImage imgInput) {
		int d;
		byte[][][] imageByte = new byte[imageHeight][imageWidth][3];
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				d = imgInput.getRGB(j, i);
				imageByte[i][j][2] = (byte) (d & 255);
				imageByte[i][j][1] = (byte) (d >> 8 & 255);
				imageByte[i][j][0] = (byte) (d >> 16 & 255);
			}
		}
		return imageByte;
	}

	private String Digest(Byte[] msg) {
		int digest;
		// 0x753ec223 is random number
		digest = MurmurHash3.murmurhash3x8632(msg, 0x753ec223);
		return Integer.toHexString(digest);
	}

	// called by getResultImage&computeImageByte
	private void getHash(int i, int j, int currentBlockSize) {
		List<Byte> hex = new ArrayList<>();
		String tempHash;
		try {
			for (int l = 0; l < currentBlockSize; l++) {
				for (int k = 0; k < currentBlockSize; k++) {
					hex.add(originalMatrix[i + l][j + k][0]);
					hex.add(originalMatrix[i + l][j + k][1]);
					hex.add(originalMatrix[i + l][j + k][2]);
				}
			}
			tempHash = Integer.toString(currentBlockSize) + ":"
					+ Digest(hex.toArray(new Byte[hex.size()]));
			if (allPossibleHash.containsKey(tempHash)) {
				repeatKey.add(tempHash);
			}
			allPossibleHash.put(tempHash, new Point(i, j));
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	// called by getResultImage
	private void computeImageByte() {
		System.out.println("Start Compute Image Byte");
		HashMultimap<String, Point> tempResult;
		for (int i = 0;; i++) {
			tempResult = HashMultimap.create();
			repeatKey.clear();
			allPossibleHash.clear();
			// get all keys string from row i
			for (String stringResult : allResultRepeated.get(i).keys()) {
				
				// get point from key string
				for (Point point : allResultRepeated.get(i).get(stringResult)) {
					getHash(point.x, point.y, blockSize + i + 1);
				}

				// compare size of new subimage(incresed) and old subimage
//				if (repeatKey.size() == allResultRepeated.get(last)
//						.get(stringResult).size()) {
//					allResultRepeated.get(last).removeAll(stringResult);
//					System.out.println("Result duplicate");
//				}

				for (String string : repeatKey) {
					tempResult.putAll(string, allPossibleHash.get(string));
				}
			}
			
			if (tempResult.size() == 0) {
				break;
			}
			allResultRepeated.add(tempResult);
		}
		System.out.println("Finish Compute Image Byte");
	}

	// called by getResultImage
	private void initiateCompute() {
		System.out.println("Start Initiate Compute");
		HashMultimap<String, Point> tempResult = HashMultimap.create();
		for (int i = 0; i < imageHeight - blockSize; i++) {
			for (int j = 0; j < imageWidth - blockSize; j++) {
				getHash(i, j, blockSize);
			}
		}
		for (String string : repeatKey) {
			tempResult.putAll(string, allPossibleHash.get(string));
		}
		allResultRepeated.add(tempResult);
		System.out.println("Finish Initiate Compute");
	}

	private BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public String getName() {
		String name = "Copy-Move version2";
		return name;
	}

//	public BufferedImage getResultImage() {
//		return null;
//	}

	public BufferedImage getResultImage() {
		 if (imageInstance == null) {
		 return null;
		 }
		 BufferedImage img;
		BufferedImage resultImage;

		 img = imageInstance.getBufferedImage();
		imageHeight = img.getHeight();
		imageWidth = img.getWidth();
		resultImage = deepCopy(img);
		originalMatrix = makeImageArray(img);
		initiateCompute();
		computeImageByte();

		// convert resultRepeated to BufferImage
		HashMultimap<String, Point> tempResult = allResultRepeated
				.get(allResultRepeated.size()-1);
		for (String stringResult : tempResult.keys()) {
			for (Point point : tempResult.get(stringResult)) {
				int square = Integer.parseInt(stringResult.split(":")[0]);
				int color = -65536;
				for (int i = 0; i < square; i++) {
					for (int j = 0; j < square; j++) {
						resultImage.setRGB(point.y + j, point.x + i, color);
					}
				}
			}
		}
		return resultImage;
	}

	public boolean needEnvelope() {
		return false;
	}
}
