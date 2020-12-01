package com.example.workapp.presentation.screen.main.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.databinding.MainRecyclerViewWorkTemplatesBinding;
import com.example.workapp.presentation.screen.main.MainWorkAdapter;

import java.util.List;

public class WorkTemplatesHolder extends RecyclerView.ViewHolder {

    private MainRecyclerViewWorkTemplatesBinding binding;

    public WorkTemplatesHolder(@NonNull View itemView,
                               List<Object> listOfWorks,
                               MainWorkAdapter.OnUserCardClickListener onUserCardClickListener) {
        super(itemView);
        binding = MainRecyclerViewWorkTemplatesBinding.bind(itemView);

        binding.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkTemplatesModel workTemplatesModel =
                        (WorkTemplatesModel) listOfWorks.get(getLayoutPosition());
                onUserCardClickListener.onUserClick(workTemplatesModel);
            }
        });
    }

    public TextView getActivityDescription() {
        return binding.activityDescription;
    }

    public ImageView getWorkImage() {
        return binding.activityTemplateImage;
    }
}