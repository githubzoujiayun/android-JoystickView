package com.rustfisher.uijoystick.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import org.jetbrains.annotations.Nullable;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.rustfisher.uijoystick.business.RoundCalculator;
import com.rustfisher.uijoystick.listener.JoystickTouchViewListener;
import com.rustfisher.uijoystick.model.TouchViewModel;

/**
 * 触摸式控制盘
 * 假设背景是一个圆盘
 * 圆盘中有一个小圆球
 * 圆盘外围绕着一个箭头
 */
public class TouchView extends View {
    private static final String TAG = "TouchView";

    private Bitmap bgBmp;        // 视图背景图片  假设是一个圆盘
    private Bitmap touchBmp;     // 视图中间的随手指移动的图片  假设是一个圆球
    private Bitmap directionBmp; // 指示方向的图片  假设是一个箭头  整体是一个正方形的图片
    private Paint viewRectPaint; // 用来画定宽高的透明背景
    private boolean shouldShowDirectionBmp; // 是否显示方向指示图片

    private JoystickTouchViewListener jListener;

    private float circleBgGapPx = 20;// 背景圆到view边界的像素
    protected float wholeViewRadius; // 整个view的半径

    protected float touchImageX;
    protected float touchImageY;

    private float touchBmpDefaultX; // 滚动球图片默认左上角x
    private float touchBmpDefaultY; // 滚动球图片默认左上角y

    private boolean isMoving;

    private ValueAnimator valueAnimatorResetX;
    private ValueAnimator valueAnimatorResetY;

    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 初始化
     *
     * @param model 获取指定的图片资源
     */
    public void init(TouchViewModel model) {
        Bitmap tmpBgBmp = BitmapFactory.decodeResource(getResources(), model.getBgResId());
        Bitmap tmpTouchBmp = BitmapFactory.decodeResource(getResources(), model.getTouchBmpResId());
        this.circleBgGapPx = model.getCircleBgGapPx();
        this.wholeViewRadius = model.getWholeViewRadius();
        isMoving = false;

        float scaleBg = (wholeViewRadius * 2.0f) / tmpBgBmp.getWidth(); // 要放大的倍数

        shouldShowDirectionBmp = model.isShowDirectionPic();
        if (shouldShowDirectionBmp) {
            Bitmap tmpDirectionBmp = BitmapFactory.decodeResource(getResources(), model.getDirectionPicResId());
            float dScale = (wholeViewRadius * 2.0f) / tmpDirectionBmp.getWidth(); // 缩放到最大
            directionBmp = Bitmap.createScaledBitmap(tmpDirectionBmp,
                    (int) (tmpDirectionBmp.getWidth() * dScale + 0.5),
                    (int) (tmpDirectionBmp.getHeight() * dScale + 0.5),
                    true);
        }

        bgBmp = Bitmap.createScaledBitmap(tmpBgBmp,
                (int) (tmpBgBmp.getWidth() * scaleBg + 0.5 - 2 * circleBgGapPx),
                (int) (tmpBgBmp.getHeight() * scaleBg + 0.5 - 2 * circleBgGapPx),
                true);
        touchBmp = Bitmap.createScaledBitmap(tmpTouchBmp,
                (int) (tmpTouchBmp.getWidth() * scaleBg + 0.5 - circleBgGapPx),
                (int) (tmpTouchBmp.getHeight() * scaleBg + 0.5 - circleBgGapPx),
                true);

        touchBmpDefaultX = wholeViewRadius - touchBmp.getWidth() / 2;
        touchBmpDefaultY = wholeViewRadius - touchBmp.getWidth() / 2;

        touchImageX = touchBmpDefaultX;
        touchImageY = touchBmpDefaultY;
        viewRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        viewRectPaint.setColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bgBmp != null && getWidth() > 0) {

            // 画透明边框以确定view的大小
            canvas.drawRect(0, 0, wholeViewRadius * 2, wholeViewRadius * 2, viewRectPaint);

            // 画背景
            canvas.drawBitmap(bgBmp, circleBgGapPx, circleBgGapPx, null);

            if (shouldShowDirectionBmp && touchBmpDefaultX != touchImageX && touchBmpDefaultY != touchImageY) {
                // 画方向指示箭头
                float rotationDegree = (float) RoundCalculator.calTwoPointAngleDegree(wholeViewRadius, wholeViewRadius,
                        touchImageX + touchBmp.getWidth() / 2, touchImageY + touchBmp.getWidth() / 2);
                drawRotateBitmap(canvas, directionBmp, 180 - rotationDegree, 0, 0);
            }

            // 画中心控制圆圈
            canvas.drawBitmap(touchBmp, touchImageX, touchImageY, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isMoving = false;
            reset();
            if (null != jListener) {
                jListener.onActionUp();
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isMoving = true;  // 直接移动圆球到点击位置
            userMoving(event);
            if (null != jListener) {
                jListener.onActionDown();
            }
        } else if (isMoving) {
            userMoving(event);
        }
        return true;
    }

    private void userMoving(MotionEvent event) {
        if (valueAnimatorResetX != null && valueAnimatorResetY != null) {
            valueAnimatorResetX.removeAllUpdateListeners();
            valueAnimatorResetY.removeAllUpdateListeners();
        }

        float tr = (float) RoundCalculator.calTwoPointDistant(wholeViewRadius, wholeViewRadius, event.getX(), event.getY());
        if (tr <= (wholeViewRadius - circleBgGapPx - touchBmp.getWidth() / 2)) {
            // 点击在背景圆圈内
            onBallMove(event.getX(), event.getY());
        } else {
            // 点击后拖出了边界  计算出拖动圆的圆心坐标
            double dotCenterOnShow[] = RoundCalculator.calPointLocationByAngle(
                    wholeViewRadius, wholeViewRadius, event.getX(), event.getY(), (wholeViewRadius - circleBgGapPx - touchBmp.getWidth() / 2));
            onBallMove((float) dotCenterOnShow[0], (float) dotCenterOnShow[1]);
        }
    }

    protected void onBallMove(float touchPointX, float touchPointY) {
        touchImageX = touchPointX - touchBmp.getWidth() / 2;
        touchImageY = touchPointY - touchBmp.getWidth() / 2;

        invalidate();

        if (jListener != null) {
            float horizontalPercent = (touchPointX - wholeViewRadius) / (wholeViewRadius - circleBgGapPx - touchBmp.getWidth() / 2.0f);
            float verticalPercent = (wholeViewRadius - touchPointY) / (wholeViewRadius - circleBgGapPx - touchBmp.getWidth() / 2.0f);
            jListener.onTouch(horizontalPercent, verticalPercent);
        }
    }

    protected void reset() {
        valueAnimatorResetX = new ValueAnimator();
        valueAnimatorResetX.setFloatValues(touchImageX, touchBmpDefaultX);
        valueAnimatorResetX.setDuration(200);
        valueAnimatorResetX.start();
        valueAnimatorResetX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                touchImageX = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimatorResetY = new ValueAnimator();
        valueAnimatorResetY.setFloatValues(touchImageY, touchBmpDefaultY);
        valueAnimatorResetY.setDuration(200);
        valueAnimatorResetY.start();
        valueAnimatorResetY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                touchImageY = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        if (jListener != null) {
            jListener.onReset();
        }
    }

    public void setListener(JoystickTouchViewListener listener) {
        this.jListener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled && isMoving) {
            isMoving = false;
            reset();
        }
    }

    /**
     * @param canvas   画布
     * @param bitmap   要绘制的bitmap
     * @param rotation 旋转角度
     * @param posX     左上角顶点的x值 - left
     * @param posY     左上角顶点的y值 - top
     */
    private static void drawRotateBitmap(Canvas canvas, Bitmap bitmap,
                                         float rotation, float posX, float posY) {
        Matrix matrix = new Matrix();
        int offsetX = bitmap.getWidth() / 2;
        int offsetY = bitmap.getHeight() / 2;
        matrix.postTranslate(-offsetX, -offsetY);
        matrix.postRotate(rotation);
        matrix.postTranslate(posX + offsetX, posY + offsetY);
        canvas.drawBitmap(bitmap, matrix, null);
    }
}
