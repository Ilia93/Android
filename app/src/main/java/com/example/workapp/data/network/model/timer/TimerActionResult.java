package com.example.workapp.data.network.model.timer;

import java.util.List;

public interface TimerActionResult {

    void onSuccess(List<TimerModel> timerModel);

    void onFailure(String message);
}
