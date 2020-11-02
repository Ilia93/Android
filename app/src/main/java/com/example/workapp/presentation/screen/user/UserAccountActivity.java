package com.example.workapp.presentation.screen.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.workapp.R;
import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.user.UserActionResult;
import com.example.workapp.data.network.model.user.UserCloudSource;
import com.example.workapp.data.network.model.user.UserModel;
import com.example.workapp.databinding.UserAccountBinding;
import com.example.workapp.presentation.screen.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.workapp.presentation.screen.user.UserEditActivity.USER_ID;
import static com.example.workapp.presentation.screen.user.UserEditActivity.USER_PREFERENCES_NAME;
import static com.example.workapp.presentation.screen.user.UserEditActivity.USER_PREFERENCES_SECOND_NAME;
import static com.example.workapp.presentation.screen.user.UserEditActivity.isUpdated;

public class UserAccountActivity extends AppCompatActivity {

    public static final String USER_STORAGE_PHOTO_PATH = "user_photo_path";
    private static final String USER_NAME = "userName";
    private static final String USER_SECOND_NAME = "userSecondName";
    private static final String USER_AGE = "userAge";
    private static final String USER_WEIGHT = "userWeight";
    private static final String USER_GENDER = "userGender";
    public static String USER_OBJECT_ID = null;
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
        setSupportActionBar(binding.userToolbar);
        setSharedPreferences();
        loadUserPhoto();
        loadUserDataFromServer();
        setClickListeners();
    }

    private void setSharedPreferences() {
        sharedPreferences = getSharedPreferences(USER_ID, MODE_PRIVATE);
    }

    private void loadUserPhoto() {
        ImageView imageView = findViewById(R.id.user_image);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (sharedPreferences.contains(USER_STORAGE_PHOTO_PATH)) {
                        imageView.setImageBitmap(rotateImageOrientation(sharedPreferences
                                .getString(USER_STORAGE_PHOTO_PATH, " ")));
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
                if (users.isEmpty()) {
                    setUndefinedFields();
                } else {
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
                showToastMessage();
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
                putUserDataExtrasToTheEditActivity(editUserIntent);
            }
        });
    }

    private void putUserDataExtrasToTheEditActivity(@NotNull Intent intent) {
        intent.putExtra(USER_NAME, binding.userName.getText());
        intent.putExtra(USER_SECOND_NAME, binding.userSecondName.getText());
        intent.putExtra(USER_AGE, binding.userAge.getText());
        intent.putExtra(USER_GENDER, binding.userGender.getText());
        intent.putExtra(USER_WEIGHT, binding.userWeight.getText());
        startActivityForResult(intent, REQUEST_CODE_EDIT_DATA);
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
                isUpdated = false;
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

    private void getDataFromStorage(@NotNull ImageView imageView, @NotNull Intent data) {
        Uri selectedImage = data.getData();
        Bitmap Image = loadFromUri(selectedImage);
        imageView.setImageBitmap(Image);
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            if (Build.VERSION.SDK_INT > 27) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
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
        imageView.setImageBitmap(rotateImageOrientation(photoPath));
    }

    public Bitmap rotateImageOrientation(String photoFilePath) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        int rotationAngle = 90;
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        return rotatedBitmap;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserToolbarLabel();
        setUserObjectId();
    }

    private void setUserToolbarLabel() {
        sharedPreferences = getSharedPreferences(USER_ID, MODE_PRIVATE);
        if (sharedPreferences.contains(USER_PREFERENCES_NAME)) {
            binding.userCollapsingToolbar.setTitleEnabled(true);
            binding.userCollapsingToolbar
                    .setTitle(sharedPreferences.getString(USER_PREFERENCES_NAME, "")
                            + " "
                            + sharedPreferences.getString(USER_PREFERENCES_SECOND_NAME, ""));
        }
    }

    private void setUserObjectId() {
        NetworkClient.getInstance();
        Call<List<UserModel>> userModelCall = NetworkClient
                .getUserApi()
                .getUser(userModel.getUserName());
        userModelCall.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<UserModel>> call,
                                   @NotNull Response<List<UserModel>> response) {
                getUserObjectId();
            }

            @Override
            public void onFailure(@NotNull Call<List<UserModel>> call, @NotNull Throwable t) {

            }
        });
    }

    private void getUserObjectId() {
        UserCloudSource userCloudSource = new UserCloudSource();
        userCloudSource.getUserId(userModel.getUserName(), new UserActionResult() {
            @Override
            public void onSuccess(List<UserModel> users) {
                findUserObjectIdOnServer(users);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void findUserObjectIdOnServer(@NotNull List<UserModel> users) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(sharedPreferences.getString(USER_ID, " "))) {
                userModel.setUserObjectId(users.get(i).getUserObjectId());
                USER_OBJECT_ID = userModel.getUserObjectId();
            }
        }
    }

    private void showToastMessage() {
        Toast toast = Toast.makeText(getApplication(), "Failed to load user", Toast.LENGTH_SHORT);
        toast.show();
    }
}