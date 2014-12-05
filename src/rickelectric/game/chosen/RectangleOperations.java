package rickelectric.game.chosen;
import java.awt.Rectangle;


public class RectangleOperations 
{
	public static float getHorizontalIntersectionDepth (Rectangle rectA, Rectangle rectB)
	{
		//calculate half sizes 
		float halfWidthA = rectA.width / 2.0f; 
		float halfWidthB = rectB.width / 2.0f;
		
		//calculate centers 
		float centerA = rectA.x + halfWidthA; 
		float centerB = rectB.x + halfWidthB; 
		
		//calculate current and minimum non intersecting distances between centers 
		float distanceX = centerA - centerB; 
		float minDistanceX = halfWidthA + halfWidthB; 
		
		//if we are not intersecting, return 0 
		if (Math.abs(distanceX) >= minDistanceX)
			return 0F; 
		
		//calculate and return intersection depths 
		return distanceX > 0 ? minDistanceX - distanceX : -minDistanceX - distanceX; 
	}
	
	public static float getVerticalIntersectionDepth (Rectangle rectA, Rectangle rectB)
	{
		//calculate half sizes 
		float halfHeightA = rectA.height / 2.0f; 
		float halfHeightB = rectB.height / 2.0f;
		
		//calculate centers 
		float centerA = rectA.y + halfHeightA; 
		float centerB = rectB.y + halfHeightB; 
		
		//calculate current and minimum non intersecting distances between centers 
		float distanceY = centerA - centerB; 
		float minDistanceY = halfHeightA + halfHeightB; 
		
		//if we are not intersecting, return 0 
		if (Math.abs(distanceY) >= minDistanceY)
			return 0F; 
		
		//calculate and return intersection depths 
		return distanceY > 0 ? minDistanceY - distanceY : -minDistanceY - distanceY; 
	}

}
