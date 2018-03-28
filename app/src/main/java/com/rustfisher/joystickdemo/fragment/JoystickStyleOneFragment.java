package com.rustfisher.joystickdemo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rustfisher.joystickdemo.R;
import com.rustfisher.uijoystick.controller.DefaultController;
import com.rustfisher.uijoystick.listener.JoystickTouchViewListener;

public class JoystickStyleOneFragment extends Fragment {

    private static final String TAG = "rustApp";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_joystick_style_1, container, false);
        initJoystick(root);
        return root;
    }

    private void initJoystick(View root) {
        DefaultController defaultController =
                new DefaultController(getContext(),
                        (RelativeLayout) root.findViewById(R.id.joystick_container));
        defaultController.createViews();
        defaultController.showViews(false);
        defaultController.setLeftTouchViewListener(new JoystickTouchViewListener() {
            @Override
            public void onTouch(float horizontalPercent, float verticalPercent) {
                Log.d(TAG, "onTouch left: " + horizontalPercent + ", " + verticalPercent);
            }

            @Override
            public void onReset() {
                Log.d(TAG, "onReset: left");
            }

            @Override
            public void onActionDown() {
                Log.d(TAG, "onActionDown: left");
            }

            @Override
            public void onActionUp() {
                Log.d(TAG, "onActionUp: left");
            }
        });
        defaultController.setRightTouchViewListener(new JoystickTouchViewListener() {
            @Override
            public void onTouch(float horizontalPercent, float verticalPercent) {
                Log.d(TAG, "onTouch right: " + horizontalPercent + ", " + verticalPercent);
            }

            @Override
            public void onReset() {
                Log.d(TAG, "onReset: right");
            }

            @Override
            public void onActionDown() {
                Log.d(TAG, "onActionDown: right");
            }

            @Override
            public void onActionUp() {
                Log.d(TAG, "onActionUp: right");
            }
        });
    }
}
