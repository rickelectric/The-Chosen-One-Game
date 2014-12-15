package rickelectric.game.chosen.entities;

public interface Projectile extends GameEntityServices{

	public Entity getSource();
	void setSource(Entity e);
	
	public void setSpeed(float speed);

}
