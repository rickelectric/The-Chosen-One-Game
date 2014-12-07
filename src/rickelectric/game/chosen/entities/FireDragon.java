package rickelectric.game.chosen.entities;
import java.awt.Color;
import java.awt.Graphics2D;

import rickelectric.game.chosen.GameSystem;

public class FireDragon extends DualAnimatedSprite {

	private float boundX1, boundX2;
	private Lightning dragonBolt;

	private Long lastAttacked;

	private int hp;

	public FireDragon(float x, float y) {
		super("Dragon/Dragon-R", "Dragon/Dragon-L", x, y, 13, 20, 1);
		boundX1 = x;
		boundX2 = x + 300;
		boundingRect.setBounds((int) x, (int) y, (int) frameWidth,
				(int) frameHeight);

		dragonBolt = new Lightning(x + width, y + height / 3);
		dragonBolt.setVisible(false);
		dragonBolt.setElevation(-1);

		hp = 3000;
		lastAttacked = System.currentTimeMillis() - 20000;
	}

	public void bind(int x1, int x2) {
		this.boundX1 = x1;
		this.boundX2 = x2;
	}

	public void update() {
		super.update();
		if (x < boundX1)
			this.setActiveImage(1);
		if (x > boundX2)
			this.setActiveImage(2);
		int inc = moveAttacked()?20:10;
		this.setX(activeImage == 1 ? boundingRect.x + inc : boundingRect.x - inc);

		if (activeImage == 1) {
			dragonBolt.setX(boundingRect.x + boundingRect.width - 5);
			dragonBolt.setDirection(1);
			dragonBolt.update();
		} else {
			dragonBolt.setX(boundingRect.x);
			dragonBolt.setDirection(-1);
			dragonBolt.update();
		}
		if (GameSystem.getInstance().getLevelScreen().getPlayer().boundingRect
				.intersects(dragonBolt.boundingRect)) {
			if (Math.random() < 0.3) {
				dragonBolt.setVisible(true);
				GameSystem.getInstance().getLevelScreen().decreaseHP(2);
			}
		} else
			dragonBolt.setVisible(false);

		if (GameSystem.getInstance().getLevelScreen().getPlayer()
				.getLightning().boundingRect.intersects(boundingRect)
				&& GameSystem.getInstance().getLevelScreen().getPlayer()
						.getLightning().isVisible()) {
			attacked();
			decreaseHP(50);
		}

		if (GameSystem.getInstance().getLevelScreen().getPlayer().boundingRect
				.intersects(boundingRect)) {
			GameSystem.getInstance().getLevelScreen().decreaseHP(1);
		}

		boundingRect.setBounds((int) x, (int) y, (int) frameWidth,
				(int) frameHeight);
	}

	public void draw(Graphics2D g2d) {
		super.draw(g2d);
		if (dragonBolt.isVisible()) {
			dragonBolt.draw(g2d);
		}
		if (beingAttacked()) {
			g2d.setColor(hp < 200 ? Color.red : hp < 1500 ? Color.orange
					: Color.yellow);
			g2d.drawRect((int) (x + frameWidth / 2 - 50), (int) (y - 30), 100,
					20);
			g2d.fillRect((int) (x + frameWidth / 2 - 50), (int) (y - 30),
					hp / 30, 20);
		}
	}

	public Lightning getLightning() {
		return dragonBolt;
	}
	
	private boolean moveAttacked(){
		return System.currentTimeMillis() - lastAttacked < 800;
	}

	public boolean beingAttacked() {
		return System.currentTimeMillis() - lastAttacked < 5000;
	}

	public void attacked() {
		lastAttacked = System.currentTimeMillis();
	}

	public void decreaseHP(int i) {
		hp -= i;
		if (hp < 0) {
			hp = 0;
			destroy();
		}
	}

	private void destroy() {
		setVisible(false);
		dragonBolt.setVisible(false);
	}

}
