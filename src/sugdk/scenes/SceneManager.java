package sugdk.scenes;

import java.util.HashMap;

import com.badlogic.gdx.Game;

/**
 * SceneManager
 * <p/>
 * SceneManager is a state machine manager for use with libGdx that helps abstract
 * the setting and creation of scenes.
 * @author nhydock
 *
 */
public class SceneManager {
	
	HashMap<Integer, Scene<?, ?>> sceneList = new HashMap<Integer, Scene<?, ?>>();
	
	Game linkedGame;
	
	/**
	 * Creates a new scene manager
	 * @param game - game to link the scene manager to
	 */
	public SceneManager(Game game)
	{
		linkedGame = game;
	}
	
	/**
	 * Adds a new scene to the manager
	 * @param id - identifying number for the scene
	 * @param scene - scene class
	 * @throws Exception when id is taken
	 * @throws NullPointerException when scene is null
	 */
	public void addScene(int id, Scene<?, ?> scene) throws Exception, NullPointerException
	{
		if (!sceneList.containsKey(id))
		{
			if (scene != null)
			{
				sceneList.put(id, scene);
			}
			else
			{
				throw (new NullPointerException("Scene is null"));
			}
		}
		else
		{
			throw (new Exception("ID already taken by another scene"));
		}
	}
	
	/**
	 * Changes the active scene
	 * @param sceneID - id by which the scene is identified
	 */
	public void setScene(int sceneID)
	{
		Scene<?, ?> s = sceneList.get(sceneID);
		if (s != null)
		{
			linkedGame.setScreen(s);
		}
	}
}
