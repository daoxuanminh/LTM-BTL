package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


import caro.CaroAI;

/**
 *
 * @author Admin
 */
public class ServerThread implements Runnable {
	private static final String EMPTY = "-";
//    private static final String PLAYER_X = "X";
//    private static final String PLAYER_O = "O";
    private Socket socketOfServer;
    private String[][] boardGame = new String[15][15]; 
    private int clientNumber;
    private BufferedReader is;
    private BufferedWriter os;
    private boolean isClosed;
//    data test
    private String email = "daominh";
    private String username = "Dao Xuan Minh";
    private String password = "a";
    int numberOfGame = 0;
    int numberOfWin = 0;
    int numberOfDraw = 0;
	int totalScore = 0;
    
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
//            write("get-id" + "," + this.clientNumber);
//            Server.serverThreadBus.sendOnlineList();
//            Server.serverThreadBus.mutilCastSend("global-message"+","+"---Client "+this.clientNumber+" đã đăng nhập---");
            String message;
            while (!isClosed) {
                message = is.readLine();
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split(",");
                System.out.println(message);
//                System.out.println("login".length());
//                if(messageSplit[0] == "send-to-global"){
//                    Server.serverThreadBus.boardGameCast(this.getClientNumber(),"global-message"+","+"Client "+messageSplit[2]+": "+messageSplit[1]);
//                }
                if(messageSplit[0].compareTo("global-message")==0){
                	System.out.println(message);
                	Server.serverThreadBus.mutilCastSend(message);
//                	Server.serverThreadBus.sendMessageToPersion(clientNumber,"END", "");
                }
                if (messageSplit[0].compareTo("login") == 0) {
                	System.out.println("vao login r");
                	if (this.email.compareTo(messageSplit[1]) == 0 && this.password.compareTo(messageSplit[2])==0) {
                		Server.serverThreadBus.sendMessageToPersion(clientNumber, "login,OK", clientNumber+","+email+","+username+","+numberOfGame+","+numberOfWin+","+numberOfDraw+","+totalScore);
//                		Server.serverThreadBus.sendMessageToPersion(clientNumber,"END", "");
					}
                	else {
                		Server.serverThreadBus.sendMessageToPersion(clientNumber,"login,NOT-OK", "");
//                		Server.serverThreadBus.sendMessageToPersion(clientNumber,"END", "");
					}
				}
                if (messageSplit[0].compareTo("play-with-machine") == 0) {
                	System.out.println("vao play-with-machine r");
//                	System.out.println();
                	int bestMove[] = new int[100];
                	try {
                		
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