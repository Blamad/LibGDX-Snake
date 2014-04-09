package com.eryk.snake;

import com.badlogic.gdx.Game;
import com.eryk.snake.screen.GameScreen;

public class SnakeGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
	
}