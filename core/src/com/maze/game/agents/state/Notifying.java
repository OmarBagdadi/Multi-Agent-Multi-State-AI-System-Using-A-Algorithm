/**
 * 
 */
package com.maze.game.agents.state;

import com.maze.game.agents.Agent;
import com.maze.game.agents.PathfindingAgent;
import com.maze.game.agents.TreasureHuntingAgent;

/**
 * Class for the notifying state
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public class Notifying implements State {
	private Agent thisAgent;
	private Agent otherAgent;
	
	/**
	 * Constructor for the notifying state
	 * @param agent
	 * @param otherAgent
	 */
	public Notifying(Agent agent, Agent otherAgent) {
		this.thisAgent = agent;
		this.otherAgent = otherAgent;
	}
	
	/**
	 * The call method responsible for performing the actions while in the current state
	 */
	@Override
	public void call() {
		System.out.println(thisAgent.getAgentName() + " currently notifying --> " + otherAgent.getAgentName());
		System.out.println(thisAgent.getAgentName() + " is currently in the Idle state awaiting instructions.");
		//Change the notifying agents state to Idle
		thisAgent.setCurrentState(new Idle());
		if(thisAgent instanceof PathfindingAgent) {
			// If the PathfindingAgent is notifying
			//Set the TreasureHuntingAgent agents path to follow
			otherAgent.setPathToNextTreasure(this.thisAgent.getPathToNextTreasure());
			System.out.println(otherAgent.getAgentName() + " is currently in the Moving state");
			// Change the TreasureHuntingAgents state to moving
			otherAgent.setCurrentState(new Moving(otherAgent, otherAgent.getEnv()));
		}else if(thisAgent instanceof TreasureHuntingAgent) {
			// If the TreasureHuntingAgent is notifying
			System.out.println(otherAgent.getAgentName() + " is currently in the Searching state:\nSearching for a path to the treasure");
			// Change the PathfindingAgents state to searching
			otherAgent.setCurrentState(new Searching(otherAgent, otherAgent.getEnv()));
			
		}
	}

}
