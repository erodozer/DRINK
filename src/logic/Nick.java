package logic;
import java.awt.Graphics;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import sugdk.graphics.Sprite;


/**
 * Nick
 * @author nhydock
 *
 *	Nick is the hero of this game.  His goal is simple, to drink as much OJ as he
 *	can while the dark, rule enforcing body known simply as Girard tries to stop him.
 */
public class Nick{
	
	static final int CHUGMIN = 1;
	static final int CHUGMAX = 6;
							//amount of different states of chugging
	static final int CHUGANIM = 4;	
							//frames of animation for chugging
	static final int RATE_TO_INCREASE = 10;
							//number of button presses needed to increase the rate
	public static final long DIE_RATE = 500;
							//rate at which you fall down ranks in chugging
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("m:s.S");
							//formats the time display
	static final DecimalFormat df = new DecimalFormat("0.00##");
							//formats the score display
	
	double score;			//fluid ounces that have been drunken by Nick
	long time;				//time that Nick has been able to drink
	long startChugTime;		//time of when chugging started
	
	int chugRate;			//the rate at which Nick chugs his OJ
							//also determines the animation used
	int chugStep;			//closeness to upgrading to the next chug rate
	
	int animFrame;			//frame of animation currently being displayed
	boolean loop;			//animation knows if it's looping back or not
		
	long breathe;			//when chugging at the fastest rate, Nick starts to lose his breathe
							//when he runs out of breathe, he passes out and falls over.
	
	Sprite sprite;			//the sprite animation for Nick
	
	/**
	 * Creates an instance of Nick
	 */
	public Nick()
	{

		sprite = new Sprite("nick.png", CHUGANIM, CHUGMAX);
		sprite.setX(82);
		sprite.setY(54);
	}
	
	/**
	 * Sets Nick's values to their defaults
	 */
	public void setup()
	{
		chugRate = 1;
		chugStep = 0;
		score = 0;
		time = 0;
		loop = false;
	}
	/**
	 * Increases the rate of chugging
	 */
	public void increaseRate()
	{
		if (chugRate >= CHUGMAX)
			return;
		
		if (chugRate == 1 && chugStep == 0)
			startChugTime = System.currentTimeMillis();
		
		if (chugStep >= RATE_TO_INCREASE)
		{
			chugRate = Math.min(CHUGMAX, chugRate+1);
			chugStep = 0;
		}
		else
			chugStep++;
	}
	
	public void decreaseRate()
	{
		chugRate = Math.max(1, chugRate-1);
		chugStep = 0;
	}
	
	/**
	 * @return the amount of time needed between key presses to know if the rate should be modified
	 */
	public long pressRate()
	{
		return (CHUGMAX-chugRate)*1000;
	}
	
	/**
	 * @return the rate at which Nick is chugging
	 */
	public int getRate()
	{
		return chugRate;
	}

	/**
	 * Updates timer and score while chugging
	 */
	public void update()
	{
		if (chugRate > 1)
		{
			long cT = System.currentTimeMillis();		//current time
			time += cT-startChugTime;			//update timer by amount since last checked while chugging
			startChugTime = cT;					//set last checked time to current time
			
			score += chugRate/100.0;				//increases the score
		}
	}
	
	/**
	 * @return total amount of time spent chugging
	 */
	public long getTime()
	{
		return time;
	}
	
	/**
	 * Paints the character to screen
	 * @param g
	 */
	public void paint(Graphics g)
	{
		if (loop)
			if (animFrame - 1 == 0)
				loop = false;
			else
				animFrame--;
		else
			if (animFrame + 1 > CHUGMAX)
				loop = true;
			else
				animFrame++;
		
		sprite.setFrame(animFrame, chugRate);
		sprite.paint(g);
	}

	/**
	 * @return the player's score
	 */
	public double getScore() {
		return score;
	}
}
