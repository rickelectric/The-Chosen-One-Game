package rickelectric.game.chosen.screens;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import rickelectric.game.chosen.AssetManager;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.KeyboardInputService;
import rickelectric.game.chosen.MouseInputService;
import rickelectric.game.chosen.entities.CatEyes;
import rickelectric.game.chosen.entities.DualSprite;
import rickelectric.game.chosen.entities.ParallaxBackground;

public class HelpScreen implements GameScreen {

	private ParallaxBackground bg;
	private CatEyes e;
	private DualSprite back;

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
		bg.draw(g2d);
		g2d.setFont(AssetManager.getInstance().getFont()
				.deriveFont(Font.BOLD, 25));
		// g2d.setColor(Color.white);
		g2d.drawString("Controls: ", 100, 100);
		g2d.drawString("Left / Right: Move Left And Right", 120, 130);
		g2d.drawString(
				"Z: Shoot Lightning. Use Up And Down To Control Lightning.",
				120, 160);
		g2d.drawString("X: Shoot Spark Balls", 120, 190);
		g2d.drawString("Spacebar: Jump", 120, 220);
		g2d.drawString("Escape: Pause Menu, Resume", 120, 250);

		g2d.drawString("Objectives: ", 100, 300);
		g2d.drawString("1. Find And Shoot Lightning At The Eyes.", 120, 330);
		g2d.drawString("- They Reveal Hidden Paths", 180, 360);
		g2d.drawString("- One Of Them Opens The Portal", 180, 390);
		g2d.drawString(
				"2. Find And Slay The Dragon. Lightning Is Most Effective.",
				120, 420);
		g2d.drawString("3. You Only Have 3 Lives. So, Avoid Dying.", 120, 450);
		g2d.drawString("4. Totem Poles Shoot Sparks At You. Avoid.", 120, 480);
		g2d.drawString("-You May Also Use Your Lightning To Destroy Sparks.",
				180, 510);
		g2d.drawString(
				"5. Find The Portal And Use It To Slide Off The Planet.", 120,
				540);

		e.draw(g2d);
		back.draw(g2d);

	}

	@Override
	public void update() {
		bg.update();
		if (System.currentTimeMillis()
				- GameSystem.getInstance().lastSwitchTime() < 300)
			return;
		if (KeyboardInputService.getInstance().isEscape()) {
			GameSystem.getInstance().changeScreen(GameSystem.START_SCREEN);
		}
		if (back.getBoundingRect().contains(MouseInputService.getInstance()
				.getMouseX(), MouseInputService.getInstance().getMouseY())) {
			back.setActiveImage(1);
			if (MouseInputService.getInstance().buttonL()) {
				GameSystem.getInstance().changeScreen(GameSystem.START_SCREEN);
			}
		} else {
			back.setActiveImage(2);
		}
	}

	@Override
	public void loadScreen() {
		LoadingScreen.getInstance().setText("Loading Help...");
		bg = new ParallaxBackground(GameSystem.getInstance(), "JungleDay",
				-0.5f);
		LoadingScreen.getInstance().setPercent(60);
		e = new CatEyes(600, 220);
		LoadingScreen.getInstance().setPercent(70);
		back = new DualSprite("Buttons/BackActive", "Buttons/BackInactive",
				Globals.SCREEN_WIDTH - 300, 20, 1f);
		LoadingScreen.getInstance().setPercent(75);
		back.setActiveImage(2);
	}

}
