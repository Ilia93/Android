package com.example.workapp.data.network.api;

import com.example.workapp.data.network.model.work.WorkModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WorkApi {

    @GET("data/Work")
    Call<List<WorkModel>> getWorkName(@Query("workName") String workName);

    @GET("data/Work")
    Call<List<WorkModel>> getWorkObjectId(@Query("objectId") String workName);

    @POST("data/Work")
    Call<WorkModel> createWork(@Body WorkModel workModel);

    @PUT("data/Work/{objectID}")
    Call<WorkModel> updateWork(@Path("objectID")String objectID, @Body WorkModel workModel);
}