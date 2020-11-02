package com.example.workapp.presentation.screen.timer.timer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.workapp.R;
import com.example.workapp.data.network.model.timer.TimerActionResult;
import com.example.workapp.data.network.model.timer.TimerCloudDataSource;
import com.example.workapp.data.network.model.timer.TimerModel;
import com.example.workapp.databinding.TimerFinishWorkFragmentBinding;
import com.example.workapp.presentation.screen.main.MainFragment;

import java.util.List;

public class StopTimerFragment extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment mainFragment = new MainFragment();
    private TimerFinishWorkFragmentBinding binding;
    private TimerModel timerModel = new TimerModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TimerFinishWorkFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        TimerCloudDataSource timerCloudDataSource = new TimerCloudDataSource();
        timerCloudDataSource.getTimer(timerModel.getStartTime(), new TimerActionResult() {
            @Override
            public void onSuccess(List<TimerModel> timerModel) {
                binding.timerStartResultView.setText
                        (timerModel.get(timerModel.size() - 1).getStartTime());
                binding.timerFinishResultView.setText
                        (timerModel.get(timerModel.size() - 1).getFinishTime());
                binding.elapsedTimeView.setText
                        (timerModel.get(timerModel.size() - 1).getElapsedTime());
                binding.timeInPauseView.setText
                        (timerModel.get(timerModel.size() - 1).getTimeInPause());
            }

            @Override
            public void onFailure(String message) {

            }
        });
        binding.timerExitFinishFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(mainFragment);
            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragmentName) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            fragmentManager = activity.getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.navigation_content_frame, fragmentName);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}