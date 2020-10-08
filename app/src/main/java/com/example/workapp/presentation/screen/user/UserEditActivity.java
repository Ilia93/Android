package com.example.workapp.presentation.screen.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.user.UserModel;
import com.example.workapp.databinding.UserEditAccountBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    String selectedAge, selectedGender, selectedWeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserEditAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setClickListener();
        setSpinnerAdapter();
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
                try {
                    setUserData();
                    saveUniqueUserId(clearOldUniqueUserId());
                    putUserDataToServer();
                    Intent userAccountIntent = new Intent(getApplicationContext(),
                            UserAccountActivity.class);
                    putUserAccountIntentExtras(userAccountIntent);
                    setResult(RESULT_OK, userAccountIntent);
                    finish();
                } catch (NullPointerException exception) {
                    showToastMessage("User name should be defined");
                }
            }
        });
    }

    private void setSpinnerAdapter() {
        List<Integer> age = new ArrayList<>();
        List<Integer> weight = new ArrayList<>();
        initializeGenderSpinner();
        initializeSpinnerArrayAdapter(age,
                binding.userAgeSpinner,
                90, 10,
                "Select user age");
        initializeSpinnerArrayAdapter(weight, binding.userWeightSpinner, 150, 15, "Select user weight");
    }

    private void initializeSpinnerArrayAdapter(List<Integer> arrayList, Spinner spinner, int maxValue,
                                               int minValue, String text) {
        for (int i = minValue; i < maxValue; i++) {
            arrayList.add(i);
        }
        ArrayAdapter<Integer> userAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_list_item_1,
                arrayList);
        userAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setText(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(userAdapter);
    }

    private void initializeGenderSpinner() {
        List<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Other");
        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                gender);
        userAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        binding.userGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setText("Select user gender");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.userGenderSpinner.setAdapter(userAdapter);
    }

    private void setUserData() {
        getSpinnerUserData();
        userNameAndSecondNameValidation();
        setOtherUserData();
    }

    private void getSpinnerUserData() {
        selectedAge = binding.userAgeSpinner.getSelectedItem().toString();
        selectedGender = binding.userGenderSpinner.getSelectedItem().toString();
        selectedWeight = binding.userWeightSpinner.getSelectedItem().toString();
    }

    private void userNameAndSecondNameValidation() {
        userModel.setUserName(binding.userEditName.getText().toString());
        userModel.setUserSecondName(binding.userEditSecondName.getText().toString());
        if (userModel.getUserName().equals("")) {
            throw new NullPointerException();
        }
    }

    private void setOtherUserData() {
        userModel.setUserAge(selectedAge);
        userModel.setUserGender(selectedGender);
        userModel.setUserWeight(selectedWeight);
    }

    private void saveUniqueUserId(@NotNull SharedPreferences.Editor editor) {
        editor.putString(USER_ID, userModel.getUserId());
        editor.apply();
    }

    @NotNull
    private SharedPreferences.Editor clearOldUniqueUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor;
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