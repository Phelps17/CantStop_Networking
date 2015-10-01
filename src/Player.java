///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  CantStop.java
// File:             Player.java
//
// Author:           phelps3@wisc.edu
//
///////////////////////////////////////////////////////////////////////////////

import java.awt.Color;

/**
 * A single player of the game has a name and an id.
 * 
 * @author albrooks
 */
public class Player {

	//data fields
	private String name; //stores the player name
	private char id; //stores the player id
	private Color color; //stores the player's color
	
    /**
     * Create a player.
     * 
     * @param name The player's name.
     * @param id The player's assigned id.
     * @param color The player's color.
     */
    public Player(String name, char id, Color color) {
    	//creates a new player with the proper attributes provided
    	this.name = name;
        this.id = id;
        this.color = color;
    }

    /**
     * Get the player's name.
     * 
     * @return The player's name.
     */
    public String getName() {
    	//returns the current players name
        return this.name;
    }

    /**
     * Get the player's id.
     * 
     * @return The player's id.
     */
    public char getId() {
    	//returns the current players id
        return this.id;
    }

    /**
     * Get the player's color
     * 
     * @return The player's color
     */
    public Color getColor() {
    	//returns the current player's color
        return this.color;
    }
}
