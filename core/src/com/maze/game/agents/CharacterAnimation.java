package com.maze.game.agents;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
/**
 * Class that handles any character animation
 * @author Omar Bagdadi 
 * @version Practical Assignment
 */
public class CharacterAnimation {
	
	private static final int DIMENSION = 32; 
	private float elapsedTime;
	private Animation<TextureRegion> animation;
	private Texture animationSheet;
	private Texture idleTexture;
	private int rows, cols;
	
	/**
	 * Constructor for character animation 
	 * @param animationSheet The image with all the character animation frames
	 * @param idleTexture The idle texture after the animation is complete
	 * @param fps The speed at which the animation plays
	 */
	public CharacterAnimation(Texture animationSheet, Texture idleTexture, float fps) {
		//Load sprite Sheet as a texture
		this.animationSheet = animationSheet;
		this.idleTexture = idleTexture;
		
		//Calculates the number of rows and cols in the animation sheet
		rows = this.animationSheet.getHeight()/DIMENSION;
		cols = this.animationSheet.getWidth()/DIMENSION;
		
		
		//create a 2d array of textureRegions
		TextureRegion[][] tmp = TextureRegion.split(this.animationSheet, 32, 32);
		
		//Place it into a 1d array in the correct order
		TextureRegion[] walkFrames = new TextureRegion[rows  * cols];
		int index = 0;
		for (int i = 0; i < rows ; i++) {
			for (int j = 0; j < cols; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		
		
		// Initialize the Animation with the frame interval and array of frames
		animation = new Animation<TextureRegion>(fps, walkFrames);

		// Instantiate a SpriteBatch for drawing and reset the elapsed animation time to 0
		elapsedTime = 0f;
	}
	
	/**
	 * Method that updates the current frame of animation
	 * @param batch
	 * @param newPos
	 */
	public void update(SpriteBatch batch,Vector2 newPos) {
		// Accumulate elapsed animation time
		elapsedTime += Gdx.graphics.getDeltaTime(); 
		
		// gets the current animation frame and draws it at the newPos
		TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
		batch.draw(currentFrame, newPos.x, newPos.y); 
	}
	
	/**
	 * Gets the idle texture for the particular movement animation
	 * @return
	 */
	public Texture getIdleTexture() {
		return idleTexture;
	}
	
	
}
