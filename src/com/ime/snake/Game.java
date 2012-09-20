/* 
 *
 * Author : Rejith
 * Version 1.0
 *
 */
package com.ime.snake;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;

public class Game extends View implements OnKeyListener, OnTouchListener {

	enum Direction {
		LEFT, RIGHT, UP, DOWN
	};

	enum Activity {
		MOVE, GAMEOVER
	};

	static int rectSize = 3;
	
	boolean specialBallActive;
	int specialBallLife;
	int canvasX,canvasY;
	List<Point> points;
	Ball regular, special;
	ArrayList<Ball> balls;
	float farRightX, farRightY;
	int point = 0;
	int level = 1;
	ArrayList<Destroyer> destroyers;
	Destroyer des1, des2, des3, des4;

	float rectCurv;
	int initSize;
	int initYPos;
	int gapBetweenRects;
	int regularPoints;

	int catchCount;
	int initTotal;
	int xDirection;
	int yDirection;
	Direction direction;
	Activity activity;

	boolean isGameOver;
	int gameOverTimeout;

	
	float snakeHeadX, snakeHeadY ,clickX, clickY;
	String dbg;
	int dbgcnt;

	Paint paint = new Paint();

	public Game(Context context) {
		super(context);
		
		specialBallActive = false;
		specialBallLife = 0;
		canvasX = 400;
		canvasY = 400;
		points = new ArrayList<Point>();


		balls = new ArrayList<Ball>();
		point = 0;
		level = 1;
		destroyers = new ArrayList<Destroyer>();

		rectCurv = 1.0f;
		initSize = 3;
		initYPos = 152;
		gapBetweenRects = 2 * rectSize + 2;
		regularPoints = 1;

		catchCount = 0;
		initTotal = (initSize * gapBetweenRects) + 9;
		xDirection = 1;
		yDirection = 0;
		direction = Direction.RIGHT;
		activity = Activity.MOVE;

		isGameOver = false;
		gameOverTimeout = 0;


		dbg="";
		
		setFocusable(true);
		for (int i = gapBetweenRects; i < initTotal;) {
			points.add(new Point(i, initYPos));
			i = i + gapBetweenRects;
		}
		farRightX = points.get(points.size() - 1).x;
		farRightY = points.get(points.size() - 1).y;

		regular = new Ball();
		special = new Ball();
		special.setIsSpecial(true);

		regular.createRandomBall(this);
		regular.setColor(Color.BLUE);
		regular.setLength(1);
		regular.setPoints(3);
		regular.setSize(10);
		regular.setIsSpecial(false);
		regular.setCollisionDistance();
		balls.add(regular);

		special.setColor(Color.YELLOW);
		special.setLength(2);
		special.setPoints(5);
		special.setSize(15);
		special.setIsSpecial(true);
		special.createRandomBall(this);
		special.setCollisionDistance();

		balls.add(special);


		des1 = new Destroyer(new Point(176,256),30,Direction.DOWN);
		destroyers.add(des1);
		des3 = new Destroyer(new Point(320, 256),30,Direction.DOWN);
		destroyers.add(des3);

		
		//setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		this.setOnKeyListener(this);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3.0f);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (specialBallActive)
			specialBallLife++;
		if (specialBallActive && (specialBallLife > 100)) {
			specialBallLife = 0;
			specialBallActive = false;
		}
		

		setBackgroundColor(Color.BLACK);
		canvasX = canvas.getWidth();
		canvasY = canvas.getHeight() - 20;
		paint.setColor(Color.argb(1, 255, 220, 220));
		canvas.drawRect(new RectF(0, 100, canvasX, canvasY - 57), paint);
		paint.setColor(Color.GREEN);
		canvas.drawLine(0, 100, canvasX, 100, paint);
		canvas.drawLine(0, canvasY - 57, canvasX, canvasY - 57, paint);
		canvas.drawLine(0, 100, 0, canvasY - 57, paint);
		canvas.drawLine(canvasX, 100, canvasX, canvasY - 57, paint);

		paint.setStrokeWidth(3.0f);
		paint.setColor(Color.LTGRAY);
		for (Point p : points) {
			canvas.drawRoundRect(new RectF(p.x - rectSize, p.y - rectSize, p.x
					+ rectSize, p.y + rectSize), rectCurv, rectCurv, paint);
		}

		paint.setColor(regular.getColor());
		canvas.drawCircle(regular.getCenter().x, regular.getCenter().y, regular
				.getSize(), paint);
		if (specialBallActive) {
			paint.setColor(special.getColor());
			canvas.drawCircle(special.getCenter().x, special.getCenter().y,
					special.getSize(), paint);

		}
		for (Destroyer d : destroyers){
			d.Draw(canvas,paint);
		}
		if(isGameOver){
			paint.setTextSize(30);
			paint.setColor(Color.RED);
			canvas.drawText("GAME OVER", 50, 300, paint);
		}
		paint.setColor(Color.MAGENTA);
		paint.setTextSize(25);
		canvas.drawText(Integer.toString(point), 250, 60, paint);
		paint.setTextSize(40);
		canvas.drawText("SNAKE", 50, 60, paint);
		paint.setTextSize(20);
		canvas.drawText(Integer.toString(initSize), 600, 450, paint);
		
	   
	}

	void reDraw() {

		level = 2;
		farRightX = points.get(points.size() - 1).x;
		farRightY = points.get(points.size() - 1).y;

		if (activity == Activity.MOVE) {
			growSnake(farRightX, farRightY, 1, regularPoints, false);
			points.remove(0);
			if (activity == Activity.GAMEOVER){
			    this.invalidate();
			}

		}

		if (regular.catchBall(farRightX, farRightY)) {
			catchCount++;
			growSnake(farRightX, farRightY, regular.getLength(), regular
					.getPoints(), true);
			regular.createRandomBall(this);
			if ((catchCount % 5 == 0) && catchCount > 0) {
				special.createRandomBall(this);
				specialBallActive = true;
				specialBallLife = 0;
			}
		}
		if (specialBallActive && special.catchBall(farRightX, farRightY)) {
			growSnake(farRightX, farRightY, special.getLength(), special
					.getPoints(), true);
			specialBallLife = 0;
			specialBallActive = false;
		}
		
		if (activity == Activity.MOVE){
			this.invalidate();
		}
		else if (activity == Activity.GAMEOVER) {
			gameOverTimeout++;
			if (gameOverTimeout > 20) {
				gameOver();
			}

		} else {
		}
	}

	public Point findNewPoint(float x, float y, int i) {
		float newX, newY;
		newX = x + (xDirection * ((i + 1) * gapBetweenRects));
		newY = y + (yDirection * ((i + 1) * gapBetweenRects));
		if (newX > (canvasX - gapBetweenRects))
			newX = gapBetweenRects;
		if (newY > (canvasY - 62))
			newY = 112 - (112 % gapBetweenRects);
		if (newX < gapBetweenRects)
			newX = (canvasX - gapBetweenRects)
					- ((canvasX - gapBetweenRects) % gapBetweenRects);
		if (newY < 105)
			newY = (canvasY - 61) - ((canvasY - 61) % gapBetweenRects);
		return new Point(newX, newY);
	}

	public void growSnake(float x, float y, int l, int p, boolean grow) {
		for (int i = 0; i < l; i++) {
			Point pnt = findNewPoint(x, y, i);
			if (points.contains(pnt) || Destroyer.getDestroyerCoords().contains(new Point(x,y))) {
				isGameOver = true;
				activity = Activity.GAMEOVER;
			} else {
				points.add(pnt);
				farRightX = farRightX + (xDirection) * gapBetweenRects;
				farRightY = farRightY + (yDirection) * gapBetweenRects;
				if (grow) {
					point = point + p;
				}
			}
		}

	}

	public void gameOver() {
		points = null;
		points = new ArrayList<Point>();

		direction = Direction.RIGHT;
		for (int i = gapBetweenRects; i < initTotal;) {
			points.add(new Point(i, initYPos));
			i = i + gapBetweenRects;
		}
		regular.createRandomBall(this);
		point = 0;
		specialBallLife = 0;
		specialBallActive = false;
		catchCount = 0;
		xDirection = 1;
		yDirection = 0;
		farRightX = 0;
		farRightY = 0;
		activity = Activity.MOVE;
		gameOverTimeout = 0;
		isGameOver = false;

	}

	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		Direction prevDirection = direction;
		xDirection = 0;
		yDirection = 0;
		if (arg1 == KeyEvent.KEYCODE_W) {
			if ((direction == Direction.LEFT) || (direction == Direction.RIGHT))
				direction = Direction.UP;
		} else if (arg1 == KeyEvent.KEYCODE_X) {
			if ((direction == Direction.LEFT) || (direction == Direction.RIGHT))
				direction = Direction.DOWN;
		} else if (arg1 == KeyEvent.KEYCODE_A) {
			if ((direction == Direction.UP) || (direction == Direction.DOWN))
				direction = Direction.LEFT;
		} else if (arg1 == KeyEvent.KEYCODE_D) {
			if ((direction == Direction.UP) || (direction == Direction.DOWN))
				direction = Direction.RIGHT;
		} else if (arg1 == KeyEvent.KEYCODE_MENU) {
			Intent i = new Intent(this.getContext(), Menu.class);
			this.getContext().startActivity(i);
		} else {

		}
		if (direction == Direction.DOWN)
			yDirection = 1;
		else if (direction == Direction.UP)
			yDirection = -1;
		else if (direction == Direction.RIGHT)
			xDirection = 1;
		else
			xDirection = -1;
		if (!prevDirection.equals(direction))
			reDraw();
		return true;
	}

	public boolean onTouch(View v, MotionEvent event) {
		Direction prevDirection = direction;
		xDirection = 0;
		yDirection = 0;
		
		snakeHeadX = points.get(points.size() -1).x;
		clickX = event.getX();
		snakeHeadY = points.get(points.size() -1).y;
		clickY =  event.getY();
		
		if ((direction == Direction.LEFT) || (direction == Direction.RIGHT)) {
			if (clickY > snakeHeadY + 50)
				direction = Direction.DOWN;
			else if (clickY < snakeHeadY + 50)
				direction = Direction.UP;

		} else if ((direction == Direction.UP) || (direction == Direction.DOWN)) {
			if (clickX > snakeHeadX + 50)
				direction = Direction.RIGHT;
			else if (clickX < snakeHeadX + 50)
				direction = Direction.LEFT;

		} else {

		}

		if (direction == Direction.DOWN)
			yDirection = 1;
		else if (direction == Direction.UP)
			yDirection = -1;
		else if (direction == Direction.RIGHT)
			xDirection = 1;
		else
			xDirection = -1;
		if (!prevDirection.equals(direction))
			reDraw();
		dbg = direction.toString();
		dbgcnt++ ;
		return super.onTouchEvent(event);
	}


}
