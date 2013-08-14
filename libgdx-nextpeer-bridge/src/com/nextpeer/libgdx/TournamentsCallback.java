package com.nextpeer.libgdx;

public interface TournamentsCallback {

	/**
	 * This method will be called when a tournament is about to start
	 * @param tournamentRandomSeed Used when the game 'generate' the level with its random function -> so all players will have the same world
	 * @note Should switch to the game scene
	 */
    public void onTournamentStart(long tournamentRandomSeed);

    /**
     * This method is invoked whenever the current tournament has finished 
	 * @note Should switch to the main menu scene
     */
    public void onTournamentEnd();
}
