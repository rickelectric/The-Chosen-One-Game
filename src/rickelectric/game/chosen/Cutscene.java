package rickelectric.game.chosen;

import java.awt.Graphics2D;

import rickelectric.game.chosen.entities.AnimatedSprite;

public class Cutscene extends AnimatedSprite {

	private double scale;

	public Cutscene(String imageName, int numFrames, int duration) {
		super(imageName, 0, 0, numFrames, duration);
		scale = Globals.SCREEN_HEIGHT / getFrameHeight();
		this.x = (float) ((1/scale) * (Globals.SCREEN_WIDTH / 2 - (scale * getFrameWidth() / 2)));
		this.y = (float) ((1/scale)*(Globals.SCREEN_HEIGHT / 2 - (scale * getFrameHeight() / 2)));
		setLooping(false);
	}

	public void draw(Graphics2D g2d) {
		g2d.scale(scale, scale);
		super.draw(g2d);
		g2d.scale(1 / scale, 1 / scale);
	}

}
