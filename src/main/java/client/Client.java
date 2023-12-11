package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import caro.controllers.BoardController;
import caro.controllers.HomeController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
 
public class Client {
    private String SERVER_IP = "127.0.0.1";
    private int SERVER_PORT = 7777;
    private Socket socket;
    private BufferedWriter os;
    private BufferedReader is;
    
    
    
    public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Client() throws UnknownHostException, IOException {
    	socket = new Socket(SERVER_IP, SERVER_PORT);
    	System.out.println(socket);
    	is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
    
    public void sendMessage(String username, String mess) {
    	try {
    		String header = "mess";
		    os.write(header+","+username+","+mess);
			// Xuống Dòng + Xóa bộ đệm
			os.newLine();
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public void getMessageFromServer(Node node) {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (socket.isConnected()) {
					try {
						String messFromServer = is.readLine();
						System.out.println("mess getMessageFromServer: "+messFromServer);
						if (messFromServer.split(",")[0].compareTo("mess") == 0) {							
							HomeController.addMess(messFromServer, (VBox) node);
						}
						if (messFromServer.split(",")[0].compareTo("play-with-machine") == 0) {
//							BoardController.move((Button) gridPane.getScene().lookup(messStringSplit[2]));
						}
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
				
			}
		}).start();
	}
    
    public void sendMove(String header, String des, String btn_id) {
		try {
		    os.write(header+","+des+","+btn_id);
			// Xuống Dòng + Xóa bộ đệm
			os.newLine();
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public void getMove() {
		try {
			String messMoveString = is.readLine();
			System.out.println(messMoveString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    public void getMoveFromServer(GridPane gridPane) {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				while (socket.isConnected()) {
					try {
						String messFromServer = is.readLine();
						System.out.println(messFromServer);
						String messStringSplit[] = messFromServer.split(","); 
						if (messStringSplit[0].compareTo("play-with-machine") == 0) {							
//							BoardController.move(messStringSplit[2]);
						}
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
				
			}
		}).start();
    	
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				btnButton.setText("O");
//				btnButton.setStyle("-fx-font: 18 arial; -fx-text-fill: green;");
//				
//			}
//		}).start();
//		
//		
	}

	public String loginRequest(String email, String password) throws IOException, InterruptedException{
		System.out.println(socket);
    	try {
    		String header = "login";
		    os.write(header+","+email+","+password);
			// Xuống Dòng + Xóa bộ đệm
			os.newLine();
			os.flush();
			String message, data="";
			while (true) {
				message = is.readLine();
				if (message.split(",")[0].compareTo("END")==0) {
					break;
				}
				else {
					data = message;
				}
				System.out.println(message);
				
			}
            return data;
		} catch (IOException e) {
			e.printStackTrace();
			return "False";
		}
    	
	}
	
//	Close connect
	public void closeConnect() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}

