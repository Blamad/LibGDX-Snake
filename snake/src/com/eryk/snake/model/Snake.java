package com.eryk.snake.model;

import java.util.LinkedList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Snake {
	private static float VELOCITY;
	private static float snakeBodySize;
	private static float growth;
	private static boolean movingX = false;
	private static boolean grows = false;
	private static int score = 0;
	
	public enum Dir { UP, DOWN, LEFT, RIGHT };
	
	//cialo weza
	private LinkedList<Rectangle> snakeBody;
	//kolejne kierunki ciala weza
	private LinkedList<Dir> snakeDir;
	
	public Snake(Vector2 snakePos, float vel, float length, float size, float growth) {
		this.snakeBody = new LinkedList<Rectangle>();
		this.snakeDir = new LinkedList<Dir>();
		Snake.VELOCITY = vel;
		Snake.snakeBodySize = size;
		Snake.growth = growth;
		
		snakeBody.offer(new Rectangle(snakePos.x,snakePos.y,length,snakeBodySize));
		snakeDir.offer(Dir.RIGHT);
		movingX = true;
	}
	
	public LinkedList<Rectangle> getSnakeShape() {
		return snakeBody;
	}

	public LinkedList<Rectangle> getSnakeBodyList() {
		return snakeBody;
	}
	
	public LinkedList<Dir> getSnakeDirList() {
		return snakeDir;
	}
	
	public float getBodySize() {
		return snakeBodySize;
	}
	
	public float getVelocity() {
		return VELOCITY;
	}
	
	public void boostVelocity() {
		VELOCITY += 2f;
	}
	
	public boolean isMovingX() {
		return movingX;
	}
	
	public void setMovingX(boolean set) {
		movingX = set;
	}
	
	public boolean isGrowing() {
		return grows;
	}
	
	public void setGrowing(boolean set) {
		grows = set;
	}
	
	public float getGrowth() {
		return growth;
	}
	
	public int retScore() {
		return score;
	}
	
	public void incScore() {
		score++;
	}
}