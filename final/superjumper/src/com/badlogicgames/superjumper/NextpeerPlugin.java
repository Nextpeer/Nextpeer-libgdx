package com.badlogicgames.superjumper;

import com.nextpeer.libgdx.Tournaments;

public final class NextpeerPlugin {

	private static NextpeerPlugin _sInstance = null;

	public final Tournaments tournaments;
	public long lastKnownTournamentRandomSeed = 0;
	
	/***
	 * Private methods
	 */

	/**
	 * Private CTOR for the NextpeerPlugin class
	 * @param tournaments Holding the implementation of the NextpeerPlugin
	 */
	private NextpeerPlugin(Tournaments tournaments) {
		this.tournaments = tournaments;
	}

	
	/**
	 * Load the NextpeerPlugin instance with the Game object
	 * @param tournaments Holding the implementation of the NextpeerPlugin
	 * @return The initialized NextpeerPlugin object
	 */
	public synchronized static NextpeerPlugin load(Tournaments tournaments) {
		if (_sInstance == null) {
			_sInstance = new NextpeerPlugin(tournaments);
		}

		return _sInstance;
	}
	
	/**
	 * Gets the NextpeerPlugin instance if loaded, null if not
	 * @return The NextpeerPlugin instance if loaded, null if not
	 */
	public static NextpeerPlugin instance() {
		return _sInstance;
	}
	
	/**
	 * Convenience method. Return true if the Tournaments instance if available, false if not
	 * @return true Tournaments instance if available, false if not
	 */
	public static boolean isAvailable() {
		return (instance() != null);
	}
	/**
	 * Convenience method. Gets the Tournaments instance if available, null if not
	 * @return The Tournaments instance if available, null if not
	 */
	public static Tournaments tournaments() {
		if (!NextpeerPlugin.isAvailable()) {
			return null;
		}
		
		return instance().tournaments;
	}
}