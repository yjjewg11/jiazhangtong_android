package com.wj.kindergarten.ui.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tangt on 2015/11/19.
 */
public class ManDrawLine extends View {
    private  LeftRunnable lefeRun;
    private  RightRunnable rightRun;
    private Paint paint;
    private  ThreadPoolExecutor threadPoolExecutor;
    //画单元的宽度
    private int drawWidth;

    //执行动画的时间,默认200ms
    private long time = 200;
    //画的水平位置
    private int location ;
    //指定画的颜色
    private int color;
    private int fromX;
    private int toX;

    public void setColor(int color) {
        this.color = color;
    }

    public void setDrawWidth(int drawWidth) {
        this.drawWidth = drawWidth;
    }

    public void setLocation(int location) {
        this.location = location;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(fromX,0,toX,3,paint);
        super.onDraw(canvas);
    }

    public ManDrawLine(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.parseColor("#ff4966"));
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();
        threadPoolExecutor = new ThreadPoolExecutor(1,1,1000*60, TimeUnit.MILLISECONDS,queue);
        lefeRun = new LeftRunnable();
        rightRun =  new RightRunnable();
    }

    public ManDrawLine(Context context, AttributeSet attrs) {
        super(context, attrs);
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
            synchronized (ManDrawLine.this){
                try {
                    int i = 0;
                    while(i <= 10){
                        Thread.sleep(time/10);
                        fromX-=location/10;
                        toX-=location/10;
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
            synchronized (ManDrawLine.this){
                try {
                    int i = 0;
                    while(i <= 10){
                        Thread.sleep(time/10);
                        fromX+=location/10;
                        toX+=location/10;
                        postInvalidate();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
