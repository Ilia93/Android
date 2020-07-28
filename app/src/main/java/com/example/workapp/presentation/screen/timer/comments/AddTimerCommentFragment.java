package com.example.workapp.presentation.screen.timer.comments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workapp.R;
import com.example.workapp.presentation.screen.timer.timer.TimerActivity;

import org.jetbrains.annotations.NotNull;

public class AddTimerCommentFragment extends Fragment {

    private CreateCommentsListener createCommentsListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TimerActivity) {
            createCommentsListener = (CreateCommentsListener) context;
        } else {
            throw new ClassCastException(context.toString() + "wrong implementation");
        }
    }

    public interface CreateCommentsListener {

        void createComment();

        void exitComments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_buttons_fragment, container, false);
        setCreateCommentsListener(view, R.id.createComment);
        setCreateCommentsListener(view, R.id.exitComment);
        return view;
    }

    private void setCreateCommentsListener(@NotNull View view, Integer id) {
        view.findViewById(id).setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.createComment:
                    createCommentsListener.createComment();
                    break;
                case R.id.exitComment:
                    createCommentsListener.exitComments();
                    break;
            }
        });
    }
}