package rickelectric.game.chosen.screens;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import rickelectric.game.chosen.AssetManager;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.KeyboardInputService;

public class GameOverScreen implements GameScreen {

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(Globals.SCREEN_WIDTH / 2 - 350,
				Globals.SCREEN_HEIGHT / 2 - 130, 700, 250);
		g2d.setColor(Color.red);
		g2d.setFont(AssetManager.getInstance().getFont()
				.deriveFont(Font.BOLD, 90));
		String s = new String(GameSystem.getInstance().getLevelScreen()
				.getLives() > 0 ? "YOU WIN" : "GAME OVER");
		g2d.drawString(s, Globals.SCREEN_WIDTH / 2
				- g2d.getFontMetrics().stringWidth(s) / 2,
				Globals.SCREEN_HEIGHT / 2 - 30);

		g2d.setColor(Color.green);
		g2d.setFont(AssetManager.getInstance().getFont()
				.deriveFont(Font.BOLD, 30));
		g2d.drawString(
				"Press Enter To Return To Main Menu",
				Globals.SCREEN_WIDTH
						/ 2
						- g2d.getFontMetrics().stringWidth(
								"Press Enter To Return To Main Menu") / 2,
				Globals.SCREEN_HEIGHT / 2 + 30);
	}

	@Override
	public void update() {
		if (KeyboardInputService.getInstance().isEnter()) {
			GameSystem.getInstance().changeScreen(GameSystem.START_SCREEN);
		}
	}

	@Override
	public void loadScreen() {

	}
}