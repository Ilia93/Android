package com.example.workapp.data.network.api;

import com.example.workapp.data.network.model.comments.CommentsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentsApi {

    @GET("data/Comment")
    Call<List<CommentsModel>> getComment(@Query("text") String text);

    @POST("data/Comment")
    Call<CommentsModel> createComment(@Body CommentsModel commentModel);
}
