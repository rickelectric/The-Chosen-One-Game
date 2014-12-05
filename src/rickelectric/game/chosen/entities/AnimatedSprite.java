package rickelectric.game.chosen.entities;
import java.awt.Graphics2D;

public class AnimatedSprite extends Sprite
{
	private float frameWidth; 
	private float frameHeight; 
	private int [] duration; 
	private int numFrames; 
	private int currentFrame; 
	private int currentFrameCount; 
	private boolean isLooping; 
	private boolean hasFinished; 
	private boolean isVisible; 
	
	/**
	 * Constructor Method 
	 * @param imageName
	 * @param x
	 * @param y
	 * @param numFrames
	 * @param duration
	 */
	public AnimatedSprite(String imageName, float x, float y, int numFrames, int duration) 
	{
		super(imageName, x, y);
		
		this.numFrames = numFrames; 
		this.frameWidth = this.width / numFrames; 
		this.frameHeight = this.height;
		
		currentFrame = 0; 
		currentFrameCount = 0; 
		
		this.duration = new int [this.numFrames]; 
		
		for(int i=0; i<this.numFrames; i++)
		{
			this.duration[i] = duration/this.numFrames; 
		}
		
		this.isLooping = true;
		this.hasFinished = false; 
		this.isVisible = true; 
	}
	
	/**
	 * Update method 
	 */
	public void update()
	{
		if (!hasFinished)
		{
			currentFrameCount++;
			
			if(duration[currentFrame] == currentFrameCount)
			{//time to switch frame 
				
				if((currentFrame + 1) == numFrames)
				{//we are at the last frame 
					if(!isLooping)
					{
						this.hasFinished = true; 
					}
				}//end if 
				
				currentFrame++;
				currentFrameCount = 0; 
				currentFrame = currentFrame % numFrames; 
				
			}//end if 
		}//end if 
	}
	
	/**
	 * Draw method
	 */
	public void draw(Graphics2D g2d)
	{
		if(isVisible)
		{
			g2d.drawImage(image, (int)x, (int)y, 
						  (int)(x + frameWidth), 
						  (int)(y + frameHeight),
						  (int)(frameWidth * currentFrame), 
						  0, 
						  (int)(frameWidth * (currentFrame+1)), 
						  (int)frameHeight, 
						  null); 
		} 
	}

	public float getFrameWidth() {
		return frameWidth;
	}

	public void setFrameWidth(float frameWidth) {
		this.frameWidth = frameWidth;
	}

	public float getFrameHeight() {
		return frameHeight;
	}

	public void setFrameHeight(float frameHeight) {
		this.frameHeight = frameHeight;
	}

	public boolean isLooping() {
		return isLooping;
	}

	public void setLooping(boolean isLooping) {
		this.isLooping = isLooping;
	}

	public boolean isHasFinished() {
		return hasFinished;
	}

	public void setHasFinished(boolean hasFinished) {
		this.hasFinished = hasFinished;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void reset(){
		currentFrame=0;
		currentFrameCount=0;
		hasFinished=false;
	}
	
	

}
