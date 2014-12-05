package rickelectric.game.chosen.entities;
public class Bullet extends AnimatedSprite {

	public double dx, dy;
	private float speed;
	private int type;
	private int angle;
	private Entity source;

	public Bullet(Entity source, float x, float y, int angle, int speed) {
		super("Buzz", x, y, 16, 40);
		this.source = source;
		if (source != null) {
			this.x = x - source.width / 2;
			this.y = y - source.height / 2;
		}
		this.speed = speed;
		this.angle = angle;
		calcDyDx(angle);
		boundingRect.setBounds((int) this.x, (int) this.y,
				(int) getFrameWidth(), (int) getFrameHeight());
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

	private void calcDyDx(int angle) {
		double rAngle = Math.toRadians(angle - 90);
		dy = Math.sin(rAngle) * speed;
		dx = Math.cos(rAngle) * speed;
	}

	@Override
	public void update() {
		super.update();
		setX((float) (x + dx));
		setY((float) (y + dy));
		boundingRect.setBounds((int) this.x, (int) this.y,
				(int) getFrameWidth(), (int) getFrameHeight());
	}

	public void setX(float x) {
		super.setX(x);
		boundingRect.setLocation((int) x, (int) y);
	}

	public void setY(float y) {
		super.setY(y);
		boundingRect.setLocation((int) x, (int) y);
	}

	public int getType() {
		return type;
	}

	public void setAngle(int angle) {
		this.angle = angle;
		calcDyDx(angle);
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		calcDyDx(angle);
	}

}
