package caro.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import caro.player.Player;
import caro.room.Room;
import caro.Client;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


public class HomeController extends BaseController implements Initializable{
	private Alert alert;
	private Stage primaryStage;
	@FXML
	private Button btn_play_with_machine;
	
	@FXML
	private ScrollPane scrollPane;
	
	@FXML
	private Button btn_play_now;
	
	@FXML
	private Button btn_defy;
	
	@FXML
	private Button btn_logout;
	
	@FXML
    private TextField inputMessagesTextField;

    @FXML
    private Text messageSenderName;

    @FXML
    private Text messageSenderName1;

    @FXML
    private HBox rankingButton;

    @FXML
    private Text username = new Text();

    @FXML
    private VBox viewMessVbox;

    @FXML
    private AnchorPane viewMessages;
    
    private Timeline timeline;
    
    private Player player;
    
    private Client client;
    
    private ArrayList<Room> listRoom;
    
    private String messRecive;
    
	public void setClient(Client client) {
		System.out.println(client.getSocket());
		this.client = client;
	}
	public void setPlayer(Player player) {
		this.player = player;
		String nameString = player.getUsername();
		username.setText((String)nameString);
	}
	public Player getPlayer() {
		return player;
	}
	

	public String getMessRecive() {
		return messRecive;
	}
	public void setMessRecive(String messRecive) {
		this.messRecive = messRecive;
	}
	public void setup(Client client, Player newplayer) {
//		System.out.println(client.getSocket());
		this.client = client;
		this.player = newplayer;
		this.username.setText(player.getUsername());
		String messString = this.client.getServerMessage();
	}
	@FXML
	private void sendMessageGlobal() {
		String message = inputMessagesTextField.getText();
		if (!message.isEmpty()) {
			client.write("global-message,"+player.getUsername()+","+message);
		}
	}
	public void closeStage() {
		
	} 
	public void handleServerResponse(String response) {
	      // Xử lý phản hồi từ server và hiển thị trong giao diện người dùng
		if (response.split(",")[0].compareTo("global-message") == 0) {
			Platform.runLater(() -> appendToChatArea(response));		
		}
//		cac truong hop khac
	}
	public void appendToChatArea(String messFromServer) {
		System.out.println(messFromServer);
		String usernameString = messFromServer.split(",")[1];
		String messString = messFromServer.split(",")[2];
		HBox newMessBox = new HBox();
		Text usernameText = new Text(usernameString+": ");
		Text messText = new Text(messString);
		newMessBox.getChildren().addAll(usernameText, messText);
		viewMessVbox.getChildren().add(newMessBox);
		inputMessagesTextField.clear();
	}
	
	
	
	@FXML
    public void sendMessEnterKey(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (!inputMessagesTextField.getText().isEmpty()) {				
				sendMessageGlobal();
			}
		}
    }
	
	@FXML
    void showRankingWindow(MouseEvent event) throws IOException {
		openModal("Ranking");
    }
	
	@FXML
    void openBoardWindow(MouseEvent event) {
//		openModal("Board");
		try {
			Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/"+ "Board" + ".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            BoardController boardController = loader.getController();
            player.setPlaying(true);
            boardController.setup(client, player);
            client.setBoardController(boardController);
            stage.setTitle("Board");
            stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			System.out.println(e);
		}
    }
	
    public void openBoardPlayerWindow(String rolePlay, String competitorId) {
//		openModal("Board");
		Platform.runLater(()->{
			try {
				Stage stage = new Stage();
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(getClass().getResource("/fxml/"+ "BoardPlayer" + ".fxml"));
	            Parent root = loader.load();
	            Scene scene = new Scene(root);
	            BoardPlayerController boardController = loader.getController();
	            player.setPlaying(true);
	            boardController.setRolePlay(rolePlay);
	            boardController.setCompetitorId(Integer.parseInt(competitorId));
	            boardController.setup(client, player);
	            client.setBoardPlayController(boardController);
	            stage.setTitle("BoardPlayer");
	            stage.initModality(Modality.APPLICATION_MODAL);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		});
    }
	
	public void closeAlert() {
		Platform.runLater(()->{
			this.alert.close();
		});
		
	}
	@FXML
	void openClock(ActionEvent event) {
	    String messageString = "match-making," + player.getId() + ",";
	    client.write(messageString);

//	    System.out.println(messageString);

	    Platform.runLater(() -> {
	        ProgressIndicator progressIndicator = new ProgressIndicator();
	        // Create an Alert with custom content
	        this.alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Searching for Match");
	        alert.setHeaderText(null);
	        alert.setContentText("Searching for match...");
	        alert.setGraphic(progressIndicator);
	        alert.getButtonTypes().clear();
	        ButtonType cancer = new ButtonType("Hủy ghép");
	        alert.getButtonTypes().add(cancer);

	        // Set the alert to not close on focus loss
	        // alert.initOwner(null);

	        Optional<ButtonType> option = alert.showAndWait();

	        if (option.isPresent() && option.get() == cancer) {
	        	String messCancerString = "cancer-making," + player.getId() + ",";
	            client.write(messCancerString);
//	            System.out.println(messCancerString);
	            
	        }
	    });
	}
	
	
	@FXML
    void openBoardPlayerWindow(ActionEvent event) {
//		openModal("Board");
		try {
			Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/"+ "BoardPlayer" + ".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            BoardPlayerController boardPlayerController = loader.getController();
            player.setPlaying(true);
            boardPlayerController.setup(client, player);
            client.setBoardPlayController(boardPlayerController);
            stage.setTitle("Board");
            stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			System.out.println(e);
		}
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	public void setPrimaryStage(Stage primaryStage2) {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage2;
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
}

