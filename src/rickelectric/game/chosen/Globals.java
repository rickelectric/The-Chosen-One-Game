package rickelectric.game.chosen;

import rickelectric.game.chosen.level.tilemap.LevelMap;

public class Globals 
{
	//here we will store our global variables 
	
	//this is not the best design, but should work for 
	// simple games.  Ideally, we should have a 
	// service locator implementation 
	
	public static int SCREEN_WIDTH = 1366; 
	public static int SCREEN_HEIGHT = 768;
	
	//should generally be the size of your tilemap (in pixels) 
	public static int WORLD_WIDTH = 2000; 
	
	public static int WORLD_HEIGHT = 2000; 
	
	//game map 
	public static LevelMap MAP;

}
