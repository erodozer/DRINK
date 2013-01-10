package sugdk.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Creates a texture region grid that can be used as a spritesheet for animations
 * @author nhydock
 *
 */
public class SpriteSheet
{

	TextureRegion[][] rows;
	TextureRegion[][] columns;
	
	int xFrames;
	int yFrames;
	private int[] frameSize;
	
	/**
	 * Creates a spritesheet from a texture
	 * @param tex
	 * @param xFrames
	 * @param yFrames
	 */
	public SpriteSheet(Texture tex, int xFrames, int yFrames)
	{
		this(new TextureRegion(tex), xFrames, yFrames);
	}
	
	/**
	 * Creates a spritesheet from a texture region
	 * @param tex - texture file
	 * @param xFrames - number of horizontal frames in the spritesheet
	 * @param yFrames - number of vertical frames in the spritesheet
	 */
	public SpriteSheet(TextureRegion tex, int xFrames, int yFrames)
	{
		this.xFrames = xFrames;
		this.yFrames = yFrames;
		rows = new TextureRegion[yFrames][xFrames];
		columns = new TextureRegion[xFrames][yFrames];
		frameSize = new int[]{tex.getRegionWidth()/xFrames, tex.getRegionHeight()/yFrames};
		for (int y = 0; y < yFrames; y++)
		{
			for (int x = 0; x < xFrames; x++)
			{
				TextureRegion r = new TextureRegion(tex, frameSize[0] * x, frameSize[1] * y, frameSize[0], frameSize[1]);
				rows[y][x] = r;
				columns[x][y] = r;
			}
		}
	}
	
	/**
	 * Pick a row of the spritesheet
	 * @param row
	 * @return all the texture regions of the specified row
	 */
	public TextureRegion[] getRow(int row)
	{
		return rows[row];
	}
	
	/**
	 * Pick a column of the spritesheet
	 * @param col
	 * @return all the texture regions of the specified column
	 */
	public TextureRegion[] getColumn(int col)
	{
		return columns[col];
	}
	
	/**
	 * Pick a single frame
	 * @param x
	 * @param y
	 * @return cell at x, y in the spritesheet
	 */
	public TextureRegion getFrame(int x, int y)
	{
		return columns[x][y];
	}
	
	/**
	 * Pick a single frame as if the Spritesheet is along one long row/col
	 * @param frame - frame index in the spritesheet (0...n-1)
	 * @param indexDirection - true for down rows then next column, false for across columns then next row
	 * @return a region of the spritesheet
	 */
	public TextureRegion getFrame(int frame, boolean indexDirection)
	{
		if (indexDirection)
		{
			return columns[frame/yFrames][frame%yFrames];
		}
		else
		{
			return rows[frame/xFrames][frame%xFrames];
		}
	}
	
	public int getFrameWidth()
	{
		return frameSize[0];
	}
	
	public int getFrameHeight()
	{
		return frameSize[1];
	}
}
