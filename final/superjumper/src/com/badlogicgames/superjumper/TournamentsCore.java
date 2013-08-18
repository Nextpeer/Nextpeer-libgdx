package com.badlogicgames.superjumper;

import com.nextpeer.libgdx.Tournaments;

public final class TournamentsCore {

	private static TournamentsCore _sInstance = null;

	public final Tournaments tournaments;
	public long lastKnownTournamentRandomSeed = 0;
	
	/***
	 * Private methods
	 */

	/**
	 * Private CTOR for the TournamentsCore class
	 * @param tournaments Holding the implementation of the TournamentsCore
	 */
	private TournamentsCore(Tournaments tournaments) {
		this.tournaments = tournaments;
	}

	
	/**
	 * Load the TournamentsCore instance with the Game object
	 * @param tournaments Holding the implementation of the TournamentsCore
	 * @return The initialized TournamentsCore object
	 */
	public synchronized static TournamentsCore load(Tournaments tournaments) {
		if (_sInstance == null) {
			_sInstance = new TournamentsCore(tournaments);
		}

		return _sInstance;
	}
	
	/**
	 * Gets the TournamentsCore instance if loaded, null if not
	 * @return The TournamentsCore instance if loaded, null if not
	 */
	public static TournamentsCore instance() {
		return _sInstance;
	}

	/**
	 * Convenience method. Gets the Tournaments instance if available, null if not
	 * @return The Tournaments instance if available, null if not
	 */
	public static Tournaments tournaments() {
		TournamentsCore core = instance();
		if (core == null) {
			return null;
		}
		
		return core.tournaments;
	}
}
