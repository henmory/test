package com.digiarty.phoneassistant.net.dataparse;

/***
 *
 * Created on：2018/5/25
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class CommandKey {

    public final static String KEY_COMMAND = "Command";
    public final static String KEY_ADD_CONTACTS = "AddContacts";
    public final static String KEY_NUM = "Num";
    public final static String KEY_RESULT = "Result";
    public final static String KEY_REQUEST = "Request";
    public final static String KEY_NID = "Nid";
    public final static String KEY_OID = "Oid";
    public final static String KEY_STATUS = "Status";


    private final static int BASE_CODE = 0;
    public final static int SUCCESS_CODE = BASE_CODE + 1;
    public final static int ERROR_CODE = BASE_CODE + 2;
    public final static int SOCKET_READ_DATA_ERROR = BASE_CODE + 3;
    public final static int PARSE_DATA_ERROR = BASE_CODE + 4;
    public final static int DO_ACTION_ERROR = BASE_CODE + 5;






}
