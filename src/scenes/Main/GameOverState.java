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
public class GameOverState extends DrinkState {
	public static final int ID = 1;

	public GameOverState(DrinkSystem parent)
	{
		super(parent);
	}

	@Override
	public boolean start() {
		Engine.getInstance().getGirard().surprise();
		return false;
	}

	/**
	 * Do Nothing
	 */
	@Override
	public void handle(float delta) {}

	/**
	 * Cleanup
	 */
	@Override
	public boolean end() {
		return false;
	}

	@Override
	public boolean keyDown(int key)
	{
		if (key == Input.RESET.code)
		{
			//first we save the values to the high score table
			
			//then we start the game again
			parent.statemachine.setState(PlayState.ID);	
		}
		return false;
	}

	@Override
	public boolean keyUp(int key)
	{
		return false;
	}

}
