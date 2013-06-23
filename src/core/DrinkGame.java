package core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import core.Engine;

public class DrinkGame extends Game
{
	
	public DrinkGame()
	{
		new Engine();
	}
	
	@Override
	public void create()
	{
		Gdx.graphics.setVSync(true);
		
		this.setScreen(new scenes.Main.Scene());
	}
}
