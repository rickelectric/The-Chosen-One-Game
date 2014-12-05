package rickelectric.game.chosen.entities;
import java.awt.Graphics2D;

public class Portal extends Entity {

	public static final int CLOSED = 0, OPENING = 1, OPEN = 2, CLOSING = 3;

	private Sprite closed;
	private AnimatedSprite opening, open, closing;
	
	private int state;

	public Portal(float x, float y) {
		super(x, y);
		
		closed = new Sprite("Portal/Closed",x,y);
		opening = new AnimatedSprite("Portal/Opening",x,y,14,40);
		opening.setLooping(false);
		open = new AnimatedSprite("Portal/Open",x,y,6,17);
		closing = new AnimatedSprite("Portal/Closing",x,y,14,40);
		closing.setLooping(false);
		
		this.boundingRect.setBounds((int) x, (int)y, (int)closed.width, (int)closed.height);
		
		state = CLOSED;
	}

	@Override
	public void update() {
		switch(state){
		case CLOSED:
			break;
		case OPENING:
			opening.update();
			if(opening.isHasFinished()){
				state=OPEN;
				opening.reset();
			}
			break;
		case OPEN:
			open.update();
			break;
		case CLOSING:
			closing.update();
			if(closing.isHasFinished()){
				state=CLOSED;
				closing.reset();
			}
			break;
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		switch(state){
		case CLOSED:
			closed.draw(g2d);
			break;
		case OPENING:
			opening.draw(g2d);
			break;
		case OPEN:
			open.draw(g2d);
			break;
		case CLOSING:
			closing.draw(g2d);
			break;
		}
	}

	public void setState(int state) {
		this.state=state;
	}

	public int getState() {
		return state;
	}

}
