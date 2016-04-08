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

    int count = 3;

    public void setCount(int count) {
        this.count = count;
        invalidate();
    }

    float scale;

    public void setScale(float scale) {
        this.scale = scale;
        invalidate();
    }
    int padlet = 15;
    int padrig = 15;
    public void setPad(int left,int right){
        padlet = left;
        padrig = right;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintNor.setColor(Color.parseColor("#ffffff"));
        for(int i = 0 ; i < count ; i++){
            canvas.drawCircle(15+distance*i, radius + 10, radius, paintNor);
        }
        canvas.drawCircle(15 + distance * scale, radius + 10, radius, paintSingle);
        setPadding(padlet, 15, padrig, 15);
    }
    int distance = 80;

    public void setDistance(int distance) {
        this.distance = distance;
        invalidate();
    }

    int radius = 15;

    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {

        } else if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = getPaddingLeft() + count*distance + getPaddingRight();
        }
        if (heightMode == MeasureSpec.EXACTLY) {

        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = getPaddingTop() + (radius + 10) + getPaddingBottom();
        }
        setMeasuredDimension(widthSize, heightSize);
    }
}
