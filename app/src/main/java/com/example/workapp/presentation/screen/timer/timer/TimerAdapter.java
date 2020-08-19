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

import com.example.workapp.R;

import java.util.List;

public class TimerAdapter extends RecyclerView
        .Adapter<TimerAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<TimerMenuModel> timerMenuModelList;
    private OnUserClickListener onUserClickListener;


    public TimerAdapter(Context context, List<TimerMenuModel> timerMenuModelList,
                        OnUserClickListener onUserClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.timerMenuModelList = timerMenuModelList;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public TimerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.timer_list_of_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerAdapter.ViewHolder holder, int position) {
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
            menuImage = itemView.findViewById(R.id.timerCommentSampleImage);
            timerMenuText = itemView.findViewById(R.id.timerCommentSampleText);
            cardView = itemView.findViewById(R.id.timerCardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimerMenuModel timerMenuModel = timerMenuModelList.get(getLayoutPosition());
                    onUserClickListener.onClick(timerMenuModel);
                }
            });
        }
    }
}
