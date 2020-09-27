package com.example.workapp.presentation.screen.main.recycler_view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;

public class TextHolder extends RecyclerView.ViewHolder {
    private TextView textHeader;

    public TextHolder(@NonNull View itemView) {
        super(itemView);
        textHeader = itemView.findViewById(R.id.main_work_text_header);
    }

    public TextView getTextHeader() {
        return textHeader;
    }

    public void setTextHeader(TextView textHeader) {
        this.textHeader = textHeader;
    }
}
