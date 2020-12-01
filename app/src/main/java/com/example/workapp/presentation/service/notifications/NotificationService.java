package com.example.workapp.presentation.service.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.example.workapp.R;
import com.example.workapp.presentation.service.receiver.NotificationReceiver;

import org.jetbrains.annotations.NotNull;

public class NotificationService extends Service {

    public static final int NOTIFICATION_ID = 1;
    public static final int NOTIFICATION_STOP_ID = 2;
    public static final String NOTIFICATION_LEAVE = "Leave notification";
    public static final String STOP_NOTIFICATION = "com.example.service.LEAVE_NOTIFICATION";
    public static String KEY_TEXT_REPLY = "key_text_reply";
    public final String CHANNEL_ID = "1";
    public final String STOP_SERVICE = "Service stopped";
    public final String EMPTY_TIMER = "0";
    public final String START_TIMER_KEY = "1";
    public final String REPLY_TITLE = "Add comment";
    public final String NOTIFICATION_WORK_ID = "work_id";
    public final String CHANNEL_NAME = "MyChannel";
    private final String NOTIFICATION_REPLY_ID = "Enter your text";
    private final IBinder iBinder = new LocalBinder();
    public Intent leaveNotification, replyIntent;
    public NotificationCompat.Action replyAction, leaveNotificationAction;
    private Handler handler = new Handler();
    private PendingIntent pendingIntent, leaveNotificationPending;
    private Runnable runnable;
    private long serviceTimerStartTime;
    private long milliseconds, minutes, seconds = 0L;
    private String resultString = null;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        serviceTimerStartTime = System.currentTimeMillis();
    }

    public void createNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService
                (Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(notificationChannel);
    }

    @Override
    public IBinder onBind(@NotNull Intent intent) {
        createStartServiceNotification(intent);
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        createStopServiceNotification();
        return false;
    }


    public void createStartServiceNotification(@NotNull Intent intent) {
        createReplyAction(intent, createRemoteInput());
        createLeaveNotificationIntent();
        createNotification(intent, 0, EMPTY_TIMER);
    }

    private void createReplyAction(@NotNull Intent intent, RemoteInput remoteInput) {
        replyIntent = new Intent(this, NotificationReceiver.class);
        replyIntent.putExtra(NOTIFICATION_WORK_ID, intent.getStringExtra("workId"));

        pendingIntent = PendingIntent.getBroadcast(this, 0,
                replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_send_black_24dp, REPLY_TITLE, pendingIntent)
                .addRemoteInput(remoteInput)
                .build();
    }

    public void createLeaveNotificationIntent() {
        leaveNotification = new Intent(this, NotificationReceiver.class);
        leaveNotification.putExtra(NOTIFICATION_LEAVE, NOTIFICATION_STOP_ID);
        leaveNotification.setAction(STOP_NOTIFICATION);
        leaveNotificationPending = PendingIntent.getBroadcast(this, 0,
                leaveNotification, PendingIntent.FLAG_UPDATE_CURRENT);

        leaveNotificationAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_clear_black_24dp, NOTIFICATION_LEAVE, leaveNotificationPending)
                .build();
    }

    private void createNotification(@NotNull Intent intent, long timerStart, String contentText) {
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                .setContentTitle(intent.getStringExtra("workNameForService"))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.running))
                .addAction(replyAction)
                .addAction(leaveNotificationAction)
                .setAutoCancel(true);
        if (contentText.equals(EMPTY_TIMER)) {
            builder.setContentText(contentText);
            startTimer(serviceTimerStartTime, intent);
        } else if (contentText.equals(START_TIMER_KEY)) {
            builder.setContentText(initStartTimerField(timerStart));
        }
        startForeground(NOTIFICATION_ID, builder.build());
    }

    public void createStopServiceNotification() {
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                .setContentText(STOP_SERVICE)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.running))
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService
                (NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_STOP_ID, builder.build());
        }
        stopForeground(true);
        handler.removeCallbacks(runnable);
    }

    @NotNull
    private RemoteInput createRemoteInput() {
        return new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(NOTIFICATION_REPLY_ID)
                .build();
    }

    public void startTimer(long timerStart, Intent intent) {
        runnable = new Runnable() {
            @Override
            public void run() {
                createNotification(intent, timerStart, START_TIMER_KEY);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    private String initStartTimerField(long timerStart) {
        milliseconds = System.currentTimeMillis() - timerStart;
        seconds = (int) (milliseconds / 1000);
        minutes = seconds / 60;
        seconds = seconds % 60;
        resultString = minutes + " " + "min" + ":";
        resultString = resultString.concat(String.valueOf(seconds)) + " " + "sec";
        return resultString;
    }

    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }
}