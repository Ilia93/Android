package com.example.workapp.presentation.screen.user;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.workapp.R;

public class UserCameraDialog extends DialogFragment {

    private final int REQUEST_CODE_PHOTO = 1;
    private final int REQUEST_CODE_FILE_STORAGE = 2;

    public static UserCameraDialog getNewInstance() {
        return new UserCameraDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_get_photo_dialog, container, false);
        setListeners(view);
        return view;
    }

    private void setListeners(View view) {
        setOnDialogListeners(view, R.id.user_dialog_camera);
        setOnDialogListeners(view, R.id.user_dialog_storage);
        setOnDialogListeners(view, R.id.user_dialog_cancel);
    }

    private void setOnDialogListeners(@NonNull View view, Integer id) {
        view.findViewById(id).setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.user_dialog_camera:
                    Intent getPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(getPhoto, REQUEST_CODE_PHOTO);
                    dismiss();
                    break;
                case R.id.user_dialog_storage:
                    Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK);
                    pickPhotoIntent.setType("image/*");
                    getActivity().startActivityForResult(pickPhotoIntent, REQUEST_CODE_FILE_STORAGE);
                    dismiss();
                    break;
                case R.id.user_dialog_cancel:
                    dismiss();
                    break;
            }
        });
    }
}