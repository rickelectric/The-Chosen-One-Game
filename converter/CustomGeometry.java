
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class CustomGeometry {
	
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

	public static Area getOutline(BufferedImage image, Color color,
			boolean include, int tolerance) {
		Area area = new Area();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color pixel = new Color(image.getRGB(x, y));
				if (include) {
					if (isIncluded(color, pixel, tolerance)) {
						Rectangle r = new Rectangle(x, y, 1, 1);
						area.add(new Area(r));
					}
				} else {
					if (!isIncluded(color, pixel, tolerance)) {
						Rectangle r = new Rectangle(x, y, 1, 1);
						area.add(new Area(r));
					}
				}
			}
		}
		return area;
	}

	public static boolean isIncluded(Color target, Color pixel, int tolerance) {
		int rT = target.getRed();
		int gT = target.getGreen();
		int bT = target.getBlue();
		int rP = pixel.getRed();
		int gP = pixel.getGreen();
		int bP = pixel.getBlue();
		return ((rP - tolerance <= rT) && (rT <= rP + tolerance)
				&& (gP - tolerance <= gT) && (gT <= gP + tolerance)
				&& (bP - tolerance <= bT) && (bT <= bP + tolerance));
	}

	public static Polygon getPolygonOutline(BufferedImage image) {
		Area a = getOutline(image, new Color(0, 0, 0, 0), false, 5);
		Polygon p = new Polygon();
		FlatteningPathIterator fpi = new FlatteningPathIterator(
				a.getPathIterator(null), 0.5);
		double[] pts = new double[6];
		while (!fpi.isDone()) {
			switch (fpi.currentSegment(pts)) {
			case FlatteningPathIterator.SEG_MOVETO:
			case FlatteningPathIterator.SEG_LINETO:
				p.addPoint((int) pts[0], (int) pts[1]);
				break;
			}
			fpi.next();
		}
		return p;
	}

	public static void rotatePolygon(Polygon p, Point center, double angle) {
		for (int i = 0; i < p.npoints; i++) {
			Point q = new Point(p.xpoints[i], p.ypoints[i]);
			Point result = rotatePoint(q, center, angle);
			p.xpoints[i] = result.x;
			p.ypoints[i] = result.y;
		}
	}

	public static Point rotatePoint(Point pt, Point center, double angleDeg) {
		double angleRad = (angleDeg / 180) * Math.PI;
		double cosAngle = Math.cos(angleRad);
		double sinAngle = Math.sin(angleRad);

		double dx = (pt.x - center.x);
		double dy = (pt.y - center.y);

		pt.x = center.x + (int) (dx * cosAngle - dy * sinAngle);
		pt.y = center.y + (int) (dx * sinAngle + dy * cosAngle);
		return pt;
	}

	public static void translatePolygon(Polygon p, float dx, float dy) {
		for (int i = 0; i < p.npoints; i++) {
			p.xpoints[i] += dx;
			p.ypoints[i] += dy;
		}
	}

	public static float highestPointOnPolygonAt(Polygon p, int xLoc) {
		int maxY = 0;
		for (int i = 0; i < p.xpoints.length; i++) {
			if (p.ypoints[i] > maxY)
				if(p.xpoints[i]>xLoc-15 && p.xpoints[i]<xLoc+15)
				maxY = p.ypoints[i];
		}
		return maxY;
	}

	public static void getVisibleTerrain(BufferedImage img, float[] terrainY) {
		int imageAlpha;
		int pixel;

		int width = img.getWidth(), height = img.getHeight();

		int[] pixels = new int[width * height];

		img.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] pixel2d = new int[width][height];

		dimensionalConvert(pixels, pixel2d, width);

		for (int i = 0; i < pixel2d.length; i++) {
			terrainY[i]=-1;
			for (int j = 0; j < pixel2d[i].length; j++) {
				pixel = pixel2d[i][j];
				
				imageAlpha = (pixel & 0xFF000000) >>> 24;

				if (imageAlpha >50){
					terrainY[i] = j-1;
					break;
				}
			}
		}
	}

	public static void dimensionalConvert(int[] src, int[][] dest, int cols) {
		int x = 0, y = 0;
		for (int i = 0; i < src.length; i++) {
			dest[x][y] = src[i];
			y++;
			if (y >= cols) {
				y = 0;
				x++;
			}
		}
	}

	public static void dimensionalConvert(int[][] src, int[] dest, int cols) {
		int x = 0, y = 0;
		for (int i = 0; i < src.length; i++) {
			dest[i] = src[x][y];
			y++;
			if (y >= cols) {
				y = 0;
				x++;
			}
		}
	}
}
