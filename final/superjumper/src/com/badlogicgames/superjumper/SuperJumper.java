/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogicgames.superjumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.nextpeer.libgdx.Tournaments;
import com.nextpeer.libgdx.TournamentsCallback;

public class SuperJumper extends Game implements TournamentsCallback {
	boolean firstTimeCreate = true;
	FPSLogger fps;
	Tournaments tournaments = null;
	
	public SuperJumper() {
		this(null);
	}
	
	public SuperJumper(Tournaments tournaments) {
		
		// If we have a supported tournaments object, set the game as callback
		if (tournaments != null && tournaments.isSupported()) {
			this.tournaments = tournaments;
			this.tournaments.setTournamentsCallback(this);
		}
	}
	
	@Override
	public void create () {
		Settings.load();
		Assets.load();

		// Load the TournamentsCore if we have a valid implementation of it
		if (this.tournaments != null) {
			TournamentsCore.load(this.tournaments);
		}
		
		setScreen(new MainMenuScreen(this));
		fps = new FPSLogger();
	}
	
	@Override
	public void render() {
		super.render();
		fps.log();
	}

	/** {@link Game#dispose()} only calls {@link Screen#hide()} so you need to override {@link Game#dispose()} in order to call
	 * {@link Screen#dispose()} on each of your screens which still need to dispose of their resources. SuperJumper doesn't
	 * actually have such resources so this is only to complete the example. */
	@Override
	public void dispose () {
		super.dispose();

		getScreen().dispose();
	}
	
	/**
	 * TournamentsCallback implementation
	 * Responsible to answer on certain tournament events such as start tournament & end tournament.
	 */

	/**
	 * This method will be called when a tournament is about to start
	 * @param tournamentRandomSeed Used when the game 'generate' the level with its random function -> so all players will have the same world
	 * @note Should switch to the game scene
	 */
    public void onTournamentStart(long tournamentRandomSeed) {
        // Start the game scene
        TournamentsCore.instance().lastKnownTournamentRandomSeed = tournamentRandomSeed;
    	setScreen(new GameScreen(this));
    }

    /**
     * This method is invoked whenever the current tournament has finished 
	 * @note Should switch to the main menu scene
     */
    public void onTournamentEnd() {
        // End the game scene, switch to main menu
        TournamentsCore.instance().lastKnownTournamentRandomSeed = 0;
    	setScreen(new MainMenuScreen(this));
    }
}