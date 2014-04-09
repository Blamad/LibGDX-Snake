package com.eryk.snake.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.eryk.snake.controller.BaitController;
import com.eryk.snake.controller.SnakeController;
import com.eryk.snake.model.World;
import com.eryk.snake.view.Renderer;

/**
 * stad kontroluje obsluge przyciskow
 * inicjuje swiat gry, kontroler, render
 * 
 */


public class GameScreen implements Screen, InputProcessor {

	private static final float CAM_WIDTH = Gdx.graphics.getWidth();
	private static final float CAM_HEIGHT = Gdx.graphics.getHeight();
	
	private enum GameState {
		GAME_READY, GAME_RUNNING, GAME_PAUSED, GAME_OVER;
	}
	
	private GameState STATE;
	private Renderer renderer;
	private SnakeController snakeController;
	private BaitController baitController;
	private World world;
	
	private float currentTime = 0;
	private boolean renderIt = false;

	
	//konstruktor screenu
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
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			renderer.renderReady();
			break;
		case GAME_RUNNING:
			Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			baitController.update();
			if(snakeController.update(delta)) {
				STATE = GameState.GAME_OVER; // game over
				currentTime = System.nanoTime();
			}
			renderer.render();
			break;
		case GAME_PAUSED:
			Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			renderer.render();
			break;
		case GAME_OVER:
			Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			if((System.nanoTime() - currentTime)/1000 > 500000) {
				currentTime = System.nanoTime();
				renderIt = !renderIt;
			}
			if(renderIt)
				renderer.render();
			else
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
		//andek sie nie resize'uje
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
		if(keycode == Keys.W || keycode == Keys.UP)
			snakeController.upPressed();
		if(keycode == Keys.S || keycode == Keys.DOWN)
			snakeController.downPressed();
		if(keycode == Keys.A || keycode == Keys.LEFT)
			snakeController.leftPressed();
		if(keycode == Keys.D || keycode == Keys.RIGHT)
			snakeController.rightPressed();
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.W || keycode == Keys.UP)
			snakeController.upReleased();
		if(keycode == Keys.S || keycode == Keys.DOWN)
			snakeController.downReleased();
		if(keycode == Keys.A || keycode == Keys.LEFT)
			snakeController.leftReleased();
		if(keycode == Keys.D || keycode == Keys.RIGHT)
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
		if(STATE == GameState.GAME_PAUSED || STATE == GameState.GAME_READY)
			STATE = GameState.GAME_RUNNING;
		else if(STATE == GameState.GAME_OVER) {
			reset();
		} else 			
			STATE = GameState.GAME_PAUSED;
			
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}

