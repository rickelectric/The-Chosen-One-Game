import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public class Particle extends Entity
{
	private Ellipse2D circle;  

	public Particle(float x, float y, float width, float height) 
	{
		super(x, y, width, height);
		circle = new Ellipse2D.Double(x, y, width, height); 
	}

	@Override
	public void update() {

		//System.out.println("angle: " + x + " " + y); 
		
 
		x += 2; 
		y = -((float)Math.cos(x / 200) *100) + 300 ;
		
		circle.setFrame(x, y, width, height); 
		
	}

	@Override
	public void draw(Graphics2D g2d) {
		// TODO Auto-generated method stub
		g2d.setColor(Color.yellow);
		g2d.fill(circle); 
		
	}

}
