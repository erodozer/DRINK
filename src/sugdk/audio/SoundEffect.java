package sugdk.audio;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;

/**
 * SoundEffect
 * @author nhydock
 *
 *	Simple sound container class that allows playing and spawning of new sound effects
 *	Multiple sound effects can be played at the same time, allowing for ambient
 *	noises as well as battle attacks to be heard simultaneously.
 */
public class SoundEffect{
	
	String name;	//name of the sound effect file
	Line line;
	Clip clip;		//the sound clip to play
	AudioInputStream ais;
	
	boolean loop;
	
	/**
	 * Creates a SoundEffect to use
	 * @param s	name of the sound effect
	 */
	public SoundEffect(String s)
	{
		name = s;
		try {
			File f = new File("data/audio/fx/nature.wav");
			ais = AudioSystem.getAudioInputStream(f);
			AudioFormat af = ais.getFormat();
			clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, ais.getFormat()));
			clip.open(ais);
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
		}
	}
	
	/**
	 */
	public void setLoop(boolean loop)
	{
		if (clip != null)
		{
			this.loop = loop;
		}
	}
	
	/**
	 * Starts to play the clip
	 */
	public void play()
	{
		if (clip != null)
		{
			if (clip.isRunning())
				return;
			
			if (loop)
				clip.loop(-1);
			else
				clip.start();
		}
	}
	
	/**
	 * Stops a sound that's already playing
	 * When a sound is stopped, it resets to the beginning
	 */
	public void stop()
	{
		if (clip != null)
			clip.stop();
	}
	
	/**
	 * Kills the sound off to not be used again
	 */
	public void dispose()
	{
		if (clip != null)
		{
			clip.stop();
			clip.close();
			clip = null;
		}
	}
}
