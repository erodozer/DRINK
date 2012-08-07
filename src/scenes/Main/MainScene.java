package scenes.Main;

import sugdk.scenes.Scene;

public class MainScene extends Scene {

	/**
	 * Initialize the main scene for the game
	 */
	public MainScene()
	{
		system = new DrinkSystem();
		display = new DrinkDisplay();
		display.setParent(system);
	}
	
	/**
	 * Starts the system of the game
	 */
	@Override
	public void start() {
		system.start();
		((DrinkDisplay)display).fixScreen();
	}

	/**
	 * This is the only scene of the game, so stop is never called
	 */
	@Override
	public void stop() {}

}
