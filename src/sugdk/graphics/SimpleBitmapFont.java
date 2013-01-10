package sugdk.graphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * SimpleBitmapFont is a different kind of Bitmap font.  All it takes is a single Texture region
 * and it creates a monospaced, sprite-based font for drawing with.  It's useful for areas where a
 * full font is not necessary for rendering, such as drawing the score.  This way you could just have
 * an image that holds the numbers 0-9, tell the font that it can only render the characters 0-9,
 * and that's all the SimpleBitmapFont will be capable of doing.
 * @author nhydock
 *
 */
public class SimpleBitmapFont
{
	//all letters of the ASCII Dictionary
	private final String DEFAULT_DICT = "";
			
	SpriteSheet spriteSheet;
	byte[] letters;	//dictionary of letters capable of being drawn, only supports ASCII
	int xOffset;
	int yOffset;
	
	/**
	 * 
	 * @param fontImage - sprite area of the letters
	 * @param letters - all acceptable letters the font can render (can be represented in regex)
	 * @param rows - how many columns the letters take up
	 */
	public SimpleBitmapFont(TextureRegion fontImage, String letters, int rows)
	{
		//remove all letters that are not acceptable from the dictionary
		this.letters = new byte[128];

		//clear out letters
		for (int i = 0; i < this.letters.length; i++)
		{
			this.letters[i] = -1;
		}
		
		for (int i = 0; i < letters.length(); i++)
		{
			this.letters[letters.charAt(i)] = (byte)i;
		}
		spriteSheet = new SpriteSheet(fontImage, letters.length()/rows, rows);
		xOffset = spriteSheet.getFrameWidth();
		yOffset = spriteSheet.getFrameHeight();
	}
	
	/**
	 * Draws a string to screen using a SpriteBatch
	 * @param sb - sprite batch to use
	 * @param str - string to render
	 * @param x - x position in the SpriteBatch's viewport to render to
	 * @param y - y position in the SpriteBatch's viewport to render to
	 */
	public void draw(SpriteBatch sb, String str, int x, int y, HAlignment alignment)
	{
		int xShift = 0;
		//shift the string depending on alignment
		if (alignment == HAlignment.LEFT)
		{
			xShift = 0;
		}
		else if (alignment == HAlignment.RIGHT)
		{
			xShift = str.length()*xOffset;
		}
		else if (alignment == HAlignment.CENTER)
		{
			xShift = (str.length()*xOffset) >> 1;
		}
		x -= xShift;
		
		for (int i = 0; i < str.length(); i++)
		{
			byte letter = this.letters[str.charAt(i)];
			//System.out.println((int)letter);
			if (letter != -1)
			{
				sb.draw(spriteSheet.getFrame(letter, false), x, y);
			}
			x += xOffset;
		}
	}
}
