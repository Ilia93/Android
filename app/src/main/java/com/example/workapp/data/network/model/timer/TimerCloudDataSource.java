package com.example.workapp.data.network.model.timer;

import androidx.annotation.NonNull;

import com.example.workapp.data.network.NetworkClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class TimerCloudDataSource {

    public void getTimer(String timer, final TimerActionResult actionResult) {
        Call<List<TimerModel>> call = NetworkClient.getInstance().getTimerAPI().getTimer(timer);
        serverCall(call, actionResult);
    }

    private void serverCall(@NonNull Call<List<TimerModel>> call, final TimerActionResult actionResult) {
        call.enqueue(new Callback<List<TimerModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<TimerModel>> call, @NonNull Response<List<TimerModel>> response) {
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
            public void onFailure(@NonNull Call<List<TimerModel>> call, @NonNull Throwable t) {
                if (actionResult != null) {
                    actionResult.onFailure(t.getMessage());
                }
            }
        });
    }
}