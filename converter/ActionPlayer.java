import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class ActionPlayer extends Entity {

	private DualAnimatedSprite run, weaponOut, weaponIn, intro, outro, stance,
			jumpUp, jumpDown;

	public static final int STANDING = 0, RUNNING = 1, SHOOTING = 2, INTRO = 3,
			OUTRO = 4, JUMPING = 5;
	private int state;

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

	private Lightning lightning;
	private long lighTime, shootTime;

	private Direction direction;
	private PlayerState playerState;
	private int playerSpeed;
	private int fallSpeed;

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

	private int activeImage;

	public ActionPlayer(float x, float y) {
		super(x, y);
		run = new DualAnimatedSprite("Girl2/Running-R", "Girl2/Running-L", x,
				y, 8, 30, 2);

		weaponOut = new DualAnimatedSprite("Girl2/WeaponOut-R",
				"Girl2/WeaponOut-L", x, y, 12, 30, 2);
		weaponOut.setLooping(false);

		weaponIn = new DualAnimatedSprite("Girl2/WeaponIn-R",
				"Girl2/WeaponIn-L", x, y, 5, 20, 2);
		weaponIn.setLooping(false);

		intro = new DualAnimatedSprite("Girl2/Intro-R", "Girl2/Intro-L", x, y,
				23, 80, 2);
		outro = new DualAnimatedSprite("Girl2/Outro-R", "Girl2/Outro-L", x, y,
				23, 80, 2);

		stance = new DualAnimatedSprite("Girl2/Stance-R", "Girl2/Stance-L", x,
				y, 1, 30, 2);
		jumpUp = new DualAnimatedSprite("Girl2/JumpUp-R", "Girl2/JumpUp-L", x,
				y, 1, 30, 2);
		jumpDown = new DualAnimatedSprite("Girl2/JumpDown-R",
				"Girl2/JumpDown-L", x, y, 1, 30, 2);

		setState(OUTRO);

		boundingRect.setBounds((int) x, (int) y,
				(int) ((2 * stance.getFrameWidth()) / 3),
				(int) stance.getFrameHeight());
		boundingRect.x = boundingRect.x
				+ (int) (stance.getFrameWidth() / 2 - stance.image1
						.getWidth(null) / 2);
		playerSpeed = 10;
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
		bindRect();

	}

	private void bindRect() {
		boundingRect.setLocation((int) x, (int) y);
		run.setX(x);
		run.setY(y);
		weaponOut.setX(x);
		weaponOut.setY(y);
		weaponIn.setX(x);
		weaponIn.setY(y);
		intro.setX(x);
		intro.setY(y);
		outro.setX(x);
		outro.setY(y);
		stance.setX(x);
		stance.setY(y);
		jumpUp.setX(x);
		jumpUp.setY(y);
		jumpDown.setX(x);
		jumpDown.setY(y);
	}

	@Override
	public float getWidth() {
		return stance.getFrameWidth();
	}

	private void setState(int state) {
		this.state = state;
	}

	public void setX(float x) {
		super.setX(x);
		boundingRect.x = (int) ((x + stance.getFrameWidth() / 2) - stance.image1
				.getWidth(null) / 2);
		bindRect();
	}

	public void setY(float y) {
		super.setY(y);
		boundingRect.y = (int) ((y + stance.getFrameHeight() / 2) - stance.image1
				.getHeight(null) / 2);
		bindRect();
	}

	public void setActiveImage(int active) {
		this.activeImage = active;
		run.setActiveImage(active);
		weaponOut.setActiveImage(active);
		weaponIn.setActiveImage(active);
		intro.setActiveImage(active);
		outro.setActiveImage(active);
		stance.setActiveImage(active);
		jumpUp.setActiveImage(active);
		jumpDown.setActiveImage(active);
	}

	public void updatePlayer() {
		// Check Motion
		int horizontal = KeyboardInputService.getInstance().isLeft() ? -1
				: (KeyboardInputService.getInstance().isRight() ? 1 : 0);
		/*
		 * int vertical = KeyboardInputService.getInstance().isUp() ? -1 :
		 * (KeyboardInputService.getInstance().isDown() ? 1 : 0);
		 */
		{
			// Player movement
			if (horizontal != 0) {
				direction = Direction.Horizontal;
				setState(RUNNING);
				setActiveImage(horizontal == 1 ? 1 : 2);
				velocityX = horizontal * playerSpeed;
				velocityX += horizontal * accel;
			} else {
				setState(STANDING);
			}

			if (velocityX != 0) {
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

			}
			setX(x + velocityX);
		}

		if (KeyboardInputService.getInstance().isZ() && energy >= 10) {
			if (velocityX != 0 || velocityY != 0) {
				lightning.setX(x + boundingRect.width);
				lightning.setY(y + height / 2);
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
				// energy -= 10;
				setState(SHOOTING);
			}
		} else {
			lightning.setVisible(false);
		}

		if (KeyboardInputService.getInstance().isX()
				&& System.currentTimeMillis() - shootTime > 200 && energy >= 20) {
			GameSystem.getInstance().getLevelScreen().shoot(this, activeImage == 1 ? 90 : -90,
					20);
			energy -= 20;
			shootTime = System.currentTimeMillis();
		}

		// Check Incline At Fw of Movement

		// If Not Moving, Reset Player To Base Position (s1 = 5,s2 = 11)

		// Check For Ground Below @fn

		// Check For Ceiling Above @fn 2

		if (KeyboardInputService.getInstance().isSpace() && doubleJump < 2
				&& System.currentTimeMillis() - lastJump > 500) {
			lastJump = System.currentTimeMillis();
			this.playerState = PlayerState.InAir;
			velocityY = -jumpForce;
			doubleJump++;
		}

		switch (playerState) {
		case InAir:
			velocityY += gravity;
			y += velocityY;
			setState(JUMPING);
			break;
		case OnGround:
			doubleJump = 0;
			if (horizontal != 0)
				;

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
		if (energy < 200)
			energy += 1.5;
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
					Point2D iPoint = CustomGeometry.getIntersectionPoint(
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

	@Override
	public void update() {
		if (KeyboardInputService.getInstance().isX()) {
			weaponIn.update();
			weaponOut.setFrame(0);
		} else {
			weaponOut.update();
			weaponIn.setFrame(0);
		}
		switch (state) {
		case RUNNING:
			updatePlayer();
			run.update();
			break;
		case SHOOTING:
			updatePlayer();
			break;
		case INTRO:
			intro.update();
			break;
		case OUTRO:
			outro.update();
			break;
		case JUMPING:
			updatePlayer();
			if(this.velocityY<0)
				jumpUp.update();
			else
				jumpDown.update();
			break;
		default:
			updatePlayer();
			stance.update();
			break;
		}
		bindRect();
	}

	@Override
	public void draw(Graphics2D g2d) {
		switch (state) {
		case RUNNING:
			run.draw(g2d);
			break;
		case SHOOTING:
			if (KeyboardInputService.getInstance().isX())
				weaponOut.draw(g2d);
			else
				weaponIn.draw(g2d);
			break;
		case INTRO:
			intro.draw(g2d);
			break;
		case OUTRO:
			outro.draw(g2d);
			break;
		case JUMPING:
			// If velocityY<0
			jumpUp.draw(g2d);
			// Else
			jumpDown.draw(g2d);
			break;
		default:
			stance.draw(g2d);
			break;
		}
	}

	private void handleTileCollisions0(Direction currentDirection) {
		int leftTile = (int) (x / Globals.MAP.getMapData().tilewidth);
		int topTile = (int) (y / Globals.MAP.getMapData().tileheight);
		int rightTile = (int) (Math.ceil((x + getWidth())
				/ Globals.MAP.getMapData().tilewidth)) - 1;
		int bottomTile = (int) (Math.ceil((y + height)
				/ Globals.MAP.getMapData().tileheight)) - 1;

		for (int layer = 0; layer < Globals.MAP.getMapData().layers.length; layer++)
			for (int y = topTile; y < bottomTile; ++y) {
				for (int x = leftTile; x <= rightTile; ++x) {
					// if we have a tile at this location
					if (Globals.MAP.getMapData().layers[layer].visible
							&& Globals.MAP.getTileAt(layer, x, y) != null) {

						if (layer < 2) {
							// Background Object (Water Included). Do Not
							// Collide. Invoke Action.
						} else {

							// Bounding rectangle of tile
							Rectangle tileRect = Globals.MAP.getTileAt(layer,
									x, y).boundingRect;

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

}
