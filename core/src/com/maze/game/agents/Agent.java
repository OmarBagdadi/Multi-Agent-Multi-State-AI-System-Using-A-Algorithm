/**
 * 
 */
package com.maze.game.agents;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.maze.game.agents.state.State;
import com.maze.game.world.Environment;
import com.maze.game.world.GameObject;

/**
 * Abstract class representing an agent in the game world
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public abstract class Agent {
	//GUI related variables
	protected Vector2 position;
	protected Sprite sprite;
	protected Texture trailer;
	protected SpriteBatch batch;
	
	//Environment
	protected Environment env;
	protected int i,j;
	
	//State variable
	protected State currentState;
	
	//Agent Variables
	protected String agentName;
	protected LinkedList<GameObject> pathToNextTreasure;
	protected Agent otherAgent;
	
	//Animation variables
	protected ArrayList<CharacterAnimation> charactersAnimations;
	protected CharacterAnimation currentAnimation = null;
	
	/**
	 * Default constructor for any Agent class
	 * @param img
	 * @param trailer
	 * @param batch
	 * @param i
	 * @param j
	 * @param startPos
	 * @param env
	 */
	public Agent(Texture img,Texture trailer, SpriteBatch batch, int i,int j,Vector2 startPos, Environment env) {
		this.batch = batch;
		this.sprite = new Sprite(img);
		this.trailer = trailer;
		this.i = i;
		this.j = j;
		this.position = startPos;
		sprite.setPosition(position.x, position.y);
		this.env = env;
		
		charactersAnimations = new ArrayList<>();
		
		this.pathToNextTreasure = new LinkedList<>();
	}
	
	/**
	 * Abstract method that creates the agents animations
	 */
	protected abstract void createAnimation();
	
	// Getters & Setters
	
	public Texture getTrailer() {
		return trailer;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public Environment getEnv() {
		return env;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public LinkedList<GameObject> getPathToNextTreasure() {
		return pathToNextTreasure;
	}

	public void setPathToNextTreasure(LinkedList<GameObject> pathToNextTreasure) {
		this.pathToNextTreasure = pathToNextTreasure;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public Agent getOtherAgent() {
		return otherAgent;
	}

	public void setOtherAgent(Agent otherAgent) {
		this.otherAgent = otherAgent;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public CharacterAnimation getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation(CharacterAnimation currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

	public ArrayList<CharacterAnimation> getCharactersAnimations() {
		return charactersAnimations;
	}
	
}
