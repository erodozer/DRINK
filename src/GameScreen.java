import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * GameScreen
 * @author nhydock
 *
 *	Main GUI frontend to the engine.  This contains the content panel and provides it with a frame.
 *	It is also the container of the update thread for the graphics buffer and core, linking both
 *	together.
 */
public class GameScreen extends JFrame{

	private static GameScreen _instance;	//singleton instance
	
	ContentPanel c;
	Engine engine;
	
	//frame limiting
    final static int FRAMES_PER_SECOND = 15;					
    final static int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
    private int sleep_time = 0;

    Long next_game_tick = System.currentTimeMillis();
    // the current number of milliseconds
    // that have elapsed since the system was started

	
	/**
	 * Get the singleton instance
	 * @return
	 */
	public static GameScreen getInstance()
	{
		if (_instance == null)
			new GameScreen();
		
		return _instance;
	}
    
	/**
	 * Creates the main game screen
	 */
	private GameScreen()
	{
		engine = Engine.getInstance();
		engine.startGame();
		
		setTitle("DRINK");
		c = new ContentPanel();
		
		//main execution thread will update the scene
		// and then paint the graphics for the scene
		new Thread(){
			@Override
			public void run()
			{
				while (!isInterrupted())
				{
					engine.update();
					c.paint();
					
			        next_game_tick += SKIP_TICKS;
			        sleep_time = (int) (next_game_tick - System.currentTimeMillis());
			        if( sleep_time >= 0 ) {
			            try {
							sleep( sleep_time );
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			        }
				}
			}
		}.start();
		
		c.setPreferredSize(new Dimension(c.INTERNAL_RES_W*5, c.INTERNAL_RES_H*5));
		
		setLayout(null);
		setContentPane(c);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(engine);
		validate();
		
		pack();
		_instance = this;
		
	}

	public static void main(String[] args)
	{
		GameScreen gs = new GameScreen();
	}
}
