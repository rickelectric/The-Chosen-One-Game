package rickelectric.game.chosen.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import rickelectric.game.chosen.AssetManager;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.KeyboardInputService;

public class LoadingScreen implements GameScreen {

	public static LoadingScreen thisInstance;

	public synchronized static LoadingScreen getInstance() {
		if (thisInstance == null) {
			thisInstance = new LoadingScreen();
		}
		return thisInstance;
	}

	private String loadingText;
	private int loadingPercent;
	private int returnScreen;

	private LoadingScreen() {
		loadingText = "Loading...";
		loadingPercent = 0;
		returnScreen = GameSystem.START_SCREEN;
	}

	public void loading(int returnScreen) {
		this.returnScreen = returnScreen;
		loadingPercent = 0;
		loadingText = "Loading...";
	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);

		Image rn = AssetManager.getInstance().getImage("RickNew.png");
		g2d.drawImage(rn, 0, 10, null);

		int sz = 180;
		String branding = "Rickelectric Games";
		do {
			g2d.setFont(AssetManager.getInstance().getFont()
					.deriveFont(Font.BOLD | Font.ITALIC, sz));
			sz--;
		} while (g2d.getFontMetrics().stringWidth(branding)
				+ (rn.getWidth(null)) >= Globals.SCREEN_WIDTH);

		g2d.setColor(Color.yellow);
		g2d.drawString(branding, rn.getWidth(null), rn.getHeight(null) - 90);

		g2d.setFont(AssetManager.getInstance().getFont()
				.deriveFont(Font.BOLD, 28));

		int sX = Globals.SCREEN_WIDTH / 2
				- g2d.getFontMetrics().stringWidth(loadingText);
		int sY = Globals.SCREEN_HEIGHT / 2 - 100;
		int tLen = Globals.SCREEN_WIDTH - 200;

		g2d.setColor(Color.cyan);
		g2d.drawString(loadingText, sX, sY);

		g2d.setColor(Color.green);
		g2d.drawRect(100, Globals.SCREEN_HEIGHT / 2 - 20,
				Globals.SCREEN_WIDTH - 200, 40);
		g2d.fillRect(100, Globals.SCREEN_HEIGHT / 2 - 20,
				(int) ((loadingPercent / 100f) * tLen), 40);
	}

	public void setPercent(int percent) {
		this.loadingPercent = percent;
	}

	public void update() {
		if (KeyboardInputService.getInstance().isEscape()) {
			System.exit(0);
		}
		if (loadingPercent >= 100) {
			GameSystem.getInstance().changeScreen(returnScreen);
		}
	}

	public void loadScreen() {

	}

	public void setText(String text) {
		this.loadingText = text;
	}

	@Override
	public void refreshSize() {
		
	}

}
