package rickelectric.game.chosen;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import rickelectric.game.chosen.entities.PlayerID;
import rickelectric.game.chosen.level.LevelScreen_1;
import rickelectric.game.chosen.level.LevelScreen_2;
import rickelectric.game.chosen.screens.GameOverScreen;
import rickelectric.game.chosen.screens.GameScreen;
import rickelectric.game.chosen.screens.HelpScreen;
import rickelectric.game.chosen.screens.LevelScreen;
import rickelectric.game.chosen.screens.LoadingScreen;
import rickelectric.game.chosen.screens.SelectScreen;
import rickelectric.game.chosen.screens.StartScreen;
import rickelectric.game.chosen.screens.VortexScreen;

public class GameSystem implements Runnable {

	private static GameSystem gameSystem;

	private GameFrame gameFrame;

	public static final int LOADING_SCREEN = -1, START_SCREEN = 0,
			SELECT_PLAYER = 1, HELP_SCREEN = 2, LEVEL_1_START = 3, LEVEL_1 = 4,
			LEVEL_2_START = 5, LEVEL_2 = 6, CUTSCENE = 7, VORTEX = 8,
			GAME_OVER = 9;

	private int lastLevel = -1;

	public static final int EASY = 1, MEDIUM = 2, HARD = 3, VERY_HARD = 4;

	private LoadingScreen loadingScreen;
	private StartScreen startScreen;
	private SelectScreen selectScreen;
	private HelpScreen helpScreen;
	private LevelScreen_1 levelScreen;
	private LevelScreen_2 level2Screen;
	private int screen;

	private VortexScreen vortexScreen;

	private GameScreen gameOverScreen;

	private int difficulty;

	private long switchTime;

	/**
	 * Private Constructor
	 */
	private GameSystem() {
		gameFrame = new GameFrame("The Chosen One");
		Globals.SCREEN_WIDTH = gameFrame.getArea().getWidth();
		Globals.SCREEN_HEIGHT = gameFrame.getArea().getHeight();

		// Create new window for game

		gameFrame.getArea().addKeyListener(KeyboardInputService.getInstance());
		gameFrame.getArea().addMouseListener(MouseInputService.getInstance());
		gameFrame.getArea().addMouseMotionListener(
				MouseInputService.getInstance());

		loadingScreen = LoadingScreen.getInstance();
		startScreen = new StartScreen();
		selectScreen = new SelectScreen();
		helpScreen = new HelpScreen();

		levelScreen = new LevelScreen_1(PlayerID.Man1);
		level2Screen = new LevelScreen_2(PlayerID.Man1);
		vortexScreen = new VortexScreen();
		gameOverScreen = new GameOverScreen();

		difficulty = EASY;
	}

	/**
	 * Get instance method. - implements Singleton pattern
	 * 
	 * @return
	 */
	public synchronized static GameSystem getInstance() {
		if (gameSystem == null) {
			gameSystem = new GameSystem();
		}
		return gameSystem;
	}

	/**
	 * Run method. Contains game loop.
	 */
	public void run() {
		BufferStrategy bs = gameFrame.getBufferStrategy();
		changeScreen(LOADING_SCREEN);
		new Thread(new Runnable(){
			public void run(){
				CutscenesManager.getInstance();
				startScreen.loadScreen();
				helpScreen.loadScreen();
				selectScreen.loadScreen();
			}
		}).start();
		while (true) {
			switch (screen) {
			case LOADING_SCREEN:
				runLoadingScreen(bs);
				break;
			case START_SCREEN:
				runStartScreen(bs);
				break;
			case SELECT_PLAYER:
				runSelectPlayer(bs);
				break;
			case HELP_SCREEN:
				runHelpScreen(bs);
				break;
			case LEVEL_1_START:
				levelScreen.loadScreen();
				break;
			case LEVEL_1:
				lastLevel = LEVEL_1;
				runLevel1(bs);
				break;
			case LEVEL_2_START:
				level2Screen.loadScreen();
				break;
			case LEVEL_2:
				lastLevel = LEVEL_2;
				runLevel2(bs);
				break;
			case CUTSCENE:
				runCutscene(bs);
				break;
			case VORTEX:
				runVortex(bs);
				break;
			case GAME_OVER:
				runGameOver(bs);
				break;
			}
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	private void runLoadingScreen(BufferStrategy bs) {
		loadingScreen.update();
		loadingScreen.draw((Graphics2D) bs.getDrawGraphics());
		bs.show();
	}

	private void runStartScreen(BufferStrategy bs) {
		startScreen.update();
		startScreen.draw((Graphics2D) bs.getDrawGraphics());
		levelScreen.reset();
		level2Screen.reset();
		bs.show();
	}

	private void runSelectPlayer(BufferStrategy bs) {
		selectScreen.update();
		selectScreen.draw((Graphics2D) bs.getDrawGraphics());
		bs.show();
	}

	private void runHelpScreen(BufferStrategy bs) {
		helpScreen.update();
		helpScreen.draw((Graphics2D) bs.getDrawGraphics());
		bs.show();
	}

	private void runLevel1(BufferStrategy bs) {
		// Run & Update Level 1
		levelScreen.update();
		levelScreen.draw((Graphics2D) bs.getDrawGraphics());
		bs.show();
	}

	private void runLevel2(BufferStrategy bs) {
		// Run & Update Level 2
		level2Screen.update();
		level2Screen.draw((Graphics2D) bs.getDrawGraphics());
		bs.show();
	}

	public void playCutscene(PlayerID pid) {
		CutscenesManager.getInstance().playScene(pid, screen);
		changeScreen(CUTSCENE);
	}

	private void runCutscene(BufferStrategy bs) {
		CutscenesManager.getInstance().update();
		CutscenesManager.getInstance().draw((Graphics2D) bs.getDrawGraphics());
		bs.show();
	}

	private void runVortex(BufferStrategy bs) {
		vortexScreen.update();
		vortexScreen.draw((Graphics2D) bs.getDrawGraphics());
		bs.show();
	}

	private void runGameOver(BufferStrategy bs) {
		gameOverScreen.update();
		gameOverScreen.draw((Graphics2D) bs.getDrawGraphics());
		bs.show();
	}

	public int getScreenWidth() {
		return Globals.SCREEN_WIDTH;
	}

	public int getScreenHeight() {
		return Globals.SCREEN_HEIGHT;
	}

	public void changeScreen(int screenID) {
		screen = screenID;
		switchTime = System.currentTimeMillis();
		soundOff();
	}

	private void soundOff() {
		SoundManager.getInstance().stopAll();
		switch (screen) {
		case START_SCREEN:
			SoundManager.getInstance().playSound("title", true);
			break;
		case SELECT_PLAYER:
			SoundManager.getInstance().playSound("theme", true);
			break;
		case HELP_SCREEN:
			SoundManager.getInstance().playSound("title", true);
			break;
		case LEVEL_1:
			SoundManager.getInstance().playSound("xfiles", true);
			break;
		case LEVEL_2:
			SoundManager.getInstance().playSound("space", true);
			break;
		case CUTSCENE:
			break;
		case VORTEX:
			SoundManager.getInstance().playSound("vortex", true);
			break;
		case GAME_OVER:
			break;
		}
	}

	public long lastSwitchTime() {
		return switchTime;
	}

	public LevelScreen getLevelScreen() {
		return lastLevel == LEVEL_1 ? levelScreen
				: lastLevel == LEVEL_2 ? level2Screen : null;
	}

	public void nextLevelScreen() {
		if (lastLevel == LEVEL_1)
			screen = LEVEL_2_START;
		else
			screen = GAME_OVER;
	}

	public VortexScreen getVortexScreen() {
		return vortexScreen;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setPlayer(PlayerID selectedPlayer) {
		levelScreen.setPlayer(selectedPlayer);
		level2Screen.setPlayer(selectedPlayer);
	}

	public PlayerID getPlayerID() {
		return this.levelScreen.getPlayerID();
	}

}
