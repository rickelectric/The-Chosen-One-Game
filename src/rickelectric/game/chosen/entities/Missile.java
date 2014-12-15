package rickelectric.game.chosen.entities;
import java.awt.Graphics2D;

public class Missile extends Entity implements Projectile{

	private float speed;
	private int type;
	private Entity source;

	private int direction, state;
	public static final int LEFT = 2, RIGHT = 1;
	public static final int STATE_FLYING = 0, STATE_EXPLODING = 1,
			STATE_END = 2;

	private DualAnimatedSprite flying, exploding;

	public Missile(Entity source, float x, float y, int speed) {
		super(x, y);
		this.source = source;
		if (source != null) {
			this.x = x - source.width / 2;
			this.y = y - source.height / 2;
		}
		flying = new DualAnimatedSprite("Missile/Flying-R", "Missile/Flying-L",
				x, y, 4, 10, 2);
		exploding = new DualAnimatedSprite("Missile/Explode-R",
				"Missile/Explode-L", x, y, 11, 30, 2);
		exploding.setLooping(false);
		this.speed = speed;
		boundingRect.setBounds((int) this.x, (int) this.y,
				(int) flying.getFrameWidth(), (int) flying.getFrameHeight());
	}

	public void setSource(Entity source) {
		this.source = source;
		if (source != null) {
			this.x = x - source.width / 2;
			this.y = y - source.height / 2;
		}
	}

	public Entity getSource() {
		return source;
	}

	@Override
	public void update() {
		switch (state) {
		case STATE_FLYING:
			setX(x+speed);
			flying.update();
			break;
		case STATE_EXPLODING:
			exploding.update();
			if (exploding.isHasFinished()) {
				state = STATE_END;
			}
			break;
		case STATE_END:
			break;
		}
	}

	public void setX(float x) {
		super.setX(x);
		flying.boundingRect.setLocation((int) x, (int) y);
		exploding.boundingRect.setLocation((int) x, (int) y);
		boundingRect.setLocation((int) x, (int) y);
	}

	public void setY(float y) {
		super.setY(y);
		flying.boundingRect.setLocation((int) x, (int) y);
		exploding.boundingRect.setLocation((int) x, (int) y);
		boundingRect.setLocation((int) x, (int) y);
	}

	public int getType() {
		return type;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	public void draw(Graphics2D g2d) {
		switch (state) {
		case STATE_FLYING:
			flying.draw(g2d);
			break;
		case STATE_EXPLODING:
			exploding.draw(g2d);
		case STATE_END:
			flying.reset();
			exploding.reset();
			break;
		}
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
		flying.setActiveImage(direction==RIGHT?1:2);
		exploding.setActiveImage(direction==RIGHT?1:2);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
