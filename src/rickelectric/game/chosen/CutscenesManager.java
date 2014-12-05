package rickelectric.game.chosen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import rickelectric.game.chosen.entities.GameEntityServices;
import rickelectric.game.chosen.entities.PlayerID;
import rickelectric.game.chosen.screens.LoadingScreen;

public class CutscenesManager implements GameEntityServices {

	private static CutscenesManager thisInstance;

	public static synchronized CutscenesManager getInstance() {
		if (thisInstance == null)
			thisInstance = new CutscenesManager();
		return thisInstance;
	}

	private HashMap<PlayerID, Cutscene> cutscenes;
	private PlayerID currentScene;
	private int returnScreen;

	private CutscenesManager() {
		cutscenes = new HashMap<PlayerID, Cutscene>();
		//LoadingScreen.getInstance().setText("Loading Cutscenes...");
		cutscenes.put(null, new Cutscene("Cutscenes/The-Jump-34F", 34, 140));
		cutscenes.get(null).setLooping(true);
		LoadingScreen.getInstance().setPercent(25);
		cutscenes.put(PlayerID.Ghost, new Cutscene(
				"Cutscenes/Ghost/Ghost-Attack-35F", 35, 150));
		LoadingScreen.getInstance().setPercent(50);
		cutscenes.put(PlayerID.Man2, new Cutscene(
				"Cutscenes/Man2/Man2-Attack-35F", 35, 150));
		LoadingScreen.getInstance().setPercent(75);
		cutscenes.put(PlayerID.Woman3, new Cutscene(
				"Cutscenes/Woman3/Woman3-Attack-35F", 35, 150));
		LoadingScreen.getInstance().setPercent(100);
		currentScene = null;
		returnScreen = -1;
	}

	public void playScene(PlayerID pid, int returnScreen) {
		currentScene = pid;
		this.returnScreen = returnScreen;
		cutscenes.get(currentScene).reset();
	}

	@Override
	public void update() {
		if (cutscenes.get(currentScene) != null) {
			cutscenes.get(currentScene).update();
			if (cutscenes.get(currentScene).isHasFinished()) {
				if (returnScreen == -1) {
					playScene(null, -1);
				} else {
					GameSystem.getInstance().changeScreen(returnScreen);
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
		if (cutscenes.get(currentScene) != null) {
			cutscenes.get(currentScene).draw(g2d);
		}
	}

}
