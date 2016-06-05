package com.wj.kindergarten.ui.other;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.wenjie.jiazhangtong.R;

/**
 * 圆形或者圆角图片
 * Created by liujigang on 2015/4/24 0024.
 */
public class CircleImageView extends ImageView {

    private int mRadius;
    private Paint mBitmapPaint;
    private Matrix mMatrix;
    private boolean mAsCircle;
    private int mCornerRadius;
    private boolean mTopLeft;
    private boolean mTopRight;
    private boolean mBottomRight;
    private boolean mBottomLeft;
    private RectF mRoundRect;
    private Path mPath;

    public CircleImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);

            mAsCircle = typedArray.getBoolean(R.styleable.CircleImageView_roundAsCircle, true);
            if (!mAsCircle) {
                mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_roundedCornerRadius,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                mTopLeft = typedArray.getBoolean(R.styleable.CircleImageView_roundTopLeft, true);
                mTopRight = typedArray.getBoolean(R.styleable.CircleImageView_roundTopRight, true);
                mBottomRight = typedArray.getBoolean(R.styleable.CircleImageView_roundBottomRight, true);
                mBottomLeft = typedArray.getBoolean(R.styleable.CircleImageView_roundBottomLeft, true);
            } else {
                mPath = new Path();
            }
            typedArray.recycle();
        }
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mBitmapPaint.setAntiAlias(true);
        mMatrix = new Matrix();
    }

    /**
     * 设置圆角矩形角度
     *
     * @param cornerRadius 角度
     */
    public void setCornerRadius(int cornerRadius) {
        this.mCornerRadius = cornerRadius;
        invalidate();
    }

    /**
     * 是否为圆形
     *
     * @param isCircle true 圆形 other 圆角矩形
     */
    public void setCircle(boolean isCircle) {
        this.mAsCircle = isCircle;
        if (!isCircle) {
            mPath = new Path();
        }
        invalidate();
    }

    /**
     * 设置四个角落,顺时针方向
     *
     * @param topLeft     上左
     * @param topRight    上右
     * @param bottomRight 下右
     * @param bottomLeft  下左
     */
    public void setCorners(boolean topLeft, boolean topRight, boolean bottomRight, boolean bottomLeft) {
        this.mTopLeft = topLeft;
        this.mTopRight = topRight;
        this.mBottomRight = bottomRight;
        this.mBottomLeft = bottomLeft;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        if (mAsCircle) {
            mRadius = measuredWidth > measuredHeight ? measuredHeight : measuredWidth;
            setMeasuredDimension(mRadius, mRadius);
            mRadius = mRadius / 2;
        } else {
            mRoundRect = new RectF(0, 0, measuredWidth, measuredHeight);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            super.onDraw(canvas);
            return;
        }
        Bitmap bitmap = drawableToBitmap(drawable);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale;
        if (mAsCircle) {
            scale = mRadius * 2.0f / (w > h ? h : w);
        } else {
            float scaleWidth = getWidth() * 1.0f / w;
            float scaleHeight = getHeight() * 1.0f / h;
            scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
        }

        mMatrix.setScale(scale, scale);
        bitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(bitmapShader);
        if (mAsCircle) {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
//            path.addCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius, Path.Direction.CW);
        } else {
//            canvas.drawRoundRect(mRoundRect, mCornerRadius, mCornerRadius, mBitmapPaint);
            if(mPath == null){
                mPath = new Path();
            }
            mPath.reset();
            float[] radii = new float[]{
                    mTopLeft ? mCornerRadius : 0, mTopLeft ? mCornerRadius : 0,
                    mTopRight ? mCornerRadius : 0, mTopRight ? mCornerRadius : 0,
                    mBottomRight ? mCornerRadius : 0, mBottomRight ? mCornerRadius : 0,
                    mBottomLeft ? mCornerRadius : 0, mBottomLeft ? mCornerRadius : 0
            };
            mPath.addRoundRect(mRoundRect, radii, Path.Direction.CW);
            canvas.drawPath(mPath, mBitmapPaint);
        }

    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if (w <= 0) {
            w = getWidth();
        }
        if (h <= 0) {
            h = getHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}