package com.wj.kindergarten.ui.more;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tangt on 2016/3/27.
 */
public class MyCircleView extends View {
    private Paint paintNor;
    private Paint paintSingle;

    public MyCircleView(Context context) {
        super(context);
    }

    public MyCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintNor = new Paint();
        paintNor.setColor(Color.parseColor("#ffffff"));
        paintSingle = new Paint();
        paintSingle.setColor(Color.parseColor("#ff4966"));
    }
    int count;
    public void setCount(int count){
        this.count = count;
    }

    float scale;
    public void setScale(float scale){
        this.scale = scale;
        invalidate();
    }

    boolean init;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintNor.setColor(Color.parseColor("#ffffff"));
            canvas.drawCircle(15,radius+10,radius, paintNor);
            canvas.drawCircle(95,radius+10,radius, paintNor);
            canvas.drawCircle(175,radius+10,radius, paintNor);
        canvas.drawCircle(15 + 80 * scale, radius+10, radius, paintSingle);
        setPadding(15,15,15,15);
    }
    int radius = 15;

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode == MeasureSpec.EXACTLY){

        }else if(widthMode == MeasureSpec.AT_MOST){
            widthSize = getPaddingLeft()+175+getPaddingRight();
        }
        if(heightMode == MeasureSpec.EXACTLY){

        }else if(heightMode == MeasureSpec.AT_MOST){
            heightSize = getPaddingTop()+(radius+10)+getPaddingBottom();
        }
        setMeasuredDimension(widthSize,heightSize);
    }
}
