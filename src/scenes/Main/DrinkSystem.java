package scenes.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import logic.Nick;
import core.DataDirs;
import core.Engine;
import sugdk.scenes.GameStateMachine;
import sugdk.scenes.GameSystem;

/**
 * @author nhydock
 *
 */
public class DrinkSystem implements GameSystem {

	GameStateMachine<DrinkState> statemachine;
	
	PlayState ps;
	GameOverState gs;
	
	Music music;
	
	@Override
	public void start()
	{
		statemachine = new GameStateMachine<DrinkState>();
		statemachine.addState(PlayState.ID, new PlayState(this));
		statemachine.addState(GameOverState.ID, new GameOverState(this));
		statemachine.setState(PlayState.ID);
		
		music = Gdx.audio.newMusic(Gdx.files.internal(DataDirs.BGMDir.path + "bgm.mp3"));
		music.setLooping(true);
		
		//music is first started when the game begins
		music.play();
	}

	/**
	 * Resets the system values
	 */
	public void resetValues() {
		Engine.getInstance().getNick().setup();
		Engine.getInstance().getGirard().setup();
	}

	@Override
	public boolean keyDown(int key)
	{
		statemachine.getCurrentState().keyDown(key);
		return false;
	}

	@Override
	public boolean keyTyped(char arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void end()
	{
		//can never end
	}

	@Override
	public void update(float delta)
	{
		statemachine.handleCurrentState(delta);
	}
	
	/**
	 * @return the current state ID of the state machine
	 */
	public int getState()
	{
		return statemachine.getCurrentStateID();
	}
}
