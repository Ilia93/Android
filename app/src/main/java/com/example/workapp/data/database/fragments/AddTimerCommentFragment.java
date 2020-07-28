package com.example.workapp.data.database.fragments;
/*
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workapp.R;

public class AddTimerCommentFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        createComentsListener = (CreateCommentsListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_buttons_fragment, container, false);
        view.findViewById(R.id.createComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createComentsListener.createComment();
            }
        });
        view.findViewById(R.id.exitComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createComentsListener.exitComments();
            }
        });
        return view;
    }

    public interface CreateCommentsListener {

        void createComment();

        void exitComments();
    }

    private CreateCommentsListener createComentsListener;
}
*/