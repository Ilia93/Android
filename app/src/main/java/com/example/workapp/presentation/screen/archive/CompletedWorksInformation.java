package com.example.workapp.presentation.screen.archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workapp.R;
import com.example.workapp.data.network.model.comments.CommentsActionResult;
import com.example.workapp.data.network.model.comments.CommentsCloudDataSource;
import com.example.workapp.data.network.model.comments.CommentsModel;
import com.example.workapp.data.network.model.timer.TimerActionResult;
import com.example.workapp.data.network.model.timer.TimerCloudDataSource;
import com.example.workapp.data.network.model.timer.TimerModel;
import com.example.workapp.data.network.model.work.WorkActionResult;
import com.example.workapp.data.network.model.work.WorkCloudDataSource;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.databinding.ActivityCompletedWorksInformationBinding;
import com.example.workapp.presentation.screen.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompletedWorksInformation extends AppCompatActivity {

    ActivityCompletedWorksInformationBinding binding;
    private WorkModel workModel = new WorkModel();
    private TimerModel timerModel = new TimerModel();
    private CommentsModel commentsModel = new CommentsModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedWorksInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getCommonWorksInformation();
    }

    private void getCommonWorksInformation() {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWork(workModel.getName(), new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                displayCommonWorkInformation(works);
            }

            @Override
            public void onFailure(String message) {
            }
        });
        TimerCloudDataSource timerCloudDataSource = new TimerCloudDataSource();
        timerCloudDataSource.getTimer(timerModel.getStartTime(), new TimerActionResult() {
            @Override
            public void onSuccess(List<TimerModel> timerModel) {
                displayCommonTimerInformation(timerModel);
            }

            @Override
            public void onFailure(String message) {

            }
        });
        CommentsCloudDataSource commentsCloudDataSource = new CommentsCloudDataSource();
        commentsCloudDataSource.getComments(commentsModel.getText(), new CommentsActionResult() {
            @Override
            public void onSuccess(List<CommentsModel> comments) {
                displayCommonCommentInformation(comments);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }


    private void displayCommonWorkInformation(@NonNull List<WorkModel> works) {
        if (works.size() == 0) {
            binding.firstInformationNote.setText(R.string.main_empty_works);
        } else {
            displayServerWorksData(binding.firstInformationNote, works);
        }
    }

    private void displayCommonTimerInformation(@NotNull List<TimerModel> timerModels) {
        if (timerModels.size() == 0) {
            binding.secondInformationNote.setText(R.string.main_empty_works);
        } else {
            displayServerTimerData(binding.secondInformationNote, timerModels);
        }
    }

    private void displayCommonCommentInformation(@NotNull List<CommentsModel> comments) {
        if (comments.size() == 0) {
            binding.thirdInformationNote.setText(R.string.archive_works_name);
        } else {
            displayServerCommentInformation(binding.thirdInformationNote, comments);
        }
    }

    private void displayServerWorksData(@NonNull TextView textView,
                                        @NonNull List<WorkModel> works) {
        StringBuilder stringBuilder = new StringBuilder();
        String id = (getIntent().getStringExtra("NOTE_CLICKED_ID_ARG"));
        for (int i = 0; i < works.size(); i++) {
            if ((works.get(i).getId().equals(id))) {
                stringBuilder.append(getString(R.string.archive_works_name))
                        .append(works.get(i).getName());
            }
        }
        textView.setText(stringBuilder);
    }

    private void displayServerTimerData(@NonNull TextView textView,
                                        @NonNull List<TimerModel> timerModel) {
        StringBuilder stringBuilder = new StringBuilder();
        String id = (getIntent().getStringExtra("NOTE_CLICKED_ID_ARG"));
        for (int i = 0; i < timerModel.size(); i++) {
            if (timerModel.get(i).getWorkId().equals(id)) {
                stringBuilder.append(getString(R.string.archive_work_started))
                        .append(timerModel.get(i).getStartTime())
                        .append("\n")
                        .append(getString(R.string.archive_work_finished))
                        .append(timerModel.get(i).getFinishTime())
                        .append("\n")
                        .append(getString(R.string.archive_activity_elapsed_time))
                        .append(timerModel.get(i).getElapsedTime())
                        .append("\n")
                        .append(getString(R.string.archive_activity_pause_time))
                        .append(timerModel.get(i).getTimeInPause());
            }
        }
        textView.setText(stringBuilder);
    }

    private void displayServerCommentInformation(@NonNull TextView textView,
                                                 @NonNull List<CommentsModel> comments) {
        StringBuilder stringBuilder = new StringBuilder();
        String allWorkComments = "";
        for (int i = 0; i < comments.size(); i++) {
            do {
                if (comments.get(i).getWorkId().equals(getIntent().getStringExtra("NOTE_CLICKED_ID_ARG"))) {
                    allWorkComments = allWorkComments.concat(comments.get(i).getText() + " at " +
                            comments.get(i).getTime() + "\n");
                }
            } while (i == comments.size());
        }
        stringBuilder.append(getString(R.string.archive_latest_comments))
                .append("\n")
                .append(allWorkComments);
        textView.setText(stringBuilder);
    }

    public void moveToMainScreen(View view) {
        Intent closeWorks = new Intent(CompletedWorksInformation.this, MainActivity.class);
        startActivity(closeWorks);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}