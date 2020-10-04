package com.example.workapp.presentation.screen.main.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;

public class WorkHolder extends RecyclerView.ViewHolder {
    private TextView activityDescription;

    public WorkHolder(@NonNull View itemView) {
        super(itemView);
        activityDescription = itemView.findViewById(R.id.main_screen_work_name);
    }

    public void setActivityDescription(TextView activityDescription) {
        this.activityDescription = activityDescription;
    }

    public TextView getActivityDescription() {
        return activityDescription;
    }
}
