package core;

import scenes.Main.MainScene;
import sugdk.scenes.SceneManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;

/**
 * Main game runner for Drink
 * @author nhydock
 *
 */
public class AppletMain extends Game {

	private static AppletMain instance = null;
	
	/**
	 * @return instance of singleton Main
	 */
	public static AppletMain getInstance()
	{
		if (instance == null)
			instance = new AppletMain();
		return instance;
	}
	
	Engine engine;
	SceneManager sm;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create the listener that will receive the application events
        ApplicationListener listener = new AppletMain();
 
        // define the window's title
        String title = "DRINK";
 
        // define the window's size
        int width = 600, height = 400;
 
        // use ES2 to allow for textures that are not powers of 2
        boolean useOpenGLES2 = true;
 
        // create the game using Lwjgl starter class
        new LwjglApplication( listener, title, width, height, useOpenGLES2 );
	}

	@Override
	public void create()
	{
		engine = Engine.getInstance();
		
		sm = new SceneManager(this);
		
		try
		{
			sm.addScene(MainScene.ID, new MainScene());
			sm.setScene(MainScene.ID);
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
