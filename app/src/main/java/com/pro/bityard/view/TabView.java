package com.pro.bityard.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.pro.bityard.R;

import androidx.annotation.Nullable;

public class TabView extends View {
    /**
     * 线的高度
     */
    public int mLineHeight = 1;
    /**
     * 线的颜色
     */
    private int mLineColor;
    /**
     * 中间的线宽度
     */
    private int mDistance;
    /**
     * 夹角
     */
    private double Q = Math.PI / 4;
    /**
     * 圆的半径
     */
    private float mRadius;
    /**
     * 圆偏移的距离
     */
    private int mOffsetY;

    private Paint mPaint;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        mLineHeight = (int) array.getDimension(R.styleable.TabView_outViewLineHeight, dip2px(mLineHeight));
        mLineColor = array.getColor(R.styleable.TabView_outViewLineColor, Color.parseColor("#dddddd"));
        mDistance = (int) array.getDimension(R.styleable.TabView_outViewDistance, dip2px(20));
        float angle = array.getFloat(R.styleable.TabView_outViewOffsetAngle, 30);
        array.recycle();
        // rad(弧度) = deg(角度) *  PI / 180
        Q = angle * Math.PI / 180;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineHeight);
    }

    private float dip2px(int value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics()) + 0.5f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 圆的半径
        mRadius = (float) (mDistance / 2 / Math.cos(Q));
        // 圆心偏移Y的距离
        mOffsetY = (int) (mDistance / 2 * Math.tan(Q));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画线
        int y = getHeight() - mLineHeight / 2;
        int stopX = (getWidth() - mDistance) / 2;
        canvas.drawLine(0, y, stopX, y, mPaint);
        // 画圆
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth() / 2, getHeight() +mOffsetY, mRadius, mPaint);
        // 画线
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawLine(stopX + mDistance, y, getWidth(), y, mPaint);
    }
}