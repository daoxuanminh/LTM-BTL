package caro.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import caro.player.Player;
import caro.room.Room;
import client.Client;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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


public class HomeController extends BaseController implements Initializable{
	@FXML
	private Button btn_play_with_machine;
	
	@FXML
	private ScrollPane scrollPane;
	
	@FXML
	private Button btn_play;
	
	@FXML
	private Button btn_find_room;
	
	@FXML
	private Button btn_create_room;
	
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
    
    private Player player;
    
    private Client client;
    
    private ArrayList<Room> listRoom;
    
    public Client getClient() {
		return client;
	}
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

	public void setup(Client client, Player newplayer) {
		System.out.println(client.getSocket());
		this.client = client;
		this.player = newplayer;
		this.username.setText(player.getUsername());
		this.client.getMessageFromServer(viewMessVbox);
	}
	
	@FXML
    public void sendMessage(ActionEvent event) {
		String mesString = inputMessagesTextField.getText();
		if (!mesString.isEmpty()) {				
			client.sendMessage(this.player.getUsername(), mesString);
//			client.getMessageFromServer(viewMessVbox);
			inputMessagesTextField.clear();
		}
		
    }
	
	public static void addMess(String messFromServer, VBox vbox) {
		System.out.println(messFromServer);
		String usernameString = messFromServer.split(",")[1];
		String messString = messFromServer.split(",")[2];
		HBox newMessBox = new HBox();
		Text usernameText = new Text(usernameString+": ");
		Text messText = new Text(messString);
		newMessBox.getChildren().addAll(usernameText, messText);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vbox.getChildren().add(newMessBox);
				
			}
		});
	}
	
	@FXML
    public void sendMessEnterKey(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (!inputMessagesTextField.getText().isEmpty()) {				
				sendMessage(null);
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
			Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/"+ "Board" + ".fxml"));
            Parent boardParentView = loader.load();
            Scene scene = new Scene(boardParentView);
            BoardController boardController = loader.getController();
//            homeController.setPlayer(currentPlayer);
//            System.out.println("login info player: "+currentPlayer.getUsername());
            boardController.setup(client, player);
            loader.setController(boardController);
            stage.setScene(scene);
			stage.show();
			
//			Stage stage = new Stage();
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/fxml/"+ "Board" + ".fxml"));
//            Parent root = loader.load();
//            Scene scene = new Scene(root);
//            BoardController boardController = loader.getController();
//            player.setPlaying(true);
//            boardController.setPlayer(player);
//            boardController.setClient(client);
////            System.out.println(player.getUsername());
//            stage.setTitle("Board");
//			stage.setScene(new Scene(root));
//		    stage.initModality(Modality.APPLICATION_MODAL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		try {
//			System.out.println(client);
//			this.client = new Client();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
}

