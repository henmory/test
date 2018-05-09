package com.example.junit5.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.example.junit5.bean.ContactBean;


import java.util.ArrayList;
import java.util.List;

/***
 *
 * Created on：09/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ContractsProvider {

//    private static Logger logger = LoggerFactory.getLogger(ContractsProvider.class);
    private static List<ContactBean> contacts = new ArrayList<>();

    private final static Uri CONTACT_ID = ContactsContract.Contacts.CONTENT_URI;
    private final static Uri RAW_CONTACT_ID = ContactsContract.RawContacts.CONTENT_URI;
    private final static Uri DATA_ID = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    // 获取用来操作数据的类的对象，对联系人的基本操作都是使用这个对象
    private static ContentResolver contentResolver;

    public static List<ContactBean> getContactDatas(Context context){
        contentResolver =  context.getContentResolver();
        return getContactsDatasFromContactsProvider();
    }

    private static List<ContactBean> getContactsDatasFromContactsProvider(){

        // 查询contacts表的所有记录
        Cursor cursor = getContactsCursorFromContactsTable();
        if (null == cursor){
            return null;
        }
        // 如果记录不为空
        if (cursor.getCount() > 0){

            // 游标初始指向查询结果的第一条记录的上方，执行moveToNext函数会判断,下一条记录是否存在，如果存在，指向下一条记录。否则，返回false。
            while (cursor.moveToNext()){

                String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                System.out.println("contact_id = " + contact_id);

                //从RawContract表中获取RawContactId
                String rawContactId = getRawContactIdFromRawContractTable(contact_id);
                System.out.println("rawContactId = " + rawContactId);

                //从ContractDataTable表中获取数据
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0){
                    ContactBean bean = getOneItemDataFromContractDataTable(rawContactId);
                    contacts.add(bean);
                }else{
                    System.out.println("rawContactId = " + rawContactId + "  的数据没有添加任何电话号码");
                    contacts = null;
                }
            }
            System.out.println("联系人数据读取完毕");
        }else{
            System.out.println("获取联系人Contract数据为空");
            contacts = null;
        }
        cursor.close();
        return contacts;
    }

    private static Cursor getContactsCursorFromContactsTable(){
        // 查询contacts表的所有记录===返回id跟是否有电话号码列
        Cursor cursor = contentResolver.query(CONTACT_ID, new String[]{ContactsContract.Contacts._ID,
                ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null,null);
        if (null == cursor){
            System.out.println("获取联系人Contract游标为空");
            return null;
        }
        return cursor;
    }

    private static String getRawContactIdFromRawContractTable(String contactId){

        String rawContactId;

        // 获取RawContacts表的游标
        Cursor rawContactCur = contentResolver.query(RAW_CONTACT_ID, null,
                ContactsContract.RawContacts.CONTACT_ID + "=?",new String[] { contactId },null);
        if(null == rawContactCur){
            System.out.println("获取联系人RawContacts游标为空");
            return null;
        }

        // 该查询结果一般只返回一条记录，所以我们直接让游标指向第一条记录
        if (rawContactCur.moveToFirst()){
            // 读取第一条记录的RawContacts._ID列的值
            rawContactId = rawContactCur.getString(rawContactCur.getColumnIndex(ContactsContract.RawContacts._ID));
        }else{
            rawContactCur.close();
            return null;
        }
        // 关闭游标
        rawContactCur.close();
        return rawContactId;


    }
    private static ContactBean getOneItemDataFromContractDataTable(String rawContactId){
        ContactBean contactBean =  new ContactBean();

        // 根据查询RAW_CONTACT_ID查询该联系人的号码
        Cursor phoneCurosr = contentResolver.query(DATA_ID, null,
                ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + "=?", new String[] { rawContactId }, null);
        if (null == phoneCurosr){
            System.out.println("获取联系人Data游标为空");
            return null;
        }
        // 一个联系人可能有多个号码，需要遍历
        while (phoneCurosr.moveToNext()){

            // 获取号码
            String number = phoneCurosr.getString(phoneCurosr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            // 获取号码类型
            int type = phoneCurosr.getInt(phoneCurosr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            if (null != number){
                contactBean.setPhoneNumber(number);
            }
            contactBean.setPhoneType(type);
        }
        phoneCurosr.close();

        return contactBean;
    }





}
