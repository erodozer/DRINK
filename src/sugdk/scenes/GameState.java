package sugdk.scenes;

/**
 * GameState
 * <p/>
 * State interface for allowing scene systems to have their own statemachine to them
 * @author nhydock
 * @param <System> kind of GameSystem that the GameState belongs to
 *
 */
public abstract class GameState<System extends GameSystem> {

	/**
	 * ID identifier of the state
	 */
	public static final int ID = 0;
	
	/**
	 * System that the state belongs to
	 */
	protected System parent;
	
	/**
	 * Creates a game state
	 * @param parent
	 */
	public GameState(System parent)
	{
		this.parent = parent;
	}
	
	/**
	 * Does handling of things that need to be updated per cycle
	 * @param delta - time passed since last update cycle
	 */
	public abstract void handle(float delta);
	
	/**
	 * Performs state setup when the state is switched to
	 * Start processing can be performed repeatedly until an end condition is met
	 * before the state machine will switch into doing regular update handling.
	 * <p/>
	 * Note that process operations should not and can not be dependent on updating time
	 * @return 
	 *  	true if the state is done performing its start processing
	 */
	public abstract boolean start();
	
	/**
	 * Performs state setup when the state is switched away from.
	 * End processing can be performed repeatedly until an end condition is met before
	 * the machine will switch to the next state.
	 * <p/>
	 * Note that process operations should not and can not be dependent on updating time
	 * @return 
	 *  	true if the state is done performing its end processing
	 */
	public abstract boolean end();
	
	/*
	 * INPUT PROCESSING METHODS
	 * 
	 * Only the most commonly used ones for games are set as abstract to require definition,
	 * else they'll just auto assume they do nothing.
	 */
	
	/**
	 * Handle input when a key is pressed down
	 * @param key
	 * @return
	 */
	public abstract boolean keyDown(int key);
	
	/**
	 * Handle input when a key is released
	 * @param key
	 * @return
	 */
	public abstract boolean keyUp(int key);

	/**
	 * Handle input when a key is typed
	 * @param keyChar
	 * @return
	 */
	public boolean keyTyped(char keyChar) {
		// TODO Auto-generated method stub
		return false;
	}


	/**
	 * Handle mouse input when the mouse moves
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Handle mouse input when the scrolls a page (scroll wheel)
	 * @param arg0
	 * @return
	 */
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Handle mouse input when a button is clicked
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 */
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Handle mouse input when a button is clicked down and the mouse is moved before releasing
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Handle mouse input when a button is released
	 * @param x
	 * @param y
	 * @param pointer
	 * @param button
	 * @return
	 */
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
}
