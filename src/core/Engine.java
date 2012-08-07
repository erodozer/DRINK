package core;

import logic.Girard;
import logic.Nick;
import sugdk.engine.GameEngine;

public class Engine extends GameEngine {

	private static Engine instance = null;
	
	/**
	 * Get an instance of the engine
	 * @return
	 */
	public static Engine getInstance()
	{
		if (instance == null)
			instance = new Engine();
		return instance;
	}
	
	private Nick nick;
	private Girard girard;
	
	public Engine()
	{
		nick = new Nick();
		girard = new Girard();
	}
	
	/**
	 * Get instance of Nick associated with this system
	 * @return
	 */
	public Nick getNick(){
		return nick;
	}
	
	/**
	 * Get instance of Girard associated with this system
	 * @return
	 */
	public Girard getGirard(){
		return girard;
	}
}
