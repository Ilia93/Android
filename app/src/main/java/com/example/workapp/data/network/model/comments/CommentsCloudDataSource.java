package com.example.workapp.data.network.model.comments;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.workapp.data.network.NetworkClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class CommentsCloudDataSource {
    public void getComments(String comment, final CommentsActionResult actionResult) {
        Call<List<CommentsModel>> call = NetworkClient.getInstance().getCommentAPI().getComment(comment);
        serverCall(call, actionResult);
    }

    public void putCommentsToTheServer(CommentsModel commentsModel, Context context) {
        Call<CommentsModel> call = NetworkClient.getInstance().getCommentAPI().createComment(commentsModel);
        call.enqueue(new Callback<CommentsModel>() {
            @Override
            public void onResponse(@NonNull Call<CommentsModel> call,
                                   @NonNull Response<CommentsModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        showToastMessage("Comment created", context);
                    }
                } else {
                    if (response.errorBody() != null) {
                        showToastMessage("Failed to create comment", context);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentsModel> call, @NonNull Throwable t) {
                showToastMessage("Failed to put comment to the server", context);
            }
        });
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
                        actionResult.onFailure("Failed to load server data");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CommentsModel>> call, @NonNull Throwable t) {
                if (actionResult != null) {
                    actionResult.onFailure("Failed to load comments server data");
                }
            }
        });
    }

    private void showToastMessage(String text, Context context) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}