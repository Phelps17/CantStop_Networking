import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class TestClient extends Application{

	private String host = "localhost";
	
	ObjectOutputStream toServer = null;
	ObjectInputStream fromServer = null;

	public void start(Stage primaryStage) {
		connectToServer();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public void connectToServer() {
		try {
			Socket socket = new Socket(host, 8000);
			
			fromServer = new ObjectInputStream(socket.getInputStream());
			toServer = new ObjectOutputStream(socket.getOutputStream());
			
		}
		catch (Exception ex) {
			System.out.println("Error. Couldn't connect you to the game server.");
			ex.printStackTrace();
			System.exit(0);
		}
		
		try {

			int playerNo = fromServer.readInt();
			System.out.println("You are player " + playerNo);
			
			int playerTurn = fromServer.readInt();
			
			System.out.println("It is player " + playerTurn + "'s turn.");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println("Here");
		
		new Thread(() -> {
			
			/*TODO: this is where the game should run
			try {
				GameBoard gameboard = (GameBoard) fromServer.readObject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			
			
		}).start();
	}
}