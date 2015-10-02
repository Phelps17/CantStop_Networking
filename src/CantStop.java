///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            Cant Stop
// Files:            CantStop.java, GameBoard,java, Track.java, Player.java
//                   Gui.java, StartUpDialog.java, Util.java
//
// Author:           Tyler Phelps
// Email:            phelps3@wisc.edu
//
///////////////////////////////////////////////////////////////////////////////

import java.awt.Color;
import java.io.Serializable;

public class CantStop implements Serializable {

	//data fields 
	private GameBoard cantStopBoard; //gameboard to play on
	
	/**
     * Creates a new GameBoard instance
     * 
     * @param names The names of the players
     * @param colors The colors of the players (respective to names)
     */
    public CantStop(String[] names, Color[] colors) {
    	//create new gameboard named cantStopGameboard with names and colors as params
        cantStopBoard = new GameBoard(names, colors);
    }

    /**
     * Get the GameBoard
     * @return the GameBoard instance created in the constructor
     */
    public GameBoard getGameBoard() {
    	//returns the current gameboard
        return this.cantStopBoard;
    }

    /**
     * Creates a player selection window.
     * Once players have been chosen, the game GUI will
     * start, creating a CantStop object in the process.
     * 
     * @param args unused
     */
    public static void main(String[] args) {
    	//starts the game with opening dialog
    	Util.showNewGameDialog("Choose player names and colors.");
    }
}
