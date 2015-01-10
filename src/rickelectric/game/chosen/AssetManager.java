package rickelectric.game.chosen;

import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class AssetManager {
	private static AssetManager assets;

	// Remember to store images in a data structure
	private HashMap<String, Image> tilesets;
	private HashMap<String, Image> loadedImages;

	private Font font;

	private AssetManager() {
		loadAssets();
	}

	public synchronized static AssetManager getInstance() {
		if (assets == null)
			assets = new AssetManager();
		return assets;
	}

	private void loadAssets() {
		tilesets = new HashMap<String, Image>();
		tilesets.put("walls.png", loadImage("images/walls.png"));
		tilesets.put("slopes.png", loadImage("images/slopes.png"));
		tilesets.put("other.png", loadImage("images/other.png"));
		tilesets.put("totemWalkways.png", loadImage("images/totemWalkways.png"));
		tilesets.put("Water1.png", loadImage("images/Water1.png"));
		tilesets.put("Water2.png", loadImage("images/Water2.png"));
		tilesets.put("WaterInWall.png", loadImage("images/WaterInWall.png"));
		tilesets.put("level2/WallCornersAlien.png",
				loadImage("images/level2/WallCornersAlien.png"));
		tilesets.put("level2/WallsAlien.png",
				loadImage("images/level2/WallsAlien.png"));

		loadedImages = new HashMap<String, Image>();

		getFont();
	}

	/**
	 * Retrieves an image from disk
	 * 
	 * @param ref
	 * @return
	 */
	private Image loadImage(String ref) {
		Image tempImage = null;
		BufferedImage sourceImage = null;

		try {
			URL url = this.getClass().getResource(ref);

			if (url == null) {
				System.out.println("Can't find ref: " + ref);
			}
			sourceImage = ImageIO.read(url);

		} catch (IOException e) {
			System.out.println("Failed to load: " + ref);
		}

		try {
			// creates an accelerated image
			GraphicsConfiguration gc = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice()
					.getDefaultConfiguration();
			tempImage = gc.createCompatibleImage(sourceImage.getWidth(),
					sourceImage.getHeight(), Transparency.TRANSLUCENT);

			// draw source image into the accelerated image
			tempImage.getGraphics().drawImage(sourceImage, 0, 0, null);
		} catch (Exception e) {
			return sourceImage;
		}

		return tempImage;

	}

	// Remember to have a method for accessing our
	// data structure that we are using to store
	// assets in
	public Image getTileset(String tilesetName) {
		return tilesets.get(tilesetName);
	}

	public Image getImage(String imageExtName) {
		Image img = loadedImages.get(imageExtName);
		if (img == null) {
			img = loadImage("images/" + imageExtName);
			loadedImages.put(imageExtName, img);
		}
		return img;
	}

	public Font getFont() {
		if (font == null) {
			try {
				URL fontUrl = getClass().getResource("fonts/font1.ttf");
				font = Font
						.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
			} catch (Exception e) {
				e.printStackTrace();
				font = new Font(Font.DIALOG, Font.BOLD, 40);
			}
		}
		return font;
	}

	public String readMapFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(file)));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		reader.close();
		return stringBuilder.toString();
	}

}
