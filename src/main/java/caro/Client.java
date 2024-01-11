package caro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;

import caro.controllers.BoardController;
import caro.controllers.BoardPlayerController;
import caro.controllers.DefyListController;
import caro.controllers.HomeController;
import caro.controllers.LoginController;
import caro.controllers.RankingController;
import caro.controllers.SignupController;
import caro.player.Player;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Client implements Runnable {
	private static LinkedList<Player> listClientIdOnline = new LinkedList<Player>();
	private static LinkedList<Player> listRanking = new LinkedList<Player>();
    public static LinkedList<Player> getListRanking() {
		return listRanking;
	}

	public static void setListRanking(LinkedList<Player> listRanking) {
		Client.listRanking = listRanking;
	}

	public LinkedList<Player> getListClientIdOnline() {
		return listClientIdOnline;
	}
    
	private Socket socket;
    private BufferedWriter os;
    private BufferedReader is;
    private String serverMessage;
    private Stage primaryStage;
    private HomeController homeController;
    private LoginController loginController;
    private BoardController boardController;
	private BoardPlayerController boardPlayerController;
	
	public BoardPlayerController getBoardPlayerController() {
		return boardPlayerController;
	}

	public void setBoardPlayerController(BoardPlayerController boardPlayerController) {
		this.boardPlayerController = boardPlayerController;
	}

	private SignupController signupController;
	private DefyListController defyListController;
	private Player player;
	private RankingController rankingController;
    
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
                if (serverMessageSplit[0].compareTo("room-message") == 0) {
                	if (boardPlayerController != null) {						
                		boardPlayerController.handleRoomMessResponse(serverMessage);
					}
				}
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
                
                if (serverMessageSplit[0].compareTo("signup-success") == 0) {
                	if (signupController != null) {						
                		signupController.handleSignupResponse(serverMessage);
                	}
				}
                if (serverMessageSplit[0].compareTo("signup-fail") == 0) {
                	if (signupController != null) {						
                		signupController.handleSignupFailResponse(serverMessageSplit[1]);
                	}
				}
                if (serverMessageSplit[0].compareTo("play-with-machine") == 0) {
                	if (boardController != null) {						
                		boardController.handleServerResponse(serverMessage);
                		
					}
				}
                if (serverMessageSplit[0].compareTo("defy") == 0) {
                	if (homeController != null) {				
                		homeController.handleDefyResponse(serverMessage);
					}
				}
                if (serverMessageSplit[0].compareTo("cancel-defy") == 0) {
					homeController.getAlert().getOnCloseRequest();
					
				}
                if (serverMessageSplit[0].compareTo("accept-defy") == 0) {
					defyListController.getAlert().close();
				}
                if (serverMessageSplit[0].compareTo("refuse-defy") == 0) {
					defyListController.closeAlert();
					defyListController.refuseAlert();
					
				}
                if (serverMessageSplit[0].compareTo("play-with-player") == 0) {
                	
                	boardPlayerController.handleServerResponse(serverMessage);
				}
                if (serverMessageSplit[0].compareTo("match-making-success") == 0) {
                	if (defyListController != null) {
						defyListController.closeAlert();
					}
					homeController.closeAlert();
					homeController.openBoardPlayerWindow(serverMessageSplit[1], serverMessageSplit[2], serverMessageSplit[3]);
				}
                if (serverMessageSplit[0].compareTo("update-online-list") == 0) {
                	String res[] = serverMessage.split(";");
                	listClientIdOnline.clear();
                	for (int i=0; i<res.length; i++) {
                		Player onlPlayer = new Player(res[i]);
                		if (player != null && !player.equals(onlPlayer)) {
                			listClientIdOnline.add(onlPlayer);                										
						}
					}
//                	listClientIdOnline.add(new Player();
                	System.out.println(listClientIdOnline);
                	
				}
                if (serverMessageSplit[0].compareTo("ranking-list") == 0) {
                	String res[] = serverMessage.split(";");
                	listRanking.clear();
                	for (int i=0; i<res.length; i++) {
                		Player playerR = new Player(res[i]);
                		listRanking.add(playerR);        										
					}
                	System.out.println(listRanking);
                }
            }
        } catch (IOException e) {
        	try {
				socket.close();
				homeController.openAlertDisconnect();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	e.printStackTrace();
            return;
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

	public void setSignUpController(SignupController signupController) {
		// TODO Auto-generated method stub
		this.signupController = signupController;
	}

	public void setDefyListController(DefyListController defyListController) {
		// TODO Auto-generated method stub
		this.defyListController = defyListController;
		
	}

	public void setPlayer(Player player) {
		// TODO Auto-generated method stub
		this.player = player;
	}
	
	public Player getPlayer() {
		// TODO Auto-generated method stub
		return this.player;
	}

	public void setRankingController(RankingController rankingController) {
		// TODO Auto-generated method stub
		this.rankingController = rankingController;
	}
}
