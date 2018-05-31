package jetfighter.objects;

/**
 * Objects Coordinate
 * Base class for coordinate system all objects will extend
 */

public class Coordinate{
	// Variables
	private int x; // X Coordinate
	private	int y; // Y Coordinate
	
	// Constructor
	/**
	 * Constructor of Coordinate
	 * @param x:int
	 * @param y:int
	 */
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	// Getters and Setters
	/**
	 * Return X Coordinate
	 * @return int X Coordinate
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Return Y Coordinate
	 * @return int Y Coordinate
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Set X Coordinate
	 * @param int X Coordinate
	 */
	public void setX(int x){
		this.x = x;
	}
	
	/**
	 * Set Y Coordinate
	 * @param int Y Coordinate
	 */
	public void setY(int y){
		this.y = y;
	}
}
