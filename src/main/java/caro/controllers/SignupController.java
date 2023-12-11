package caro.controllers;

import java.io.IOException;

import caro.MainApp;
import caro.player.Player;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignupController {
	
	private Player currentPlayer;
	private Client client;
	
	@FXML
	private Text warningText;

	@FXML
    private TextField emailInputText;

    @FXML
    private PasswordField passwordInputText;

    @FXML
    private Button signupButton;

    @FXML
    private TextField usernameInputText;
    
   boolean sendRequestSignupToServer(){
//    	send request to server to signup
    	return true;
    }

    @FXML
    void handleSignUp(ActionEvent event) throws IOException {
    	if (emailInputText.getText().isEmpty() || usernameInputText.getText().isEmpty() || passwordInputText.getText().isEmpty()) {
			warningText.setVisible(true);
		}
    	else {
    		if (sendRequestSignupToServer() == true) {
    			currentPlayer = new Player(usernameInputText.getText() ,emailInputText.getText(), passwordInputText.getText());
        		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/"+ "Home" + ".fxml"));
                Parent homeParentView = loader.load();
                Scene scene = new Scene(homeParentView);
                HomeController homeController = loader.getController();
                homeController.setPlayer(currentPlayer);
                stage.setScene(scene);
    		}
        	else {
    			warningText.setText("response warning from server!");
    		}
		}
    }
    

    @FXML
    void inputEmailHandler(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
			usernameInputText.requestFocus();
		}
    }

    @FXML
    void inputPasswordHandler(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
			signupButton.requestFocus();
		}
    }

    @FXML
    void inputUsernameHandler(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
			passwordInputText.requestFocus();
		}
    }

    @FXML
    void changSceneLogin(ActionEvent event) {
    	MainApp app = new MainApp();
    	try {
			app.setRoot("Login", "login");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
