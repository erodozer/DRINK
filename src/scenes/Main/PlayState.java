package scenes.Main;

import core.Engine;
import core.Input;
import logic.Girard;
import logic.Nick;
import sugdk.scenes.GameState;

/**
 * State of the game when you can chug your OJ
 * @author nhydock
 */
public class PlayState extends DrinkState {
	public static final int ID = 0;
	
	float timePassed = 0;
	
	/**
	 * Creates a PlayState
	 * @param parent
	 */
	public PlayState(DrinkSystem parent)
	{
		super(parent);
	}
	
	@Override
	public boolean start() {
		//values are cleared on start
		parent.resetValues();
		return false;
	}

	@Override
	public void handle(float delta) {
		
		Nick n = Engine.getInstance().getNick();
		Girard g = Engine.getInstance().getGirard();
		
		//switch to game over if still chugging while Girard is aware
		if (n.getRate() > 1 && g.isAware())
		{
			parent.statemachine.setState(GameOverState.ID);
			return;
		}
		
		g.update(delta);
		
		timePassed += delta;			//time when the key was last pressed
		
		//if the key hasn't been hit fast enough, then the chug rate goese down
		if (timePassed > n.pressRate())
		{
			n.decreaseRate();
		}
		
		n.update(delta);
		
	}

	/**
	 * Only time finish is called is to switch to game over
	 */
	@Override
	public boolean end() {
		return false;
	}

	/**
	 * Handles chugging
	 */
	@Override
	public boolean keyDown(int key)
	{
		if (key == Input.DRINK.code)
		{
			Nick n = Engine.getInstance().getNick();
			
			//increase the drinking rate if the key is pressed in time
			if (timePassed < n.pressRate())
			{
				n.increaseRate();
			}

			timePassed = 0;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int key)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
