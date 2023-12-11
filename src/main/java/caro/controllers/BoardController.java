package caro.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import caro.player.Player;
import client.Client;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BoardController implements Initializable {
	private Player player;
	private Client client;
	@FXML
    private Text myName;
	
	@FXML
    private Pane boardInformation;

    @FXML
    private GridPane gridBoard;
    
	

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
//		this.username.setText(player.getUsername());
//		this.client.getMessageFromServer(viewMessVbox);
		this.client.getMoveFromServer(gridBoard);
	}
	
	public void move(String id) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Button btnButton = (Button) gridBoard.getScene().lookup(id);
				btnButton.setText("O");
				btnButton.setStyle("-fx-font: 18 arial; -fx-text-fill: green;");
				
			}
		});
		
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				Button btnButton = new Button();
				btnButton.setPrefSize(40.0, 40.0);
				btnButton.setId("btn_"+i+"_"+j);
				btnButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						btnButton.setText("X");
						btnButton.setStyle("-fx-font: 18 arial; -fx-text-fill: red;");
//						System.out.println(btnButton.getId());
						client.sendMove("play-with-machine", "server", btnButton.getId());
//						gridBoard.getScene().lookup(client.receiveMove());
					}
					
				});
				gridBoard.add(btnButton, i, j);
			}
		}
	}
	
}
