package com.example.workapp.data.network.api;

import com.example.workapp.data.network.model.user.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    @GET("data/User")
    Call<List<UserModel>> getUser(@Query("userName") String userName);

    @POST("data/User")
    Call<UserModel> createUser(@Body UserModel userModel);
}