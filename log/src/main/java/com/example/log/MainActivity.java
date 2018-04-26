package com.example.log;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity {
    //定义日志对象
    private static Logger logger = LoggerFactory.getLogger(MainActivity.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //级别为debug的日志
        logger.debug("Hello! debug!");
        //级别为info的日志
        logger.info("Hello! info!");
        //级别为warn的日志
        logger.warn("Hello! warn!");
        //级别为error的日志
        logger.error("Hello! error!");
    }


}
