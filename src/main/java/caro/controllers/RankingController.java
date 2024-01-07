package caro.controllers;

import java.util.Optional;

import caro.Client;
import caro.player.Player;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class RankingController {

    @FXML
    private TableView<Player> rankingTable;

    @FXML
    private TableColumn<Player, Integer> score;

    @FXML
    private TableColumn<Void, Integer> stt;

    @FXML
    private TableColumn<Player, String> username;

    private Client client;

	private Player player;
	
	private ObservableList<Player> listRanking;
	
	
	public void setup(Client client, Player player) {
		this.client = client;
		this.player = player;
		listRanking = FXCollections.observableArrayList(client.getListRanking());
	    score.setCellValueFactory(new PropertyValueFactory<Player, Integer>("totalScore"));
	    username.setCellValueFactory(new PropertyValueFactory<Player, String>("username"));
	    stt.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(rankingTable.getItems().indexOf(column.getValue()) + 1));
	    rankingTable.setItems(listRanking);
	}
}
