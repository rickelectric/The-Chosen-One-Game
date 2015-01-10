package rickelectric.game.chosen.entities.attacks;

import rickelectric.game.chosen.entities.Entity;
import rickelectric.game.chosen.entities.GameEntityServices;

public interface Projectile extends GameEntityServices{

	public Entity getSource();
	void setSource(Entity e);
	
	public void setSpeed(float speed);

}
