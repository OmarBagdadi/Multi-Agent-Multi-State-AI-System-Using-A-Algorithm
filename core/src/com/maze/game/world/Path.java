/**
 * 
 */
package com.maze.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Class for a Path GameObject
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public class Path extends GameObject {
	
	/**
	 * Constructor for a GamePath
	 * @param i row
	 * @param j col
	 * @param pos coordinates of the GameObject
	 * @param f f value
	 * @param g g value
	 * @param h h value
	 * @param texture Texture of the GameObject
	 */
	public Path(int i, int j, Vector2 pos, double f, double g, double h, Texture texture) {
		super(i, j, pos, f, g, h, texture);
	}
	
	public void setTexture(Texture tex) {
		this.sprite.setTexture(tex);
	}
}
