package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.controller.BaitController;
import com.mygdx.game.controller.SnakeController;
import com.mygdx.game.model.World;
import com.mygdx.game.view.Renderer;

import java.security.Key;


public class GameScreen implements Screen, InputProcessor {

	private static final float CAM_WIDTH = Gdx.graphics.getWidth();
	private static final float CAM_HEIGHT = Gdx.graphics.getHeight();
	
	private enum GameState {
		GAME_READY, GAME_RUNNING, GAME_OVER;
	}
	
	private GameState STATE;
	private Renderer renderer;
	private SnakeController snakeController;
	private BaitController baitController;
	private World world;

	@Override
	public void show() {
		this.world = new World(CAM_WIDTH, CAM_HEIGHT);
		this.renderer = new Renderer(world, CAM_WIDTH, CAM_HEIGHT);
		this.snakeController = new SnakeController(world, CAM_WIDTH, CAM_HEIGHT);
		this.baitController = new BaitController(world, CAM_WIDTH, CAM_HEIGHT);
		Gdx.input.setInputProcessor(this);
		STATE = GameState.GAME_READY;
		
	}
	
	@Override
	public void render(float delta) {
		switch (STATE) {
		case GAME_READY:
			Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			renderer.renderReady();
			break;
		case GAME_RUNNING:
			Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			baitController.update();
			if(snakeController.update(delta)) {
				STATE = GameState.GAME_OVER;
			}
			renderer.render();
			break;
		case GAME_OVER:
			Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			renderer.renderFail();
			break;
		}
	}
	
	private void reset() {
		this.world = new World(CAM_WIDTH, CAM_HEIGHT);
		this.renderer = new Renderer(world, CAM_WIDTH, CAM_HEIGHT);
		this.snakeController = new SnakeController(world, CAM_WIDTH, CAM_HEIGHT);
		this.baitController = new BaitController(world, CAM_WIDTH, CAM_HEIGHT);
		STATE = GameState.GAME_READY;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(STATE == GameState.GAME_READY)
			STATE = GameState.GAME_RUNNING;
		else if(STATE == GameState.GAME_OVER) {
			reset();
		}

		if(keycode == Keys.UP)
			snakeController.upPressed();
		if(keycode == Keys.DOWN)
			snakeController.downPressed();
		if(keycode == Keys.LEFT)
			snakeController.leftPressed();
		if(keycode == Keys.RIGHT)
			snakeController.rightPressed();
		if(keycode == Keys.ESCAPE || keycode == Keys.ENTER)
			Gdx.app.exit();
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.UP)
			snakeController.upReleased();
		if(keycode == Keys.DOWN)
			snakeController.downReleased();
		if(keycode == Keys.LEFT)
			snakeController.leftReleased();
		if(keycode == Keys.RIGHT)
			snakeController.rightReleased();
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

}

