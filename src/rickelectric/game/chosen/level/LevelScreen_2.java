package rickelectric.game.chosen.level;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import rickelectric.game.chosen.AssetManager;
import rickelectric.game.chosen.BackgroundManager;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.KeyboardInputService;
import rickelectric.game.chosen.entities.CatEyes;
import rickelectric.game.chosen.entities.DualSprite;
import rickelectric.game.chosen.entities.Key;
import rickelectric.game.chosen.entities.PlayerID;
import rickelectric.game.chosen.entities.Portal;
import rickelectric.game.chosen.entities.SparkBall;
import rickelectric.game.chosen.entities.enemies.Dragon;
import rickelectric.game.chosen.entities.enemies.LightningDragon;
import rickelectric.game.chosen.entities.enemies.MiniDragon;
import rickelectric.game.chosen.entities.enemies.WaterDragon;
import rickelectric.game.chosen.level.tilemap.LevelMap;
import rickelectric.game.chosen.screens.LevelScreen;
import rickelectric.game.chosen.screens.LoadingScreen;

public class LevelScreen_2 extends LevelScreen {

	private BackgroundManager background;

	private LevelMap map;
	private Camera camera;

	private String notifyText;
	private long notifyTime;

	private CatEyes portalOpen;
	private Portal exitPortal;

	private ArrayList<Key> keys;

	private double scaler;

	private long suspendTime;

	private String mapName;

	private DualSprite resume, endgame;
	private String levelName;
	private boolean levelEnding = false;

	private Point[] coinLocs;

	public LevelScreen_2(PlayerID playerID) {
		super(playerID);
		this.mapName = "DarkPlanet";
		this.levelName = "Dreamland: The Impossible Planet";

		scaler = 0.8;

		notifyTime = System.currentTimeMillis();
		notifyText = "";
	}

	@Override
	public void loadScreen() {
		LoadingScreen.getInstance().loading(GameSystem.LEVEL_2);
		GameSystem.getInstance().changeScreen(GameSystem.LOADING_SCREEN);

		new Thread(new Runnable() {
			public void run() {

				LoadingScreen.getInstance().setText("Loading Level 2...");
				LoadingScreen.getInstance().setPercent(0);
				background = new BackgroundManager(GameSystem.getInstance(), 2);
				LoadingScreen.getInstance().setPercent(10);
				LevelScreen_2.super.loadScreen();

				LoadingScreen.getInstance().setText("Loading Level Map...");
				map = new LevelMap(mapName);
				LoadingScreen.getInstance().setPercent(25);

				camera = new Camera();

				keys = new ArrayList<Key>();
				populateCoins();
				LoadingScreen.getInstance().setPercent(35);

				portalOpen = new CatEyes(
						2 * Globals.MAP.getMapData().tilewidth, 3 * Globals.MAP
								.getMapData().tileheight);

				exitPortal = new Portal(1 * Globals.MAP.getMapData().tilewidth,
						19 * Globals.MAP.getMapData().tileheight + 20);
				LoadingScreen.getInstance().setPercent(50);

				Point p;

				LightningDragon d;
				WaterDragon wd;
				MiniDragon md;
				
				p = Globals.MAP.getXYOf(3, 25);
				wd = new WaterDragon(p.x, p.y);
				//wd.bind(p.x, p.x + 1024);
				getDragons().add(wd);
				
				p = Globals.MAP.getXYOf(10, 14);
				p.y+=50;
				md = new MiniDragon(p.x, p.y);
				//md.bind(p.x, p.x + 1024);
				getDragons().add(md);
				
				p = Globals.MAP.getXYOf(3, 3);
				d = new LightningDragon(p.x, p.y);
				d.bind(p.x, p.x + 1024);
				getDragons().add(d);
				LoadingScreen.getInstance().setPercent(60);

				p = Globals.MAP.getXYOf(24, 27);
				d = new LightningDragon(p.x, p.y);
				d.bind(p.x, p.x + 1024);
				getDragons().add(d);
				LoadingScreen.getInstance().setPercent(70);

				p = Globals.MAP.getXYOf(2, 27);
				d = new LightningDragon(p.x, p.y);
				d.bind(p.x, p.x + 1024);
				getDragons().add(d);
				LoadingScreen.getInstance().setPercent(80);

				p = Globals.MAP.getXYOf(33, 23);
				d = new LightningDragon(p.x, p.y);
				d.bind(p.x, p.x + 1024);
				getDragons().add(d);
				LoadingScreen.getInstance().setPercent(70);

				int centerX = Globals.SCREEN_WIDTH / 2;
				resume = new DualSprite("Buttons/ResumeActive",
						"Buttons/ResumeInactive", centerX - 105, 100, 0.7f);
				endgame = new DualSprite("Buttons/QuitLevelActive",
						"Buttons/QuitLevelInactive", centerX - 105, 160, 0.7f);
				endgame.setActiveImage(2);
				LoadingScreen.getInstance().setPercent(90);

				// Follow Player
				camera.followEntity(getPlayer());

				suspendTime = System.currentTimeMillis();

				// GameSystem.getInstance().changeScreen(GameSystem.LEVEL_2);
				sendNotify("Find Eyes That Open The Exit Portal");
				LoadingScreen.getInstance().setPercent(100);

			}
		}).start();
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
		super.setTotalCollectables(keys.size());

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

			for (Dragon d : getDragons())
				d.draw(g2d);
			getPlayer().draw(g2d);

			for (Key c : keys) {
				c.draw(g2d);
			}
			for (SparkBall o : getBullets()) {
				o.draw(g2d);
			}
			megaDraw(g2d);
		}
		camera.drawCameraStop(g2d, scaler);

		// Reset Zoom for OSD draw
		Globals.SCREEN_HEIGHT = sh;
		Globals.SCREEN_WIDTH = sw;
		g2d.scale(1 / scaler, 1 / scaler);

		if (getPlayer().getBoundingRect().intersects(
				exitPortal.getBoundingRect())) {
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

		super.drawStats(g2d, "Keys");
		drawNotify(g2d);
		if (isPaused()) {
			drawPaused(g2d);
		}
		if (System.currentTimeMillis() - suspendTime <= 2000) {
			drawIntro(g2d);
		}

//		g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
//		g2d.setColor(Color.WHITE);
//		g2d.drawString("Zoom: " + scaler + ", Camera X: " + camera.getCameraX()
//				+ ", Camera Y: " + camera.getCameraY(), 10, 10);
//
//		g2d.setColor(Color.white);
//		g2d.drawString("State: " + getPlayer().getPlayerState(), 10, 30);
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

	@Override
	public void update() {
		if (System.currentTimeMillis()
				- GameSystem.getInstance().lastSwitchTime() < 300)
			return;

		super.updatePause();
		
		if (isPaused()) {
			return;
		}

		exitPortal.update();

		if (levelEnding) {
			if (exitPortal.getState() == Portal.CLOSED) {
				GameSystem.getInstance().changeScreen(GameSystem.VORTEX);
			}
			return;
		}

		if (getPlayer().getBoundingRect().intersects(
				exitPortal.getBoundingRect())) {
			if (exitPortal.getState() == Portal.OPEN) {
				if (KeyboardInputService.getInstance().isUp()) {
					getPlayer().setVisible(false);
					exitPortal.setState(Portal.CLOSING);
					GameSystem
							.getInstance()
							.getVortexScreen()
							.setCenter(
									exitPortal.getBoundingRect().x
											- camera.getCameraX()
											+ exitPortal.getBoundingRect().width
											/ 4,
									exitPortal.getBoundingRect().y
											- camera.getCameraY()
											+ exitPortal.getBoundingRect().height
											/ 4);
					levelEnding = true;
				}
			}
		}

		megaUpdate();
		if (getPlayer().isMega()) {
			getPlayer().update();
			return;
		}

		background.update();
		camera.updateCamera();
		getPlayer().update();

		if (getPlayer().getY() > Globals.WORLD_HEIGHT) {
			lifeLost();
		}

		map.update();

		/*
		 * if (KeyboardInputService.getInstance().isPageUp()) { if (scaler <
		 * 1.8) scaler += 0.2; } else if
		 * (KeyboardInputService.getInstance().isPageDown()) { if (scaler > 0.6)
		 * scaler -= 0.2; }
		 */

		if (getPlayer().getLightning().isVisible()) {
			if (portalOpen.getBoundingRect().intersects(
					getPlayer().getLightning().getBoundingRect())) {
				boolean openActive = true;
				for (Dragon d : getDragons()) {
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

		for (Dragon d : getDragons())
			if (d.isVisible())
				d.update();

		Iterator<Key> ci = keys.iterator();
		while (ci.hasNext()) {
			Key c = ci.next();
			c.update();
			if (c.getBoundingRect().intersects(getPlayer().getBoundingRect())) {
				setNumCollectables(getNumCollectables() + 1);
				ci.remove();
			}
		}

		super.updateBullets();
	}

	public BackgroundManager getBackground() {
		return background;
	}

	public void reset() {
		setLives(3);
		setHp(200);
		setNumCollectables(0);

		levelEnding = false;

		super.reset();
	}

	public double getScaler() {
		return scaler;
	}

	@Override
	public int getScreenID() {
		return GameSystem.LEVEL_2;
	}
	
	@Override
	public Camera getCamera() {
		return camera;
	}
}
