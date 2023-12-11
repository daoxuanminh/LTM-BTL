/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Admin
 */
public class ServerThread implements Runnable {

    private Socket socketOfServer;
    private int[][] boardGame = new int[15][15]; 
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
    }

    @Override
    public void run() {
        try {
            // Mở luồng vào ra trên Socket tại Server.
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
//                    Server.serverThreadBus.boardCast(this.getClientNumber(),"global-message"+","+"Client "+messageSplit[2]+": "+messageSplit[1]);
//                }
                if(messageSplit[0].compareTo("mess")==0){
                	System.out.println(message);
                	Server.serverThreadBus.mutilCastSend(message);
//                	Server.serverThreadBus.sendMessageToPersion(clientNumber,"END", "");
                }
                if (messageSplit[0].compareTo("login") == 0) {
                	System.out.println("vao login r");
                	if (this.email.compareTo(messageSplit[1]) == 0 && this.password.compareTo(messageSplit[2])==0) {
                		Server.serverThreadBus.sendMessageToPersion(clientNumber, "OK", clientNumber+","+email+","+username+","+numberOfGame+","+numberOfWin+","+numberOfDraw+","+totalScore);
                		Server.serverThreadBus.sendMessageToPersion(clientNumber,"END", "");
					}
                	else {
                		Server.serverThreadBus.sendMessageToPersion(clientNumber,"NOT-OK", "");
                		Server.serverThreadBus.sendMessageToPersion(clientNumber,"END", "");
					}
				}
                if (messageSplit[0].compareTo("play-with-machine") == 0) {
                	System.out.println("vao play-with-machine r");
//                	Server.serverThreadBus.sendMessageToPersion(clientNumber,"play-with-machine",messageSplit[2]);
                	Server.serverThreadBus.sendMoveToPersion(clientNumber,"play-with-machine","btn_14_14");
				}
            }
        } catch (IOException e) {
            isClosed = true;
            Server.serverThreadBus.remove(clientNumber);
            System.out.println(this.clientNumber+" đã thoát");
            Server.serverThreadBus.sendOnlineList();
            Server.serverThreadBus.mutilCastSend("global-message"+","+"---Client "+this.clientNumber+" đã thoát---");
        }
    }
    public void write(String message) throws IOException{
        os.write(message);
        os.newLine();
        os.flush();
    }
    
    
}