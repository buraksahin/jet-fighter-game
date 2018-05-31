package jetfighter.login;

/**
 * Player
 * This class will keep player profiles
 * and profile properties to ensure game login and
 * score system
 */
public class Player{
	/*
	 * Variables
	 */
	private int	id;			// User ID
	private String name;	// User Name
	private String pass;	// User Password
	private int score;		// User Score

	/*
	 * Constructors
	 */
	public Player(){
		super();
	}
	public Player(int id, String name, String pass, int score){
		super();
		this.id = id;
		this.name = name;
		this.pass = pass;
		this.score = score;
	}

	/*
	 * Getters and Setters
	 */
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getPass(){
		return pass;
	}
	public void setPass(String pass){
		this.pass = pass;
	}
	public int getScore(){
		return score;
	}
	public void setScore(int score){
		this.score = score;
	}
	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", pass=" + pass + ", score=" + score + "]";
	}
}
