package rickelectric.game.chosen.entities;
public class CatEyes extends DualSprite {

	public CatEyes(float x, float y) {
		super("CatEyes", "CatEyesBig", x, y, 2);
		setActiveImage(1);
		boundingRect.setBounds((int) x, (int) y, (int) width, (int) height);
	}

}
