package rickelectric.game.chosen.entities.enemies;

import java.awt.Color;
import java.awt.Graphics2D;

import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.entities.DualAnimatedSprite;

public class WaterDragon extends DualAnimatedSprite implements Dragon {

	private static final int MAX_HP=800;
	
	private int hp;
	private long lastAttacked;
	private float boundX1,boundX2,baseY;

	public WaterDragon(float x, float y) {
		super("Dragon/WaterDragon-R", "Dragon/WaterDragon-L", x, y, 6, 40, 1);

		boundingRect.setBounds((int) x, (int) y, (int) frameWidth,
				(int) frameHeight);

		baseY = y;
		hp = MAX_HP;
		bind(x,x+600);
		lastAttacked = System.currentTimeMillis() - 5000;
	}

	@Override
	public void update() {
		super.update();
		if (x < boundX1)
			this.setActiveImage(1);
		if (x > boundX2)
			this.setActiveImage(2);
		int inc = 10;
		this.setX(activeImage == 1 ? boundingRect.x + inc : boundingRect.x - inc);
		this.setY((float) (baseY+(30*Math.sin(Math.toRadians(getX()-boundX1)))));
		
		if (GameSystem.getInstance().getLevelScreen().getPlayer()
				.getLightning().getBoundingRect().intersects(boundingRect)
				&& GameSystem.getInstance().getLevelScreen().getPlayer()
						.getLightning().isVisible()) {
			attacked();
			decreaseHP(50);
		}

		if (GameSystem.getInstance().getLevelScreen().getPlayer()
				.getBoundingRect().intersects(boundingRect)) {
			GameSystem.getInstance().getLevelScreen().decreaseHP(1);
		}
		
		boundingRect.setBounds((int) x, (int) y, (int) frameWidth,
				(int) frameHeight);
	}

	@Override
	public void draw(Graphics2D g2d) {
		super.draw(g2d);
		if(beingAttacked()){
			g2d.setColor(hp < MAX_HP / 5 ? Color.red
					: hp < MAX_HP / 2 ? Color.orange
							: hp < ((MAX_HP / 4) * 3) ? Color.yellow
									: Color.green);
			g2d.drawRect((int) (x + frameWidth / 2 - 50), (int) (y - 30), 100,
					20);
			g2d.fillRect((int) (x + frameWidth / 2 - 50), (int) (y - 30),
					(int) (((float)hp/(float)MAX_HP)*100f), 20);
		}
	}

	public void decreaseHP(int i) {
		hp -= i;
		if (hp < 0) {
			hp = 0;
			destroy();
		}
	}

	public void destroy() {
		setVisible(false);
		// Destroy Attacks, Or Wait For Them To Go Off-Screen
	}

	public boolean beingAttacked() {
		return System.currentTimeMillis() - lastAttacked < 2500;
	}

	public void attacked() {
		lastAttacked = System.currentTimeMillis();
	}

	public void bind(float x1, float x2) {
		this.boundX1 = x1;
		this.boundX2 = x2;
	}

}
