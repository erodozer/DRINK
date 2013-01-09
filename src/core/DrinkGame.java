package core;

import com.badlogic.gdx.Game;

import scenes.Main.MainScene;
import sugdk.scenes.SceneManager;

public class DrinkGame extends Game
{

	private static DrinkGame instance = null;
	
	/**
	 * @return instance of singleton Main
	 */
	public static DrinkGame getInstance()
	{
		if (instance == null)
			instance = new DrinkGame();
		return instance;
	}
	
	Engine engine;
	SceneManager sm;
	
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
