package com.example.daily.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


/**
 * ClassName: LineTextView
 * Author: zlq
 * CreateDate:
 * Version: 1.0
 * Desc: 给textView添加对角线
 **/
@SuppressLint("AppCompatCustomView")
public class LineTextView extends TextView {

    private int height ;
    private int width;
    private String text1 = "天";
    private String text2 = "份";
    private int textColor = Color.BLACK;
    private Paint linePaint ;
    private Paint drawText;
    private  int lineColor = Color.BLACK;
    private float textSize = 30;


    public LineTextView(Context context) {
        this(context,null);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        linePaint=  new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setAntiAlias(true);
        drawText = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawText.setColor(textColor);
        drawText.setTextSize(textSize);
        drawText.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        width = getMeasuredWidth();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float startX;
        float startY;
        float endX;
        float endY;

        //斜线的起始坐标
        startX = 0;
        startY = 0;
        endX = width;
        endY = height;
        canvas.drawLine(startX,startY,endX,endY,linePaint);
        canvas.drawText(text1,endX/2,endY/2-10,drawText);
        canvas.drawText(text2, (endX/3+10),endY/2+40,drawText);
    }


    public void setLineColor(int color){
        lineColor = ContextCompat.getColor(getContext(),color);
        init();
        invalidate();
    }

    public void setText(String...strings){
        text1 = strings[0];
        text2 = strings[1];
        init();
        invalidate();
    }

    public void setTextColors(int color){
        this.textColor = ContextCompat.getColor(getContext(),color);
        init();
        invalidate();
    }

    public void setTextSizes(float size){
        textSize = size;
        init();
        invalidate();
    }
}
