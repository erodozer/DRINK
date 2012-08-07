package scenes.Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import core.Engine;
import core.Input;
import core.Main;

import sugdk.graphics.RenderManager;
import sugdk.graphics.SFont;
import sugdk.graphics.Sprite;
import sugdk.scenes.SceneDisplay;

public class DrinkDisplay extends SceneDisplay {

	/**
	 * The background image
	 */
	private Sprite bg;
	
	private boolean gameover;
	
	private String score;
	private String time;
	
	public DrinkDisplay()
	{
		bg = new Sprite("bg.png");
		font = SFont.loadFont("default", 7.0f);
		System.out.println(bg.getWidth());
	}
	
	public void fixScreen()
	{
		//fix the resolution once it's loaded
		RenderManager.getInstance().setInternalResolution((int)bg.getWidth(),(int)bg.getHeight());
		RenderManager.getInstance().setScale(400.0);	
	}
	
	@Override
	public void update() {
		Engine e = Engine.getInstance();
		gameover = parent.getState() instanceof GameOverState;
		score = String.format("%.2f", e.getNick().getScore());
		long t = e.getNick().getTime();
		time = String.format("%02d:%02d:%03d", TimeUnit.NANOSECONDS.toMinutes(t), TimeUnit.NANOSECONDS.toSeconds(t) % 60, TimeUnit.NANOSECONDS.toMillis(t) % 1000);
	}

	@Override
	public void paint(Graphics g) {
		Engine e = Engine.getInstance();
				
		bg.paint(g);
		e.getNick().paint(g);
		e.getGirard().paint(g);
		
		//draw the shadows
		font.drawString(g, "TIME: " + time, 2, 18, SFont.LEFT, Color.GRAY, bg);
		font.drawString(g, "FL Dranked", 2, 18, SFont.RIGHT, Color.GRAY, bg);
		font.drawString(g, score, 2, 25, SFont.RIGHT, Color.GRAY, bg);
		
		//now draw properly
		font.drawString(g, "TIME: " + time, 2, 17, bg);
		font.drawString(g, "FL Dranked", 2, 17, 2, bg);
		font.drawString(g, score, 2, 24, 2, bg);

		font.drawString(g, (gameover)?"YOU HAVE BEEN CAUGHT!":"PRESS " + KeyEvent.getKeyText(Input.KEY_DRINK) + " TO CHUG OJ", 0, 95, 1, bg);
		font.drawString(g, (gameover)?"PRESS " + KeyEvent.getKeyText(Input.KEY_RESET) + " TO RESET GAME":"", 0, 8, 1, Color.BLACK, bg);	
	
	}

}
