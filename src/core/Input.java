package core;

import com.badlogic.gdx.Input.Keys;

/**
 * Input
 * @author nhydock
 *
 * Here we define some buttons for use specifically for this game
 */
public enum Input {

	//basic input keys
	/**
	 * Key for chugging
	 */
	DRINK(Keys.SPACE, "SPACE"),
	/**
	 * Key for resetting the game after Game Over
	 */
	RESET(Keys.ENTER, "ENTER");

	/**
	 * 
	 */
	public final String key;
	/**
	 * 
	 */
	public final int code;
	
	private Input(final int key, final String c)
	{
		this.code = key;
		this.key = c;
	}
}
