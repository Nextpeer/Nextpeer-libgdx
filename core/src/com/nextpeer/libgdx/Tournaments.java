//
//  Nextpeer libGDX plugin
//  http://www.nextpeer.com
//
//  Created by Nextpeer development team.
//  Copyright (c) 2014 Innobell, Ltd. All rights reserved.
//

package com.nextpeer.libgdx;

/**
 * Abstract Class. Used as a layer between the game code and Nextpeer engine. Will switch the scenes as instructed by TournamentsCallback.
 * Should be implemented on each Nextpeer supported platform (Android, iOS) project.
 */
public abstract class Tournaments {

	/**
	 * The TournamentsCallback accessed by the OS specific code when its required to notify on certain events
	 * @note Can be null in case we don't have callback
	 */
	protected TournamentsCallback callback;

	/**
	 * Default constructor for the Tournaments class
	 */
	public Tournaments() {
		this(null);
	}

	/**
	 * Constructor for the Tournaments class
	 * @param callback The callback interface to the GameCore logic during tournament events
	 */
	public Tournaments(TournamentsCallback callback) {
		setTournamentsCallback(callback);
	}

	/**
	 * Set the tournaments callback value
	 * @param callback The tournament callback
	 */
	public void setTournamentsCallback(TournamentsCallback callback) {
		this.callback = callback;
	}
    
	/**
	 * Call this method to report that the view is visible to the user.
	 */
	public void onStart() {}
    
	/**
	 * Does tournaments supported by the current OS (Android, iOS, Desktop)
	 * @return true if it is supported, false otherwise
	 */
	public boolean isSupported() {
		return false;
	}

	/**
	 * Launch Nextpeer UI
	 */
	public abstract void launch();

	/**
	 * Call this method to report the current score for the tournament
	 * @param score The current score for the player in the tournament
	 */
	public abstract void reportScoreForCurrentTournament(int score);

	/**
	 * Call this method when your game manage the current tournament and the player just died (a.k.a. 'Last Man Standing').
	 * Nextpeer will call TournamentsCallback's onTournamentEnd method after reporting the last score.
	 * @param score The last score for the player in the tournament
	 * @note: The method will act only if the current tournament is from 'GameControlled' tournament type
	 */
	public abstract void reportControlledTournamentOverWithScore(int score);

	/**
	 * Call this method when the user wishes to exit the current tournament (for example to go back to the main menu).
	 */
	public abstract void reportForfeitForCurrentTournament();

	/**
	 * Does the game is in a tournament at the moment or no.
	 * @return true if it the game is in a tournament mode, false otherwise
	 */
	public abstract boolean isCurrentlyInTournament();

	/**
	 * This method is used to push a buffer to the other players.
	 * Unlike the pushDataToOtherPlayers method, buffers sent through this function are not guaranteed to reach 
	 * the other players. However, the buffers sent via this method arrive on the other end much more quickly.
	 * This can potentially be used notify other players of changes in the game state. 
	 * The buffer will be sent to the other players and will activate the 
	 * {@code TournamentsCallback::onReceiveUnreliableTournamentCustomMessage:} method on their listener.
	 * @param data The byte array to send to the other connected players.
	 * @throws IllegalArgumentException if {@code data} is empty.
	 */
	public abstract void unreliablePushDataToOtherPlayers(byte[] data);

	/**
 	 * This method is used to push a buffer to the other players.
 	 * This can potentially be used to create custom notifications or some other interactive mechanism
 	 * that incorporates the other players. The buffer will be sent to the other players and will activate the 
 	 * {@code TournamentsCallback::onReceiveTournamentCustomMessage:} method on their listener.
 	 * @param data The byte array to send to the other connected players.
     * @throws IllegalArgumentException if {@code data} is empty.
	 */
    public abstract void pushDataToOtherPlayers(byte[] data);
    
    /**
     * Register to a synchronized event which will be triggered once all connected players register to such event.
     * Nextpeer will trigger a call to {@code TournamentsCallback#onReceiveSynchronizedEvent(String)}
     * when the rest of the connected players will register to this synchronized event.
     * For example, you can register for a start game synchronized event.
     * This way, your can be sure that all players will start the game together, without taking into consideration different loading time per device.
  	 * @param eventName The synchronized event name that the clients will register to. Cannot be empty or null. Try to use top down domain. For example, "com.yourcompany.yourgame.syncevent".
     * @param timeoutInMilliseconds The timeout in milliseconds for the synchronized event. Cannot be zero or negative.
	 * @throws IllegalArgumentException if {@code eventName} is empty or null.
	 * @throws IllegalArgumentException if {@code timeoutInMilliseconds} is zero or negative.
     */
    public abstract void registerToSynchronizedEvent(String eventName, int timeoutInMilliseconds);
}
