package jetfighter.objects;

import java.awt.Image;
import java.util.Random;

public class Rocket extends ObjectProperties{
	/*
	 * Variables
	 */
	int damage, direction, screenHeight;
	Random rand = new Random();
	int speed = rand.nextInt(15)+10;

	/**
	 * Rocket
	 * @param x:int 'x coordinate'
	 * @param y:int 'y coordinate'
	 * @param screenHeight:int 'Height of the screen'
	 * @param damage:int 'Rocket damage'
	 * @param direction:int 'Rocket direction if direction is 1 -> increase Y coordinate otherwise decrease'
	 * @param h:int	'height of the object's image'
	 * @param w:int 'width of the object's image'
	 * @param image:Image 'Image of the object'
	 */
	public Rocket(int x, int y, int screenHeight, int damage, int direction, int w, int h, Image image){
		super(x, y, w, h, image);
		this.screenHeight = screenHeight;
		this.damage = damage;
		this.direction = direction;
	}
	
	// Move
	public void move(){
		if(direction == 1){
			setY(getY()+speed);
		}
		else{
			setY(getY()-speed);
		}
	}

	/*
	 * Getters and Setters
	 */
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDirection(){
		return direction;
	}

	public void setDirection(int direction){
		this.direction = direction;
	}
}
