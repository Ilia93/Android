package com.example.workapp.presentation.screen.timer.timer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workapp.R;
import com.example.workapp.data.network.model.timer.TimerActionResult;
import com.example.workapp.data.network.model.timer.TimerCloudDataSource;
import com.example.workapp.data.network.model.timer.TimerModel;
import com.example.workapp.databinding.TimerFinishWorkFragmentBinding;

import java.util.List;

public class StopTimerFragment extends Fragment {

    private TimerCloseable timerCloseable;
    private TimerFinishWorkFragmentBinding binding;
    private TimerModel timerModel = new TimerModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TimerFinishWorkFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setTimerCloseableListener(view);
        TimerCloudDataSource timerCloudDataSource = new TimerCloudDataSource();
        timerCloudDataSource.getTimer(timerModel.getStartTime(), new TimerActionResult() {
            @Override
            public void onSuccess(List<TimerModel> timerModel) {
                binding.timerStartResultView.setText(timerModel.get(timerModel.size() - 1).getStartTime());
                binding.timerFinishResultView.setText(timerModel.get(timerModel.size() - 1).getFinishTime());
                binding.elapsedTimeView.setText(timerModel.get(timerModel.size() - 1).getElapsedTime());
                binding.timeInPauseView.setText(timerModel.get(timerModel.size() - 1).getTimeInPause());
            }

            @Override
            public void onFailure(String message) {

            }
        });
        return view;
    }

    private void setTimerCloseableListener(@NonNull View view) {
        view.findViewById(R.id.timerExitFinishFragment).setOnClickListener(v -> timerCloseable.onTimerClosed());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface TimerCloseable {
        void onTimerClosed();
    }
}