package scenes.Main;

import core.Engine;
import sugdk.audio.MP3;
import sugdk.scenes.GameSystem;

public class DrinkSystem extends GameSystem {

	PlayState ps;
	GameOverState gs;
	
	MP3 music;
	
	public DrinkSystem(){
		ps = new PlayState(this);
		gs = new GameOverState(this);
		music = new MP3("bgm.mp3");
		
		state = ps;
	}
	
	@Override
	public void start()
	{
		//music is first started when the game begins
		music.play(true);
	}
	
	@Override
	public void setNextState() {
		if (state == ps)
			state = gs;
		else
			state = ps;
		state.start();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	/**
	 * Resets the system values
	 */
	public void resetValues() {
		Engine.getInstance().getNick().setup();
		Engine.getInstance().getGirard().setup();
	}
}
