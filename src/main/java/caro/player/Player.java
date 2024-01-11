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
	public Player(String mesString) {
		String res[] = mesString.split(",");
		this.id = Integer.valueOf(res[1]);
		this.email = res[2];
		this.username = res[3];
		this.numberOfGame = Integer.valueOf(res[4]);
		this.numberOfWin = Integer.valueOf(res[5]);
	 	this.numberOfDraw = Integer.valueOf(res[6]);
	 	this.totalScore = Integer.valueOf(res[7]);
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

	public Player(String email, String username, int numberOfGame, int numberOfWin, int numberOfDraw,
			int totalScore) {
		super();
		this.email = email;
		this.username = username;
		this.numberOfGame = numberOfGame;
		this.numberOfWin = numberOfWin;
		this.numberOfDraw = numberOfDraw;
		this.totalScore = totalScore;
	}
	
	
	
	public Player(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
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
	@Override
    public String toString() {
		
        return  email +
                "," + username +
                "," + numberOfGame +
                "," + numberOfWin +
                "," + numberOfDraw +
                "," + totalScore;
    }
	@Override
	public boolean equals(Object obj) {
        // Check if the object is the same instance
        if (this == obj) {
            return true;
        }

        // Check if the object is null or of a different class
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Cast the object to the current class
        Player other = (Player) obj;

        // Compare the attributes for equality
        return username.equals(other.getUsername()) && email.equals(other.getEmail());
    }
}
