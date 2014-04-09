package com.eryk.snake.model;

import com.badlogic.gdx.math.Vector2;

public class World {

	private Snake snake;
	private Bait bait;
	private Vector2 snakeBegPos = new Vector2();
	
	private static final float VELOCITY = 110f;
	private static final float initLength = 160f;
	private static final float snakeBodySize = 8;
	private static final float growth = 80f;
	
	public World(float CAM_WIDTH, float CAM_HEIGHT) {
		snakeBegPos.set(CAM_WIDTH/2-100, CAM_HEIGHT/2);
		this.snake = new Snake(snakeBegPos, VELOCITY, initLength, snakeBodySize, growth);
		this.bait = new Bait(snakeBodySize);
	}
	
	public Snake getSnake() {
		return snake;
	}
	
	public Bait getBait() {
		return bait;
	}
}
