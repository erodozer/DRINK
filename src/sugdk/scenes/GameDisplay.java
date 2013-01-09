package sugdk.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * GameDisplay
 * <p/>
 * Abstracted away the display specifics for a game.  
 * Useful if you want your game to follow a MVC structure.
 * 
 * @param <SystemType> GameSystem that the display can examine/be linked to
 * @author nhydock
 * 
 */
public abstract class GameDisplay<SystemType extends GameSystem> {

	protected SpriteBatch batch;
	
	/**
	 * System the the display is to watch and update with
	 */
	protected SystemType system;
	
	/**
	 * Creates a game scene instance
	 * @param system
	 */
	public GameDisplay(SystemType system)
	{
		this.system = system;
		this.batch = new SpriteBatch();
	}
	
	/**
	 * Things to perform when this display is loaded up,
	 * such as making sprite batches and other image loading
	 */
	public abstract void init();
	
	/**
	 * You should dispose of all the graphical elements when done with this display to prevent leaking
	 */
	public abstract void dispose();
	
	/**
	 * Update should be used to update things element properties dependent on the system
	 * @param delta
	 */
	public abstract void update(float delta);
	
	/**
	 * Render should be specifically for drawing stuff to the screen
	 */
	public abstract void render();
	
	/**
	 * Update the display's dimensions.  
	 * Useful for fixing game cameras when the game is adjusted
	 * @param width - width of the viewport
	 * @param height - height of the viewport
	 */
	public abstract void resize(int width, int height);
	
}
