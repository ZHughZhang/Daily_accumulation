package com.example.daily.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.blankj.utilcode.util.SizeUtils;
import com.example.daily.R;


/**
 * ClassName: SliderView
 * Author: zlq
 * CreateDate:
 * Version: 1.0
 * Desc: 滑动解锁
 **/
public class SliderView extends View {


    private Paint mPaint;//绘制背景
    private Paint mTextPaint;//绘制文字
    private Paint CirclePaint;//绘制圆
    private Bitmap mIconBitmap;
    private int mWidth;
    private int mHeight;
    private int mPadding;
    private int lineHeight;
    private String content = "滑动验证";
    private Rect mTextRect;
    private float mDrawX;
    private boolean mCanMove;
    private OnSlideListener mListener;
    private int mBackgroudColor;
    private int mTextColor;
    private int mIconId;
    private boolean unSlide = false;
    private float textSize;
    private float radius ;

    public SliderView(Context context) {
        this(context, null);
    }

    public SliderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        init();
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SliderView);
        content = ta.getString(R.styleable.SliderView_sl_text);
        if (content == null) {
            content = "           ";
        }
        mBackgroudColor = ta.getColor(R.styleable.SliderView_sl_backgroundColor, getResources().getColor(R.color.color_03AAF3));
        mTextColor = ta.getColor(R.styleable.SliderView_sl_textColor, getResources().getColor(android.R.color.white));
        textSize = ta.getDimension(R.styleable.SliderView_sl_textSize, SizeUtils.sp2px(16));
        lineHeight = (int) ta.getDimension(R.styleable.SliderView_sl_height,SizeUtils.dp2px(50));
        ta.recycle();
    }


    private void init() {

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        mTextRect = new Rect();
        mTextPaint.getTextBounds(content, 0, content.length(), mTextRect);

        mPadding = SizeUtils.dp2px(5);


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mBackgroudColor);

        CirclePaint = new Paint();
        CirclePaint.setAntiAlias(true);
        CirclePaint.setStyle(Paint.Style.FILL);
        CirclePaint.setColor(Color.WHITE);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);

        if (wSpecMode == MeasureSpec.EXACTLY) {

            mWidth = MeasureSpec.getSize(widthMeasureSpec);
            mHeight = MeasureSpec.getSize(heightMeasureSpec);

        }
        radius = lineHeight/2-10;
        setMeasuredDimension(mWidth, mHeight);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (mDrawX < mPadding+radius) {
            mDrawX = mPadding+radius+5;
        } else if (mDrawX > mWidth - mPadding-radius) {
            mDrawX = mWidth - mPadding-radius-5;
        }
        mPaint.setStrokeWidth(lineHeight);
        //画背景颜色，需要给圆形笔锋预留半个高度的位置
        canvas.drawLine(mHeight/2,mHeight/2,mWidth-mHeight/2,mHeight/2 ,mPaint);
        int textX = mTextRect.width() / 2 - SizeUtils.dp2px(5);
        //绘制文字到中间位置
        canvas.drawText(content, textX, mHeight / 2 + mTextRect.height() / 2, mTextPaint);
        //根据滑动位置绘制图标
        canvas.drawCircle(mDrawX, mHeight/2,radius, CirclePaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果不可滑动，立即返回
                if (unSlide) return false;
                mCanMove = true;
                radius = lineHeight/2+2;
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                //如果是可移动状态才继续绘制
                if (mCanMove) {
                    mDrawX = x;
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                //如果是可移动状态才继续绘制
                radius = lineHeight/2-10;
                invalidate();
                if (mCanMove) {
                    //reset();
                    mCanMove = false;
                    //判断是否在指定位置抬起手指
                    if (mDrawX >= mWidth-radius-mPadding) {
                        unSlide = true;
                        if (mListener != null)
                            mListener.onSuccess();
                    } else {
                        reset();
                        if (mListener != null){
                            mListener.onFailure();
                        }
                    }
                }
                break;

        }
        return super.onTouchEvent(event);
    }

    //用属性动画将图标位置重置
    private void reset() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "mDrawX", mDrawX+5, mHeight/2);
        animator.setDuration(200);
        animator.start();
    }

    private float getMDrawX() {
        return mDrawX;
    }

    private void setMDrawX(float mDrawX) {
        this.mDrawX = mDrawX;
        invalidate();
    }

    public interface OnSlideListener {
        void onSuccess();
        void onFailure();
    }


}

