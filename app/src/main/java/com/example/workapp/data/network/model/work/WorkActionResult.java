package com.example.workapp.data.network.model.work;

import java.util.List;

public interface WorkActionResult {

    void onSuccess(List<WorkModel> works);

    void onFailure(String message);
}