package com.example.workapp.presentation.screen.timer.timer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.databinding.TimerRecyclerViewMenuBinding;

import java.util.List;

public class TimerMenuAdapter extends RecyclerView
        .Adapter<TimerMenuAdapter.ViewHolder> {
    TimerRecyclerViewMenuBinding binding;
    private LayoutInflater layoutInflater;
    private List<TimerMenuModel> timerMenuModelList;
    private OnUserClickListener onUserClickListener;


    public TimerMenuAdapter(Context context, List<TimerMenuModel> timerMenuModelList,
                            OnUserClickListener onUserClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.timerMenuModelList = timerMenuModelList;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public TimerMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = TimerRecyclerViewMenuBinding.inflate(layoutInflater, parent, false);
        View view = binding.getRoot();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerMenuAdapter.ViewHolder holder, int position) {
        TimerMenuModel model = timerMenuModelList.get(position);
        holder.timerMenuText.setText(model.getText());
        holder.menuImage.setImageResource(model.getImage());
    }

    @Override
    public int getItemCount() {
        return timerMenuModelList.size();
    }

    public interface OnUserClickListener {
        void onClick(TimerMenuModel timerMenuModel);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView timerMenuText;
        final ImageView menuImage;
        final CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImage = binding.timerCommentSampleImage;
            timerMenuText = binding.timerCommentSampleText;
            cardView = binding.timerCardView;

            cardView.setOnClickListener(v -> {
                TimerMenuModel timerMenuModel = timerMenuModelList.get(getLayoutPosition());
                onUserClickListener.onClick(timerMenuModel);
            });
        }
    }
}
