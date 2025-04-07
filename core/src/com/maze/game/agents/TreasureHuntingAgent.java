/**
 * 
 */
package com.maze.game.agents;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.maze.game.agents.state.Idle;
import com.maze.game.agents.state.Moving;
import com.maze.game.agents.state.Notifying;
import com.maze.game.world.Environment;
import com.maze.game.world.GameObject;
import com.maze.game.world.Goal;
import com.maze.game.world.Obstacle;
import com.maze.game.world.Treasure;

/**
 * Concrete agent in the game world that collects all the treasure
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public class TreasureHuntingAgent extends Agent {
	
	/**
	 * Constructor for the TreasureHuntingAgent
	 * @param img The image used to draw the agent
	 * @param trailer The image used to reset any trailing effects
	 * @param batch SpriteBatch used to draw the Agent to the screen
	 * @param i row location in the grid
	 * @param j column location in the grid
	 * @param startPos Starting location of the agent 
	 * @param env the environment that the agent is situated in
	 */
	public TreasureHuntingAgent(Texture img, Texture trailer, SpriteBatch batch, int i,int j,Vector2 startPos, Environment env) {
		super(img, trailer, batch, i, j, startPos, env);
		this.agentName = "TreasureHuntingAgent";
		//Creates the characters animations
		createAnimation();
		// Sets its initial state to Idle
		this.currentState = new Idle();
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
	 * Method that creates the animation for the agent
	 */
	@Override
	protected void createAnimation() {
		charactersAnimations = new ArrayList<CharacterAnimation>();
		float fps = 1f/8f;
		//Creating the walk up animation
		Texture upAnimation = new Texture(Gdx.files.internal("Player/walk_animation_up.png"));
		Texture idleUp = new Texture(Gdx.files.internal("Player/idle_up.png"));
		CharacterAnimation animateUp = new CharacterAnimation(upAnimation,idleUp, fps);
		charactersAnimations.add(animateUp);
		
		//Creating the walk down animation
		Texture downAnimation = new Texture(Gdx.files.internal("Player/walk_animation_down.png"));
		Texture idleDown = new Texture(Gdx.files.internal("Player/idle_down.png"));
		CharacterAnimation animateDown = new CharacterAnimation(downAnimation,idleDown, fps);
		charactersAnimations.add(animateDown);
		
		//Creating the walk left animation
		Texture leftAnimation = new Texture(Gdx.files.internal("Player/walk_animation_left.png"));
		Texture idleLeft = new Texture(Gdx.files.internal("Player/idle_left.png"));
		CharacterAnimation animateLeft = new CharacterAnimation(leftAnimation,idleLeft, fps);
		charactersAnimations.add(animateLeft);
		
		//Creating the walk right animation
		Texture rightAnimation = new Texture(Gdx.files.internal("Player/walk_animation_right.png"));
		Texture idleRight = new Texture(Gdx.files.internal("Player/idle_right.png"));
		CharacterAnimation animateRight = new CharacterAnimation(rightAnimation,idleRight, fps);
		charactersAnimations.add(animateRight);
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

}
