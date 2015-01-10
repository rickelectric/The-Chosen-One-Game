package rickelectric.game.chosen.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;

import rickelectric.game.chosen.BackgroundManager;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.KeyboardInputService;
import rickelectric.game.chosen.MouseInputService;
import rickelectric.game.chosen.entities.DualSprite;
import rickelectric.game.chosen.entities.Entity;
import rickelectric.game.chosen.entities.Player;
import rickelectric.game.chosen.entities.PlayerID;
import rickelectric.game.chosen.entities.Sprite;
import rickelectric.game.chosen.entities.attacks.SparkBall;
import rickelectric.game.chosen.entities.attacks.SparkPool;
import rickelectric.game.chosen.entities.enemies.Dragon;
import rickelectric.game.chosen.level.Camera;
import rickelectric.game.chosen.level.tilemap.Tile;
import rickelectric.game.chosen.sounds.SoundManager;

public abstract class LevelScreen implements GameScreen {

	private ArrayList<SparkBall> sparkBalls;
	private ArrayList<Dragon> dragons;

	private DualSprite resume, endgame;

	private int lives;
	private int hp;
	private int numCollectables, totalCollectables;

	private PlayerID playerID;
	private Player player;
	private boolean paused;
	private long keyDelay;
	private long pauseDelay;
	private Sprite rnBtn;
	private boolean camFreeze;
	private int cpd;

	public LevelScreen(PlayerID playerID) {
		this.playerID = playerID;

		dragons = new ArrayList<Dragon>();
		sparkBalls = new ArrayList<SparkBall>();
		removal = new ArrayList<SparkBall>();

		lives = 3;
		hp = 200;
		numCollectables = 0;
		cpd=0;
	}

	public void lifeLost() {
		if (lives > 1) {
			sendNotify("You Died");
			player.setX(385);
			player.setY(2432);
			player.update();
			freezeNextCameraUpdate();
			getCamera().updateCamera(false);
			lives--;
			hp = 200;
		} else {
			lives = 0;
			GameSystem.getInstance().changeScreen(GameSystem.GAME_OVER);
		}
		getBackground().reset();
	}

	public void freezeNextCameraUpdate() {
		cpd=0;
		this.camFreeze=true;
	}
	
	public boolean unfreezeCamera(){
		boolean tcf = this.camFreeze==true;
		if(cpd++>2)this.camFreeze=false;
		return tcf;
	}

	public abstract void sendNotify(String text);

	public void setPlayer(PlayerID playerID) {
		this.playerID = playerID;
		player = new Player(playerID, 385, 2432);
	}

	public Player getPlayer() {
		return player;
	}

	public void decreaseHP(int hp) {
		this.hp -= hp;
		if (this.hp < 0) {
			lifeLost();
		}
	}

	public void updatePause() {
		if (paused) {
			if (KeyboardInputService.getInstance().isUp()
					|| KeyboardInputService.getInstance().isDown()) {
				if (System.currentTimeMillis() - keyDelay > 200) {
					resume.setActiveImage(resume.getActiveImage() == 1 ? 2 : 1);
					endgame.setActiveImage(resume.getActiveImage() == 1 ? 2 : 1);
					keyDelay = System.currentTimeMillis();
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

			if (resume.getBoundingRect().contains(
					MouseInputService.getInstance().getMouseX(),
					MouseInputService.getInstance().getMouseY())) {
				resume.setActiveImage(1);
				endgame.setActiveImage(2);
				if (MouseInputService.getInstance().buttonL()) {
					pause();
					pauseDelay = keyDelay = System.currentTimeMillis();
				}
			}
			if (endgame.getBoundingRect().contains(
					MouseInputService.getInstance().getMouseX(),
					MouseInputService.getInstance().getMouseY())) {
				endgame.setActiveImage(1);
				resume.setActiveImage(2);
				if (MouseInputService.getInstance().buttonL()) {
					GameSystem.getInstance().changeScreen(
							GameSystem.START_SCREEN);
				}
			}
		}
		
		if(rnBtn.getBoundingRect().contains(MouseInputService.getInstance().getMouseX(),
				MouseInputService.getInstance().getMouseY())){
			if(MouseInputService.getInstance().buttonL() && !paused){
				pause();
				pauseDelay = keyDelay = System.currentTimeMillis();
			}
		}

		if (KeyboardInputService.getInstance().isEscape()
				&& System.currentTimeMillis() - pauseDelay > 200) {
			pause();
			pauseDelay = System.currentTimeMillis();
		}
	}

	private void pause() {
		paused = !paused;
		if (paused) {
			SoundManager.getInstance().stopAll();
		} else {
			GameSystem.getInstance().soundResume();
		}
	}

	public void createControls() {
		int centerX = Globals.SCREEN_WIDTH / 2;
		resume = new DualSprite("Buttons/ResumeActive",
				"Buttons/ResumeInactive", centerX - 105, 100, 0.7f);
		endgame = new DualSprite("Buttons/QuitLevelActive",
				"Buttons/QuitLevelInactive", centerX - 105, 160, 0.7f);
		endgame.setActiveImage(2);
		rnBtn = new Sprite("RickNewBtn",Globals.SCREEN_WIDTH-70,Globals.SCREEN_HEIGHT-70);
	}

	public void drawControls(Graphics2D g2d) {
		resume.draw(g2d);
		endgame.draw(g2d);
	}

	public abstract BackgroundManager getBackground();

	public void loadScreen() {
		setPlayer(playerID);

		sparkBalls.removeAll(sparkBalls);
		dragons.removeAll(dragons);

		// Initialize 20 Bullets To Pool To Increase Speed In Game.
		for (int i = 0; i < 20; i++)
			SparkPool.getInstance().addReusable(
					new SparkBall(null, 0, 0, 0, 0));
	}

	protected ArrayList<SparkBall> getBullets() {
		return sparkBalls;
	}

	public void shoot(Entity entity, int angle, int speed) {
		float x = entity.getX() + entity.getWidth() / 6;
		float y = entity.getY() + entity.getHeight() / 10;

		/**
		 * If Entity Is WaterDragon, Shoot Water-Plasma Ball. If Entity Is
		 * MiniDragon, Shoot Small FireBall If Entity Is Player: >If Fire Queen
		 * - Shoot FireBall >If Ice Princess - Shoot SnowBall >If Time-Lord -
		 * Shoot SparkBall >If Space-Lord - Shoot White EnergyBall >If Tribrid -
		 * Shoot DarkBall >If Ghost - Shoot Yellow LightBall >If Sailor - Shoot
		 * Gold Bullet >If Cyber Raider - Shoot Hot Metal Bullets
		 */
		SparkBall b = SparkPool.getInstance().aquireReusable();
		if (b == null) {
			b = new SparkBall(entity, x, y, angle, speed);
		} else {
			b.setSource(entity);
			b.setX(x);
			b.setY(y);
			b.setSpeed(speed);
			b.setAngle(angle);
		}
		sparkBalls.add(b);
	}

	public abstract double getScaler();

	public void drawStats(Graphics2D g2d, String collectable) {
		rnBtn.draw(g2d);
		
		int lifeX = Globals.SCREEN_WIDTH - 240;
		int lifeY = 20;

		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(hp < 20 ? Color.red : hp < 100 ? Color.magenta
				: Color.green);
		g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
		g2d.drawRect(lifeX, lifeY, 200, 34);
		g2d.fillRect(lifeX, lifeY, hp, 34);
		g2d.drawString(getLives() + " Lives", lifeX - 100, lifeY + 24);

		g2d.setColor(player.getEnergy() < 20 ? Color.red
				: player.getEnergy() < 100 ? Color.orange
						: player.getEnergy() < 300 ? Color.yellow : Color.cyan);
		g2d.drawString(player.getEnergy() + " Energy", lifeX - 370, lifeY + 75);
		g2d.drawRect(lifeX - 200, lifeY + 45, 400, 40);
		g2d.fillRect(lifeX - 200, lifeY + 45, player.getEnergy(), 40);
		if (player.getEnergy() >= 300) {
			g2d.setColor(Color.red);
			String mega = "MEGA (Up + A)";
			g2d.drawString(mega, lifeX
					- (g2d.getFontMetrics().stringWidth(mega) / 2), lifeY + 75);
		}

		g2d.setColor(Color.yellow);
		g2d.drawString(numCollectables + "/" + totalCollectables + " Coins",
				lifeX, lifeY + 125);
	}

	public PlayerID getPlayerID() {
		return playerID;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getNumCollectables() {
		return numCollectables;
	}

	public void setNumCollectables(int numCollectables) {
		this.numCollectables = numCollectables;
	}

	public int getTotalCollectables() {
		return totalCollectables;
	}

	public void setTotalCollectables(int totalCollectables) {
		this.totalCollectables = totalCollectables;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public abstract int getScreenID();

	public void megaRelease() {
		sparkBalls.removeAll(sparkBalls);
		// TODO Implement This Attack/Ability.
		// TODO Big Attack/Power Based On PlayerID

		switch (playerID) {
		case Cyber:
			// TODO Ominous stream of symbols thrown in all directions, damaging
			// everything it touches.
			break;
		case Ghost:
			ghostAttack = new ArrayList<Ellipse2D>();
			for (int i = 0; i < 80; i++) {
				ghostAttack.add(new Ellipse2D.Double(getPlayer().getX(),
						getPlayer().getY(), 30, 15));
			}
			// TODO Mosaic of Yellow Oval Bullets In All Directions, Damaging
			// everything it hits.
			break;
		case Man1:
			// TODO Stop Time, Player Is Invincible.
			break;
		case Man2:
			// TODO Player In Air, Big Gun Over Shoulder Shoots Giant White
			// Ball Positive To Player's Direction
			break;
		case Man3:
			// TODO Air strike Of fiery cannon balls dropped from the sailor's
			// mother-ship (partially visible from top of screen)
			break;
		case Woman1:
			// TODO Concentric Circular Ice Wave, Damaging everything within
			// it's
			// radius while moving.
			break;
		case Woman2:
			// TODO Lightning Fills Up The Entire Screen, Damaging Everything.
			break;
		case Woman3:
			// TODO Thick Lines Of Fire Blast In Both Directions, Damaging
			// Everything
			break;
		}
	}

	private ArrayList<Ellipse2D> ghostAttack;
	private ArrayList<SparkBall> removal;

	public void megaUpdate() {
		updateGhostAttack();
	}

	public void megaDraw(Graphics2D g2d) {
		drawGhostAttack(g2d);
	}

	public void updateGhostAttack() {
		if (ghostAttack != null) {
			Iterator<Ellipse2D> g = ghostAttack.iterator();
			ghost: while (g.hasNext()) {
				Ellipse2D e = g.next();
				e.setFrame(e.getX() + (40 * Math.random()), e.getY()
						+ (30 * Math.random() - 15), e.getWidth(),
						e.getHeight());
				Iterator<SparkBall> v = sparkBalls.iterator();
				while (v.hasNext()) {
					SparkBall b = v.next();
					if (e.intersects(b.getBoundingRect())) {
						v.remove();
						g.remove();
						continue ghost;
					}
				}
				for (Dragon d : getDragons()) {
					if (d.isVisible() && e.intersects(d.getBoundingRect())) {
						d.attacked();
						d.decreaseHP(20);
						g.remove();
						continue;
					}
				}
			}
		}
	}

	public void drawGhostAttack(Graphics2D g2d) {
		g2d.setColor(Color.yellow);
		if (ghostAttack != null)
			for (Ellipse2D e : ghostAttack) {
				g2d.fill(e);
			}
	}

	public ArrayList<Dragon> getDragons() {
		return dragons;
	}

	public void reset() {
		setPaused(false);
		pauseDelay = System.currentTimeMillis();

	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public abstract Camera getCamera();

	public void updateBullets() {
		for (SparkBall b : getBullets()) {
			b.update();
			if (b.getX() < getCamera().getCameraX()
					|| b.getX() > getCamera().getCameraX()
							+ Globals.SCREEN_WIDTH / getScaler()
					|| b.getY() < getCamera().getCameraY()
					|| b.getY() > getCamera().getCameraY()
							+ Globals.SCREEN_HEIGHT / getScaler()) {
				removal.add(b);
			} else if (getPlayer().getLightning().isVisible()
					&& b.getBoundingRect().intersects(
							getPlayer().getLightning().getBoundingRect())
					&& !b.getSource().equals(getPlayer())) {
				removal.add(b);
			} else if (b.getBoundingRect().intersects(
					getPlayer().getBoundingRect())
					&& !b.getSource().equals(getPlayer())) {
				removal.add(b);
				decreaseHP(15);
			} else {
				for (Dragon d : getDragons()) {
					if (d.isVisible()
							&& b.getBoundingRect().intersects(
									d.getBoundingRect())
							&& b.getSource().equals(getPlayer())) {
						d.attacked();
						d.decreaseHP(20);
						removal.add(b);
					}
				}
			}
			if (!removal.contains(b)) {
				int myX = (int) (Math.ceil((b.getBoundingRect().x + b
						.getBoundingRect().getWidth())
						/ Globals.MAP.getMapData().tilewidth));
				int myY = (int) (Math.ceil((b.getBoundingRect().y + b
						.getBoundingRect().getHeight())
						/ Globals.MAP.getMapData().tileheight));
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

		for (SparkBall b : removal) {
			SparkPool.getInstance().addReusable(b);
			getBullets().remove(b);
		}
		removal.removeAll(removal);
	}
	
	@Override
	public void refreshSize() {
		
	}
}
