package com.example.workapp.presentation.screen.archive;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.workapp.databinding.ArchiveCompletedWorksFragmentBinding;
import com.example.workapp.presentation.App;
import com.example.workapp.presentation.screen.main.MainActivity;
import com.example.workapp.presentation.screen.main.MainFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompletedWorksFragment extends Fragment {

    public FragmentActivity fragmentActivity;
    private ArchiveCompletedWorksFragmentBinding binding;

    public static CompletedWorksFragment newInstance(Bundle bundle) {
        CompletedWorksFragment fragment = new CompletedWorksFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof MainActivity) {
            this.fragmentActivity = (FragmentActivity) context;
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ArchiveCompletedWorksFragmentBinding.inflate(
                inflater,
                container,
                false);
        View view = binding.getRoot();
        setOnClickListener();
        getCommonWorksInformation();
        return view;
    }

    private void setOnClickListener() {
        binding.completedWorksExit.setOnClickListener(v -> exit());
    }

    private void getCommonWorksInformation() {
        getWorkData();
        getTimerData();
        getCommentsData();
    }

    private void exit() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.navigation_content_frame, MainFragment.newInstance())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .addToBackStack(null).commit();
        }
    }

    private void getWorkData() {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWork("", new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                displayCommonWorkInformation(works);
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("Failed to load work server data");
            }
        });
    }

    private void getTimerData() {
        TimerCloudDataSource timerCloudDataSource = new TimerCloudDataSource();
        timerCloudDataSource.getTimer("", new TimerActionResult() {
            @Override
            public void onSuccess(List<TimerModel> timerModel) {
                displayCommonTimerInformation(timerModel);
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("Failed to load timer server data");
            }
        });
    }

    private void getCommentsData() {
        CommentsCloudDataSource commentsCloudDataSource = new CommentsCloudDataSource();
        commentsCloudDataSource.getComments("", new CommentsActionResult() {
            @Override
            public void onSuccess(List<CommentsModel> comments) {
                displayCommonCommentInformation(comments);
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("Failed to load comments server data");
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
            binding.thirdInformationNote.setText(R.string.archive_latest_comments);
        } else {
            displayServerCommentInformation(binding.thirdInformationNote, comments);
        }
    }

    private void displayServerWorksData(@NonNull TextView textView,
                                        @NonNull List<WorkModel> works) {
        StringBuilder stringBuilder = new StringBuilder();
        if (getArguments() != null) {
            String id = (getArguments().getString("NOTE_CLICKED_ID_ARG"));
            for (int i = 0; i < works.size(); i++) {
                if ((works.get(i).getId().equals(id))) {
                    stringBuilder.append(App.getInstance().getString(R.string.archive_works_name))
                            .append(works.get(i).getName());
                }
            }
            textView.setText(stringBuilder);
        }
    }

    private void displayServerTimerData(@NonNull TextView textView,
                                        @NonNull List<TimerModel> timerModel) {
        StringBuilder stringBuilder = new StringBuilder();
        if (getArguments() != null) {
            String id = (getArguments().getString("NOTE_CLICKED_ID_ARG"));
            try {
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
            } catch (NullPointerException exception) {
                stringBuilder.append("Didn't created");
            }
            textView.setText(stringBuilder);
        }
    }

    private void displayServerCommentInformation(@NonNull TextView textView,
                                                 @NonNull List<CommentsModel> comments) {
        StringBuilder stringBuilder = new StringBuilder();
        if (getArguments() != null) {
            for (int i = 0; i < comments.size(); i++) {
                do {
                    if (comments.get(i).getWorkId().equals
                            (getArguments().getString("NOTE_CLICKED_ID_ARG"))) {
                        stringBuilder.append(getString(R.string.archive_latest_comments))
                                .append("\n")
                                .append(comments.get(i).getText())
                                .append(" at ")
                                .append(comments.get(i).getTime())
                                .append("\n");
                    }
                } while (i == comments.size());
            }
            textView.setText(stringBuilder);
        }
    }

    private void showToastMessage(String text) {
        Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}