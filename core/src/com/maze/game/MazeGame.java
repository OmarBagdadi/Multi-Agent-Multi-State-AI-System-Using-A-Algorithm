package com.maze.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.maze.game.world.Environment;

public class MazeGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Environment env;
	OrthographicCamera camera;
	
	@Override
	public void create () {
		//create environment
		batch = new SpriteBatch();
		env = new Environment(batch);
		env.loadLevel(Gdx.files.internal("Levels/Level1.txt"));
		
		//Setup camera
		camera = new OrthographicCamera(env.getWidth(), env.getHeight());
        camera.setToOrtho(false, env.getWidth(), env.getHeight());
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	}

	@Override
	public void render () {
		//Setting the SpriteBatch to project to the camera
		batch.setProjectionMatrix(camera.combined);
		//Rendering the game world
		batch.begin();
		env.renderWorld();
		batch.end();
	}
	
	@Override
	public void dispose () {
		//Disposing of all assets
		batch.dispose();
		env.dispose();
	}
}
