package com.example.workapp.data.database.fragments;
/*
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.workapp.presentation.screen.timer.timer.TimerFragment.isPaused;
import static com.example.workapp.presentation.screen.timer.timer.TimerFragment.isResumed;
import static com.example.workapp.presentation.screen.timer.timer.TimerFragment.isStarted;
import static com.example.workapp.presentation.screen.timer.timer.TimerFragment.timeOfPauseFinish;
import static com.example.workapp.presentation.screen.timer.timer.TimerFragment.timeOfPauseStart;
import static com.example.workapp.presentation.screen.timer.timer.TimerFragment.timeOfStart;

public class StartTimerFragment extends Fragment {
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public void onResume() {
        super.onResume();
        if (isStarted) {
            ((TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.timerStartView)).
                    setText(simpleDateFormat.format(timeOfStart));
            Objects.requireNonNull(getActivity()).findViewById(R.id.startTimer).setEnabled(false);
        }
        if (isPaused) {
            ((TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.timerPauseStartView)).
                    setText(simpleDateFormat.format(timeOfPauseStart));
        }
        if (isResumed) {
            ((TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.timerEndPauseView)).
                    setText(simpleDateFormat.format(timeOfPauseFinish));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        timerClickListener = (TimerClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_activity_main_fragment, container, false);
        view.findViewById(R.id.stopTimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerClickListener.onTimerStopped();
            }
        });
        view.findViewById(R.id.startTimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerClickListener.onTimerStarted();
            }
        });
        view.findViewById(R.id.pauseTimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerClickListener.onTimerPaused();
            }
        });
        view.findViewById(R.id.resumeTimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerClickListener.onTimerResumed();
            }
        });
        view.findViewById(R.id.addComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerClickListener.onTimerAddComment();
            }
        });
        view.findViewById(R.id.watchComments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerClickListener.watchComments();
            }
        });
        view.findViewById(R.id.timerExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerClickListener.timerExit();
            }
        });
        return view;
    }

    public interface TimerClickListener {

        void onTimerStarted();

        void onTimerStopped();

        void onTimerResumed();

        void onTimerPaused();

        void onTimerAddComment();

        void watchComments();

        void timerExit();

    }

    private TimerClickListener timerClickListener;

    public String setTime() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }

    public String calculateDifferense(Date dateOfFinish, Date dateOfStart) {
        long milliseconds = dateOfStart.getTime() - dateOfFinish.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        return (hours + " hours " + minutes + " minutes " + seconds + " seconds ");
    }
}*/