

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * ContentPanel
 * @author nhydock
 *
 *	Main rendering panel for all the graphics.  Performs buffer manipulation and scaling
 */
public class ContentPanel extends JPanel{

	private static final Color DEFAULT_CLEAR_COLOR = Color.BLACK;
	//NES Native resolution
	public static int INTERNAL_RES_W;
	public static int INTERNAL_RES_H;
	
	private Image dbImage;				//image to draw to
	private Graphics dbg;				//graphics context of the component
	private Engine engine;
	
	private Color clearColor;			//color the background clears to
	
	private Sprite bg;					//background image

	//There's only 1 font used in the game
	public static SFont font;
	
	MP3 bgm = new MP3("bgm.mp3");
	
	public ContentPanel()
	{
		setVisible(true);
		
		engine = Engine.getInstance();
		clearColor = DEFAULT_CLEAR_COLOR;

		bg = new Sprite("bg.png");
		font = SFont.loadFont("default", 7.0f);
		
		INTERNAL_RES_W = (int)bg.getWidth();
		INTERNAL_RES_H = (int)bg.getHeight();
		
		bgm.play();
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
	}

	
	/**
	 * Updates the buffer
	 */
	public void render()
	{

		//create the buffer if it hasn't been yet
		if (dbImage == null)
		{
			dbImage = createImage(INTERNAL_RES_W, INTERNAL_RES_H);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			}
			else
				dbg = dbImage.getGraphics();
		}
		dbg = dbImage.getGraphics();
		
		//clear the buffer
		dbg.setColor(clearColor);
		dbg.fillRect(0, 0, INTERNAL_RES_W, INTERNAL_RES_H);
		
		
		//paint background
		bg.paint(dbg);
		
		//paint characters
		engine.you.paint(dbg);
		engine.foe.paint(dbg);
		
		//paint hud
		font.drawString(dbg, "TIME: " + engine.you.getTime(), 2, 19, 0, Color.GRAY, bg);
		font.drawString(dbg, "FL Dranked", 2, 19, 2, Color.GRAY, bg);
		font.drawString(dbg, engine.you.getScore(), 2, 25, 2, Color.GRAY, bg);
		font.drawString(dbg, "TIME: " + engine.you.getTime(), 2, 18, bg);
		font.drawString(dbg, "FL Dranked", 2, 18, 2, bg);
		font.drawString(dbg, engine.you.getScore(), 2, 24, 2, bg);
		font.drawString(dbg, (engine.gameover)?"YOU HAVE BEEN CAUGHT!":"PRESS SPACE TO CHUG OJ", 0, 95, 1, bg);
		font.drawString(dbg, (engine.gameover)?"PRESS ENTER TO RESET GAME":"", 0, 8, 1, Color.BLACK, bg);

	}
	
	/**
	 * Paints the buffer to the panel
	 */
	public void paint()
	{
		Graphics graphics = getGraphics();
		render();
		
		if (dbImage != null)
			graphics.drawImage(dbImage, 0, 0, getWidth(), getHeight(), null);
	}	
}
