package com.digiarty.phoneassistant.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.digiarty.phoneassistant.R;
import com.digiarty.phoneassistant.model.IGetDatasService;
import com.digiarty.phoneassistant.model.ProviderDataType;

import java.util.List;


public class TestActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView = findViewById(R.id.textView);
    }


    public void getContactMessage(View view) {
        new IGetDatasService<String>() {
            @Override
            public List<String> getDatasFromMobilePhone(Context context, ProviderDataType dataType) {
                return null;
            }
        };
//        List<String> d =  datas.getDatasFromMobilPhone(this, ProviderDataType.PICTURE);
//        System.out.println(d.toString());

    }
}
