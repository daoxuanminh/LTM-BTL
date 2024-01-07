package caro;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import caro.controllers.LoginController;
import caro.player.Player;

public class MainApp extends Application {
	private Client client;
	private Player player;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private TextArea chatArea;
    private TextField messageField;
    
    
    @Override
    public void start(Stage primaryStage) {
    	try {
			socket = new Socket("127.0.0.1", 7777);
			System.out.println("socket: "+socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Kết nối đến server
        connectToServer(primaryStage);
        // Tạo giao diện người dùng
//    	player = new Player("daominh", "daominh", "a");
//    	Scene scene = setRootGui("Login", primaryStage);
    	try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/"+ "Login" + ".fxml"));
            Parent homeParentView = loader.load();
            Scene scene = new Scene(homeParentView);
			LoginController loginController = loader.getController();
	        client.setLoginController(loginController);
	        loginController.setup(client);
	        loginController.setPrimaryStage(primaryStage);
	        loader.setController(loginController);
	        primaryStage.setScene(scene);
	        primaryStage.setOnCloseRequest(event -> {
	            System.out.println("User is closing the window!");
	            client.write("close-connect");
	        });
	        primaryStage.show();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			System.out.println(e);
			try {
				stop();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
    }

    private void connectToServer(Stage stage) {
    	client = new Client(socket);
    	client.setPrimaryStage(stage);
        new Thread(client).start();
    }

    @Override
    public void stop() throws Exception {
        // Đóng kết nối khi ứng dụng đóng
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
