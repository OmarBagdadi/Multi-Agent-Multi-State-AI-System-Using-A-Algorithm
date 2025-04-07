/**
 * 
 */
package com.maze.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Class for a Treasure GameObject
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public class Treasure extends GameObject {
	private boolean isCollected;
	private Texture collectedTex;
	
	/**
	 * Constructor for a Treasure
	 * @param i row
	 * @param j col
	 * @param pos coordinates of the GameObject
	 * @param f f value
	 * @param g g value
	 * @param h h value
	 * @param notCollected Texture of the treasure when not collected
	 * @param collected Texture of the treasure when collected
	 */
	public Treasure(int i, int j, Vector2 pos, double f, double g, double h, Texture notCollected,Texture collected) {
		super(i, j, pos, f, g, h, notCollected);
		collectedTex = collected;
		isCollected = false;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if(isCollected == false) {
			super.draw(batch);
		}else {
			batch.draw(collectedTex, this.position.x, this.position.y);
		}
	}

	public boolean isCollected() {
		return isCollected;
	}

	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		collectedTex.dispose();
	}

}
