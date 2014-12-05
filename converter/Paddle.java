import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Paddle extends Entity
{
	private Rectangle paddle; 

	public Paddle( float x, float y, float width, float height) {
		super( x, y, width, height);

		paddle = new Rectangle (); 
		paddle.setBounds((int) x, (int)y, (int)width, (int)height); 
	}

	@Override
	public void update() 
	{
		// TODO Auto-generated method stub
		paddle.setLocation((int) x, (int) y); 
		
		//update bounding rectangle; 
		this.boundingRect = paddle; 
		
		//player 1 is on left of screen 
//		if (this.boundingRect.intersects(GameSystem.getInstance().getBall().getBoundingRect()))
//		{
//			System.out.println("Collide with ball!"); 
//			GameSystem.getInstance().getBall().hitBall(); 
//			
//			
//		}
	}

	@Override
	public void draw(Graphics2D g2d) 
	{
		// TODO Auto-generated method stub
		g2d.setColor(Color.green); 
		g2d.fill(paddle); 
	}
	
	public void moveUp()
	{
		this.y -= 15; 
	}
	
	public void moveDown()
	{
		this.y += 15; 
	}
	
	

}
