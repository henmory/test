package com.digiarty.phoneassistant.model;

import android.content.Context;
import android.provider.Settings;

/***
 *
 * Created on：10/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
class SettingsProvider {
    private Context mContext;

    public SettingsProvider(Context mContext) {
        this.mContext = mContext;
    }

    public static void getSettingsDatas(Context context){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            String globalValue = Settings.Global.getString(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON);
            System.out.println("global = " + globalValue);
            String systemValue = Settings.System.getString(context.getContentResolver(), Settings.System.DATE_FORMAT);
            System.out.println("sysytem = " + systemValue);
            String secureValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
            System.out.println("secure = " + secureValue);
        }else{
            int globalValue = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);

            System.out.println("global" + globalValue);
        }
    }
}
