/**
 * 
 */
package com.maze.game.world;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class for a GameObject
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public abstract class GameObject {
	private int i, j;				//coords in grid
	protected Vector2 position;		//coords in game
	private double f ,g ,h;			// used to for A* calculation
	protected Sprite sprite;
	
	private ArrayList<GameObject> neighbours;	//neighbors of the game object
	private GameObject cameFrom;				//used to backtrack after the A* is complete
	protected boolean isTraversable;			//used to check if the current object is traversable
	
	/**
	 * Constructor for a GameObject
	 * @param i row
	 * @param j col
	 * @param pos coordinates of the GameObject
	 * @param f f value
	 * @param g g value
	 * @param h h value
	 * @param texture Texture of the GameObject
	 */
	public GameObject(int i , int j, Vector2 pos, double f,double g,double h, Texture texture) {
		this.i = i;
		this.j = j;
		this.position = pos;
		this.f = f;
		this.g = g;
		this.h = h;
		this.sprite = new Sprite(texture);
		this.sprite.setPosition(pos.x, pos.y);
		this.isTraversable = true;
		neighbours = new ArrayList<>();
		this.cameFrom = null;
	}
	
	/**
	 * Method that adds all the neighbors to the GameObject
	 * @param grid 
	 */
	public void addNeighbours(GameObject[][] grid) {
		
		if(i < grid.length - 1) {
			this.neighbours.add(grid[i + 1][j]);
		}
		if(i > 0) {
			this.neighbours.add(grid[i - 1][j]);
		}
		if(j < grid[0].length - 1) {
			this.neighbours.add(grid[i][j + 1]);
		}
		if(j > 0) {
			this.neighbours.add(grid[i][j - 1]);
		}
	}
	
	/**
	 * renders the game object to the screen
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	//Getters & Setters
	
	public GameObject getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(GameObject cameFrom) {
		this.cameFrom = cameFrom;
	}

	public ArrayList<GameObject> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(ArrayList<GameObject> neighbours) {
		this.neighbours = neighbours;
	}

	public boolean isTraversable() {
		return isTraversable;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public Vector2 getPosition() {
		return position;
	}

	public double getF() {
		return f;
	}



	public void setF(double f) {
		this.f = f;
	}



	public double getG() {
		return g;
	}



	public void setG(double g) {
		this.g = g;
	}



	public double getH() {
		return h;
	}



	public void setH(double h) {
		this.h = h;
	}
	
	public void setTexture(Texture newTexture) {
		sprite.setTexture(newTexture);
	}

	public Sprite getSprite() {
		return sprite;
	}


	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void dispose() {
		sprite.getTexture().dispose();
	}
	
}
