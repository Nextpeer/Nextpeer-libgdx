package com.badlogicgames.superjumper;

import com.nextpeer.android.Nextpeer;
import com.nextpeer.android.NextpeerListener;
import com.nextpeer.android.NextpeerTournamentEndData;
import com.nextpeer.android.NextpeerTournamentStartData;
import com.nextpeer.libgdx.Tournaments;

import android.content.Context;

/**
 * Android implementation of the Tournaments class with Nextpeer Android SDK
 *
 */
public final class AndroidTournaments extends Tournaments {
	public AndroidTournaments(Context context) {
		
		final String gameKey = ?; // TODO: Get your Game Key from https://developers.nextpeer.com
		
		Nextpeer.initialize(context, gameKey, _listener);
	}

	/***
	 * NextpeeerListeners methods
	 */	
	private NextpeerListener _listener = new NextpeerListener() {
		
		/**
		 * This method will be called when a tournament is about to start
		 * @param startData  The tournament start container will give you some details on the tournament which is about to be played.
		 * @note Switch to the game scene
		 */
	    public void onTournamentStart(NextpeerTournamentStartData startData) {
	    	
	    	if (callback != null) {
	    		callback.onTournamentStart(startData.tournamentRandomSeed);
	    	}
	    }

	    /**
	     *  This method is invoked whenever the current tournament has finished 
		 * @param endData  The tournament end container will give you some details on the tournament which just got played.
		 * @note Switch to the main menu scene
	     */
	    public void onTournamentEnd(NextpeerTournamentEndData endData) {
	    	if (callback != null) {
	    		callback.onTournamentEnd();
	    	}
	    }
	};

	/***
	 * Tournaments methods
	 */
	
	/**
	 * Does tournaments supported by the current OS (Android)
	 * @return true if it is supported, false otherwise
	 */
	public boolean isSupported() {
		return true;
	}
	
	/**
	 * Launch Nextpeer UI
	 */
	public void launch() {
		Nextpeer.launch();
	}

    /**
     * Call this method to report the current score for the tournament
     * @param score The current score for the player in the tournament
     */
    public void reportScoreForCurrentTournament(int score) {
    	Nextpeer.reportScoreForCurrentTournament(score);
    }

    /**
     * Call this method when your game manage the current tournament and the player just died (a.k.a. 'Last Man Standing').
     * Nextpeer will call NextpeerListener's onTournamentEnd method after reporting the last score.
     * @param score The last score for the player in the tournament
     * @note: The method will act only if the current tournament is from 'GameControlled' tournament type
     */
    public void reportControlledTournamentOverWithScore(int score) {

    	Nextpeer.reportControlledTournamentOverWithScore(score);
    }
    
    /**
     * Call this method when the user wishes to exit the current tournament (for example to go back to the main menu).
     */
    public void reportForfeitForCurrentTournament() {
    	Nextpeer.reportForfeitForCurrentTournament();
    }

    /**
     * Does the game is in a tournament at the moment or no.
	 * @return true if it the game is in a tournament mode, false otherwise
     */
    public boolean isCurrentlyInTournament() {
    	return Nextpeer.isCurrentlyInTournament();
    }

    /**
     * Call this method to report that the view is visible to the user.
     * @note This method is specific for Android. Call this method from {@code Activity#onStart()}.
     */
    public void onStart() {
    	Nextpeer.onStart();
    }
}
