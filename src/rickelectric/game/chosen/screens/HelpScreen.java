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
		
		//g2d.setColor(Color.red);
		
		g2d.setFont(new Font(Font.DIALOG,Font.BOLD, 30));
		g2d.drawString("Controls: ", 100, 95);
		g2d.setFont(AssetManager.getInstance().getFont()
				.deriveFont(Font.BOLD, 25));
		
		g2d.drawString("Left / Right: Move Left And Right", 120, 130);
		g2d.drawString(
				"Z: Short-Range Attack. Use Up/Down To Control Attack.",
				120, 160);
		g2d.drawString("X: Long-Range Attack", 120, 190);
		g2d.drawString("Spacebar: Jump", 120, 220);
		g2d.drawString("Down - A: Supercharge Energy", 120, 250);
		g2d.drawString("Up - A: Use Mega Attack / Special Ability", 120, 280);
		g2d.drawString("Escape: Pause Menu, Resume", 120, 310);

		g2d.setFont(new Font(Font.DIALOG,Font.BOLD, 30));
		g2d.drawString("Objectives: ", 100, 355);
		
		g2d.setFont(AssetManager.getInstance().getFont()
				.deriveFont(Font.BOLD, 25));
		g2d.drawString("1. Find And Shoot Lightning At The Eyes.", 120, 390);
		g2d.drawString("- They Reveal Hidden Paths", 180, 420);
		g2d.drawString("- One Of Them Opens The Portal", 180, 450);
		g2d.drawString(
				"2. Find And Slay All Dragons. Different Attacks Are Effective Against Different Dragons.",
				120, 480);
		g2d.drawString("3. You Only Have 3 Lives. So, Avoid Dying.", 120, 510);
		g2d.drawString("4. Totem Poles Shoot Sparks At You. Avoid.", 120, 540);
		g2d.drawString("-You May Also Use Your Lightning To Destroy Sparks.",
				180, 570);
		g2d.drawString(
				"5. Find The Portal And Use It To Slide Off The Planet.", 120,
				600);

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

	@Override
	public void refreshSize() {
		if(back==null) return;
		back.setX(Globals.SCREEN_WIDTH - 300);
	}

}
