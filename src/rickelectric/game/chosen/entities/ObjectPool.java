package rickelectric.game.chosen.entities;


import java.util.Iterator;
import java.util.Stack;

public abstract class ObjectPool <T extends Entity>{
	
	private static final int MAX_POOL_SIZE = 100;
	private Stack<T> pool;

	protected ObjectPool() {
		pool = new Stack<T>();
	}
	
	public void addReusable(T o) {
		if (this.pool.size() < MAX_POOL_SIZE) {
			pool.push(o);
		}
	}

	public T aquireReusable() {
		if (pool.size() > 0)
			return pool.pop();
		return null;
	}

	public void releaseReusable(T o) {
		Iterator<T> iterator = pool.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(o)) {
				pool.remove(o);
			}
		}
	}

	public boolean isEmpty() {
		if (pool.size() > 0)
			return false;
		return true;
	}

}
