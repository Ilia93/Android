package com.example.workapp.data.network.model.user;

import java.util.List;

public interface UserActionResult {

    void onSuccess(List<UserModel> users);

    void onFailure(String message);
}
