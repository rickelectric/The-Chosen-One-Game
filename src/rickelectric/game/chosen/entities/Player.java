package rickelectric.game.chosen.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import rickelectric.game.chosen.CutscenesManager;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.KeyboardInputService;
import rickelectric.game.chosen.MyGeometry;
import rickelectric.game.chosen.RectangleOperations;
import rickelectric.game.chosen.SoundManager;
import rickelectric.game.chosen.level.LevelScreen_1;
import rickelectric.game.chosen.level.tilemap.Tile;

/**
 * Original
 *
 */
public class Player extends DualAnimatedSprite {
	/**
	 * Direction enumeration
	 *
	 */
	private enum Direction {
		Vertical, Horizontal
	}

	private enum PlayerState {
		InAir, OnGround
	}

	private PlayerID player;
	private Lightning lightning;
	private long lighTime, shootTime;

	private Direction direction;
	private PlayerState playerState;
	private float playerSpeed;

	// For Jump & Gravity
	private int jumpForce;
	private float gravity;
	private float velocityY;
	private int doubleJump;

	// For Running & Friction
	private float accel;
	private float drag;
	private float velocityX;
	private long lastJump;
	private double energy;

	private DualAnimatedSprite megaTrans;
	private boolean megaOn, postMega;

	public Player(PlayerID player, float x, float y) {
		super(player.getImageR(), player.getImageL(), x, y, player
				.getNumFrames(), 35, player.reverseImageID());
		this.player = player;
		boundingRect.setBounds((int) x, (int) y,
				(int) ((2 * getFrameWidth()) / 3), (int) getFrameHeight());
		boundingRect.x = boundingRect.x
				+ (int) (getFrameWidth() / 2 - image1.getWidth(null) / 2);
		playerSpeed = 15;
		jumpForce = 20;
		gravity = 1;
		drag = 1;

		accel = 0.5f;
		drag = 1f;
		velocityX = 0;
		velocityY = 0;
		playerState = PlayerState.InAir;

		doubleJump = 1;
		lastJump = lighTime = shootTime = System.currentTimeMillis();

		energy = 200;
		lightning = new Lightning(x + boundingRect.width, y + height / 2);

		boundingRect.setLocation((int) x, (int) y);
		postMega = megaOn = false;
	}

	public void setDuration(int duration) {
		for (int i = 0; i < this.numFrames; i++) {
			this.duration[i] = duration / this.numFrames;
		}
	}

	@Override
	public float getWidth() {
		return getFrameWidth();
	}

	public void setX(float x) {
		super.setX(x);
		boundingRect.x = 10 + (int) ((x + getFrameWidth() / 2) - image1
				.getWidth(null) / 2);
	}

	public void setY(float y) {
		super.setY(y);
		boundingRect.y = (int) ((y + getFrameHeight() / 2) - image1
				.getHeight(null) / 2);
	}

	public void update() {
		// Check Motion
		int horizontal = megaOn || postMega ? 0 : KeyboardInputService
				.getInstance().isLeft() ? -1 : (KeyboardInputService
				.getInstance().isRight() ? 1 : 0);

		// Player movement
		if (horizontal != 0) {
			direction = Direction.Horizontal;
			setActiveImage(horizontal == 1 ? 1 : 2);
			velocityX = horizontal * playerSpeed;
			velocityX += horizontal * accel;
		} else {
			setFrame(player.restOrdinalID());
		}

		if (!(megaOn || postMega) && velocityX != 0) {
			direction = Direction.Horizontal;
			if (velocityX > 0) {
				velocityX -= playerState == PlayerState.OnGround ? drag
						: drag / 5;
				if (velocityX < 0)
					velocityX = 0;
			} else {
				velocityX += playerState == PlayerState.OnGround ? drag
						: drag / 5;
				if (velocityX > 0)
					velocityX = 0;
			}
			setX(x + velocityX);
		}

		if (postMega) {
			postMegaRelease();
		}

		if (megaOn) {
			if (megaTrans != null) {
				if (megaTrans.isHasFinished()) {
					megaAttack();
				} else
					megaTrans.update();
			} else
				megaAttack();
		}

		if (!(megaOn || postMega) && KeyboardInputService.getInstance().isUp()
				&& KeyboardInputService.getInstance().isA()
				&& getEnergy() >= 300) {

			if (player == PlayerID.Ghost && megaOn == false) {
				megaTrans = new DualAnimatedSprite(
						"Cutscenes/Ghost/GhostTransforms-R",
						"Cutscenes/Ghost/GhostTransforms-L", x, y, 18, 40, 2);
				megaTrans.setActiveImage(getActiveImage());
				megaTrans.setLooping(false);
			}
			megaOn = true;
			energy -= 300;

		}

		if (!(megaOn || postMega) && KeyboardInputService.getInstance().isZ()
				&& energy >= 10) {
			if (velocityX != 0 || velocityY != 0) {
				lightning.setX(x
						+ (activeImage == 1 ? boundingRect.width
								: boundingRect.width / 2));
				lightning.setY(y + height / 2);

				lightning.setElevation(KeyboardInputService.getInstance()
						.isUp() ? 1 : (KeyboardInputService.getInstance()
						.isDown() ? -1 : 0));

				if ((lightning.isVisible() && System.currentTimeMillis()
						- lighTime > 50)
						|| (!lightning.isVisible() && System
								.currentTimeMillis() - lighTime > 50)) {
					lightning.setVisible(!lightning.isVisible());
					if (lightning.isVisible())
						lightning.updateChosenPath();
					lighTime = System.currentTimeMillis();
				}
				lightning.setDirection(activeImage == 1 ? 1 : -1);
				lightning.update();
				energy -= 10;
			}
		} else {
			lightning.setVisible(false);
			SoundManager.getInstance().stopSound("lightning");
		}
		if(lightning.isVisible()){
			SoundManager.getInstance().playSound("lightning", false);
		}

		if (!(megaOn || postMega) && KeyboardInputService.getInstance().isX()
				&& System.currentTimeMillis() - shootTime > 200 && energy >= 20) {
			GameSystem
					.getInstance()
					.getLevelScreen()
					.shoot(this,
							(int) ((activeImage == 1 ? 75 : -105) + (Math
									.random() * 30)), 20);
			energy -= 20;
			shootTime = System.currentTimeMillis();
		}

		// Check Incline At Fw of Movement

		// If Not Moving, Reset Player To Base Position (s1 = 5,s2 = 11)

		// Check For Ground Below @fn

		// Check For Ceiling Above @fn 2

		if (!(megaOn || postMega)
				&& KeyboardInputService.getInstance().isSpace()
				&& doubleJump < 2
				&& System.currentTimeMillis() - lastJump > 500) {
			jump(true);
		}

		switch (playerState) {
		case InAir:
			if (megaOn || postMega)
				break;
			velocityY += gravity;
			y += velocityY;
			setFrame(player.jumpOrdinalID());
			break;
		case OnGround:
			if (megaOn || postMega)
				break;
			doubleJump = 0;
			if (horizontal != 0)
				super.update();

			velocityY = gravity;
			velocityX = 0;
			break;
		}

		boundingRect.setLocation((int) x, (int) y);

		/**
		 * Checks for h-collisions Remove Vertical Checks from here (Separate
		 * Into 2 Function (Independent)
		 */

		handleTerrainCollisions();
		handleTileCollisions0(direction);

		// Restore Energy
		if (!megaOn)
			restoreEnergy();
	}

	private void jump(boolean full) {
		lastJump = System.currentTimeMillis();
		this.playerState = PlayerState.InAir;
		velocityY = -(full?jumpForce:jumpForce/2);
		doubleJump++;
	}

	private void megaAttack() {
		CutscenesManager.getInstance().playScene(player,
				GameSystem.getInstance().getLevelScreen().getScreenID());
		GameSystem.getInstance().changeScreen(GameSystem.CUTSCENE);
		megaOn = false;
		postMega = true;
	}

	private void postMegaRelease() {
		GameSystem.getInstance().getLevelScreen().megaRelease();
		jump(false);
		velocityX = -15;
		postMega = false;
	}

	private void restoreEnergy() {
		if (energy < 200)
			energy += 1.5;
		if (KeyboardInputService.getInstance().isDown()
				&& KeyboardInputService.getInstance().isA()) {
			energy += 1.5;
		}
		if (energy > 400) {
			energy = 400;
		}
	}

	private void handleTerrainCollisions() {
		Point2D topCenter = new Point2D.Double(boundingRect.x
				+ boundingRect.width / 2, boundingRect.y);
		Point2D bottomCenter = new Point2D.Double(boundingRect.x
				+ boundingRect.width / 2, boundingRect.y + boundingRect.height);
		Line2D centerLine = new Line2D.Double(topCenter, bottomCenter);
		playerState = PlayerState.InAir;
		int terrainStart = Globals.MAP.getTerrainStart();
		for (int t = terrainStart; t < Globals.MAP.getMapData().layers.length; t++) {
			if (Globals.MAP.getMapData().layers[t].visible == true)
				for (Line2D l : Globals.MAP.getTerrainLayer(t)) {
					Point2D iPoint = MyGeometry.getIntersectionPoint(
							centerLine, l);
					if (iPoint == null) {
						continue;
					}
					// System.out.println("Intersection Point: "+iPoint);

					this.y = this.y
							- (float) Math.abs(iPoint.getY()
									- bottomCenter.getY()) + 1;
					setY(this.y);
					playerState = PlayerState.OnGround;
					break;
				}
		}
	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.red);
		if (megaOn && megaTrans != null) {
			megaTrans.draw(g2d);
		} else {
			super.draw(g2d);
		}
		if (KeyboardInputService.getInstance().isDown()
				&& KeyboardInputService.getInstance().isA()) {
			g2d.setStroke(new BasicStroke(6));
			g2d.setColor(Color.orange);
			g2d.drawOval((int) x, (int) y, (int) getFrameWidth(), (int) height);
		}
		if (lightning.isVisible()) {
			lightning.draw(g2d);
		}
	}

	private void handleTileCollisions0(Direction currentDirection) {
		int leftTile = (int) (x / Globals.MAP.getMapData().tilewidth);
		int topTile = (int) (y / Globals.MAP.getMapData().tileheight);
		int rightTile = (int) (Math.ceil((x + getWidth())
				/ Globals.MAP.getMapData().tilewidth)) - 1;
		int bottomTile = (int) (Math.ceil((y + height)
				/ Globals.MAP.getMapData().tileheight)) - 1;

		for (int layer = 0; layer < Globals.MAP.getTileLayerCount(); layer++)
			for (int y = topTile; y < bottomTile; ++y) {
				for (int x = leftTile; x <= rightTile; ++x) {
					// if we have a tile at this location
					if (Globals.MAP.getMapData().layers[layer].visible
							&& Globals.MAP.getTileAt(layer, x, y) != null) {

						if (layer < (GameSystem.getInstance().getLevelScreen() instanceof LevelScreen_1 ? 2
								: 1)) {
							// Background Object (Water Included). Do Not
							// Collide. Invoke Action.
						} else {

							// Bounding rectangle of tile
							Tile tile = Globals.MAP.getTileAt(layer, x, y);
							if (tile.getTileTypeID() == 53
									|| tile.getTileTypeID() == 54)
								continue;
							Rectangle tileRect = tile.boundingRect;

							float vInc = RectangleOperations
									.getVerticalIntersectionDepth(boundingRect,
											tileRect);
							float hInc = RectangleOperations
									.getHorizontalIntersectionDepth(
											boundingRect, tileRect);

							// vInc=0;
							if (Math.abs(hInc) <= Math.abs(vInc)) {
								this.x += hInc;
								this.x = Math.round(this.x);
							} else {
								if (y == topTile) {
									this.y += vInc;
									this.y = Math.round(this.y);
									this.playerState = PlayerState.InAir;
								}
							}
						}
					}

				}
			}
	}

	public PlayerState getPlayerState() {
		return playerState;
	}

	public Lightning getLightning() {
		return lightning;
	}

	public int getEnergy() {
		return (int) energy;
	}

	public boolean isMega() {
		return megaOn||postMega;
	}

}
