/* 
 *
 * Author : Rejith
 * Version 1.0
 *
 */

package com.ime.snake;

import com.ime.snake.R;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;

public class Snake extends Activity {
	Activity a = this;
	Game game = null;
	int level = 2;
	int speed = 100;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			game.reDraw();
		}
	};
	AtomicBoolean isRunning = new AtomicBoolean(false);

	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.main);
		game = new Game(this);
		setContentView(game);
		game.requestFocus();
		
	}

	public void onStart() {
		super.onStart();

		Thread background = new Thread(new Runnable() {
			public void run() {
				game.requestFocus();
				try {
					while (true) {
						Thread.sleep(speed);
						handler.sendMessage(handler.obtainMessage());
					}
				} catch (Throwable t) {
				}
			}
		});

		isRunning.set(true);
		background.start();
	}

	public void onStop() {
		super.onStop();
		isRunning.set(false);
		handler = null;
		this.finish();

		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	    	handler = null;
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}


}