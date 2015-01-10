package rickelectric.game.chosen.screens;
import java.awt.Graphics2D;

public interface GameScreen {
	
	/**
	 * Draws the screen
	 * @param g2d
	 */
	public void draw(Graphics2D g2d);
	
	/**
	 * Updates The Screen
	 */
	public void update();
	
	/**
	 * Loads The Map & Graphics For The Screen.
	 */
	public void loadScreen();

	public void refreshSize();

}
