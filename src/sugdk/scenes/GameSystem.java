package sugdk.scenes;

import com.badlogic.gdx.InputProcessor;

/**
 * GameSystem
 * <p/>
 * Abstracted away the logic specifics for a game.  
 * Useful if you want your game to follow a MVC structure.
 * @author nhydock
 */
public interface GameSystem extends InputProcessor{

	/**
	 * Things to do when the system is first started/the scene is switched to
	 */
	public void start();
	
	/**
	 * Things to do when the scene is over and the system should be shutdown
	 */
	public void end();
	
	/**
	 * Update they system while the scene is active
	 * @param delta - amount of time passed since previous update
	 */
	public void update(float delta);
	
}
