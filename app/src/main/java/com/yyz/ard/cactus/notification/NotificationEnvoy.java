package com.yyz.ard.cactus.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * 通知管理者
 */
public class NotificationEnvoy {
    private NotificationManager mManager = null;
    private static NotificationEnvoy sNotificationEnvoy = null;


    private NotificationEnvoy() {
    }

    public static synchronized NotificationEnvoy getInstance() {
        if (sNotificationEnvoy == null) {
            synchronized (NotificationEnvoy.class) {
                sNotificationEnvoy = new NotificationEnvoy();
            }
        }
        return sNotificationEnvoy;
    }

    public void init(Context context) {
        if (context == null) {
            throw new NullPointerException("NotificationEnvoy init() context can not be null !!!");
        }
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notifyNotification(NotificationBuilder builder) {
        if (mManager != null && builder != null) {
            Notification notification;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder newBuilder = builder.getNewBuilder();
                notification = newBuilder.build();
                mManager.createNotificationChannel(builder.getNotificationChannel());
            } else {
                NotificationCompat.Builder oidBuilder = builder.getOldBuilder();
                notification = oidBuilder.build();
            }
            mManager.notify(builder.getTag(), builder.getId(), notification);
        }
    }

    public void cancelNotification(NotificationBuilder builder) {
        if (mManager != null && builder != null) {
            mManager.cancel(builder.getTag(), builder.getId());
        }
    }

    public void cancelAll() {
        if (mManager != null) {
            mManager.cancelAll();
        }
    }
}
