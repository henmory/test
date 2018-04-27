package com.digiarty.phoneassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.digiarty.phoneassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_start)
    Button buttonStart;
    @BindView(R.id.button_stop)
    Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    @OnClick(R.id.button_start)
    public void startService(View view){
        sendBroadcast(new Intent("com.digiarty.phoneassistant.broadcast.START_SERVICE"));
    }

    @OnClick(R.id.button_stop)
    public void stopService(View view){
        sendBroadcast(new Intent("com.digiarty.phoneassistant.broadcast.STOP_SERVICE"));

    }

}
