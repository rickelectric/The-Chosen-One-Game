package rickelectric.game.chosen.entities;


public class BulletPool extends ObjectPool<SparkBall> {
	
	private static BulletPool objectPool = null;

	private BulletPool(){
		super();
	}
	
	/**
	 * Singleton implementation
	 * 
	 * @return
	 */
	public synchronized static BulletPool getInstance() {
		if (objectPool == null) {
			objectPool = new BulletPool();
		}
		return objectPool;
	}


}
