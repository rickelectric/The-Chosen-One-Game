package rickelectric.game.chosen.entities.attacks;

import rickelectric.game.chosen.entities.ObjectPool;


public class SparkPool extends ObjectPool<SparkBall> {
	
	private static SparkPool objectPool = null;

	private SparkPool(){
		super();
	}
	
	/**
	 * Singleton implementation
	 * 
	 * @return
	 */
	public synchronized static SparkPool getInstance() {
		if (objectPool == null) {
			objectPool = new SparkPool();
		}
		return objectPool;
	}


}
