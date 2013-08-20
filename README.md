#libGDX - Nextpeer Integration guide

<a id="Introductions"></a>
##Introduction
SuperJumper is a platform jumping game (powered by [libGDX][libgdx]) in which players earn points by collecting coins as they guide their jumper up the game map by bouncing on platforms and springs while dodging the evil squirrels. 

The [Nextpeer][np] platform lets you easily add multiplayer tournaments to your single player game. This in turn enables users to play with each other and to remain better engaged with the game. The platform supports either time based tournaments or game controlled (in which the game decides when to end the tournament) tournaments. 
Nextpeer has many advanced features which allow you to turn your game into a multiplayer phenomena. This tutorial will cover the basic integration that needs to be done. We strongly advise serious developers to dig deeper in our [documentation][docs] section.
<a id="Prerequisites"></a>
##Prerequisites

[Install Eclipse][eclipse] <br>
[Install Android SDK][AndroidSDK] <br>
[Install Android ADT][AndroidADT] <br>
[Download the latest Nextpeer Andoid SDK] [LatestSDK] <br>
[Download the Nextpeer-libGDX example][libgdxexample] <br>

<a id="ImportProjects"></a>
## Import Projects
The Nextpeer-libGDX example has 2 folders:

+ `starter` - which contains the start project for this tutorial. Use this project when going through this tutorial.
+ `final`- which contains the complete source code for this tutorial. This version is how your code will look like once you’ve completed the tutorial.

The `starter` folder has 5 Eclipse projects in it: <br>
**superjumper** - The source code for the game core (libgdx)<br>
**superjumper-android** - The Android project for the game<br>
**NextpeerConnect** - NextpeerAndroid SDK (we suggest updating this folder with the latest Android SDK from our developer’s dashboard).<br>
**libgdx-nextpeer-bridge** - An interface between the core java game project to the Android project (as instructed by libgdx’s wiki under [ApplicationPlatformSpecific)][wiki].<br>
**glue** - This Android library is been used as a workaround for issues involved when linking a pure java project (libgdx-nextpeer-bridge) with an Android project.<br>

###Import those projects into Eclipse
Import superjumper, superjumper-android, NextpeerConnect, libgdx-nextpeer-bridge and glue into Eclipse. When importing, be sure to use the standard general import option under “General” labeled “Existing Projects into Workspace”.<br>
When building the projects the “superjumper-android” should be the only project which doesn’t compile. If anything gets goofy during this import process, try closing and re-opening projects, doing clean builds, or re-importing.

<a id="SetupTheManifest"></a>
##Setup The Manifest For superjumper-android
###Add Required Permissions
Next, open the AndroidManifest.xml file in the superjumper-android project and switch to the Permissions tab. Click Add..., then Uses Permission, then OK, You'll see Uses Permission in the Permissions list, which you should click to highlight. Pick `android.permission.INTERNET` from the drop-down menu to the right.
If you’re using raw XML, copy and paste the following line into the manifest file:

	<uses-permission android:name="android.permission.INTERNET"/>

###Add The NextpeerActivity
Under Application Nodes, click Add…, then Activity, then OK. Once created, select the Activity in the list. It should be named Activity. On the bottom right panel in the ‘Name*’ field enter `com.nextpeer.android.NextpeerActivity`. Save the AndroidManifest.xml file.
If you’re using raw XML, copy and paste the following line into the manifest file:
	
	<activity android:name="com.nextpeer.android.NextpeerActivity" />


###Update The Package Identifier
To avoid collisions with other games, change the package identifier of the app to something unique, say “com.yourcompany.sample.superjumper”.
Like so:

	<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    	  package="com.mycompany.samples.superjumper"
Be sure to save the file before closing it.

<a id="CreateGameEntry"></a>
##Create A Game Entry In The Nextpeer Dashboard
The next part involves creating a new game in Nextpeer’s developer [dashboard][dashboard]. Without a game in the dashboard, you won’t be able to connect the game to the Nextpeer network.

Start out by logging into the dashboard. If you don’t have a Nextpeer account, just sign up for one. Go to the Games tab and create a new Android game called “SuperJumper”.
Add the package identifier you’ve picked to the “Package” field. Tap “Save” to save your changes.
Copy the “Game key” value to the clipboard, you will need this to initialize the SDK.

![gamekey][gamekey]


###Integrating Nextpeer In Your Game
The bridge project (libgdx-nextpeer-bridge) defines an abstract class named `Tournaments.java`. This abstract class defines OS specific code which is required by the Nextpeer platform. You can see the Android implementation for this abstract class under “superjumper-android” with “AndroidTournaments.java”.


Paste they game key you received from the Nextpeer developer dashboard in the constructor in `AndroidTournaments.java`. Build the project. At this point superjumper-android should compile properly.

<a id="Expanding"></a>
##Expanding The Game-core To Support The Tournaments Class
We’ll start out by adding the tournaments to the game logic.
Navigate to the “superjumper/com.badlogicgames.superjumper/SuperJumper.java”  class. We will modify it to have a Tournaments member and to accept via the constructor.
The code should look like this:
	
	Tournaments tournaments = null;

	public SuperJumper() {
		this(null);
	}
	
	public SuperJumper(Tournaments tournaments) {
		// If we have a supported tournaments object, set the game as callback
		if (tournaments != null && tournaments.isSupported()) {
			this.tournaments = tournaments;
		}
	}

The next thing we’ll do is have the SuperJumper class comform to the `TournamentsCallback.java` interface. This interface will let us know when a particular tournament is supposed to start or end. For the time being, we’ll just leave those methods empty:

	@Override
	public void onTournamentStart(long tournamentRandomSeed) 
	{
		// Start the tournament!
	}
	@Override
	public void onTournamentEnd() 
	{
		// End the tournament
	}

<a id="TheNextpeerPluginClass"></a>
##The NextpeerPlugin Class
Since different parts of the game will require access to the tournaments class, we should wrap it with a singleton (in the same manner as Settings and Assets are wrapped).
Take a look on the `NextpeerPlugin.java` class in “SuperJumper” project. It holds the instance of the Tournaments object so it can be safely accessed from the different game screens at different points in time.

<a id="StartingandFinishingTournaments"></a>
##Starting And Finishing Tournaments

Let’s go back to the SuperJumper.java file and look at the method that is in charge of starting tournaments - `onTournamentStart(long tournamentRandomSeed)`.
This method will be triggered by Nextpeer when it’s time to start the tournament and show the game sequence. Nextpeer will hide its user interface in order for the game to show up.
The `tournamentRandomSeed` paramater can be used to seed your random generator. All players are handed identical random seeds so this guarantees players all compete using the same exact level, enemies, powerups etc. 
Since the World class is responsible for generating the level, we will use this value later on when we’ll create the Random object. For now, let’s store the value in the `NextpeerPlugin` container and then switch to the GameScreen screen:

	public void onTournamentStart(long tournamentRandomSeed) {
    	// Start the game
    	NextpeerPlugin.instance().lastKnownTournamentRandomSeed = tournamentRandomSeed;
	    setScreen(new GameScreen(this));
	}

This takes care of starting a tournament, but what should we do once the tournament ends? Well, we should switch back to the main menu, that way if the player exits Nextpeer she will be taken back to the main menu screen.

	public void onTournamentEnd() {
    	// End the game, switch to main menu
	    NextpeerPlugin.instance().lastKnownTournamentRandomSeed = 0;
    	setScreen(new MainMenuScreen(this));
	}
	
<a id="LaunchingNextpeer"></a>
##Launching Nextpeer
Now that we’ve set the callback handlers, we can open up an entrypoint to Nextpeer for the player. Basically, we want the player to be taken to Nextpeer’s screen once she taps “Play”. Modify the update() method in superjumper/com.badlogicgames.superjumper/MainMenuScreen.java as follows (add the code when the player taps the “Play” rect):

	if (OverlapTester.pointInRectangle(playBounds, touchPoint.x, touchPoint.y)) {
    	Assets.playSound(Assets.clickSound);
    
    	// game.setScreen(new GameScreen(game));
    
		// Launch Nextpeer the dashboard when someone taps on Play
    	if (NextpeerPlugin.isAvailable()) {
			NextpeerPlugin.tournaments().launch();
    	}
    	// Else, we don't have tournament mode, run the game normally
    	else {
			game.setScreen(new GameScreen(game));
    	}
 
    	return;
	}

With this code, if the OS supports Nextpeer then it will be launched once the player taps “Play”. If not then a single player game is started.

<a id="ReportingScore"></a>
##Reporting Score
Whilst a tournament is ongoing, Nextpeer needs to keep track of each player's score in real time. If a player's score changes (for better or for worse) during a tournament, it needs to be reported to Nextpeer. 
Go to the `GameScreen.java` class in the superjumper project. Find the `updateRunning()` method and add a call to `reportScoreForCurrentTournament()` once lastScore changes (add the following code at the bottom of the method):

	if (world.score != lastScore) {
		lastScore = world.score;
		scoreString = "SCORE: " + lastScore;

		if (NextpeerPlugin.isAvailable()) {
			NextpeerPlugin.tournaments().reportScoreForCurrentTournament(lastScore);
		}
	}

<a id="ForfeitingaTournament"></a>
##Forfeiting A Tournament
When a player wishes to exit  the game mid-tournament, Nextpeer needs to be notified so that it can remove that player from the tournament. In `GameScreen.java`, find the `updatePaused()` method and inside it locate the section where the user opts to return to the main menu. There, we’ll add a call to `reportForfeitForCurrentTournament()` of the Tournaments object if the user wish to exit the game.

	if (OverlapTester.pointInRectangle(quitBounds, touchPoint.x, touchPoint.y)) {
		Assets.playSound(Assets.clickSound);	

		if (NextpeerPlugin.isAvailable()) {
			NextpeerPlugin.tournaments().reportForfeitForCurrentTournament();
		}
			
		game.setScreen(new MainMenuScreen(game));
		return;
	}

<a id="EndingaTournament"></a>
##Ending A Tournament
In SuperJumper, a tournament ends once the player falls off the edge of the screen. Once that happens, the Nextpeer tournament for that player should end and whatever score that player had at that point is that player’s final score. To report on that, find the `updateLevelEnd()` method in the GameScreen class, and add a call to the `reportControlledTournamentOverWithScore()` method:

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
##Making Sure All Players Are On The Same Level
In order to keep the game fair for all players we need to make sure the players playing the exact same level. This is done by setting the random seed. Go to the World class and locate the constructor method. There, use the random seed we’ve stored in `NextpeerPlugin` and pass it to Random’s constructor (right before `generateLevel()`):

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


<a id="NextSteps"></a>
##Next Steps
Congratulations!
You're all done! You should be able to compile and run your new Super Jumper game, complete with Nextpeer-enabled multiplayer features! 
You can continue building the project by adding advanced player to player communication, for example show the other players on the screen, letting players interact with the other players whilst in-game (with powerups) or even make the game time-based!
Checkout Nextpeer’s version for the game on [Playstore][playStore].

Thanks for following the Super Jumper - LibGDX Example! If you have any questions or comments, we'd love to hear your thoughts. Please send all feedback through the [support@nextpeer.com][support].

[libgdx]: http://libgdx.badlogicgames.com/
[np]: http://www.nextpeer.com
[docs]: https://developers.nextpeer.com/docs
[eclipse]: http://www.eclipse.org/downloads/
[AndroidSDK]: http://developer.android.com/sdk/index.html
[AndroidADT]: http://developer.android.com/sdk/eclipse-adt.html#installing
[LatestSDK]: https://developers.nextpeer.com/download
[libgdxexample]: https://github.com/ItamarM/Nextpeer-libgdx/archive/master.zip
[wiki]: https://code.google.com/p/libgdx/wiki/ApplicationPlatformSpecific
[dashboard]: https://developers.nextpeer.com/games
[playstore]: https://play.google.com/store/apps/details?id=com.nextpeer.android.example
[support]: mailto:support@nextpeer.com
[gamekey]: http://s3.amazonaws.com/nextpeer_dashboard_docs/markdown/libgdx/gamekey.jpg
