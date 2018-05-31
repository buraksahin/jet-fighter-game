package jetfighter.objects;

import java.awt.Image;

/**
 * Object Properties
 * This is a base class for every object of the game
 * All objects will extend this class 
 */
public class ObjectProperties extends Coordinate{

	// Variables
	private int width;			// Width of the object
	private int height;			// Height of the object
	private Image sourceImage;	// Object Mesh Image

	/**
	 * Constructor 
	 */
	public ObjectProperties(int x, int y, int w, int h, Image image){
		super(x, y);
		width = w;
		height = h;
		sourceImage = image;
	}

	// Getters and Setters
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Image getSourceImage() {
		return sourceImage;
	}

	public void setSourceImage(Image sourceImage) {
		this.sourceImage = sourceImage;
	}
}
