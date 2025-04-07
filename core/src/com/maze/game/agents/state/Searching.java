/**
 * 
 */
package com.maze.game.agents.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.maze.game.agents.Agent;
import com.maze.game.world.Environment;
import com.maze.game.world.GameObject;
import com.maze.game.world.Goal;
import com.maze.game.world.Treasure;

/**
 * Class for the searching state
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public class Searching implements State{
	private Environment env;
	private GameObject[][] gameWorld;
	private LinkedList<Treasure> treasures;
	private Goal goal;
	public Agent agent;
	private LinkedList<LinkedList<GameObject>> allPaths;
	
	//A* variables
	
	/**
	 * Constructor for the Searching state
	 * @param agent the agent that is in the searching state
	 * @param env the environment that the agent is in
	 */
	public Searching(Agent agent, Environment env) {
		this.agent = agent;
		this.env = env;
		this.gameWorld = env.getGameWorld();
		
		treasures = env.getTreasures();
		goal = env.getGoal();
		allPaths = new LinkedList<>();
	}
	
	/**
	 * The call method responsible for performing the actions while in the current state
	 */
	@Override
	public void call() {
		//Check if all the treasures have been found
		if(!isEndGame()) {
			//If not get the shortest path to the closest treasure
			LinkedList<GameObject> pathToTreasure = getBestPath();
			System.out.println("A* complete found the shortest path to the next treasure");
			//Set the agents next path
			agent.setPathToNextTreasure(pathToTreasure);
			printPath(agent.getPathToNextTreasure());
//			resetAStar();
			// change the agents state to moving
			agent.setCurrentState(new Moving(this.agent, agent.getEnv()));
		}else {
			//Get path to the end goal
			LinkedList<GameObject> pathToTreasure = AStar(agent.getI(), agent.getJ(), goal.getI(), goal.getJ());
			resetAStar();
			//Set the agents path to the goal
			agent.setPathToNextTreasure(pathToTreasure);
			// Change the agents state to moving
			agent.setCurrentState(new Moving(this.agent, agent.getEnv()));
		}
		
	}
	
	/**
	 * Method that gets the path to the next closest treasure
	 * @return A list of GameObjects to the next treasure
	 */
	private LinkedList<GameObject> getBestPath() {
		LinkedList<GameObject> bestPath = null;
		//Performs the A* algorithm to all the treasures
		for(Treasure t: treasures) {
			if(!t.isCollected()) {
				allPaths.add(AStar(agent.getI(), agent.getJ(), t.getI(), t.getJ()));
				resetAStar();
			}
		}
		//Finds the path that is the shortest
		bestPath = allPaths.getFirst();
		int lowestNoMoves = allPaths.getFirst().size();
		for(LinkedList<GameObject> path: allPaths) {
			if (lowestNoMoves > path.size()) {
				bestPath = path;
				lowestNoMoves = path.size();
			}
		}
		
		return bestPath;
	}
	
	/**
	 * Method that performs the A* algorithm and finds the shortest path
	 * @param startI Start row
	 * @param startJ Start col
	 * @param endI End row
	 * @param endJ End col
	 * @return A list of GameObjects that represent the path from the start location to the end location
	 */
	private LinkedList<GameObject> AStar(int startI, int startJ, int endI, int endJ){
		GameObject end = env.getGameWorld()[endI][endJ];
		
		LinkedList<GameObject> path = new LinkedList<>();
		ArrayList<GameObject> openSet = new ArrayList<>();
		ArrayList<GameObject> closedSet = new ArrayList<>();
		
		openSet.add(gameWorld[agent.getI()][agent.getJ()]);
		
		while(openSet.size() > 0) {
			//Keep going
			int lowestIndex = 0;
			for(int i = 0; i < openSet.size(); i++) {
				if(openSet.get(i).getF() < openSet.get(lowestIndex).getF()) {
					lowestIndex = i;
				}
			}
			
			GameObject current = openSet.get(lowestIndex);
			
			// If the current is the end we done
			if(current == end) {
				//Find the path
				GameObject temp = current;
				path.add(temp);
				while(temp.getCameFrom() != null) {
					path.addFirst(temp.getCameFrom());
					temp = temp.getCameFrom();
				}
				return path;
			}
			
			// Remove from open set and put in closed set
			openSet.remove(current);
			closedSet.add(current);
			
			// get all of currents neighbors
			ArrayList<GameObject> neighbors = current.getNeighbours();
			
			for(GameObject n : neighbors) {
				if(!closedSet.contains(n) && n.isTraversable()) {
					double tempG = current.getG() + 1;
					
					//Have i evaluated the neighbor before to see if tempG is better 
					if(openSet.contains(n)) {
						if(tempG < n.getG()) {
							n.setG(tempG);
						}
					}else {
						// If not set the neighbors g score and add it to the open set 
						n.setG(tempG);
						openSet.add(n);
					}
					
					//Now calculate the heuristic
					n.setH(heuristic(n,end));
					n.setF(n.getG() + n.getH());
					n.setCameFrom(current);
				}
			}
			
		}
		
		return null;
	}
	
	/**
	 * Method responsible for resetting all the f,g,h values after the A* algorithm is complete
	 */
	private void resetAStar() {
		for (int i = 0; i < env.getRows(); i++) {
			for(int j = 0; j < env.getCols(); j++) {
				GameObject go = env.getGameWorld()[i][j];
				go.setF(0);
				go.setG(0);
				go.setH(0);
				go.setCameFrom(null);
			}
		}
	}
	
	/**
	 * Method that prints the path
	 * @param pathToTreasure
	 */
	private void printPath(LinkedList<GameObject> pathToTreasure) {
		for (GameObject g : pathToTreasure) {
			System.out.print("(" + g.getI() + "," + g.getJ() + ")");
		}
		System.out.println(pathToTreasure.size());
	}
	
	/**
	 * Method that calculates the heuristic of a particular node using the Manhattan distance
	 * @param current current node
	 * @param end end node
	 * @return h value
	 */
	private double heuristic(GameObject current, GameObject end) {
		//Uses the Manhattan distance 
		return Math.abs(current.getI() - end.getI()) + Math.abs(current.getJ() - end.getJ());
	}
	
	/**
	 * Method that checks if all treasures have been collected
	 * @return
	 */
	private boolean isEndGame() {
		boolean isEndGame = false;
		for(Treasure t : treasures) {
			if (!t.isCollected()) {
				return isEndGame;
			}
		}
		isEndGame = true;
		return isEndGame;
	}

}
