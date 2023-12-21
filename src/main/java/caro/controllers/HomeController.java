package caro.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import caro.player.Player;
import caro.room.Room;
import caro.Client;
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
	private Button btn_play_now;
	
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
}

