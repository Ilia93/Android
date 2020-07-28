package com.example.workapp.data.network;

import com.example.workapp.data.network.api.CommentsApi;
import com.example.workapp.data.network.api.TimerApi;
import com.example.workapp.data.network.api.WorkApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkClient {

    private static final String BASE_URL = "https://api.backendless.com/BDFBA7B2-0396-E510-FF7F-4B5685F7A600/B2EBB787-A086-4E1E-B1EB-B1EFC125A8F8/";

    private static volatile NetworkClient instance;

    private static Retrofit retrofit;

    private static WorkApi workApi;
    private static TimerApi timerApi;
    private static CommentsApi commentsApi;

    public static NetworkClient getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (NetworkClient.class) {
            if (instance == null) {
                instance = new NetworkClient();
            }
        }
        return instance;
    }

    private NetworkClient() {
        setupRetrofit();
    }

    private void setupRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static WorkApi getWorkApi() {
        if (workApi == null) {
            workApi = retrofit.create(WorkApi.class);
        }
        return workApi;
    }

    public static TimerApi getTimerAPI() {
        if (timerApi == null) {
            timerApi = retrofit.create(TimerApi.class);
        }
        return timerApi;
    }

    public static CommentsApi getCommentAPI() {
        if (commentsApi == null) {
            commentsApi = retrofit.create(CommentsApi.class);
        }
        return commentsApi;
    }
}