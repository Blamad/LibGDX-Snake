package com.eryk.snake.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.eryk.snake.model.Bait;
import com.eryk.snake.model.World;

public class BaitController {
	
	private float worldWidth, worldHeight;
	private float collisionTime;
	private Bait bait;
	private LinkedList<Rectangle> snakeRects;
	private boolean spawnIt = true;
	
	public BaitController(World world, float width, float height) {
		worldWidth = width;
		worldHeight = height;
		snakeRects = world.getSnake().getSnakeBodyList();
		bait = world.getBait();
	}
	
	public void update() {
		if(bait.isSpawned()) {
			checkCollision();
		} else if(spawnIt) {
			spawnTry();
		} else {
			if((System.nanoTime() - collisionTime)/1000 > (float)2000000 ) {
				spawnIt = true;
			}
		}
	}
	
	private void checkCollision() {
		if(snakeRects.peekLast().overlaps(bait)) {
			collisionTime = System.nanoTime();
			bait.setSpawned(false);
		}
	}
	
	private void spawnTry() {
		Random rn = new Random();
		bait.setX(rn.nextInt((int)(worldWidth-bait.getWidth())));
		bait.setY(rn.nextInt((int)(worldHeight-bait.getHeight())));
		Iterator<Rectangle> it = snakeRects.iterator();
		while(it.hasNext()) {
			if(it.next().overlaps(bait))
				return;
		}
		spawnIt = false;
		bait.setSpawned(true);
	}
}
