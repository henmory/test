package com.digiarty.phoneassistant.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.digiarty.phoneassistant.R;
import com.digiarty.phoneassistant.bean.ContactBean;
import com.digiarty.phoneassistant.model.ContactsProvider;

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
        List<ContactBean> beans =  ContactsProvider.getContactDatas(this);
        textView.setText(beans.get(0).getPhoneNumber());
    }
}
