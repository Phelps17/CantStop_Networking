import java.awt.Color;
import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class TestClient extends Application{

	private String host = "localhost";
	
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;

	public void start(Stage primaryStage) {
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
			
			int playerNo = fromServer.readInt();
			System.out.println("You are player " + playerNo);
		}
		catch (Exception ex) {
			System.out.println("Error. Couldn't connect you to the game server.");
			System.exit(0);
		}
		
		new Thread(() -> {
			//TODO: this is where the game should run
			String[] names = {"Player 1", "Player 2"};
			Color[] colors = {Color.RED, Color.BLUE};
			
			//client currently runs it's own game rather than through the server
			Gui gui = new Gui(new CantStop(names, colors));
			gui.update();
			gui.start();
			
		}).start();
	}
}
