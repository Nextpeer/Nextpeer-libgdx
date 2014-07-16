// TODO: Add your package here

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
	@Override
	public boolean isSupported() {
		return true;
	}
	
	/**
	 * Launch Nextpeer UI
	 */
	@Override
	public void launch() {
		Nextpeer.launch();
	}

    /**
     * Call this method to report the current score for the tournament
     * @param score The current score for the player in the tournament
     */
	@Override
    public void reportScoreForCurrentTournament(int score) {
    	Nextpeer.reportScoreForCurrentTournament(score);
    }

    /**
     * Call this method when your game manage the current tournament and the player just died (a.k.a. 'Last Man Standing').
     * Nextpeer will call NextpeerListener's onTournamentEnd method after reporting the last score.
     * @param score The last score for the player in the tournament
     * @note: The method will act only if the current tournament is from 'GameControlled' tournament type
     */
	@Override
    public void reportControlledTournamentOverWithScore(int score) {

    	Nextpeer.reportControlledTournamentOverWithScore(score);
    }
    
    /**
     * Call this method when the user wishes to exit the current tournament (for example to go back to the main menu).
     */
	@Override
    public void reportForfeitForCurrentTournament() {
    	Nextpeer.reportForfeitForCurrentTournament();
    }

    /**
     * Does the game is in a tournament at the moment or no.
	 * @return true if it the game is in a tournament mode, false otherwise
     */
	@Override
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
	@Override
	public void unreliablePushDataToOtherPlayers(byte[] data) {
		Nextpeer.unreliablePushDataToOtherPlayers(data);
		
	}

	/**
 	 * This method is used to push a buffer to the other players.
 	 * This can potentially be used to create custom notifications or some other interactive mechanism
 	 * that incorporates the other players. The buffer will be sent to the other players and will activate the 
 	 * {@code TournamentsCallback::onReceiveTournamentCustomMessage:} method on their listener.
 	 * @param data The byte array to send to the other connected players.
     * @throws IllegalArgumentException if {@code data} is empty.
	 */
	@Override
	public void pushDataToOtherPlayers(byte[] data) {
		Nextpeer.pushDataToOtherPlayers(data);
	}

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
	@Override
	public void registerToSynchronizedEvent(String eventName, int timeoutInMilliseconds) {
		Nextpeer.registerToSynchronizedEvent(eventName, timeoutInMilliseconds);
	}
}
