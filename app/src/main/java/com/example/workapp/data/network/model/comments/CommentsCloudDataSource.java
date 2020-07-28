package com.example.workapp.data.network.model.comments;

import androidx.annotation.NonNull;

import com.example.workapp.data.network.NetworkClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class CommentsCloudDataSource {
    public void getComments(String comment, final CommentsActionResult actionResult) {
        NetworkClient.getInstance();
        Call<List<CommentsModel>> call = NetworkClient.getCommentAPI().getComment(comment);
        serverCall(call, actionResult);
    }

    private void serverCall(@NonNull Call<List<CommentsModel>> call, final CommentsActionResult actionResult) {
        call.enqueue(new Callback<List<CommentsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CommentsModel>> call, @NonNull Response<List<CommentsModel>> response) {
                if (response.isSuccessful()) {
                    if (actionResult != null) {
                        actionResult.onSuccess(response.body());
                    }
                } else {
                    if (actionResult != null) {
                        actionResult.onFailure("Error occurred");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CommentsModel>> call, @NonNull Throwable t) {
                if (actionResult != null) {
                    actionResult.onFailure("Error");
                }
            }
        });
    }
}