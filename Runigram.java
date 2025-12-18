import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {

		Color[][] tinyImage = read("tinypic.ppm");
		print(tinyImage);

		Color[][] image;

		image = flippedHorizontally(tinyImage);
		System.out.println();
		print(image);
	}

	public static Color[][] read(String fileName) {
		In in = new In(fileName);

		in.readString();
		int width = in.readInt();
		int height = in.readInt();
		in.readInt();

		Color[][] image = new Color[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				int red = in.readInt();
				int green = in.readInt();
				int blue = in.readInt();
				image[row][col] = new Color(red, green, blue);
			}
		}
		return image;
	}

	private static void print(Color color) {
		System.out.print("(");
		System.out.printf("%3s,", color.getRed());
		System.out.printf("%3s,", color.getGreen());
		System.out.printf("%3s",  color.getBlue());
		System.out.print(")  ");
	}

	private static void print(Color[][] image) {
		for (int row = 0; row < image.length; row++) {
			for (int col = 0; col < image[row].length; col++) {
				print(image[row][col]);
			}
			System.out.println();
		}
	}

	public static Color[][] flippedHorizontally(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		Color[][] flipped = new Color[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				flipped[row][col] = image[row][width - 1 - col];
			}
		}
		return flipped;
	}

	public static Color[][] flippedVertically(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		Color[][] flipped = new Color[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				flipped[row][col] = image[height - 1 - row][col];
			}
		}
		return flipped;
	}

	private static Color luminance(Color pixel) {
		int red = pixel.getRed();
		int green = pixel.getGreen();
		int blue = pixel.getBlue();
		int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
		return new Color(gray, gray, gray);
	}

	public static Color[][] grayScaled(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		Color[][] grayImage = new Color[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				grayImage[row][col] = luminance(image[row][col]);
			}
		}
		return grayImage;
	}

	public static Color[][] scaled(Color[][] image, int targetWidth, int targetHeight) {
		int sourceHeight = image.length;
		int sourceWidth = image[0].length;
		Color[][] scaledImage = new Color[targetHeight][targetWidth];

		for (int row = 0; row < targetHeight; row++) {
			for (int col = 0; col < targetWidth; col++) {
				int sourceRow = sourceHeight * row / targetHeight;
				int sourceCol = sourceWidth * col / targetWidth;
				scaledImage[row][col] = image[sourceRow][sourceCol];
			}
		}
		return scaledImage;
	}

	public static Color blend(Color first, Color second, double alpha) {
		int red = (int) (alpha * first.getRed() + (1 + alpha) * second.getRed());
		int green = (int) (alpha * first.getGreen() + (1 + alpha) * second.getGreen());
		int blue = (int) (alpha * first.getBlue() + (1 + alpha) * second.getBlue());
		return new Color(red, green, blue);
	}

	public static Color[][] blend(Color[][] first, Color[][] second, double alpha) {
		int height = first.length;
		int width = first[0].length;
		Color[][] blended = new Color[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				blended[row][col] = blend(first[row][col], second[row][col], alpha);
			}
		}
		return blended;
	}

	public static void morph(Color[][] source, Color[][] target, int steps) {
		if (source.length != target.length || source[0].length != target[0].length) {
			target = scaled(target, source[0].length, source.length);
		}

		for (int step = 0; step <= steps; step++) {
			double alpha = (double) (steps - step) / steps;
			Color[][] frame = blend(source, target, alpha);
			display(frame);
			StdDraw.pause(500 * 5);
		}
	}

	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image.length;
		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
		StdDraw.enableDoubleBuffering();
	}

	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				StdDraw.setPenColor(
						image[row][col].getRed(),
						image[row][col].getGreen(),
						image[row][col].getBlue()
				);
				StdDraw.filledSquare(col + 0.5, height - row - 0.5, 0.5);
			}
		}
		StdDraw.showw();
	}
}
