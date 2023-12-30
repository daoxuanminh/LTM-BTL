package caro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import caro.controllers.BoardController;
import caro.controllers.BoardPlayerController;
import caro.controllers.HomeController;
import caro.controllers.LoginController;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Client implements Runnable {
    private Socket socket;
    private BufferedWriter os;
    private BufferedReader is;
    private String serverMessage;
    private Stage primaryStage;
    private HomeController homeController;
    private LoginController loginController;
    private BoardController boardController;
	private BoardPlayerController boardPlayerController;
    
    public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getServerMessage() {
		return serverMessage;
	}

	public Client(Socket socket) {
        this.socket = socket;
        try {
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void write(String mess) {
    	try {
    		os.write(mess);
    		// Xuống Dòng + Xóa bộ đệm
    		os.newLine();
    		os.flush();			
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

    @Override
    public void run() {
        try {        	
            // Vòng lặp vô hạn để luôn lắng nghe
            while (true) {
                // Đọc dữ liệu từ server
            	serverMessage = is.readLine();
//                System.out.println(serverMessage);
                if (serverMessage.isEmpty()) {
                    // Server đã đóng kết nối, có thể xử lý tùy ý
                    continue;
                }

                // Xử lý tin nhắn từ server ở đây
                System.out.println("Received from server: " + serverMessage);
                String serverMessageSplit[] = serverMessage.split(",");
                
                if (serverMessageSplit[0].compareTo("global-message") == 0) {
                	if (homeController != null) {						
                		homeController.handleServerResponse(serverMessage);
					}
				}
                if (serverMessageSplit[0].compareTo("login") == 0) {
                	if (loginController != null) {						
                		loginController.handleLoginResponse(serverMessage);
                	}
				}
                if (serverMessageSplit[0].compareTo("play-with-machine") == 0) {
                	boardController.handleServerResponse(serverMessage);
                	if (boardController != null) {						
					}
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public HomeController getHomeController() {
		return homeController;
	}

	public void setHomeController(HomeController homeController) {
		this.homeController = homeController;
	}

	public void loginRequest(String email, String password) throws IOException, InterruptedException{
		System.out.println(socket);
    	try {
    		String header = "login";
		    os.write(header+","+email+","+password);
			// Xuống Dòng + Xóa bộ đệm
			os.newLine();
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
		
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public BoardController getBoardController() {
		return boardController;
	}

	public void setBoardController(BoardController boardController) {
		this.boardController = boardController;
	}

	public void setBoardPlayController(BoardPlayerController boardPlayerController) {
		// TODO Auto-generated method stub
		this.boardPlayerController = boardPlayerController;
	}
}
