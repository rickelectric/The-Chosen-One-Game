package rickelectric.game.chosen;
import java.awt.Graphics2D;

import rickelectric.game.chosen.entities.ParallaxBackground;


public class BackgroundManager {
	
	private ParallaxBackground[] backgrounds;
	private float[] relativeSpeed;
	public float speedMultiplier;
	
	public BackgroundManager(GameSystem game,int levelNo){
		if(levelNo==2){
			backgrounds = new ParallaxBackground[5];
			relativeSpeed = new float[5];
			backgrounds[0] = new ParallaxBackground(game,"level1/Layer1_Sky",0);
			relativeSpeed[0] = -0.002f;
			backgrounds[1] = new ParallaxBackground(game,"level1/Layer2_Moon",0);
			backgrounds[1].setRepeat(false);
			relativeSpeed[1] = 0.0f;
			backgrounds[2] = new ParallaxBackground(game,"level1/Layer3_Mountains",0);
			relativeSpeed[2] = 0.05f;
			backgrounds[3] = new ParallaxBackground(game,"level1/Layer4_TreesFar",0);
			relativeSpeed[3] = 0.1f;
			backgrounds[4] = new ParallaxBackground(game,"level1/Layer5_TreesNear",0);
			relativeSpeed[4] = 0.2f;
		}
		else{
			backgrounds = new ParallaxBackground[4];
			relativeSpeed = new float[4];
			backgrounds[0] = new ParallaxBackground(game,"level2/Layer1_Sky",0);
			relativeSpeed[0] = -0.002f;
			backgrounds[1] = new ParallaxBackground(game,"level2/Layer2_Mountains",0);
			relativeSpeed[1] = 0.05f;
			backgrounds[2] = new ParallaxBackground(game,"level2/Layer3_Buildings",0);
			relativeSpeed[2] = 0.2f;
			backgrounds[3] = new ParallaxBackground(game,"level2/Layer4_Trees",0);
			relativeSpeed[3] = 0.3f;
		}
		
		speedMultiplier = 0;
	}
	
	public void update(){
		for(ParallaxBackground bg:backgrounds){
			bg.update();
		}
	}
	
	public void draw(Graphics2D g2d){
		for(ParallaxBackground bg:backgrounds){
			bg.draw(g2d);
		}
	}
	
	public void setSpeed(float speed){
		this.speedMultiplier=-speed;
		for(int i=0;i<backgrounds.length;i++){
			backgrounds[i].setSpeed(relativeSpeed[i]*speedMultiplier);
		}
	}
	
	public float getSpeedMultiplier(){
		return speedMultiplier;
	}

	public void reset() {
		//setSpeed(50);
		for(int i=0;i<backgrounds.length;i++){
			backgrounds[i].setSpeed(0);
			backgrounds[i].reset();
		}
	}

}
