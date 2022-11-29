package com.mygdx.game.view;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.model.Bait;
import com.mygdx.game.model.World;

public class Renderer {
		
	private static World world;
	private OrthographicCamera cam;
	private ShapeRenderer renderer = new ShapeRenderer();
	private LinkedList<Rectangle> snakeRects;
	private Iterator<Rectangle> it;
	private Bait bait;
	private float worldWidth, worldHeight;
	
	private SpriteBatch spriteBatch;
	private BitmapFont font = new BitmapFont();
	
	public Renderer(World world, float width, float height) {
		Renderer.world = world;
		worldWidth = width;
		worldHeight = height;
		this.cam = new OrthographicCamera(width, height);
		this.cam.position.set((width/2), (height/2), 0);
		this.cam.update();
		snakeRects = world.getSnake().getSnakeShape();
		bait = world.getBait();
		
		spriteBatch = new SpriteBatch();
	}
	
	public void renderFail() {
		spriteBatch.begin();
		font.draw(spriteBatch, "    Score: "+ world.getSnake().retScore() +"\nGAME OVER!", worldWidth/2 - 40 , worldHeight/2 + 20);
		spriteBatch.end();
	}
	
	public void renderReady() {
		spriteBatch.begin();
		font.draw(spriteBatch, "Touch to begin!", worldWidth/2 - 50, worldHeight/2 + 20);
		spriteBatch.end();
	}
	
	public void render() {
		drawSnake();
		if(bait.isSpawned())
			drawBait();
	}
	
	private void drawSnake() {
		Rectangle rect;
		renderer.setProjectionMatrix(cam.combined);
		renderer.begin(ShapeType.Filled);
		renderer.setColor(new Color(1,1,1,1));
		it = snakeRects.iterator();
		while(it.hasNext()){
			rect = it.next();
			renderer.rect(rect.x, rect.y, rect.width, rect.height);
		}
		renderer.end();
	}
	
	private void drawBait() {
		renderer.setProjectionMatrix(cam.combined);
		renderer.begin(ShapeType.Filled);
		renderer.setColor(new Color(1,1,1,1));
		renderer.rect(bait.x, bait.y, bait.width, bait.height);
		renderer.end();
	}
}
