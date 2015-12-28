package algorithmsPackage;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import ImageObject.ImageObject;
import Interface.AlgorithmInterface;

public class copyMoveImplement implements AlgorithmInterface {

	private int blockSize = 16;
	private double[][] arrayT;
	double[][] quantizationMatrix;
	private int[][] originalMatrix;
	private int[][] resultMatrix;
	private int[] color = { 0x800000, 0x0000ff, 0x008000, 0xffff00, 0xffffff,
			0xccffff, 0xff0000, 0x00ff00 };
	private ImageObject imageInstance;
	static public int[][] commondQuantizationMatrix = {
			{ 16, 11, 10, 16, 24, 40, 51, 61 },
			{ 12, 12, 14, 19, 26, 58, 60, 55 },
			{ 14, 13, 16, 24, 40, 57, 69, 56 },
			{ 14, 17, 22, 29, 51, 87, 80, 62 },
			{ 18, 22, 37, 56, 68, 109, 103, 77 },
			{ 24, 35, 55, 64, 81, 104, 113, 92 },
			{ 49, 64, 78, 87, 103, 121, 120, 101 },
			{ 72, 92, 95, 98, 112, 100, 103, 99 } };

	public copyMoveImplement() {
		imageInstance = ImageObject.getInstance();
		arrayT = initialTMatrix();
		quantizationMatrix = initialQuantizationMatrix();

	}

	private void initialResultMatrix() {
		BufferedImage inputImage = imageInstance.getBufferedImage();
		resultMatrix = new int[inputImage.getWidth()][inputImage.getHeight()];
		for (int i = 0; i < resultMatrix.length; i++) {
			for (int j = 0; j < resultMatrix[0].length; j++) {
				resultMatrix[i][j] = 0;
			}
		}
	}

	private double[][] initialTMatrix() {

		double[][] t = new double[blockSize][blockSize];
		double n = blockSize;

		for (int i = 0; i < blockSize; i++) {
			for (int j = 0; j < blockSize; j++) {
				if (i == 0) {
					t[i][j] = 1 / Math.sqrt(n);
				} else {
					t[i][j] = Math.sqrt(2 / n)
							* Math.cos((((2 * j) + 1) * i * Math.PI) / (2 * n));

				}
			}
		}

		return t;

	}

	private double[][] initialQuantizationMatrix() {

		int[][] commondQuantizationMatrix = copyMoveImplement.commondQuantizationMatrix;
		double[][] quantizationMatrix = new double[blockSize][blockSize];

		for (int i = 0; i < blockSize; i++) {
			for (int j = 0; j < blockSize; j++) {

				if (i < 8) {
					if (i == 0 && j == 0) {
						quantizationMatrix[i][j] = 2.0 * commondQuantizationMatrix[i][j];
					} else {
						if (j < 8) {
							quantizationMatrix[i][j] = 2.5 * commondQuantizationMatrix[j][i];
						} else {
							quantizationMatrix[i][j] = 2.5 * commondQuantizationMatrix[7][0];
						}
					}
				} else {
					if (j < 8) {
						quantizationMatrix[i][j] = 2.5 * commondQuantizationMatrix[0][7];
					} else {
						quantizationMatrix[i][j] = 2.5 * commondQuantizationMatrix[7][7];
					}
				}
			}
		}

		return quantizationMatrix;

	}

	private int[][] makeGrayArray(BufferedImage imgInput) {

		int[][] grayArray = new int[imgInput.getWidth()][imgInput.getHeight()];
		for (int x = 0; x < imgInput.getWidth(); ++x) {
			for (int y = 0; y < imgInput.getHeight(); ++y) {
				int rgb = imgInput.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);

				int gray = (int) ((r * 0.299) + (g * 0.587) + (b * 0.114));
				grayArray[x][y] = gray - 128;
			}
		}

		return grayArray;

	}

	private int[][] makeArray(BufferedImage imgInput) {

		int[][] grayArray = new int[imgInput.getWidth()][imgInput.getHeight()];
		for (int x = 0; x < imgInput.getWidth(); ++x) {
			for (int y = 0; y < imgInput.getHeight(); ++y) {
				grayArray[x][y] = imgInput.getRGB(x, y);
			}
		}
		return grayArray;

	}

	public int[][] createArrayQuantizationDCTMatrix(BufferedImage jpgImage) {

		int[][] grayImage = makeGrayArray(jpgImage);
		int[][] a = new int[(grayImage.length - blockSize + 1)
				* (grayImage[0].length - blockSize + 1)][blockSize * blockSize];
		double[][] temp = new double[blockSize][blockSize];

		int count = 0;

		for (int i = 0; i < (grayImage.length - blockSize + 1); i++) {
			for (int j = 0; j < (grayImage[0].length - blockSize + 1); j++) {

				for (int k = 0; k < blockSize; k++) {
					for (int l = 0; l < blockSize; l++) {
						temp[k][l] = grayImage[i + k][j + l];
					}
				}
				
				double[] dctMatrix = dctMatrix2(temp);
				a[count] = quantizationMatrix(dctMatrix);
				
				
				count++;
			}
		}

		return a;

	}

	private int[] quantizationMatrix(double[] dctMatrix) {

		int l = (this.quantizationMatrix.length * this.quantizationMatrix[0].length);
		int[] quantizationMatrix = new int[l];
		for (int i = 0; i < l; i++) {
			quantizationMatrix[i] = (int) Math.round(dctMatrix[i]
					/ this.quantizationMatrix[i / blockSize][i % blockSize]);
		}

		return quantizationMatrix;
	}

	private int[][] sortingArray(int[][] matrix) {

		int[] sort = new int[matrix.length];
		int[] original = new int[matrix.length];
		int[][] index = new int[2][matrix.length];
		int sum = 0;
		for (int i = 0; i < matrix.length; i++) {
			sum = 0;
			for (int j = 0; j < matrix[0].length; j++) {
				sum += matrix[i][j];
			}

			sort[i] = sum;
			original[i] = sum;
		}

		Arrays.sort(sort);
		for (int i = 0; i < original.length; i++) {
			index[1][i] = sort[i];
			index[0][i] = search(original, sort[i]);

		}

		return index;
	}

	private int search(int[] numbers, int key) {
		for (int index = 0; index < numbers.length; index++) {
			if (numbers[index] == key) {
				numbers[index] = Integer.MAX_VALUE;
				return index;
			}
		}

		return -1;
	}

	private double[] dctMatrix(double[][] f) {
		int N = blockSize;
		double[] c = new double[N];

		for (int i = 1; i < N; i++) {
			c[i] = 1;
		}
		c[0] = 1 / Math.sqrt(2.0);
		double[] dctArray = new double[N * N];
		int k = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				double sum = 0.0;
				for (int x = 0; x < N; x++) {
					for (int y = 0; y < N; y++) {
						sum += ((c[i] * c[j]) / Math.sqrt(2 * N))
								* Math.cos(((2 * x + 1) / (2.0 * N)) * i
										* Math.PI)
								* Math.cos(((2 * y + 1) / (2.0 * N)) * j
										* Math.PI) * f[x][y];
					}
				}
				dctArray[k] = sum;
				k++;
			}
		}
		return dctArray;
	}

	private double[] dctMatrix2(double[][] f) {
		int n = blockSize;
		double[] dctArray = new double[n * n];
		double[][] tempMatrix = new double[n][n];

		tempMatrix = multiplyMatrix(arrayT, f);
		dctArray = multiplyMatrixTranspose(tempMatrix, arrayT);

		return dctArray;
	}

	private double[][] multiplyMatrix(double[][] a, double[][] b) {
		double temp = 0;
		double[][] tempMatrix = new double[a.length][a[0].length];
		int n = a.length;

		for (int j = 0; j < n; j++) {
			for (int k = 0; k < n; k++) {
				temp = 0;
				for (int m = 0; m < n; m++) {
					temp = temp + a[j][m] * b[m][k];
				}
				tempMatrix[j][k] = temp;
			}
		}
		return tempMatrix;
	}

	private double[] multiplyMatrixTranspose(double[][] a, double[][] b) {
		double temp = 0;
		int count = 0;
		double[] tempMatrix = new double[a.length * a[0].length];
		int n = a.length;
		for (int j = 0; j < n; j++) {
			for (int k = 0; k < n; k++) {
				temp = 0;
				for (int m = 0; m < n; m++) {
					temp = temp + a[j][m] * b[k][m];
				}
				tempMatrix[count] = temp;

				count++;
			}
		}

		return tempMatrix;
	}

	private void compareArrays(int[][] array, int[][] index) {
		
		for (int i = 0; i < index[1].length - 1; i++) {

			for (int j = i + 1; j < index[1].length; j++) {
				if (index[1][i] != index[1][j]) {
					break;
				}
				if (Arrays.equals(array[index[0][i]], array[index[0][j]])) {
					Point px = indexToPoint(index[0][i]);
					Point py = indexToPoint(index[0][j]);
				
					// Point.distance(px.x, px.y,py.x,py.y);
					for (int k = 0; k < blockSize; k++) {
						for (int k2 = 0; k2 < blockSize; k2++) {
							resultMatrix[px.x + k][px.y + k2] = originalMatrix[px.x][px.y];
							resultMatrix[py.x + k][py.y + k2] = originalMatrix[py.x][py.y];
						}
					}
					break;
				}
			}

		}
	}

	private Point indexToPoint(int index) {
		BufferedImage img = imageInstance.getBufferedImage();
		int width = img.getWidth();
		Point point = new Point(index % (width - blockSize + 1), index
				/ (width - blockSize + 1));
		return point;

	}

	@Override
	public String getName() {
		String name = "Copy-Move";
		return name;
	}

	@Override
	public BufferedImage getResultImage() {

		if (imageInstance != null) {
			initialResultMatrix();
			long startTime = System.currentTimeMillis();

			BufferedImage img = imageInstance.getBufferedImage();
			originalMatrix = makeArray(img);

			BufferedImage resultImage = new BufferedImage(img.getWidth(),
					img.getHeight(), BufferedImage.TYPE_INT_RGB);
			int[][] matrix = createArrayQuantizationDCTMatrix(img);
			int[][] a = sortingArray(matrix);
			compareArrays(matrix, a);

			System.out.println("Finish  "
					+ (System.currentTimeMillis() - startTime));

			for (int i = 0; i < resultMatrix.length; i++) {
				for (int j = 0; j < resultMatrix[0].length; j++) {
					resultImage.setRGB(i, j, resultMatrix[i][j]);
				}
			}

			return resultImage;

		}
		return null;
	}

	@Override
	public boolean needEnvelope() {
		return false;
	}

}
