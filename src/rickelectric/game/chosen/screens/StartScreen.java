package rickelectric.game.chosen.screens;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;

import rickelectric.game.chosen.AssetManager;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.KeyboardInputService;
import rickelectric.game.chosen.MouseInputService;
import rickelectric.game.chosen.entities.DualSprite;

public class StartScreen implements GameScreen {

	private DualSprite[] buttons;
	private long keyTime;

	private Image startScreen;
	private int imageX;
	private int imageY;
	private int imageHeight;
	private int active;

	public StartScreen() {
		keyTime = System.currentTimeMillis();
	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
		g2d.drawImage(startScreen, imageX, imageY, null);

		for (DualSprite s : buttons)
			s.draw(g2d);

		g2d.setColor(Color.white);
		g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		g2d.drawString("Press ENTER To " + getActiveAction() + "...",
				imageX + 600, imageY + imageHeight - 100);
	}

	private String getActiveAction() {
		switch (active) {
		case 0:
			return "Start Game";
		case 1:
			return "Select Your Player";
		case 2:
			return "Get Help";
		default:
			return "Quit Game";
		}
	}

	public void update() {
		if(System.currentTimeMillis()-GameSystem.getInstance().lastSwitchTime() < 300) return;
		if (KeyboardInputService.getInstance().isEnter()) {
			invokeActiveButton();
		}
		if (KeyboardInputService.getInstance().isUp()
				&& System.currentTimeMillis() - keyTime > 200) {
			setActive(active - 1 < 0 ? buttons.length - 1 : active - 1);
			keyTime = System.currentTimeMillis();
		} else if (KeyboardInputService.getInstance().isDown()
				&& System.currentTimeMillis() - keyTime > 200) {
			setActive((active + 1) % buttons.length);
			keyTime = System.currentTimeMillis();
		} else {
			MouseInputService mis = MouseInputService.getInstance();
			Point2D p = new Point2D.Float(mis.getMouseX(), mis.getMouseY());
			for (int b = 0; b < buttons.length; b++) {
				DualSprite s = buttons[b];
				if (s.getBoundingRect().contains(p)) {
					setActive(b);
					if (mis.buttonL())
						invokeActiveButton();
					break;
				}
			}
		}
	}

	private void invokeActiveButton() {
		if (active == 0)
			GameSystem.getInstance().changeScreen(GameSystem.LEVEL_1_START);
		else if (active == 1) {
			GameSystem.getInstance().changeScreen(GameSystem.SELECT_PLAYER);
		} else if (active == 2) {
			GameSystem.getInstance().changeScreen(GameSystem.HELP_SCREEN);
		} else if (active == 3) {
			System.exit(0);
		}
	}

	private void setActive(int btn) {
		active = btn;
		for (int i = 0; i < buttons.length; i++)
			buttons[i].setActiveImage(i == btn ? 1 : 2);
	}

	@Override
	public void loadScreen() {
		LoadingScreen.getInstance().setText("Loading Main Menu...");
		startScreen = AssetManager.getInstance().getImage("startScreen.jpg");
		LoadingScreen.getInstance().setPercent(35);
		imageX = GameSystem.getInstance().getScreenWidth() / 2
				- startScreen.getWidth(null) / 2;
		imageY = GameSystem.getInstance().getScreenHeight() / 2
				- startScreen.getHeight(null) / 2;
		imageHeight = startScreen.getHeight(null);

		buttons = new DualSprite[4];

		buttons[0] = new DualSprite("Buttons/StartActive", "Buttons/StartInactive",
				imageX + 80, imageY + 400, 0.6f);
		LoadingScreen.getInstance().setPercent(39);
		buttons[1] = new DualSprite("Buttons/SelectPlayerActive",
				"Buttons/SelectPlayerInactive", imageX + 80, imageY + 480, 0.6f);
		LoadingScreen.getInstance().setPercent(42);
		buttons[2] = new DualSprite("Buttons/HelpActive", "Buttons/HelpInactive", imageX + 80,
				imageY + 560, 0.6f);
		LoadingScreen.getInstance().setPercent(46);
		buttons[3] = new DualSprite("Buttons/QuitActive", "Buttons/QuitInactive", imageX + 80,
				imageY + 640, 0.6f);
		LoadingScreen.getInstance().setPercent(50);
		setActive(0);
	}
}
