package jetfighter.login;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jetfighter.objects.GameManager;

/**
 * Game Account User Interface 
 * Login User Interface for Sign-in
 * If user doesn't not exist on database show message
 * If user is exists on the database start game as current user
 * Player can sign-up with help button
 * Manage and handle user works
 * 
 */

@SuppressWarnings("serial")
public class GameAccountUI extends JFrame implements ActionListener{

	/*
	 * Variables
	 */
	private static final  int width = 384;							// Window Width Size
	private static final  int height = 300;         				// Window height Size
	private JButton      helpButton, loginButton;					// Buttons
	private JTextField   nameField;									// Name Field
	private JPasswordField passwordField;							// Password Field
	private JLabel nameLabel, passwordLabel,gameHeader; 			// Labels
	private JLabel recLabel,  msgLabel;								// Labels
	private DataManager dManager;									// Data Manager
	private Player currentPlayer;									// Current Player

	public static final int WIDTH = 500;	// Game Screen Width
	public static final int HEIGHT = 700;	// Game Screen Height
	public static GameManager GM;			// Game Manager
	public static Thread gameThread;		// Game Thread
	private static String GAMETITLE = "Jet Fight";

	/*
	 * GameAccountUI Constructor
	 */
    public GameAccountUI(){
    	// Window Properties
    	setLayout(null);											// Set Layout
    	Font headerFont = new Font("SansSerif", Font.BOLD, 20);		// Font Type
		Color bgColor = new Color(15, 100, 50); 					// Background Color
		getContentPane().setBackground(bgColor);					// Set Background
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				// Close Operation
		setPreferredSize(new Dimension(width, height));				// Set Dimension of Window
	    setVisible(true);											// Enable Frame
		setResizable(false);										// Disable resize window
		
		// Labels Properties
		gameHeader = new JLabel("JET FIGHTER");		  	 			// Name label
		gameHeader.setFont(headerFont);								// Set Font Type
		gameHeader.setHorizontalAlignment(SwingConstants.CENTER);	// Text Alignment
		gameHeader.setSize(200,20);				 	 				// Label Location
		gameHeader.setLocation(width/2-100, 10);	 				// Label position
		gameHeader.setForeground(Color.white);	 	 				// Label Text Color
		add(gameHeader);								 			// Add to layout window
		
		nameLabel = new JLabel("Name");			  	 				// Name label
		nameLabel.setHorizontalAlignment(SwingConstants.RIGHT); 	// Text Alignment
		nameLabel.setSize(75,20);				 	 				// Label Location
		nameLabel.setLocation(width/2 - 130, height/3-60); 			// Label position
		nameLabel.setForeground(Color.white);	 	 				// Label Text Color
		add(nameLabel);								 				// Add to layout window

		passwordLabel = new JLabel("Password");   	 				// Password Label
		passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);	// Text Alignment
		passwordLabel.setSize(75,20);		  	  	 				// Size of the Password Label
		passwordLabel.setLocation(width/2 - 130, height/3-10);		// Password Label position
		passwordLabel.setForeground(Color.white); 	 				// Label Text Color
		add(passwordLabel);					 	  	 				// Add to layout window 

		msgLabel = new JLabel("Please Login"); 	 					// Message Label
		msgLabel.setHorizontalAlignment(SwingConstants.CENTER);		// Text Alignment
		msgLabel.setSize(150,20);		  	  	 					// Size of the Login Label
		msgLabel.setLocation(width/2 - 75, height - 50);			// Message Label position
		msgLabel.setForeground(Color.white); 	 					// Label Text Color
		add(msgLabel);					 	  	 					// Add to layout window 

		recLabel = new JLabel("RECORDS"); 	 						// Record Label
		recLabel.setHorizontalAlignment(SwingConstants.CENTER);		// Text Alignment
		recLabel.setSize(380,150);		  	  	 					// Size of the Login Label
		recLabel.setLocation(0, height/2 - 20);						// Record Label position
		recLabel.setForeground(Color.white); 	 					// Label Text Color
		add(recLabel);		

		// Text Field Properties
		nameField=new JTextField();									// Name Field
		nameField.setLocation(width/2 - 50, height/3-65);			// Name Field Location
		nameField.setSize(150, 30);									// Size of the Name Field
		nameField.setBackground(Color.black);						// Background Color
		nameField.setForeground(Color.green);						// Text Color
		add(nameField);												// Add to layout window

		passwordField=new JPasswordField();							// Password Field
		passwordField.setLocation(width/2 - 50, height/3-15);		// Password Field Location
		passwordField.setSize(150, 30);								// Size of the Password Field
		passwordField.setBackground(Color.black);					// Background Color
		passwordField.setForeground(Color.green);					// Text Color
		add(passwordField);											// Add to layout window

		// Buttons Properties
		loginButton = new JButton("Login");							// Login Button
		loginButton.setLocation(width/2 - 75, height/2-20);			// Button Location
		loginButton.setSize(70, 20);								// Button Size
		loginButton.addActionListener(this);						// Add Action Listener
		add(loginButton);											// Add button to layout window

		helpButton = new JButton("Help");							// Login Button
		helpButton.setLocation(width/2 + 15, height/2-20);			// Button Location
		helpButton.setSize(70, 20);									// Button Size
		helpButton.addActionListener(this);							// Add Action Listener
		add(helpButton);											// Add button to layout window
	    pack();

	    showScores();
	    
    }
    
    // Show Scores
    public void showScores(){
    	dManager = new DataManager();
    	ArrayList<Player> pHighScores = dManager.getHighScores();
    	recLabel.setText("<html><center>=====================<br>RECORDS<br>=====================");
    	for(int p=0; p<pHighScores.size(); p++){
    		recLabel.setText(recLabel.getText() + "<br>" + pHighScores.get(p).getName() + " " + pHighScores.get(p).getScore());
    	}
    	recLabel.setText(recLabel.getText()+"</center></html>");
    	
    	if(pHighScores.size()==0){
    		recLabel.setText("<html><center>=====================<br>RECORD NOT FOUND<br>=====================</center></html>");
    	}
    }
    
    // Action Performer (Button Actions)
    public void actionPerformed(ActionEvent e){
    	if(e.getSource()==loginButton) {
    		login();	// Login
    	}
    	if(e.getSource()==helpButton) {
    		help();		// Help
    	}
	}
    
    // Login
    @SuppressWarnings("deprecation")
	public void login(){
    	dManager = new DataManager();
    	currentPlayer = dManager.getPlayer(nameField.getText(), passwordField.getText());
   
    	if(currentPlayer!=null){
    		System.out.println(currentPlayer.toString());
    		msgLabel.setText("Login Success");
    	}
    	else{
    		msgLabel.setText("User not found..");
    	}
    	
    	if(currentPlayer!=null) {
        	this.setVisible(false);
    		GM = new GameManager(WIDTH, HEIGHT, currentPlayer, this);
    		GM.setVisible(true);
    		GM.setTitle(GAMETITLE);
    		GM.setLocationRelativeTo(null);
    		GM.setSize(WIDTH, HEIGHT);
    		GM.setResizable(false);
    		GM.setLocationRelativeTo(null);
    		GM.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    		gameThread = new Thread(GM);
    		gameThread.start(); 
    		GM.addWindowListener(new java.awt.event.WindowAdapter() {
    		    @Override
    		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
    		        if (JOptionPane.showConfirmDialog(GM, 
    		            "Do you want to exit?", "Exit", 
    		            JOptionPane.YES_NO_OPTION,
    		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
    		        	GM.gAUI.setVisible(true);
    		        	GM.dispose();
    		        	gameThread.stop();
    		        	showScores();
    		        }
    		    }
    		});
    	}
    }

    // Help
    public void help(){
    	msgLabel.setText("Help and SignUp");
    	int result = JOptionPane.showConfirmDialog(null, "You have to register. Do you want create a new user?", "Help", JOptionPane.YES_NO_OPTION);
    	if(result == 0) {
    		register();
    	}
    }

    // Register
    public void register(){
    	String name = "";
        String pass = "";
        name = JOptionPane.showInputDialog(null, "Enter name for new user: ", "");
        pass = JOptionPane.showInputDialog(null, "Enter password for new user: ", "");
        dManager = new DataManager();
        if(name!=null && pass!=null){
        	if(name.length()!=0 && pass.length()!=0 || name.contains(" " ) || pass.contains(" ")){
            	if(dManager.registerUser(name, pass)){
            		msgLabel.setText("Registeration is success for user " + name);
            	}
            	else{
            		msgLabel.setText("Cannot create a new user.");
            	}
        	}
        	msgLabel.setText("Error: Fill all fields!");
        }
        else{
        	msgLabel.setText("Error: Fill all fields!");
        }
        showScores();
    }
 }
