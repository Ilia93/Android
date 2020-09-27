package com.example.workapp.presentation.screen.main.recycler_view;

import android.widget.TextView;

public class TextHolderModel {
    TextView workHeader;

    public TextHolderModel(TextView workHeader) {
        this.workHeader = workHeader;
    }

    public TextView getWorkHeader() {
        return workHeader;
    }

    public void setWorkHeader(TextView workHeader) {
        this.workHeader = workHeader;
    }
}
