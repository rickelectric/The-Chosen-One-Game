package rickelectric.game.chosen;


import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * 
 * @author Rick Lewis
 *
 */
public class MyGeometry {

	public static Point getPointOnCircle(Point center, double radius,
			double degreesAngle) {
		double radiansAngle = Math.toRadians(degreesAngle);

		double x = center.x + (radius * Math.cos(radiansAngle));
		double y = center.y + (radius * Math.sin(radiansAngle));
		Point mark = new Point();
		mark.setLocation(x, y);
		return mark;
	}

	public static Point getPointOnOval(Point center, double width,
			double height, double degreesAngle) {
		double radiansAngle = Math.toRadians(degreesAngle);

		double rsel = Math.max(width, height);
		double radius = rsel / 2;

		double offsetX = (radius * Math.cos(radiansAngle));
		if (rsel == height) {
			offsetX = (offsetX / height) * width;
		}
		double x = center.x + offsetX;

		double offsetY = (radius * Math.sin(radiansAngle));
		if (rsel == width) {
			offsetY = (offsetY / width) * height;
		}
		double y = center.y + offsetY;
		Point mark = new Point();
		mark.setLocation(x, y);
		return mark;
	}

	public static Polygon constructIsoTrianglePolygon(Point center, int base,
			int height) {
		Point hTop = new Point(center.x, center.y - height / 2);
		Point hBL = new Point(center.x - base / 2, center.y + height / 2);
		Point hBR = new Point(center.x + base / 2, center.y + height / 2);
		Polygon pl = new Polygon();
		pl.addPoint(hTop.x, hTop.y);
		pl.addPoint(hBL.x, hBL.y);
		pl.addPoint(hBR.x, hBR.y);
		return pl;
	}

	public static Point getPointOnIsoTriangle(Point center, int base, int height, double degAngle){
		double radAngle = Math.toRadians(degAngle);
		double theta = Math.atan(height/base);
		double wideTheta = Math.atan((2*height)/base);
		radAngle %=2*Math.PI;
		theta %=2*Math.PI;
		
		System.out.println("Theta: "+theta+"\nWide Theta: "+wideTheta+"\nRadAngle: "+radAngle);
		
		if(3*Math.PI/2 < radAngle && radAngle  < Math.PI/2 - theta){
			//TODO On Right Hyp
			double dist = 2*Math.sin(theta) / height;
			Point d = getPointOnCircle(center, dist, degAngle);
			double hn = (center.y - height/2) + d.y;
			double bn = center.x+((2*height)/Math.tan(wideTheta));
			Point np = new Point();
			np.setLocation(bn, hn);
			return np;
		}
		else if(Math.PI/2 - theta <=radAngle && radAngle <= Math.PI/2 + theta){
			//TODO On Base
			double dist = 2*Math.sin(theta) / height;
			Point d = getPointOnCircle(center, dist, degAngle);
			Point np = new Point();
			np.setLocation(d.x, center.y+(height/2));
			return np;
		}
		else if(Math.PI/2 + theta < radAngle && radAngle  < 3*Math.PI/2){
			//TODO On Left Hyp
			double dist = 2*Math.sin(theta) / height;
			Point d = getPointOnCircle(center, dist, degAngle);
			double hn = (center.y - height/2) + d.y;
			double bn = center.x-((2*height)/Math.tan(wideTheta));
			Point np = new Point();
			np.setLocation(bn, hn);
			return np;
		}
		return new Point(center.x,center.y-height/2);
	}
	
public static Point2D getIntersectionPoint(Line2D lineA, Line2D lineB) {
		
		if(!lineA.intersectsLine(lineB)) return null;

        double x1 = lineA.getX1();
        double y1 = lineA.getY1();
        double x2 = lineA.getX2();
        double y2 = lineA.getY2();

        double x3 = lineB.getX1();
        double y3 = lineB.getY1();
        double x4 = lineB.getX2();
        double y4 = lineB.getY2();

        Point2D p = null;
        
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d != 0) {
            double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
            double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

            p = new Point2D.Double(xi, yi);
        }
        if(p==null) return null;
        if(p.getX()<lineA.getX1() && p.getX() < lineA.getX2() && p.getX()<lineB.getX1() && p.getX() < lineB.getX2()){
        	return null;
        }
        if(p.getY()<lineA.getY1() && p.getY() < lineA.getY2() && p.getY()<lineB.getY1() && p.getY() < lineB.getY2()){
        	return null;
        }
        if(p.getX()>lineA.getX1() && p.getX() > lineA.getX2() && p.getX()>lineB.getX1() && p.getX() > lineB.getX2()){
        	return null;
        }
        if(p.getY()>lineA.getY1() && p.getY() > lineA.getY2() && p.getY()>lineB.getY1() && p.getY() > lineB.getY2()){
        	return null;
        }
        return p;
    }
}
