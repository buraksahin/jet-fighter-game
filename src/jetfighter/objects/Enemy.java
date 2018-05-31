package jetfighter.objects;

import java.awt.Image;
import java.util.Random;

/**
 * Plane - Player Object 
 */
public class Enemy extends ObjectProperties{
	// Variables
	boolean alive;
	int health;
	int screenWidth;
	int moveState;
	int moveCount;
	int ammo;

	// Constructor
	/**
	 * Enemy
	 * 
	 * @param x:int 'x coordinate'
	 * @param y:int 'y coordinate'
	 * @param h:int	'height of the object image'
	 * @param w:int 'width of the object image'
	 * @param image:Image 'Image of the object'
	 */
	public Enemy(int x, int y, int w, int h, Image image, int screenWidth){
		super(x, y, w, h, image);
		alive = true;
		health = 100;
		this.screenWidth = screenWidth;
		setAmmoValue();
	}

	/*
	 * Getters and Setters
	 */
	// Decrease health of the enemy
	public void decreaseHealth(int x){
		health = health - x;
		if(health<=0) {
			alive = false;
		}
	}

	// Move enemy plane left
	public void moveLeft(){
		if(getX()>5 && moveCount>0){
			setX(getX()-5);
			moveCount--;
		}
		else{
			moveState=1;
		}
	}

	// Move enemy plane right
	public void moveRight(){
		if(screenWidth-getWidth()>getX()+5 && moveCount>0){
			setX(getX()+5);
			moveCount--;
		}
		else{
			moveState=0;
		}
	}

	// Move enemy plane
	public void moveLR(){
		if(moveState==1){
			moveRight();
		}
		else{
			moveLeft();
		}
	}

	// Move enemy plane down
	public void moveToDown(){
		setY(getY()+2);
	}

	public void setAmmoValue(){
		Random r = new Random();
		ammo = r.nextInt(100);
	}

	public boolean isEnemyAlive(){
		return alive;
	}

	public void setAlive(boolean alive){
		this.alive = alive;
	}

	public int getHealth(){
		return health;
	}

	public void setHealth(int health){
		this.health = health;
	}
	
	public int getMoveState(){
		return moveState;
	}

	public void setMoveState(int moveState){
		this.moveState = moveState;
	}

	public int getMoveCount(){
		return moveCount;
	}

	public void setMoveCount(int moveCount){
		this.moveCount = moveCount;
	}

	public int getAmmo(){
		return ammo;
	}

	public void setAmmo(int ammo){
		this.ammo = ammo;
	}
	
	public void decreaseAmmo(){
		ammo--;		
	}
}
