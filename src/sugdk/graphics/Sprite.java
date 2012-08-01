package sugdk.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import sugdk.engine.GameEngine;


/**
 * Sprite
 * @author nhydock
 * @name
 *	Main class used for drawing things to screen
 *	It's like a glorified BufferedImage mixed with Component
 */
public class Sprite{
	
	/**
	 * This is the viewport that sprites will be rendering to
	 * This should be set as soon as you create one so then if you are
	 * positioning by percentages, Sprite will automatically handle it for you
	 */
	private static RenderManager viewport = null;
	
	/**
	 * Sets the rendering viewport for dimensional references
	 * @param c the new viewport to reference
	 */
	public static void setPanel(RenderManager c)
	{
		viewport = c;
	}
	
	/**
	 * provides a cache of buffered images so then when a sprite is made, if the image has already been loaded before it adds it
	 */
	private static HashMap<String, BufferedImage> TEXTURECACHE = new HashMap<String, BufferedImage>();
	
	/**
	 * Clears all buffered images loaded from the cache
	 */
	public static void clearCache()
	{
		TEXTURECACHE.clear();
	}
	
	/**
	 * original image data
	 */
	protected BufferedImage image;
	/**
	 * image that is drawn to screen
	 */
	protected BufferedImage subimage;
	
	/**
	 * width of the buffered image
	 */
	protected double width  = 1;
	/**
	 * height of the buffered image
	 */
	protected double height = 1;
	/**
	 * x draw position on screen
	 */
	protected double x = 0;
	/**
	 * y draw position on screen
	 */
	protected double y = 0;
	/**
	 * the angle at which the image should draw
	 */
	protected double angle = 0;
	/**
	 * alignment anchor of the image
	 */
	protected int alignment;
	
	
	/**
	 * rectangle cropping for frames
	 */
	protected int[] rect;
	/**
	 * further cropping for what displays on screen
	 */
	protected double[] crop;
	/**
	 * bounds of the image after processing, used in point collision detection
	 */
	protected Rectangle bounds = new Rectangle(0, 0, 1, 1);
	
	protected double scaleW;
	protected double scaleH;
	/**
	 * number of horizontal frames
	 */
	protected int xFrames;	
	/**
	 * number of vertical frames
	 */
	protected int yFrames;	
	/**
	 * the currently selected x frame
	 */
	protected int currentXFrame;
	/**
	 * the currently selected y frame
	 */
	protected int currentYFrame;
	
	/**
	 * filepath to the image
	 */
	protected String path;
	/**
	 * filename of the image
	 */
	protected String name;
	
	/**
	 * graphics transformation matrix
	 */
	AffineTransform at;
	/**
	 * tell it to bilinear filter from scaling or not
	 */
	boolean filtered;
	
	/**
	 * Load the sprite
	 * @param s			name of the file
	 */
	public Sprite(String s)
	{
		this(s, 1, 1, true);
	}
	
	
	/**
	 * Load the sprite
	 * @param s			name of the file
	 * @param caching	load from/into cache
	 */
	public Sprite(String s, boolean caching)
	{
		this(s, 1, 1, caching);
	}
	
	/**
	 * Load the sprite
	 * @param s			name of the file
	 * @param xFrames	x frames of the sheet
	 * @param yFrames	y frames of the sheet
	 */
	public Sprite(String s, int xFrames, int yFrames)
	{
		this(s, xFrames, yFrames, true);
	}
	
	/**
	 * Load the sprite
	 * @param s			name of the file
	 * @param xFrames	x frames of the sheet
	 * @param yFrames	y frames of the sheet
	 * @param caching	load from/into cache
	 */
	public Sprite(String s, int xFrames, int yFrames, boolean caching)
	{
		rect = new int[4];
		crop = new double[4];
		if (s != null)
		{
			path = s;
			name = path.substring(path.lastIndexOf('/')+1);
			if (caching)
			{
				if (TEXTURECACHE.containsKey(s))
				{
					image = TEXTURECACHE.get(s);
				}
				else
				{
					try
					{
						image = ImageIO.read((GameEngine.isRscLoading())?getClass().getResourceAsStream("data/"+s):new FileInputStream("data/" + s));
					}
					catch (IOException e) {
						System.err.println("can not read or find: data/" + s);
					}
					TEXTURECACHE.put(s, image);
				}
			}
			else
				try
				{
					image = ImageIO.read((GameEngine.isRscLoading())?getClass().getResourceAsStream("data/"+s):new FileInputStream("data/" + s));
				}
				catch (IOException e) {
					System.err.println("can not read or find: data/" + s);
				}				
		}
		if (image != null)
		{
			width = image.getWidth();
			height = image.getHeight();
			this.xFrames = xFrames;
			this.yFrames = yFrames;
			crop = new double[]{0, 0, 1, 1};
			currentXFrame = 0;
			currentYFrame = 0;
			setFrame(1, 1);
			at = new AffineTransform();
		} 
	}
	
	/**
	 * Set the x coordinate
	 * @param i
	 */
	public void setX(double i)
	{
		//assume position on screen is a percentage if between 0 and 1 inclusive
		if (i >= 0 && i <= 1)
			i *= viewport.getInternalWidth();
		x = i;
	}

	/**
	 * Retrieve the x coordinate
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * Set the y coordinate
	 * @param i
	 */
	public void setY(double i)
	{
		//assume position on screen is a percentage if between 0 and 1 inclusive
		if (i >= 0 && i <= 1)
			i *= viewport.getInternalHeight();
		y = i;
	}
	
	/**
	 * Retrieve the y coordinate
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Adds x and y to current position
	 * @param x
	 * @param y
	 */
	public void slide(double x, double y)
	{
		this.x += x;
		this.y += y;
	}
	
	/**
	 * Retrieve the width of the image
	 */
	public double getWidth()
	{
		return rect[2];
	}
	
	/**
	 * Retrieve the height of the image
	 */
	public double getHeight()
	{
		return rect[3];
	}
	
	/**
	 * @return the width of the image after scaling
	 */
	public double getScaledWidth()
	{
		return scaleW;
	}
	
	/**
	 * @return the height of the image after scaling
	 */
	public double getScaledHeight()
	{
		return scaleH;
	}
	
	/**
	 * Retrieves the image in case of requiring more complex rendering
	 * than basic drawing to screen
	 * @return
	 */
	public BufferedImage getImage()
	{
		return image;
	}
	
	/**
	 * Sets the frame of animation
	 * @param x
	 * @param y
	 */
	public void setFrame(int x, int y)
	{
		if (x == currentXFrame && y == currentYFrame)
			return;
		
		//if x is -1 then show all the x frames
		if (x == -1)
		{	
			rect[0] = 0;
			rect[2] = (int)width;
			scaleW = (int)width;
		}
		else if (x >= 1 && x <= xFrames)
		{
			rect[0] = (int)(((x-1)/(double)xFrames)*width);
			rect[2] = (int)width/xFrames;
			scaleW = rect[2];	
		}
		
		//if y is -1 show all the y frames
		if (y == -1)
		{
			rect[1] = 0;
			rect[3] = (int)height;
			scaleH = (int)height;
		}
		else if (y >= 1 && y <= yFrames)
		{
			rect[1] = (int)(((y-1)/(double)yFrames)*height);
			rect[3] = (int)height/yFrames;
			scaleH = rect[3];	
		}
		
		currentXFrame = x;
		currentYFrame = y;
		
		updateFrame();
	}
	
	/**
	 * Gets the current frame of animation
	 * @return
	 */
	public int[] getFrame()
	{
		return new int[]{currentXFrame, currentYFrame};
	}
	
	/**
	 * Crop the image a bit
	 * values are percentages
	 */
	public void trim(double x, double y, double width, double height)
	{
		crop[0] = x;
		crop[1] = y;
		crop[2] = width;
		crop[3] = height;
	}
	
	/**
	 * Scales the width and height of the image by percentage
	 */
	public void scale(double w, double h)
	{
		scale(w, h, false);
	}
	
	/**
	 * Scales the width and height of the image by percentage
	 * @param filter	bilinear filter or not
	 */
	public void scale(double w, double h, boolean filter)
	{
		w *= rect[2];
		h *= rect[3];
		scale((int)w, (int)h, filter);
	}

	/**
	 * Scales the width and height of the image to the specified pixel size
	 * @param w
	 * @param h
	 */
	public void scale(int w, int h)
	{
		scale(w, h, false);
	}
	
	/**
	 * Scales the width and height of the image to the specified pixel size
	 * @param w
	 * @param h
	 */
	public void scale(int w, int h, boolean filter)
	{
		scaleW = w;
		scaleH = h;
		filtered = filtered;
	}
	
	/**
	 * Sets the angle of the image
	 * @param angle
	 */
	public void rotate(double i)
	{
		angle = i;
	}
	
	/**
	 * Determines if the point is within the bounds of the image
	 * Should only be called using mouse input.
	 * @param i x position of the point
	 * @param j y position of the point
	 * @return if the point is within the bounds of the image on screen 
	 */
	public boolean collides(int i, int j)
	{
		int drawX = (int)x;
		int drawY = (int)y;
		int finalWidth = (int)(drawX+scaleW*(crop[2]-crop[0]));
		int finalHeight = (int)(drawY+scaleH*(crop[3]-crop[1]));
		bounds.setBounds(drawX, drawY, finalWidth, finalHeight);
		
		return bounds.contains(i, j);
	}
	
	/**
	 * Updates the subimage frame that is to be drawn to screen
	 */
	private void updateFrame()
	{
		int sourceX = (int)(rect[0]+crop[0]*rect[2]);
		int sourceY = (int)(rect[1]+crop[1]*rect[3]);
		double sourceWidth = (rect[2]*(crop[2]-crop[0]));
		double sourceHeight = (rect[3]*(crop[3]-crop[1]));
		
		// crop the frame
		subimage = image.getSubimage(sourceX, sourceY, (int)sourceWidth, (int)sourceHeight);
	}
	
	/**
	 * Paint the sprite to screen
	 * @param g
	 */
	public void paint(Graphics g)
	{
		//ignore if no image or if the graphics passed is null or if the cropping area leaves a surface too small to draw
		if (image != null && g != null && crop[3]-crop[1] > 0 && crop[2]-crop[0] > 0)
		{
			//temp variables
			int drawX = (int)x;
			int drawY = (int)y;
			int finalWidth = (int)(scaleW*(crop[2]-crop[0]));
			int finalHeight = (int)(scaleH*(crop[3]-crop[1]));
			
			int offset = 0;
			//center alignment
			if (alignment == 1)
				offset = finalWidth/2;
			//right aligned
			else if (alignment == 2)
				offset = finalWidth;

            // rotation
            at.setToRotation(Math.toRadians(angle), subimage.getWidth() / 2.0, subimage.getHeight() / 2.0);
            
            // scale the image
            at.scale(finalWidth/(double)subimage.getWidth(), finalHeight/(double)subimage.getHeight());
            
            // applies the transformation to the cropped image
            AffineTransformOp op = new AffineTransformOp(at, (filtered)?AffineTransformOp.TYPE_BILINEAR:AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            
            //draw the image to the graphics buffer
			((Graphics2D) g).drawImage(subimage, op, drawX-offset, drawY);
			
			op = null;
		}
	}

	/**
	 * @return	the file name of the image
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return	path to the image file
	 */
	public String getPath() {
		return path;
	}

	public double getAngle() {
		return angle;
	}
}
