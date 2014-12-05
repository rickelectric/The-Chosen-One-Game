package rickelectric.game.chosen.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import rickelectric.game.chosen.AssetManager;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.KeyboardInputService;
import rickelectric.game.chosen.MouseInputService;
import rickelectric.game.chosen.entities.AnimatedSprite;
import rickelectric.game.chosen.entities.DualSprite;
import rickelectric.game.chosen.entities.ParallaxBackground;
import rickelectric.game.chosen.entities.PlayerID;
import rickelectric.game.chosen.entities.Sprite;

public class SelectScreen implements GameScreen {

	private PlayerID selectedPlayer;
	Rectangle2D[][] playerRects;
	private Sprite[][] players;
	private PlayerID[][] ids;
	private DualSprite back, begin;

	int selX, selY;

	private ParallaxBackground bg;
	private long lp;

	public SelectScreen() {
		selectedPlayer = PlayerID.Man1;
		playerRects = new Rectangle2D[2][4];
		players = new Sprite[2][4];
		ids = new PlayerID[2][4];
		selX = 0;
		selY = 0;
		lp = System.currentTimeMillis();
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
		bg.draw(g2d);
		g2d.setFont(AssetManager.getInstance().getFont()
				.deriveFont(Font.BOLD, 40));
		g2d.drawString("SELECT YOUR PLAYER", Globals.SCREEN_WIDTH / 2
				- g2d.getFontMetrics().stringWidth("SELECT YOUR PLAYER") / 2,
				45);

		g2d.setFont(AssetManager.getInstance().getFont()
				.deriveFont(Font.BOLD, 20));
		for (int i = 0; i < players.length; i++) {
			for (int j = 0; j < players[i].length; j++) {
				g2d.setColor((i == selY && j == selX) ? Color.red
						: ids[i][j] == selectedPlayer ? Color.green
								: Color.black);
				g2d.fill(playerRects[i][j]);
				players[i][j].draw(g2d);
			}
		}

		g2d.setColor(Color.black);
		g2d.setFont(AssetManager.getInstance().getFont()
				.deriveFont(Font.BOLD, 30));
		g2d.drawString(selectedPlayer.getPlayerName(), 110, 100);
		back.draw(g2d);
		begin.draw(g2d);
	}

	@Override
	public void update() {
		bg.update();
		if (System.currentTimeMillis()
				- GameSystem.getInstance().lastSwitchTime() < 300)
			return;
		if (KeyboardInputService.getInstance().isEnter()) {
			if (System.currentTimeMillis() - lp > 200) {
				invokeButtonAction();
			}
		}
		if (KeyboardInputService.getInstance().isUp()) {
			if (System.currentTimeMillis() - lp > 200) {
				selY--;
				if (selY < 0) {
					selY = 2;
					selX = selX % 2;
				}
				lp = System.currentTimeMillis();
			}
		} else if (KeyboardInputService.getInstance().isDown()) {
			if (System.currentTimeMillis() - lp > 200) {
				selY++;
				selY %= 3;
				if (selY == 2) {
					selX = selX % 2;
				}
				lp = System.currentTimeMillis();
			}
		}
		if (KeyboardInputService.getInstance().isLeft()) {
			if (System.currentTimeMillis() - lp > 200) {
				selX--;
				if (selX < 0) {
					if (selY == 2)
						selX = 1;
					else
						selX = 3;
				}
				lp = System.currentTimeMillis();
			}
		}
		if (KeyboardInputService.getInstance().isRight()) {
			if (System.currentTimeMillis() - lp > 200) {
				selX++;
				selX %= selY == 2 ? 2 : 4;
				lp = System.currentTimeMillis();
			}
		}

		MouseInputService mis = MouseInputService.getInstance();
		Point2D p = new Point2D.Float(mis.getMouseX(), mis.getMouseY());
		if (back.getBoundingRect().contains(p)) {
			selX = 0;
			selY = 2;
			if (mis.buttonL())
				invokeButtonAction();
		}
		if (begin.getBoundingRect().contains(p)) {
			selX = 1;
			selY = 2;
			if (mis.buttonL())
				invokeButtonAction();
		}
		outer: for (int i = 0; i < players.length; i++) {
			for (int j = 0; j < players[i].length; j++) {
				Rectangle2D r = playerRects[i][j];
				if (r.getBounds().contains(p)) {
					selX = j;
					selY = i;
					if (mis.buttonL())
						invokeButtonAction();
					break outer;
				}
			}
		}

		for (int i = 0; i < players.length; i++) {
			for (int j = 0; j < players[i].length; j++) {
				players[i][j].update();
			}
		}

		back.setActiveImage((selY == 2 && selX == 0) ? 1 : 2);
		begin.setActiveImage((selY == 2 && selX == 1) ? 1 : 2);

	}

	private void invokeButtonAction() {
		if (selY == 2) {
			if (selX == 0)
				GameSystem.getInstance().changeScreen(GameSystem.START_SCREEN);
			else
				GameSystem.getInstance().changeScreen(GameSystem.LEVEL_1_START);
		} else {
			selectedPlayer = ids[selY][selX];
			if(GameSystem.getInstance().getPlayerID() == selectedPlayer) return;
			GameSystem.getInstance().setPlayer(selectedPlayer);
		}
	}

	@Override
	public void loadScreen() {
		bg = new ParallaxBackground(GameSystem.getInstance(), "JungleSunset",
				-2f);

		int sx = 110, sy = 110, sw = 200, sh = 200;

		PlayerID player = PlayerID.Man1;

		ids[0][0] = player;
		playerRects[0][0] = new Rectangle2D.Double(sx, sy, sw, sh);
		players[0][0] = new AnimatedSprite(player.getImageL(),
				(float) (playerRects[0][0].getX() + 25),
				(float) (playerRects[0][0].getY() + 15), player.getNumFrames(),
				40);

		player = PlayerID.Woman1;
		ids[0][1] = player;
		playerRects[0][1] = new Rectangle2D.Double(sx + (sw + 10), sy, sw, sh);
		players[0][1] = new AnimatedSprite(player.getImageR(),
				(float) (playerRects[0][1].getX() + 45),
				(float) (playerRects[0][1].getY() + 15), player.getNumFrames(),
				40);

		player = PlayerID.Man2;
		ids[0][2] = player;
		playerRects[0][2] = new Rectangle2D.Double(sx + 2 * (sw + 10), sy, sw,
				sh);
		players[0][2] = new AnimatedSprite(player.getImageR(),
				(float) (playerRects[0][2].getX() + 55),
				(float) (playerRects[0][2].getY() + 15), player.getNumFrames(),
				40);

		player = PlayerID.Ghost;
		ids[0][3] = player;
		playerRects[0][3] = new Rectangle2D.Double(sx + 3 * (sw + 10), sy, sw,
				sh);
		players[0][3] = new AnimatedSprite(player.getImageR(),
				(float) (playerRects[0][3].getX() + 55),
				(float) (playerRects[0][3].getY() + 15), player.getNumFrames(),
				40);

		player = PlayerID.Woman2;
		ids[1][0] = player;
		playerRects[1][0] = new Rectangle2D.Double(sx, sy + (sh + 10), sw, sh);
		players[1][0] = new AnimatedSprite(player.getImageR(),
				(float) (playerRects[1][0].getX() + 55),
				(float) (playerRects[1][0].getY() + 15), player.getNumFrames(),
				40);

		player = PlayerID.Woman3;
		ids[1][1] = player;
		playerRects[1][1] = new Rectangle2D.Double(sx + (sw + 10), sy
				+ (sh + 10), sw, sh);
		players[1][1] = new AnimatedSprite(player.getImageL(),
				(float) (playerRects[1][1].getX() + 55),
				(float) (playerRects[1][1].getY() + 15), player.getNumFrames(),
				40);

		player = PlayerID.Woman;
		ids[1][2] = player;
		playerRects[1][2] = new Rectangle2D.Double(sx + 2 * (sw + 10), sy
				+ (sh + 10), sw, sh);
		players[1][2] = new AnimatedSprite(player.getImageL(),
				(float) (playerRects[1][2].getX() + 55),
				(float) (playerRects[1][2].getY() + 25), player.getNumFrames(),
				40);

		player = PlayerID.Man3;
		ids[1][3] = player;
		playerRects[1][3] = new Rectangle2D.Double(sx + 3 * (sw + 10), sy
				+ (sh + 10), sw, sh);
		players[1][3] = new AnimatedSprite(player.getImageR(),
				(float) (playerRects[1][3].getX() + 25),
				(float) (playerRects[1][3].getY() + 25), player.getNumFrames(),
				40);

		back = new DualSprite("Buttons/BackActive", "Buttons/BackInactive",
				110, 550, 1f);
		back.setActiveImage(2);

		begin = new DualSprite("Buttons/BeginActive", "Buttons/BeginInactive",
				330, 550, 1f);
		begin.setActiveImage(2);

	}

	public PlayerID getSelectedPlayer() {
		return selectedPlayer;
	}

}
