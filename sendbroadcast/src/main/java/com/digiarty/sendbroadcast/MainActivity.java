package com.digiarty.sendbroadcast;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送有权限广播
                System.out.println("click");
                Intent i = new Intent();
                i.setAction("digiarty.phoneassistant.startservice.broadcast");
                i.setPackage("com.digiarty.sendbroadcast");
//                sendBroadcast(i);
                                sendBroadcast(i, "com.digiarty.phoneassistant.permission.receive_broadcast");
            }
        });
    }

}
