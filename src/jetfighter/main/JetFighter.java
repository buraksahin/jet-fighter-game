package jetfighter.main;

import jetfighter.login.GameAccountUI;

/**
 * @author Burak Sahin
 * 
 * JFrame Project
 * 
 * */
public class JetFighter{

	// Game Screen Properties
	private static String GAMETITLE = "Jet Fight";  

	// Main method
	public static void main(String args[]){
		GameAccountUI myLayout = new GameAccountUI();
		myLayout.setTitle(GAMETITLE);       		// Title of the Windows
		myLayout.setLocationRelativeTo(null); 		// Center the frame
		myLayout.setVisible(true);            		// Set Visible
	}
}
// end of the class
