/* 
 *
 * Author : Rejith
 * Version 1.0
 *
 */
package com.ime.snake;


import java.util.ArrayList;

import com.ime.snake.Game.Direction;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Destroyer {
	private ArrayList<Point> points;
	public ArrayList<Point> getPoints() {
		return points;
	}


	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}


	private Point start;
	private int length;
	Direction dir;

	static ArrayList<Point> destroyerCoords;
	static{
		destroyerCoords =  new ArrayList<Point>();
	}
	
	public static ArrayList<Point> getDestroyerCoords() {
		return destroyerCoords;
	}


	Destroyer( Point s,  int l, Direction d){
		this.start = s;
		this.length = l;
		this.dir = d;
		int xDirection = 0;
		int yDirection =0 ;
		if (dir == Direction.DOWN)
			yDirection = 1;
		else if (dir == Direction.UP)
			yDirection = -1;
		else if (dir == Direction.RIGHT)
			xDirection = 1;
		else
			xDirection = -1;
		points = new ArrayList<Point>();
		for (int i =0 ; i< length; i++){
				points.add(new Point((i+1)*xDirection*8 + start.x, (i+1)*yDirection*8 + start.y ));
				destroyerCoords.add(new Point((i+1)*xDirection*8 + start.x, (i+1)*yDirection*8 + start.y ));
		}
	}

	
	void createRandomDestroyer() {

	}


	public void Draw(Canvas canvas, Paint paint) {
		int rectSize = 3;
		paint.setColor(Color.RED);
		for (Point p : points) {
		canvas.drawRoundRect(new RectF(p.x - rectSize, p.y - rectSize, p.x
				+ rectSize, p.y + rectSize), 1, 1, paint);
		}
	}



}
