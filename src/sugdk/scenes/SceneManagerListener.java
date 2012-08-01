package sugdk.scenes;

/**
 * SceneChangeObserver
 * @author nhydock
 *
 * Small observer class for those who want a class to observe when the active scene changes
 */
public interface SceneManagerListener {

	/**
	 * Action to perform when the active scene has changed
	 */
	public void onSceneChange();
	
	/**
	 * Action to perform when a scene has been added
	 */
	public void onSceneAdd();
}
