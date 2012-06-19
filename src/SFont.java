

/**
 * SFont.java
 * @author nhydock
 *
 *  SFont is an extended functionality wrapper to drawing strings in java's Graphics class.
 *  Too many times have I had to do silly math to right align things or get font to look like
 *  it's being rendered inside of a "window", so this was created to help handle all that
 *  for me.
 *  
 *  Some of the key features:
 *  	Setting font alignment
 *  	Anchoring text so coordinates are in relation to the bounds of sprite objects
 *  	Font caching
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class SFont {

	/*
	 * Alignment values
	 */
	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;
	
	private static final Color DEFAULT_COLOR = Color.WHITE;
	
	private static HashMap<String, SFont> cache = new HashMap<String, SFont>();
	
	Font f;					//font to use for rendering
	FontMetrics fm;			//font's metrics
	
	/**
	 * Loads a SFont, will load from cache if the font has already been loaded previously
	 * @param fontName
	 * @return
	 */
	public static SFont loadFont(String fontName)
	{
		return loadFont(fontName, 24.0f);
	}
	
	public static SFont loadFont(String fontName, float size)
	{
		SFont s;
		if (cache.containsKey(fontName))
			s = cache.get(fontName);
		else
		{
			s = new SFont(fontName, size);
			cache.put(fontName, s);
		}
		return s;
	}
	
	/**
	 * Formats a string into multiple lines following the wrap mode passed
	 * @param s		String to format
	 * @param wrap	wrapping mode - 0 = none, 1 = crop, 2 = wrap
	 * @return
	 */
	public String[] formatIntoLines(String s, int wrap, int width)
	{
		String[] processed;
		
		//no matter the wrapping mode, split lines by \n char
		processed = s.split("\n");
		
		//no reason to crop or wrap if the width limit is unlimited
		if (width < 0)
			return processed;
		
		//crop the lines
		if (wrap == 1)
		{
			for (int i = 0; i < processed.length; i++)
			{
				String line = processed[i].trim();
				int r = line.length();
				String cropped = line.substring(0, r);
				while (fm.stringWidth(cropped) > width && r > 1)
				{
					r--;
					cropped = line.substring(0, r);
				}
				//if the cropped string is smaller than the original string
				// add elipsis to visually show the string is cropped.
				if (r < line.length() && r > 1)
				{
					r -= 3;
					//crop it some more to make sure the elipsis don't go outside the edge
					while (fm.stringWidth(cropped+"...") > width && r > 1)
					{
						r--;
						cropped = line.substring(0, r);
					}
					cropped = cropped+ "...";
				}
				processed[i] = cropped;
			}
		}
		//wrap the lines
		else if (wrap == 2)
		{
			ArrayList<String> lines = new ArrayList<String>();
			for (int i = 0; i < processed.length; i++)
			{
				String line = processed[i].trim();
				int r = 1;
				int l = 0;
				String cropped = line.substring(0, 0);
				
				while (r < line.length())
				{
					while (fm.stringWidth(cropped) < width)
					{
						String search = line.substring(l);
						l = search.indexOf(" ");
						r += l;
						if (r > line.length())
							break;
						cropped = line.substring(0, r);
					}
					lines.add(line.substring(0, r-l));
					line = line.substring(l);
				}
			}
			processed = lines.toArray(new String[]{});
		}
		
		return processed;
	}

	/**
	 * Constructs a SFont	
	 * @param fontName		name of the font file
	 */
	private SFont(String fontName)
	{
		this(fontName, 24.0f);
	}
	
	/**
	 * Constructs a SFont	
	 * @param fontName		name of the font file
	 */
	private SFont(String fontName, float size)
	{
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, (Engine.isRscLoading)?getClass().getResourceAsStream("data/font/"+fontName+".ttf"):new FileInputStream(new File("data/font/"+fontName+".ttf"))).deriveFont(size);
			fm = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR).getGraphics().getFontMetrics(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Draws a string to the display
	 * @param g				Graphics buffer to draw to
	 * @param text			String to draw
	 * @param x				x position
	 * @param y				y position
	 * @param alignment		alignment of the font - 0 = left, 1 = center, 2 = right
	 * @param c				color of the font
	 */
	public void drawString(Graphics g, String text, int x, int y, int alignment, Color c)
	{
		//don't try drawing if the font doesn't exist
		if (f == null || text == null)
			return;
		
		//right aligned
		if (alignment == 2)
			x -= fm.stringWidth(text);
		//center aligned
		else if (alignment == 1)
			x -= fm.stringWidth(text)/2;
		//left aligned do nothing
		
		//save the current color of graphics
		Color c2 = g.getColor();
		Font fn = g.getFont();
		
		//change color to the font color, white by default
		if (c != null)
			g.setColor(c);
		else 
			g.setColor(DEFAULT_COLOR);
			
		g.setFont(f);
		g.drawString(text, x, y);		//draws the string to screen
		
		//make sure things reset to what they were before rendering the text
		g.setFont(fn);
		g.setColor(c2);
	}
	
	/**
	 * Draws a string to the display
	 * 	Alignment is left by default
	 * 	Color is white by default
	 * @param g				Graphics buffer to draw to
	 * @param text			String to draw
	 * @param x				x position
	 * @param y				y position
	 */
	public void drawString(Graphics g, String text, int x, int y)
	{
		drawString(g, text, x, y, 0, (Sprite)null);
	}
	
	/**
	 * Draws a string to the display
	 * 	Color is white by default
	 * @param g				Graphics buffer to draw to
	 * @param text			String to draw
	 * @param x				x position
	 * @param y				y position
	 * @param alignment		alignment of the font - 0 = left, 1 = center, 2 = right
	 */
	public void drawString(Graphics g, String text, int x, int y, int alignment)
	{
		drawString(g, text, x, y, alignment, (Color)null);
	}
	
	/**
	 * Draws a string to the display
	 * 	Alignment is left by default
	 * 	Color is white by default
	 * @param g				Graphics buffer to draw to
	 * @param text			String to draw
	 * @param x				x position
	 * @param y				y position
	 * @param anchor		Sprite to anchor relative position to
	 */
	public void drawString(Graphics g, String text, int x, int y, Sprite anchor)
	{
		drawString(g, text, x, y, 0, null, anchor);
	}
	
	/**
	 * Draws a string to the display
	 * 	Color is white by default
	 * @param g				Graphics buffer to draw to
	 * @param text			String to draw
	 * @param x				x position
	 * @param y				y position
	 * @param alignment		alignment of the font - 0 = left, 1 = center, 2 = right
	 * @param anchor		Sprite to anchor relative position to
	 */
	public void drawString(Graphics g, String text, int x, int y, int alignment, Sprite anchor)
	{
		drawString(g, text, x, y, alignment, null, anchor);
	}
	
	/**
	 * Draws a string to the display
	 * @param g				Graphics buffer to draw to
	 * @param text			String to draw
	 * @param x				x position
	 * @param y				y position
	 * @param alignment		alignment of the font - 0 = left, 1 = center, 2 = right
	 * @param c				color of the font
	 * @param anchor		Sprite to anchor relative position to
	 */
	public void drawString(Graphics g, String text, int x, int y, int alignment, Color c, Sprite anchor)
	{
		if (anchor != null)
		{
			//right align anchors to right side
			if (alignment == 2)
				x = (int)(anchor.getX() + anchor.getWidth() - x);
			//center align anchors to center of the window
			else if (alignment == 1)
				x = (int)(anchor.getX() + anchor.getWidth()/2 - x);
			else
				x = (int)(anchor.getX() + x);
				
			//windows have additional reserved thickness of 10 pixels
			y += anchor.getY();	
		}
		
		drawString(g, text, x, y, alignment, c);
	}

}
