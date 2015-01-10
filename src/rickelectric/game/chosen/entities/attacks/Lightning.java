package rickelectric.game.chosen.entities.attacks;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import rickelectric.game.chosen.entities.Entity;

public class Lightning extends Entity implements ShortRangeAttack{

	ArrayList<ArrayList<Line2D>> paths;
	int[] pathIDs;
	private Random r;
	private boolean visible;
	private Color color;
	private int direction;
	private int elevation;

	public Lightning(float x, float y) {
		super(x, y);
		setWidth(600);
		r = new Random();
		pathIDs = new int[]{0,0};
		color = Color.yellow;
		visible = false;
		paths = new ArrayList<ArrayList<Line2D>>();
		generateLightning();
		direction = 1;
		elevation = 0;
	}

	public void setDirection(AttackDirection direction) {
		this.direction = direction==DIRECTION_LEFT?-1:1;
	}
	
	/**
	 * 1=up, 0=none, -1=down
	 * @param elevation
	 */
	public void setElevation(int elevation){
		this.elevation = elevation;
		
	}

	public void setVisible(boolean b) {
		this.visible = b;
	}

	public boolean isVisible() {
		return visible;
	}

	public void update() {
		generateLightning();
	}
	
	public void updateChosenPath(){
		pathIDs[0]=r.nextInt(4);
		pathIDs[1]=r.nextInt(4);
	}

	public void draw(Graphics2D g2d) {
		if (paths.size() > 0 && visible) {
			g2d.setColor(color);
			for (Line2D l : paths.get(pathIDs[0])) {
				g2d.draw(l);
			}
			for (Line2D l : paths.get(pathIDs[1])) {
				g2d.draw(l);
			}
		}
	}

	public void generateLightning() {
		paths.removeAll(paths);
		double startX = this.x;
		double startY = this.y;

		double endX = startX + Math.signum(direction) * 600;
		double endY = this.y
				+ (elevation==1 ? -300
						: elevation==-1 ? 300
								: height);

		double min = 0, max = 0, length = 0;
		bolt:
		for (int i = 0; i < 4; i++) {
			ArrayList<Line2D> curr = new ArrayList<Line2D>();
			double x = startX;
			double y = startY;

			while (true) {
				if (y - startY < min)
					min = y - startY;
				if (y - startY > max)
					max = y - startY;
				double nx;
				if (Math.signum(direction) > 0) {
					nx = x + (r.nextInt(50) + 10);
					if (nx > endX)
						break;
				} else {
					nx = x - (r.nextInt(50) + 10);
					if (nx < endX)
						break;
				}
				double ny = startY + (r.nextInt(300) - (elevation==1? 300
						: elevation==-1 ? 0
								: 150));
				Line2D l2 = new Line2D.Double(x, y, nx, ny);
				curr.add(l2);

				x = nx;
				y = ny;
				length=x;
				if(Math.random()<0.05){
					paths.add(curr);
					continue bolt;
				}
			}
			Line2D last = new Line2D.Double(x, y, endX, endY);
			curr.add(last);
			paths.add(curr);
		}
		if (Math.signum(direction) > 0) {
			boundingRect.setLocation((int) startX, (int) (startY + min));
		} else {
			boundingRect.setLocation((int) endX, (int) (startY + min));
		}
		boundingRect.setSize((int)Math.abs(length-startX), (int) (max - min));
		updateChosenPath();
	}
}
