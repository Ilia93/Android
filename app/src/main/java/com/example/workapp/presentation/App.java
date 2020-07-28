package com.example.workapp.presentation;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.comments.CommentsModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends Application {

    public static void putCommentOnServer(CommentsModel commentsModel) {
        Call<CommentsModel> call = NetworkClient.getCommentAPI().createComment(commentsModel);
        call.enqueue(new Callback<CommentsModel>() {
            @Override
            public void onResponse(@NonNull Call<CommentsModel> call,
                                   @NonNull Response<CommentsModel> response) {
                if (response.isSuccessful()) {

                } else {
                    if (response.errorBody() != null) {

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentsModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}