package com.example.workapp.data.network.model.work;

import androidx.annotation.NonNull;

import com.example.workapp.data.network.NetworkClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class WorkCloudDataSource {
    // TODO: почитать про Java Callbacks + Anonymous function+ classes
    public void getWork(String workName, final WorkActionResult action) {
        NetworkClient.getInstance();
        Call<List<WorkModel>> call = NetworkClient.getWorkApi().getWorkName(workName);
        serverCall(call, action);
    }

    public void getWorkObjectId(String workName, final WorkActionResult action) {
        NetworkClient.getInstance();
        Call<List<WorkModel>> call = NetworkClient.getWorkApi().getWorkObjectId(workName);
        serverCall(call, action);
    }

    private void serverCall(@NonNull Call<List<WorkModel>> call, final WorkActionResult action) {
        call.enqueue(new Callback<List<WorkModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<WorkModel>> call,
                                   @NonNull Response<List<WorkModel>> response) {
                if (response.isSuccessful()) {
                    if (action != null) {
                        action.onSuccess(response.body());
                    }
                } else {
                    if (action != null) {
                        action.onFailure("Something went wrong.");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WorkModel>> call, @NonNull Throwable t) {
                if (action != null) {
                    action.onFailure(t.getMessage());
                }
            }
        });
    }
}