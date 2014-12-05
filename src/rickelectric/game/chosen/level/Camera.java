package rickelectric.game.chosen.level;
import java.awt.Graphics2D;

import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.entities.Entity;

public class Camera {
	private float translateX;
	private float translateY;
	private float cameraX;
	private float cameraY;
	// private float cameraSpeed;
	private float cameraWidth;
	private float cameraHeight;
	private Entity entity;

	/**
	 * Default Constructor
	 */
	public Camera() {
		this.cameraX = 0;
		this.cameraY = 0;
		this.translateX = 0;
		this.translateY = 0;
		// this.cameraSpeed = 4F;
		this.cameraWidth = Globals.SCREEN_WIDTH;
		this.cameraHeight = Globals.SCREEN_HEIGHT;
	}

	/**
	 * Constructor
	 * 
	 * @param x
	 * @param y
	 */
	public Camera(float x, float y) {
		this();

		this.cameraX = x;
		this.cameraY = y;
		this.translateX = -x;
		this.translateY = -y;
	}

	/**
	 * update camera
	 */
	public void updateCamera() {
		float oldTranslateX = this.translateX;

		if (entity != null) {
			float translateX = -(entity.getX() + entity.getWidth() / 2 - GameSystem
					.getInstance().getScreenWidth() / 2);
			if ((-translateX >= 0
					&& -translateX
							+ Globals.SCREEN_WIDTH
							/ GameSystem.getInstance().getLevelScreen()
									.getScaler() < Globals.WORLD_WIDTH))
				this.translateX = translateX;
			if(entity.getBoundingRect().x < -translateX){
				this.translateX = 0;
			}
			float translateY = -(entity.getY() + entity.getHeight() / 2 - GameSystem
					.getInstance().getScreenHeight() / 2);
			if (-translateY >= 0
					&& -translateY
							+ Globals.SCREEN_HEIGHT
							/ GameSystem.getInstance().getLevelScreen()
									.getScaler() <= Globals.WORLD_HEIGHT)
				this.translateY = translateY;

		}

		// update camera X and camera Y
		this.cameraX = -translateX;
		this.cameraY = -translateY;

		GameSystem.getInstance().getLevelScreen().getBackground()
				.setSpeed(-(translateX - oldTranslateX));
		GameSystem.getInstance().getLevelScreen().getBackground().update();
	}

	/**
	 * Start g2d translation
	 */
	public void drawCameraStart(Graphics2D g2d) {
		g2d.translate(translateX, translateY);
	}

	public void drawCameraStart(Graphics2D g2d, double scaler) {
		g2d.translate(translateX, translateY);
		this.cameraWidth /= scaler;
		this.cameraHeight /= scaler;
	}

	/**
	 * revert g2d translation
	 */
	public void drawCameraStop(Graphics2D g2d) {
		g2d.translate(-translateX, -translateY);
	}

	public void drawCameraStop(Graphics2D g2d, double scaler) {
		g2d.translate(-translateX, -translateY);
		this.cameraWidth *= scaler;
		this.cameraHeight *= scaler;
	}

	public float getCameraX() {
		return cameraX;
	}

	public float getCameraY() {
		return cameraY;
	}

	public float getCameraWidth() {
		return cameraWidth;
	}

	public float getCameraHeight() {
		return cameraHeight;
	}

	public void followEntity(Entity entity) {
		this.entity = entity;
	}

}
