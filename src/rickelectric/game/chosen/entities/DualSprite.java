package rickelectric.game.chosen.entities;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;

import rickelectric.game.chosen.AssetManager;

public class DualSprite extends Entity {
	protected Image image1, image2;
	protected float opacity;
	
	protected int activeImage;

	/**
	 * Constructor Method
	 * 
	 * @param name
	 * @param imageName
	 * @param x
	 * @param y
	 */
	public DualSprite(String image1, String image2, float x, float y, float opacity) {
		super(x, y);
		this.image1 = AssetManager.getInstance().getImage(image1 + ".png");
		this.image2 = AssetManager.getInstance().getImage(image2 + ".png");
		
		this.width = this.image1.getWidth(null);
		this.height = this.image1.getHeight(null);
		this.opacity = 1.0f;
		
		boundingRect.setBounds((int)x,(int)y,(int)width,(int)height);
		activeImage = 1;
	}
	
	@Override
	public void update() {
		boundingRect.setBounds((int)x,(int)y,(int)width,(int)height);
	}
	
	public int getActiveImage(){
		return activeImage;
	}
	
	public void setActiveImage(int active){
		activeImage=active;
	}

	@Override
	public void draw(Graphics2D g2d) {
		// set alphacomposite to our opacity
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				opacity));
		g2d.drawImage(activeImage==1?this.image1:this.image2, (int) x, (int) y, null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));
	}

}
