package caro.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import caro.player.Player;
import caro.Client;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BoardController implements Initializable {
	private Player player;
	private Client client;
	private String rolePlay;
	private Button btns[][];
	private String board[][];
	
	public void resetBoard(){
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				btns[i][j].setText("");
				board[i][j] = "";
			}
		}
	}
	
	@FXML
    private Text myName;
	
	@FXML
    private Text turnText;
	
	@FXML
    private Pane boardInformation;

    @FXML
    private GridPane gridBoard;
    
	

	public Text getTurnText() {
		return turnText;
	}

	public void setTurnText(Text turnText) {
		this.turnText = turnText;
	}

	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		myName.setText(this.player.getUsername());
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setup(Client client, Player newplayer) {
		System.out.println(client.getSocket());
		this.client = client;
		this.player = newplayer;
	}
	
	public void setDisableBtn(boolean bool) {
		if (bool) {
			turnText.setText("AI TURN!");
		}
		else {
			turnText.setText("YOUR TURN!");
		}
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				btns[i][j].setDisable(bool);
			}
		}
	}
	
	public void handleServerResponse(String serverResponse) {
		String serverResponseSplit[] = serverResponse.split(",");
		String header = serverResponseSplit[0];
		client.getPlayer().setPlaying(true);
		if (serverResponseSplit[2].compareTo("WIN!") == 0) {
			Platform.runLater(()->{
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Thông báo!");
		        alert.setHeaderText("Bạn đã chiến thắng!");
		        alert.setContentText("Bạn có muốn chơi lại!");
		        ButtonType yesBtn = new ButtonType("Có");
				ButtonType noBtn = new ButtonType("Không");
				alert.getButtonTypes().clear();
				alert.getButtonTypes().addAll(yesBtn, noBtn);
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get()==yesBtn) {
					client.write("play-with-machine,X,play-again");
					resetBoard();
					setDisableBtn(false);
				}
				
				if (option.get()==noBtn) {
					alert.close();
					client.getPlayer().setPlaying(false);
				}
			});
			
		}
		else if (header.compareTo("play-with-machine") == 0) {
			String playerO = serverResponseSplit[1];
			String btnId = serverResponseSplit[2];
			Integer btnRow = Integer.parseInt(btnId.split("_")[1]);
			Integer btnCol = Integer.parseInt(btnId.split("_")[2]);
			Platform.runLater(()->{
				if (board[btnRow][btnCol].compareTo("") == 0) {
					Button btnButton = btns[btnRow][btnCol];
					btnButton.setText(playerO);
					btnButton.setStyle("-fx-font: 18 arial; -fx-text-fill: green;");
					board[btnRow][btnCol] = "O";
					setDisableBtn(false);
				}
			});
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		turnText.setText("YOUR TURN!");
		board = new String[15][15];
		btns = new Button[15][15];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				board[i][j] = "";
				Button btnButton = new Button();
				btnButton.setPrefSize(40.0, 40.0);
				btnButton.setId("btn_"+i+"_"+j);
				System.out.println("btn_"+i+"_"+j);
				btnButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
	//						message type ("play-with-machine,X,btn_i_j")
						System.out.println(btnButton.getId());
						String btnId = btnButton.getId();
						String btnIdSplit[] = btnId.split("_");
						Integer btnRow = Integer.parseInt(btnIdSplit[1]);
						Integer btnCol = Integer.parseInt(btnIdSplit[2]);
						if(board[btnRow][btnCol].compareTo("") == 0)
						{
							btnButton.setText("X");
							btnButton.setStyle("-fx-font: 18 arial; -fx-text-fill: red;");
							client.write("play-with-machine,X,"+btnId);
							board[btnRow][btnCol] = "X";
							setDisableBtn(true);
							
						}
					}
				});
				gridBoard.add(btnButton, j, i);
				btns[i][j] = btnButton;
			}
		}
	}

	
	
}
