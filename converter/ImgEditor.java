package converter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.json.JSONObject;

public class ImgEditor {

	public static void main(String[] args) throws Exception {
		// createTilesetFrom("./ImagesForTransformation.json");
		Color remCol = new Color(64, 128, 0);
		// Color remCol = new Color(255, 255, 255);
		//Color remCol = new Color(0,0,0);
		File folder = new File(
				"C:\\Users\\Ionicle\\Desktop\\GameResources\\Girl2\\");
		for (File f : folder.listFiles()) {
			if (f.getName().contains(".bmp")) {
				convertTransparentImage(
						f.getAbsolutePath(),
						remCol,
						folder.getAbsolutePath() + "\\"
								+ f.getName().split(".bmp")[0] + ".png");
			}
		}
		/*
		convertTransparentImage(
				"C:\\Users\\Ionicle\\Desktop\\GameResources\\Girl2\\MissileExplode.bmp",
				remCol,
				"C:\\Users\\Ionicle\\Desktop\\GameResources\\Girl2\\MissileExplode.png");
				*/
	}

	public static void createTilesetFrom(String json) throws IOException {
		String jStr = fileToString(json);
		JSONObject obj = new JSONObject(jStr);
		@SuppressWarnings("unchecked")
		Iterator<String> keys = obj.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			JSONObject curr = obj.getJSONObject(key);
			int size = curr.getInt("size");
			int rows = curr.getInt("rows");
			int num = curr.getInt("num");
			String[] imagePaths = new String[num];
			JSONObject tiles = curr.getJSONObject("tiles");
			for (int i = 0; i < num; i++) {
				imagePaths[i] = tiles.getJSONObject("" + i).getString("image");
			}
			joinImagesIntoTileset(key + ".png", imagePaths, size, rows);
		}
	}

	private static Image loadImage(String ref) {
		Image tempImage = null;
		BufferedImage sourceImage = null;

		try {
			URL url = ImgEditor.class.getResource(ref);

			if (url == null) {
				System.out.println("Can't find ref: " + ref);
			}
			sourceImage = ImageIO.read(url);

		} catch (IOException e) {
			System.out.println("Failed to load: " + ref);
		}

		// creates an accelerated image
		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		tempImage = gc.createCompatibleImage(sourceImage.getWidth(),
				sourceImage.getHeight(), Transparency.TRANSLUCENT);

		// draw source image into the accelerated image
		tempImage.getGraphics().drawImage(sourceImage, 0, 0, null);

		return tempImage;

	}

	public static void joinImagesIntoTileset(String destination,
			String[] sourceImages, int size, int rows) throws IOException {
		int len = sourceImages.length;
		int cols = len / rows;
		System.out.println("Map: " + destination + ", Rows: " + rows
				+ ", Columns: " + cols + "\n");

		Image[] images = new Image[len];
		for (int x = 0; x < images.length; x++) {
			images[x] = loadImage("/images/" + sourceImages[x]);
		}

		int width = cols * size, height = rows * size;
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) bi.getGraphics();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int x = j * size;
				int y = i * size;
				g2d.drawImage(images[i * cols + j], x, y, null);
			}
		}
		g2d.dispose();
		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(bi)));
		ImageIO.write(bi, "png", new File(destination));
	}

	public static String fileToString(String filepath) throws IOException {
		FileReader f = new FileReader(filepath);
		BufferedReader b = new BufferedReader(f);
		String full = "", line = b.readLine();
		while (line != null) {
			full += line + "\n";
			line = b.readLine();
		}
		b.close();
		return full;
	}

	public static void convertTransparentImage(String imgLocation,
			Color removeColor, String destination) throws Exception {
		BufferedImage img = ImageIO.read(new File(imgLocation));
		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(img)));
		img = transform(img, removeColor);
		//JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(img)));
		ImageIO.write(img, "png", new File(destination));
	}

	private static BufferedImage transform(BufferedImage img, Color remCol) {
		int alpha = 0, red, green, blue, imageAlpha;
		int pixel;

		int width = img.getWidth(), height = img.getHeight();

		int[] pixels = new int[width * height];

		BufferedImage nImg = ImageSFXs.getInstance().makeTransImage(img);

		nImg.getRGB(0, 0, width, height, pixels, 0, width);
		System.out.println("Length of pixel array = " + pixels.length);

		for (int i = 0; i < pixels.length; i++) {
			pixel = pixels[i];

			imageAlpha = (pixel & 0xFF000000) >>> 24;

			red = (pixel & 0x00FF0000) >>> 16;
			green = (pixel & 0x0000FF00) >>> 8;
			blue = (pixel & 0x000000FF) >>> 0;

			if (red == remCol.getRed() && blue == remCol.getBlue()
					&& green == remCol.getGreen())
				pixels[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
			else
				pixels[i] = (imageAlpha << 24) | (red << 16) | (green << 8)
						| blue;
		}

		nImg.setRGB(0, 0, width, height, pixels, 0, width);
		// g2d.drawImage(im, x, y, width, height, null);

		return nImg;
	}

}
