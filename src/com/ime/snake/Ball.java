/* 
 *
 * Author : Rejith
 * Version 1.0
 *
 */
package com.ime.snake;

import java.util.Random;

public class Ball {
	private Point center;
	private int color;
	private int size;
	private int points;
	private boolean isSpecial;
	private int length;
	private int collisionDistance;

	public int getCollisionDistance() {
		return collisionDistance;
	}

	public void setCollisionDistance() {
		this.collisionDistance = this.size + Game.rectSize + 3;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	void createRandomBall(Game game) {
		float x = Math.abs(new Random().nextInt() % (game.canvasX - 20));
		float y = Math.abs(new Random().nextInt() % (game.canvasY - 125));
		if (this.getIsSpecial()) {
			Ball r = game.regular;
			if (Math.abs(x - r.getCenter().x) < 20
					&& Math.abs(y - r.getCenter().y) < 20) {
				x = x + this.getSize()*2 ;
				y = y + this.getSize()*2;
			}
		}
		x = x - x% game.gapBetweenRects;
		y = y - y% game.gapBetweenRects;
		if(game.points.contains(new Point(x,y)))
		{
			x = x + 8;
			y = y + 8;
		}
		if (x < 10)
			x = x + 16;
		if (y < 110)
			y = y + 112;
		this.setCenter(new Point(x, y));
	}

	public boolean catchBall(float x, float y) {
		if ((Math.abs(x - this.getCenter().x) < collisionDistance)
				&& (Math.abs(y - this.getCenter().y) < collisionDistance)) {

			return true;

		} else
			return false;
	}

}
