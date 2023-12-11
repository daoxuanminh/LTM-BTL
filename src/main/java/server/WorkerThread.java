package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//import caro.player.Player;
 
public class WorkerThread extends Thread {
    private Socket socket;
    private BufferedWriter os;
    private BufferedReader is;
//    data tesst
    private String id = "1";
	private int numberOfGame = 0;
	private int numberOfWin = 0;
	private int numberOfDraw = 0;
//	private boolean isOline;
//	private boolean isPlaying;
	private int totalScore = 0;
    private String email = "daominh";
    private String username = "Dao Xuan Minh";
    private String password = "1";
     
    
    
    public WorkerThread(Socket socket) throws IOException {
        this.socket = socket;
        is = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }
    
    public void send() {
		
	}
    
    public void handleLogin(String email, String password) throws IOException {
    	if (this.email.equals(email) && this.password.equals(password)) {
    		
    		os.write("OK"+"-"+id+"-"+this.email+"-"+username+"-"+numberOfGame+"-"+numberOfWin+"-"+numberOfDraw+"-"+totalScore);
    		// Xuống Dòng + Xóa bộ đệm
			os.newLine();
			os.flush();
		}else {
			System.out.println("server phan hoi");
			os.write("Sai thong tin");
    		// Xuống Dòng + Xóa bộ đệm
			os.newLine();
			os.flush();
		}
    	
	}
 
    public void run() {
        System.out.println("Processing: " + socket);
        try {
        	String messString = is.readLine();
        	System.out.println(messString);
            String messStrings[] = messString.split("-");
            if (messStrings[0].equals("login")) {
            	System.out.println("header: "+messStrings[0]);
            	System.out.println("email: "+messStrings[1]);
            	System.out.println("pass: "+messStrings[2]);
            	handleLogin(messStrings[1], messStrings[2]);
            	
			}
        } catch (IOException e) {
            System.err.println("Request Processing Error: " + e);
        }
        System.out.println("Complete processing: " + socket);
    }
}