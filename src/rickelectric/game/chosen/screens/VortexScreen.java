package rickelectric.game.chosen.screens;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Random;

import rickelectric.game.chosen.CanvasPainter;
import rickelectric.game.chosen.GameSystem;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.KeyboardInputService;
import rickelectric.game.chosen.MouseInputService;

public class VortexScreen implements GameScreen, ImageObserver {

	int angle = 0;
	int angularOffset = 0;
	double radius = 10;
	int dir = 0;
	Color currColor;
	Color currColor1;
	Color currColor2;
	int angularOffset1 = 0;
	int angularOffset2 = 0;
	int angle1 = 0;
	int angle2 = 0;
	double radius1 = 10;
	double radius2 = 10;

	int tc = 0;

	Point center;
	Random r;
	private int screenRefs = 0;

	public VortexScreen() {
		r = new Random();
		currColor = new Color(255 - r.nextInt(256), r.nextInt(256),
				r.nextInt(256));
		currColor1 = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		currColor2 = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		center = new Point(Globals.SCREEN_WIDTH / 2, Globals.SCREEN_HEIGHT / 2);
		radius = Math.max(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT) - 60;
		radius1 = radius;
		radius2 = radius;
	}

	@Override
	public void draw(Graphics2D g2d) {
		drawVortex(g2d);
		if (Math.random() > 0.3) {
			update();
			drawVortex(g2d);
		}
		if (Math.random() > 0.6) {
			update();
			drawVortex(g2d);
		}
		if (Math.random() > 0.8) {
			update();
			drawVortex(g2d);
		}

	}

	private void drawVortex(Graphics2D g2d) {
		g2d.setColor(currColor);
		CanvasPainter.markPointOnCircle(g2d, center, radius, angle);
		CanvasPainter.markPointOnCircle(g2d, center, radius, 90 + angle);
		CanvasPainter.markPointOnCircle(g2d, center, radius, 180 + angle);
		CanvasPainter.markPointOnCircle(g2d, center, radius, 270 + angle);

		g2d.setColor(currColor1);
		CanvasPainter.markPointOnCircle(g2d, center, radius1, angle1);
		CanvasPainter.markPointOnCircle(g2d, center, radius1, 90 + angle1);
		CanvasPainter.markPointOnCircle(g2d, center, radius1, 180 + angle1);
		CanvasPainter.markPointOnCircle(g2d, center, radius1, 270 + angle1);

		g2d.setColor(currColor2);
		CanvasPainter.markPointOnCircle(g2d, center, radius2, angle2);
		CanvasPainter.markPointOnCircle(g2d, center, radius2, 90 + angle2);
		CanvasPainter.markPointOnCircle(g2d, center, radius2, 180 + angle2);
		CanvasPainter.markPointOnCircle(g2d, center, radius2, 270 + angle2);
	}

	@Override
	public void update() {
		screenRefs++;
		if (screenRefs > 1000) {
			if (KeyboardInputService.getInstance().anyKeyPressed()
					|| MouseInputService.getInstance().buttonL())
				GameSystem.getInstance().nextLevelScreen();
		}
		angle += 10 + angularOffset;
		angle %= 360;
		if (dir == 0)
			radius -= 2;
		else
			radius += 2;
		if (radius <= 15) {
			radius = Math.max(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT) - 40;
			// dir=1;
			/**
			 * currColor = currColor.equals(Color.RED) ? Color.BLUE :
			 * currColor.equals(Color.BLUE) ? Color.GREEN : Color.RED;
			 */
			currColor = new Color(255 - r.nextInt(256), r.nextInt(256),
					r.nextInt(256));
			angularOffset = r.nextInt(300) + 30;
		}
		{
			angle1 += 10 + angularOffset1;
			angle1 %= 360;
			if (dir == 0)
				radius1 -= 3;
			else
				radius1 += 3;
			if (radius1 <= 15) {
				radius1 = Math.min(Globals.SCREEN_WIDTH / 2,
						Globals.SCREEN_HEIGHT / 2) - 40;
				// dir=1;
				currColor1 = new Color(r.nextInt(256), r.nextInt(128),
						r.nextInt(256));
				angularOffset1 = r.nextInt(100) + 200;
			}
		}
		{
			angle2 += 10 + angularOffset2;
			angle2 %= 360;
			if (dir == 0)
				radius2 -= 1;
			else
				radius2 += 4;
			if (radius2 <= 15) {
				radius2 = Math.min(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT) - 40;
				// dir=1;
				currColor2 = currColor2.equals(Color.white) ? Color.black
						: Color.white;
				angularOffset2 = r.nextInt(100) + 200;
			}
		}
	}

	@Override
	public void loadScreen() {

	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		return false;
	}

	public void setCenter(float x, float y) {
		center.setLocation(x, y);
	}

}
