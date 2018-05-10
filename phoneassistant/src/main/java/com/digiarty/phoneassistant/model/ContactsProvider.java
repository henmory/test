package com.digiarty.phoneassistant.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


import com.digiarty.phoneassistant.bean.ContactBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ContactsProvider {
    private static Logger logger = LoggerFactory.getLogger(ContactsProvider.class);

    private static class ContractsString{

        //contact table
        public Uri CONTACT_URI = ContactsContract.Contacts.CONTENT_URI;//contacts table
        public String TCONTACT_ID = ContactsContract.Contacts._ID;//contact id
        public String TCONTACT_HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        //raw contact table
        public Uri RAW_CONTACT_URI = ContactsContract.RawContacts.CONTENT_URI;//rawContacts table
        public String TRAW_ID = ContactsContract.RawContacts._ID;//raw id
        public String TRAW_CONTACT_ID = ContactsContract.RawContacts.CONTACT_ID;// contact id in raw table

        //data table
        public Uri DATA_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;//data table
        public String TDATA_ID = ContactsContract.CommonDataKinds.Phone._ID;// data id
        public String TDATA_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID;// contact id in data table
        public String TDATA_PHONE_NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
        public String TDATA_PHONE_TYPE = ContactsContract.CommonDataKinds.Phone.TYPE;

    }
    private static List<ContactBean> contacts = new ArrayList<>();
    private static ContractsString contractsString =  new ContractsString();

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

                String contact_id = cursor.getString(cursor.getColumnIndex(contractsString.TCONTACT_ID));
                logger.debug("contact_id = " + contact_id);

                //从RawContract表中获取RawContactId
                String rawContactId = getRawContactIdFromRawContractTable(contact_id);
                logger.debug("rawContactId = " + rawContactId);

                //从ContractDataTable表中获取数据
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(contractsString.TCONTACT_HAS_PHONE_NUMBER))) > 0){
                    ContactBean bean = getOneItemDataFromContractDataTable(rawContactId);
                    contacts.add(bean);
                }else{
                    logger.debug("rawContactId = " + rawContactId + "  的数据没有添加任何电话号码");
                    contacts = null;
                }
            }
            logger.debug("联系人数据读取完毕");
        }else{
            logger.debug("获取联系人Contract数据为空");
            contacts = null;
        }
        cursor.close();
        return contacts;
    }

    private static Cursor getContactsCursorFromContactsTable(){
        // 查询contacts表的所有记录===返回id跟是否有电话号码列
        Cursor cursor = contentResolver.query(contractsString.CONTACT_URI, new String[]{contractsString.TCONTACT_ID,
                contractsString.TCONTACT_HAS_PHONE_NUMBER}, null, null,null);
        if (null == cursor){
            logger.debug("获取联系人Contract游标为空");
            return null;
        }
        return cursor;
    }

    private static String getRawContactIdFromRawContractTable(String contactId){

        String rawContactId;

        // 获取RawContacts表的游标
        Cursor rawContactCur = contentResolver.query(contractsString.RAW_CONTACT_URI, null,
                contractsString.TRAW_CONTACT_ID + "=?",new String[] { contactId },null);
        if(null == rawContactCur){
            logger.debug("获取联系人RawContacts游标为空");
            return null;
        }

        // 该查询结果一般只返回一条记录，所以我们直接让游标指向第一条记录
        if (rawContactCur.moveToFirst()){
            // 读取第一条记录的RawContacts._ID列的值
            rawContactId = rawContactCur.getString(rawContactCur.getColumnIndex(contractsString.TRAW_ID));
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
        Cursor phoneCurosr = contentResolver.query(contractsString.DATA_URI, null,
                contractsString.TDATA_CONTACT_ID + "=?", new String[] { rawContactId }, null);
        if (null == phoneCurosr){
            logger.debug("获取联系人Data游标为空");
            return null;
        }
        // 一个联系人可能有多个号码，需要遍历
        while (phoneCurosr.moveToNext()){

            // 获取号码
            String number = phoneCurosr.getString(phoneCurosr.getColumnIndex(contractsString.TDATA_PHONE_NUM));

            // 获取号码类型
            int type = phoneCurosr.getInt(phoneCurosr.getColumnIndex(contractsString.TDATA_PHONE_TYPE));
            if (null != number){
                contactBean.setPhoneNumber(number);
            }
            contactBean.setPhoneType(type);
        }
        phoneCurosr.close();

        return contactBean;
    }





}
