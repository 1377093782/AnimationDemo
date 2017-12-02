package com.example.liuan.screenview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by LiuShuai on 2017/11/25.
 * Email:liushuailem@gmail.com
 */

public class DoughnutProgress extends View {

    private static final int DEFAULT_MIN_WIDTH = 400; //View默认最小宽度
    private static final int RED = 255, GREEN = 255, BLUE = 255; //基础颜色，这里是橙红色
    private static final int MIN_ALPHA = 30; //最小不透明度
    private static final int MID_ALPHA = 120; //最小不透明度
    private static final int MAX_ALPHA = 255; //最大不透明度
    private static final float doughnutRaduisPercent = 0.65f; //圆环外圆半径占View最大半径的百分比
    private static final float doughnutWidthPercent = 0.05f; //圆环宽度占View最大半径的百分比
    private static final float MIDDLE_WAVE_RADUIS_PERCENT = 0.9f; //第二个圆出现时，第一个圆的半径百分比
    private static final float WAVE_WIDTH = 5f; //波纹圆环宽度

    //圆环颜色
    private static int[] doughnutColors = new int[]{
            Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
            Color.argb(MID_ALPHA, RED, GREEN, BLUE),
            Color.argb(MAX_ALPHA, RED, GREEN, BLUE)};

    private Paint paint = new Paint(); //画笔
    private float width; //自定义view的宽度
    private float height; //自定义view的高度
    private float currentAngle = 0f; //当前旋转角度
    private float raduis; //自定义view的最大半径
    private float firstWaveRaduis;
    private float secondWaveRaduis;

    //
    private void resetParams() {
        width = getWidth();
        height = getHeight();
        raduis = Math.min(width, height) / 2;
    }

    private void initPaint() {
        paint.reset();
        paint.setAntiAlias(true);
    }

    public DoughnutProgress(Context context) {
        super(context);
    }

    public DoughnutProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DoughnutProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 当布局为wrap_content时设置默认长宽
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int origin) {
        int result = DEFAULT_MIN_WIDTH;
        int specMode = MeasureSpec.getMode(origin);
        int specSize = MeasureSpec.getSize(origin);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        resetParams();
        //将画布中心设为原点(0,0), 方便后面计算坐标
        canvas.translate(width / 2, height / 2);

        //转起来
        canvas.rotate(currentAngle, 0, 0);
        if (currentAngle >= 360f) {
            currentAngle = currentAngle - 360f;
        } else {
            currentAngle = currentAngle + 2f;
        }

        float doughnutWidth = raduis * doughnutWidthPercent;//圆环宽度
        //圆环外接矩形
        RectF rectF = new RectF(
                -raduis * doughnutRaduisPercent,
                -raduis * doughnutRaduisPercent,
                raduis * doughnutRaduisPercent,
                raduis * doughnutRaduisPercent);
        initPaint();
        paint.setStrokeWidth(doughnutWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShader(new SweepGradient(0, 0, doughnutColors, null));
//        paint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawArc(rectF, 0, 360, false, paint);

//        //画旋转头部圆
//        initPaint();
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.argb(MAX_ALPHA, RED, GREEN, BLUE));
//        canvas.drawCircle(raduis * doughnutRaduisPercent, 0, doughnutWidth / 2, paint);

    }

    boolean isStart = false;
    private static final String TAG = "DoughnutProgress";
    public void startRotate() {
        if (isStart) {


             //   thread.destroy();
           //     thread.stop();
                Log.e(TAG, "startRotate: stop" );
            isStart=false;
            return;
        }
        thread.start();
        isStart=!isStart;
    }


    private Thread thread = new Thread() {
        @Override
        public void run() {
            while (true&&isStart) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
        }
    };
}
