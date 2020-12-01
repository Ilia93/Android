package com.example.workapp.presentation;

import android.app.Application;
import android.content.Context;

import com.example.workapp.data.network.model.comments.CommentsCloudDataSource;
import com.example.workapp.data.network.model.comments.CommentsModel;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static void putCommentOnServer(CommentsModel commentsModel, Context context) {
        CommentsCloudDataSource commentsCloudDataSource = new CommentsCloudDataSource();
        commentsCloudDataSource.putCommentsToTheServer(commentsModel, context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}