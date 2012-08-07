package sugdk.scenes;

import java.awt.Graphics;

import sugdk.graphics.transitions.*;

/**
 * Scene.java
 * @author nhydock
 *
 *	Scene class to encapsulate logic and display
 */

abstract public class Scene{
	
	/**
	 * logic system of the scene
	 */
    protected GameSystem system;
    /**
     * display system of the scene
     */
    protected SceneDisplay display;
    
    /**
     * Effect called when scene transitions in
     * By default we fade the screen from black to the new view
     */
    protected final Class transIn = FadeOut.class;
    
    /**
     * Effect called when the previous scene transitions in
     * By default we fade the screen to black
     */
    protected final Class transOut = FadeIn.class;
    
	/**
	 * Starts the scene
	 */
	abstract public void start();
	
	/**
	 * Stops the scene
	 */
	abstract public void stop();
	
	/**
	 * Mathematical computation run portion
	 */
	public void update()
	{
	    system.update();
	    
	    //forces system to be updated before the display can be updated
	    //this ensures that the display is always showing feedback of the most recent events
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }	    
        
        display.update();
	}
	
	/**
	 * Key pressed is the only kind of input acceptable
	 * @param keyCode the key code of the key press
	 */
	public void keyPressed(int keyCode)
	{
	    system.keyPressed(keyCode);
	}
	
	/**
	 * Main rendering method call for the scene
	 * @param g The graphics buffer
	 */
	public void render(Graphics g)
	{
		if (display != null)
			display.paint(g);
	}
	
	/*
	 * Systems and displays should not be set at any time other than
	 * start, but they can be fetched
	 */
	
	/**
	 * Gets the logic system
	 * @return system
	 */
	public GameSystem getSystem()
	{
	    return system;
	}
	
	/**
	 * Gets the graphical display
	 * @return display
	 */
	public SceneDisplay getDisplay()
	{
	    return display;
	}
	
	/**
	 * Gets the scene's transition in effect
	 * @return transIn
	 */
	public Class getTransIn()
	{
		return transIn;
	}
	
	/**
	 * Gets the scene's transition out effect
	 * @return transOut
	 */
	public Class getTransOut()
	{
		return transOut;
	}
}
