import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Player extends DualAnimatedSprite {

	// http://dabuttonfactory.com/b.png?t=Begin&f=Komika-Bold&ts=35&tc=ffffff&tshs=3&tshc=222222&it=png&c=5&bgt=gradient&bgc=47c&ebgc=238&w=210&h=48

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

	public static enum PlayerID {
		Man1, Man2, Woman1, Woman2, Woman3, Woman4
	}

	private PlayerID character;

	private Direction direction;
	private PlayerState playerState;
	private int playerSpeed;
	private int fallSpeed;
	private int jumpForce;
	private float gravity;
	private float velocityY;

	boolean goingRight;

	public Player(PlayerID playerCharacter, float x, float y) {
		super(getImageR(playerCharacter), getImageL(playerCharacter), x, y,
				getFrames(playerCharacter), 5);
		this.character = playerCharacter;

		playerSpeed = 20;
		jumpForce = 30;
		gravity = 1.0f;
		velocityY = 0;
		playerState = PlayerState.InAir;

		goingRight = true;
	}

	private static int getFrames(PlayerID character) {
		switch (character) {
		case Man1:
			return 11;
		case Man2:
			return 8;
		case Woman1:
			return 8;
		case Woman2:
			return 16;
		case Woman3:
			return 8;
		default:
			return 16;
		}
	}

	private static String getImageL(PlayerID character) {
		switch (character) {
		case Man1:
			return "man-1-L";
		case Man2:
			return "man-2-L";
		case Woman1:
			return "woman-1-L";
		case Woman2:
			return "woman-2-L";
		case Woman3:
			return "woman-3-L";
		default:
			return "woman-4-L";
		}
	}

	private static String getImageR(PlayerID character) {
		switch (character) {
		case Man1:
			return "man-1-R";
		case Man2:
			return "man-2-R";
		case Woman1:
			return "woman-1-R";
		case Woman2:
			return "woman-2-R";
		case Woman3:
			return "woman-3-R";
		default:
			return "woman-4-R";
		}
	}

	public void update() {
		super.update();
		int horizontal = KeyboardInputService.getInstance().isLeft() ? -1
				: (KeyboardInputService.getInstance().isRight() ? 1 : 0);
		int vertical = KeyboardInputService.getInstance().isUp() ? -1
				: (KeyboardInputService.getInstance().isDown() ? 1 : 0);

		if (horizontal != 0) {
			direction = Direction.Horizontal;
			goingRight = horizontal < 0 ? false : true;
			this.x = Math.round(this.x + (playerSpeed * horizontal));
			// super.update();
			super.setActiveImage(goingRight ? 1 : 2);
		}

		if (vertical != 0) {
			direction = Direction.Vertical;
			this.y = Math.round(this.y + (playerSpeed * vertical));
		}

		if (KeyboardInputService.getInstance().isX())
			GameSystem.getInstance().shoot(this, horizontal * 90, 10);

		switch (playerState) {
		case InAir:
			velocityY += gravity;
			y += velocityY;
			break;

		case OnGround:
			velocityY = 0;
			// super.update();
			if (KeyboardInputService.getInstance().isSpace()) {
				this.playerState = PlayerState.InAir;
				velocityY = -jumpForce;
				this.y += velocityY;
			}
			break;
		}

		handleTileCollisions(direction);
		setX(x);
		setY(y);

	}

	public void draw(Graphics2D g2d) {
		super.draw(g2d);
		g2d.draw(new Rectangle2D.Double(x, y, getFrameWidth(), getFrameHeight()));
	}

	/**
	 * leftTile - tile on the left of the player rigtTile - tile on the right of
	 * the player topTile - tile above player bottomTile - tile below player
	 * 
	 * These values are calculated by dividing the x or y coordinate of the
	 * player by the tilewidth or tileheight of the tiles
	 * 
	 * for example: rightTile = ((x + width) / tilewidth ) - 1
	 * 
	 * After we have int values for tile surrounding player, we can check for
	 * collisions using our array of tiles in the LevelMap class
	 * 
	 * our outer for loop goes from the topTile to the bottomTile value, and our
	 * inner for loop goes from our leftTile to our rightTile value.
	 * 
	 * This ensures that we calculate all possible collisions
	 * 
	 * we check our tile array to see if a tile exists at our (x, y) from for
	 * loop
	 * 
	 * if the tile exists, we calculate the horizontal and vertical depths of
	 * our player and the tile, and then account for this, by adjusting our
	 * player's x, y values
	 */
	private void handleTileCollisions(Direction currentDirection) {
		int leftTile = (int) (x / Globals.MAP.getMapData().tilewidth);
		int topTile = (int) (y / Globals.MAP.getMapData().tileheight);
		int rightTile = (int) (Math.ceil((x + width)
				/ Globals.MAP.getMapData().tilewidth)) - 1;
		int bottomTile = (int) (Math.ceil((y + height)
				/ Globals.MAP.getMapData().tileheight)) - 1;

		for (int k = 0; k < Globals.MAP.getMapData().layers.length; k++) {
			for (int y = topTile; y <= bottomTile; ++y) {
				for (int x = leftTile; x <= rightTile; ++x) {
					// if we have a tile at this location
					if (Globals.MAP.getMapData().layers[k].visible
							&& Globals.MAP.getTileAt(k, x, y) != null) {
						this.playerState = PlayerState.OnGround;

						Rectangle tileRect = Globals.MAP.getTileAt(k, x, y).boundingRect;

						float vInc = RectangleOperations
								.getVerticalIntersectionDepth(boundingRect,
										tileRect);
						float hInc = RectangleOperations
								.getHorizontalIntersectionDepth(boundingRect,
										tileRect);

						if (Math.abs(hInc) >= Math.abs(vInc)) {
							this.y += vInc;
							this.y = Math.round(this.y);
						} else {
							this.x += hInc;
							this.x = Math.round(this.x);
						}
					}

				}
			}
		}
	}

	private void handleTileCollisions0(Direction currentDirection) {
		int leftTile = (int) (x / Globals.MAP.getMapData().tilewidth);
		int topTile = (int) (y / Globals.MAP.getMapData().tileheight);
		int rightTile = (int) (Math.ceil((x + width)
				/ Globals.MAP.getMapData().tilewidth)) - 1;
		int bottomTile = (int) (Math.ceil((y + height)
				/ Globals.MAP.getMapData().tileheight)) - 1;

		for (int y = topTile; y <= bottomTile; ++y) {
			for (int x = leftTile; x <= rightTile; ++x) {
				// if we have a tile at this location
				if (Globals.MAP.getTileAt(1, x, y) != null) {
					this.playerState = PlayerState.OnGround;

					// Bounding rectangle of tile
					Rectangle tileRect = Globals.MAP.getTileAt(1, x, y).boundingRect;

					if (currentDirection == Direction.Horizontal) {
						this.x += RectangleOperations
								.getHorizontalIntersectionDepth(boundingRect,
										tileRect);
						Math.round(x);
					} else {
						this.y += RectangleOperations
								.getVerticalIntersectionDepth(boundingRect,
										tileRect);
						Math.round(y);
					}

				}

			}
		}
	}

	public PlayerState getPlayerState() {
		return playerState;
	}

}
