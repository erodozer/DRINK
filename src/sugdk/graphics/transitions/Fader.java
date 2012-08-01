package sugdk.graphics.transitions;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Fader
 * @author nhydock
 *
 *	Fader is a special type of transition where it can take a grayscale image and slide
 *	up the white value, using it as a mask.  It's a very heavy task for operation because
 *	LookupOp generates a new BufferedImage every frame, so use this in as few places as you can
 */
public class Fader extends Transition {
	//first we need to manipulate it so greys will slide to white
	private static LookupTable lut;
	private static LookupOp luo ;
	
	private static LookupTable blt;
	private static LookupOp toBlack;
	
	static
	{
		byte[] r = new byte[256];
		byte[] b = new byte[256];
		byte[] g = new byte[256];
		byte[] a = new byte[256];
		byte[][] data;
		
		for (int i = 1; i < 256; i++)
		{
			r[i] = b[i] = g[i] = (byte)(i-1);
			a[i] = (byte)(i);
		}
		data = new byte[][]{a, r, g, b};
		
		lut = new ByteLookupTable(0, data);
		luo = new LookupOp(lut, null);
		
		for (int i = 1; i < 256; i++)
		{
			r[i] = b[i] = g[i] = (byte)0;
			a[i] = (byte)(i);
		}
		data = new byte[][]{a, r, g, b};
		
		blt = new ByteLookupTable(0, data);
		toBlack = new LookupOp(blt, null);
	}

	private BufferedImage fader;
	
	public Fader()
	{
		try {
			fader = ImageIO.read(new File("data/transitions/slide.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		step();
		g.drawImage(buffer, 0, 0, null);
		
		if (fader == null)
			return;
		luo.filter(fader, fader);
		g.drawImage(toBlack.filter(fader, null), 0, 0, buffer.getWidth(), buffer.getHeight(), null);
	}

	@Override
	public void step()
	{
		currTime += length/255.0;
	}
}
