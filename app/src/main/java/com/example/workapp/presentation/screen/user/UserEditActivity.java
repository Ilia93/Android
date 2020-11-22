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
import com.example.workapp.data.network.model.user.UserActionResult;
import com.example.workapp.data.network.model.user.UserCloudSource;
import com.example.workapp.data.network.model.user.UserModel;
import com.example.workapp.databinding.UserEditAccountBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.workapp.presentation.screen.user.UserAccountActivity.USER_OBJECT_ID;

public class UserEditActivity extends AppCompatActivity {

    public final static String USER_ID = "userId";
    public final static String USER_PREFERENCES_NAME = "userPreferencesName";
    public final static String USER_PREFERENCES_SECOND_NAME = "userPreferencesSecondName";
    public static boolean isUpdated = false;
    public final String USER_NAME = "userName";
    public final String USER_SECOND_NAME = "userSecondName";
    public final String USER_AGE = "userAge";
    public final String USER_GENDER = "userGender";
    public final String USER_WEIGHT = "userWeight";
    UserEditAccountBinding binding;
    UserModel userModel = new UserModel();
    SharedPreferences sharedPreferences;
    String selectedAge, selectedGender, selectedWeight;
    boolean isSelectedAge, isSelectedWeight, isSelectedGender = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserEditAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSpinnerAdapter();
        setClickListener();
    }

    // TODO: недостаточно информативное имя
    private void setSpinnerAdapter() {
        List<Integer> age = new ArrayList<>();
        List<Integer> weight = new ArrayList<>();
        initializeGenderSpinner();
        initializeSpinnerArrayAdapter(
                age,
                binding.userAgeSpinner,
                90, 10);
        initializeSpinnerArrayAdapter(
                weight,
                binding.userWeightSpinner,
                150, 15);
    }

    private void initializeSpinnerArrayAdapter(List<Integer> arrayList,
                                               Spinner spinner,
                                               int maxValue,
                                               int minValue) {
        for (int i = minValue; i < maxValue; i++) {
            arrayList.add(i);
        }
        ArrayAdapter<Integer> userAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                arrayList);
        userAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.equals(binding.userAgeSpinner)) {
                    if (!isSelectedAge) {
                        setFieldSelection(parent, USER_AGE, null);
                        isSelectedAge = true;
                    } else {
                        setFieldSelection(parent, null, binding.userAgeSpinner);
                    }

                } else if (spinner.equals(binding.userWeightSpinner)) {
                    if (!isSelectedWeight) {
                        setFieldSelection(parent, USER_WEIGHT, null);
                        isSelectedWeight = true;
                    } else {
                        setFieldSelection(parent, null, binding.userWeightSpinner);
                    }
                }
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
        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                gender);
        userAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        binding.userGenderSpinner.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSelectedGender) {
                    setFieldSelection(parent, USER_GENDER, null);
                    isSelectedGender = true;
                } else {
                    setFieldSelection(parent, null, binding.userGenderSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.userGenderSpinner.setAdapter(userAdapter);
    }

    private void setFieldSelection(@NotNull AdapterView<?> parent,
                                   String intentTag, Spinner spinner) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        if (intentTag != null) {
            ((TextView) parent.getChildAt(0)).setText(getIntent().getStringExtra(intentTag));
        } else {
            ((TextView) parent.getChildAt(0)).setText(spinner.getSelectedItem().toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeSharedPreferences();
        getUserAccountData();
    }

    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences(USER_ID, Context.MODE_PRIVATE);
    }

    private void getUserAccountData() {
        if (getIntent().getStringExtra(USER_NAME).equals("Undefined")) {
            binding.userEditName.setText(null);
            binding.userEditSecondName.setText(null);
        } else {
            binding.userEditName.setText(getIntent().getStringExtra(USER_NAME));
            binding.userEditSecondName.setText(getIntent().getStringExtra(USER_SECOND_NAME));
        }
    }

    private void setClickListener() {
        binding.userSaveEditData.setOnClickListener(v -> {
            try {
                setUserFields();
                saveUniqueUserData(clearOldUniqueUserId());
                checkUserStatus();
            } catch (NullPointerException exception) {
                showToastMessage("User name should be defined");
            }
        });
    }

    private void setUserFields() {
        getUserSpinnerData();
        checkIfUserNameDefined();
        setFlagToUpdateOrCreateUser();
        setUserModelData();
    }

    private void getUserSpinnerData() {
        selectedAge = binding.userAgeSpinner.getSelectedItem().toString();
        selectedGender = binding.userGenderSpinner.getSelectedItem().toString();
        selectedWeight = binding.userWeightSpinner.getSelectedItem().toString();
    }

    private void checkIfUserNameDefined() {
        userModel.setUserName(binding.userEditName.getText().toString());
        userModel.setUserSecondName(binding.userEditSecondName.getText().toString());
        if (userModel.getUserName().equals("")) {
            throw new NullPointerException();
        }
    }

    private void setFlagToUpdateOrCreateUser() {
        String userName = getIntent().getStringExtra(USER_NAME);
        String userSecondName = getIntent().getStringExtra(USER_SECOND_NAME);
        if (userModel.getUserName().equals(userName) &&
                userModel.getUserSecondName().equals(userSecondName)) {
            isUpdated = true;
        }
    }

    private void setUserModelData() {
        userModel.setUserAge(selectedAge);
        userModel.setUserGender(selectedGender);
        userModel.setUserWeight(selectedWeight);
    }

    private void saveUniqueUserData(@NotNull SharedPreferences.Editor editor) {
        editor.putString(USER_ID, userModel.getUserId());
        editor.putString(USER_PREFERENCES_NAME, userModel.getUserName());
        editor.putString(USER_PREFERENCES_SECOND_NAME, userModel.getUserSecondName());
        editor.apply();
    }

    @NotNull
    private SharedPreferences.Editor clearOldUniqueUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor;
    }

    private void checkUserStatus() {
        UserCloudSource userCloudSource = new UserCloudSource();
        userCloudSource.getUser(userModel.getUserName(), new UserActionResult() {
            @Override
            public void onSuccess(List<UserModel> users) {
                if (users.isEmpty() || !isUpdated) {
                    putUserDataToServer();
                } else {
                    for (int i = 0; i < users.size(); i++) {
                        if (userModel.getUserName()
                                .equals(users.get(i).getUserName())
                                && userModel.getUserSecondName()
                                .equals(users.get(i).getUserSecondName())) {
                            updateUser();
                            isUpdated = true;
                            Intent userAccountIntent = new Intent(getApplicationContext(),
                                    UserAccountActivity.class);
                            putUserAccountIntentExtras(userAccountIntent);
                            setResult(RESULT_OK, userAccountIntent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("User undefined");
            }
        });
    }

    private void putUserDataToServer() {
        NetworkClient.getInstance();
        Call<UserModel> call = NetworkClient.getUserApi().createUser(userModel);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NotNull Call<UserModel> call,
                                   @NotNull Response<UserModel> response) {
                if (response.isSuccessful()) {
                    showToastMessage("User created");
                    Intent userAccountIntent = new Intent(getApplicationContext(),
                            UserAccountActivity.class);
                    putUserAccountIntentExtras(userAccountIntent);
                    setResult(RESULT_OK, userAccountIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserModel> call, @NotNull Throwable t) {
                showToastMessage("User creation failed");
            }
        });
    }

    private void updateUser() {
        NetworkClient.getInstance();
        Call<UserModel> userModelCall = NetworkClient.getUserApi()
                .updateUser(USER_OBJECT_ID, userModel);
        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NotNull Call<UserModel> call,
                                   @NotNull Response<UserModel> response) {
                if (response.isSuccessful()) {
                    showToastMessage("User updated");
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserModel> call, @NotNull Throwable t) {
                showToastMessage("User isn't updates");
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

    @Override
    protected void onStop() {
        super.onStop();
        isSelectedAge = false;
        isSelectedWeight = false;
        isSelectedGender = false;
    }

    private void showToastMessage(String text) {
        Toast toast = Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}