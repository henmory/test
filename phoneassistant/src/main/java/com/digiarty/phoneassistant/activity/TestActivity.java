package com.digiarty.phoneassistant.activity;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.digiarty.phoneassistant.R;
import com.digiarty.phoneassistant.model.bean.beanfromclient.ContactBean;
import com.digiarty.phoneassistant.model.bean.beantoclient.AddContactCommandToClientBean;
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
    public void deleteContactMessage(View view){
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ContentProviderOperation.Builder op = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection("raw_contact_id=?",new String[]{"1"});
        ops.add(op.build());
        op.withYieldAllowed(true);

        ops.add(op.build());

        try {
            ContentProviderResult[] results = this.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            System.out.println("lenth = " + results.length);
            for (int j = 0; j < results.length; j++) {
                System.out.println(results[j].toString());
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("异常发生 " + e.getMessage());
        } catch (OperationApplicationException e) {
            e.printStackTrace();
            System.out.println("异常发生2 " + e.getMessage());
        }
    }

    public void insetContactMessage(View view) {
        ModelManager modelManager = ModelManager.getInstance();

        List<ContactAction.ContactBeanWrap> contactBeans = new ArrayList<>();

        handleData(contactBeans);
        System.out.println(contactBeans.size());
        ArrayList<AddContactCommandToClientBean.Result> replies = (ArrayList<AddContactCommandToClientBean.Result>) modelManager.insertDatas(this, ProviderDataType.CONTACT, contactBeans);
        for (int i = 0; i < replies.size(); i++){
            System.out.println(replies.get(i));
        }
    }

    private void handleData(List<ContactAction.ContactBeanWrap> contactBeans) {



        for (int i = 0; i < 20; i++){
            ContactAction.ContactBeanWrap beanWrap1 = new ContactAction.ContactBeanWrap();
            ContactBean bean = new ContactBean();
            //设置key
            bean.setKey("key" + i);

            //设置图片
//        byte[] image = FileHelper.readBytes(new File("/storage/emulated/0/DCIM/1.png"));
//        beanWrap.setImage(image);

            //设置名字
            bean.setLastName("aaa" + i);
            bean.setFirstName("aaa" + i);
            bean.setMiddleName("hen111mory" + i);

            //设置组织
            bean.setCompanyName("digiarty" + i);
            bean.setDepartment("development" + i);
            bean.setJobTitle("engineer" + i);

            //设置手机
//            List<ContactBean.ContactKeyValueEntity> phoneList = new ArrayList<>();
//            phoneList.add(new ContactBean.ContactKeyValueEntity(2+"", "13488783239"));
//            phoneList.add(new ContactBean.ContactKeyValueEntity(1+"", "1215644454565"));
//            phoneList.add(new ContactBean.ContactKeyValueEntity(0+"", "1215644454565", "我的"));
//            bean.setPhoneNumberList(phoneList);
//
//            //设置电子邮箱
//            List<ContactBean.ContactKeyValueEntity> mailList = new ArrayList<>();
//            mailList.add(new ContactBean.ContactKeyValueEntity(2+"", "han@gmail.com"));
//            mailList.add(new ContactBean.ContactKeyValueEntity(1+"", "han@163.com"));
//            bean.setEmailAddressList(mailList);
//
//            //设置地址
//            List<ContactBean.ContactAddressEntity> addressEntityList = new ArrayList<>();
//
//            ContactBean.ContactAddressEntity addressEntity = new ContactBean.ContactAddressEntity();
//            addressEntity.setCity("chengdu");
//            addressEntity.setCountry("china");
//            addressEntity.setPostalCode("20155");
//            addressEntity.setState("sichuan");
//            addressEntity.setStreet("tianfu 3th");
//            addressEntity.setType("home");
//            addressEntityList.add(addressEntity);
//
//            bean.setAddressList(addressEntityList);
//
//            //设置网址
//            List<ContactBean.ContactKeyValueEntity> urlList = new ArrayList<>();
//            urlList.add(new ContactBean.ContactKeyValueEntity(2+"", "www.baidu.com"));
//            urlList.add(new ContactBean.ContactKeyValueEntity(1+"", "www.digiarty.com"));
//            bean.setUrlList(urlList);
//
//            //设置事件
//            List<ContactBean.ContactKeyValueEntity> eventList = new ArrayList<>();
//            eventList.add(new ContactBean.ContactKeyValueEntity(2+"", "2018112456658"));
//            eventList.add(new ContactBean.ContactKeyValueEntity(1+"", "201811245665855555"));
//            bean.setEvent(eventList);
//
//
//            //设置注释 todo 与ios不同
////        List<ContactBean.ContactKeyValueEntity> noteList = new ArrayList<>();
////        noteList.add(new ContactBean.ContactKeyValueEntity(2+"", "注释1"));
////        noteList.add(new ContactBean.ContactKeyValueEntity(1+"", "注释12"));
////        bean.setNotes(noteList);
//            bean.setNotes("注释");
//
//            //设置im
            List<ContactBean.ContactKeyValueEntity> IMList = new ArrayList<>();
            IMList.add(new ContactBean.ContactKeyValueEntity(1+"", "msn")); //msn
            IMList.add(new ContactBean.ContactKeyValueEntity(4+"", "qq"));
            IMList.add(new ContactBean.ContactKeyValueEntity(-1 +"","henmory", "微信"));
            bean.setImList(IMList);
//
//            //设置昵称
//            bean.setNickname("hmh");
//
//            //设置关系
            List<ContactBean.ContactKeyValueEntity> relationList = new ArrayList<>();
            relationList.add(new ContactBean.ContactKeyValueEntity(2+"", "李四"));
            relationList.add(new ContactBean.ContactKeyValueEntity(1+"", "张三"));
            relationList.add(new ContactBean.ContactKeyValueEntity(0+"", "王五", "狗友"));
            bean.setRelatedNameList(relationList);

            beanWrap1.setContactBean(bean);
            contactBeans.add(beanWrap1);

        }
    }


}
