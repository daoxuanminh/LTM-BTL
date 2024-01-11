package caro.controllers;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;



import caro.Client;
import caro.player.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class DefyListController implements Initializable{
	@FXML
    private TableView<Player> table;
	
    @FXML
    private TableColumn<Player, Void> action;

    @FXML
    private TableColumn<Player, Integer> score;

    @FXML
    private TableColumn<Player, Integer> numberGame;

    @FXML
    private TableColumn<Player, String> userName;

	private Client client;

	private Player player;
	
	private Alert alert;
	
	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	private ArrayList<Button> btns = new ArrayList<Button>();

	private ObservableList<Player> studentList;
	
	public void setup(Client client, Player player) {
		this.client = client;
		this.player = player;
		studentList = FXCollections.observableArrayList(client.getListClientIdOnline());

		addButtonToTable();
	    score.setCellValueFactory(new PropertyValueFactory<Player, Integer>("totalScore"));
	    numberGame.setCellValueFactory( new PropertyValueFactory<Player, Integer>("numberOfGame"));
	    userName.setCellValueFactory(new PropertyValueFactory<Player, String>("username"));
	    table.setItems(studentList);
	    
	}
	
	private void addButtonToTable() {
//        TableColumn<Player, Void> colBtn = new TableColumn("Action");
		
        Callback<TableColumn<Player, Void>, TableCell<Player, Void>> cellFactory = new Callback<TableColumn<Player, Void>, TableCell<Player, Void>>() {
            @Override
            public TableCell<Player, Void> call(final TableColumn<Player, Void> param) {
                final TableCell<Player, Void> cell = new TableCell<Player, Void>() {

                    private final Button btn = new Button("Mời đấu");
                    
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Player data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedPlayer: " + data);
                            client.write("defy,"+data.getId()+","+player);
                            btn.setText("Đã mời");
                            btn.setDisable(true);
                            ProgressIndicator progressIndicator = new ProgressIndicator();
                            alert = new Alert(Alert.AlertType.INFORMATION);
                	        alert.setTitle("Chờ đối thủ phản hồi");
                	        alert.setHeaderText(null);
                	        alert.setContentText("Vui lòng chờ trong giây lát!");
                	        alert.setGraphic(progressIndicator);
                	        alert.getButtonTypes().clear();
                	        ButtonType cancelButtonType = new ButtonType("Hủy");
                	        alert.getButtonTypes().add(cancelButtonType);
                	        Optional<ButtonType> option = alert.showAndWait();
                	        if(option.get().equals(cancelButtonType)) {
                	        	client.write("cancel-defy,"+data.getId());
                	        }
                	        
                        });
                        btns.add(btn);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        action.setCellFactory(cellFactory);

    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void refuseAlert() {
		Platform.runLater(()->{
			alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Thông báo!");
	        alert.setHeaderText("Đối thủ đang bận!");					
	        alert.setContentText("Chọn OK để tiếp tục!");
	        ButtonType yesBtn = new ButtonType("OK");
//			ButtonType noBtn = new ButtonType("Không");
			alert.getButtonTypes().clear();
			alert.getButtonTypes().add(yesBtn);
			alert.showAndWait();
		});
	}
	
	public void closeAlert() {
		Platform.runLater(()->{
			this.alert.close();
		});
		
	}

	
}
