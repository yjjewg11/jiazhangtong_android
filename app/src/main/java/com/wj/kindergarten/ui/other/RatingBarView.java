package com.wj.kindergarten.ui.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wenjie.jiazhangtong.R;

import java.util.ArrayList;
import java.util.List;

public class RatingBarView extends LinearLayout {
    private List<ImageView> mStars = new ArrayList<ImageView>();
    private boolean mClickable = false;
    private OnRatingListener onRatingListener;
    private Object bindObject;
    private float starImageSize;
    private int starCount;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
<<<<<<< HEAD
    private Drawable starHalfDrawable;
    private int clickedCount;

    public int getClickedCount() {
        return clickedCount;
    }
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

<<<<<<< HEAD
    public void setHalfDrawable (Drawable starHalfDrawable){
        this.starHalfDrawable = starHalfDrawable;
    }

=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setStarCount(int startCount) {
        this.starCount = starCount;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    private int startCount;


    public void setBindObject(Object bindObject) {
        this.bindObject = bindObject;
    }

    public void setOnRatingListener(OnRatingListener onRatingListener) {
        this.onRatingListener = onRatingListener;
    }

    public void setmClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public RatingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingBarView);
<<<<<<< HEAD
        starImageSize = a.getDimension(R.styleable.RatingBarView_starImageSize, 10);
        starCount = a.getInteger(R.styleable.RatingBarView_starCount, 5);
        starEmptyDrawable = a.getDrawable(R.styleable.RatingBarView_starEmpty);
        starFillDrawable = a.getDrawable(R.styleable.RatingBarView_starFill);
        starHalfDrawable = a.getDrawable(R.styleable.RatingBarView_starHalf);
//        starEmptyDrawable.setBounds(0,0,10,10);
//        starFillDrawable.setBounds(0,0,10,10);
//        starHalfDrawable.setBounds(0,0,10,10);
=======
        starImageSize = a.getDimension(R.styleable.RatingBarView_starImageSize, 20);
        starCount = a.getInteger(R.styleable.RatingBarView_starCount, 5);
        starEmptyDrawable = a.getDrawable(R.styleable.RatingBarView_starEmpty);
        starFillDrawable = a.getDrawable(R.styleable.RatingBarView_starFill);
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

        for (int i = 0; i < starCount; ++i) {
            ImageView imageView = getStarImageView(context, attrs);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickable){
                        setStar(indexOfChild(v) + 1);
                        if (onRatingListener != null) {
                            onRatingListener.onRating(bindObject,indexOfChild(v) + 1);
                        }
                    }
<<<<<<< HEAD
=======

>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
                }
            });
            addView(imageView);
        }

    }

    private ImageView getStarImageView(Context context, AttributeSet attrs) {


        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(Math.round(starImageSize), Math.round(starImageSize));
        imageView.setLayoutParams(para);
        imageView.setPadding(0, 0, 5, 0);
        imageView.setImageDrawable(starEmptyDrawable);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        return imageView;

    }
    public void setStar(int starCount) {
<<<<<<< HEAD
        clickedCount = starCount;
        setStar(starCount, true);
=======
        setStar(starCount,true);
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    }

    public void setStar(int starCount,boolean animation) {
        starCount = starCount > this.starCount ? this.starCount : starCount;
        starCount = starCount < 0 ? 0 : starCount;
        //TODO
        for (int i = 0; i < starCount; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
            if(animation) YoYo.with(Techniques.BounceIn).duration(400).playOn(getChildAt(i));
        }

        for (int i = this.starCount-1 ; i >= starCount; --i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }

    }

<<<<<<< HEAD
    public void setFloatStar(float starCount,boolean animation){
        if(starCount%10>0){
            int totalStarCount =(int) starCount/10 + 1;
            totalStarCount = totalStarCount > this.starCount ? this.starCount : totalStarCount;
            totalStarCount = totalStarCount < 0 ? 0 : totalStarCount;

            for(int j  = 0; j < totalStarCount ; j++){
                ((ImageView) getChildAt(j)).setImageDrawable(starFillDrawable);
                if(animation) YoYo.with(Techniques.BounceIn).duration(400).playOn(getChildAt(j));
                if(j==totalStarCount-1) {
                    ((ImageView) getChildAt(j)).setImageDrawable(starHalfDrawable);
                }
            }
            for (int i = this.starCount-1 ; i >= totalStarCount; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }
        }else{
            setStar((int)starCount);
        }
    }

=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0



    public interface OnRatingListener {

        void onRating(Object bindObject, int RatingScore);

    }
}
