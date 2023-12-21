package caro.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import caro.MainApp;
import caro.player.Player;
import caro.Client;

public class LoginController implements Initializable{

	private Client client;
	private Player player=null;
	private Stage primaryStage;

    public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Text warningField;

    @FXML
    private TextField emailField;
    
    public void initialize(URL location, ResourceBundle resources) {
    	emailField.setText("daominh");
    	passwordField.setText("a");
    }
    
    @FXML
    void inputPassword(KeyEvent event) {
//    	System.out.println(event.getCode());
    	if (event.getCode() == KeyCode.ENTER) {
    		loginBtn.requestFocus();
		}
    }

    @FXML
    void inputUsername(KeyEvent event) {
//    	System.out.println(event.getCode());
    	if (event.getCode() == KeyCode.ENTER) {
			passwordField.requestFocus();
		}
    }
    
    @FXML
    public void sendLoginMessage(ActionEvent event) throws IOException, InterruptedException {
    	String email, password;
    	email = emailField.getText();
    	password = passwordField.getText();
    	System.out.println(email+"--"+password);
    	if (!email.isEmpty() && !password.isEmpty()) {
			String data = "login,"+email+","+password;
			client.write(data);
		}
    	else if (email.isBlank() || password.isBlank()) {
    		warningField.setVisible(true);
		}else {
    		warningField.setVisible(true);
		}
    }

	public void setPlayer(Player player) {
		this.player = player;
		
	}

	public void setup(Client client) {
		// TODO Auto-generated method stub
		this.client = client;
	}

    @FXML
    void register(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SignUp.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println(e);
		}
    }
    @FXML
    public void changeToHomePage() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
			HomeController homeController = loader.getController();
			loader.setController(homeController);
	        client.setHomeController(homeController);
	        homeController.setup(client,player);
	        primaryStage.show();
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println(e);
		}
    }
    
	public void handleLoginResponse(String serverMessage) {
		String res[] = serverMessage.split(",");
	    if (res[1].equals("OK")) {
			this.player = new Player(Integer.valueOf(res[2]), res[3], res[4], Integer.valueOf(res[5]) , Integer.valueOf(res[6]), Integer.valueOf(res[7]),
					Integer.valueOf(res[8]));
			Platform.runLater(()->{changeToHomePage();});
		}
		else {
			warningField.setVisible(true);
		}
		
	}

}
