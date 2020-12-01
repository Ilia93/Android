package com.example.workapp.presentation.screen.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.workapp.databinding.UserGetPhotoDialogBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.workapp.presentation.screen.user.UserAccountActivity.photoPath;

public class UserCameraDialog extends DialogFragment {

    private final int REQUEST_CODE_PHOTO = 1;
    private final int REQUEST_CODE_FILE_STORAGE = 2;
    UserGetPhotoDialogBinding binding;

    public static UserCameraDialog getNewInstance() {
        return new UserCameraDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = UserGetPhotoDialogBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setOnDialogListeners();
        return view;
    }

    private void setOnDialogListeners() {
        binding.userDialogCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStoragePhoto();
                dismiss();
            }
        });
        binding.userDialogStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhotoIntent = new Intent
                        (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhotoIntent.setType("image/*");
                if (getActivity() != null) {
                    getActivity().startActivityForResult
                            (pickPhotoIntent, REQUEST_CODE_FILE_STORAGE);
                }
                dismiss();
            }
        });
        binding.userDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void createStoragePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    showToastMessage();
                }
                if (photoFile != null) {
                    Uri imageURI = FileProvider.getUriForFile(activity.getApplicationContext(),
                            "com.example.android.fileProvider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                    getActivity().startActivityForResult(takePictureIntent, REQUEST_CODE_PHOTO);
                }
            }
        }
    }

    @NotNull
    private File createImageFile() throws IOException {
        String pattern = "MM-dd-yyyy";
        String timeTrigger = new SimpleDateFormat(pattern).format(new Date());
        String imageName = "Camera" + timeTrigger + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageName,
                ".jpg",
                storageDir
        );
        photoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void showToastMessage() {
        Toast toast = Toast.makeText(getContext(), "IO exception", Toast.LENGTH_SHORT);
        toast.show();
    }
}