package rickelectric.game.chosen.entities;
import java.awt.Graphics2D;

public class DualAnimatedSprite extends DualSprite {
	protected float frameWidth;
	protected float frameHeight;
	protected int[] duration;
	protected int numFrames;
	protected int currentFrame;
	protected int currentFrameCount;
	protected boolean isVisible;

	private boolean isLooping;
	private boolean hasFinished;

	private int reverseID;

	/**
	 * Constructor Method
	 * 
	 * @param imageName
	 * @param x
	 * @param y
	 * @param numFrames
	 * @param duration
	 * @param reverseID
	 */
	public DualAnimatedSprite(String image1, String image2, float x, float y,
			int numFrames, int duration, int reverseID) {
		super(image1, image2, x, y, 1.0f);
		this.reverseID = reverseID;
		this.numFrames = numFrames;
		this.frameWidth = this.width / numFrames;
		this.frameHeight = this.height;

		currentFrame = 0;
		currentFrameCount = 0;

		this.duration = new int[this.numFrames];

		for (int i = 0; i < this.numFrames; i++) {
			this.duration[i] = duration / this.numFrames;
		}

		this.isVisible = true;
		isLooping = true;
		hasFinished = false;
	}

	public void setFrame(int ordinal) {
		currentFrame = ordinal % numFrames;
	}

	/**
	 * Update method
	 */
	public synchronized void update() {
		if (!hasFinished) {
			currentFrameCount++;

			if (duration[currentFrame] == currentFrameCount) {// time to switch
																// frame

				if ((currentFrame + 1) == numFrames) {// we are at the last
														// frame
					if (!isLooping) {
						this.hasFinished = true;
					}
				}// end if

				currentFrame++;
				currentFrameCount = 0;
				currentFrame = currentFrame % numFrames;

			}// end if
		}// end if
	}

	/**
	 * Draw method
	 */
	public void draw(Graphics2D g2d) {
		if (isVisible) {
			if (activeImage == 1)
				g2d.drawImage(
						this.image1,
						(int) x,
						(int) y,
						(int) (x + frameWidth),
						(int) (y + frameHeight),
						reverseID == 1 ? (int) (width - (frameWidth * (currentFrame + 1)))
								: (int) (frameWidth * currentFrame),
						0,
						reverseID == 1 ? (int) (width - (frameWidth * currentFrame))
								: (int) (frameWidth * (currentFrame + 1)),
						(int) frameHeight, null);
			else
				g2d.drawImage(
						this.image2,
						(int) x,
						(int) y,
						(int) (x + frameWidth),
						(int) (y + frameHeight),
						reverseID != 1 ? (int) (width - (frameWidth * (currentFrame + 1)))
								: (int) (frameWidth * currentFrame),
						0,
						reverseID != 1 ? (int) (width - (frameWidth * currentFrame))
								: (int) (frameWidth * (currentFrame + 1)),
						(int) frameHeight, null);
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

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setLooping(boolean looping) {
		this.isLooping = looping;
	}

	public boolean isHasFinished() {
		return hasFinished;
	}

	public void reset() {
		currentFrame = 0;
		currentFrameCount = 0;
		hasFinished = false;
	}

}
