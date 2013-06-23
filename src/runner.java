import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import core.DrinkGame;

/**
 * Main game runner for Drink on the Desktop
 * @author nhydock
 *
 */
public class runner{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create the listener that will receive the application events
        ApplicationListener listener = new DrinkGame();
 
        // define the window's title
        String title = "DRINK";
 
        // define the window's size
        int width = 600, height = 400;
 
        // use ES2 to allow for textures that are not powers of 2
        boolean useOpenGLES2 = true;
 
        // create the game using Lwjgl starter class
        new LwjglApplication(listener, title, width, height, useOpenGLES2 );
	}
}
