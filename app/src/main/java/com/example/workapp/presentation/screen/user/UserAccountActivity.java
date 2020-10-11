package com.example.workapp.presentation.screen.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.workapp.R;
import com.example.workapp.data.network.model.user.UserActionResult;
import com.example.workapp.data.network.model.user.UserCloudSource;
import com.example.workapp.data.network.model.user.UserModel;
import com.example.workapp.databinding.UserAccountBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static com.example.workapp.presentation.screen.user.UserEditActivity.USER_ID;
import static com.example.workapp.presentation.screen.user.UserEditActivity.USER_ID_PREFERENCES;

public class UserAccountActivity extends AppCompatActivity {

    public static final String USER_STORAGE_PHOTO_PATH = "user_photo_path";
    public static String photoPath;
    public final int REQUEST_CODE_EDIT_DATA = 0;
    public final int REQUEST_CODE_PHOTO = 1;
    public final int REQUEST_CODE_FILE_STORAGE = 2;
    SharedPreferences sharedPreferences;
    private UserAccountBinding binding;
    private UserModel userModel = new UserModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSharedPreferences();
        loadUserPhoto();
        loadUserDataFromServer();
        setClickListeners();
    }

    private void setSharedPreferences() {
        sharedPreferences = getSharedPreferences(USER_ID_PREFERENCES, MODE_PRIVATE);
    }

    private void loadUserPhoto() {
        ImageView imageView = findViewById(R.id.user_image);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (sharedPreferences.contains(USER_STORAGE_PHOTO_PATH)) {
                        Drawable drawable = Drawable.createFromPath
                                (sharedPreferences.getString(USER_STORAGE_PHOTO_PATH, " "));
                        imageView.setImageDrawable(drawable);
                    }
                } catch (NullPointerException exception) {
                    imageView.setImageResource(R.drawable.ic_baseline_account_circle_50);
                }
            }
        });
    }

    private void loadUserDataFromServer() {
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
        setSupportActionBar(binding.userToolbar);
        getSupportActionBar().setTitle(users.get(i).getUserName()
                + " "
                + users.get(i).getUserSecondName());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = findViewById(R.id.user_image);
        if (requestCode == REQUEST_CODE_EDIT_DATA && resultCode == RESULT_OK) {
            if (data != null) {
                binding.userName.setText(data.getStringExtra("userName"));
                binding.userSecondName.setText(data.getStringExtra("userSecondName"));
                binding.userAge.setText(data.getStringExtra("userAge"));
                binding.userGender.setText(data.getStringExtra("userGender"));
                binding.userWeight.setText(data.getStringExtra("userWeight"));
                binding.userImage.setImageResource(R.drawable.ic_baseline_account_circle_50);

                binding.userToolbar.setTitle(data.getStringExtra("userName")
                        + " "
                        + data.getStringExtra("userSecondName"));
                setSupportActionBar(binding.userToolbar);
            }
        } else if (requestCode == REQUEST_CODE_FILE_STORAGE && resultCode == RESULT_OK) {
            if (data != null) {
                getDataFromStorage(imageView, data);
            }
        } else if (requestCode == REQUEST_CODE_PHOTO && resultCode == RESULT_OK) {
            if (data != null) {
                galleryAddPhoto();
                setUserPhoto();
            }
        }
    }

    //TODO разобраться с тулбаром
    //TODO ND full screen
    private void getDataFromStorage(@NotNull ImageView imageView, @NotNull Intent data) {
        Uri selectedImage = data.getData();
        imageView.setImageURI(selectedImage);
    }

    private void galleryAddPhoto() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File galleryPathFile = new File(photoPath);
        Uri contentUri = Uri.fromFile(galleryPathFile);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private void setUserPhoto() {
        ImageView imageView = findViewById(R.id.user_image);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_STORAGE_PHOTO_PATH, photoPath);
        editor.apply();
        Drawable drawable = Drawable.createFromPath(photoPath);
        imageView.setImageDrawable(drawable);
    }

    private void showToastMessage(String text) {
        Toast toast = Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}