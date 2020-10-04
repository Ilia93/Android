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
import com.example.workapp.presentation.screen.timer.operations.TimerOperations;
import com.example.workapp.presentation.service.notifications.NotificationService;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.workapp.presentation.service.notifications.NotificationService.NOTIFICATION_ID;

public class NotificationReceiver extends BroadcastReceiver {
    CommentsModel commentsModel = new CommentsModel();
    NotificationService notificationService = new NotificationService();
    TimerOperations timerOperations = new TimerOperations();
    NotificationCompat.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            getBroadcastMessage(remoteInput, intent, context);
        }
    }

    private void getBroadcastMessage(@NotNull Bundle remoteInput, @NotNull Intent intent,
                                     Context context) {
        setCommentsModelData(getReplyText(remoteInput), intent);
        buildNotification(context);
        App.putCommentOnServer(commentsModel);
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
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @NotNull
    private String getReplyText(@NotNull Bundle remoteInput) {
        return remoteInput
                .getCharSequence(NotificationService.KEY_TEXT_REPLY)
                .toString();
    }
}