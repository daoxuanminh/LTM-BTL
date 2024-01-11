/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import caro.player.Player;
import service.DB;
import service.PlayerService;

/**
 *
 * @author Admin
 */
public class Server {

    public static volatile ServerThreadService serverThreadService;
    public static Socket socketOfServer;
    public static LinkedList<Player> listClientIdWaite = new LinkedList<Player>();
    public static LinkedList<Player> listClientOnline = new LinkedList<Player>();
	public static List<Player> listRanking = new ArrayList<Player>();
    
    
    
    public static void main(String[] args) {
//    	Connection connection = DB.getConnection();
        ServerSocket listener = null;
        serverThreadService = new ServerThreadService();
        System.out.println("Server is waiting to accept user...");
        int clientNumber = 0;

        // Mở một ServerSocket tại cổng 7777.
        // Chú ý bạn không thể chọn cổng nhỏ hơn 1023 nếu không là người dùng
        // đặc quyền (privileged users (root)).
        try {
            listener = new ServerSocket(7777);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                100, // corePoolSize
                100, // maximumPoolSize
                10, // thread timeout
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(8) // queueCapacity
        );
        try {
            while (true) {
                // Chấp nhận một yêu cầu kết nối từ phía Client.
                // Đồng thời nhận được một đối tượng Socket tại server.
                socketOfServer = listener.accept();
                ServerThread serverThread = new ServerThread(socketOfServer, clientNumber++);
                serverThreadService.add(serverThread);
                System.out.println("Số thread đang chạy là: "+serverThreadService.getLength());
                executor.execute(serverThread);
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                listener.close();
                System.out.println("closed connect");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}