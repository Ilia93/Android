package com.example.workapp.presentation.screen.main;

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

public class ActivityTemplatesAdapter extends RecyclerView
        .Adapter<ActivityTemplatesAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<ActivityTemplatesModel> listOfTemplates;
    private OnUserClickListener onUserClickListener;


    public ActivityTemplatesAdapter(Context context
            , List<ActivityTemplatesModel> listOfTemplates
            , OnUserClickListener onUserClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.listOfTemplates = listOfTemplates;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.main_predefined_works_list, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityTemplatesModel workTemplates = listOfTemplates.get(position);
        holder.activityDescription.setImageResource(workTemplates.getWorkImage());
        holder.activityTemplate.setText(workTemplates.getActivityDescription());
    }

    @Override
    public int getItemCount() {
        return listOfTemplates.size();
    }

    public interface OnUserClickListener {
        void onUserClick(ActivityTemplatesModel activityTemplatesModel);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView activityDescription;
        final TextView activityTemplate;
        final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityDescription = itemView.findViewById(R.id.activityTemplateImage);
            activityTemplate = itemView.findViewById(R.id.activityDescription);
            cardView = itemView.findViewById(R.id.mainCardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityTemplatesModel activityTemplatesModel = listOfTemplates.get(getLayoutPosition());
                    onUserClickListener.onUserClick(activityTemplatesModel);
                }
            });
        }
    }
}
