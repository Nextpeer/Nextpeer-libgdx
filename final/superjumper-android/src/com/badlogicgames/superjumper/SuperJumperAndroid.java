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

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class SuperJumperAndroid extends AndroidApplication {
	private AndroidTournaments mTournaments = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        // Initialize the tournament instance with the Context
		mTournaments = new AndroidTournaments(this);

        // Pass the Android tournaments instance to the game
		SuperJumper superJumper = new SuperJumper(mTournaments);
		initialize(superJumper, false);
	}

	// Nextpeer integration: Let Nextpeer know that the user session has started
	@Override
	protected void onStart() {
	    super.onStart();

	    // Notify the beginning of a user session.
    	if (mTournaments != null) {
    		mTournaments.onStart();
    	}
	}
	// Nextpeer integration

	// Nextpeer integration: Let Nextpeer know that the user session has ended while in tournament
	@Override 
	public void onStop() {
	    super.onStop(); 

	    // If there is an on-going tournament make sure to forfeit it 
    	if (mTournaments != null && mTournaments.isCurrentlyInTournament()) {
    		mTournaments.reportForfeitForCurrentTournament();
    	}
	}
	// Nextpeer integration
	
	// Nextpeer integration: In case that the on back pressed and we still in game, we wish to forfeit the current game
    /** The user pressed on the back button */
    @Override
    public void onBackPressed() {
	    // If the game is in tournament mode -> forfeit the tournament.
    	if (mTournaments != null && mTournaments.isCurrentlyInTournament()) {
    		mTournaments.reportForfeitForCurrentTournament();
    	}

		super.onBackPressed();
    }
	// Nextpeer integration
}
