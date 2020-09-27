package com.example.workapp.presentation.screen.main.recycler_view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;

public class WorkTemplatesHolder extends RecyclerView.ViewHolder {

    private TextView activityDescription;
    private ImageView workImage;
    private CardView cardView;
    //private OnUserClickListener onUserClickListener;

    public WorkTemplatesHolder(@NonNull View itemView) {
        super(itemView);
        activityDescription = itemView.findViewById(R.id.activityDescription);
        workImage = itemView.findViewById(R.id.activityTemplateImage);
        cardView = itemView.findViewById(R.id.mainCardView);

       /* cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainTemplatesModel mainTemplatesModel = listOfWorks.get(getLayoutPosition());
                onUserClickListener.onUserClick(mainTemplatesModel);
            }
        });*/
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public TextView getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(TextView activityDescription) {
        this.activityDescription = activityDescription;
    }

    public ImageView getWorkImage() {
        return workImage;
    }

    public void setWorkImage(ImageView workImage) {
        this.workImage = workImage;
    }

   /* interface OnUserClickListener {
        void onUserClick(MainTemplatesModel mainTemplatesModel);
    }*/
}
