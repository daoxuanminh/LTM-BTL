package caro.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import caro.Client;
import caro.player.Player;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BoardPlayerController implements Initializable{
	private Alert alert;
    @FXML
    private Pane boardInformation;
    @FXML
    private VBox viewMessVbox;
    @FXML
    private GridPane gridBoard;
    @FXML
    private TextField inputMessagesTextField;
    @FXML
    private Text myName;
    @FXML
    private Text userRolePlay;
    @FXML
    private Text competitorRole;
    @FXML
    private Text turnText;
    
    private Player player;
    
    private Player competitor;
    
    private Client client;
    private int competitorId;
    private String rolePlay;
    private String header = "play-with-user";
    private Button btns[][];
	private String board[][];
	
	public void setDisableBtn(boolean bool) {
		if (bool) {
			turnText.setText("Lượt đối thủ!");
		}
		else {
			turnText.setText("Lượt của bạn!");
		}
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				btns[i][j].setDisable(bool);
			}
		}
	}

	public void setup(Client client, Player player) {
		// TODO Auto-generated method stub
		this.client = client;
		this.player = player;
		myName.setText(this.player.getUsername());
		if (rolePlay.compareTo("X") == 0) {
			turnText.setText("Lượt của bạn!");
			userRolePlay.setText("X");
			competitorRole.setText("O");
			setDisableBtn(false);
		}
		else {
			userRolePlay.setText("O");
			userRolePlay.setStyle("-fx-text-fill: green;");
			competitorRole.setText("X");
			competitorRole.setStyle("-fx-text-fill: red;");
			turnText.setText("Lượt đối thủ!");
			setDisableBtn(true);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		board = new String[15][15];
		btns = new Button[15][15];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				board[i][j] = "";
				Button btnButton = new Button();
				btnButton.setPrefSize(40.0, 40.0);
				btnButton.setId("btn_"+i+"_"+j);
//				System.out.println("btn_"+i+"_"+j);
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
							if (rolePlay.compareTo("X") == 0) {
								btnButton.setText(rolePlay);
								btnButton.setStyle("-fx-font: 18 arial; -fx-text-fill: red;");
								client.write("play-with-player,"+rolePlay+","+btnId+","+competitorId);
								board[btnRow][btnCol] = rolePlay;
								setDisableBtn(true);								
							}else {
								btnButton.setText(rolePlay);
								btnButton.setStyle("-fx-font: 18 arial; -fx-text-fill: green;");
								client.write("play-with-player,"+rolePlay+","+btnId+","+competitorId);
								board[btnRow][btnCol] = rolePlay;
								setDisableBtn(true);								
							}
							
						}
					}
				});
				gridBoard.add(btnButton, j, i);
				btns[i][j] = btnButton;
			}
		}
		
	}

	public String getRolePlay() {
		return rolePlay;
	}

	public void setRolePlay(String rolePlay) {
		this.rolePlay = rolePlay;
	}

	public int getCompetitorId() {
		return competitorId;
	}

	public void setCompetitorId(int competitorId) {
		this.competitorId = competitorId;
	}
	
	public void resetBoard(){
		Platform.runLater(()->{
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					btns[i][j].setText("");
					board[i][j] = "";
				}
			}
		});
	}
	
	public void closeAlert() {
		Platform.runLater(()->{
			alert.close();
		});
	}
	
	public void playAgain() {
		Platform.runLater(()->{
			if (rolePlay.compareTo("X") == 0) {
				setDisableBtn(false);
			}
			else {
				setDisableBtn(true);
			}
		});
	}
	
	public void openAlertPlayAgain() {
		Platform.runLater(()->{
			alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Thông báo!");
	        alert.setHeaderText("Đối thủ mời bạn đấu lại!");					
	        alert.setContentText("Bạn có muốn chơi lại!");
	        ButtonType yesBtn = new ButtonType("Có");
			ButtonType noBtn = new ButtonType("Không");
			alert.getButtonTypes().clear();
			alert.getButtonTypes().addAll(yesBtn, noBtn);
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get()==yesBtn) {
				String messageString = "match-making," + player.getId() + ",";
				client.write("play-with-player,"+rolePlay+",agree-play-again");
				resetBoard();
				setDisableBtn(false);
			}
			if (option.get()==noBtn) {
				client.write("play-with-player,"+rolePlay+",disagree-play-again");
				client.getHomeController().closeStage();
				alert.close();
			}
		});
	}
	
	public void openAlertWaitePlayAgain() {
		Platform.runLater(()->{
			alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Thông báo!");
	        alert.setHeaderText("Đang chờ đối thủ xác nhận!");					
	        alert.setContentText("Vui lòng chờ!");
			alert.show();

		});
	}
	
	@FXML
	private void sendMessageGlobal() {
		String message = inputMessagesTextField.getText();
		if (!message.isEmpty()) {
			client.write("room-message,"+player.getUsername()+","+message+","+competitorId);
		}
	}
	@FXML
    public void sendMessEnterKey(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (!inputMessagesTextField.getText().isEmpty()) {				
				sendMessageGlobal();
			}
		}
    }
	public void handleServerResponse(String serverResponse) {
		// TODO Auto-generated method stub
		String serverResponseSplit[] = serverResponse.split(",");
		String header = serverResponseSplit[0];
		if (serverResponseSplit[2].compareTo("WIN!") == 0) {
			Platform.runLater(()->{
				alert = new Alert(AlertType.CONFIRMATION);
		        alert.setTitle("Thông báo!");
		        if (serverResponseSplit[1].compareTo(rolePlay) == 0) {
		        	alert.setHeaderText("Bạn đã chiến thắng!");					
				}
		        else {
		        	alert.setHeaderText("Bạn đã thua!");					
				}
		        alert.setContentText("Bạn có muốn chơi lại!");
		        ButtonType yesBtn = new ButtonType("Có");
				ButtonType noBtn = new ButtonType("Không");
				alert.getButtonTypes().clear();
				alert.getButtonTypes().addAll(yesBtn, noBtn);
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get()==yesBtn) {
					openAlertWaitePlayAgain();
					client.write("play-with-player,"+rolePlay+",play-again");
				}
				if (option.get()==noBtn) {
					alert.close();
				}
			});
			
		}
		else {
			String playerO = serverResponseSplit[1];
			String btnId = serverResponseSplit[2];
			Integer btnRow = Integer.parseInt(btnId.split("_")[1]);
			Integer btnCol = Integer.parseInt(btnId.split("_")[2]);
			Platform.runLater(()->{
				if (board[btnRow][btnCol].compareTo("") == 0) {
					Button btnButton = btns[btnRow][btnCol];
					btnButton.setText(playerO);
					if (playerO.compareTo("O") == 0) {
						btnButton.setStyle("-fx-font: 18 arial; -fx-text-fill: green;");						
					}
					else {
						btnButton.setStyle("-fx-font: 18 arial; -fx-text-fill: red;");
					}
					board[btnRow][btnCol] = playerO;
					setDisableBtn(false);
				}
			});
		}
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
	public void handleRoomMessResponse(String response) {
		// TODO Auto-generated method stub
		if (response.split(",")[0].compareTo("room-message") == 0) {
			Platform.runLater(() -> appendToChatArea(response));		
		}
	}
}