package caro.controllers;

import java.io.IOException;
import java.lang.System.LoggerFinder;
import java.util.Optional;

import caro.MainApp;
import caro.player.Player;
import caro.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignupController {
	private Player player;
	private Client client;
	private Stage primaryStage;
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

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
   	
   
   	public boolean isValidTextField(String emailString, String usernameString, String passwordString) {
   		if ((emailString.contains(" ") || passwordString.contains(" ")) || emailString.contains("	") || passwordString.contains("	")) {
			return false;
		}
		return true;
	}

    @FXML
    void handleSignUp(ActionEvent event) throws IOException {
    	String emailString = emailInputText.getText();
    	String usernameString = usernameInputText.getText();
    	String passwordString = passwordInputText.getText();
    	if (emailString.isEmpty() || usernameString.isEmpty() || passwordString.isEmpty()) {
			warningText.setVisible(true);
		}
    	else if (!isValidTextField(emailString, usernameString, passwordString)) {
    		warningText.setText("Email, password không được có dấu cách!");
    		warningText.setVisible(true);
		}
    	else {
			client.write("signup,"+emailString+","+usernameString+","+passwordString);
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
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
			LoginController loginController = loader.getController();
			loader.setController(loginController);
			loginController.setPrimaryStage(primaryStage);
	        client.setLoginController(loginController);
	        loginController.setup(client);
	        primaryStage.show();
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println(e);
		}
    }

	public void setup(Client client, Player player) {
		// TODO Auto-generated method stub
		this.client = client;
		this.player = player;
	}

	public void handleSignupResponse(String serverMessage) {
		// TODO Auto-generated method stub
		
		Platform.runLater(()->{
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Thông báo!");
	        alert.setHeaderText("Đăng ký thành công!");					
	        alert.setContentText("Đăng nhập để chơi!");
	        ButtonType yesBtn = new ButtonType("Đăng nhập");
			alert.getButtonTypes().clear();
			alert.getButtonTypes().add(yesBtn);
			Optional<ButtonType> optional =  alert.showAndWait();
			if (optional.get().equals(yesBtn)) {
				try {
		    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
		            Parent root = loader.load();
		            primaryStage.setScene(new Scene(root));
					LoginController loginController = loader.getController();
					loader.setController(loginController);
					loginController.setPrimaryStage(primaryStage);
			        client.setLoginController(loginController);
			        loginController.setup(client);
			        primaryStage.show();
				} catch (IOException e) {
					// TODO: handle exception
					System.out.println(e);
				}
			}
		});
		
	}
	public void handleSignupFailResponse(String string) {
		// TODO Auto-generated method stub
		Platform.runLater(()->{
			warningText.setText(string);
			warningText.setVisible(true);
		});
	}

}
