import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

class HandleASession implements Runnable, GameServerUtil {
	private Socket player1;
	private Socket player2;

	private DataInputStream fromPlayer1;
	private ObjectOutputStream toPlayer1;
	private DataInputStream fromPlayer2;
	private ObjectOutputStream toPlayer2;

	public HandleASession(Socket player1, Socket player2) {
		this.player1 = player1;
		this.player2 = player2;

	}

	public void run() {
		try {
			DataInputStream fromPlayer1 = new DataInputStream(
					player1.getInputStream());
			ObjectOutputStream toPlayer1 = new ObjectOutputStream(
					player1.getOutputStream());
			DataInputStream fromPlayer2 = new DataInputStream(
					player2.getInputStream());
			ObjectOutputStream toPlayer2 = new ObjectOutputStream(
					player2.getOutputStream());

			String[] players = {"Player 1", "Player 2"};
			Color[] colors = {Color.RED, Color.BLUE};
			System.out.println("Starting a new game");
			CantStop game = new CantStop(players, colors);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}