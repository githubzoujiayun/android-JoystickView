package com.rustfisher.uijoystick.controller;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.rustfisher.uijoystick.R;
import com.rustfisher.uijoystick.listener.JoystickTouchViewListener;
import com.rustfisher.uijoystick.model.TouchViewModel;
import com.rustfisher.uijoystick.view.TouchView;

/**
 * 触摸式控制器
 */
public class DefaultController implements IJoystickController {
    private Context ctx;
    private RelativeLayout containerView;

    private TouchView leftControlTouchView;
    private TouchView rightControlTouchView;

    /**
     * @param context       需要关联context获取资源文件
     * @param containerView 父view
     */
    public DefaultController(Context context, RelativeLayout containerView) {
        this.ctx = context;
        this.containerView = containerView;
    }

    @Override
    public void createViews() {
        createLeftControlTouchView();
        containerView.addView(leftControlTouchView);

        createRightControlTouchView();
        containerView.addView(rightControlTouchView);
    }

    @Override
    public void showViews(boolean showAnimation) {
        leftControlTouchView.clearAnimation();
        leftControlTouchView.setVisibility(View.VISIBLE);

        rightControlTouchView.clearAnimation();
        rightControlTouchView.setVisibility(View.VISIBLE);
    }

    private void createLeftControlTouchView() {
        TouchViewModel model = new TouchViewModel(
                R.drawable.ui_pic_joystick_left_pad,
                R.drawable.ui_pic_joystick_control_ball,
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_dimen) / 2.0f
        );
        int gapPx = ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_circle_bg_gap);
        model.setCircleBgGapPx(gapPx);
        leftControlTouchView = new TouchView(ctx);
        leftControlTouchView.init(model);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_dimen),
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_dimen)
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.leftMargin = ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_hor_margin);
        params.bottomMargin = ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_bottom_margin);
        leftControlTouchView.setLayoutParams(params);
    }

    private void createRightControlTouchView() {
        TouchViewModel model = new TouchViewModel(
                R.drawable.ui_pic_joystick_right_pad,
                R.drawable.ui_pic_joystick_control_ball,
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_dimen) / 2.0f
        );
        model.setDirectionPicResId(R.drawable.ui_pic_joystick_arrow);
        int gapPx = ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_circle_bg_gap);
        model.setCircleBgGapPx(gapPx);
        rightControlTouchView = new TouchView(ctx);
        rightControlTouchView.init(model);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_dimen),
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_dimen)
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.rightMargin = ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_hor_margin);
        params.bottomMargin = ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_bottom_margin);
        rightControlTouchView.setLayoutParams(params);

    }

    /**
     * 必须先初始化view
     */
    public void setLeftTouchViewListener(JoystickTouchViewListener leftTouchViewListener) {
        if (null != leftControlTouchView) {
            leftControlTouchView.setListener(leftTouchViewListener);
        }
    }

    public void setRightTouchViewListener(JoystickTouchViewListener rightTouchViewListener) {
        if (null != rightControlTouchView) {
            rightControlTouchView.setListener(rightTouchViewListener);
        }
    }
}
