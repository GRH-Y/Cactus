package com.yyz.ard.cactus.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import util.StringEnvoy;


/**
 * 通知构建者
 */
public class NotificationBuilder {

    private Notification.Builder newBuilder = null;
    private NotificationCompat.Builder oldBuilder = null;
    private NotificationChannel notificationChannel = null;

    public static final int default_Notify_Id = 123;
    public static final String default_Channel_Id = "channel_123";
    public static final String default_Channel_Name = "channel_name_123";

    private Context mContext;
    private int mId = 0;
    private String mTag = null;
    private String mChannelId;
    private String mChannelName = null;

    public NotificationBuilder(Context context) {
        if (context == null) {
            throw new NullPointerException("NotificationBuilder context can not be null !!!");
        }
        this.mContext = context;
        this.mChannelId = default_Channel_Id;
        init();
    }

    public NotificationBuilder(Context context, String channelId) {
        if (context == null) {
            throw new NullPointerException("NotificationBuilder context can not be null !!!");
        }
        this.mContext = context;
        this.mChannelId = StringEnvoy.isEmpty(channelId) ? default_Channel_Id : channelId;
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            newBuilder = new Notification.Builder(mContext, mChannelId);
            newBuilder.setAutoCancel(true);
        } else {
            oldBuilder = new NotificationCompat.Builder(mContext, mChannelId);
            oldBuilder.setAutoCancel(true);
        }
    }

    public Notification.Builder getNewBuilder() {
        return newBuilder;
    }

    public NotificationCompat.Builder getOldBuilder() {
        return oldBuilder;
    }

    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    public String getTag() {
        return mTag;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getId() {
        return mId > 0 ? mId : default_Notify_Id;
    }

    public void setChannelName(String channelName) {
        this.mChannelName = channelName;
    }

    public void setCommonParameters(String title, String content, PendingIntent intent,
                                    @DrawableRes int icon, boolean ongoing) {
        if (newBuilder != null) {
            newBuilder.setContentTitle(title).setContentText(content).setContentIntent(intent)
                    .setSmallIcon(icon).setOngoing(ongoing);
        } else {
            oldBuilder.setContentTitle(title).setContentText(content).setContentIntent(intent)
                    .setSmallIcon(icon).setOngoing(ongoing);
        }
    }

    protected NotificationChannel getNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationChannel == null) {
            this.mChannelName = StringEnvoy.isEmpty(mChannelName) ? default_Channel_Name : mChannelName;
            notificationChannel = new NotificationChannel(mChannelId, mChannelName, NotificationManager.IMPORTANCE_HIGH);
        }
        return notificationChannel;
    }
}
