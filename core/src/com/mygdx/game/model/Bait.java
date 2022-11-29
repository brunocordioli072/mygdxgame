package com.mygdx.game.model;

import com.badlogic.gdx.math.Rectangle;

public class Bait extends Rectangle {
	private boolean spawned = false;
	
	public Bait(float baitSize) {
		super();
		this.width = baitSize;
		this.height = baitSize;
	}

	public boolean isSpawned() {
		return spawned;
	}
	
	public void setSpawned(boolean set) {
		spawned = set;
	}
}
