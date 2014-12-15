package rickelectric.game.chosen.entities.enemies;

import java.awt.Rectangle;

import rickelectric.game.chosen.entities.GameEntityServices;

public interface Dragon extends GameEntityServices {
	
	void destroy();
	void decreaseHP(int i);
	
	public boolean beingAttacked();

	public void attacked();
	
	public void bind(float x1,float x2);
	public Rectangle getBoundingRect();
	public boolean isVisible();

}
