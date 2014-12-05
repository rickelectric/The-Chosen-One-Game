package rickelectric.game.chosen;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;

/**
 * 
 * @author Rick Lewis
 *
 */
public class CanvasPainter {
	
	public static Ellipse2D drawCircle(Graphics g,Point center, int radius,boolean fill){
		int x=center.x-radius;
		int y=center.y-radius;
		Graphics2D g2d = (Graphics2D)g.create();
		Ellipse2D e = new Ellipse2D.Double(x, y, 2*radius, 2*radius);
		if(fill) g2d.fill(e);
		else g2d.draw(e);
		g2d.dispose();
		return e;
	}
	
	public static void markPointOnCircle(Graphics g, Point center,double radius,double degAngle){
		Point mark = MyGeometry.getPointOnCircle(center, radius, degAngle);
		drawCircle(g,mark,(int)Math.pow(radius,0.40),true);
	}
	
	public static Ellipse2D drawOval(Graphics g,Point center,int width,int height,boolean fill){
		Point tl = new Point(center.x-width/2,center.y-height/2);
		Graphics2D g2d = (Graphics2D)g.create();
		Ellipse2D e = new Ellipse2D.Double(tl.x, tl.y, width, height);
		if(fill) g2d.fill(e);
		else g2d.draw(e);
		g2d.dispose();
		return e;
	}
	
	public static void markPointOnOval(Graphics g, Point center,int width,int height,double degAngle){
		Point mark = MyGeometry.getPointOnOval(center, width, height, degAngle);
		drawCircle(g,mark,8,true);
	}

	public static Polygon drawIsoTriangle(Graphics g, Point center, int base, int height, boolean fill) {
		Graphics2D g2d = (Graphics2D)g.create();
		Polygon pl = MyGeometry.constructIsoTrianglePolygon(center, base, height);
		if(fill)
			g2d.fill(pl);
		else
			g2d.draw(pl);
		g2d.dispose();
		return pl;
	}
	
	
}
