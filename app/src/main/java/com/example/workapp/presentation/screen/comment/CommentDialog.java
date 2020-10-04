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
        View view = inflater.inflate(R.layout.timer_dialog_add_comment, container, false);
        userInput = view.findViewById(R.id.inputDialogComment);
        setListeners(view);
        return view;
    }

    private void setListeners(View view) {
        setOnDialogListeners(view, R.id.dialog_cancel);
        setOnDialogListeners(view, R.id.dialog_create);
    }

    private void setOnDialogListeners(@NonNull View view, Integer id) {
        view.findViewById(id).setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.dialog_cancel:
                    dismiss();
                    break;
                case R.id.dialog_create:
                    sendBackResult();
                    break;
            }
        });
    }

    public void sendBackResult() {
        dialogListener = (DialogListener) getTargetFragment();
        dialogListener.onPositiveClicked(userInput.getText().toString());
        dismiss();
    }

    public interface DialogListener {

        void onPositiveClicked(String inputText);
    }
}
