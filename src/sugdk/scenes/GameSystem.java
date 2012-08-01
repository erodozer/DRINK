package sugdk.scenes;


/**
 * GameSystem
 * @author nhydock
 *
 *  GameSystems is the base class for scene controlling systems
 *  They house all the logic for the scene
 */
public abstract class GameSystem
{
	/**
	 * current state of the scene
	 */
    protected GameState state;
    
    /**
     * Updates the system
     */
    public void update()
    {
    	state.handle();
    }

    /**
     * Advances the system to the next logical state
     */
    abstract public void setNextState();

    /**
     * Gets the current game state of the system
     * @return The current game state
     */
    public GameState getState() {
        return state;
    }   
    
    /**
     * Handles key input
     * @param keyCode	the key code of the key pressed
     */
    public void keyPressed(int keyCode) {
        state.handleKeyInput(keyCode);
    }

    /**
     * Performs actions that should occur when a system is finished
     */
	abstract public void finish();

    /**
     * Performs actions that should occur when a system is started
     */
	public void start() {}

}
