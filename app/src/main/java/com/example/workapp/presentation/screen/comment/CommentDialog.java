package com.example.workapp.presentation.screen.comment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.workapp.R;

public class CommentDialog extends DialogFragment {

    DialogListener dialogListener;
    private EditText userInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.timer_dialog_add_comment, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners(view);
        userInput = view.findViewById(R.id.inputDialogComment);
    }

    private void setListeners(View view) {
        setOnDialogListeners(view, R.id.dialog_cancel);
        setOnDialogListeners(view, R.id.dialog_create);
    }

    private void setOnDialogListeners(@NonNull View view, Integer id) {
        view.findViewById(id).setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.dialog_cancel:
                    dialogListener.onNegativeClicked();
                    break;
                case R.id.dialog_create:
                    dialogListener.onPositiveClicked(userInput.getText().toString());
                    break;
            }
        });
    }

    public interface DialogListener {

        void onPositiveClicked(String text);

        void onNegativeClicked();
    }
}
