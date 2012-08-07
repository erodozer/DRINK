package sugdk.scenes;

import java.util.ArrayList;

/**
 * SceneManager
 * @author nhydock
 *
 * Scene manager is a class that allows simple construction of Scenes as wells
 * as their visible stack.  Scenes can be generated dynamically with ease with
 * the Scene manager, and scenes generated can also told to only have one instance
 * available that'll get reused.
 * 
 * The scene manager prevent you from having 2 scenes of the same type open at
 * one time.  It can also allow you to have singleton scenes which will be scenes
 * that only ever get initialized once by the manager and will be restarted whenever
 * it becomes the active scene.
 * 
 */
public class SceneManager{

	//list of observers who want to be notified on various actiosn by the scene manager
	ArrayList<SceneManagerListener> observers = new ArrayList<SceneManagerListener>();
	
	//list of currently loaded scenes
	ArrayList<Scene> sceneList = new ArrayList<Scene>();
	
	//list of all the scene classes
	//by keeping track of the classes, which is what we use to identify the scenes
	//we're able to know which ones have already been loaded
	//class list is parallel to scene list, so the indexes are shared with the respectively loaded scene
	ArrayList<Class<? extends Scene>> classList = new ArrayList<Class<? extends Scene>>();
	
	/**
	 * Generates a new scene
	 * @param scene the main class of the scene you wish to add
	 * @return true if the scene was able to be managed
	 *          false if the scene has already been added to the list
	 */
	public boolean addScene(Class<? extends Scene> scene)
	{
		Scene s = null;
		
		//don't try and manage the scene if it's already being managed
		if (classList.contains(scene))
			return false;
				
		//first it checks to see if there's a singleton getter for the scene
		try {
			s = (Scene) scene.getMethod("getInstance").invoke(null);
		} 
		//if no singleton method exists, then it will just create a new instance
		catch (NoSuchMethodException e) {
			try {
				s = scene.newInstance();
			} 
			//if a scene isn't able to instantiate properly, then you have a problem
			catch (Exception e1) {
				e1.printStackTrace();
				System.exit(-1);
			}
		}
		//if a scene isn't able to instantiate properly, then you have a problem
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);	
		}
		sceneList.add(s);
		classList.add(scene);
		
		informSceneAdded();
		
		//detect if this is the first scene being added, if so then the manager is also recognizing it as the active scene
		if (sceneList.size() == 1)
			changeScene(scene);
		
		return true;
	}
	
	/**
	 * Changes the actively displaying scene
	 */
	public boolean changeScene(Class<? extends Scene> scene)
	{
		//a scene must be added to the manager before it can be changed to
		if (!classList.contains(scene))
			return false;
		
		Scene active = getCurrentScene();
		
		active.stop();
		
		//swap the class into active position
		int i = classList.indexOf(scene);
		classList.add(classList.remove(i));
		sceneList.add(sceneList.remove(i));
		
		//now get the scene that's supposed to be active after the swap
		active = getCurrentScene();
		
		//and start the scene
		active.start();
		
		informSceneChange();
		
		return true;
	}

	/**
	 * @return the currently visible/active scene
	 */
	public Scene getCurrentScene()
	{
		//return null if no scenes have been loaded yet
		if (sceneList.size() == 0)
			return null;
		
		//active scene is the one on top of the stack
		return sceneList.get(sceneList.size()-1);
	}
	
	/**
	 * Adds a new listener to this scene manager
	 * @param o
	 */
	public void addSceneManagerListener(SceneManagerListener o)
	{
		observers.add(o);
	}
	
	/**
	 * Informs all listeners of an active scene change
	 */
	private void informSceneChange()
	{
		for (SceneManagerListener s : observers)
			s.onSceneChange();
	}
	
	/**
	 * Informs all listeners of a scene being added
	 */
	private void informSceneAdded()
	{
		for (SceneManagerListener s : observers)
			s.onSceneAdd();
	}
}
