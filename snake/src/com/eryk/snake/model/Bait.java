package com.eryk.snake.model;

import com.badlogic.gdx.math.Rectangle;

public class Bait extends Rectangle {
	
	private static final long serialVersionUID = 1L;
	
	private boolean spawned = false;
	
	public Bait(float baitSize) {
		super();
		this.width = baitSize;
		this.height = baitSize;
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isSpawned() {
		return spawned;
	}
	
	public void setSpawned(boolean set) {
		spawned = set;
	}
}
