package com.rustfisher.joystickdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rustfisher.joystickdemo.R;
import com.rustfisher.joystickdemo.fragment.JoystickStyleOneFragment;

public class MainActivity extends AppCompatActivity {

    JoystickStyleOneFragment mJoystickStyleOneFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        mJoystickStyleOneFragment = new JoystickStyleOneFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mJoystickStyleOneFragment).commit();
    }
}
