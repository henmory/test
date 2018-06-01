package com.digiarty.phoneassistant.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.digiarty.phoneassistant.R;
import com.digiarty.phoneassistant.model.dataprovider.ModelManager;
import com.digiarty.phoneassistant.model.dataprovider.ProviderDataType;



public class TestActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView = findViewById(R.id.textView);
    }


    public void getContactMessage(View view) {
        ModelManager modelManager = ModelManager.getInstance();
        System.out.println(modelManager.getDatas(this, ProviderDataType.PICTURE));
        System.out.println(modelManager.getDatas(this, ProviderDataType.AUDIO));
        System.out.println(modelManager.getDatas(this, ProviderDataType.CONTACT));

    }

    public void insetContactMessage(View view) {
        ModelManager modelManager = ModelManager.getInstance();
        modelManager.setDatas(this, ProviderDataType.CONTACT);
    }
}
