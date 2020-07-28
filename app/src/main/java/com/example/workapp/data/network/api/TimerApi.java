package com.example.workapp.data.network.api;

import com.example.workapp.data.network.model.timer.TimerModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TimerApi {
    @GET("data/Timer")
    Call<List<TimerModel>> getTimer(@Query("start") String startTime);

    @POST("data/Timer")
    Call<TimerModel> createTimer(@Body TimerModel timerModel);

}