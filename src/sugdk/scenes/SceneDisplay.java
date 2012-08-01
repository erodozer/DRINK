package sugdk.scenes;

import java.awt.Color;
import java.awt.Graphics;

import sugdk.graphics.SFont;
import sugdk.graphics.Sprite;

/**
 * @author nhydock
 * 
 * Display for a scene to render, mainly used for facading sprites for a scene to render
 */
public abstract class SceneDisplay extends Sprite
{

	/**
	 * The system the display is attached to
	 * Useful to know because your display will most likely be changing depending on what's happening in the scene
	 */
    protected GameSystem parent;
    
    /**
     * color the background of the scene clears to
     */
    protected Color clearColor;

    /**
     * The font used throughout the display to display text
     */
    protected SFont font;

    protected boolean refresh = false;
    
    /**
	 * position of which to drawn the arrow in the scene in relation to this HUD
	 */
    protected int[] arrowPosition = new int[2];
    								
    
    /**
     * Constructs a hud
     */
    public SceneDisplay()
    {
        super("");
    }
    
    /**
     * Sets the parent system of the hud so it knows what information to draw
     * @param p
     */
    public void setParent(GameSystem p)
    {
        parent = p;
    }
    
    /**
     * @return the parent system
     */
    public GameSystem getParent()
    {
    	return parent;
    }
    
    /**
     * Updates the display
     * Anything involving calculations about the display, but not pertaining specifically
     * to painting a component of the scene should be in here
     */
    abstract public void update();
    
    /**
     * Paints the display
     * This method is strictly for setting where graphics should be rendered and paints them to the display
     */
    abstract public void paint(Graphics g);
    
    /**
     * @return 
     *  the colour that the background of a scene will clear to
     *  only important if the display is the top most facade of the scene
     */
	public Color getClearColor() {
		return clearColor;
	}

	/**
	 * Just fetches the arrow's current position
	 * @return
	 */
	public int[] getArrowPosition()
	{
		return arrowPosition;
	}
	
	/**
	 * Most scenes have an arrow drawn to screen.
	 * This will control where the arrow should be drawn if it's
	 * dependent on a HUD's control/view
	 * @param index 
	 * @return	the position of where the arrow was updated to
	 */
	public int[] updateArrowPosition(int index)
	{
		return new int[]{-100, -100};
	}
}
