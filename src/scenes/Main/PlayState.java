package scenes.Main;

import core.Engine;
import core.Input;
import logic.Girard;
import logic.Nick;
import sugdk.scenes.GameState;
import sugdk.scenes.GameSystem;

public class PlayState extends GameState {

	long lastKeyPress = System.currentTimeMillis();
	
	public PlayState(DrinkSystem c) {
		super(c);
	}
	
	@Override
	public void start() {
		//values are cleared on start
		((DrinkSystem)parent).resetValues();
	}

	@Override
	public void handle() {
		
		Nick n = Engine.getInstance().getNick();
		Girard g = Engine.getInstance().getGirard();
		
		//switch to game over if still chugging while Girard is aware
		if (n.getRate() > 1 && g.isAware())
		{
			finish();
			return;
		}
		
		g.update();
		
		long cT = System.currentTimeMillis();			//time when the key was last pressed
		
		//if the key hasn't been hit fast enough, then the chug rate goese down
		if (cT - lastKeyPress > n.pressRate())
		{
			n.decreaseRate();
		}
		
		n.update();
	}

	/**
	 * Only time finish is called is to switch to game over
	 */
	@Override
	public void finish() {
		parent.setNextState();
	}

	/**
	 * Handles chugging
	 */
	@Override
	public void handleKeyInput(int key) {
		if (key == Input.KEY_DRINK)
		{
			long now = System.currentTimeMillis();
			
			Nick n = Engine.getInstance().getNick();
			
			//increase the drinking rate if the key is pressed in time
			if (now - lastKeyPress < n.pressRate())
			{
				n.increaseRate();
			}

			lastKeyPress = now;
		}
	}

}
