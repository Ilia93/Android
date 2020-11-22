package com.example.workapp.presentation.screen.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workapp.R;
import com.example.workapp.presentation.screen.main.MainActivity;

// TODO: избыточное имя
public class SplashScreenActivity extends AppCompatActivity {

    // TODO: мод. доступа
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        int SPLASH_DISPLAY_LENGTH = 4000;
        handler.postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            SplashScreenActivity.this.startActivity(intent);
        }, SPLASH_DISPLAY_LENGTH);
        // TODO: утечка
        finish();
    }
}
