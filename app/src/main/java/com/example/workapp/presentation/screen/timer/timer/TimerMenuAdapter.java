package com.example.workapp.presentation.screen.timer.timer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;

import java.util.List;

public class TimerMenuAdapter extends RecyclerView.Adapter<TimerMenuHolder> {
    private final List<TimerMenuModel> timerMenuModelList;
    private final OnUserClickListener onUserClickListener;


    public TimerMenuAdapter(List<TimerMenuModel> timerMenuModelList,
                            OnUserClickListener onUserClickListener) {
        this.timerMenuModelList = timerMenuModelList;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public TimerMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timer_recycler_view_menu, parent, false);
        return new TimerMenuHolder(view, onUserClickListener, timerMenuModelList);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerMenuHolder holder, int position) {
        TimerMenuModel model = timerMenuModelList.get(position);
        holder.binding.timerCommentSampleText.setText(model.getText());
        holder.binding.timerCommentSampleImage.setImageResource(model.getImage());
    }

    @Override
    public int getItemCount() {
        return timerMenuModelList.size();
    }

    public interface OnUserClickListener {
        void onClick(TimerMenuModel timerMenuModel);
    }
}