# libGDX - Nextpeer Integration guide

<a id="Introductions"></a>
## Introduction
SuperJumper is a platform-jumping game powered by [libGDX][libgdx] in which players earn points by collecting coins as they guide their character up the game map by bouncing on platforms and springs while dodging the evil squirrels. 

The [Nextpeer][np] platform lets you easily add multiplayer tournaments to your single player game, so users can play with each other become more engaged with the game. The platform supports both time-based and game-controlled (in which the game decides when to end the tournament) tournaments. Nextpeer has many advanced features which allow you to turn your game into a multiplayer phenomenon. This tutorial will cover the basic integration between a libGDX game and Nextpeer. For more advanced topics, please see our [documentation][docs] section.

<a id="Prerequisites"></a>
## Prerequisites

* [Install Eclipse][eclipse]
* [Install Android SDK][AndroidSDK]
* [Install Android ADT][AndroidADT]
* [Download the latest Nextpeer Andoid SDK] [LatestSDK]
* [Download the Nextpeer-libGDX example][libgdxexample]

<a id="ImportProjects"></a>
## Import Projects
The Nextpeer-libGDX example has 2 folders:

+ `starter` - contains the initial version of companion project for this tutorial (without Nextpeer integration). Use this project when going through this tutorial.
+ `final` - contains the final version companion project for this tutorial (with Nextpeer integration). This version is how your code will look like once you’ve completed the tutorial.

The `starter` folder has 3 Java projects in it:

* **superjumper** - the source code for the game core (libGDX)
* **superjumper-android** - the Android project for the game
* **NextpeerConnect** - the Nextpeer Android SDK (we suggest updating this folder with the latest Android SDK from our developer’s dashboard)

Import `superjumper`, `superjumper-android` and `NextpeerConnect` into Eclipse. When importing, be sure to use the standard general import option under *General*, labeled *Existing Projects into Workspace*.

When building the projects, the “superjumper-android” should be the only project which doesn’t compile. If anything goes wrong during this import process, try closing and re-opening projects, doing clean builds, or re-importing.

<a id="SetupTheManifest"></a>
## Setup the Manifest for superjumper-android

### Add Required Permissions

Open the AndroidManifest.xml file in the `superjumper-android` project and switch to the Permissions tab. Click *Add…,* then *Uses Permission*, then *OK*. (You'll see *Uses Permission* in the *Permissions* list, which you should click to highlight.) Pick `android.permission.INTERNET` from the drop-down menu to the right. If you’re using raw XML, copy and paste the following line into the manifest file:

    <uses-permission android:name="android.permission.INTERNET"/>

### Add the NextpeerActivity

Under Application Nodes, click Add…, then Activity, then OK. Once created, select the Activity in the list. It should be named Activity. On the bottom right panel in the ‘Name*’ field enter `com.nextpeer.android.NextpeerActivity`. Save the AndroidManifest.xml file. If you’re using raw XML, copy and paste the following line into the manifest file:

	<activity android:name="com.nextpeer.android.NextpeerActivity" />


### Update the Package Identifier

To avoid collisions with other games, change the package identifier of the app to a unique value. A reverse domain name is recommended:

	<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.mycompany.samples.superjumper">

Be sure to save the file before closing it.

<a id="CreateGameEntry"></a>
## Create a Game Entry in the Nextpeer Dashboard

This part involves creating a new game in Nextpeer’s developer [dashboard][dashboard]. Without a game in the dashboard, you won’t be able to connect the game to the Nextpeer network.

Log into the dashboard. If you don’t have a Nextpeer account, you need to sign up (it's free). Go to the *Games* tab and create a new Android game called “SuperJumper”. Set the *Package* field to the package identifier from the previous section. Click *Save* to save your changes. Copy the *Game key* value to the clipboard, you will need this to initialize the SDK.

![gamekey][gamekey]


### Integrating Nextpeer in Your Game

Inside `superjumper` project, under the `com.nextpeer.libgdx` package you can find the interface between the core Java game project and the Android project (as instructed by libgdx’s wiki under [ApplicationPlatformSpecific][wiki]). The `com.nextpeer.libgdx` package has an abstract class named `Tournaments.java`. This is an OS-independent interface to Nextpeer. You will need to use a concrete, OS-specific implementation in your game. For example, the Android implementation is called `AndroidTournaments.java` (in the `superjumper-android` project).

Paste they game key you received from the Nextpeer developer dashboard in the constructor of `AndroidTournaments.java`. Build the project. At this point the `superjumper-android` project should compile properly.

<a id="Expanding"></a>
## Expanding the Game-Core to Support the Tournaments Class

We’ll start by adding the tournaments to the game logic. Navigate to the `superjumper/com.badlogicgames.superjumper/SuperJumper.java` class. We will modify it to have a Tournaments member and to accept it via the constructor. The code should look like this:
	
	Tournaments tournaments = null;

	public SuperJumper() {
		this(null);
	}
	
	public SuperJumper(Tournaments tournaments) {
		// Make sure Nextpeer is supported:
		if (tournaments != null && tournaments.isSupported()) {
			this.tournaments = tournaments;
		}
	}

Next, we'll have the SuperJumper class conform to the `TournamentsCallback.java` interface. This interface defines callbacks which will be called when a tournament is about to start or end. For now we’ll just leave these methods empty:

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

<a id="TheNextpeerPluginClass"></a>
## The NextpeerPlugin Class

Since different parts of the game will require access to the Tournaments object, we should wrap it with a singleton (in the same manner as Settings and Assets). See the `NextpeerPlugin.java` class in `superjumper` project. It holds the instance of the Tournaments class so it can be safely accessed from the different game screens at any time.

<a id="StartingandFinishingTournaments"></a>
## Starting And Finishing Tournaments

Go back to the `SuperJumper.java` file and find the method that is in charge of starting tournaments - `onTournamentStart(long tournamentRandomSeed)`. This method will be called by Nextpeer when it’s time to start the tournament and show the game sequence. Nextpeer will hide its user interface in order for the game to show up. The `tournamentRandomSeed` parameter can be used to seed a random number generator. All players are handed identical random seeds so this guarantees all players compete on the same exact level, with the same enemies, power-ups, etc. Since the World class is responsible for generating the level, we will use this value later on when we create the Random object. For now, we'll store the value in the `NextpeerPlugin` container and then switch to the GameScreen screen:

	public void onTournamentStart(long tournamentRandomSeed) {
    	// Start the game
    	NextpeerPlugin.instance().lastKnownTournamentRandomSeed = tournamentRandomSeed;
	    setScreen(new GameScreen(this));
	}

When the tournament ends, we will switch back to the main menu, so when the player exits Nextpeer she will be taken back to the main menu screen.

	public void onTournamentEnd() {
    	// End the game, switch to main menu
	    NextpeerPlugin.instance().lastKnownTournamentRandomSeed = 0;
    	setScreen(new MainMenuScreen(this));
	}
	
<a id="LaunchingNextpeer"></a>
## Launching Nextpeer

Now that we’ve set the callback handlers, we can set up an entry point to Nextpeer. We'll open Nextpeer when the player taps *Play*. Modify the `update()` method in `superjumper/com.badlogicgames.superjumper/MainMenuScreen.java` as follows:

	if (OverlapTester.pointInRectangle(playBounds, touchPoint.x, touchPoint.y)) {
    	Assets.playSound(Assets.clickSound);
    
    	// game.setScreen(new GameScreen(game));
    	
		// Launch Nextpeer the dashboard when the player taps Play
    	if (NextpeerPlugin.isAvailable()) {
			NextpeerPlugin.tournaments().launch();
    	}
    	// Else, we don't have tournament mode, run the game normally
    	else {
			game.setScreen(new GameScreen(game));
    	}
 
    	return;
	}

This way, if the OS supports Nextpeer, it will be launched once the player taps “Play”. If not, a single player game will start.

<a id="ReportingScore"></a>
## Reporting the Score

While a tournament is ongoing, Nextpeer needs to keep track of each player's score in real time. If a player's score changes (for better or worse) during a tournament, it needs to be reported to Nextpeer. Go to the `GameScreen.java` class in the `superjumper` project. Find the `updateRunning()` method and add a call to `reportScoreForCurrentTournament()` at the bottom of `updateRunning()`:

	if (world.score != lastScore) {
		lastScore = world.score;
		scoreString = "SCORE: " + lastScore;

		if (NextpeerPlugin.isAvailable()) {
			NextpeerPlugin.tournaments().reportScoreForCurrentTournament(lastScore);
		}
	}

<a id="ForfeitingaTournament"></a>
## Forfeiting a Tournament
When a player wishes to exit the game mid-tournament, Nextpeer needs to be notified so that it can remove that player from the tournament. In `GameScreen.java`, find the `updatePaused()` method and inside it locate the section where the user opts to return to the main menu. Add a call to `reportForfeitForCurrentTournament()`:

	if (OverlapTester.pointInRectangle(quitBounds, touchPoint.x, touchPoint.y)) {
		Assets.playSound(Assets.clickSound);	

		if (NextpeerPlugin.isAvailable()) {
			NextpeerPlugin.tournaments().reportForfeitForCurrentTournament();
		}
			
		game.setScreen(new MainMenuScreen(game));
		return;
	}

<a id="EndingaTournament"></a>
## Ending a Tournament

In SuperJumper, a tournament ends once the player falls off the edge of the screen or when the player reaches the castle. Once that happens, the Nextpeer tournament for that player should end and whatever score that player had at that point is that player’s final score. To report the end of the tournament, find the `updateLevelEnd()` method in the `GameScreen` class, and add a call to the `reportControlledTournamentOverWithScore()` method:

	private void updateLevelEnd () {
	
		if (Gdx.input.justTouched()) {
			world = new World(worldListener);
			renderer = new WorldRenderer(batcher, world);
			world.score = lastScore;
			state = GAME_READY;

			if (NextpeerPlugin.isAvailable()) {
				NextpeerPlugin.tournaments().reportControlledTournamentOverWithScore(lastScore);
			}
		}
	}

Add the same code in the `updateGameOver()` method:

	private void updateGameOver () {

		if (Gdx.input.justTouched()) {			
	
			if (NextpeerPlugin.isAvailable()) {
				NextpeerPlugin.tournaments().reportControlledTournamentOverWithScore(lastScore);
			}

			game.setScreen(new MainMenuScreen(game));
		}
	}

<a id="SameLevel"></a>
## Making Sure all Players Are on the Same Level

In order to keep the game fair for all players we need to make sure all the players are playing the exact same level. This is done by setting the seed for the random number generator. Go to the `World` class and locate the constructor. There, use the random seed we’ve stored in `NextpeerPlugin` and pass it to Random’s constructor (right before `generateLevel()`):

	public World (WorldListener listener) {
		this.bob = new Bob(5, 1);
		this.platforms = new ArrayList<Platform>();
		this.springs = new ArrayList<Spring>();
		this.squirrels = new ArrayList<Squirrel>();
		this.coins = new ArrayList<Coin>();
		this.listener = listener;
		
		long randomSeed = 0;

		if (NextpeerPlugin.isAvailable()) {
			randomSeed = NextpeerPlugin.instance().lastKnownTournamentRandomSeed;
		}

		if (randomSeed == 0) {
			rand = new Random();
		}
		else {
			rand = new Random(randomSeed);	
		}

		generateLevel();

		this.heightSoFar = 0;
		this.score = 0;
		this.state = WORLD_STATE_RUNNING;
	}

<a id="AndroidSpecific"></a>
## Android-Specific Integration

Navigate to the `superjumper-android` project's `SuperJumperAndroid.java` class. We will need to pass the `AndroidTournaments` instance to the `SuperJumper` game constructor. We will also need to handle the use case of navigation out of the game (pressing on the back button or navigate to the home screen) while in tournament mode (so the other active players will know that the player has forfeit the game). We will also let Nextpeer know that the user session has started. Expand the `SuperJumperAndroid.java` class with the following code:

	private AndroidTournaments mTournaments = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mTournaments = new AndroidTournaments(this);
		
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
    
<a id="NextSteps"></a>
## Next Steps

Congratulations!

You're all done! You should be able to compile and run your new Super Jumper game, complete with Nextpeer-enabled multiplayer features! 

You can continue building the project by adding advanced player-to-player communication. For example, show the other players on the screen or let players interact with the other players while in-game (with power-ups). You can also let your players challenge their Facebook friends by completing the required steps for Facebook integration (for more on that, follow our [Android quick start][docs]).

Check out Nextpeer’s version of the game on the [Play Store][playStore].

Thanks for following the Super Jumper - libGDX Example! If you have any questions or comments, we'd love to hear your thoughts. Please send all feedback to [support@nextpeer.com][support].

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
