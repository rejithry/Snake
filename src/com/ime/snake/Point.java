/* 
 *
 * Author : Rejith
 * Version 1.0
 *
 */
package com.ime.snake;

public class Point {
	public float x, y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public boolean equals(Object p) {
		if (((Point) p).x == this.x && ((Point) p).y == this.y)
			return true;
		else
			return false;
	}

}
