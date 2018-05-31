package jetfighter.objects;

import java.awt.Image;

public class Bonus extends ObjectProperties{
	// Variables
	int speed;	// Move speed

	public Bonus(int x, int y, int w, int h, Image image, Plane p){
		super(x, y, w, h, image);
		speed = 10;
	}

	// Move bonus to down
	public void move(){
		setY(getY() + speed);
	}

	// Set speed
	public void setSpeed(int s){
		speed = s;
	}
}
