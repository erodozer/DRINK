package sugdk.graphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author nhydock
 *
 */
public class SFont extends BitmapFont
{
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int CENTER = 2;
	
	int alignment = LEFT;

	/**
	 * @param alignment
	 */
	public void setAlignment(int alignment)
	{
		this.alignment = alignment;
	}
	
	/**
	 * @param batch
	 * @param str
	 * @param x
	 * @param y
	 * @return 
	 */
	@Override
	public TextBounds draw(SpriteBatch batch, CharSequence str, float x, float y)
	{
		TextBounds bounds = this.getBounds(str);
		
		if (alignment == LEFT)
		{
			//do nothing
		}
		else if (alignment == RIGHT)
		{
			x -= bounds.width;
		}
		else if (alignment == CENTER)
		{
			x -= bounds.width/2;
		}
		super.draw(batch, str, x, y);
		
		return null;
	}
	
}
