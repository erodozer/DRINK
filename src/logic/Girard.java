package logic;
import java.awt.Color;
import java.awt.Graphics;

import sugdk.graphics.Sprite;


/**
 * Girard
 * @author nhydock
 *
 *	The main enemy of your heroic quest to drink OJ in a room where drinking and eating is
 *	not allowed.  Girard is the only thing standing in your way between freedom from rules
 *	and the oppressive system.  In fact, he is the system, and he's there to put you down.
 *	Lucky for you, he gets distracted very easily, so while he's busy playing League of Legends
 *	and Runes of Magic, chug as quickly as you can.
 */
public class Girard {
	
	//Girard doesn't immediately recognize when you're drinking
	// so you sometimes have a few moments to stop chugging before he
	// realizes
	
	//amount needed to be fully aware
	static final double AWARENESS_NEEDED = 5;	
	
	//awareness needed before looking down again
	static final double AWARENESS_END = 6;

	//awareness can start at a random amount
	static final double AWARENESS_MAX = AWARENESS_NEEDED/2.0;
	static final double AWARENESS_MIN = 0;		

	//rate at which he becomes aware
	static final double AWARENESS_RATE = .1;
	
	//time before next time he looks up
	static final long MAX_TIME_BEFORE_LOOKUP = 25000;
	static final long MIN_TIME_BEFORE_LOOKUP = 5000;
	
	Sprite sprite;
	Sprite surprise;
	
	double awareness = 0;
	long timeWhenLookedDown;
	long timeSinceLookDown;
	long timeUntilLookUp;

	boolean surprised;
	
	/**
	 * Creates a Girard
	 */
	public Girard()
	{
		sprite = new Sprite("girard.png", 3, 1);
		sprite.setX(51);
		sprite.setY(53);
		surprise = new Sprite("surprise.png");
		surprise.setX(51);
		surprise.setY(38);
		setup();
	}
	
	/**
	 * Set Girard to his initial values
	 */
	public void setup()
	{
		timeUntilLookUp = 0;
		timeSinceLookDown = 0;
		surprised = false;
		lookDown();
	}
	
	/**
	 * Girard enters a state of awareness
	 */
	public void lookUp()
	{
		awareness = Math.random()*(AWARENESS_MAX - AWARENESS_MIN);
	}
	
	/**
	 * Girard fiddles with his computer
	 */
	public void lookDown()
	{
		awareness = -1;
		timeWhenLookedDown = System.currentTimeMillis();
		timeSinceLookDown = 0;
		
		//calculate time until he looks up again, it's completely random
		timeUntilLookUp = (long)(Math.random()*(MAX_TIME_BEFORE_LOOKUP-MIN_TIME_BEFORE_LOOKUP));
	}
	
	/**
	 * Updates Girard's lookup/lookdown timer
	 */
	public void update()
	{
		//when looking down, update until look up starts
		if (awareness < 0)
		{
			timeSinceLookDown = System.currentTimeMillis() - timeWhenLookedDown;
			//System.err.println(timeSinceLookDown);
			if (timeSinceLookDown > timeUntilLookUp)
				lookUp();
		}
		//if he's done looking around, look back down at the computer
		else if (awareness >= AWARENESS_END)
		{
			lookDown();
		}
		//update his awareness
		else
		{
			awareness += AWARENESS_RATE;
		}
	}
	
	/**
	 * @return is Girard is aware of his surroundings
	 */
	public boolean isAware()
	{
		return (awareness > AWARENESS_NEEDED);
	}
	
	/**
	 * Girard is surprised by what he sees
	 */
	public void surprise()
	{
		surprised = true;
	}
	
	/**
	 * Draws Girard
	 * @param g
	 */
	public void paint(Graphics g)
	{
		if (awareness >= 0)
			sprite.setFrame(1, 1);
		//makes it look like he's fiddling with his computer when he's not aware
		else
			sprite.setFrame((int)(Math.random()*2)+2, 1);
		sprite.paint(g);
		
		//draw a little awareness meter above his head when he's looking around
		if (awareness > 0 && !surprised)
		{
			g.setColor(Color.RED);
			g.fillRect((int)sprite.getX()-5, (int)sprite.getY()-5, (int)((Math.min(AWARENESS_NEEDED, awareness)/AWARENESS_NEEDED)*20), 2);
			g.setColor(Color.BLACK);
			g.drawRect((int)sprite.getX()-5, (int)sprite.getY()-5, 20, 2);
		}
		else if (surprised)
		{
			surprise.paint(g);
		}
	}
}
