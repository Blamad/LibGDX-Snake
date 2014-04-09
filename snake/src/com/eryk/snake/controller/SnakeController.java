package com.eryk.snake.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.eryk.snake.model.Bait;
import com.eryk.snake.model.Snake;
import com.eryk.snake.model.Snake.Dir;
import com.eryk.snake.model.World;

public class SnakeController {

	enum Keys { UP, DOWN, LEFT, RIGHT }
	
	static Map<Keys, Boolean> keys = new HashMap<SnakeController.Keys, Boolean>(); 
	static {
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
	}
	
	private Snake snake;
	private LinkedList<Rectangle> snakeBody;
	private LinkedList<Dir> snakeDir;
	private float snakeBodySize;
	private float snakeGrowth;
	private float snakeCurrGrowth = 0;
	private Bait bait;
	private float worldWidth, worldHeight;
	
	public SnakeController(World world, float width, float height) {
		this.snake = world.getSnake();
		this.snakeBody = snake.getSnakeBodyList();
		this.snakeDir = snake.getSnakeDirList();
		this.snakeBodySize = snake.getBodySize();
		this.snakeGrowth = snake.getGrowth();
		this.bait = world.getBait();
		this.worldWidth = width;
		this.worldHeight = height;
	}
	
	public boolean update(float delta) {
		processInput();
		
		move(delta);
		if(bait.isSpawned())
			collidedWithBait();
		return collidedWithSelf() || outOfBorder();
	}
	
	private boolean outOfBorder() {
		Rectangle tmp = snakeBody.peekLast();
		if(tmp.x <= 0 || tmp.y <= 0 || tmp.x + tmp.width >= worldWidth || tmp.y + tmp.height >= worldHeight)
			return true;
		return false;
	}
	
	private boolean collidedWithSelf() {
		for(int i = 0; i < snakeBody.size() - 2; i++) {
			if(snakeBody.get(i).overlaps(snakeBody.peekLast()))
				return true;
		}
		return false;
	}
	
	private void collidedWithBait() {
		if(snakeBody.peekLast().overlaps(bait)) {
			snake.setGrowing(true);
			snake.boostVelocity();
			snake.incScore();
		}
	}

	/**
	 * Zamiast linii naszego snake'a bêdzie reprezentowa³a kolekcja rectangle'ów.
	 * 4 ruchy g³owy:
	 * -UP: zwiêkszamy tylko height
	 * -DOWN: zwiêkszamy height i zmniejszamy Y o delte
	 * -LEFT: zwiêkszamy width i zmniejszamy X o delte
	 * -RIGHT: zwiêkszamy tylko width
	 * 
	 * 4 ruchy ogona:
	 * -UP: zmniejszamy height i zwiêkszmay Y o delte
	 * -DOWN: zmniejszamy tylko height
	 * -LEFT: zmniejszamy tylko width
	 * -RIGHT: zmniejszamy width i zwiêkszamy X o delte
	 * 
	 * W razie gdy liczba el. = 1, specjalna obs³uga:
	 * -UP: zwiêkszamy tylko Y
	 * -DOWN: zmniejszamy tylko Y
	 * -LEFT: zmniejszamy tylko X
	 * -RIGHT: zwiêkszmay tylko X
	 * 
	 * Kiedy przejœæ do natêpnego recta? 
	 * Gdy proponowane width lub height < 0.
	 * 
	 * Nowy rect:
	 * ZAWSZE NA SIEBIE ZACHODZ¥!
	 * Id¹c po X:	je¿eli ruch byl w RIGHT:
	 * 					-UP: x = pos.x + width - snakeBody; y = pos.y
	 * 					-DOWN: x = pos.x + width - snakeBody; y = pos.y + snakeBody
	 * 				jezeli ruch byl w LEFT:
	 * 					-UP:  x = pos.x; y = pos.y
	 * 					-DOWN: x = pos.x; y = pos.y + snakeBody
	 * Idac po Y:	jezeli ruch byl w UP:
	 * 					-LEFT: x = pos.x; y = pos.y + height - snakeBody
	 * 					-RIGHT: x = pos.x; y = pos.y + height - snakeBody
	 * 				jezeli ruch byl w DOWN:
	 * 					-LEFT: x = pos.x; y = pos.y
	 * 					-RIGHT: x = pos.x; y = pos.y
	 * 
	 * Warunek skasowania recta z ogona:
	 * zmieniana wartoœæ musi byæ mniejsza od snakeBody
	 * 
	 * Jak ustaliæ pocz¹tek kolejnego recta? 
	 * Pobraæ x,y poprzedniego i w potrzebie zwiêkszyæ o width/height odpowiedni parametr.
	 * + aspekty estetyki (ci¹g³oœæ wê¿a)
	 * 
	 * Jak to wygl¹da w liœcie? 
	 * peek.first() pierwszy element listy to zawsze koniec ogona wê¿a
	 * peek.last() ostatni element listy to zawsze g³owa wê¿a
	 */
	
	
	public void move(float delta) {
		delta *= snake.getVelocity();
		
		if(snake.isGrowing()) {
			snakeCurrGrowth += delta;
			if(snakeGrowth < snakeCurrGrowth) {
				snakeCurrGrowth = 0;
				snake.setGrowing(false);
			}
		}
		
		if(snakeBody.size() != 1) {
			/** obsluga glowy:
			  * 4 ruchy g³owy:
			  * -UP: zwiêkszamy tylko height
			  * -DOWN: zwiêkszamy height i zmniejszamy Y o delte
			  * -LEFT: zwiêkszamy width i zmniejszamy X o delte
			  * -RIGHT: zwiêkszamy tylko width
			  */
			switch (snakeDir.peekLast()) {
			case UP:
				snakeBody.peekLast().height += delta;
				break;
			case DOWN:
				snakeBody.peekLast().height += delta;
				snakeBody.peekLast().y -= delta;
				break;
			case LEFT:
				snakeBody.peekLast().width += delta;
				snakeBody.peekLast().x -= delta;
				break;
			case RIGHT:
				snakeBody.peekLast().width += delta;
				break;
			}
			
			/** 4 ruchy ogona:
			 * -UP: zmniejszamy height i zwiêkszmay Y o delte
			 * -DOWN: zmniejszamy tylko height
			 * -LEFT: zmniejszamy tylko width
			 * -RIGHT: zmniejszamy width i zwiêkszamy X o delte
			 */
			if(!snake.isGrowing()) {
				switch (snakeDir.peek()) {
				case UP:
					if(snakeBody.peek().height - delta <= snakeBodySize) {
						delta = snakeBodySize + delta - snakeBody.peek().height;
						snakeBody.poll();
						snakeDir.poll();
					} else {
						snakeBody.peek().height -= delta;
						snakeBody.peek().y += delta;
					}
					break;
				case DOWN:
					if(snakeBody.peek().height - delta <= snakeBodySize) {
						delta = snakeBodySize + delta - snakeBody.peek().height;
						snakeBody.poll();
						snakeDir.poll();
					} else {
						snakeBody.peek().height -= delta;
					}
					break;
				case LEFT:
					if(snakeBody.peek().width - delta <= snakeBodySize) {
						delta = snakeBodySize + delta - snakeBody.peek().width;
						snakeBody.poll();
						snakeDir.poll();
					} else {
						snakeBody.peek().width -= delta; 
					}
					break;
				case RIGHT:
					if(snakeBody.peek().width - delta <= snakeBodySize) {
						delta = snakeBodySize + delta - snakeBody.peek().height;
						snakeBody.poll();
						snakeDir.poll();
					} else {
						snakeBody.peek().width -= delta;
						snakeBody.peek().x += delta; 
					}
					break;
				}
		}
			
			
		} else {
			//w przypadku gdy snake jest 1-segmentowy
			switch (snakeDir.peek()) {
			case UP:
				if(snake.isGrowing())
					snakeBody.peek().height += delta;
				else
					snakeBody.peek().y += delta;
				break;
			case DOWN:
				if(snake.isGrowing())
					snakeBody.peek().height += delta;
				snakeBody.peek().y -= delta;
				break;
			case LEFT:
				if(snake.isGrowing())
					snakeBody.peek().width += delta;
				snakeBody.peek().x -= delta;
				break;
			case RIGHT:
				if(snake.isGrowing())
					snakeBody.peek().width += delta;
				else
					snakeBody.peek().x += delta;
				break;
			}
		}
	}
	
	public void changeDirectory(Dir movDemand) {
		
		if(snakeBody.peekLast().width <= snakeBodySize*2+1 && snakeBody.peekLast().height <= snakeBodySize*2+1)
			return;
		/** Nowy rect:
			 * ZAWSZE NA SIEBIE ZACHODZ¥!
			 * 	je¿eli ruch byl w RIGHT:
			 * 					-UP: x = pos.x + width - snakeBody; y = pos.y
			 * 					-DOWN: x = pos.x + width - snakeBody; y = pos.y + snakeBody
			 * 	jezeli ruch byl w LEFT:
			 * 					-UP:  x = pos.x; y = pos.y
			 * 					-DOWN: x = pos.x; y = pos.y + snakeBody
			 * 	jezeli ruch byl w UP:
			 * 					-LEFT: x = pos.x - snakeBody; y = pos.y + height - snakeBody
			 * 					-RIGHT: x = pos.x; y = pos.y + height - snakeBody
			 * 	jezeli ruch byl w DOWN:
			 * 					-LEFT: x = pos.x; y = pos.y
			 * 					-RIGHT: x = pos.x; y = pos.y
			 */
		//pierwszy element listy to najdalszy ogon, ostatni el. to glowa
		if(!snake.isMovingX() && (movDemand == Dir.LEFT || movDemand == Dir.RIGHT)) {
			Rectangle head = snakeBody.peekLast();
			Rectangle rect = new Rectangle();
			snake.setMovingX(true);
			if(snakeDir.peekLast() == Dir.UP) {
				if(movDemand == Dir.LEFT)
					rect.set(head.x, head.y + head.height - snakeBodySize, snakeBodySize, snakeBodySize);
				else
					rect.set(head.x, head.y + head.height - snakeBodySize, snakeBodySize, snakeBodySize);
			} else {
					if(movDemand == Dir.LEFT)
						rect.set(head.x, head.y, snakeBodySize, snakeBodySize);
					else
						rect.set(head.x, head.y, snakeBodySize, snakeBodySize);
			}
			snakeBody.offer(rect);
			snakeDir.offer(movDemand);
			
		} else if(snake.isMovingX() && (movDemand == Dir.UP || movDemand == Dir.DOWN)) {
			Rectangle head = snakeBody.peekLast();
			Rectangle rect = new Rectangle();
			snake.setMovingX(false);
			if(snakeDir.peekLast() == Dir.RIGHT) {
				if(movDemand == Dir.UP)
					rect.set(head.x + head.width - snakeBodySize, head.y, snakeBodySize, snakeBodySize);
				else
					rect.set(head.x + head.width - snakeBodySize, head.y, snakeBodySize, snakeBodySize);
			} else {
				if(movDemand == Dir.UP)
					rect.set(head.x, head.y, snakeBodySize, snakeBodySize);
				else
					rect.set(head.x, head.y, snakeBodySize, snakeBodySize);
			}
			snakeBody.offer(rect);
			snakeDir.offer(movDemand);
		}
	}
	
	
	private void processInput() {
		if(keys.get(Keys.UP)) {
			changeDirectory(Dir.UP);
			return;
		}
		if(keys.get(Keys.DOWN)) {
			changeDirectory(Dir.DOWN);
			return;
		}
		if(keys.get(Keys.LEFT)) {
			changeDirectory(Dir.LEFT);
			return;
		}
		if(keys.get(Keys.RIGHT)) {
			changeDirectory(Dir.RIGHT);
			return;
		}
	}
	
	public void upPressed() {
		keys.get(keys.put(Keys.UP,true));
	}
	
	public void upReleased() {
		keys.get(keys.put(Keys.UP, false));
	}
	
	public void downPressed() {
		keys.get(keys.put(Keys.DOWN, true));
	}
	
	public void downReleased() {
		keys.get(keys.put(Keys.DOWN, false));
	}
	
	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}
	
	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}
	
	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}
	
}
