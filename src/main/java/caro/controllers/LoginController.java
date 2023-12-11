package caro.controllers;

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
import client.Client;

public class LoginController implements Initializable{

	private Client client;
	private Player currentPlayer=null;
	
	public LoginController() throws UnknownHostException, IOException {
		client = new Client();
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
    boolean sendRequestLoginToServer() throws IOException, InterruptedException {
    	System.out.println(emailField.getText() + " -- " + passwordField.getText());
    	String res[] = client.loginRequest(emailField.getText(), passwordField.getText()).split(",");
    	if (res[0].equals("OK")) {
    		this.currentPlayer = new Player(Integer.valueOf(res[1]), res[2], res[3], Integer.valueOf(res[4]) , Integer.valueOf(res[5]), Integer.valueOf(res[6]),
    				Integer.valueOf(res[7]));
    		return true;
		}
    	else {
    		warningField.setVisible(true);
			return false;
		}
    }
    @FXML
    void requestLogin(ActionEvent event) throws IOException, InterruptedException {
//    	get username and password send to server
//    	System.out.println();
    	if (sendRequestLoginToServer() == true) {
    		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/"+ "Home" + ".fxml"));
            Parent homeParentView = loader.load();
            Scene scene = new Scene(homeParentView);
            HomeController homeController = loader.getController();
//            homeController.setPlayer(currentPlayer);
//            System.out.println("login info player: "+currentPlayer.getUsername());
            homeController.setup(client, currentPlayer);
            loader.setController(homeController);
            stage.setScene(scene);
		}
    	else {
			
		}
    	
    }

    @FXML
    void register(ActionEvent event) {
    	MainApp app = new MainApp();
    	try {
			app.setRoot("Signup", "Signup");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
