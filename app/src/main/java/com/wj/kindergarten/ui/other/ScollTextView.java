package com.wj.kindergarten.ui.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/11/13.
 */
public class ScollTextView extends View {
	private Paint paint ;
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
	private ThreadPoolExecutor threadPoolExecutor;
	private LeftRunnable lefeRun;
	private RightRunnable rightRun;
	//width为画的宽度
	private int width;
	//动画的执行默认时间
	private long duration = 500;

	private int onWindowHeight ;

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public ScollTextView(Context context) {
		super(context);
		paint = new Paint();
		paint.setColor(Color.parseColor("#ff4966"));
		BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();
		threadPoolExecutor = new ThreadPoolExecutor(1,1,1000*60, TimeUnit.MILLISECONDS,queue);
		lefeRun = new LeftRunnable();
		rightRun =  new RightRunnable();

	}
	public void setMyWidth(int width,int onWindowHeight){
		this.width = width;
		this.onWindowHeight = onWindowHeight;
		fromX = 0;
		fromY = 0;
		toX = width;
		toY = 3;
	}

	public ScollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRect(fromX,fromY,toX,toY,paint);
		super.onDraw(canvas);
	}

	public void drawLeft(){
		threadPoolExecutor.execute(lefeRun);
	}

	public void drawRight(){
		threadPoolExecutor.execute(rightRun);
	}

	class LeftRunnable implements Runnable {
		@Override
		public void run() {
			synchronized (ScollTextView.this){
				try {
					while(fromX > 0){
						Thread.sleep(duration/10);
						fromX-=width/10;
						toX-=width/10;
						postInvalidate();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class RightRunnable implements Runnable {
		@Override
		public void run() {
			synchronized (ScollTextView.this){
				try {
					while(fromX < width){
						Thread.sleep(duration/10);
						fromX+=width/10;
						toX+=width/10;
						postInvalidate();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
