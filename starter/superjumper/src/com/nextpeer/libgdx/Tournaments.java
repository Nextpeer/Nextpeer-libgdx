package com.nextpeer.libgdx;

/**
 * Used as a layer between the game code and Nextpeer engine. Will switch the scenes as instructed by NextpeerListener.
 *
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
     * Nextpeer will call NextpeerListener's onTournamentEnd method after reporting the last score.
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
     * Call this method to report that the view is visible to the user.
     */
    public void onStart() {}
}
