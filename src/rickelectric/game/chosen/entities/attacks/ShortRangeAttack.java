package rickelectric.game.chosen.entities.attacks;

public interface ShortRangeAttack {

	enum AttackDirection {
		LEFT, RIGHT
	}

	public static final AttackDirection DIRECTION_LEFT = AttackDirection.LEFT,
			DIRECTION_RIGHT = AttackDirection.RIGHT;

	public void setDirection(AttackDirection direction);

	public void setVisible(boolean b);

	public boolean isVisible();

	public void update();

}
