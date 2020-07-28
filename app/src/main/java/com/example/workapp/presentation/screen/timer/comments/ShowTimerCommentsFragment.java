package com.example.workapp.presentation.screen.timer.comments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workapp.R;
import com.example.workapp.data.network.model.comments.CommentsActionResult;
import com.example.workapp.data.network.model.comments.CommentsCloudDataSource;
import com.example.workapp.data.network.model.comments.CommentsModel;
import com.example.workapp.databinding.CommentsShowFragmentBinding;

import java.util.List;

public class ShowTimerCommentsFragment extends Fragment {

    private CommentsModel commentsModel = new CommentsModel();
    private CommentsShowFragmentBinding commentsBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        commentsBinding = CommentsShowFragmentBinding.inflate(inflater, container, false);
        View view = commentsBinding.getRoot();

        CommentsCloudDataSource commentsCloudDataSource = new CommentsCloudDataSource();
        commentsCloudDataSource.getComments(commentsModel.getText(), new CommentsActionResult() {
            @Override
            public void onSuccess(List<CommentsModel> comments) {
                handleComments(comments);
            }

            @Override
            public void onFailure(String message) {

            }
        });
        return view;
    }

    private void handleComments(@NonNull List<CommentsModel> comments) {
        switch (comments.size()) {
            case 0:
                commentsBinding.showFirstComment.setText(R.string.comments_no_comments);
                break;
            case 1:
                displayTimerComments(commentsBinding.showFirstComment, 1, comments);
                break;
            case 2:
                displayTimerComments(commentsBinding.showFirstComment, 1, comments);
                displayTimerComments(commentsBinding.showSecondComment, 2, comments);
                break;
            case 3:
                displayTimerComments(commentsBinding.showFirstComment, 1, comments);
                displayTimerComments(commentsBinding.showSecondComment, 2, comments);
                displayTimerComments(commentsBinding.showThirdComment, 3, comments);
                break;
            case 4:
                displayTimerComments(commentsBinding.showFirstComment, 1, comments);
                displayTimerComments(commentsBinding.showSecondComment, 2, comments);
                displayTimerComments(commentsBinding.showThirdComment, 3, comments);
                displayTimerComments(commentsBinding.showFourthComment, 4, comments);
                break;
            default:
                displayTimerComments(commentsBinding.showFirstComment, 1, comments);
                displayTimerComments(commentsBinding.showSecondComment, 2, comments);
                displayTimerComments(commentsBinding.showThirdComment, 3, comments);
                displayTimerComments(commentsBinding.showFourthComment, 4, comments);
                displayTimerComments(commentsBinding.showFifthComment, 5, comments);
        }
    }

    private void displayTimerComments(@NonNull TextView textView, Integer index, @NonNull List<CommentsModel> comments) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.comments_comment_text))
                .append(comments.get(comments.size() - index).getText())
                .append("\n")
                .append(getString(R.string.comments_comment_date))
                .append(comments.get(comments.size() - index).getTime());
        textView.setText(stringBuilder);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        commentsBinding = null;
    }
}