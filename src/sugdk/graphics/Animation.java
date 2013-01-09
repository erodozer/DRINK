package sugdk.graphics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Animation
 * <p/>
 * Not being satisfied with Gdx's 2D animation system, this extends off of it by giving
 * animations their own timers that'll allow them to cycle through their animations through
 * an update system.  It's also easier to get the current frame of the animation.
 * @author nhydock
 *
 */
public class Animation
{
	/**
	 * Plays once in forward direction
	 */
	public static final int NORMAL = 0;
	/**
	 * Plays once in backward direction
	 */
	public static final int REVERSE = 1;
	/**
	 * Plays once forward then in reverse
	 */
	public static final int PINGPONG = 2;
	
	boolean loop;		//whether or not the animation should loop
	int loopcounter;	//how many times the animation should loop
	
	//array of all the frames of the animation
	Array<TextureRegion> regions;
	
	float stepLength;	//how much time must pass before going to the next frame
	float timer;		//current time in the animation
	
	int currentFrame;	//current frame of animation
	int frames;			//number of frames to the animation
	
	//way it should play
	int playMode;
	
	//animation controllers
	private boolean reverse;
	private boolean done;
	
	//location on screen of the animation
	int x, y;
	
	/**
	 * Creates an animation
	 * @param stepLength - time between steps
	 * @param regions - texture frames to use in the animation
	 */
	public Animation(float stepLength, Array<TextureRegion> regions)
	{
		this.regions = regions;
		
		this.stepLength = stepLength;
		this.timer = 0;
		this.currentFrame = 0;
		this.frames = regions.size;
		this.reverse = false;
		this.playMode = Animation.NORMAL;
		this.loop = false;
		this.loopcounter = 0;
	}
	
	/**
	 * @param stepLength
	 * @param regionList
	 */
	public Animation(float stepLength, TextureRegion[] regionList)
	{
		this(stepLength, new Array<TextureRegion>(regionList));
	}
	
	/**
	 * Updates the animation timer and current frame
	 * @param delta - seconds passed since previous update
	 */
	public void update(float delta)
	{
		if (!done)
		{
			this.timer += delta;
			if (this.timer > this.stepLength)
			{
				currentFrame += (reverse)?-1:1;
				
				if (isAtEnd())
				{
					switch (loopcounter)
					{
						//done when loop counter is at 0
						case 0:
						{
							done = true;
							break;
						}
						//infinite
						case -1:
						{
							if (playMode == PINGPONG)
								reverse = !reverse;
							break;
						}
						//decrement loop counter
						default:
						{
							//decrement on PINGPONG only if it has finished going in reverse
							if (playMode != PINGPONG)
							{
								loopcounter -= 1;
							}
							else
							{
								if (reverse)
								{
									loopcounter -= 1;	
								}
								reverse = !reverse;
							}
							break;
						}
					}
				}
				this.timer = 0;
			}
		}
	}
	
	/**
	 * Detects if the animation is at an end of a cycle
	 * @return
	 */
	private boolean isAtEnd()
	{
		if (reverse)
			return (currentFrame <= 0);
		else
			return (currentFrame >= frames-1);
	}
	
	/**
	 * Resets the animation
	 * NOTE: Does not affect current looping properties
	 */
	public void reset()
	{
		done = false;
		timer = 0;
		if (playMode != REVERSE)
		{
			currentFrame = 0;
		}
		else
		{
			currentFrame = frames-1;
		}
	}
	
	/**
	 * Tells animation to loop infinitely
	 */
	public void loop()
	{
		loop(true, -1);
	}
	
	/**
	 * Tells animation whether or not it should loop infinitely
	 * @param doLoop
	 */
	public void loop(boolean doLoop)
	{
		loop(doLoop, -1);
	}
	
	/**
	 * Tells animation whether or not it should loop, and how many times it should look if it should
	 * @param doLoop
	 * @param count
	 */
	public void loop(boolean doLoop, int count)
	{
		loop = doLoop;
		loopcounter = count;
	}
	
	/**
	 * Changes how the animation should play
	 * @param playMode
	 */
	public void setPlayMode(int playMode)
	{
		this.playMode = playMode;
		if (playMode == Animation.REVERSE)
		{
			reverse = true;
		}
		else if (playMode == Animation.NORMAL)
		{
			reverse = false;
		}
		else
		{
			//Ping Pong should not change the direction it's going	
		}
	}
	
	/**
	 * @return the texture region of the current frame
	 */
	public TextureRegion getFrame()
	{
		return regions.get(currentFrame);
	}
	
	/**
	 * @param x - X position of the animation
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	/**
	 * @param y - Y position of the animation
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/**
	 * @return the X position of the animation
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * @return the Y position of the animation
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * @param i - frame index
	 * @return the texture region of the specified frame
	 */
	public TextureRegion getFrame(int i)
	{
		return regions.get(i);
	}
	
	/**
	 * Draws the animation with a sprite batch to the display
	 * @param sb - sprite batch
	 */
	public void draw(SpriteBatch sb)
	{
		sb.draw(regions.get(currentFrame), x, y);
	}
	
	/**
	 * Draws a specific frame of the animation to the display
	 * @param sb - sprite batch
	 * @param frame
	 */
	public void draw(SpriteBatch sb, int frame)
	{
		sb.draw(regions.get(frame), x, y);
	}
}
