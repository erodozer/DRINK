package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplet;

/**
 * Main game runner for Drink, applet form
 * @author nhydock
 *
 */
public class AppletMain extends LwjglApplet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("javadoc")
	public AppletMain()
	{
		super(new DrinkGame(), false);
		Gdx.graphics.setVSync(true);
	}

}
