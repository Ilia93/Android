package com.example.workapp.presentation.screen.timer.timer;

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
import java.util.Date;
import java.util.Objects;

import static com.example.workapp.presentation.screen.timer.timer.TimerActivity.isPaused;
import static com.example.workapp.presentation.screen.timer.timer.TimerActivity.isResumed;
import static com.example.workapp.presentation.screen.timer.timer.TimerActivity.isStarted;
import static com.example.workapp.presentation.screen.timer.timer.TimerActivity.timeOfTimerPauseFinish;
import static com.example.workapp.presentation.screen.timer.timer.TimerActivity.timeOfTimerPauseStart;
import static com.example.workapp.presentation.screen.timer.timer.TimerActivity.timeOfTimerStart;

public class StartTimerFragment extends Fragment {

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private TimerClickListener timerClickListener;

    public void onResume() {
        super.onResume();
        saveData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TimerActivity) {
            timerClickListener = (TimerClickListener) context;
        } else {
            throw new ClassCastException(context.toString() + "wrong implementation");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_activity_main_fragment, container, false);
        setOnClickListeners(view);
        return view;
    }

    private void setOnClickListeners(View view) {
        setTimerClickListener(view, R.id.startTimer);
        setTimerClickListener(view, R.id.addComment);
        setTimerClickListener(view, R.id.pauseTimer);
        setTimerClickListener(view, R.id.resumeTimer);
        setTimerClickListener(view, R.id.stopTimer);
        setTimerClickListener(view, R.id.watchComments);
        setTimerClickListener(view, R.id.timerExit);
    }

    private void setTimerClickListener(@NonNull View view, Integer id) {
        view.findViewById(id).setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.timerExit:
                    timerClickListener.onTimerExit();
                    break;
                case R.id.startTimer:
                    timerClickListener.onTimerStarted();
                    break;
                case R.id.stopTimer:
                    timerClickListener.onTimerStopped();
                    break;
                case R.id.pauseTimer:
                    timerClickListener.onTimerPaused();
                    break;
                case R.id.resumeTimer:
                    timerClickListener.onTimerResumed();
                    break;
                case R.id.addComment:
                    timerClickListener.onTimerAddComment();
                    break;
                case R.id.watchComments:
                    timerClickListener.onTimerWatchComments();
                    break;
            }
        });
    }

    //toDO: избавиться от этого в будущем
    private void saveData() {
        if (isStarted) {
            setData(R.id.timerStartView, timeOfTimerStart);
            getActivity().findViewById(R.id.startTimer).setEnabled(false);
        }
        if (isPaused) {
            setData(R.id.timerPauseStartView, timeOfTimerPauseStart);
        }
        if (isResumed) {
            setData(R.id.timerEndPauseView, timeOfTimerPauseFinish);
        }
    }

    private void setData(Integer id, Date date) {
        ((TextView) Objects.requireNonNull(getActivity()).findViewById(id)).
                setText(simpleDateFormat.format(date));
    }

    public interface TimerClickListener {

        void onTimerStarted();

        void onTimerStopped();

        void onTimerResumed();

        void onTimerPaused();

        void onTimerAddComment();

        void onTimerWatchComments();

        void onTimerExit();

    }
}