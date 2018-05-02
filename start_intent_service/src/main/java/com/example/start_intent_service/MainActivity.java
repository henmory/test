package com.example.start_intent_service;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button start_service;
    private Button stop_service;
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
            }
        });


        start_service = findViewById(R.id.btn_start_service);
        stop_service = findViewById(R.id.btn_stop_service);

        start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent();
                intent.setAction("com.example.start_intent_service.START_SERVICE");
                intent.setPackage("com.example.start_intent_service");
                startService(intent);
//                startService(new Intent(MainActivity.this, MyIntentService.class));

            }
        });

        stop_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent();
                intent.setAction("com.example.start_intent_service.STOP_SERVICE");
                intent.setPackage("com.example.start_intent_service");
                stopService(intent);

            }
        });
    }

}
