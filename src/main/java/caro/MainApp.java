package caro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import caro.player.Player;


public class MainApp extends Application {
    private static Stage stage;
    private Player player;
    public static boolean isStartGame;
    public final static String SERVER_IP = "127.0.0.1";
    public final static int SERVER_PORT = 7;
    
    public MainApp() {
		super();
	}

	public MainApp(Player player) {
		super();
		this.player = player;
	}

	@Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
        stage=s;
        setRoot("Login","");
    }

    public void setRoot(String fxml) throws IOException {
        setRoot(fxml,stage.getTitle());
    }

    public void setRoot(String fxml, String title) throws IOException {
        Scene scene = new Scene(loadFXML(fxml));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) throws InterruptedException, IOException {
    	
        launch(args);
    }

}
