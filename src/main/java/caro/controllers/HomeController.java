package caro.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import caro.player.Player;
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

public class HomeController extends BaseController implements Initializable {
	private Alert alert;

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

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

	@FXML
	private Text numberGame;

	@FXML
	private Text score;

	@FXML
	private Text win;

	@FXML
	private Text draw;

	private Timeline timeline;

	private Player player;

	private Client client;

	private String messRecive;

	public void setClient(Client client) {
		System.out.println(client.getSocket());
		this.client = client;
	}

	public void setPlayer(Player player) {
		this.player = player;
		String nameString = player.getUsername();
		username.setText((String) nameString);
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
		// System.out.println(client.getSocket());
		this.client = client;
		this.player = newplayer;
		this.username.setText(player.getUsername());
		this.numberGame.setText(player.getNumberOfGame() + "");
		this.win.setText(player.getNumberOfWin() + "");
		this.draw.setText(player.getNumberOfDraw() + "");
		this.score.setText(player.getTotalScore() + "");
		String messString = this.client.getServerMessage();
	}

	@FXML
	private void sendMessageGlobal() {
		String message = inputMessagesTextField.getText();
		if (!message.isEmpty()) {
			client.write("global-message," + player.getUsername() + "," + message);
		}
	}

	public void closeStage() {

	}

	public void handleServerResponse(String response) {
		// Xử lý phản hồi từ server và hiển thị trong giao diện người dùng
		if (response.split(",")[0].compareTo("global-message") == 0) {
			Platform.runLater(() -> appendToChatArea(response));
		}
		// cac truong hop khac
	}

	public void appendToChatArea(String messFromServer) {
		System.out.println(messFromServer);
		String usernameString = messFromServer.split(",")[1];
		String messString = messFromServer.split(",")[2];
		HBox newMessBox = new HBox();
		Text usernameText = new Text(usernameString + ": ");
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
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/" + "Ranking" + ".fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			RankingController rankingController = loader.getController();
			rankingController.setup(client, player);
			client.setRankingController(rankingController);
			stage.setTitle("Ranking");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@FXML
	void openBoardWindow(MouseEvent event) {
		// openModal("Board");
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/" + "Board" + ".fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			BoardController boardController = loader.getController();
			client.getPlayer().setPlaying(true);
			boardController.setup(client, player);
			client.setBoardController(boardController);
			stage.setTitle("Board");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			// stage.setOnCloseRequest(e -> {
			// System.out.println("User is closing the window Board!");
			// client.getPlayer().setPlaying(false);
			// e.consume();
			// });
			stage.show();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void openBoardPlayerWindow(String rolePlay, String competitorId, String competitorName) {
		// openModal("Board");
		Platform.runLater(() -> {
			try {
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/fxml/" + "BoardPlayer" + ".fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root);
				BoardPlayerController boardController = loader.getController();
				client.getPlayer().setPlaying(true);
				boardController.setRolePlay(rolePlay);
				boardController.setCompetitorId(Integer.parseInt(competitorId), competitorName);
				boardController.setup(client, player);
				client.setBoardPlayController(boardController);
				stage.setTitle("BoardPlayer");
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setScene(scene);
				stage.show();
				stage.setOnCloseRequest(e -> {
					System.out.println("User is closing the window BoardPlayer!");
					client.getPlayer().setPlaying(false);
					client.write("update-user");
					// e.consume();
				});
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		});
	}

	public void closeAlert() {
		Platform.runLater(() -> {
			alert.close();
		});

	}

	@FXML
	void openClock(ActionEvent event) {

		String messageString = "match-making," + player.getId() + ",";
		client.write(messageString);

		// System.out.println(messageString);
		Platform.runLater(() -> {
			client.getPlayer().setPlaying(true);
			System.out.println(client.getPlayer().isPlaying());
			ProgressIndicator progressIndicator = new ProgressIndicator();
			// Create an Alert with custom content
			this.alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Searching for Match");
			alert.setHeaderText(null);
			alert.setContentText("Searching for match...");
			alert.setGraphic(progressIndicator);
			alert.getButtonTypes().clear();
			ButtonType cancel = new ButtonType("Hủy ghép");
			alert.getButtonTypes().add(cancel);

			// Set the alert to not close on focus loss
			// alert.initOwner(null);
			alert.setOnCloseRequest(e->{
				String messCancelString = "cancel-making," + player.getId() + ",";
				client.write(messCancelString);
				// System.out.println(messCancelString);
				client.getPlayer().setPlaying(false);
			});

			Optional<ButtonType> option = alert.showAndWait();
			if (option.isPresent() && option.get() == cancel) {
				String messCancelString = "cancel-making," + player.getId() + ",";
				client.write(messCancelString);
				// System.out.println(messCancelString);
				client.getPlayer().setPlaying(false);
			}
		});
	}

	@FXML
	void openDefyList(ActionEvent event) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/" + "DefyList" + ".fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			DefyListController defyListController = loader.getController();
			defyListController.setup(client, player);
			client.setDefyListController(defyListController);
			stage.setTitle("Defy list");
			stage.initModality(Modality.APPLICATION_MODAL);
			// stage.setOnCloseRequest(e -> {
			// System.out.println("User is closing the window BoardPlayer!");
			// client.write("end-game");
			// });
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@FXML
	void openBoardPlayerWindow(ActionEvent event) {
		// openModal("Board");
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/" + "BoardPlayer" + ".fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			BoardPlayerController boardPlayerController = loader.getController();
			player.setPlaying(true);
			boardPlayerController.setup(client, player);
			client.setBoardPlayController(boardPlayerController);
			stage.setTitle("Board");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setOnCloseRequest(e -> {
				System.out.println("User is closing the window BoardPlayer!");
				client.getPlayer().setPlaying(false);
				client.write("update-user");
				// e.consume();
			});
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
			client.write("logout,");
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	public void handleDefyResponse(String serverMessage) {
		// TODO Auto-generated method stub
		String serverMessageSplit[] = serverMessage.split(",");
		if (client.getPlayer().isPlaying() == true) {
			client.write("refuse-defy," + serverMessageSplit[1]);
		} else {
			Platform.runLater(() -> {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Thông báo!");
				alert.setHeaderText(serverMessageSplit[2] + " thách đấu bạn!");
				alert.setContentText("Bạn có muốn chấp nhận!");
				ButtonType yesBtn = new ButtonType("Có");
				ButtonType noBtn = new ButtonType("Không");
				alert.getButtonTypes().clear();
				alert.getButtonTypes().addAll(yesBtn, noBtn);
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get() == yesBtn) {
					System.out.println("btnOK");
					client.write("accept-defy," + serverMessageSplit[1]);
				} else if (option.get() == noBtn) {
					client.write("refuse-defy," + serverMessageSplit[1]);
					// client.getPlayer().setPlaying(false);
				} else {
					alert.getOnCloseRequest();
				}
			});
			// client.write("accept-defy,"+serverMessageSplit[1]);
		}
	}

	public void openAlertDisconnect() {
		// TODO Auto-generated method stub
		Platform.runLater(() -> {
			client.getPlayer().setPlaying(true);
			System.out.println(client.getPlayer().isPlaying());
			ProgressIndicator progressIndicator = new ProgressIndicator();
			// Create an Alert with custom content
			this.alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thông báo");
			alert.setHeaderText("Mất kết nối đến Server!");
			alert.setContentText("Đang khắc phục sự cố!");
			alert.setGraphic(progressIndicator);
			alert.getButtonTypes().clear();
			ButtonType okButtonType = new ButtonType("OK");
			alert.getButtonTypes().add(okButtonType);
			alert.showAndWait();
		});
	}

	public void updatePlayer(String serverMessage) {
		// TODO Auto-generated method stub
		String res[] = serverMessage.split(",");
		this.player = new Player(Integer.valueOf(res[2]), res[3], res[4], Integer.valueOf(res[5]) , Integer.valueOf(res[6]), Integer.valueOf(res[7]),
				Integer.valueOf(res[8]));
		client.setPlayer(player);
		this.username.setText(player.getUsername());
		this.numberGame.setText(player.getNumberOfGame() + "");
		this.win.setText(player.getNumberOfWin() + "");
		this.draw.setText(player.getNumberOfDraw() + "");
		this.score.setText(player.getTotalScore() + "");
	}
}
