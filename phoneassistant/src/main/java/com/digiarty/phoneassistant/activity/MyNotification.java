package com.digiarty.phoneassistant.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.digiarty.phoneassistant.R;

/***
 *
 * Created on：07/05/2018
 *
 * Created by：henmory
 *
 * Description:设置显示的前台服务的notification格式和channelID，在API26以上必须这个ID
 *
 *
 **/
public class MyNotification {

    public final static String CHANNEL_ID = "phoneAssistant";

    public static Notification buildForegroundNotification(Context context) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(context, CHANNEL_ID);

        b.setOngoing(true)
                .setContentTitle("手机助手")
                .setContentText("数据传输中....")
                .setSmallIcon(R.drawable.ic_launcher_foreground);
//                .setTicker(getString(R.string.downloading));

        return (b.build());
    }

    public static void createNotificationChannelForGlobalApplication(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString("");
//            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, null, importance);
//            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
