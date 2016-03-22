import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class GameServer extends Application implements GameServerUtil{
	private int sessionNo = 1;

	public static void main (String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		new Thread(() -> {
			try {
				ServerSocket serverSocket = new ServerSocket(8000);
				Platform.runLater(() -> System.out.println(new Date() + ": Server started at socket 8000"));

				while (true) {
					Platform.runLater(() -> {
						System.out.println(new Date() + ": Wait for players to join session " + sessionNo);
					});

					Socket player1 = serverSocket.accept();

					Platform.runLater(() -> {
						System.out.println(new Date() + ": Player 1 joined session " + sessionNo);
						System.out.println(new Date() + "Player 1's IP address " + player1.getInetAddress().getHostAddress());
					});

					//notify player that they are player 1
					new ObjectOutputStream(player1.getOutputStream()).writeInt(PLAYER1);

					Socket player2 = serverSocket.accept();

					Platform.runLater(() -> {
						System.out.println(new Date() + ": Player 2 joined session " + sessionNo);
						System.out.println(new Date() + "Player 2's IP address " + player2.getInetAddress().getHostAddress());
					});


					//notify player that they are player 2
					new ObjectOutputStream(player2.getOutputStream()).writeInt(PLAYER2);

					Platform.runLater(() -> {
						System.out.println(new Date() + ": Start a thread for session " + sessionNo++);
					});

					new Thread(new HandleASession(player1, player2)).start();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}).start();
	}

}
