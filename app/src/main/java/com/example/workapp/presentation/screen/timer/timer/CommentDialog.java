package com.example.workapp.presentation.screen.timer.timer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.workapp.databinding.TimerDialogAddCommentBinding;

public class CommentDialog extends DialogFragment {

    TimerDialogAddCommentBinding binding;
    DialogListener dialogListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TimerDialogAddCommentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setListeners();
        return view;
    }


    private void setListeners() {
        binding.dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.dialogCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBackResult();
            }
        });
    }

    public void sendBackResult() {
        dialogListener = (DialogListener) getTargetFragment();
        dialogListener.onPositiveClicked(binding.inputDialogComment.getText().toString());
        dismiss();
    }

    public interface DialogListener {

        void onPositiveClicked(String inputText);
    }
}
