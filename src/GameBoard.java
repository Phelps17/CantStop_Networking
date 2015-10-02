///////////////////////////////////////////////////////////////////////////////
//
// Main Class File:  CantStop.java
// File:             GameBoard.java
//
// Author:           phelps3@wisc.edu
///////////////////////////////////////////////////////////////////////////////

import java.awt.Color;

/**
 * The GameBoard handles the flow of the game, as well as maintaining the set
 * of tracks and players which make up the state of the game.
 * 
 * @author albrooks
 */
public class GameBoard {
	//data fields
	private Player[] gamePlayers; //array of game players
	private Track[] gameboardTracks = new Track[11]; //array of tracks

	//class variables
	private int playerTurn; //decides whos turn it is

	/**
	 * Initialize all GameBoard information
	 * 
	 * @param names The names of the players
	 * @param colors The colors of the players (respective to names)
	 */
	public GameBoard(String[] names, Color[] colors) {
		//loop through track possibilities
		for (int i = 0; i < gameboardTracks.length; i++) {
			//create a track at every possibility for game
			//plus 2 is because the tracks start at 2 and not 0
			gameboardTracks[i] = new Track(i+2);
		}

		//set up players in an array
		gamePlayers = new Player[names.length];
		
		//loop through the names provided
		for (int i = 0; i < names.length; i++) {
			//assign player attributes to their spot in the player array
			gamePlayers[i] = new Player(names[i], Util.PLAYER_IDS[i], colors[i]);
		}

		//set up current turn
		playerTurn = 0;

		//check to make sure the gameboard is set up properly
		//comented out to not overload the server output
		//System.out.println(this.toString());

	}

	/**
	 * Get the player whose turn it is.
	 * 
	 * @return The current Player object.
	 */
	public Player getCurrentPlayer() {
		//return the array value that corresponds to who's turn it is
		return gamePlayers[playerTurn];
	}

	/**
	 * Get the players
	 * 
	 * @return The array of Player objects
	 */
	public Player[] getPlayers() {
		//returns the array of players
		return gamePlayers;
	}

	/**
	 * Get the tracks
	 * 
	 * @return The array of Track objects
	 */
	public Track[] getTracks() {
		//returns the array of gameboard tracks
		return gameboardTracks;
	}

	/**
	 * Check whether the given player won.
	 * 
	 * @param toCheck The player to check.
	 * @return Whether the given player won.
	 */
	public boolean isWinner(Player toCheck) {
		//set initial tracks owned by player to 0
		int tracksOwned = 0;
		
		//loops through all tracks
		for (int i = 0; i < gameboardTracks.length; i++) {
			//checks to see if the player owns that track
			if (gameboardTracks[i].getOwner() == toCheck.getId()) {
				//if they own it, it adds one to their owning count
				tracksOwned++;
			}
		}

		//if they own 3 or more, then they win
		if (tracksOwned >= 3) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if a dice pair sum is a valid track choice.
	 *  
	 * @param sum the sum of the chosen dice pair
	 * @return null if choice is valid. Otherwise return (with this priority):
	 * <br />
	 * "Sorry, that track is already owned." if the chosen track is owned
	 * (including by the current player)<br />
	 * "Sorry, you are out of neutral cones." if placing in that track would
	 * require a cone, and they are out.
	 * 
	 */
	public String isValidChoice(int sum) {
		//set the amount of tracks the player owns to 0
		int activeTracks = 0;

		//check the track corresponding to their choice for an owner
		if (gameboardTracks[sum-2].isOwned()) {
			return "Sorry, that track is already owned.";
		}

		//checks to see how many neutral cones they have out
		for (int i = 0; i < gameboardTracks.length; i++) {
			if (gameboardTracks[i].active() && i != sum-2) {
				activeTracks++;
			}
		}
		
		//returns if they are out of neutral cones to place
		if (activeTracks == 3) {
			return "Sorry, you are out of neutral cones.";
		}

		return null;
	}

	/**
	 * Initializes information required to start a new player's turn.
	 */
	public void startNewTurn() {
		//Didn't need this method in my program
		//Piazza said it was okay
	}

	/**
	 * Checks if a roll results in the player busting.
	 * @param rolls The four dice roll values (1-6)
	 * @return true if the player busted, false otherwise.
	 */
	public boolean didPlayerBust(int[] rolls) {
		//creates an array for all possible dice combinations
		int[] sums = new int[6]; //array of all sum possibilities
		//loops through dice pair combos and adds to sums array
		for (int i = 0; i < sums.length; i++) { 
			if (i >= 0 && i <= 2) {
				sums[i] = rolls[0] + rolls[i+1];
			}
			else if (i <= 4) {
				sums[i] = rolls[1] + rolls[i-1];
			}
			else {
				sums[i] = rolls[2] + rolls[i-2];
			}
		}

		//check to see if any of those combinations would be valid
		for (int i = 0; i < sums.length; i++) {
			if (isValidChoice(sums[i]) == null) {
				return false;
			}
		}
		
		//otherwise you bust
		return true;
	}

	/**
	 * Updates the board according to the dice rolls and the sum of a valid pair chosen
	 * by the user. If the second pair produces a valid move, updates the board for that
	 * pair sum as well.
	 * @param rolls The four dice roll values (1-6)
	 * @param firstPairSum The sum of the dice the user chose to pair.
	 */
	public void handleUserChoice(int[] rolls, int firstPairSum) {
		//sets the sum of their roll to 0 and counts second pair value
		int totalSumOfRoll = 0; //total sum of the dice selected
		int secondPairSum; //sum of the second pair of dice
		
		//finds value of total dice rolls
		for (int i = 0; i < rolls.length; i++) {
			totalSumOfRoll = totalSumOfRoll + rolls[i];
		}
		
		//sets second pair to total - first pair
		secondPairSum = totalSumOfRoll - firstPairSum;

		//attempts to advance the first pair
		gameboardTracks[firstPairSum-2].advance(this.getCurrentPlayer().getId());

		//check if second pair is valid and if so, advances it as well
		if (isValidChoice(secondPairSum) == null) {
			gameboardTracks[secondPairSum-2].advance(this.getCurrentPlayer().getId());
		}
	}

	/**
	 * Ends the current users turn. If the player busted, progress is lost.
	 * If the player did not bust, neutral cones are replaced with the player's
	 * tokens. Checks to see if the current player won and advances to next player's turn.
	 * @param busted Whether the player busted or not.
	 * @return Whether or not the current player won.
	 */
	public boolean endTurn(boolean busted) {
		//runs through the track and either clears or commits the moves
		for (int i = 0; i < gameboardTracks.length; i++) {
			if (busted) {
				gameboardTracks[i].clear();
			}
			else {
				gameboardTracks[i].commit(this.getCurrentPlayer().getId());
			}
		}
		
		//checks to see if the player won and returns true if they did
		if (isWinner(this.getCurrentPlayer())) {
			return true;
		}

		//advances to the next players turn
		advancePlayer();
		return false;
	}

	/**
	 * Advances the current player to the next player.
	 */
	public void advancePlayer() {
		//goes to the next player in the array unless it is at the last player
		//in that case, we jump back to the start
		if (playerTurn == (gamePlayers.length - 1)) {
			playerTurn = 0;
		}
		else playerTurn++;

	}

	/**
	 * Return an ASCII version of the game board for display.
	 * 
	 * @return An ASCII game board, including track owners.
	 */
	@Override
	public String toString() {
		//starts a string to hold the game board
		StringBuilder result = new StringBuilder();
		//creates a 2d array to hold our graphic representation of the board
		char[][] printableTracks = new char[11][12];

		//adds owners of the tracks to our string
		result.append("OWNERS: \n");
		for (int i = 0; i < gameboardTracks.length; i++) {
			result.append(gameboardTracks[i].getOwner());
		}
		result.append("\n");
		result.append("BOARD: \n");

		//retrieves values from tracks and formats them into an array
		for (int i = 0; i < 11; i++) {
			int position = 0;
			while (position < ((12 - gameboardTracks[i].getLength())/2)) {
				printableTracks[i][position] = '-';
				position++;
			}
			for (int x = 0; x < gameboardTracks[i].getLength(); x++) {
				printableTracks[i][position] = gameboardTracks[i].getIdAt(x);
				position++;
			}
			while (position < 12) {
				printableTracks[i][position] = '-';
				position++;
			}

		}

		//appends the properly formatted array into our returning string
		for (int i = 11; i >= 0; i--) {
			for (int x = 0; x < gameboardTracks.length; x++) {
				result.append(printableTracks[x][i]);
			}
			result.append("\n");
		}

		//returns the console version of the board
		return result.toString();
	}
}

