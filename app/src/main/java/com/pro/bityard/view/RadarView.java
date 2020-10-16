package com.pro.bityard.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.pro.bityard.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class RadarView extends View {

    //数据个数
    private int count = 5;
    //成绩圆点半径
    private int valueRadius = 0;
    //网格最大半径
    private float radius;
    //中心X
    private float centerX;
    //中心Y
    private float centerY;
    //雷达区画笔
    private Paint mainPaint, mainPaintBg;
    //文本画笔
    private Paint textPaint, textValuePaint;
    //数据区画笔
    private Paint valuePaint;
    //标题文字
    private List<String> titles;

    //各维度分值
    private List<String> data;
    private List<Double> scaleData;

    //数据最大值
    private double maxValue = 100;


    //弧度
    private float angle;
    private int radarLineCol;
    private int radarBgCol;
    private int radarTextCol;
    private int radarValueCol;
    private int radarValueTextSize;
    private int radarTextValueCol;
    private int radarTextValueTextSize;
    private int radarShadowColOne;
    private int radarShadowColTwo;
    private float x;
    private float y;
    private float x4;
    private float y4;


    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();


    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
            radarLineCol = typedArray.getColor(R.styleable.RadarView_radarLineCol, 0xffFFA800);
            radarBgCol = typedArray.getColor(R.styleable.RadarView_radarBgCol, 0xffFFA800);
            radarTextCol = typedArray.getColor(R.styleable.RadarView_radarTextCol, 0xffFFA800);
            radarValueCol = typedArray.getColor(R.styleable.RadarView_radarValueCol, 0xffFFA800);
            radarValueTextSize = typedArray.getInt(R.styleable.RadarView_radarValueTextSize, 9);
            radarTextValueCol = typedArray.getColor(R.styleable.RadarView_textValueCol, 0xffFFA800);
            radarTextValueTextSize = typedArray.getInt(R.styleable.RadarView_radarTextValueTextSize, 15);
            radarShadowColOne = typedArray.getColor(R.styleable.RadarView_shadowColOne, 0xffFFA800);
            radarShadowColTwo = typedArray.getColor(R.styleable.RadarView_shadowColTwo, 0xffFFA800);

            typedArray.recycle();
        }

    }

    private void init() {
        //雷达区画笔初始化
        mainPaint = new Paint();
        mainPaint.setColor(radarLineCol);
        mainPaint.setAntiAlias(true);
        mainPaint.setStrokeWidth(1);
        mainPaint.setStyle(Paint.Style.STROKE);

        //雷达区背景画笔初始化
        mainPaintBg = new Paint();
        mainPaintBg.setColor(radarBgCol);
        mainPaintBg.setAntiAlias(true);
        mainPaintBg.setStrokeWidth(1);
        mainPaintBg.setStyle(Paint.Style.FILL);

        //文本画笔初始化
        textPaint = new Paint();
        textPaint.setColor(radarTextCol);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(radarValueTextSize);
        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);
        //数值画笔初始化
        textValuePaint = new Paint();
        textValuePaint.setColor(radarTextValueCol);
        textValuePaint.setTextAlign(Paint.Align.CENTER);
        textValuePaint.setTextSize(radarTextValueTextSize);
        textValuePaint.setStrokeWidth(1);
        textValuePaint.setAntiAlias(true);

        //数据区（分数）画笔初始化
        valuePaint = new Paint();
        valuePaint.setColor(radarValueCol);
        valuePaint.setAntiAlias(true);
        valuePaint.setStyle(Paint.Style.FILL);


        titles = new ArrayList<>();
        titles.add(getContext().getString(R.string.text_trader_30_days_count));
        titles.add(getResources().getString(R.string.text_trader_30_days_rate));
        titles.add(getResources().getString(R.string.text_trader_30_days_defeat));
        titles.add(getResources().getString(R.string.text_trader_total_income));
        titles.add(getResources().getString(R.string.text_follower));

        data = new ArrayList<>();
        data.add("10.0");
        data.add("20.0");
        data.add("30.0");
        data.add("40.0");
        data.add("50.0");


        scaleData = new ArrayList<>();
        scaleData.add(100.0);
        scaleData.add(100.0);
        scaleData.add(100.0);
        scaleData.add(100.0);
        scaleData.add(100.0);
        count = titles.size();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(w, h) / 2 * 0.5f;  //这个是占图的多少比例
        centerX = w / 2;
        centerY = h / 2;
        //一旦size发生改变，重新绘制
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolygonBG(canvas);//绘制蜘蛛网背景
        drawPolygon(canvas);//绘制蜘蛛网
       // drawLines(canvas);//绘制直线
        drawTitle(canvas);//绘制标题
        drawValue(canvas);//绘制数据
        drawRegion(canvas);//绘制覆盖区域
    }

    /**
     * 绘制多边形
     *
     * @param canvas
     */


    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        //1度=1*PI/180   360度=2*PI   那么我们每旋转一次的角度为2*PI/内角个数
        //中心与相邻两个内角相连的夹角角度
        angle = (float) (2 * Math.PI / count);
        //每个蛛丝之间的间距
        float r = radius / (count - 1);

        for (int i = 0; i < count; i++) {
            //当前半径
            float curR = r * i;
            path.reset();
            for (int j = 0; j < count; j++) {
                if (j == 0) {
                    x = (float) (centerX + curR * Math.sin(angle));
                    y = (float) (centerY - curR * Math.cos(angle));
                    path.moveTo(x, y);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x1 = (float) (centerX + curR * Math.sin(angle / 2));
                    float y1 = (float) (centerY + curR * Math.cos(angle / 2));
                    path.lineTo(x1, y1);
                    float x2 = (float) (centerX - curR * Math.sin(angle / 2));
                    float y2 = (float) (centerY + curR * Math.cos(angle / 2));
                    path.lineTo(x2, y2);
                    float x3 = (float) (centerX - curR * Math.sin(angle));
                    float y3 = (float) (centerY - curR * Math.cos(angle));
                    path.lineTo(x3, y3);
                    x4 = centerX;
                    y4 = centerY - curR;
                    path.lineTo(x4, y4);
                    float x = (float) (centerX + curR * Math.sin(angle));
                    float y = (float) (centerY - curR * Math.cos(angle));
                    path.lineTo(x, y);
                }
            }
            path.close();

            canvas.drawPath(path, mainPaint);
        }
    }

    /*绘制蜘蛛网背景*/
    private void drawPolygonBG(Canvas canvas) {
        Path path = new Path();
        //1度=1*PI/180   360度=2*PI   那么我们每旋转一次的角度为2*PI/内角个数
        //中心与相邻两个内角相连的夹角角度
        angle = (float) (2 * Math.PI / count);
        //每个蛛丝之间的间距
        float r = radius / (count - 1);

        for (int i = 0; i < count; i++) {
            //当前半径
            float curR = r * i;
            path.reset();
            for (int j = 0; j < count; j++) {
                if (j == 0) {
                    x = (float) (centerX + curR * Math.sin(angle));
                    y = (float) (centerY - curR * Math.cos(angle));
                    path.moveTo(x, y);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x1 = (float) (centerX + curR * Math.sin(angle / 2));
                    float y1 = (float) (centerY + curR * Math.cos(angle / 2));
                    path.lineTo(x1, y1);
                    float x2 = (float) (centerX - curR * Math.sin(angle / 2));
                    float y2 = (float) (centerY + curR * Math.cos(angle / 2));
                    path.lineTo(x2, y2);
                    float x3 = (float) (centerX - curR * Math.sin(angle));
                    float y3 = (float) (centerY - curR * Math.cos(angle));
                    path.lineTo(x3, y3);
                    x4 = centerX;
                    y4 = centerY - curR;
                    path.lineTo(x4, y4);
                    float x = (float) (centerX + curR * Math.sin(angle));
                    float y = (float) (centerY - curR * Math.cos(angle));
                    path.lineTo(x, y);
                }
            }
            path.close();

            canvas.drawPath(path, mainPaintBg);
        }
    }

    /**
     * 绘制直线
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        path.reset();
        //直线1
        path.moveTo(centerX, centerY);
        float x1 = (float) (centerX + radius * Math.sin(angle));
        float y1 = (float) (centerY - radius * Math.cos(angle));
        path.lineTo(x1, y1);
        //直线2
        path.moveTo(centerX, centerY);
        float x2 = (float) (centerX + radius * Math.sin(angle / 2));
        float y2 = (float) (centerY + radius * Math.cos(angle / 2));
        path.lineTo(x2, y2);
        //直线3
        path.moveTo(centerX, centerY);
        float x3 = (float) (centerX - radius * Math.sin(angle / 2));
        float y3 = (float) (centerY + radius * Math.cos(angle / 2));
        path.lineTo(x3, y3);
        //直线4
        path.moveTo(centerX, centerY);
        float x4 = (float) (centerX - radius * Math.sin(angle));
        float y4 = (float) (centerY - radius * Math.cos(angle));
        path.lineTo(x4, y4);
        //直线5
        path.moveTo(centerX, centerY);
        float x5 = (float) (centerX);
        float y5 = (float) (centerY - radius);
        path.lineTo(x5, y5);
        path.close();
        canvas.drawPath(path, mainPaint);
    }

    /**
     * 绘制标题文字
     *
     * @param canvas
     */

    private void drawTitle(Canvas canvas) {
        for (int i = 0; i < count; i++) {
            //获取到雷达图最外边的坐标
            float x = (float) (centerX + Math.sin(angle * i) * (radius + 12));
            float y = (float) (centerY - Math.cos(angle * i) * (radius + 12));
            if (angle * i == 0) {
                //第一个文字位于顶角正上方
                textPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(titles.get(i), x, y - 35, textPaint);
                textPaint.setTextAlign(Paint.Align.LEFT);
            } else if (angle * i > 0 && angle * i < Math.PI / 2) {
                //微调
                canvas.drawText(titles.get(i), x, y, textPaint);
            } else if (angle * i >= Math.PI / 2 && angle * i < Math.PI) {
                //最右下的文字获取到文字的长、宽，按文字长度百分比向左移
                String txt = titles.get(i);
                Rect bounds = new Rect();
                textPaint.getTextBounds(txt, 0, txt.length(), bounds);
                float height = bounds.bottom - bounds.top;
                float width = textPaint.measureText(txt);
                canvas.drawText(txt, x - width * 0.4f, y + height, textPaint);
            } else if (angle * i >= Math.PI && angle * i < 3 * Math.PI / 2) {
                //同理最左下的文字获取到文字的长、宽，按文字长度百分比向左移
                String txt = titles.get(i);
                Rect bounds = new Rect();
                textPaint.getTextBounds(txt, 0, txt.length(), bounds);
                float width = textPaint.measureText(txt);
                float height = bounds.bottom - bounds.top;
                canvas.drawText(txt, x - width * 0.6f, y + height, textPaint);
            } else if (angle * i >= 3 * Math.PI / 2 && angle * i < 2 * Math.PI) {
                //文字向左移动
                String txt = titles.get(i);
                float width = textPaint.measureText(txt);
                canvas.drawText(txt, x - width, y, textPaint);
            }

        }
    }

    private void drawValue(Canvas canvas) {
        for (int i = 0; i < count; i++) {
            //获取到雷达图最外边的坐标
            float x = (float) (centerX + Math.sin(angle * i) * (radius + 12));
            float y = (float) (centerY - Math.cos(angle * i) * (radius + 12));
            if (angle * i == 0) {
                //第一个文字位于顶角正上方
                textValuePaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(data.get(i), x, y, textValuePaint);
                textValuePaint.setTextAlign(Paint.Align.LEFT);
            } else if (angle * i > 0 && angle * i < Math.PI / 2) {
                //微调
                canvas.drawText(data.get(i).toString(), x, y + 35, textValuePaint);
            } else if (angle * i >= Math.PI / 2 && angle * i < Math.PI) {
                //最右下的文字获取到文字的长、宽，按文字长度百分比向左移
                String txt = data.get(i).toString();
                Rect bounds = new Rect();
                textValuePaint.getTextBounds(txt, 0, txt.length(), bounds);
                float height = bounds.bottom - bounds.top;
                float width = textValuePaint.measureText(txt);
                canvas.drawText(txt, x - width * 0.4f, y + height + 35, textValuePaint);
            } else if (angle * i >= Math.PI && angle * i < 3 * Math.PI / 2) {
                //同理最左下的文字获取到文字的长、宽，按文字长度百分比向左移
                String txt = data.get(i).toString();
                Rect bounds = new Rect();
                textValuePaint.getTextBounds(txt, 0, txt.length(), bounds);
                float width = textValuePaint.measureText(txt);
                float height = bounds.bottom - bounds.top;
                canvas.drawText(txt, x - width * 0.6f, y + height + 35, textValuePaint);
            } else if (angle * i >= 3 * Math.PI / 2 && angle * i < 2 * Math.PI) {
                //文字向左移动
                String txt = data.get(i);
                float width = textValuePaint.measureText(txt);
                canvas.drawText(txt, x - width, y + 35, textValuePaint);
            }

        }
    }


    /**
     * 绘制覆盖区域
     */
    private void drawRegion(Canvas canvas) {
        valuePaint.setAlpha(128);
        Path path = new Path();
        double dataValue;
        double percent;
        //绘制圆点1
        dataValue = scaleData.get(0);
        if (dataValue != maxValue) {
            percent = dataValue / maxValue;
        } else {
            percent = 1;
        }
        float x1 = centerX;
        float y1 = (float) (centerY - radius * percent);
        path.moveTo(x1, y1);
        canvas.drawCircle(x1, y1, valueRadius, valuePaint);
        //绘制圆点2
        dataValue = scaleData.get(1);
        if (dataValue != maxValue) {
            percent = dataValue / maxValue;
        } else {
            percent = 1;
        }
        float x2 = (float) (centerX + radius * percent * Math.sin(angle));
        float y2 = (float) (centerY - radius * percent * Math.cos(angle));
        path.lineTo(x2, y2);
        canvas.drawCircle(x2, y2, valueRadius, valuePaint);
        //绘制圆点3
        dataValue = scaleData.get(2);
        if (dataValue != maxValue) {
            percent = dataValue / maxValue;
        } else {
            percent = 1;
        }
        float x3 = (float) (centerX + radius * percent * Math.sin(angle / 2));
        float y3 = (float) (centerY + radius * percent * Math.cos(angle / 2));
        path.lineTo(x3, y3);
        canvas.drawCircle(x3, y3, valueRadius, valuePaint);
        //绘制圆点4
        dataValue = scaleData.get(3);
        if (dataValue != maxValue) {
            percent = dataValue / maxValue;
        } else {
            percent = 1;
        }
        float x4 = (float) (centerX - radius * percent * Math.sin(angle / 2));
        float y4 = (float) (centerY + radius * percent * Math.cos(angle / 2));
        path.lineTo(x4, y4);
        canvas.drawCircle(x4, y4, valueRadius, valuePaint);
        //绘制圆点5
        dataValue = scaleData.get(4);
        if (dataValue != maxValue) {
            percent = dataValue / maxValue;
        } else {
            percent = 1;
        }
        float x5 = (float) (centerX - radius * percent * Math.sin(angle));
        float y5 = (float) (centerY - radius * percent * Math.cos(angle));
        path.lineTo(x5, y5);
        canvas.drawCircle(x5, y5, valueRadius, valuePaint);

        path.close();
        valuePaint.setStyle(Paint.Style.STROKE);
        //绘制覆盖区域外的连线
        canvas.drawPath(path, valuePaint);
        //填充覆盖区域
        valuePaint.setStyle(Paint.Style.FILL);
        //设置渐变色
        LinearGradient mShader = new LinearGradient(x1, y1, x5, y5, radarShadowColOne, radarShadowColTwo, Shader.TileMode.CLAMP);
        valuePaint.setShader(mShader);

        canvas.drawPath(path, valuePaint);
    }


    //设置蜘蛛网颜色
    public void setMainPaint(Paint mainPaint) {
        this.mainPaint = mainPaint;
        postInvalidate();
    }

    //设置标题颜色
    public void setTextPaint(Paint textPaint) {
        this.textPaint = textPaint;
    }

    //设置覆盖局域颜色
    public void setValuePaint(Paint valuePaint) {
        this.valuePaint = valuePaint;
        postInvalidate();
    }


    public void setData(List<String> data, List<Double> scaleData) {
        this.data = data;
        this.scaleData = scaleData;
        postInvalidate();
    }

    //设置满分分数，默认是100分满分
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }
}
