package com.example.workapp.presentation.screen.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.user.UserModel;
import com.example.workapp.databinding.UserEditAccountBinding;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserEditActivity extends AppCompatActivity {

    public final static String USER_ID_PREFERENCES = "savedUserId";
    public final static String USER_ID = "userId";
    public final String USER_NAME = "userName";
    public final String USER_SECOND_NAME = "userSecondName";
    public final String USER_AGE = "userAge";
    public final String USER_GENDER = "userGender";
    public final String USER_WEIGHT = "userWeight";
    UserEditAccountBinding binding;
    UserModel userModel = new UserModel();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserEditAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeSharedPreferences();
    }

    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences(USER_ID_PREFERENCES, Context.MODE_PRIVATE);
    }

    private void setClickListener() {
        binding.userSaveEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserData();
                saveUniqueUserId(clearOldUniqueUserId());
                putUserDataToServer();
                Intent userAccountIntent = new Intent(getApplicationContext(),
                        UserAccountActivity.class);
                putUserAccountIntentExtras(userAccountIntent);
                setResult(RESULT_OK, userAccountIntent);
                finish();
            }
        });
    }

    @NotNull
    private SharedPreferences.Editor clearOldUniqueUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor;
    }

    private void setUserData() {
        userModel.setUserName(binding.userEditName.getText().toString());
        userModel.setUserSecondName(binding.userEditSecondName.getText().toString());
        userModel.setUserAge(binding.userEditAge.getText().toString());
        userModel.setUserGender(binding.userEditGender.getText().toString());
        userModel.setUserWeight(binding.userEditWeight.getText().toString());
    }

    private void saveUniqueUserId(@NotNull SharedPreferences.Editor editor) {
        editor.putString(USER_ID, userModel.getUserId());
        editor.apply();
    }

    private void putUserDataToServer() {
        NetworkClient.getInstance();
        Call<UserModel> call = NetworkClient.getUserApi().createUser(userModel);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    showToastMessage("User created");
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                showToastMessage("User creation failed");
            }
        });
    }

    private void putUserAccountIntentExtras(@NotNull Intent userAccountIntent) {
        userAccountIntent.putExtra(USER_NAME, userModel.getUserName());
        userAccountIntent.putExtra(USER_SECOND_NAME, userModel.getUserSecondName());
        userAccountIntent.putExtra(USER_AGE, userModel.getUserAge());
        userAccountIntent.putExtra(USER_GENDER, userModel.getUserGender());
        userAccountIntent.putExtra(USER_WEIGHT, userModel.getUserWeight());
    }

    private void showToastMessage(String text) {
        Toast toast = Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}