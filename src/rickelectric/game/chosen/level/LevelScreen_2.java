package rickelectric.game.chosen.level;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import rickelectric.game.chosen.AssetManager;
import rickelectric.game.chosen.BackgroundManager;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.KeyboardInputService;
import rickelectric.game.chosen.entities.Bullet;
import rickelectric.game.chosen.entities.BulletPool;
import rickelectric.game.chosen.entities.CatEyes;
import rickelectric.game.chosen.entities.Dragon;
import rickelectric.game.chosen.entities.DualSprite;
import rickelectric.game.chosen.entities.Key;
import rickelectric.game.chosen.entities.Player;
import rickelectric.game.chosen.entities.PlayerID;
import rickelectric.game.chosen.entities.Portal;
import rickelectric.game.chosen.level.tilemap.LevelMap;
import rickelectric.game.chosen.level.tilemap.Tile;
import rickelectric.game.chosen.screens.LevelScreen;

public class LevelScreen_2 extends LevelScreen {

	private boolean paused;
	private BackgroundManager background;

	private LevelMap map;
	private Camera camera;

	private Player player;

	private String notifyText;
	private long notifyTime;

	private CatEyes portalOpen;
	private Portal exitPortal;

	private Dragon[] dragon;
	private ArrayList<Key> keys;

	private double scaler;

	private int numKeys;
	private int hp;

	private long suspendTime, pauseDelay, keyDelay;

	private String mapName;

	private DualSprite resume, endgame;
	private String levelName;
	private boolean levelEnding = false;
	private ArrayList<Bullet> removal;
	private Point[] coinLocs;

	public LevelScreen_2(PlayerID playerID) {
		super(playerID);
		this.mapName = "DarkPlanet";
		this.levelName = "Dreamland: The Impossible Planet";

		scaler = 0.8;
		numKeys = 0;
		hp = 200;

		paused = false;
		pauseDelay = notifyTime = System.currentTimeMillis();
		notifyText = "";
	}

	@Override
	public void loadScreen() {
		background = new BackgroundManager(GameSystem.getInstance(), 2);

		super.loadScreen();
		map = new LevelMap(mapName);

		camera = new Camera();

		keys = new ArrayList<Key>();
		populateCoins();

		portalOpen = new CatEyes(2 * Globals.MAP.getMapData().tilewidth,
				3 * Globals.MAP.getMapData().tileheight);

		exitPortal = new Portal(1 * Globals.MAP.getMapData().tilewidth,
				19 * Globals.MAP.getMapData().tileheight + 20);

		Point p;
		dragon = new Dragon[4];
		p = Globals.MAP.getXYOf(3, 3);
		dragon[0] = new Dragon(p.x, p.y);
		dragon[0].bind(p.x, p.x + 1024);

		p = Globals.MAP.getXYOf(24, 27);
		dragon[1] = new Dragon(p.x, p.y);
		dragon[1].bind(p.x, p.x + 1024);

		p = Globals.MAP.getXYOf(2, 27);
		dragon[2] = new Dragon(p.x, p.y);
		dragon[2].bind(p.x, p.x + 1024);

		p = Globals.MAP.getXYOf(33, 23);
		dragon[3] = new Dragon(p.x, p.y);
		dragon[3].bind(p.x, p.x + 1024);

		int centerX = Globals.SCREEN_WIDTH / 2;
		resume = new DualSprite("Buttons/ResumeActive",
				"Buttons/ResumeInactive", centerX - 105, 100, 0.7f);
		endgame = new DualSprite("Buttons/QuitLevelActive",
				"Buttons/QuitLevelInactive", centerX - 105, 160, 0.7f);
		endgame.setActiveImage(2);

		// Follow Player
		camera.followEntity(player);

		suspendTime = System.currentTimeMillis();

		GameSystem.getInstance().changeScreen(GameSystem.LEVEL_2);
		sendNotify("Find Eyes That Open The Exit Portal");
	}

	private void populateCoins() {
		coinLocs = new Point[21];
		coinLocs[0] = Globals.MAP.getXYOf(2, 3);
		coinLocs[1] = Globals.MAP.getXYOf(16, 8);
		coinLocs[2] = Globals.MAP.getXYOf(17, 8);
		coinLocs[3] = Globals.MAP.getXYOf(18, 8);
		coinLocs[4] = Globals.MAP.getXYOf(19, 8);
		coinLocs[5] = Globals.MAP.getXYOf(6, 11);
		coinLocs[6] = Globals.MAP.getXYOf(33, 14);
		coinLocs[7] = Globals.MAP.getXYOf(34, 14);
		coinLocs[8] = Globals.MAP.getXYOf(37, 13);
		coinLocs[9] = Globals.MAP.getXYOf(38, 13);
		coinLocs[10] = Globals.MAP.getXYOf(32, 9);
		coinLocs[11] = Globals.MAP.getXYOf(33, 9);
		coinLocs[12] = Globals.MAP.getXYOf(34, 9);
		coinLocs[13] = Globals.MAP.getXYOf(39, 22);
		coinLocs[14] = Globals.MAP.getXYOf(40, 22);
		coinLocs[15] = Globals.MAP.getXYOf(28, 24);
		coinLocs[16] = Globals.MAP.getXYOf(29, 24);
		coinLocs[17] = Globals.MAP.getXYOf(6, 19);
		coinLocs[18] = Globals.MAP.getXYOf(7, 19);
		coinLocs[19] = Globals.MAP.getXYOf(8, 19);
		coinLocs[20] = Globals.MAP.getXYOf(9, 19);

		for (Point p : coinLocs) {
			keys.add(new Key(p.x, p.y));
		}

	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, Globals.SCREEN_HEIGHT, Globals.SCREEN_HEIGHT);

		// draw background
		background.draw(g2d);

		// Zoom In/Out
		g2d.scale(scaler, scaler);
		int sw = Globals.SCREEN_WIDTH;
		int sh = Globals.SCREEN_HEIGHT;

		Globals.SCREEN_WIDTH = (int) (Globals.SCREEN_WIDTH / scaler);
		Globals.SCREEN_HEIGHT = (int) (Globals.SCREEN_HEIGHT / scaler);

		camera.drawCameraStart(g2d, scaler);
		{
			// draw map
			map.draw(g2d);

			portalOpen.draw(g2d);
			exitPortal.draw(g2d);

			for (Dragon d : dragon)
				d.draw(g2d);
			player.draw(g2d);

			for (Key c : keys) {
				c.draw(g2d);
			}
			for (Bullet o : getBullets()) {
				o.draw(g2d);
			}
		}
		camera.drawCameraStop(g2d, scaler);

		// Reset Zoom for OSD draw
		Globals.SCREEN_HEIGHT = sh;
		Globals.SCREEN_WIDTH = sw;
		g2d.scale(1 / scaler, 1 / scaler);

		if (player.getBoundingRect().intersects(exitPortal.getBoundingRect())) {
			String str;
			if (exitPortal.getState() == Portal.OPEN)
				str = "Press Up To Enter The Portal";
			else
				str = "The Portal Must Be Open Before You Can Enter";
			g2d.setFont(AssetManager.getInstance().getFont()
					.deriveFont(Font.BOLD | Font.ITALIC, 30));
			int fm = g2d.getFontMetrics().stringWidth(str);
			g2d.setColor(Color.black);
			g2d.fillRect(Globals.SCREEN_WIDTH / 2 - fm / 2, 265, fm + 10, 40);
			g2d.setColor(Color.green);
			g2d.drawString(str, Globals.SCREEN_WIDTH / 2 - fm / 2 - 5, 300);
		}

		drawLifeCoinsAndEnergy(g2d);
		drawNotify(g2d);
		if (paused) {
			drawPaused(g2d);
		}
		if (System.currentTimeMillis() - suspendTime <= 2000) {
			drawIntro(g2d);
		}

		g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		g2d.setColor(Color.WHITE);
		g2d.drawString("Zoom: " + scaler + ", Camera X: " + camera.getCameraX()
				+ ", Camera Y: " + camera.getCameraY(), 10, 10);

		g2d.setColor(Color.white);
		g2d.drawString("State: " + player.getPlayerState(), 10, 30);
	}

	private void drawPaused(Graphics2D g2d) {
		String str = "PAUSED";
		g2d.setColor(Color.green);
		g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 60));
		int centerX = Globals.SCREEN_WIDTH / 2
				- g2d.getFontMetrics().stringWidth(str) / 2;
		int centerY = Globals.SCREEN_HEIGHT / 2 - +30;
		g2d.drawString(str, centerX, centerY);

		resume.draw(g2d);
		endgame.draw(g2d);
	}

	private void drawIntro(Graphics2D g2d) {
		String str = levelName;
		g2d.setColor(Color.green);
		g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 40 + (int) ((System
				.currentTimeMillis() - suspendTime) / 100)));
		int centerX = Globals.SCREEN_WIDTH / 2
				- g2d.getFontMetrics().stringWidth(str) / 2;
		int centerY = Globals.SCREEN_HEIGHT / 2 - +25;
		g2d.drawString(str, centerX, centerY);
	}

	public void sendNotify(String s) {
		this.notifyText = s;
		this.notifyTime = System.currentTimeMillis();
	}

	public void drawNotify(Graphics2D g2d) {
		if (notifyText.equals(""))
			return;
		if (System.currentTimeMillis() - notifyTime > 4000)
			return;
		g2d.setFont(AssetManager.getInstance().getFont()
				.deriveFont(Font.BOLD, 50));
		int fm = g2d.getFontMetrics().stringWidth(notifyText);
		int centerX = Globals.SCREEN_WIDTH / 2 - fm / 2;
		int centerY = Globals.SCREEN_HEIGHT / 2 - 25;
		g2d.setColor(Color.black);
		g2d.fillRect(centerX - 5, centerY - 5, fm + 10, 60);
		g2d.setColor(Color.blue);
		g2d.drawString(notifyText, centerX, centerY + 45);
	}

	private void drawLifeCoinsAndEnergy(Graphics2D g2d) {
		int lifeX = Globals.SCREEN_WIDTH - 240;
		int lifeY = 20;

		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(hp < 20 ? Color.red : hp < 100 ? Color.magenta
				: Color.green);
		g2d.drawRect(lifeX, lifeY, 200, 40);
		g2d.fillRect(lifeX, lifeY, hp, 40);
		g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
		g2d.drawString(getLives() + " Lives", lifeX - 100, lifeY + 30);

		g2d.setColor(player.getEnergy() < 20 ? Color.red
				: player.getEnergy() < 100 ? Color.orange : Color.yellow);

		g2d.drawString(player.getEnergy() + " Energy", lifeX - 370, lifeY + 75);
		g2d.drawRect(lifeX-200, lifeY + 45, 400, 40);
		g2d.fillRect(lifeX-200, lifeY + 45, player.getEnergy(), 40);

		g2d.setColor(Color.yellow);
		g2d.drawString(numKeys + "/" + coinLocs.length + " Keys", lifeX,
				lifeY + 125);
		Image img = AssetManager.getInstance().getImage("KeySingle.png");
		g2d.drawImage(img, lifeX - 72, lifeY + 80, null);
	}

	@Override
	public void update() {
		if (System.currentTimeMillis()
				- GameSystem.getInstance().lastSwitchTime() < 300)
			return;

		if (paused) {
			if (KeyboardInputService.getInstance().isUp()
					|| KeyboardInputService.getInstance().isDown()) {
				if (System.currentTimeMillis() - keyDelay > 200) {
					resume.setActiveImage(resume.getActiveImage() == 1 ? 2 : 1);
					endgame.setActiveImage(resume.getActiveImage() == 1 ? 2 : 1);
					keyDelay = System.currentTimeMillis();
				}
			}
		}

		if (KeyboardInputService.getInstance().isEnter()
				&& System.currentTimeMillis() - pauseDelay > 200) {
			if (System.currentTimeMillis() - keyDelay > 200) {
				if (resume.getActiveImage() == 1) {
					pause();
					pauseDelay = keyDelay = System.currentTimeMillis();
				} else {
					GameSystem.getInstance().changeScreen(
							GameSystem.START_SCREEN);
				}
			}
		}

		if (KeyboardInputService.getInstance().isEscape()
				&& System.currentTimeMillis() - pauseDelay > 200) {
			pause();
			pauseDelay = System.currentTimeMillis();
		}

		if (paused) {
			return;
		}

		exitPortal.update();

		if (levelEnding) {
			if (exitPortal.getState() == Portal.CLOSED) {
				GameSystem.getInstance().changeScreen(GameSystem.VORTEX);
			}
			return;
		}

		if (player.getBoundingRect().intersects(exitPortal.getBoundingRect())) {
			if (exitPortal.getState() == Portal.OPEN) {
				if (KeyboardInputService.getInstance().isUp()) {
					player.setVisible(false);
					exitPortal.setState(Portal.CLOSING);
					GameSystem
							.getInstance()
							.getVortexScreen()
							.setCenter(
									exitPortal.getBoundingRect().x
											- camera.getCameraX()
											+ exitPortal.getBoundingRect().width / 4,
									exitPortal.getBoundingRect().y
											- camera.getCameraY()
											+ exitPortal.getBoundingRect().height
											/ 4);
					levelEnding = true;
				}
			}
		}

		background.update();
		camera.updateCamera();
		player.update();

		if (player.getY() > Globals.WORLD_HEIGHT) {
			lifeLost();
		}

		map.update();

		/*
		 * if (KeyboardInputService.getInstance().isPageUp()) { if (scaler <
		 * 1.8) scaler += 0.2; } else if
		 * (KeyboardInputService.getInstance().isPageDown()) { if (scaler > 0.6)
		 * scaler -= 0.2; }
		 */

		if (player.getLightning().isVisible()) {
			if (portalOpen.getBoundingRect().intersects(
					player.getLightning().getBoundingRect())) {
				boolean openActive = true;
				for (Dragon d : dragon) {
					if (d.isVisible()) {
						sendNotify("Kill All Dragons, Then Return Here");
						openActive = false;
					}
				}
				if (openActive) {
					sendNotify("Portal Is Open. Get To It ASAP.");
					portalOpen.setActiveImage(2);
					exitPortal.setState(Portal.OPENING);
				}
			}
		}

		for (Dragon d : dragon)
			if (d.isVisible())
				d.update();

		Iterator<Key> ci = keys.iterator();
		while (ci.hasNext()) {
			Key c = ci.next();
			c.update();
			if (c.getBoundingRect().intersects(player.getBoundingRect())) {
				numKeys++;
				ci.remove();
			}
		}

		removal = new ArrayList<Bullet>();
		for (Bullet b : getBullets()) {
			b.update();
			if (b.getX() < camera.getCameraX()
					|| b.getX() > camera.getCameraX() + Globals.SCREEN_WIDTH
							/ scaler
					|| b.getY() < camera.getCameraY()
					|| b.getY() > camera.getCameraY() + Globals.SCREEN_HEIGHT
							/ scaler) {
				removal.add(b);
			} else if (player.getLightning().isVisible()
					&& b.getBoundingRect()
							.intersects(player.getLightning().getBoundingRect())
					&& !b.getSource().equals(player)) {
				removal.add(b);
			} else if (b.getBoundingRect().intersects(player.getBoundingRect())
					&& !b.getSource().equals(player)) {
				removal.add(b);
				decreaseHP(20);
			} else {
				for (Dragon d : dragon) {
					if (b.getBoundingRect().intersects(d.getBoundingRect())
							&& b.getSource().equals(player)) {
						d.attacked();
						d.decreaseHP(20);
						removal.add(b);
					}
				}
			}
			if (!removal.contains(b)) {
				int myX = (int) (Math.ceil((b.getBoundingRect().x + b.getBoundingRect()
						.getWidth()) / Globals.MAP.getMapData().tilewidth));
				int myY = (int) (Math.ceil((b.getBoundingRect().y + b.getBoundingRect()
						.getHeight()) / Globals.MAP.getMapData().tileheight));
				for (int i = 2; i < Globals.MAP.getTileLayerCount(); i++) {
					Tile t = Globals.MAP.getTileAt(i, myX, myY);
					if (t == null)
						continue;
					if (b.getBoundingRect().intersects(t.getBoundingRect())) {
						removal.add(b);
					}
				}
			}
		}

		for (Bullet b : removal) {
			BulletPool.getInstance().addReusable(b);
			getBullets().remove(b);
		}
		removal.removeAll(removal);
	}

	private void pause() {
		paused = !paused;
		if (paused) {
			// SoundManager.getInstance().stopAll();
		} else {
			// SoundManager.getInstance().playSound("xfiles", true);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public BackgroundManager getBackground() {
		return background;
	}

	public void decreaseHP(int hp) {
		this.hp -= hp;
		if (this.hp < 0) {
			lifeLost();
		}
	}

	public void reset() {
		setLives(3);
		hp = 200;
		numKeys=0;

		levelEnding = false;

		paused = false;
		pauseDelay = System.currentTimeMillis();
	}

	public double getScaler() {
		return scaler;
	}
}
