///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  CantStop.java
// File:             Track.java
//
// Author:           phelps3@wisc.edu
//
///////////////////////////////////////////////////////////////////////////////

import java.util.Arrays;

/**
 * A track in Can't Stop is associated with a number from 2-12.
 * The likelihood of rolling this number determines the length of the track.
 * Each track consists of a set of spaces, each of which may either be
 * empty or may contain a single peg belonging to one of the players.
 * 
 * <p>A track may be "owned" by a single player, in which case no one may play
 * in this track anymore. A player owns a track when the player advances past
 * the entire track.
 * 
 * @author albrooks
 */
public class Track {

	//data fields
	private int length; //length of the track array
	private char[] spaces; //array of spaces on the track
	private char trackOwner; //track owner

	/**
	 * Create a track with the appropriate length, given the probability of
	 * rolling the given number.
	 * <p> One formula that works is to use either (12-roll+1)*2 or
	 * (roll-1)*2 depending on whether the roll is greater than 7.
	 * 
	 * @param roll The number associated with this track.
	 */
	public Track(int roll) {
		//check if roll is greater than 7
		if (roll > 7) {
			//get the roll track length
			this.length = (12-roll+1)*2;
		}
		//check if the roll is less than or equal to 7
		else {
			//gets the roll track length
			this.length = (roll-1)*2;
		}

		//creates an char array with space representing each spot on the board
		spaces = new char[length];

		//fills the track spaces with the value found in Util.EMPTY_SLOT
		for (int i = 0; i < length; i++) {
			spaces[i] = Util.EMPTY_SLOT;
		}

		//set the track owner to the value found in Util.EMPTY_SLOT
		trackOwner = Util.EMPTY_SLOT;
	}

	/**
	 * Report whether this track is already owned by a player or the neutral id.
	 * 
	 * @return Whether the track is owned.
	 */
	public boolean isOwned() {
		//checks and returns true if the track is owned
		if (this.trackOwner != Util.EMPTY_SLOT) {
			return true;
		}

		//returns false otherwise
		return false;
	}

	/**
	 * Return the name of the owner (if any). EMPTY_SLOT is returned if no one
	 * owns the track.
	 * 
	 * @return The name of the owner.
	 */
	public char getOwner() {
		return this.trackOwner;
	}

	/**
	 * Advance the neutral position. If no neutral marker is found, place one
	 * in the next available space above the current player's position. If the
	 * current player has not yet played in this track, her position is
	 * considered to be "-1".
	 * 
	 * @param player The current player id.
	 */
	public void advance(char player) {
		//starts player position at -1 because 0 is the first spot on the board
		int playerPosition = -1;
		
		//runs through the track to see if we have a piece or neutral cone
		//if we do, thats our starting point to advance
		for (int i = this.length-1; i >= 0; i--){
			if (this.getIdAt(i) == Util.NEUTRAL_ID || 
					this.getIdAt(i) == player) {
				//sets the player position to the position of the marker
				playerPosition = i;
				break;
			}
		}

		//positionWanted is the player position plus one space advanced
		int positionWanted = playerPosition + 1;
		
		//if the space we want is taken, we go to the next space
		if (positionWanted < this.spaces.length){
			while (this.getIdAt(positionWanted) != Util.EMPTY_SLOT) {
				positionWanted++;
				if (positionWanted >= this.spaces.length - 1){
					break;
				}
			}
		}

		//check to see if the spot our neutral marker left is on the board
		if (playerPosition >= 0) {
			//if so, we remove our neutral marker from it
			if (this.getIdAt(playerPosition) == Util.NEUTRAL_ID) {
				this.spaces[playerPosition] = Util.EMPTY_SLOT;
			}
		}
		
		//check to see if the position we are moving to is the end of the track
		if (positionWanted >= this.spaces.length) {
			//if so, we now own the track
			this.trackOwner = Util.NEUTRAL_ID;
		}
		else {
			//if its not the end we simply advance our neutral cone
			this.spaces[positionWanted] = Util.NEUTRAL_ID;
		}

	}

	/**
	 * Determine whether there is a neutral cone in this track already.
	 * 
	 * <p>If the track is owned, it is not active.
	 * 
	 * @return True if there is a neutral cone in the track, false otherwise.
	 */
	public boolean active() {
		//loops through the track
		for (int i = this.length -1; i >= 0; i--){
			//checks to see if any neutral cones have been found
			if (this.getIdAt(i) == Util.NEUTRAL_ID) {
				//if so, the track is considered active
				return true;
			}
			else if (this.trackOwner == Util.NEUTRAL_ID) {
				//else return that this track is active
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Replace the neutral cone (if present) with one of the appropriate id.
	 * If the current player now owns the track, also remove all other players'
	 * cones from the track.
	 * 
	 * @param id The id of the player being committed.
	 */
	public void commit(char id) {
		//check that track owners have been established
		if (this.trackOwner == Util.NEUTRAL_ID) {
			//sets track owner
			this.trackOwner = id;
		}
		
		//boolean to represent if the spot was used prior
		boolean spotSwapped = false;
		//loop through the spaces on the track
		for (int i = this.length - 1; i >= 0; i--){
			//if the space has a neutral cone, replace it with a marker
			if (this.getIdAt(i) == id && spotSwapped == true) {
				this.spaces[i] = Util.EMPTY_SLOT;
				spotSwapped = false;
			}
			//check if the space has a white cone
			if (this.getIdAt(i) == Util.NEUTRAL_ID) {
				//swaps the white cone out
				this.spaces[i] = id;
				spotSwapped = true;
			}
			
			//if the track is owned, clear the track
			if (this.isOwned()) {
				this.spaces[i] = Util.EMPTY_SLOT;
			}
		}

	}

	/**
	 * Remove the neutral cone (if present). Do not replace it.
	 * 
	 * <p>Used when going bust.
	 */
	public void clear() {
		//loops through spaces on the track
		for (int i = this.length - 1; i >= 0; i--){
			//if there is a neutral cone, we remove it
			if (this.getIdAt(i) == Util.NEUTRAL_ID) {
				this.spaces[i] = Util.EMPTY_SLOT;
			}
			if (this.trackOwner == Util.NEUTRAL_ID) {
				this.trackOwner = Util.EMPTY_SLOT;
			}
		}
	}

	/**
	 * Report the length of the track.
	 * 
	 * @return The length of the track.
	 */
	public int getLength() {
		//returns the length of the current track
		return this.length;
	}

	/**
	 * Report the player at a given position. 
	 * 
	 * @param i The position queried.
	 * @return The character at the given position in the track.
	 */
	public char getIdAt(int i) {
		//returning the value at a space on the current track
		return this.spaces[i];
	}


	/**
	 * Output a string representation of the current track
	 * 
	 * @return the string holding the track data
	 */
	@Override public String toString() {
		//this makes the string holding all the properties of the current track
		String trackContents = Arrays.toString(spaces);
		String returnString = ("Owner: " + this.trackOwner + " | Track Contents "
				+ "(bottom to top): " + trackContents + " | length: "
				+ this.length);

		return returnString;
	}
}
