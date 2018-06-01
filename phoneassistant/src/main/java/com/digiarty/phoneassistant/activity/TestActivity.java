package com.digiarty.phoneassistant.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.digiarty.phoneassistant.R;
import com.digiarty.phoneassistant.model.bean.beanfromclient.ContactBean;
import com.digiarty.phoneassistant.model.dataparse.ContactAction;
import com.digiarty.phoneassistant.model.dataprovider.ModelManager;
import com.digiarty.phoneassistant.model.dataprovider.ProviderDataType;
import com.digiarty.phoneassistant.utils.Base64Util;
import com.digiarty.phoneassistant.utils.FileHelper;

import java.io.File;
import java.util.ArrayList;
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
        ModelManager modelManager = ModelManager.getInstance();
        System.out.println(modelManager.getDatas(this, ProviderDataType.PICTURE));
        System.out.println(modelManager.getDatas(this, ProviderDataType.AUDIO));
        System.out.println(modelManager.getDatas(this, ProviderDataType.CONTACT));

    }

    public void insetContactMessage(View view) {
        ModelManager modelManager = ModelManager.getInstance();
        List<ContactAction.ContactBeans> contactBeans = new ArrayList<>();
        ContactAction.ContactBeans beans = new ContactAction.ContactBeans();

        ContactBean bean =  new ContactBean();
        bean.setCompanyName("company");
        bean.setDepartment("department");
        bean.setLastName("henmory");
        bean.setFirstName("han");
        bean.setMiddleName("maohui");
        bean.setJobTitle("engineer");
        bean.setKey("123456");
        bean.setNickname("hmh");
        bean.setKey("key");

        ContactBean.ContactAddressEntity entity = new ContactBean.ContactAddressEntity();
        entity.setCity("chengdu");
        entity.setCountry("china");
        entity.setPostalCode("20155");
        entity.setState("sichuan");
        entity.setStreet("tianfu 3th");
        entity.setType("home");
        List<ContactBean.ContactAddressEntity> entities = new ArrayList<>();
        entities.add(entity);

        bean.setAddressList(entities);

        ContactBean.ContactKeyValueEntity entity1 = new ContactBean.ContactKeyValueEntity();
        entity1.setType("home");
        entity1.setValue("han@gmail.com");
        List<ContactBean.ContactKeyValueEntity> entitie = new ArrayList<>();
        entitie.add(entity1);

        bean.setEmailAddressList(entitie);

        bean.setEvent(entitie);

        bean.setImList(entitie);
        bean.setNotes("note");

        bean.setUrlList(entitie);

        bean.setRelatedNameList(entitie);

        bean.setPhoneNumberList(entitie);
        byte[] image = FileHelper.readBytes(new File("/Users/henmory/Downloads/2.jpg"));
        beans.setContactBean(bean);
        beans.setImage(image);
        contactBeans.add(beans);


        modelManager.insertDatas(this, ProviderDataType.CONTACT, contactBeans);
    }
}
