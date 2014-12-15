package rickelectric.game.chosen;

public class RunGame 
{
	public static void main(String [] args)
	{
		//SoundManager.getInstance().playSound("xfiles",true);
		//get new game system 
		GameSystem game = GameSystem.getInstance(); 
		
		//run game 
		game.run();
	}
}
