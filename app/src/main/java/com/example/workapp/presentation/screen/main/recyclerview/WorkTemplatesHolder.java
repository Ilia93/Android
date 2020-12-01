package com.example.workapp.presentation.screen.main.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.databinding.MainRecyclerViewWorkTemplatesBinding;
import com.example.workapp.presentation.screen.main.MainWorkAdapter;

import java.util.List;

public class WorkTemplatesHolder extends RecyclerView.ViewHolder {

    private TextView activityDescription;
    private ImageView workImage;
    private CardView cardView;
    MainRecyclerViewWorkTemplatesBinding binding;

    public WorkTemplatesHolder(@NonNull View itemView,
                               List<Object> listOfWorks,
                               MainWorkAdapter.OnUserCardClickListener onUserCardClickListener) {
        super(itemView);
        activityDescription = itemView.findViewById(R.id.activityDescription);
        workImage = itemView.findViewById(R.id.activityTemplateImage);
        cardView = itemView.findViewById(R.id.mainCardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkTemplatesModel workTemplatesModel =
                        (WorkTemplatesModel) listOfWorks.get(getLayoutPosition());
                onUserCardClickListener.onUserClick(workTemplatesModel);
            }
        });
    }

    public TextView getActivityDescription() {
        return activityDescription;
    }

    public ImageView getWorkImage() {
        return workImage;
    }
}