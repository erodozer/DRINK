package scenes.Main;

import core.Engine;
import core.Input;
import sugdk.scenes.GameState;
import sugdk.scenes.GameSystem;

/**
 * GameOverState
 * @author nhydock
 *
 *	Handles what happens when the game is over
 */
public class GameOverState extends GameState {
	
	public GameOverState(GameSystem c) {
		super(c);
	}

	@Override
	public void start() {
		Engine.getInstance().getGirard().surprise();
	}

	/**
	 * Do Nothing
	 */
	@Override
	public void handle() {}

	@Override
	public void finish() {
		//first we save the values to the high score table
		
		//then we start the game again
		parent.setNextState();
	}

	@Override
	public void handleKeyInput(int key) {
		//we finish when b is hit
		if (key == Input.KEY_RESET)
			finish();
	}

}
