//package com.nextpeer.libgdx;
//
///**
// * Used as a layer between the game code and Nextpeer engine. Will switch the scenes as instructed by NextpeerListener.
// *
// */
//public abstract class Tournaments {
//
//	private static Tournaments _sInstance = null;
//
//	/***
//	 * Private methods
//	 */
//
//	/**
//	 * Private CTOR for the tournaments class
//	 * @param game Used by the tournaments class to switch between the screens (Game, MainMenu)
//	 */
//	private Tournaments(Game game) {
//		_tournamentRandomSeed = 0;
//		this._game = game;
//	}
//
//	/***
//	 * Public methods
//	 */
//
//	/**
//	 * Load the tournament instance with the Game object
//	 * @param game Used by the tournaments class to switch between the screens (Game, MainMenu)
//	 * @return The initialized Tournaments object
//	 */
//	public synchronized static Tournaments load(Game game) {
//		if (_sInstance == null) {
//			_sInstance = new Tournaments(game);
//		}
//		
//		return _sInstance;
//	}
//	
//	/**
//	 * Gets the tournaments instance
//	 * @return The tournaments instance
//	 */
//	public static Tournaments instance() {
//		return _sInstance;
//	}
//	
//	/**
//	 * Launch Nextpeer UI
//	 */
//	public void launch() {
//		Nextpeer.launch();
//	}
//	
//    /**
//     * Call this method to report the current score for the tournament
//     * @param score The current score for the player in the tournament
//     */
//    public void reportScoreForCurrentTournament(int score) {
//    	Nextpeer.reportScoreForCurrentTournament(score);
//    }
//
//    /**
//     * Call this method when your game manage the current tournament and the player just died (a.k.a. 'Last Man Standing').
//     * Nextpeer will call NextpeerListener's onTournamentEnd method after reporting the last score.
//     * @param score The last score for the player in the tournament
//     * @note: The method will act only if the current tournament is from 'GameControlled' tournament type
//     */
//    public void reportControlledTournamentOverWithScore(int score) {
//
//    	Nextpeer.reportControlledTournamentOverWithScore(score);
//    }
//    
//    /**
//     * Call this method when the user wishes to exit the current tournament (for example to go back to the main menu).
//     */
//    public void reportForfeitForCurrentTournament() {
//    	Nextpeer.reportForfeitForCurrentTournament();
//    }
//
//    /**
//	 * Used when the game 'generate' the level with its random function -> so all players will have the same world
//     * @return The last known value of the tournament random seed to be used. 0 means no value.
//     */
//    public long tournamentRandomSeed() {
//    	return _tournamentRandomSeed;
//    }
//
//	
//	/***
//	 * NextpeeerListeners methods
//	 */
//	
//	/**
//	 * This method will be called when a tournament is about to start
//	 * @param startData  The tournament start container will give you some details on the tournament which is about to be played.
//	 * @note Switch to the game scene
//	 */
//    public void onTournamentStart(NextpeerTournamentStartData startData) {
//    	_tournamentRandomSeed = startData.tournamentRandomSeed;
//        // Start the game scene
//    	_game.setScreen(new GameScreen(_game));
//    }
//
//    /**
//     *  This method is invoked whenever the current tournament has finished 
//	 * @param endData  The tournament end container will give you some details on the tournament which just got played.
//	 * @note Switch to the main menu scene
//     */
//    public void onTournamentEnd(NextpeerTournamentEndData endData) {
//        // End the game scene, switch to main menu
//    	_game.setScreen(new MainMenuScreen(_game));
//    }
//}
