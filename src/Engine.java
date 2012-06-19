

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Engine
 * @author nhydock
 *
 *	Main engine class that is home to the core logic of the game
 */
public class Engine implements KeyListener{

	public static final boolean isRscLoading = true;
	
	private static Engine _instance;	//singleton instance
	static final int INPUT = KeyEvent.VK_SPACE;	
							 			//the key required for input
	static final int RESET = KeyEvent.VK_ENTER;
										//the key required to reset the game after game over
	
	
	Nick you;							//the player drinking
	Girard foe;							//your mortal enemy
	
	long lastKeyPress;					//last time the key was pressed
	long knockDown;						//time to wait before knocking down chug mode
	
	boolean gameover;
	
	/**
	 * Get the singleton instance
	 * @return
	 */
	public static Engine getInstance()
	{
		if (_instance == null)
			new Engine();
		
		return _instance;
	}

	/**
	 * Create the engine instance
	 */
	private Engine()
	{
		you = Nick.getInstance();
		foe = Girard.getInstance();
		_instance = this;
	}
	
	public void startGame()
	{
		lastKeyPress = 0;
		knockDown = 0;
		you.setup();
		foe.setup();
		gameover = false;
	}

	public void GameOver()
	{
		gameover = true;
		you.chugRate = 1;
		foe.surprised = true;
	}
	
	/**
	 * Update core logic
	 */
	public void update()
	{
		//don't update if game is over
		if (gameover)
			return;
		
		if (you.chugRate > 1 && foe.isAware())
		{
			GameOver();
			return;
		}
		foe.update();
		
		long cT = System.currentTimeMillis();			//time when the key was last pressed
		
		//if the key was smashed fast enough the update the rate
		if (cT - lastKeyPress > Nick.DIE_RATE*you.chugRate/2)
		{
			you.decreaseRate();
		}
		
		you.update();
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == INPUT && !gameover)
		{
			long cT = arg0.getWhen();			//time when the key was last pressed
			
			//if the key was smashed fast enough the update the rate
			if (cT - lastKeyPress < you.pressRate())
			{
				you.increaseRate();
			}
			lastKeyPress = cT;
		}
		if (arg0.getKeyCode() == RESET && gameover)
			startGame();
		arg0.consume();
	}

	/**
	 * We use key typed so then you can't hold down the key and cheat
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {

	}

}
