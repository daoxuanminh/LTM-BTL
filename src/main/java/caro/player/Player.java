package caro.player;

public class Player {
	private int id;
	private String email;
	private String username;
	private String password;
	private int numberOfGame;
	private int numberOfWin;
	private int numberOfDraw;
	private boolean isOline;
	private boolean isPlaying;
	private int totalScore;
	
	public Player(int id, String email, String username, String password, int numberOfGame, int numberOfWin,
			int numberOfDraw, boolean isOline, boolean isPlaying, int totalScore) {
		super();
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.numberOfGame = numberOfGame;
		this.numberOfWin = numberOfWin;
		this.numberOfDraw = numberOfDraw;
		this.isOline = isOline;
		this.isPlaying = isPlaying;
		this.totalScore = totalScore;
	}


	public Player(int id, String email, String username, int numberOfGame, int numberOfWin, int numberOfDraw,
			int totalScore) {
		super();
		this.id = id;
		this.email = email;
		this.username = username;
		this.numberOfGame = numberOfGame;
		this.numberOfWin = numberOfWin;
		this.numberOfDraw = numberOfDraw;
		this.totalScore = totalScore;
	}


	public Player(String username, String email, String password, int numberOfGame, int numberOfWin, int numberOfDraw, boolean isOline, boolean isPlaying, int totalScore) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.numberOfGame = numberOfGame;
		this.numberOfWin = numberOfWin;
		this.numberOfDraw = numberOfDraw;
		this.isOline = isOline;
		this.isPlaying = isPlaying;
		this.totalScore = totalScore;
	}
	
	public Player(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.numberOfGame = 0;
		this.numberOfWin = 0;
		this.numberOfDraw = 0;
		this.isOline = true;
		this.isPlaying = false;
		this.totalScore = 0;
	}
	
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getNumberOfGame() {
		return numberOfGame;
	}
	public void setNumberOfGame(int numberOfGame) {
		this.numberOfGame = numberOfGame;
	}
	public int getNumberOfWin() {
		return numberOfWin;
	}
	public void setNumberOfWin(int numberOfWin) {
		this.numberOfWin = numberOfWin;
	}
	public int getNumberOfDraw() {
		return numberOfDraw;
	}
	public void setNumberOfDraw(int numberOfDraw) {
		this.numberOfDraw = numberOfDraw;
	}
	public boolean isOline() {
		return isOline;
	}
	public void setOline(boolean isOline) {
		this.isOline = isOline;
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	
}
