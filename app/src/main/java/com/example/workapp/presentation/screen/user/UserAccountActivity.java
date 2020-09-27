package com.example.workapp.presentation.screen.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.example.workapp.R;
import com.example.workapp.data.network.model.user.UserActionResult;
import com.example.workapp.data.network.model.user.UserCloudSource;
import com.example.workapp.data.network.model.user.UserModel;
import com.example.workapp.databinding.UserAccountBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.workapp.presentation.screen.user.UserEditActivity.USER_ID;
import static com.example.workapp.presentation.screen.user.UserEditActivity.USER_ID_PREFERENCES;

public class UserAccountActivity extends AppCompatActivity {

    public final int REQUEST_CODE_EDIT_DATA = 0;
    public final int REQUEST_CODE_PHOTO = 1;
    public final int REQUEST_CODE_FILE_STORAGE = 2;
    SharedPreferences sharedPreferences;
    private String photoPath;
    private UserAccountBinding binding;
    private UserModel userModel = new UserModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.userToolbar);
        loadUserFromServer();
        setClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSharedPreferences();
    }

    private void setSharedPreferences() {
        sharedPreferences = getSharedPreferences(USER_ID_PREFERENCES, MODE_PRIVATE);
    }

    private void setClickListeners() {
        binding.userAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                UserCameraDialog userCameraDialog = UserCameraDialog.getNewInstance();
                userCameraDialog.show(fragmentManager, "user_fragment");
            }
        });
        binding.userEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editUserIntent = new Intent(getApplicationContext(), UserEditActivity.class);
                startActivityForResult(editUserIntent, REQUEST_CODE_EDIT_DATA);
            }
        });
    }

    private void loadUserFromServer() {
        UserCloudSource userCloudSource = new UserCloudSource();
        userCloudSource.getUser(userModel.getUserName(), new UserActionResult() {
            @Override
            public void onSuccess(List<UserModel> users) {
                if (users.size() == 0) {
                    setUndefinedFields();
                } else if (users.size() > 0) {
                    if (sharedPreferences.contains(USER_ID)) {
                        for (int i = 0; i < users.size(); i++) {
                            if (users.get(i).getUserId().
                                    equals(sharedPreferences.getString(USER_ID, ""))) {
                                setUserFields(users, i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("Failed to load user");
            }
        });
    }

    private void setUndefinedFields() {
        binding.userName.setText(R.string.user_empty_field);
        binding.userSecondName.setText(R.string.user_empty_field);
        binding.userAge.setText(R.string.user_empty_field);
        binding.userGender.setText(R.string.user_empty_field);
        binding.userWeight.setText(R.string.user_empty_field);
    }

    private void setUserFields(@NotNull List<UserModel> users, int i) {
        binding.userName.setText(users.get(i).getUserName());
        binding.userSecondName.setText(users.get(i).getUserSecondName());
        binding.userAge.setText(users.get(i).getUserAge());
        binding.userGender.setText(users.get(i).getUserGender());
        binding.userWeight.setText(users.get(i).getUserWeight());
        getSupportActionBar().setTitle(users.get(i).getUserName());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap;
        ImageView imageView = findViewById(R.id.user_image);
        if (requestCode == REQUEST_CODE_EDIT_DATA && resultCode == RESULT_OK) {
            if (data != null) {
                binding.userName.setText(data.getStringExtra("userName"));
                binding.userSecondName.setText(data.getStringExtra("userSecondName"));
                binding.userAge.setText(data.getStringExtra("userAge"));
                binding.userGender.setText(data.getStringExtra("userGender"));
                binding.userWeight.setText(data.getStringExtra("userWeight"));
            }
        } else if (requestCode == REQUEST_CODE_FILE_STORAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();
                imageView.setImageURI(selectedImage);
            }
        } else if (requestCode == REQUEST_CODE_PHOTO && resultCode == RESULT_OK) {
            if (data != null) {
                createStoragePhoto();
                galleryAddPhoto();
                Bundle imageExtra = data.getExtras();
                imageBitmap = (Bitmap) imageExtra.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }

    private void createStoragePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                showToastMessage("IO exception");

            }
            if (photoFile != null) {
                Uri imageURI = FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.android.fileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_PHOTO);
            }
        }
    }

    @NotNull
    private File createImageFile() throws IOException {
        String pattern = "MM-dd-yyyy";
        String timeTrigger = new SimpleDateFormat(pattern).format(new Date());
        String imageName = "Camera" + timeTrigger + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageName,
                ".jpg",
                storageDir
        );
        photoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void galleryAddPhoto() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File galleryPathFile = new File(photoPath);
        Uri contentUri = Uri.fromFile(galleryPathFile);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private void showToastMessage(String text) {
        Toast toast = Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}