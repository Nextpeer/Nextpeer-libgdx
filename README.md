# libGDX - Nextpeer Integration guide

## Introduction 
The [Nextpeer][np] platform lets you easily add multiplayer tournaments to your single player game, so users can play with each other become more engaged with the game. The platform supports both time-based and game-controlled (in which the game decides when to end the tournament) tournaments. Nextpeer has many advanced features which allow you to turn your game into a multiplayer phenomenon. This guide will cover the basic integration between a [libGDX][libgdx] game and Nextpeer. 

This integration guide will cover an Android integration, however you can also integrate Nextpeer to your [iOS port][libGDX_iOS_plugin].
Don't forget to check our sample code, available [here][sample_code].

## Prerequisites
* [Install Eclipse][eclipse]
* [Install Android SDK][AndroidSDK]
* [Install Android ADT][AndroidADT]
* [Download the Nextpeer Android SDK][LatestSDK]
* [Download the Nextpeer-libGDX plugin][libGDX_plugin_download]

## Add your game in the Nextpeer dashboard
Register your game in Nextpeer's [developer dashboard][dashboard]. If you have not previously registered for a Nextpeer account, now would be a good time to do so.
Go to the *Games* tab and create a new Android game. Please fill out the details for your game, especially the client package id, so that the SDK can communicate properly with your game. Take note of your game's *Game key*, as you will need it later during the integration process.

## Downlading the Nextpeer SDK
The latest Nextpeer Android SDK can always be downloaded from the [downloads page][LatestSDK]. Also make sure to download the [libGDX plugin][libGDX_plugin_download] code.

## Add Nextpeer SDK to your project
The Nextpeer Android SDK is shipped as a simple library project, so you can add this to any Android project as a library. In your project properties, select "Android" and add NextpeerConnect under the section “Library”. The Nextpeer Android SDK requires the `android-support-v4.jar` library. Go to the ‘Java Build Path’ of your project properties and click “External Jars”, and add the required jar file available at `nextpeer-android-sdk-/third-party/`.

### Setup the Android manifest file

#### Add required permissions
Open the AndroidManifest.xml file in the your libGDX Android port project and switch to the Permissions tab. Click *Add…,* then *Uses Permission*, then *OK*. (You'll see *Uses Permission* in the *Permissions* list, which you should click to highlight.) Pick `android.permission.INTERNET` from the drop-down menu to the right. If you’re using raw XML, copy and paste the following line into the manifest file:

	<!-- Required. Used to access the Internet to make network requests.  -->
    <uses-permission android:name="android.permission.INTERNET"/>

#### Add the NextpeerActivity
Open the AndroidManifest.xml file and switch to the Applications tab. Under Application Nodes, click Add…, then Activity, then OK. Once created, select the Activity in the list. It should be named Activity. On the bottom right panel in the ‘Name’ field enter **com.nextpeer.android.NextpeerActivity**. Save the AndroidManifest.xml file.:

	<activity android:name="com.nextpeer.android.NextpeerActivity" />

## Integrating Nextpeer in your Game
The [Nextpeer libGDX plugin][libGDX_plugin_download] wraps the Nextpeer Android SDK so it could be easily integrated into the libGDX cross platform projects stack.
The libGDX plugin contains two folders, **core** and **ports**. 

A typical libGDX game consist of several projects. A "Game-Core" project and several cross platform projects (Android/Desktop and iOS), also known as ports. 

### Expanding the Game-Core project
Inside the `core` folder of the SDK you will find a `src` folder. This folder contains the java-based package Nextpeer plugin package, `com.nextpeer.libgdx`. Under the `com.nextpeer.libgdx` package you can find the interface between the core Java game project and the Android project (as instructed by libgdx’s wiki under [ApplicationPlatformSpecific][wiki]. Copy the `com.nextpeer.libgdx` package to the "Game-Core" project source folder.

### The NextpeerPlugin class
Since different parts of the game will require access to the Tournaments object, we should wrap it with a singleton (in the same manner as Settings and Assets). See the `NextpeerPlugin.java` class in `com.nextpeer.libgdx` package. It holds the instance of the Tournaments class so it can be safely accessed from the different game screens at any time.

### Expanding the Game-Core to support the Tournaments class
We’ll start by adding the tournaments to the game logic. Navigate to the `Game` subclass. We will modify it to have a Tournaments member and to accept it via the constructor. For example, in the famous libGDX's sample, `SuperJumper`, you should edit the `superjumper/com.badlogicgames.superjumper/SuperJumper.java` class. The code should look like this:
	
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

Next, we'll have the `Game` subclass conform to the `TournamentsCallback.java` interface. This interface defines callbacks which will be called when a tournament is about to start or end. For now we’ll just leave these methods empty:

	@Override
	public void onTournamentStart(long tournamentRandomSeed) 
	{
		// Stat the tournament
	}
	
	@Override
	public void onTournamentEnd() 
	{
		// Tournament ended - clean up
	}		
	
### Android-specific integration
Inside the `ports` folder of the SDK you will find a `Android` folder. This folder contains the Android-based Nextpeer helper class, `AndroidTournaments.java`. Navigate to the Android specific project and the copy the `AndroidTournaments.java` file to this project source folder. 

Open the main launcher Activity class. We will need to pass the `AndroidTournaments` instance to the `Game-Core` game constructor. We will also need to handle the use case of navigation out of the game (pressing on the back button or navigate to the home screen) while in tournament mode (so the other active players will know that the player has forfeit the game). We will also let Nextpeer know that the user session has started. 

Paste they game key you received from the Nextpeer developer dashboard in the constructor of `AndroidTournaments.java`.
Build the project. At this point the Android specific project should compile properly.

Expand the main launcher Activity class with the following code:

	private AndroidTournaments mTournaments = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mTournaments = new AndroidTournaments(this);
		
		// Passing the AndroidTournament instance to the game-core
		SuperJumper superJumper = new SuperJumper(mTournaments);
		initialize(superJumper, false);
	}
    	
	// Notify the beginning of a user session.
	@Override
	protected void onStart() {
		super.onStart();
		
		if (mTournaments != null) {
			mTournaments.onStart();
		}
	}

	// Let Nextpeer know that the user session has ended while in tournament
	@Override 
	public void onStop() {
		super.onStop();
		
		// If there is an on-going tournament make sure to forfeit it
		if (mTournaments != null && mTournaments.isCurrentlyInTournament()) {
			mTournaments.reportForfeitForCurrentTournament();
		}
	}
	
	/** The user pressed the back button */
	@Override
	public void onBackPressed() {
		// If the game is in tournament mode, forfeit the tournament:
		if (mTournaments != null && mTournaments.isCurrentlyInTournament()) {
			mTournaments.reportForfeitForCurrentTournament();
		}
		
		super.onBackPressed();
	}
	
## Launching Nextpeer's dashboard
Once you've added Nextpeer to your project and initialized it, you may launch the dashboard from within your game. The dashboard will usually be entered from the main menu using a button. To launch the dashboard, simply call `NextpeerPlugin.launch()`.
Now that we’ve set the callback handlers, we can set up an entry point to Nextpeer. We'll open Nextpeer when the player taps *Play*. Modify the `update()` method in yourself as follows:

	public void onMultiplayerButtonClicked() {
    	
		// Launch Nextpeer the dashboard when the player taps Play
    	if (NextpeerPlugin.isAvailable()) {
			NextpeerPlugin.launch();
    	}
    	// Else, we don't have tournament mode, run the game normally
    	else {
			game.setScreen(new GameScreen(game));
    	}
	}
	
## Starting and ending tournaments
Go back to the `Game` subclass file and find the method that is in charge of starting tournaments - `onTournamentStart(long tournamentRandomSeed)`. This method will be called by Nextpeer when it’s time to start the tournament and show the game sequence. Nextpeer will hide its user interface in order for the game to show up. The `tournamentRandomSeed` parameter can be used to seed a random number generator. All players are handed identical random seeds so this guarantees all players compete on the same exact level, with the same enemies, power-ups, etc. Since the World class is responsible for generating the level, we will use this value later on when we create the Random object. For now, we'll store the value in the `NextpeerPlugin` container and then switch to the GameScreen screen:

	public void onTournamentStart(long tournamentRandomSeed) {
    	// Start the game
    	NextpeerPlugin.instance().lastKnownTournamentRandomSeed = tournamentRandomSeed;
    	
    	// Start the game screen!
	    setScreen(new GameScreen(this));
	}

When the tournament ends, we will switch back to the main menu, so when the player exits Nextpeer she will be taken back to the main menu screen.

	public void onTournamentEnd() {
    	// End the game
	    NextpeerPlugin.instance().lastKnownTournamentRandomSeed = 0;
	    
	    // Switch back to the main menu
    	setScreen(new MainMenuScreen(this));
	}

## Reporting scores
While a tournament is happening, Nextpeer needs to track each player's score in real time. If a player's score changes (for better or for worse) during a tournament, it needs to be reported using the `NextpeerPlugin.reportScoreForCurrentTournament(int score)` method.
		
		// Game code here
		newScore = oldScore + 100;
 
		// Score has changed - report the score to Nextpeer
		NextpeerPlugin.reportScoreForCurrentTournament(newScore);


## Ending a game-controlled tournament
If your game uses game-controlled tournaments (tournaments that don't have a time limit), then your game is in charge of letting Nextpeer know when the tournament is over. This usually happens when the player dies, finishes a certain track, or runs out of time.
To report that the tournament is over, call `NextpeerPlugin.reportControlledTournamentOverWithScore(int score)` method. Use the **score** parameter to tell Nextpeer the final score for that player.
Once that function is called, Nextpeer will end the tournament for the current player and report the given score as the final score.

	// Game Over event handling
	public void onDeath() {
    	// Game is over, our current player is dead. Notify Nextpeer if needed.
	    NextpeerPlugin.reportControlledTournamentOverWithScore(finalScore);
	}
	
## Forfeiting a tournament
Sometimes you will want to forfeit a tournament on behalf of a player, if, for example, the player wants to quit mid-way and go back to the main menu. 
To forfeit the current tournament, call the `NextpeerPlugin.reportForfeitForCurrentTournament()` method:

	public void userWishesToExit() {
    	// User wishes to exit the current game
        NextpeerPlugin.reportForfeitForCurrentTournament();
	}


## Radnom seed
Nextpeer supports random seed generation for tournaments. Whenever a tournament starts, a random seed will be provided in the `tournamentRandomSeed` parameter. That way, your game can ensure players compete using the same exact level, enemies, powerups etc. For usage examples, take a look at our sample code.


## Next Steps

Congratulations!

You're all done! You should be able to compile and run your new Super Jumper game, complete with Nextpeer-enabled multiplayer features! 

You can continue building the project by adding advanced player-to-player communication. For example, show the other players on the screen or let players interact with the other players while in-game (with power-ups). You can also let your players challenge their Facebook friends by completing the required steps for Facebook integration (for more on that, follow our [Android quick start][docs]).

Check out Nextpeer’s version of the game on the [Play Store][playStore].

Thanks for following the Super Jumper - libGDX Example! If you have any questions or comments, we'd love to hear your thoughts. Please send all feedback to [support@nextpeer.com][support].
[libGDX_plugin_download]: https://github.com/Nextpeer/Nextpeer-libgdx/archive/master.zip
[sample_code]: https://github.com/Nextpeer/Nextpeer-libgdx-Sample
[libgdx]: http://libgdx.badlogicgames.com/
[np]: http://www.nextpeer.com
[docs]: https://developers.nextpeer.com/docs
[eclipse]: http://www.eclipse.org/downloads/
[AndroidSDK]: http://developer.android.com/sdk/index.html
[AndroidADT]: http://developer.android.com/sdk/eclipse-adt.html#installing
[LatestSDK]: https://developers.nextpeer.com/download
[libgdxexample]: https://github.com/Nextpeer/Nextpeer-libgdx/archive/master.zip
[wiki]: https://code.google.com/p/libgdx/wiki/ApplicationPlatformSpecific
[dashboard]: https://developers.nextpeer.com/games
[playstore]: https://play.google.com/store/apps/details?id=com.nextpeer.android.example
[support]: mailto:support@nextpeer.com
[gamekey]: http://s3.amazonaws.com/nextpeer_dashboard_docs/markdown/libgdx/gamekey.jpg
[libGDX_iOS_plugin]: https://www.nextpeer.com/port-your-nextpeer-android-game-to-ios-with-robovm
