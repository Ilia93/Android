package com.example.workapp.presentation.screen.timer.timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.presentation.screen.comment.CommentActivity;
import com.example.workapp.presentation.screen.comment.CommentDialog;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private RecyclerView timerRecyclerView;
    private List<TimerMenuModel> menuModelList = new ArrayList<>();

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
        timerRecyclerView = view.findViewById(R.id.timerRecyclerView);
        menuModelList.add(new TimerMenuModel("Watch comments", R.drawable.ic_watch_comments_48dp));
        menuModelList.add(new TimerMenuModel("Add comment", R.drawable.ic_add_commen_48dp));
        TimerAdapter.OnUserClickListener onUserClickListener = new TimerAdapter.OnUserClickListener() {
            @Override
            public void onClick(@NotNull TimerMenuModel timerMenuModel) {
                if (timerMenuModel.getText().equals("Watch comments")) {
                    Intent intent = new Intent(getContext(), CommentActivity.class);
                    startActivity(intent);
                } else if (timerMenuModel.getText().equals("Add comment")) {
                    CommentDialog commentDialog = new CommentDialog();
                    commentDialog.show(getActivity().getSupportFragmentManager(), "dialog");
                }
            }
        };
        TimerAdapter timerAdapter = new TimerAdapter(getActivity(), menuModelList, onUserClickListener);
        timerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        timerRecyclerView.setAdapter(timerAdapter);
        return view;
    }

    private void setOnClickListeners(View view) {
        setTimerClickListener(view, R.id.startTimer);
        setTimerClickListener(view, R.id.pauseTimer);
        setTimerClickListener(view, R.id.resumeTimer);
        setTimerClickListener(view, R.id.stopTimer);
    }

    private void setTimerClickListener(@NonNull View view, Integer id) {
        view.findViewById(id).setOnClickListener(v -> {
            switch (v.getId()) {
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
            }
        });
    }

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
        ((TextView) getActivity().findViewById(id)).setText(simpleDateFormat.format(date));
    }

    public interface TimerClickListener {

        void onTimerStarted();

        void onTimerStopped();

        void onTimerResumed();

        void onTimerPaused();
    }
}