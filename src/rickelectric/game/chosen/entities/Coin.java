package rickelectric.game.chosen.entities;
public class Coin extends AnimatedSprite {

	public Coin(float x, float y) {
		super("coin", x, y, 16, 50);
		boundingRect.setBounds((int) x, (int) y, (int) getFrameWidth(),
				(int) getFrameHeight());
	}
}
