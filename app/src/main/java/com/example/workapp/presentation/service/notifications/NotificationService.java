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
    public static String KEY_TEXT_REPLY = "key_text_reply";

    public final String CHANNEL_ID = "1";
    public final String REPLY_TITLE = "Add comment";
    public final String NOTIFICATION_LEAVE = "Leave notification";
    public final String NOTIFICATION_WORK_ID = "work_id";
    public final String CHANNEL_NAME = "MyChannel";
    private final String NOTIFICATION_REPLY_ID = "Enter your text";
    private final IBinder iBinder = new LocalBinder();
    Handler handler = new Handler();
    Intent replyIntent;
    Intent leaveNotification;
    NotificationCompat.Action replyAction;
    NotificationCompat.Action leaveNotificationAction;
    PendingIntent pendingIntent;
    PendingIntent leaveNotificationPending;
    Runnable runnable;
    private long serviceTimerStartTime;
    private long milliseconds = 0L;
    private long minutes = 0L;
    private long seconds = 0L;
    private String resultString = null;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        serviceTimerStartTime = System.currentTimeMillis();
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

    public void createNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService
                (Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(notificationChannel);
    }

    public void createStartServiceNotification(@NotNull Intent intent) {
        createReplyAction(intent, createRemoteInput());
        createLeaveNotificationIntent();
        createNotification(intent);
    }

    private void createNotification(@NotNull Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                .setContentTitle(intent.getStringExtra("workNameForService"))
                .setContentText("0")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.running))
                .addAction(replyAction)
                .addAction(leaveNotificationAction)
                .setAutoCancel(true);
        startForeground(NOTIFICATION_ID, builder.build());
        startTimer(serviceTimerStartTime, intent);
    }

    public void createStopServiceNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                .setContentText(getResources().getString(R.string.service_stop))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.running))
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_STOP_ID, builder.build());
        stopForeground(true);
        handler.removeCallbacks(runnable);
        //TODO stopforeground and removecallbacks
    }

    @NotNull
    private RemoteInput createRemoteInput() {
        return new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(NOTIFICATION_REPLY_ID)
                .build();
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

        leaveNotificationPending = PendingIntent.getBroadcast(this, 0,
                leaveNotification, PendingIntent.FLAG_ONE_SHOT);

        leaveNotificationAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_clear_black_24dp, NOTIFICATION_LEAVE, leaveNotificationPending)
                .build();
    }

    public void startTimer(long timerStart, Intent intent) {
        runnable = new Runnable() {
            @Override
            public void run() {
                milliseconds = System.currentTimeMillis() - timerStart;
                seconds = (int) (milliseconds / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                resultString = minutes + " " + "min" + ":";
                resultString = resultString.concat(String.valueOf(seconds)) + " " + "sec";
                NotificationCompat.Builder builder = new NotificationCompat
                        .Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                        .setContentTitle(intent.getStringExtra("workNameForService"))
                        .setContentText(resultString)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.running))
                        .addAction(replyAction)
                        .addAction(leaveNotificationAction)
                        .setAutoCancel(true);
                startForeground(NOTIFICATION_ID, builder.build());
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }
}