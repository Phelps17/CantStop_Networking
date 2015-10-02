import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestClient extends Application{

	private String host = "localhost";
	
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;

	public void start(Stage primaryStage) {
		//text area to display contents
		TextArea ta = new TextArea();

		//create a stage
		Scene scene = new Scene(ta, 450, 200);
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		primaryStage.show();

		connectToServer();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public void connectToServer() {
		try {
			Socket socket = new Socket(host, 8000);
			
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		new Thread(() -> {
			//TODO: run use input side in here
		}).start();
	}
}
