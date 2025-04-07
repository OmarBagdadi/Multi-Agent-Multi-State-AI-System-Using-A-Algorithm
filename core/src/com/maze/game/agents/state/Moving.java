/**
 * 
 */
package com.maze.game.agents.state;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.maze.game.agents.Agent;
import com.maze.game.agents.AnimationDirection;
import com.maze.game.agents.PathfindingAgent;
import com.maze.game.agents.TreasureHuntingAgent;
import com.maze.game.world.Environment;
import com.maze.game.world.GameObject;
import com.maze.game.world.Goal;
import com.maze.game.world.Obstacle;
import com.maze.game.world.Treasure;

/**
 * Class for the moving state
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public class Moving implements State{
	private Environment env;
	private GameObject[][] gameWorld;
	private Agent agent;
	private LinkedList<GameObject> pathToTreasure;
	private int currentMove;
	private Sprite sprite;
	private SpriteBatch batch;
	
	//Interpolation variables
	private boolean isInterpolating;
	private float interpolationTime = 0.3f; // time to interpolate between tiles, in seconds
    private float interpolationTimer = 0; // time elapsed since starting interpolation
    private Vector2 targetPos, origPos; // target tile for interpolation
	
    /**
     * Constructor for the moving state
     * @param agent the agent that is currently moving
     * @param env the environment that the agent is moving in
     */
	public Moving(Agent agent, Environment env) {
		this.agent = agent;
		this.env = env;
		this.gameWorld = env.getGameWorld();
		this.pathToTreasure = agent.getPathToNextTreasure();
		this.currentMove = 0;
		this.sprite = agent.getSprite();
		this.batch = agent.getBatch();
	}
	
	
	/**
	 * The call method responsible for performing the actions while in the current state
	 */
	@Override
	public void call() {
		//Check if it reached the end of the path it was meant to reach
		if(!reachedEnd()) {
			// if not delegate to update
			update();
		}else if(reachedGoal()) {
			//If the path finding agent has reached the end goal it should change its state to notifying
			if(agent instanceof PathfindingAgent) {
				agent.setCurrentState(new Notifying(agent, agent.getOtherAgent()));
			}else {
				agent.setCurrentState(new Idle());
				System.out.println("Congratulations the treasure hunting agent has sucessfully collected all treasures and escaped!!");
			}
		}
		else {
			//if the agent has reached a treasure it should notify the other agent
			agent.setCurrentState(new Notifying(agent, agent.getOtherAgent()));
		}
		
	}
	
	/**
	 * Method that checks if the agent has reached the end goal
	 * @return
	 */
	private boolean reachedGoal() {
		if(reachedEnd()) {
			GameObject lastPos = gameWorld[agent.getI()][agent.getJ()];
			if(lastPos instanceof Goal) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method responsible for actually moving the agent in the game world
	 */
	public void update() {
		// Gets the next move from the path to the treasure
		GameObject nextMove = pathToTreasure.get(currentMove);
		GameObject redrawObj = null;
		
		//Gets the object that needs to be redrawn for the trailer effect
		if(!(nextMove instanceof Treasure)) {
//			redrawObj = gameWorld[agent.getI()][agent.getJ()];
			if((currentMove - 1) > 0) {
				redrawObj = pathToTreasure.get(currentMove - 1);
			}else {
				redrawObj = pathToTreasure.get(currentMove);
			}
		}
		
		// Checks if the agent is currently in motion
		if(!isInterpolating) {
			//If not check which agent is moving
			if (agent instanceof TreasureHuntingAgent) {
				//If its the TreasureHuntingAgent check if its a valid move
				if(isValidMove(nextMove.getI(), nextMove.getJ())) {
					// Save old position
					final Vector2 oldPos = agent.getPosition();
					//Draw trailing effect
					if(redrawObj != null ) {
						redrawObj.setTexture(agent.getTrailer());
					}
					//Change agents position
					agent.setI(nextMove.getI());
					agent.setJ(nextMove.getJ());
					// Save new position
					final Vector2 newPos = nextMove.getPosition();
					//Change agents animation
					agent.setCurrentAnimation(agent.getCharactersAnimations().get(getAnimationDirection(oldPos, newPos)));
					sprite.setTexture(agent.getCurrentAnimation().getIdleTexture());
					// increment the current move count
					currentMove++;
					//Start moving
					startInterpolation(oldPos,newPos);
				}
			}else {
				// Save old position
				final Vector2 oldPos = agent.getPosition();
				//Draw trailing effect
				if(redrawObj != null) {
					redrawObj.setTexture(agent.getTrailer());
				}
				//Change agents position
				agent.setI(nextMove.getI());
				agent.setJ(nextMove.getJ());
				// Save new position
				final Vector2 newPos = nextMove.getPosition();
				//Change agents animation
				agent.setCurrentAnimation(agent.getCharactersAnimations().get(0));
				// increment the current move count
				currentMove++;
				//Start moving
				startInterpolation(oldPos,newPos);
			}
			
		}else {
			//Increase the interpolation timer
			interpolationTimer += Gdx.graphics.getDeltaTime();
            float t = Math.min(interpolationTimer / interpolationTime, 1);

            // Compute new player position using a smooth interpolation
            Interpolation interpolation = Interpolation.smoother;
            Vector2 currentPos = origPos.interpolate(targetPos, t,interpolation);

            // Draw player sprite at interpolated position
            sprite.setPosition(currentPos.x, currentPos.y);
//            if(agent instanceof TreasureHuntingAgent) {
//				agent.getCurrentAnimation().update(batch, currentPos);
//			}else {
//				sprite.draw(batch);
//			}
            agent.getCurrentAnimation().update(batch, currentPos);
            
            // If interpolation is complete, update player position and reset timer
            if (t >= 1f) {
            	sprite.setPosition(targetPos.x, targetPos.y);
                agent.setPosition(targetPos);
                isInterpolating = false;
                sprite.draw(batch);
            }
		}
	}
	
	/**
	 * Method that gets the direction of the agents movement
	 * @param oldPos agents old pos
	 * @param newPos agents new pos
	 * @return 
	 */
	private int getAnimationDirection(Vector2 oldPos, Vector2 newPos) {
		float deltaX = newPos.x - oldPos.x;
        float deltaY = newPos.y - oldPos.y;

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                return AnimationDirection.ANIMATE_RIGHT.ordinal();
            } else {
                return AnimationDirection.ANIMATE_LEFT.ordinal();
            }
        } else {
            if (deltaY > 0) {
                return AnimationDirection.ANIMATE_UP.ordinal();
            } else {
                return AnimationDirection.ANIMATE_DOWN.ordinal();
            }
        }
	}
	
	/**
	 * Method that checks if the agent has reached the end of the path
	 * @return
	 */
	private boolean reachedEnd() {
		boolean reachedEnd = false;
		if(currentMove < pathToTreasure.size()) {
			reachedEnd = false;
		}else {
			reachedEnd = true;
		}
		return reachedEnd;
	}
	
	/**
	 * Method that starts the movement
	 * @param orig agents old pos
	 * @param target agents new pos
	 */
	private void startInterpolation(Vector2 orig,Vector2 target) {
	    targetPos = target;
	    origPos = orig;
	    interpolationTimer = 0f;
	    isInterpolating = true;
	}
	
	/**
	 * Checks to see if the current move is a valid move
	 * @param i row
	 * @param j col
	 * @return
	 */
	private boolean isValidMove(int i, int j) {
		//Checking if the player tries to move out of bounds
		if(i < 0) {return false;}
		if(j < 0) {return false;}
		if(i >= env.getRows()) {return false;}
		if(j >= env.getCols()) {return false;}
		
		//Check for collisions
		GameObject[][] world = env.getGameWorld();
		if(world[i][j] instanceof Obstacle) {return false;}
		if(world[i][j] instanceof Treasure) {
			Treasure t = (Treasure) world[i][j];
			if(!t.isCollected()) {
				t.setCollected(true);
			}
		}
		agent.getEnv().setGameWorld(world);
		if(world[i][j] instanceof Goal) {
			Goal goal = (Goal) world[i][j];
			if(!goal.isMet()) {
				return false;
			}else {
				System.out.println("Conditions met");
			}
		}
		
		return true;
	}

}
