/**
 * 
 */
package com.maze.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Class for the Goal GameObject
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public class Goal extends GameObject {
	private boolean isMet;
	private Sprite doorOpen;
	
	/**
	 * Constructor for a GameObject
	 * @param i row
	 * @param j col
	 * @param pos coordinates of the GameObject
	 * @param f f value
	 * @param g g value
	 * @param h h value
	 * @param texture Texture of the GameObject
	 * @param doorOpen Texture for when the door is open
	 */
	public Goal(int i, int j, Vector2 pos, double f, double g, double h, Texture texture,Texture doorOpen) {
		super(i, j, pos, f, g, h, texture);
		isMet = false;
		this.doorOpen = new Sprite(doorOpen);
		this.doorOpen.setPosition(this.position.x, this.position.y);
	}
	
	/**
	 * renders the goal to the screen
	 * @param batch
	 */
	@Override
	public void draw(SpriteBatch batch) {
		if (!isMet) {
			super.draw(batch);
		}else {
			doorOpen.draw(batch);
		}
	}
	
	//Getter & setter
	
	public boolean isMet() {
		return isMet;
	}

	public void setMet(boolean isMet) {
		this.isMet = isMet;
	}
	
	/**
	 * disposes the texture thats not used
	 * @param batch
	 */
	@Override
	public void dispose() {
		super.dispose();
		doorOpen.getTexture().dispose();
	}

}
