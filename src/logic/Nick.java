package logic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import util.Animation;
import util.SpriteSheet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import core.Engine;

/**
 * Nick
 * @author nhydock
 *
 *	Nick is the hero of this game.  His goal is simple, to drink as much OJ as he
 *	can while the dark, rule enforcing body known simply as Girard tries to stop him.
 */
public class Nick {
	
	static final int CHUGMIN = 0;
	static final int CHUGMAX = 6;
							//amount of different states of chugging
	static final int CHUGANIM = 4;	
							//frames of animation for chugging
	static final int RATEMAX = 599;
							//maximum chugging speed
	static final int RATE_TO_INCREASE = 20;
							//number of button presses needed to increase the rate
	static final int RATE_TO_SLOW = 250;
							//how quickly you stop chugging
	static final SimpleDateFormat sdf = new SimpleDateFormat("m:s.S");
							//formats the time display
	static final DecimalFormat df = new DecimalFormat("0.00##");
							//formats the score display
	
	double score;			//fluid ounces that have been drunken by Nick
	float time;				//time that Nick has been able to drink
	
	int chugRate;			//the rate at which Nick chugs his OJ
							//also determines the animation used
	
	float chugStep;			//closeness to upgrading to the next chug rate
	
	int animFrame;			//frame of animation currently being displayed
	boolean loop;			//animation knows if it's looping back or not
		
	long breathe;			//when chugging at the fastest rate, Nick starts to lose his breathe
							//when he runs out of breathe, he passes out and falls over.
	
	//the sprite animation for Nick
	private Animation[] animations;	
	private Animation activeAnimation;
	private Sprite sprite;
	
	float drinking;			//time since last button press.  Wait too long and he'll stop drinking
	private static final float STOP_DRINKING = .25f;
							//how long it'll take before he starts to stop drinking.  Multiplied by chug rate
	
	/**
	 * Creates an instance of Nick
	 */
	public Nick()
	{
		Texture t = Engine.assets.get("data/sprites.png", Texture.class);
		
		SpriteSheet tex = new SpriteSheet(new TextureRegion(t, 0, 0, 88, 180), 1, CHUGMAX);
		sprite = new Sprite(tex.getFrame(0), 0, 0, tex.getFrameWidth()/CHUGANIM, tex.getFrameHeight());
		
		animations = new Animation[CHUGMAX];
		for (int i = 0; i < tex.frameCount; i++)
		{
			animations[i] = new Animation(tex.getFrame(i), CHUGANIM, .10f, true);
		}
		activeAnimation = animations[0];
		
		sprite.setPosition(82, 17);
	}
	
	/**
	 * Sets Nick's values to their defaults
	 */
	public void setup()
	{
		chugRate = 0;
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
		chugStep = Math.min(chugStep + RATE_TO_INCREASE, RATEMAX);
		this.chugRate = (int)(chugStep / 100);
		drinking = STOP_DRINKING;
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
			time += delta;
			score += chugRate*delta;	//increases the score
		}
		if (drinking <= 0f)
		{
			chugStep = Math.max(0, chugStep - RATE_TO_SLOW*delta);
			this.chugRate = (int)(chugStep / 100);
		}
		else
		{
			drinking -= delta;
		}
		
		this.activeAnimation = this.animations[this.chugRate];
		
		this.activeAnimation.update(delta);
	}
	
	/**
	 * @return total amount of time spent chugging
	 */
	public float getTime()
	{
		return time;
	}
	
	/**
	 * @return the player's score
	 */
	public double getScore() {
		return score;
	}
	
	/**
	 * Draws Nick to the game screen and updates his animation
	 * @param batch - Gdx sprite batch
	 */
	public void draw(SpriteBatch batch)
	{
		this.activeAnimation.set(sprite);
		sprite.draw(batch);
	}
}
