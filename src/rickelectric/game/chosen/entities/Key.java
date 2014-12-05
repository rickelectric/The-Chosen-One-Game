package rickelectric.game.chosen.entities;
public class Key extends AnimatedSprite {

	public Key(float x, float y) {
		super("Key", x, y, 44, 60);
		boundingRect.setBounds((int) x, (int) y, (int) getFrameWidth(),
				(int) getFrameHeight());
	}
}
