package rickelectric.game.chosen.entities;
import java.awt.Graphics2D;

import rickelectric.game.chosen.GameSystem;

public class Ball extends Entity {
	private float xSpeed;
	private float ySpeed;

	// sprite object
	private Sprite ball;

	public Ball(float x, float y) {
		super(x, y);

		xSpeed = 1;
		ySpeed = 1;

		ball = new Sprite("ball2", x, y);
		this.width = ball.getWidth();
		this.height = ball.getHeight();

	}

	/**
	 * Second Constructor
	 * 
	 * @param game
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param xSpeed
	 * @param ySpeed
	 */
	public Ball(float x, float y, int xSpeed, int ySpeed) {
		// call first constructor
		this(x, y);

		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;

	}

	@Override
	public void update() {
		this.x += xSpeed;
		this.y += ySpeed;

		if (x < 0 || (x + width) > GameSystem.getInstance().getScreenWidth()) {
			xSpeed = xSpeed * -1;

			// if (this.x < GameSystem.getInstance().getScreenWidth()/2)
			// {//left side of screen
			// GameSystem.getInstance().setPlayer2points(GameSystem.getInstance().getPlayer2points()
			// + 1);
			// }
			// if (this.x > GameSystem.getInstance().getScreenWidth()/2)
			// {//right side of screen
			// GameSystem.getInstance().setPlayer1points(GameSystem.getInstance().getPlayer1points()
			// + 1);
			// }
			//
			// //instead, we can reset our ball
			// this.x = GameSystem.getInstance().getScreenWidth()/2 -
			// this.width/2;
			// this.y = GameSystem.getInstance().getScreenHeight()/2 -
			// this.height/2;
		}

		if (y < 0 || (y + height) > GameSystem.getInstance().getScreenHeight()) {
			ySpeed = ySpeed * -1;
		}

		// update our ball
		ball.setX(x);
		ball.setY(y);

		// update our bounding rectangle
		this.boundingRect.setLocation((int) x, (int) y);

	}

	@Override
	public void draw(Graphics2D g2d) {
		ball.draw(g2d);

		// g2d.setColor(Color.red);
		// g2d.draw(boundingRect);

	}

	public void hitBall() {
		xSpeed = xSpeed * -1;
	}

}
