/**
 * 
 */
package com.maze.game.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.maze.game.ExitDialog;
import com.maze.game.agents.PathfindingAgent;
import com.maze.game.agents.TreasureHuntingAgent;
import com.maze.game.agents.state.Idle;
import com.maze.game.agents.state.Notifying;

/**
 * Class that represents the environment of the game
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public class Environment {
	private GameObject[][] gameWorld;
	private int rows, cols;
	private ArrayList<Texture> assets;
	private SpriteBatch batch;
	
	//Store Agents
	PathfindingAgent pathAgent;
	TreasureHuntingAgent treasureAgent;
	
	//Store end goal state
	private LinkedList<Treasure> treasures;
	private Goal goal;
	
	/**
	 * Constructor for the Environment
	 * @param batch
	 */
	public Environment(SpriteBatch batch) {
		this.batch = batch;
		
		// Load all assets
		assets = new ArrayList<>();
		treasures = new LinkedList<>();
		assets.add(new Texture(Gdx.files.internal("World/Goal_Opened.png")));
		assets.add(new Texture(Gdx.files.internal("World/Goal_Closed.png")));
		assets.add(new Texture(Gdx.files.internal("World/Wall.png")));
		assets.add(new Texture(Gdx.files.internal("World/Alt_Path.png")));
		assets.add(new Texture(Gdx.files.internal("World/Treasure_Open.png")));
		assets.add(new Texture(Gdx.files.internal("World/Treasure_Closed.png")));
		assets.add(new Texture(Gdx.files.internal("Player/Slime_Goo.png")));
		assets.add(new Texture(Gdx.files.internal("Player/PathFinder.png")));
		assets.add(new Texture(Gdx.files.internal("Player/idle_down.png")));
	}

	/**
	 * Method responsible for loading the level from a file and creating the environment
	 * @param levelFile
	 */
	public void loadLevel(FileHandle levelFile) {
		Scanner sc = new Scanner(levelFile.read());
		//Read the maze from the file
		ArrayList<String> lines = new ArrayList<>();
		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		//Reverse the order because libGDX uses the bottom left as the origin (0,0)
		lines = reverseOrder(lines);
		rows = lines.size();
		cols = lines.get(0).toCharArray().length;
		
		// Initialise the environment
		gameWorld = new GameObject[rows][cols];
		for(int i = 0; i < rows; i++) {
			char[] objects = lines.get(i).toCharArray();
			for(int j = 0; j < objects.length; j++) {
				Vector2 objectPos = new Vector2(j*32, i*32);
				switch(objects[j]) {
					case '.':
						gameWorld[i][j] = new Path(i, j, objectPos, 0, 0, 0, assets.get(OBJECT_ENUM.PATH.ordinal()));
						break;
					case 'X':
						Treasure t = new Treasure(i, j, objectPos, 0, 0, 0, assets.get(OBJECT_ENUM.TREASURE_CLOSED.ordinal()) , assets.get(OBJECT_ENUM.TREASURE_OPENED.ordinal()));
						gameWorld[i][j] = t;
						treasures.add(t);
						break;
					case '#':
						gameWorld[i][j] = new Obstacle(i, j, objectPos, 0, 0, 0, assets.get(OBJECT_ENUM.OBSTACLE.ordinal())); 
						break;
					case 'G':
						Goal g = new Goal(i, j, objectPos, 0, 0, 0, assets.get(OBJECT_ENUM.GOAL_CLOSED.ordinal()), assets.get(OBJECT_ENUM.GOAL_OPENED.ordinal()));
						gameWorld[i][j] = g;
						goal = g;
						break;
					case 'P':
						treasureAgent = new TreasureHuntingAgent(assets.get(OBJECT_ENUM.TREASURE_HUNTER.ordinal()), assets.get(OBJECT_ENUM.PATH.ordinal()), batch, i, j, objectPos, this);
						pathAgent = new PathfindingAgent(assets.get(OBJECT_ENUM.PATH_FINDER.ordinal()), assets.get(OBJECT_ENUM.SLIME_GOO.ordinal()), batch, i, j, objectPos, this);
						//Associate the agents
						treasureAgent.setOtherAgent(pathAgent);
						pathAgent.setOtherAgent(treasureAgent);
						//Set the agents initial state
						pathAgent.setCurrentState(new Idle());
						treasureAgent.setCurrentState(new Notifying(treasureAgent, pathAgent));
						gameWorld[i][j] = new Path(i, j, objectPos, 0, 0, 0, assets.get(OBJECT_ENUM.PATH.ordinal()));
						break;
				}
			}
		}
		
		//Add the neighbors for each element in the grid
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				gameWorld[i][j].addNeighbours(gameWorld);
			}
		}
		
	}
	
	/**
	 * Method that reverses the order of the ArrayList
	 * @param list
	 * @return
	 */
	private ArrayList<String> reverseOrder(ArrayList<String> list) {
		ArrayList<String> reversedList = new ArrayList<>();
		for(int i = list.size()-1; i>=0;i--) {
			reversedList.add(list.get(i));
		}
		
		return reversedList;
	}
	
	/**
	 * Method responsible for rendering the world
	 */
	public void renderWorld() {
		Texture background = assets.get(OBJECT_ENUM.PATH.ordinal());
		batch.draw(background, 0, 0, getWidth(), getHeight());
		//Draw in reverse so everything renders correctly
		for(int i = rows - 1; i >= 0; i--) {
			for(int j = cols - 1; j >= 0; j--) {
				gameWorld[i][j].draw(batch);
			}
		}
		//Check if the goal is met
		if(!goal.isMet()) {
			goal.setMet(checkEndCondition());
		}
		//Check if both the agents are IDLE
		if(! ((pathAgent.getCurrentState() instanceof Idle) && (treasureAgent.getCurrentState() instanceof Idle))) {
			// If they not Idle draw them
			pathAgent.draw();
			treasureAgent.draw();
		}
	}
	
	/**
	 * Method that checks if all the treasures have been collected
	 * @return
	 */
	public boolean checkEndCondition() {
		boolean isMet = true;
		for(Treasure t: treasures) {
			if(!t.isCollected()) {
				isMet = false;
			}
		}
		return isMet;
	}
	
	/**
	 * Method that disposes all the textures when closed
	 */
	public void dispose() {
		for(int i = rows - 1; i >= 0; i--) {
			for(int j = cols - 1; j >= 0; j--) {
				gameWorld[i][j].dispose();
			}
		}
	}
	
	// Getters & Setters
	
	public float getWidth() {
		return cols * 32;
	}
	
	public float getHeight() {
		return rows * 32;
	}
	
	public GameObject[][] getGameWorld() {
		return gameWorld;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public LinkedList<Treasure> getTreasures() {
		return treasures;
	}

	public void setTreasures(LinkedList<Treasure> treasures) {
		this.treasures = treasures;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public void setGameWorld(GameObject[][] gameWorld) {
		this.gameWorld = gameWorld;
	}
}
