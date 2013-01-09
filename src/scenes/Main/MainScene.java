package scenes.Main;

import sugdk.scenes.Scene;

/**
 * @author nhydock
 *
 */
public class MainScene extends Scene<DrinkSystem, DrinkDisplay> {
	
	public static final int ID = 0;
	
	/**
	 * Initialize the main scene for the game
	 */
	public MainScene()
	{
		system = new DrinkSystem();
		display = new DrinkDisplay(system);
	}

}
