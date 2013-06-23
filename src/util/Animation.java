package util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Animator class originally designed to be used with Artemis, but modified for generic use.
 * Works with UV coordinates to jump through a horizontal strip spritesheet.
 * 
 * @author nhydock
 */
public class Animation {
	/**
	 * Time it takes to change to the next frame
	 */
	public final float rate;
	/**
	 * Time since last frame change
	 */
	public float prev;
	/**
	 * Time until next frame change
	 */
	public float next;
	/**
	 * Tell if the animation loops or not
	 */
	public final boolean loop;
	/**
	 * Tell if the animation is done
	 */
	public boolean done;
	
	/**
	 * Current frame index of the animation
	 */
	public int index;
	/**
	 * Number of frames available in the animation
	 */
	public final int frameCount;
	
	private final float frameSize;
	
	/**
	 * Region coordinates
	 */
	private float u1, u2, v1, v2;
	private float s1, s2;
	
	/**
	 * Creates an animation from a texture
	 * @param spriteSheet
	 * @param frameCount - number of cells in the region to animate through
	 * @param rate - number of seconds it should take before jumping to the next frame
	 * @param loop - tell the animation to loop or not
	 */
	public Animation(Texture spriteSheet, int frameCount, final float rate, final boolean loop)
	{
		this(new TextureRegion(spriteSheet), frameCount, rate, loop);
	}
	
	/**
	 * Creates an animation from a texture region
	 * @param spriteSheet
	 * @param frameCount - number of cells in the region to animate through
	 * @param rate - number of seconds it should take before jumping to the next frame
	 * @param loop - tell the animation to loop or not
	 */
	public Animation(TextureRegion spriteSheet, int frameCount, final float rate, final boolean loop)
	{
		this.frameCount = frameCount;
		this.rate = this.next = rate;
		this.prev = 0;
		this.loop = loop;
		this.index = 0;
		this.done = false;
		
		this.u1 = 0;
		this.u2 = this.frameSize;
		this.v1 = 0;
		this.v2 = 1.0f;
		
		
		this.s1 = this.u1 = spriteSheet.getU();
		this.s2 = spriteSheet.getU2();
		this.v1 = spriteSheet.getV();
		this.v2 = spriteSheet.getV2();
		
		this.frameSize = (1.0f/frameCount)*(this.s2 - this.s1);
		this.u2 = this.u1 + this.frameSize;
		
	}
	
	/**
	 * Updates the timer of the animation
	 * @param delta - time passed since previous update (in seconds)
	 */
	public void update(float delta)
	{
		if (done)
			return;
		
		prev += delta;
		next -= delta;
		
		if (next <= 0)
			advance();
	}
	
	/**
	 * Advances the frame of the animation
	 */
	private void advance()
	{
		next = rate;
		prev = 0;
			
		if (index + 1 < this.frameCount)
		{
			index++;
			
			this.u1 += this.frameSize;
			this.u2 += this.frameSize;
		}
		else if (this.loop)
		{
			index = 0;
			this.u1 = this.s1;
			this.u2 = this.s1 + this.frameSize;
		}
		else
		{
			this.done = true;
		}
	}
	
	/**
	 * Takes a sprite and sets its UVs to be the animations
	 * Be sure to use a sprite whose texture region matches with the intended region animated
	 * @param sprite
	 */
	public void set(Sprite sprite)
	{
		sprite.setRegion(this.u1, this.v1, this.u2, this.v2);
	}
}
