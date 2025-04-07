/**
 * 
 */
package com.maze.game.agents;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.maze.game.agents.state.Idle;
import com.maze.game.agents.state.Moving;
import com.maze.game.agents.state.Searching;
import com.maze.game.world.Environment;

/**
 * Concrete agent in the game world that searches for all the treasures
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public class PathfindingAgent extends Agent {	
	
	/**
	 * Constructor for the PathfindingAgent
	 * @param img The image used to draw the agent
	 * @param trailer The image used to reset any trailing effects
	 * @param batch SpriteBatch used to draw the Agent to the screen
	 * @param i row location in the grid
	 * @param j column location in the grid
	 * @param startPos Starting location of the agent
	 * @param env the environment that the agent is situated in
	 */
	public PathfindingAgent(Texture img,Texture trailer, SpriteBatch batch, int i,int j,Vector2 startPos, Environment env) {
		super(img, trailer, batch, i, j, startPos, env);
	
		this.agentName = "PathfindingAgent";
		this.currentState = new Idle();
		createAnimation();
	}
	
	/**
	 * Method that acts as the sensor for the agent
	 */
	public void sense() {
		//Runs the current state
		currentState.call();
	}
	
	/**
	 * Method that acts as the actuator of the agent
	 */
	public void act() {
		//Delegates to sense
		sense();
	}
	
	/**
	 * Method responsible for drawing the agent
	 */
	public void draw() {
		if(!(this.currentState instanceof Moving)) {
			sprite.draw(batch);
		}
		act();
	}
	
	/**
	 * Creates the animation for the path finding agent
	 */
	@Override
	protected void createAnimation() {
		charactersAnimations = new ArrayList<CharacterAnimation>();
		float fps = 1f/24f;
		//Creating the walk up animation
		Texture slimeAnimation = new Texture(Gdx.files.internal("Player/PathFinder_Animation.png"));
		CharacterAnimation slimeAnimate = new CharacterAnimation(slimeAnimation ,null,fps);
		charactersAnimations.add(slimeAnimate);
		
//		this.currentAnimation = slimeAnimate;
	}
	
}
