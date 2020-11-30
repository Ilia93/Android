package com.example.workapp.presentation.service.receiver;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.workapp.R;
import com.example.workapp.data.network.model.comments.CommentsModel;
import com.example.workapp.presentation.App;
import com.example.workapp.presentation.screen.main.MainActivity;
import com.example.workapp.presentation.screen.main.MainFragment;
import com.example.workapp.presentation.screen.timer.operations.TimerOperations;
import com.example.workapp.presentation.service.notifications.NotificationService;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.workapp.presentation.service.notifications.NotificationService.NOTIFICATION_ID;
import static com.example.workapp.presentation.service.notifications.NotificationService.STOP_NOTIFICATION;

public class NotificationReceiver extends BroadcastReceiver {
    public NotificationService notificationService = new NotificationService();
    private CommentsModel commentsModel = new CommentsModel();
    private TimerOperations timerOperations = new TimerOperations();
    private NotificationCompat.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            showBroadcastMessage(remoteInput, intent, context);
        } else {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(STOP_NOTIFICATION)) {
                    context.unbindService(MainFragment.serviceConnection);
                    Intent intent1 = new Intent(context, MainActivity.class);
                    context.startActivity(intent1);
                }
            }
        }
    }

    private void showBroadcastMessage(@NotNull Bundle remoteInput, @NotNull Intent intent,
                                      Context context) {
        setCommentsModelData(getReplyText(remoteInput), intent);
        buildNotification(context);
        App.putCommentOnServer(commentsModel, context);
        showNotification(context);
    }

    private void buildNotification(Context context) {
        builder = new NotificationCompat.Builder(context,
                notificationService.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_dialog_black_24dp)
                .setContentText("Received")
                .setAutoCancel(true);
        builder.build();
    }

    private void setCommentsModelData(String replyCommentText, @NotNull Intent intent) {
        commentsModel.setText(replyCommentText);
        commentsModel.setTime(timerOperations.setTime());
        commentsModel.setWorkId(intent.getStringExtra("work_id"));
    }

    private void showNotification(@NotNull Context context) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    @NotNull
    private String getReplyText(@NotNull Bundle remoteInput) {
        return remoteInput
                .getCharSequence(NotificationService.KEY_TEXT_REPLY)
                .toString();
    }
}