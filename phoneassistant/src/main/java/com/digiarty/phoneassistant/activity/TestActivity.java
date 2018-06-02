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
        bean.setCompanyName("digiarty");
        bean.setDepartment("development");
        bean.setLastName("111");
        bean.setFirstName("111");
        bean.setMiddleName("hen111mory");
        bean.setJobTitle("engineer");
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
        List<ContactBean.ContactKeyValueEntity> entitie = new ArrayList<>();

        entity1.setType("home");
        entity1.setValue("han@gmail.com");
        entitie.add(entity1);
        entity1.setType("1");
        entity1.setValue("han@163.com");
        entitie.add(entity1);

        bean.setEmailAddressList(entitie);

        entity1.setType("结婚纪念日");
        entity1.setValue("2017-08-29");
        entitie.add(entity1);
        bean.setEvent(entitie);

        bean.setImList(entitie);
        bean.setNotes("note");


        entity1.setType("www.digiarty.com");
        entity1.setValue("");
        entitie.add(entity1);
        bean.setUrlList(entitie);


        entity1.setType("姐姐");
        entity1.setValue("dan");
        entitie.add(entity1);
        entity1.setType("妻子");
        entity1.setValue("qin");
        entitie.add(entity1);
        bean.setRelatedNameList(entitie);

        entity1.setType("home");
        entity1.setValue("13488783238");
        entitie.add(entity1);
        entity1.setType("work");
        entity1.setValue("17711353996");
        entitie.add(entity1);
        bean.setPhoneNumberList(entitie);

        byte[] image = FileHelper.readBytes(new File("/storage/emulated/0/DCIM/1.png"));
        beans.setContactBean(bean);
        beans.setImage(image);
        contactBeans.add(beans);


        modelManager.insertDatas(this, ProviderDataType.CONTACT, contactBeans);
    }
}
