package jetfighter.login;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataManager{
	// Variables
	private FileInputStream fileReader;
	private FileWriter fileWriter;
	private String fileName = "./Player.dat";
	private DataInputStream dIS;
	private BufferedReader bReader;
	private Player tempPlayer = null;
	private String tempString;
	private int numberOfUser;
	
	public DataManager(){
		 initializeManager();
	}
	
	public void initializeManager(){
		try{
			if(!Paths.get(fileName).toFile().exists()){				
				// If file doesn't exist create new file
				fileWriter = new FileWriter(fileName);
			}
			else{
				fileWriter = new FileWriter(fileName, true);
			}
			
			// File Read
			fileReader = new FileInputStream(fileName);
			dIS = new DataInputStream(fileReader);
			bReader = new BufferedReader(new InputStreamReader(dIS));

		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	public void close(){
		try {
			bReader.close();
			dIS.close();
			fileReader.close();
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateScore(String name, String score){
		try {
			String writeString = "";
			ArrayList<String> playerStringList = new ArrayList<String>();
			while((tempString = bReader.readLine()) != null){
				String[] sArr = tempString.split(" ");
				if(sArr[1].equals(name)){
					if(Integer.parseInt(sArr[3])< Integer.parseInt(score)){
						// If current score higher than old score update with new record
						writeString = sArr[0] + " " + sArr[1] + " " + sArr[2] + " " + score;						
					}
					else{
						// Keep old score
						writeString = sArr[0] + " " + sArr[1] + " " + sArr[2] + " " + sArr[3];
					}
				}
				else{
					writeString = tempString;
				}
				playerStringList.add(writeString);
			}
			close();
			fileWriter = new FileWriter(fileName);
			for(int i=0; i<playerStringList.size(); i++){
				fileWriter.write(playerStringList.get(i) + "\n");
			}			
			close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public boolean registerUser(String name, String pass){
		System.out.println(name + " " + pass);
		if(checkUserIsExist(name)){
			String tempStr = numberOfUser + " " + name + " " + pass + " 0\n";
			System.out.println(tempStr);
			try {
				fileWriter.write(tempStr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			close();
			return true;
		}
		else{
			close();
			return false;			
		}
	}
	
	public boolean checkUserIsExist(String name){
		numberOfUser = 0;
		try {
			while((tempString = bReader.readLine()) != null){
				String[] sArr = tempString.split(" ");
				if(sArr[1].equals(name)){
					return false;
				}
				numberOfUser++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public Player getPlayer(String name, String pass){
		try {
			while((tempString = bReader.readLine()) != null){
				String[] sArr = tempString.split(" ");
				if(sArr[1].equals(name) && sArr[2].equals(pass)){
					tempPlayer = new Player(Integer.parseInt(sArr[0]), sArr[1], sArr[2], Integer.parseInt(sArr[3]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		close();
		return tempPlayer;
	}
	
	public ArrayList<Player> getHighScores(){
		ArrayList<Player> playersScores = new ArrayList<Player>();
		ArrayList<Player> playerHighScores = new ArrayList<Player>();
		try {
			while((tempString = bReader.readLine()) != null){
				String[] sArr = tempString.split(" ");
				playersScores.add(new Player(Integer.parseInt(sArr[0]), sArr[1], sArr[2], Integer.parseInt(sArr[3])));
			}
			
			for(int i=0; i<3; i++){
				// Find maximum score
				int max = 0; // Index number of the high score
				for(int u=1; u<playersScores.size(); u++){
					if(playersScores.get(u).getScore() > playersScores.get(max).getScore()){
						max = u;
					}
				}
				playerHighScores.add(playersScores.get(max));
				playersScores.remove(max);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		close();
		System.out.println(playerHighScores);
		return playerHighScores;
	}
}
