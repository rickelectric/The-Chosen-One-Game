package rickelectric.game.chosen.entities;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;

import rickelectric.game.chosen.AssetManager;

public class Sprite extends Entity {
	protected Image image;
	protected float opacity;

	/**
	 * Constructor Method
	 * 
	 * @param name
	 * @param imageName
	 * @param x
	 * @param y
	 */
	public Sprite(String imageName, float x, float y) {
		super(x, y);
		this.image = AssetManager.getInstance().getImage(imageName + ".png");
		this.width = image.getWidth(null);
		this.height = image.getHeight(null);
		this.opacity = 1.0f;

	}

	public Sprite(String imageName, float x, float y, float opacity) {
		super(x, y);
		this.image = AssetManager.getInstance().getImage(imageName + ".png");
		this.width = image.getWidth(null);
		this.height = image.getHeight(null);
		this.opacity = opacity;
	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g2d) {
		// set alphacomposite to our opacity
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				opacity));
		g2d.drawImage(this.image, (int) x, (int) y, null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));
	}

	public void draw(Graphics2D g2d, float x, float y) {
		g2d.drawImage(this.image, (int) x, (int) y, null);
	}
	
}
