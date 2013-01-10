package logic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import sugdk.graphics.SpriteSheet;
import sugdk.graphics.Animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import core.DataDirs;


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
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("m:s.S");
							//formats the time display
	static final DecimalFormat df = new DecimalFormat("0.00##");
							//formats the score display
	
	double score;			//fluid ounces that have been drunken by Nick
	float time;				//time that Nick has been able to drink
	
	int chugRate;			//the rate at which Nick chugs his OJ
							//also determines the animation used
	int chugStep;			//closeness to upgrading to the next chug rate
	
	int animFrame;			//frame of animation currently being displayed
	boolean loop;			//animation knows if it's looping back or not
		
	long breathe;			//when chugging at the fastest rate, Nick starts to lose his breathe
							//when he runs out of breathe, he passes out and falls over.
	
	Animation[] sprite;			//the sprite animation for Nick
	
	/**
	 * Creates an instance of Nick
	 */
	public Nick()
	{
		SpriteSheet tex = new SpriteSheet(new TextureRegion(new Texture(Gdx.files.internal(DataDirs.ImageDir.path + "sprites.png")), 0, 0, 88, 180), CHUGANIM, CHUGMAX);
		sprite = new Animation[CHUGMAX];
		for (int i = 0; i < sprite.length; i++)
		{
			sprite[i] = new Animation(.10f, tex.getRow(i));
			sprite[i].setPlayMode(Animation.PINGPONG);
			sprite[i].loop();
			sprite[i].setX(82);
			sprite[i].setY(17);
		}
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
		
		if (chugStep >= RATE_TO_INCREASE)
		{
			chugRate = Math.min(CHUGMAX, chugRate+1);
			chugStep = 0;
		}
		else
			chugStep++;
	}
	
	/**
	 * Decreases chugging rate
	 */
	public void decreaseRate()
	{
		chugRate = Math.max(1, chugRate-1);
		chugStep = 0;
	}
	
	/**
	 * @return the amount of time needed between key presses to know if the rate should be modified
	 */
	public float pressRate()
	{
		return (CHUGMAX-chugRate)*.5f + .25f;
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
	 * @param delta - time passed since previous update
	 */
	public void update(float delta)
	{
		if (chugRate > 1)
		{
			time += delta;				//update timer by amount since last checked while chugging
			score += chugRate/100.0;	//increases the score
		}
		sprite[chugRate-1].update(delta);
	}
	
	/**
	 * @return total amount of time spent chugging
	 */
	public float getTime()
	{
		return time;
	}
	
	/**
	 * Paints the character to screen
	 * @param g
	 */
	public void draw(SpriteBatch g)
	{
		sprite[chugRate-1].draw(g);
	}

	/**
	 * @return the player's score
	 */
	public double getScore() {
		return score;
	}
}
