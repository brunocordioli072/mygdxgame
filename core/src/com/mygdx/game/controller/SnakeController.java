package com.mygdx.game.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.model.Bait;
import com.mygdx.game.model.Snake;
import com.mygdx.game.model.Snake.Dir;
import com.mygdx.game.model.World;

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
