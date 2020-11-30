package com.example.workapp.data.network.model.user;

import androidx.annotation.NonNull;

import com.example.workapp.data.network.NetworkClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class UserCloudSource {
    public void getUser(String userName, UserActionResult actionResult) {
        Call<List<UserModel>> call = NetworkClient.getInstance().getUserApi().getUser(userName);
        serverCall(call, actionResult);
    }

    public void getUserId(String userName, final UserActionResult userActionResult) {
        Call<List<UserModel>> call = NetworkClient.getInstance().getUserApi().getUserObjectId(userName);
        serverCall(call, userActionResult);
    }

    private void serverCall(@NonNull Call<List<UserModel>> call, final UserActionResult action) {
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserModel>> call,
                                   @NonNull Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    if (action != null) {
                        action.onSuccess(response.body());
                    }
                } else {
                    if (action != null) {
                        action.onFailure("Failed to load server data");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserModel>> call, @NonNull Throwable t) {
                if (action != null) {
                    action.onFailure(t.getMessage());
                }
            }
        });
    }
}
