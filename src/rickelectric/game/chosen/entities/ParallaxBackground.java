package rickelectric.game.chosen.entities;
import java.awt.Graphics2D;

import rickelectric.game.chosen.GameSystem;

public class ParallaxBackground implements GameEntityServices {
	private Sprite background;
	private float[] xPositions;
	private float speed;

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public ParallaxBackground(GameSystem game, String name, float speed) {
		background = new Sprite(name, 0f, 0f);

		this.speed = speed;

		// init x position array.
		this.xPositions = new float[(int) (game.getScreenWidth()
				/ background.getWidth() + 2)];

		for (int i = 0; i < xPositions.length; i++) {
			xPositions[i] = i * background.getWidth();
		}
	}

	@Override
	public void update() {
		for (int i = 0; i < xPositions.length; i++) {
			xPositions[i] += speed;

			// If the background is moving to the left
			if (speed <= 0) {
				if (xPositions[i] <= -background.getWidth()) {
					xPositions[i] = background.getWidth()
							* (xPositions.length - 1);
				}// end if
			}// end if
			else {
				if (xPositions[i] >= background.getWidth()
						* (xPositions.length - 1)) {
					xPositions[i] = -background.getWidth();
				}// end if
			}
		}// end for
	}

	@Override
	public void draw(Graphics2D g2d) {
		for (int i = 0; i < xPositions.length; i++) {
			background.draw(g2d, xPositions[i], 0);
		}
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setRepeat(boolean b) {
		// TODO Auto-generated method stub

	}

	public void reset() {
		for (int i = 0; i < xPositions.length; i++) {
			xPositions[i] = i * background.getWidth();
		}
	}

}
