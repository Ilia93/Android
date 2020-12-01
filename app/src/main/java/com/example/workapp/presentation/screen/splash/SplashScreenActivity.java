package com.example.workapp.presentation.screen.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.workapp.R;
import com.example.workapp.presentation.screen.main.MainActivity;

import java.lang.ref.WeakReference;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 4000;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        A a = new A(this);
        handler.postDelayed(a, SPLASH_DISPLAY_LENGTH);
    }

    private static final class A implements Runnable {

        private final WeakReference<Context> contextWeakRef;

        public A(Context context) {
            contextWeakRef = new WeakReference<>(context);
        }

        @Override
        public void run() {
            Context context = contextWeakRef.get();
            if (context instanceof FragmentActivity) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((FragmentActivity) context).finish();
            }
        }
    }
}

