package sugdk.graphics;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import sugdk.core.GameFrame;
import sugdk.graphics.transitions.IrisIn;
import sugdk.graphics.transitions.IrisOut;
import sugdk.graphics.transitions.Transition;
import sugdk.scenes.Scene;
import sugdk.scenes.SceneManagerListener;

/**
 * ContentPanel
 * @author nhydock
 * @description
 *	Content Panel holds all the rendering code for drawing the 
 *	graphics into a graphics object.  It also handles stretching
 *	the game's resolution into the window's resolution and fade
 *	transitioning.
 */
public class RenderManager implements SceneManagerListener{

	/**
	 * Singleton instance of the ContentPanel
	 * made to be protected so then child ContentPanels can override it
	 */
	protected static RenderManager instance;
	
	/**
	 * Gets an instance of a content panel
	 */
	public static RenderManager getInstance() {
		if (instance == null)
			instance = new RenderManager();
		return instance;
	}
	
	/**
	 * default clear color for the buffer
	 */
	private static final Color DEFAULT_CLEAR_COLOR = Color.BLACK;

	/**
	 * NES Native resolution
	 */
	public static final int DEFAULT_INTERNAL_RES_W = 256;
	public static final int DEFAULT_INTERNAL_RES_H = 224;
	
	/**
	 * Represents a buffer to draw to
	 */
	private BufferedImage dbImage;
	
	/**
	 * graphics context of the buffer
	 */
	private Graphics dbg;
	
	/**
	 * color the background clears to
	 */
	private Color clearColor;
	
	/**
	 * internal resolution of the buffer being drawn to
	 */
	private int[] internal_res = {1, 1};
	
	private Transition trans;

	private GameFrame parent;
	
	/**
	 * Creates a new render manager
	 */
	private RenderManager()
	{
		clearColor = DEFAULT_CLEAR_COLOR;
		
		setInternalResolution(DEFAULT_INTERNAL_RES_W, DEFAULT_INTERNAL_RES_H);
	}
		
	/**
	 * Sets the parent frame that the renderer belongs to
	 * @param p
	 */
	public void setParent(GameFrame p)
	{
		this.parent = p;
	}
	
	/**
	 * Change the internal resolution of the content panel
	 * *This method can only be called by the parent game frame and should be done at game initialization*
	 * @param width
	 * @param height
	 */
	public void setInternalResolution(int width, int height)
	{
		dbImage = null;
		dbg = null;
		internal_res[0] = width;
		internal_res[1] = height;
		dbImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		dbg = dbImage.getGraphics();
	}
	
	/**
	 * @return the current internal width of the buffer
	 */
	public int getInternalWidth()
	{
		return internal_res[0];
	}
	
	/**
	 * @return the current internal height of the buffer
	 */
	public int getInternalHeight()
	{
		return internal_res[1];
	}
	
	/**
	 * @return the current internal resolution of the buffer
	 */
	public int[] getInternalRes()
	{
		return internal_res;
	}
	
	/**
	 * Sets the color that the panel's buffer clears to
	 * @param c
	 */
	public void setClearColor(Color c)
	{
		if (c != null)
			clearColor = c;
		else
			clearColor = DEFAULT_CLEAR_COLOR;
		dbg.setColor(clearColor);
	}
	
	/**
	 * Tells the panel to show the transition animation
	 * 	false = transition to black
	 * 	true = transition to next scene
	 */
	public void evokeTransition(boolean t)
	{
		try {
			Scene currentScene = parent.getEngine().getSceneManager().getCurrentScene();
			if (t)
				trans = (Transition) currentScene.getTransIn().newInstance();
			else
				trans = (Transition) currentScene.getTransOut().newInstance();
			trans.setTime(500);
			trans.setBuffer(getScreenCopy());
			parent.pauseGame(-1);
		} catch (InstantiationException e) {
			trans = null;
			if (t)
				evokeTransition(false);
			else
				parent.pauseGame(-1);
		}
		catch (Exception e) {
			trans = null;
			parent.pauseGame(0);
		}
	}
	
	/**
	 * Gets a copy of the game screen in its current state
	 * @return	a buffered image of the game screen scaled up to the frame size
	 */
	public BufferedImage getScreenCopy()
	{
		BufferedImage b = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = b.getGraphics();
		g.drawImage(dbImage, 0, 0, parent.getWidth(), parent.getHeight(), null);
		g.dispose();
		return b;
	}
	
	/**
	 * Updates the buffer
	 */
	public void render()
	{
		Scene currentScene = parent.getEngine().getSceneManager().getCurrentScene();
		
		//use current scene's clear color if it exists
		if (currentScene != null)
			if (currentScene.getDisplay() != null)
				setClearColor(currentScene.getDisplay().getClearColor());
		
		//clear the buffer
		dbg.fillRect(0, 0, dbImage.getWidth(), dbImage.getHeight());
		
		////draw the current scene
		if (currentScene != null)
			currentScene.render(dbg);
	}
	
	/**
	 * Paints the buffer to the panel
	 */
	public void paint(Graphics g)
	{
		render();
		if (trans != null)
		{
			trans.paint(g);
			if (trans.isDone())
				if (trans.getClass() == parent.getEngine().getSceneManager().getCurrentScene().getTransIn())
					evokeTransition(false);
				else
				{
					trans = null;
					parent.pauseGame(0);
					return;
				}
		}		
		else
			g.drawImage(dbImage, 0, 0, parent.getWidth(), parent.getHeight(), null);
	}

	/**
	 * @return if the content panel is currently showing a transition animation
	 */
	public boolean isTransitioning() {
		if (trans != null)
			return trans.isDone();
		return false;
	}

	/**
	 * On Scene change, the transition is evoked
	 */
	@Override
	public void onSceneChange() {
		evokeTransition(true);
	}

	/**
	 * Nothing is performed on scene being added
	 */
	@Override
	public void onSceneAdd() {}

	/**
	 * Sets the full scale factor
	 * @param d percentage of scale
	 */
	public void setScale(double d) {
		parent.setSize((int)(internal_res[0]*d/100.0), (int)(internal_res[1]*d/100.0));
	}	
}
