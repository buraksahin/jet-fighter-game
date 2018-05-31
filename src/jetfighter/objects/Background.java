package jetfighter.objects;

import java.awt.Image;

public class Background extends ObjectProperties{
	// Variables
	int speed;
	/**
	 * Background
	 * 
	 * @param x:int 'x coordinate'
	 * @param y:int 'y coordinate'
	 * @param h:int	'height of the object image'
	 * @param w:int 'width of the object image'
	 * @param image:Image 'Image of the object'
	 */
	public Background(int x, int y, int w, int h, Image image){
		super(x, y, w, h, image);
		speed = 5;
	}

	// Move background to down
	public void move(){
		setY(getY() + speed);
	}

	// Set background move speed
	public void setSpeed(int s){
		speed = s;
	}
}
