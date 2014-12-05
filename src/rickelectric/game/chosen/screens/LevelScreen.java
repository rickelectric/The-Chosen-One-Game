package rickelectric.game.chosen.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import rickelectric.game.chosen.BackgroundManager;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.entities.Bullet;
import rickelectric.game.chosen.entities.BulletPool;
import rickelectric.game.chosen.entities.Entity;
import rickelectric.game.chosen.entities.Player;
import rickelectric.game.chosen.entities.PlayerID;

public abstract class LevelScreen implements GameScreen {

	private ArrayList<Bullet> bullets;
	
	private int lives;
	private int hp;
	private int numCollectables,totalCollectables;

	private PlayerID playerID;
	private Player player;

	public LevelScreen(PlayerID playerID){
		this.playerID = playerID;
		lives = 3;
		hp=200;
		numCollectables=0;
	}
	
	public void lifeLost() {
		if (lives > 1) {
			sendNotify("You Died");
			player.setX(385);
			player.setY(2432);
			lives--;
			hp = 200;
		} else {
			lives=0;
			GameSystem.getInstance().changeScreen(GameSystem.GAME_OVER);
		}
		getBackground().reset();
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

	public abstract BackgroundManager getBackground();

	public void loadScreen() {
		setPlayer(playerID);
		
		bullets = new ArrayList<Bullet>();
		
		// Initialize 20 Bullets To Pool To Increase Speed In Game.
		for (int i = 0; i < 20; i++)
			BulletPool.getInstance().addReusable(new Bullet(null, 0, 0, 0, 0));
	}

	protected ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public void shoot(Entity entity, int angle, int speed) {
		float x = entity.getX() + entity.getWidth() / 6;
		float y = entity.getY() + entity.getHeight() / 10;

		Bullet b = BulletPool.getInstance().aquireReusable();
		if (b == null) {
			b = new Bullet(entity, x, y, angle, speed);
		} else {
			b.setSource(entity);
			b.setX(x);
			b.setY(y);
			b.setSpeed(speed);
			b.setAngle(angle);
		}
		bullets.add(b);
	}

	public abstract double getScaler();

	public void drawStats(Graphics2D g2d, String collectable) {
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
				: player.getEnergy() < 100 ? Color.orange :player.getEnergy()<300 ? Color.yellow:Color.cyan);
		g2d.drawString(player.getEnergy() + " Energy", lifeX - 370, lifeY + 75);
		g2d.drawRect(lifeX-200, lifeY + 45, 400, 40);
		g2d.fillRect(lifeX-200, lifeY + 45, player.getEnergy(), 40);
		if(player.getEnergy()>=300){
			g2d.setColor(Color.red);
			String mega = "MEGA (Up + A)";
			g2d.drawString(mega, lifeX-(g2d.getFontMetrics().stringWidth(mega)/2), lifeY+75);
		}

		g2d.setColor(Color.yellow);
		g2d.drawString(numCollectables + "/"+totalCollectables+" Coins", lifeX, lifeY + 125);
	}

	public PlayerID getPlayerID() {
		return playerID;
	}
	
	public int getLives(){
		return lives;
	}
	
	public void setLives(int lives){
		this.lives=lives;
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

}
