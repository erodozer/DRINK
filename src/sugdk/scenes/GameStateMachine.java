package sugdk.scenes;

import java.util.HashMap;

/**
 * GameStateMachine
 * <p/>
 * State Machine for scenes that allows swapping between game states
 * @author nhydock
 * @param <State> Type of game state the machine will be managing
 */
public class GameStateMachine<State extends GameState<? extends GameSystem>>{

	private HashMap<Integer, State> stateList = new HashMap<Integer, State>();
	
	private State currentState; //current state in use by the machine
	private State nextState;	//state set to transition to
	
	private boolean switching;	//know to perform startup actions instead of handling during regular processing
	
	private int currentStateID;

	private int nextStateID;
	
	/**
	 * Adds a new state to the machine
	 * @param newState - the state to add to the machine
	 * @return true if the state was added to the machine, false if it couldn't be
	 */
	public boolean addState(Integer id, State newState)
	{
		boolean canAdd = !stateList.containsKey(id);
		
		if (canAdd)
		{
			stateList.put(id, newState);
		}
		
		return canAdd;
	}
	
	/**
	 * @return the state's current machine
	 */
	public State getCurrentState()
	{
		return currentState;
	}
	
	/**
	 * Updates the current state of the machine
	 * @param delta - time passed since previous update cycle of the game
	 */
	public void handleCurrentState(float delta)
	{
		if (nextState == null)
		{
			if (!switching)
			{
				currentState.handle(delta);
			}
			else
			{
				switching = currentState.start();
			}
		}
		else
		{
			// perform end processing until it reports as done
			if (!currentState.end())
			{
				currentState = nextState;
				currentStateID = nextStateID;
				nextState = null;
			}
		}
	}
	
	/**
	 * Sets the state machine to the state recognized by the ID number 
	 * @param ID - number identifying the state to switch to
	 */
	public void setState(int ID)
	{
		if (currentState != null)
		{
			nextState = stateList.get(ID);
			nextStateID = ID;
			// set the system to know that it's switching states and should perform
			// proper operations
			switching = (nextState != null);
		}
		else
		{
			currentStateID = ID;
			currentState = stateList.get(ID);
			switching = (currentState != null);
		}
	}
	
	public String toString()
	{
		String output = "";
		for (Integer i : stateList.keySet())
		{
			output += i + ":" + stateList.get(i) + "\n";
		}
		output += "CurrentState : " + currentState;
		return output;
	}

	public int getCurrentStateID()
	{
		return currentStateID;
	}
}
