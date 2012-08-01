package sugdk.graphics.transitions;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import sugdk.core.GameFrame;

/**
 * Transition
 * @author nhydock
 *
 *	Abstract class for building different scene changing transition effects
 *	Depending on how you want to do things, you can alter a saved buffer,
 *	draw on top of things without effecting the buffer to do effects like repeated
 *	flashing, or even play a sound effect at different steps in the transition.
 *
 *	All you need to do is initialize a transition, copy the screen buffer,
 *	set the length of the transition.  Only things you'll need to override
 *	are step and paint and you'll be good.
 */
public abstract class Transition{

	/**
	 * amount of time it should take to transition
	 */
	protected long length;
	/**
	 * Time in nanoseconds of when the transition started
	 */
	protected long start;
	/**
	 * current time in the transition
	 */
	protected long currTime;
	/**
	 * Percentage of how far into the transition the animation is
	 */
	protected double timePercentage;
	
	//dimensions on the screen that the Transition will take up
	protected static final int WIDTH = GameFrame.WIDTH;
	protected static final int HEIGHT = GameFrame.HEIGHT;
	
	/**
	 * the buffer that will display transition to the screen on
	 */
	protected BufferedImage buffer;
	
	
	/**
	 * Sets the buffer to a BufferedImage passed in
	 * Usually you want to pass in a copy of the buffer you want instead of the actual buffer
	 * @param b	the buffer to use for painting
	 */
	public void setBuffer(BufferedImage b)
	{
		buffer = b;
	}
	
	/**
	 * Sets the amount of time that the transition should take
	 * @param t	time (in milliseconds)
	 */
	public void setTime(int t)
	{
		reset();
		length = t*GameFrame.NANO_PER_MSEC;	//milliseconds to nanoseconds
	}
	
	/**
	 * Resets the transition to its starting point
	 */
	public void reset()
	{
		currTime = 0;
		start = System.nanoTime();
	}
	
	/**
	 * Steps through the transitions animation
	 * This should always be called at the end of the paint method
	 * It gives a addition time lax of the GameRunner's FPS to ensure that the entire transition displays
	 */
	protected void step()
	{
		currTime = System.nanoTime()-start;
		timePercentage = currTime/(double)length;
	}
	
	/**
	 * Tells if the transition is done being drawn
	 */
	public boolean isDone(){
		return currTime >= length;
	}
	
	/**
	 * Paints the effects of the 
	 * @param g	graphics component of the frame to draw to
	 */
	abstract public void paint(Graphics g);
}
