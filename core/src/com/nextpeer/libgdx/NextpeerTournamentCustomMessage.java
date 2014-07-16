//
//  Nextpeer libGDX plugin
//  http://www.nextpeer.com
//
//  Created by Nextpeer development team.
//  Copyright (c) 2014 Innobell, Ltd. All rights reserved.
//

package com.nextpeer.libgdx;

/**
 * Can be used to create custom notifications and events while engaging the other players
 * that are currently playing. The container that is passed contains the sending user's name and image as well as the message being sent.
 */
public final class NextpeerTournamentCustomMessage {
    
	/**
	 * A unique player identifier for the current game.
	 */
	public final String playerId;
	
	/**
	 * The player name.
	 */
	public final String playerName;
    
	/**
	 * The player's profile image URL.
	 */
	public final String playerImageUrl;
    
	/**
	 * The custom message (passed as a buffer).
	 */
	public final byte[] customMessage;
    
	/**
	 * Boolean value that indicates if this message came form a bot recording or a real-life player.
	 */
	public final boolean playerIsBot;
	
	public NextpeerTournamentCustomMessage(String playerId, String playerName, String playerImageUrl, byte[] customMessage, boolean playerIsBot) {
		this.playerId = playerId;
		this.playerName = playerName;
		this.playerImageUrl = playerImageUrl;
		this.customMessage = customMessage;
		this.playerIsBot = playerIsBot;
	}
}