package core;

import logic.Girard;
import logic.Nick;

/**
 * 
 * @author nhydock
 *
 */
public class Engine{

	private static Engine instance = null;
	
	/**
	 * @return an instance of the engine
	 */
	public static Engine getInstance()
	{
		if (instance == null)
			instance = new Engine();
		return instance;
	}
	
	private Nick nick;
	private Girard girard;
	
	/**
	 * Creates an engine instance
	 */
	private Engine()
	{
		nick = new Nick();
		girard = new Girard();
	}
	
	/**
	 * @return instance of Nick associated with this system
	 */
	public Nick getNick(){
		return nick;
	}
	
	/**
	 * @return instance of Girard associated with this system
	 */
	public Girard getGirard(){
		return girard;
	}
}
