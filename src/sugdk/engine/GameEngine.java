package sugdk.engine;

import sugdk.scenes.SceneManager;

/**
 * GameEngine
 * @author nhydock
 *
 * GameEngine is a class designed to handle all the core components of game's system
 * It should hold all the globally accessible variables, for example in an RPG your main
 * party.  It should not handle any kinds of input or graphics rendering.
 */
abstract public class GameEngine {

	//scenes are a main component of a game
	// as such, the engine is left in charge of managing scenes
	SceneManager sceneManager = new SceneManager();
	
	private static boolean isRscLoading = false;
	
	/**
	 * Gets the Engine's scene manager
	 * This is normally necessary if the scene should change or to get the current scene instance
	 * @return engine's scene manager
	 */
	public SceneManager getSceneManager() {
		return sceneManager;
	}

	public static boolean isRscLoading() {
		return isRscLoading;
	}

	public static void setRscLoading(boolean isRscLoading) {
		GameEngine.isRscLoading = isRscLoading;
	}
	
}
