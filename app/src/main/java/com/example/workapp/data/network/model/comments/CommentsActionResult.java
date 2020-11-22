package com.example.workapp.data.network.model.comments;

import java.util.List;

// TODO: можно применить Java Generic
public interface CommentsActionResult {

    void onSuccess(List<CommentsModel> comments);

    void onFailure(String message);
}
