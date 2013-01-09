package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import core.DataDirs;

import sugdk.graphics.Animation;

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
	static final float AWARENESS_NEEDED = 5.0f;	
	
	//awareness needed before looking down again
	static final float AWARENESS_END = 6.0f;

	//awareness can start at a random amount
	static final float AWARENESS_MAX = AWARENESS_NEEDED/2.0f;
	static final float AWARENESS_MIN = 0.0f;		

	//should take this long to become fully aware from a state of minimum awareness
	static final float AWARENESS_RATE = 1.5f;
	
	//time before next time he looks up
	static final float MAX_TIME_BEFORE_LOOKUP = 25;
	static final float MIN_TIME_BEFORE_LOOKUP = 5;
	
	Animation distracted;
	Sprite aware;
	Sprite surprise;
	
	//awareness bar
	Sprite bar;
	Sprite fill;
	
	float awareness = 0f;
	float timeWhenLookedDown;
	float timeSinceLookDown;
	float timeUntilLookUp;

	boolean surprised;
	
	/**
	 * Creates a Girard
	 */
	public Girard()
	{
		Texture idleTex = new Texture(Gdx.files.internal(DataDirs.ImageDir.path + "girard.png"));
		TextureRegion[] regions = {
				new TextureRegion(idleTex, idleTex.getWidth()/3, 0, idleTex.getWidth()/3, idleTex.getHeight()),
				new TextureRegion(idleTex, 2*idleTex.getWidth()/3, 0, idleTex.getWidth()/3, idleTex.getHeight()),
				};
		distracted = new Animation(.25f, regions);
		distracted.setPlayMode(Animation.PINGPONG);
		distracted.loop();
		distracted.setX(51);
		distracted.setY(23);
		aware = new Sprite(new TextureRegion(idleTex, 0, 0, idleTex.getWidth()/3, idleTex.getHeight()));
		aware.setX(51);
		aware.setY(23);
		//exclamation point!
		surprise = new Sprite(new Texture(Gdx.files.internal(DataDirs.ImageDir.path + "surprise.png")));
		surprise.setX(51);
		surprise.setY(50);
		
		Texture awareBar = new Texture(Gdx.files.internal(DataDirs.ImageDir.path + "awareness.png"));
		bar = new Sprite(new TextureRegion(awareBar, 0, 0, awareBar.getWidth(), awareBar.getHeight()/2));
		bar.setX(46);
		bar.setY(46);
		fill = new Sprite(new TextureRegion(awareBar, 0, awareBar.getHeight()/2, awareBar.getWidth(), awareBar.getHeight()/2));
		fill.setX(bar.getX());
		fill.setY(bar.getY());
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
		awareness = (float)(Math.random()*(AWARENESS_MAX - AWARENESS_MIN));
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
	 * @param delta 
	 */
	public void update(float delta)
	{
		//when looking down, update until look up starts
		if (awareness < 0)
		{
			timeSinceLookDown += delta;
			distracted.update(delta);
			
			if (timeSinceLookDown > timeUntilLookUp)
			{
				lookUp();
			}
		}
		//if he's done looking around, look back down at the computer
		else if (awareness >= AWARENESS_END)
		{
			lookDown();
		}
		//update his awareness
		else
		{
			awareness += AWARENESS_RATE*delta;
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
	 * @param batch
	 */
	public void draw(SpriteBatch batch)
	{
		if (awareness >= 0)
			aware.draw(batch);
		//makes it look like he's fiddling with his computer when he's not aware
		else
			distracted.draw(batch);
		if (surprised)
		{
			surprise.draw(batch);
		}
		
		//draw a little awareness meter above his head when he's looking around
		if (awareness > 0 && !surprised)
		{
			fill.setSize((float)((Math.min(AWARENESS_NEEDED, awareness)/AWARENESS_NEEDED)*20), 4);
			fill.draw(batch);
			bar.draw(batch);
		}
	}
}
