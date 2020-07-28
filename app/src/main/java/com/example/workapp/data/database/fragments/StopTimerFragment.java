package com.example.workapp.data.database.fragments;
/*
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
import com.example.workapp.database.entity.AppTimer;
import com.example.workapp.database.room_dao.TimerDao;

import java.util.List;

import static com.example.workapp.presentation.screen.main.MainActivity.db;

public class StopTimerFragment extends Fragment {
    private TimerDao timerDao = db.timerDao();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        timerCloseable = (TimerCloseable) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_finish_work_fragment, container, false);
        view.findViewById(R.id.timerExitFinishFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerCloseable.onTimerClosed();
            }
        });
        List<AppTimer> finishedWork = timerDao.getall();
        int finishedWorkPosition = finishedWork.size() - 1;
        ((TextView) view.findViewById(R.id.timerStartResultView)).setText(finishedWork.get(finishedWorkPosition).getStartTime());
        ((TextView) view.findViewById(R.id.timerFinishResultView)).setText(finishedWork.get(finishedWorkPosition).getFinishTime());
        ((TextView) view.findViewById(R.id.elapsedTimeView)).setText(finishedWork.get(finishedWorkPosition).getElapsedTime());
        ((TextView) view.findViewById(R.id.timeInPauseView)).setText(finishedWork.get(finishedWorkPosition).getTimeInPause());
        return view;
    }

    public interface TimerCloseable {
        void onTimerClosed();
    }

    private TimerCloseable timerCloseable;
}
*/