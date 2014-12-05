package rickelectric.game.chosen;
import java.util.HashMap;

import rickelectric.game.chosen.entities.Entity;
import rickelectric.game.chosen.level.tilemap.Tile;

public class ActionManager {

	private static ActionManager thisInstance = null;

	/**
	 * Tile Actions
	 */
	public static final int SHOOT = 1, DROWN = 2, BURN = 3, GLOW = 4, HELP = 5,
			JUMPTHRU = 6, HURT = 7;

	/**
	 * Stores Actions -> Tile IDs associated with these actions
	 */
	private HashMap<Integer, Integer> tileActions;

	private long shootDelay;

	private ActionManager() {
		tileActions = new HashMap<Integer, Integer>();

		tileActions.put(21, SHOOT);// Totem Pole Eye Tile

		for (int i = 25; i <= 28; i++) {// Bridge Tiles
			tileActions.put(i, JUMPTHRU);
		}

		{// Cat's Eyes Tiles
			tileActions.put(30, HELP);
			tileActions.put(31, HELP);
			tileActions.put(32, BURN);
			tileActions.put(33, BURN);

			tileActions.put(38, HELP);
			tileActions.put(39, HELP);
			tileActions.put(40, BURN);
			tileActions.put(41, BURN);
			tileActions.put(38, GLOW);
			tileActions.put(39, GLOW);
			tileActions.put(40, HURT);
			tileActions.put(41, HURT);
		}

		shootDelay = System.currentTimeMillis();

		for (int i = 53; i <= 54; i++)
			tileActions.put(i, DROWN);// Water Tiles
	}

	/**
	 * Singleton implementation
	 * 
	 * @return
	 */
	public synchronized static ActionManager getInstance() {
		if (thisInstance == null) {
			thisInstance = new ActionManager();
		}
		return thisInstance;
	}

	public void activateAction(Entity e) {
		if (e instanceof Tile) {
			Tile t = (Tile) e;
			Integer action = tileActions.get(t.getTileTypeID());
			if (action == null)
				return;
			if (action == SHOOT
					&& System.currentTimeMillis() - shootDelay > (4 - GameSystem
							.getInstance().getDifficulty()) * 50
					&& Math.random() > 0.8) {
				int angle = (int) Math.toDegrees(Math.atan2(
						-(e.getX() - GameSystem.getInstance().getLevelScreen()
								.getPlayer().getX()), e.getY()
								- GameSystem.getInstance().getLevelScreen()
										.getPlayer().getY()));
				GameSystem
						.getInstance()
						.getLevelScreen()
						.shoot(e, angle,
								5 * GameSystem.getInstance().getDifficulty());
				shootDelay = System.currentTimeMillis();
			}
			if (action == GLOW) {

			}
			if (action == DROWN) {
				if (Math.abs(RectangleOperations.getVerticalIntersectionDepth(
						t.getBoundingRect(), GameSystem.getInstance()
								.getLevelScreen().getPlayer().getBoundingRect())) >= 48)
					if (t.getBoundingRect().intersects(
							GameSystem.getInstance().getLevelScreen()
									.getPlayer().getBoundingRect()))
						GameSystem.getInstance().getLevelScreen().decreaseHP(5);
			}
		}
		// if(e instanceof Enemy)
	}
}
