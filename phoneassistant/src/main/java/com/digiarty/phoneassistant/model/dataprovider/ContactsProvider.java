package com.digiarty.phoneassistant.model.dataprovider;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.digiarty.phoneassistant.bean.ContactBean;
import com.digiarty.phoneassistant.bean.ContactReadBean;
import com.digiarty.phoneassistant.model.bean.beanfromclient.AddContactDataFromClientBean;
import com.digiarty.phoneassistant.model.dataparse.ContactAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
class ContactsProvider {
    private static Logger logger = LoggerFactory.getLogger(ContactsProvider.class);

    // 获取用来操作数据的类的对象，对联系人的基本操作都是使用这个对象
    private ContentResolver contentResolver;
    private List<ContactReadBean> contactReadBeans = new ArrayList<>();
    private ContractsString contractsString = new ContractsString();

    public ContactsProvider(Context context) {
        contentResolver = context.getContentResolver();
    }

    public List<ContactReadBean> getContacts() {
        return getContactsDatasFromContactsApplication();
    }

    private static class ContractsString {

        //contact table
        public Uri CONTACT_URI = ContactsContract.Contacts.CONTENT_URI;//contacts table
        public String TCONTACT_ID = ContactsContract.Contacts._ID;//id
        public String TCONTACT_RAW_CONTACT_ID = ContactsContract.Contacts.NAME_RAW_CONTACT_ID;//raw contact id
        public String TCONTACT_HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        //raw contact table
        public Uri RAW_CONTACT_URI = ContactsContract.RawContacts.CONTENT_URI;//rawContacts table
        public String TRAW_ID = ContactsContract.RawContacts._ID;//id
        public String TRAW_CONTACT_ID = ContactsContract.RawContacts.CONTACT_ID;// raw contact id

        //data table
        public Uri DATA_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;//data table
        public String TDATA_ID = ContactsContract.CommonDataKinds.Phone._ID;// id
        public String TDATA_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID;// raw contact id
        public String TDATA_PHONE_NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
        public String TDATA_PHONE_TYPE = ContactsContract.CommonDataKinds.Phone.TYPE;

    }

    private List<ContactReadBean> getContactsDatasFromContactsApplication() {

        // 查询contacts表的所有记录
        Cursor cursor = getContactIdCursorFromContactsTable();
        if (null == cursor) {
            return null;
        }
        // 如果记录不为空
        if (cursor.getCount() > 0) {

            // 游标初始指向查询结果的第一条记录的上方，执行moveToNext函数会判断,下一条记录是否存在，如果存在，指向下一条记录。否则，返回false。
            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(contractsString.TCONTACT_ID));
                logger.debug("contact_id = " + contact_id);

                //从RawContract表中获取RawContactId
                String rawContactId = getRawContactIdFromRawContractTable(contact_id);
                logger.debug("rawContactId = " + rawContactId);

                //从contact表中获取是否有电话号码标志
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(contractsString.TCONTACT_HAS_PHONE_NUMBER))) > 0) {
                    ContactReadBean bean = getOneItemDataFromDataTable(rawContactId);
                    contactReadBeans.add(bean);
                } else {
                    logger.debug("rawContactId = " + rawContactId + "  的数据没有添加任何电话号码");
                    contactReadBeans = null;
                }
            }
            logger.debug("联系人数据读取完毕");
        } else {
            logger.debug("获取联系人Contract数据为空");
            contactReadBeans = null;
        }
        cursor.close();
        return contactReadBeans;
    }

    private Cursor getContactIdCursorFromContactsTable() {
        // 查询contacts表的所有记录===返回id跟是否有电话号码列
        String[] projection = new String[]{contractsString.TCONTACT_ID, contractsString.TCONTACT_HAS_PHONE_NUMBER};
        Cursor cursor = contentResolver.query(contractsString.CONTACT_URI, projection, null, null, null);
        if (null == cursor) {
            logger.debug("获取联系人Contract游标为空");
            return null;
        }
        return cursor;
    }

    private String getRawContactIdFromRawContractTable(String rawContactIdInContactTable) {

        String rawContactId;

        // 获取RawContacts表的游标
        String selection = contractsString.TRAW_CONTACT_ID + "=?";
        String[] selectionArgs = new String[]{rawContactIdInContactTable};
        String[] projection = new String[]{contractsString.TRAW_ID};
        Cursor rawContactCur = contentResolver.query(contractsString.RAW_CONTACT_URI, projection, selection, selectionArgs, null);
        if (null == rawContactCur) {
            logger.debug("获取联系人RawContacts游标为空");
            return null;
        }

        // 该查询结果一般只返回一条记录，所以我们直接让游标指向第一条记录
        if (rawContactCur.moveToFirst()) {
            // 读取第一条记录的RawContacts._ID列的值
            rawContactId = rawContactCur.getString(rawContactCur.getColumnIndex(contractsString.TRAW_ID));
        } else {
            rawContactCur.close();
            return null;
        }
        // 关闭游标
        rawContactCur.close();
        return rawContactId;


    }

    private ContactReadBean getOneItemDataFromDataTable(String rawContactId) {
        ContactReadBean contactReadBean = new ContactReadBean();

        // 根据查询RAW_CONTACT_ID查询该联系人的号码
        String[] projection = new String[]{};
        String selection = contractsString.TDATA_CONTACT_ID + "=?";
        String[] selectionArgs = new String[]{rawContactId};

        Cursor phoneCurosr = contentResolver.query(contractsString.DATA_URI, null, selection, selectionArgs, null);
        if (null == phoneCurosr) {
            logger.debug("获取联系人Data游标为空");
            return null;
        }
        // 一个联系人可能有多个号码，需要遍历
        while (phoneCurosr.moveToNext()) {

            // 获取号码
            String number = phoneCurosr.getString(phoneCurosr.getColumnIndex(contractsString.TDATA_PHONE_NUM));

            // 获取号码类型
            int type = phoneCurosr.getInt(phoneCurosr.getColumnIndex(contractsString.TDATA_PHONE_TYPE));
            if (null != number) {
                contactReadBean.setPhoneNumber(number);
            }
            contactReadBean.setPhoneType(type);
        }
        phoneCurosr.close();

        return contactReadBean;
    }

    private void insertOneContactBean(ContactBean bean) {

        //通过字段_id在raw_contacts表中查询目前通讯录含有多少条联系人，然后在已有的联系人数目上+1就是要插入联系人的_id.
        Cursor cursor = contentResolver.query(contractsString.RAW_CONTACT_URI, new String[]{"_id"}, null, null, null);
        if (null == cursor) {
            logger.debug("获取联系人Contract游标为空");
            return;
        }
        int num = findContactsNumber(cursor);

        ContentValues values = new ContentValues();
//        insertOneContactBeanInRawContactTable(values,num);
        values.put("contact_id", num);
        contentResolver.insert(contractsString.RAW_CONTACT_URI, values);
        values.clear();

        values.put("data1", "二五");
        values.put("mimetype", "vnd.android.cursor.item/name");
        values.put("raw_contact_id", num);
        contentResolver.insert(contractsString.DATA_URI, values);
        values.clear();

        values.put("data1", "12345678901");
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("raw_contact_id", num);
        contentResolver.insert(contractsString.DATA_URI, values);
        values.clear();

        values.put("data1", "1234@haha.com");
        values.put("mimetype", "vnd.android.cursor.item/email_v2");
        values.put("raw_contact_id", num);
        contentResolver.insert(contractsString.DATA_URI, values);
        cursor.close();
    }

    private int findContactsNumber(Cursor cursor) {
        int num = 1;
        if (cursor.moveToLast()) {
            int id = cursor.getColumnIndex(contractsString.TRAW_ID);
            num = id + 1;
        }

        return num;
    }

    private void insertOneContactBeanInRawContactTable(ContentValues values, int num) {
        values.put("contact_id", num);
        contentResolver.insert(contractsString.RAW_CONTACT_URI, values);
        values.clear();

    }


    public void test() {

        byte[] bytes = readPicture("/storage/emulated/0/Android/data/com.digiarty.phoneassistant/files/log/111.jpg");
        /*
         * Prepares the batch operation for inserting a new raw contact and its data. Even if
         * the Contacts Provider does not have any data for this person, you can't add a Contact,
         * only a raw contact. The Contacts Provider will then add a Contact automatically.
         */

        // Creates a new array of ContentProviderOperation objects.
        logger.debug("start");

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "tupe")
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "name");
        // Builds the operation and adds it to the array of operations
        ops.add(op.build());


        // Creates the display name for the new raw contact, as a StructuredName data row.
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                /*
                 * withValueBackReference sets the value of the first argument to the value of
                 * the ContentProviderResult indexed by the second argument. In this particular
                 * call, the raw contact ID column of the StructuredName data row is set to the
                 * value of the result returned by the first operation, which is the one that
                 * actually adds the raw contact row.
                 */
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                // Sets the data row's MIME type to StructuredName
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)

                // Sets the data row's display name to the name in the UI.
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "hmh");

        // Builds the operation and adds it to the array of operations
        ops.add(op.build());

        // Inserts the specified phone number and type as a Phone data row
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                /*
                 * Sets the value of the raw contact id column to the new raw contact ID returned
                 * by the first operation in the batch.
                 */
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                // Sets the data row's MIME type to Phone
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)

                // Sets the phone number and type
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, 13488783)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "住宅");

        // Builds the operation and adds it to the array of operations
        ops.add(op.build());

        // Inserts the specified email and type as a Phone data row
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                /*
                 * Sets the value of the raw contact id column to the new raw contact ID returned
                 * by the first operation in the batch.
                 */
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                // Sets the data row's MIME type to Email
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)

                // Sets the email address and type
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, "hanmh@gmail.com")
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "home");
        // Inserts the specified email and type as a Phone data row
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                /*
                 * Sets the value of the raw contact id column to the new raw contact ID returned
                 * by the first operation in the batch.
                 */
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                // Sets the data row's MIME type to Email
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)//缩略图通过

                // Sets the email address and type
                .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, bytes);

        /*
         * Demonstrates a yield point. At the end of this insert, the batch operation's thread
         * will yield priority to other threads. Use after every set of operations that affect a
         * single contact, to avoid degrading performance.
         */
        op.withYieldAllowed(true);

        // Builds the operation and adds it to the array of operations
        ops.add(op.build());


        /*
         * Applies the array of ContentProviderOperation objects in batch. The results are
         * discarded.
         */

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
            logger.debug("异常发生 " + e.getMessage());
        } catch (OperationApplicationException e) {
            e.printStackTrace();
            logger.debug("异常发生2 " + e.getMessage());
        }

        logger.debug("debug");

    }

    public byte[] readPicture(String path) {
        BufferedInputStream in = null;
        byte[] content = null;
        try {
            in = new BufferedInputStream(new FileInputStream(path));
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            in.close();
            content = out.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;

    }

    public byte[] insertData(Context context, List<ContactAction.ContactBeans> contactBeans) {

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ContactAction.ContactBeans bean;
        int rawContactInsertIndex = 0;
        for (int i = 0; i < contactBeans.size(); i++) {
            rawContactInsertIndex = ops.size();
            bean = contactBeans.get(i);

            //账户相关
            ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "tupe")
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "name");
            ops.add(op.build());
            //名字相关
            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, bean.getContactBean().getFirstName());
            ops.add(op.build());
            //电话相关
            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, bean.getContactBean().getPhoneNumberList().get(0).getValue())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "住宅");
            ops.add(op.build());

            //邮件相关
            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, bean.getContactBean().getEmailAddressList().get(0).getValue())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "home");
            ops.add(op.build());

            //头像相关
            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)//缩略图通过
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, bean.getImage());

            op.withYieldAllowed(true);

            ops.add(op.build());

            try {
                contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (RemoteException e) {
                e.printStackTrace();
                logger.debug("异常发生 " + e.getMessage());
            } catch (OperationApplicationException e) {
                e.printStackTrace();
                logger.debug("异常发生2 " + e.getMessage());
            }
        }
        logger.debug("debug");
        return null;
    }


}



