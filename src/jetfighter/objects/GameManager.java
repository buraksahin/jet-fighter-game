package jetfighter.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import jetfighter.login.DataManager;
import jetfighter.login.GameAccountUI;
import jetfighter.login.Player;

/**
 * Game Manager
 */
public class GameManager extends JFrame implements Runnable, KeyListener{

	/*
	 * Game Variables 
	 */
	// Application
	private static final long serialVersionUID = 3335979173226476443L;	// Serial Number
	private Random randomNumber;								// Random Number Generator
	public File fileReader;										// File Reader
	private String shootSoundClip = "./res/sound/shoot.wav";	// Shoot Sound
	private AudioInputStream audioIO;							// Audio Reader

	// Time Variables
	private double deltaTime = 0.0;								// Delta Time for synchronize frame rate
	private long lastFrameTime;									// Last Frame Time
	private double maxFPS = 100.0;								// Max FPS
	private double nanoSecond = 1000000000 / maxFPS;			// Nanosecond/maxFPS
	private long lastBonusTime;									// Keep last created bonus time

	// Objects
	private Player playerProfile;								// Player Profile
	private static Plane player;								// Player Object
	private Background background1, background2, background3;	// Background
	public ArrayList<Rocket> rocketList;						// Rockets
	public ArrayList<Rocket> enemyRocketList;					// Enemy Rockets
	public ArrayList<Enemy> enemyList;							// Enemies
	public ArrayList<Bonus> bonusList;							// Bonus (Ammo, Health etc.)
	public Font fontUI;											// UI Font
	public GameAccountUI gAUI;									// UI Game Account

	// Graphic
	private Graphics g;											// Graphics for paint a frame
	private int WIDTH, HEIGHT;									// Game Screen Width and Height
	private BufferedImage  planeImage, bg1, bg2, bg3;			// Images
	private BufferedImage rocketImage, enemyImage, ammoImage;	// Images
	private BufferedImage enemyRocketImage;						// Images

	// Input Keyboard
	boolean isLeftPressed, isRightPressed, isSpacePressed;		// Booleans for multiple key detection

	/**
	 * Game Manager Constructor
	 * @param W:int Screen Width
	 * @param H:int Screen Height
	 */
	public GameManager(int w, int h, Player p, GameAccountUI gAUI){
    	// Window Properties
		WIDTH = w;				// Width
		HEIGHT = h;				// Height
		playerProfile = p;		// Player Profile
    	setLayout(null);		// Set Layout
		addKeyListener(this);	// KeyListener
    	fontUI = new Font("Helvetica", Font.BOLD, 18);

    	rocketList = new ArrayList<Rocket>();
    	enemyRocketList = new ArrayList<Rocket>();
    	enemyList = new ArrayList<Enemy>();
    	bonusList = new ArrayList<Bonus>();
    	randomNumber = new Random();
    	
    	fileReader = new File(shootSoundClip);
    	
    	this.gAUI = gAUI;	// Main menu (login)
    	prepareObjects();	// Prepare Game Objects
	}

	/*
	 * Game Run 
	 */
	@Override
	public void run(){
		while(player.alive){
			lastFrameTime = System.nanoTime(); 	// Set last frame time
			/* Calculate Delta Time
			 * Difference between last render and current time
			 * and last frame time (previous update and render) will increase deltaTime
			 * (For 100 FPS -> every 10.000.000 nanosecond)
			 */
			deltaTime = deltaTime + (System.nanoTime() - lastFrameTime) / nanoSecond;
			//lastFrameTime = System.nanoTime();
			if(deltaTime >= 1) {
				update();
				repaint();
				deltaTime--;
			}
			
			// Also we can use
			//Thread.currentThread();
			//threadPlayer.sleep(30);
		}

		// Update Score
		DataManager dM = new DataManager();
		dM.updateScore(playerProfile.getName(), player.getScore()+"");
		System.out.println("Score updated...");

		// Message Box
		int result = JOptionPane.showConfirmDialog(null, "GAME OVER!!!\nYour score is: " + player.getScore(), "GAME OVER", JOptionPane.CLOSED_OPTION);
    	this.setVisible(false);
    	gAUI.showScores();
    	gAUI.setVisible(true);
	}

	/*
	 * Prepare Objects
	 * Prepare all object properties and create all game object instances
	 */
	public void prepareObjects(){
		/* 	
		 * Background Images
		 * Using different background images for make realistic moving background
		 * three image follow each others and they will reset position their position at the 
		 * end of the scene
		 */
		String bg1Path = "./res/bg1.png"; // First background image path
		String bg2Path = "./res/bg2.png"; // Second background image path
		String bg3Path = "./res/bg3.png"; // Third background image path
        try {
			bg1 = ImageIO.read(new File(bg1Path)); // Read background image
			bg2 = ImageIO.read(new File(bg2Path)); // Read background image
			bg3 = ImageIO.read(new File(bg3Path)); // Read background image
		}
        catch (IOException e) {
			e.printStackTrace();
		}
        // Create background objects
    	background1 = new Background(0, 0, WIDTH, HEIGHT, bg1);
    	background2 = new Background(0, background1.getHeight(), WIDTH, HEIGHT, bg2);
    	background3 = new Background(0, -background1.getHeight(), WIDTH, HEIGHT, bg3);

    	/*
    	 * Game Player Object
    	 */
		String planeImagePath = "./res/plane.png";
        try {
			planeImage = ImageIO.read(new File(planeImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
        int planeWidth = WIDTH*35/100; // Plane image width will be %35 of the screen width
        int planeHeight = planeImage.getHeight() * planeWidth / planeImage.getWidth(); // Set plane image height as ration of width
    	player = new Plane(WIDTH/2 - planeWidth/2, HEIGHT - planeHeight - HEIGHT/50, planeWidth, planeHeight, planeImage); // Create player object
    	player.setScreenWidth(WIDTH); // Set player object screen width value. Player object will be need for calculate right moving border
    	
    	// Rocket Image
    	String rocketImagePath = "./res/rocket.png";
        try {
			rocketImage = ImageIO.read(new File(rocketImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Rocket Image
    	String enemyRocketImagePath = "./res/erocket.png";
        try {
			enemyRocketImage = ImageIO.read(new File(enemyRocketImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    	// Ammo Image
    	String ammoImagePath = "./res/ammo.png";
        try {
			ammoImage = ImageIO.read(new File(ammoImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Update Game Objects
	 */
	public void update(){
		// Check Collisions
		checkCollisions();	// Check Collisions
		for(int ed=0; ed<enemyList.size(); ed++){
			if(enemyList.get(ed).alive==false){
				enemyList.remove(ed);
				player.increaseScore();
			}
		}
		/*
		 * Move Background Images 
		 */
		if(background1.getY()>=HEIGHT){
			background1.setY(background2.getY()-background1.getHeight());
		}
		background1.setY(background1.getY() + background1.speed);
		
		if(background2.getY()>=HEIGHT){
			background2.setY(background3.getY()-background2.getHeight());
		}
		background2.setY(background2.getY() + background2.speed);
		
		if(background3.getY()>=HEIGHT){
			background3.setY(background1.getY()-background3.getHeight());
		}
		background3.setY(background3.getY() + background3.speed);
		
		/*
		 * Player Plane (Flight)
		 */
		if(isLeftPressed){
			player.moveLeft();
		}
		if(isRightPressed){
			player.moveRight();
		}
		if(isSpacePressed){
			if(player.rocketNumber > 0) {
				shootRocket(player.getX(), player.getY(), 0); // Create Rocket
			}
		}
		
		/*
		 * Rocket Update 
		 */
		for(int i=0; i<rocketList.size(); i++){
			rocketList.get(i).move();
			if(rocketList.get(i).getY()<0) {
				rocketList.remove(i);
				//Debug Console
				System.out.println("REMOVED: " + i + " Rocket List Size: " + rocketList.size());
			}
		}

		/*
		 * Handle Bonus Ammo
		 */
		long passedTime = System.currentTimeMillis() - lastBonusTime;
		if(passedTime>5 && bonusList.size()<5 && randomNumber.nextInt(150) == 25 && player.getRocketNumber()<999){
			createAmmoBonus();
			lastBonusTime = System.currentTimeMillis();
		}

		/*
		 * Move Enemy Planes
		 */
		for(int mEC=0; mEC<enemyList.size(); mEC++){
			int moveState = randomNumber.nextInt(100);
			if(enemyList.get(mEC).moveCount==0){
				if(moveState<50){
					enemyList.get(mEC).setMoveState(1);
					enemyList.get(mEC).setMoveCount(randomNumber.nextInt(100));
				}
				else{
					enemyList.get(mEC).setMoveState(0);
					enemyList.get(mEC).setMoveCount(randomNumber.nextInt(100));
				}
			}
			else{
				enemyList.get(mEC).moveLR();				
			}
		}

		for(int eRoc=0; eRoc<enemyList.size(); eRoc++){
			if(enemyList.get(eRoc).getAmmo()>0){
				if(randomNumber.nextInt(50)<25){
					enemyList.get(eRoc).decreaseAmmo();
					enemyShoot(enemyList.get(eRoc).getX()+enemyList.get(eRoc).getWidth()/2, enemyList.get(eRoc).getY()+ enemyList.get(eRoc).getHeight());
				}
			}
		}

		// Move Enemy Rockets
		for(int eRockets=0; eRockets<enemyRocketList.size(); eRockets++){
			enemyRocketList.get(eRockets).move();
		}

		// Move and Check Bonus Position
		for(int b=0; b<bonusList.size(); b++){
			bonusList.get(b).move();
			if(bonusList.get(b).getY()>HEIGHT){
				bonusList.remove(b);
				//Debug Console
				System.out.println("REMOVED: " + b + " Bonus List Size: " + bonusList.size());
			}
		}

		/*
		 * Handle Enemy
		 */
		for(int enemyCounter=0; enemyCounter<enemyList.size(); enemyCounter++){
			// Move enemy to down
			enemyList.get(enemyCounter).moveToDown();
			if(enemyList.get(enemyCounter).getY()>HEIGHT){ // If out of the screen
				enemyList.remove(enemyCounter);	// Remove enemy
				// Debug
				System.out.println("REMOVED ENEMY: " + enemyCounter + " List Size: " + enemyList.size());
			}
		}

		// Create Enemy Max 3 Enemy
		if(enemyList.size()<3 && randomNumber.nextInt(100) == 25){
			createEnemy();
		}
	}

	/*
	 * Check Collisions 
	 */
	public void checkCollisions(){

		// Check Enemy Collisions
		for(int enemyCounter=0; enemyCounter<enemyList.size(); enemyCounter++){
			if(Math.abs((player.getX() + player.getWidth() / 2) - (enemyList.get(enemyCounter).getX() + enemyList.get(enemyCounter).getWidth()/2)) < (player.getWidth()/2 + enemyList.get(enemyCounter).getWidth()/2)){
				if(Math.abs((player.getY() + player.getHeight() / 2) - (enemyList.get(enemyCounter).getY() + enemyList.get(enemyCounter).getHeight()/2)) < (player.getHeight()/2 + enemyList.get(enemyCounter).getHeight()/2)){
					player.decreaseHealth(20); // Decrease health 20
					// Destroy Enemy
					enemyList.remove(enemyCounter);
				}
			}
		}

		// Check Rocket Collisions
		for(int en=0; en<enemyList.size(); en++){
			for(int rc=0; rc<rocketList.size(); rc++){
				if(Math.abs((rocketList.get(rc).getX() + rocketList.get(rc).getWidth() / 2) - (enemyList.get(en).getX() + enemyList.get(en).getWidth()/2)) < (rocketList.get(rc).getWidth()/2 + enemyList.get(en).getWidth()/2)){
					if(Math.abs((rocketList.get(rc).getY() + rocketList.get(rc).getHeight() / 2) - (enemyList.get(en).getY() + enemyList.get(en).getHeight()/2)) < (rocketList.get(rc).getHeight()/2 + enemyList.get(en).getHeight()/2)){
						enemyList.get(en).decreaseHealth(rocketList.get(rc).getDamage());
						rocketList.remove(rc);
						System.out.println(player.getHealth() + " healtth left.");
					}
				}
			}
		}

		// Check Rocket Collisions
		for(int eRocC=0; eRocC<enemyRocketList.size(); eRocC++){
			if(Math.abs((enemyRocketList.get(eRocC).getX() + enemyRocketList.get(eRocC).getWidth() / 2) - (player.getX() + player.getWidth()/2)) < (enemyRocketList.get(eRocC).getWidth()/2 + player.getWidth()/2)){
				if(Math.abs((enemyRocketList.get(eRocC).getY() + enemyRocketList.get(eRocC).getHeight() / 2) - (player.getY() + player.getHeight()/2)) < (enemyRocketList.get(eRocC).getHeight()/2 + player.getHeight()/2)){
					player.decreaseHealth(enemyRocketList.get(eRocC).getDamage());
					enemyRocketList.remove(eRocC);
				}
			}
		}

		// Check Bonus Collisions
		for(int bonusCounter=0; bonusCounter<bonusList.size(); bonusCounter++){
			if(Math.abs((player.getX() + player.getWidth() / 2) - (bonusList.get(bonusCounter).getX() + bonusList.get(bonusCounter).getWidth()/2)) < (player.getWidth()/2 + bonusList.get(bonusCounter).getWidth()/2)){
				if(Math.abs((player.getY() + player.getHeight() / 2) - (bonusList.get(bonusCounter).getY() + bonusList.get(bonusCounter).getHeight()/2)) < (player.getHeight()/2 + bonusList.get(bonusCounter).getHeight()/2)){
					bonusList.remove(bonusCounter);
					player.setRocketNumber(randomNumber.nextInt(40)+10 + player.getRocketNumber());
				}
			}
		} // End of the check Bonus
	}

	/**
	 * Draw Scene
	 * Prepare image for print to screen
	 * @return Image
	 */
	public Image drawScene(){
		BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	    g = bufferedImage.createGraphics();
	    g.setFont(fontUI);

	    // Backgrounds
	    g.drawImage(background1.getSourceImage(), background1.getX(), background1.getY(), background1.getWidth(), background1.getHeight(), this);
	    g.drawImage(background2.getSourceImage(), background2.getX(), background2.getY(), background2.getWidth(), background2.getHeight(), this);
	    g.drawImage(background3.getSourceImage(), background3.getX(), background3.getY(), background3.getWidth(), background3.getHeight(), this);

	    // Draw Rockets
	    for(int i=0; i<rocketList.size(); i++){
	    	g.drawImage(rocketList.get(i).getSourceImage(), rocketList.get(i).getX(), rocketList.get(i).getY(), rocketList.get(i).getWidth(), rocketList.get(i).getHeight(), this);
	    }
	    for(int i=0; i<enemyRocketList.size(); i++){
	    	g.drawImage(enemyRocketList.get(i).getSourceImage(), enemyRocketList.get(i).getX(), enemyRocketList.get(i).getY(), enemyRocketList.get(i).getWidth(), enemyRocketList.get(i).getHeight(), this);
	    }

	    // Draw Bonus
	    for(int p=0; p<bonusList.size(); p++){
	    	g.drawImage(bonusList.get(p).getSourceImage(), bonusList.get(p).getX(), bonusList.get(p).getY(), bonusList.get(p).getWidth(), bonusList.get(p).getHeight(), this);
	    }

	    // Enemy
	    for(int en=0; en<enemyList.size(); en++){
	    	g.drawImage(enemyList.get(en).getSourceImage(), enemyList.get(en).getX(), enemyList.get(en).getY(), enemyList.get(en).getWidth(), enemyList.get(en).getHeight(), this);
	    }

	    // Player
	    g.drawImage(player.getSourceImage(), player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);

	    /* 
	     * User Interface 
	     */
	    // Health
	    g.drawString("Score:" + player.getScore() + " | " + "Rocket: " + player.getRocketNumber(), WIDTH/50, HEIGHT/10);// Score
	    g.drawString("Health", WIDTH-WIDTH/2, HEIGHT/12);// Score
		g.setColor(Color.RED);
	    g.fillRect(WIDTH-WIDTH/2-10, HEIGHT/10, WIDTH/2*player.getHealth()/100, HEIGHT/80);
	    return bufferedImage;
	}

	/*
	 * Paint Image
	 * Draw prepared graphic to window
	 */
	@Override
	public void paint(Graphics g){
        g.drawImage(drawScene(), 0, 0, this); // Draw Buffered Image
	}

	/*
	 * Shoot Rocket
	 */
	public void shootRocket(int x, int y, int direction){
		int rocketWidth = player.getWidth()/30;
        int rocketHeight = rocketImage.getHeight() * rocketWidth / rocketImage.getWidth(); 
		if(player.getRocketNumber()>0) {
			player.decreaseRocket(); // For left wing rocket

	        /* 
	         * Rocket Add
	         * 30 is damage
	         * Direction 0:Up Direction 1:Down Direction
	         */
			rocketList.add(new Rocket(x + player.getWidth()/5, y + player.getHeight()/3, HEIGHT, 30, direction, rocketWidth, rocketHeight, rocketImage)); // Wing 1
			playSound();
		}
		if(player.getRocketNumber()>0) {
			player.decreaseRocket(); // For right wing rocket

	        /* 
	         * Rocket Add
	         * 30 is damage
	         * Direction 0:Up Direction 1:Down Direction
	         */
			rocketList.add(new Rocket(x + player.getWidth()*4/5, y + player.getHeight()/3, HEIGHT, 30, direction, rocketWidth, rocketHeight, rocketImage)); // Wing 2
		}
	}

	/*
	 * Enemy Rocket Shoot 
	 */
	public void enemyShoot(int x, int y){
		int rocketWidth = player.getWidth()/30;
        int rocketHeight = enemyRocketImage.getHeight() * rocketWidth / enemyRocketImage.getWidth();
        // 1 is rocket damage
        enemyRocketList.add(new Rocket(x, y, HEIGHT, 1, 1, rocketWidth, rocketHeight, enemyRocketImage));
	}

	/*
	 * Create Enemy
	 */
	public void createEnemy(){

    	/*
    	 * Game Enemy Object
    	 */
		String planeImagePath = "./res/enemy.png";
        try {
			enemyImage = ImageIO.read(new File(planeImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int enemyWidth = WIDTH*25/100;
        int enemyHeight = enemyImage.getHeight() * enemyWidth / enemyImage.getWidth();
        Enemy enemyObject;
		if(enemyList.size()==0){
			enemyObject = new Enemy(randomNumber.nextInt(WIDTH-enemyWidth), -enemyHeight, enemyWidth, enemyHeight, enemyImage, WIDTH);
		}	
		else{
			if(enemyList.get(enemyList.size()-1).getY()>0){
				enemyObject = new Enemy(randomNumber.nextInt(WIDTH-enemyWidth), -enemyHeight, enemyWidth, enemyHeight, enemyImage, WIDTH);				
			}
			else{
				enemyObject = new Enemy(randomNumber.nextInt(WIDTH-enemyWidth), enemyList.get(enemyList.size()-1).getY()-randomNumber.nextInt(enemyHeight)-enemyHeight*3/2, enemyWidth, enemyHeight, enemyImage, WIDTH);	
			}
		}
        enemyList.add(enemyObject);
	}

	/*
	 * Create Ammo Bonus
	 */
	public void createAmmoBonus(){
		int ammoSize = player.getWidth()/5;
        int randX = randomNumber.nextInt(WIDTH - ammoSize*2) + ammoSize;
        //bonusList.add(new Bonus(randX , -50, ammoSize, ammoSize, ammoImage));
        Bonus b = new Bonus(randX , -50, ammoSize, ammoSize, ammoImage, player);
        bonusList.add(b);
	}

	/*
	 * Play Sound
	 */
	public void playSound(){
		try {
			audioIO = AudioSystem.getAudioInputStream(fileReader.toURI().toURL());
			Clip clip = AudioSystem.getClip();
			clip.open(audioIO);
			clip.start();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Key Listener Actions
	 */
	@Override
	public void keyPressed(KeyEvent a){
		if(a.getKeyCode() == KeyEvent.VK_LEFT) {
			isLeftPressed = true;
		}
		if(a.getKeyCode() == KeyEvent.VK_RIGHT) {
			isRightPressed = true;
		}
		if(a.getKeyCode() == KeyEvent.VK_SPACE) {
			isSpacePressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent a){
		if(a.getKeyCode() == KeyEvent.VK_LEFT) {
			isLeftPressed = false;
		}
		if(a.getKeyCode() == KeyEvent.VK_RIGHT) {
			isRightPressed = false;
		}
		if(a.getKeyCode() == KeyEvent.VK_SPACE) {
			isSpacePressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent a){
		
	}
}// end of the class
