package com.example.workapp.presentation.screen.timer.timer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.databinding.TimerRecyclerViewMenuBinding;

import java.util.List;

public class TimerMenuHolder extends RecyclerView.ViewHolder {
    TimerRecyclerViewMenuBinding binding;

    public TimerMenuHolder(@NonNull View itemView,
                           TimerMenuAdapter.OnUserClickListener onUserClickListener,
                           List<TimerMenuModel> timerMenuModelList) {
        super(itemView);
        binding = TimerRecyclerViewMenuBinding.bind(itemView);

        binding.timerCardView.setOnClickListener(v -> {
            TimerMenuModel timerMenuModel = timerMenuModelList.get(getLayoutPosition());
            onUserClickListener.onClick(timerMenuModel);
        });
    }
}