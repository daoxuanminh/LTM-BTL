package caro.controllers;

import caro.Client;
import caro.player.Player;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class BoardPlayerController {

    @FXML
    private Pane boardInformation;

    @FXML
    private GridPane gridBoard;

    @FXML
    private Text myName;

    @FXML
    private Text turnText;
    
    private Player player;
    
    private Player competitor;
    
    private Client client;
    
    private String header = "play-with-user";

	public void setup(Client client, Player player) {
		// TODO Auto-generated method stub
		this.client = client;
		this.player = player;
	}

}