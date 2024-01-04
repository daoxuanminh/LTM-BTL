package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import caro.CaroAI;
import caro.player.Player;
import service.PlayerService;

/**
 *
 * @author Admin
 */
public class ServerThread implements Runnable {
	private static final String EMPTY = "-";
//    private static final String PLAYER_X = "X";
//    private static final String PLAYER_O = "O";
    private Socket socketOfServer;
    private int clientNumber;
    Integer row=0;
	int col=0;
	String player="";
	int competitorID=0;
    private String[][] boardGame = new String[15][15]; 
    private BufferedReader is;
    private BufferedWriter os;
    private boolean isClosed;
//    data test
    private Player user; 
    private String email = "daominh";
    private String username = "Dao Xuan Minh";
    private String password = "a";
    int numberOfGame = 0;
    int numberOfWin = 0;
    int numberOfDraw = 0;
	int totalScore = 0;
	private PlayerService playerService = new PlayerService();
    
	private boolean checkWin(int lastMoveRow, int lastMoveCol, String player) {
        // Kiểm tra hàng
        for (int i = 0; i < 15; i++) {
            if (boardGame[i][lastMoveCol].equals(player)) {
                int count = 1;
                for (int j = 1; j < 5; j++) {
                    if (lastMoveRow + j < 15 && boardGame[lastMoveRow + j][lastMoveCol].equals(player)) {
                        count++;
                    } else {
                        break;
                    }
                }
                for (int j = 1; j < 5; j++) {
                    if (lastMoveRow - j >= 0 && boardGame[lastMoveRow - j][lastMoveCol].equals(player)) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count >= 5) {
                    return true;
                }
            }
        }

        // Kiểm tra cột
        for (int j = 0; j < 15; j++) {
            if (boardGame[lastMoveRow][j].equals(player)) {
                int count = 1;
                for (int i = 1; i < 5; i++) {
                    if (lastMoveCol + i < 15 && boardGame[lastMoveRow][lastMoveCol + i].equals(player)) {
                        count++;
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 5; i++) {
                    if (lastMoveCol - i >= 0 && boardGame[lastMoveRow][lastMoveCol - i].equals(player)) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count >= 5) {
                    return true;
                }
            }
        }

        // Kiểm tra đường chéo phải lên trái
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (boardGame[i][j].equals(player)) {
                    int count = 1;
                    for (int k = 1; k < 5; k++) {
                        if (i + k < 15 && j + k < 15 && boardGame[i + k][j + k].equals(player)) {
                            count++;
                        } else {
                            break;
                        }
                    }
                    for (int k = 1; k < 5; k++) {
                        if (i - k >= 0 && j - k >= 0 && boardGame[i - k][j - k].equals(player)) {
                            count++;
                        } else {
                            break;
                        }
                    }
                    if (count >= 5) {
                        return true;
                    }
                }
            }
        }

        // Kiểm tra đường chéo trái lên phải
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (boardGame[i][j].equals(player)) {
                    int count = 1;
                    for (int k = 1; k < 5; k++) {
                        if (i + k < 15 && j - k >= 0 && boardGame[i + k][j - k].equals(player)) {
                            count++;
                        } else {
                            break;
                        }
                    }
                    for (int k = 1; k < 5; k++) {
                        if (i - k >= 0 && j + k < 15 && boardGame[i - k][j + k].equals(player)) {
                            count++;
                        } else {
                            break;
                        }
                    }
                    if (count >= 5) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (boardGame[i][j].equals(EMPTY)) {
                    return false;
                }
            }
        }
        return true;
    }
	
    public BufferedReader getIs() {
        return is;
    }

    public BufferedWriter getOs() {
        return os;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public ServerThread(Socket socketOfServer, int clientNumber) {
        this.socketOfServer = socketOfServer;
        this.clientNumber = clientNumber;
        System.out.println("Server thread number " + clientNumber + " Started");
        isClosed = false;
        for (int i=0; i < 15; i++)
        {
        	for (int j = 0; j < 15; j++) {
        		boardGame[i][j] = "-";
        	}
        }
    }

    @Override
    public void run() {
        try {
            // Mở luồng vào ra trên Socket tại Server.
        	CaroAI AI = new CaroAI();
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
            System.out.println("Khời động luông mới thành công, ID là: " + clientNumber);
//            Server.serverThreadBus.sendOnlineList();
            Server.serverThreadBus.mutilCastSend("online-list"+","+"---Client "+this.clientNumber+" đã đăng nhập---");
            String message;
            while (!isClosed) {
                message = is.readLine();
                if (message == null) {
                    break;
                }
                System.out.println(Server.listClientIdWaite);
                String[] messageSplit = message.split(",");
                System.out.println("Received from client: "+message);
//                System.out.println("login".length());
//                if(messageSplit[0] == "send-to-global"){
//                    Server.serverThreadBus.boardGameCast(this.getClientNumber(),"global-message"+","+"Client "+messageSplit[2]+": "+messageSplit[1]);
//                }
                if(messageSplit[0].compareTo("room-message")==0){
                	Server.serverThreadBus.sendMessageToPersion(Integer.parseInt(messageSplit[3]), message);
                	Server.serverThreadBus.sendMessageToPersion(clientNumber, message);
//                	Server.serverThreadBus.sendMessageToPersion(clientNumber,"END", "");
                }
                if(messageSplit[0].compareTo("global-message")==0){
//                	System.out.println(message);
                	Server.serverThreadBus.mutilCastSend(message);
//                	Server.serverThreadBus.sendMessageToPersion(clientNumber,"END", "");
                }
                if (messageSplit[0].compareTo("match-making") == 0) {
                	if(Server.listClientIdWaite.isEmpty()) {
                		Server.listClientIdWaite.add(messageSplit[1]);
                		System.out.println(Server.listClientIdWaite);                		
                	}
                	else {
                		Random rd = new Random();
                		Boolean tmpBoolean = rd.nextBoolean();
                		String rolePlay1 = tmpBoolean ? "X" : "O";
                		String rolePlay2 = !tmpBoolean ? "X" : "O";
                		competitorID = Integer.parseInt(Server.listClientIdWaite.remove());
                		Server.serverThreadBus.sendMessageToPersion(clientNumber,"match-making-success,"+rolePlay1, Integer.toString(competitorID));
                		Server.serverThreadBus.sendMessageToPersion(competitorID,"match-making-success,"+rolePlay2, Integer.toString(clientNumber));
					}
				}
                if (messageSplit[0].compareTo("cancer-making") == 0) {
                	Server.listClientIdWaite.remove(Integer.toString(clientNumber));
                	Server.serverThreadBus.sendMessageToPersion(clientNumber,"cancer-making-success", "");
				}
                if (messageSplit[0].compareTo("login") == 0) {
                	System.out.println("vao login r");
                	user = playerService.getPlayerByLoginInf(messageSplit[1], messageSplit[2]);
                	if (user != null) {
                		Server.serverThreadBus.sendMessageToPersion(clientNumber, "login,OK", clientNumber+","+user.getEmail()+","+user.getUsername()+","+user.getNumberOfGame()+","+user.getNumberOfWin()+","+user.getNumberOfDraw()+","+user.getTotalScore());
//                		Server.serverThreadBus.sendMessageToPersion(clientNumber,"END", "");
					}
                	else {
                		Server.serverThreadBus.sendMessageToPersion(clientNumber,"login,NOT-OK", "");
//                		Server.serverThreadBus.sendMessageToPersion(clientNumber,"END", "");
					}
				}
                if (messageSplit[0].compareTo("play-with-player") == 0) {
					System.out.println("vào xử lý play with player");
					
					try {
						
                		if (messageSplit[2].compareTo("agree-play-again") == 0) {
                			Server.serverThreadBus.sendMoveToPersion(competitorID,"play-with-player,"+player,"agrre-play-again");
                			Server.serverThreadBus.sendMoveToPersion(clientNumber,"play-with-player,"+player,"agrre-play-again");
                			resetBoard();
                		}
                		else if (messageSplit[2].compareTo("play-again") == 0) {
//                			gui loi moi den nguoi choi con lai de đấu lai
                			player = messageSplit[1];
                			Server.serverThreadBus.sendMoveToPersion(competitorID,"play-with-player,"+player,"play-again");
                			
                		}else if (messageSplit[2].compareTo("disagrre-play-again") == 0) {
                			
						}else {
							String btnId[] = messageSplit[2].split("_");
                    		row = Integer.parseInt(btnId[1]);
                    		col = Integer.parseInt(btnId[2]);
                    		player = messageSplit[1]; 
                    		boardGame[row][col] = player;
                    		competitorID = Integer.parseInt(messageSplit[3]);
                    		if (checkWin(row, col, player)) {
//                    			gửi tin nhắn người chơi nào win
                    			Server.serverThreadBus.sendMoveToPersion(competitorID,"play-with-player,"+player,"WIN!");
                    			Server.serverThreadBus.sendMoveToPersion(clientNumber,"play-with-player,"+player,"WIN!");
                    			resetBoard();
    						}
                    		else if (isBoardFull()) {
//                    			gửi tin nhắn bàn cờ full
                    			Server.serverThreadBus.sendMoveToPersion(competitorID,"play-with-player,"+player,"DRAW!");
                    			Server.serverThreadBus.sendMoveToPersion(clientNumber,"play-with-player,"+player,"DRAW!");
    						}
                    		else {
//                    			gửi tin nhắn nước đi cho đối thủ
                        		Server.serverThreadBus.sendMoveToPersion(competitorID ,"play-with-player,"+player,messageSplit[2]);
    						}
//							Server.serverThreadBus.sendMoveToPersion(competitorID,"play-with-player,"+player,"disagrre-play-again");
						}
                		
                		
                		
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(e);
						e.printStackTrace();
					}
				}
                if (messageSplit[0].compareTo("play-with-machine") == 0) {
                	int bestMove[] = new int[100];
                	try {
                		if (messageSplit[0].compareTo("play-again") == 0) {
							resetBoard();
						}
                		String btnId[] = messageSplit[2].split("_");
                		int row = Integer.parseInt(btnId[1]);
                		int col = Integer.parseInt(btnId[2]);
                		String player = messageSplit[1]; 
                		boardGame[row][col] = player;
                		if (checkWin(row, col, player)) {
                			Server.serverThreadBus.sendMoveToPersion(clientNumber,"play-with-machine,X","WIN!");
                			resetBoard();
						}
                		else if (isBoardFull()) {
                			Server.serverThreadBus.sendMoveToPersion(clientNumber,"play-with-machine,X","DRAW!");
						}
                		else {
                			Thread.sleep(1000);
                    		bestMove = AI.getBestDefensiveMove(boardGame, "O");
                    		boardGame[bestMove[0]][bestMove[1]] = "O";
                    		Server.serverThreadBus.sendMoveToPersion(clientNumber,"play-with-machine,O","btn_"+bestMove[0]+"_"+bestMove[1]);
						}
                		
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(e);
						System.out.println(e);
					}
//                	System.out.println();
//                	Server.serverThreadBus.sendMessageToPersion(clientNumber,"play-with-machine",messageSplit[2]);
                	
				}
            }
        } catch (IOException e) {
            isClosed = true;
            Server.serverThreadBus.remove(clientNumber);
            System.out.println(this.clientNumber+" đã thoát");
            Server.serverThreadBus.sendOnlineList();
            Server.listClientIdWaite.remove(Integer.toString(clientNumber));
//            Server.serverThreadBus.mutilCastSend("global-message"+","+"---Client "+this.clientNumber+",Đã thoát!");
        }
    }
    
    public void write(String message) throws IOException{
        os.write(message);
        os.newLine();
        os.flush();
    }
    
    public void resetBoard() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				boardGame[i][j] = "-";
			}
		}
	}
}