package com.example.workapp.presentation.screen.timer.timer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.comments.CommentsModel;
import com.example.workapp.data.network.model.timer.TimerModel;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.databinding.TimerFragmentBinding;
import com.example.workapp.presentation.screen.comment.CommentDialog;
import com.example.workapp.presentation.screen.comment.CommentFragment;
import com.example.workapp.presentation.screen.main.MainFragment;
import com.example.workapp.presentation.screen.timer.operations.TimerOperations;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerFragment extends Fragment {

    public static Date timeOfTimerStart, timeOfTimerPauseStart, timeOfTimerPauseFinish;
    public static boolean isStarted, isPaused, isResumed = false;
    public Date timeOfFinish;
    TimerModel timerModel = new TimerModel();
    WorkModel workModel = new WorkModel();
    CommentsModel commentsModel = new CommentsModel();
    TimerOperations timerOperations = new TimerOperations();
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment stopTimerFragment = new StopTimerFragment();
    Timer timer;
    TimerTask timerTask;
    Fragment mainFragment = new MainFragment();
    TimerFragmentBinding binding;
    Handler uiHandler = new Handler();
    Runnable runnable;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private RecyclerView timerRecyclerView;
    private List<TimerMenuModel> menuModelList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TimerFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setClickListeners();
        initializeMenuModel();
        setMenuAdapter();
        return view;
    }

    public void onResume() {
        super.onResume();
        saveData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setClickListeners() {
        binding.startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimerStarted();
            }
        });
        binding.stopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimerStopped();
            }
        });
        binding.pauseTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimerPaused();
            }
        });
        binding.resumeTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimerResumed();
            }
        });
    }

    private void initializeMenuModel() {
        menuModelList.add(new TimerMenuModel("Watch comments", R.drawable.ic_watch_comments_48dp));
        menuModelList.add(new TimerMenuModel("Add comment", R.drawable.ic_add_commen_48dp));
    }

    private void setMenuAdapter() {
        TimerMenuAdapter.OnUserClickListener onUserClickListener = new TimerMenuAdapter.OnUserClickListener() {
            @Override
            public void onClick(@NotNull TimerMenuModel timerMenuModel) {
                if (timerMenuModel.getText().equals("Watch comments")) {
                    Intent intent = new Intent(getContext(), CommentFragment.class);
                    startActivity(intent);
                } else if (timerMenuModel.getText().equals("Add comment")) {
                    CommentDialog commentDialog = new CommentDialog();
                    commentDialog.show(getActivity().getSupportFragmentManager(), "dialog");
                }
            }
        };
        TimerMenuAdapter timerMenuAdapter = new TimerMenuAdapter(getActivity(), menuModelList, onUserClickListener);
        binding.timerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.timerRecyclerView.setAdapter(timerMenuAdapter);
    }

    public void onTimerStarted() {
        isStarted = true;
        timeOfTimerStart = timerOperations.getCalendarInstance();
        timerModel.setStartTime(timerOperations.setTime());
        timerModel.setWorkId(getArguments().getString("workId"));
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        binding.timerStartView.setText(timerOperations.setTime());
                    }
                };
                uiHandler.postDelayed(runnable, 1000);
            }
        };
        timer.schedule(timerTask, 0);
        showToastMessage("Timer started");
    }

    public void onTimerStopped() {
        isStarted = false;
        isResumed = false;
        isPaused = false;
        binding.startTimer.setEnabled(true);
        workModel.setId(getArguments().getString("workId"));
        workModel.setName(getArguments().getString("workName"));
        workModel.setCompleted(true);
        workModel.setObjectId(getArguments().getString("workObjectId"));
        Call<WorkModel> call = NetworkClient.getWorkApi().updateWork(workModel
                .getObjectId(), workModel);
        call.enqueue(new Callback<WorkModel>() {
            @Override
            public void onResponse(@NonNull Call<WorkModel> call,
                                   @NonNull Response<WorkModel> response) {
                if (response.isSuccessful()) {
                    try {
                        timerModel.setFinishTime(timerOperations.setTime());
                        timeOfFinish = timerOperations.getCalendarInstance();
                        timerModel.setElapsedTime(timerOperations.calculateDifference
                                (timeOfTimerStart, timeOfFinish));
                        Call<TimerModel> elapsedTimeCall = NetworkClient.getTimerAPI()
                                .createTimer(timerModel);
                        createTimer(elapsedTimeCall);
                        stopService();
                    } catch (NullPointerException exception) {
                        showToastMessage("Timer isn't created");
                    }

                } else {
                    try {
                        if (response.errorBody() != null) {
                            showToastMessage(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        showToastMessage("IO exception");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkModel> call, @NonNull Throwable t) {
                showToastMessage(t.getMessage());
            }
        });

    }

    private void createTimer(@NotNull Call<TimerModel> call) {
        call.enqueue(new Callback<TimerModel>() {
            @Override
            public void onResponse(@NonNull Call<TimerModel> call, @NonNull Response<TimerModel> response) {
                if (response.isSuccessful()) {
                    try {
                        timer.cancel();
                    } catch (NullPointerException exception) {
                        showToastMessage("Timer isn't created");
                    }
                    showToastMessage("Timer stopped");
                    replaceFragment(stopTimerFragment, null);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            showToastMessage(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        showToastMessage("Error happened");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimerModel> call, @NonNull Throwable t) {
                showToastMessage(t.getMessage());
            }
        });
    }

    public void onTimerResumed() {
        try {
            isResumed = true;
            timeOfTimerPauseFinish = timerOperations.getCalendarInstance();
            timerModel.setTimeInPause(timerOperations.calculateDifference
                    (timeOfTimerPauseStart, timeOfTimerPauseFinish));
            binding.timerEndPauseView.setText(timerOperations.setTime());
        } catch (NullPointerException exception) {
            showToastMessage("Timer isn't created");
        }
    }

    public void onTimerPaused() {
        try {
            if (!isStarted) {
                throw new NullPointerException();
            } else {
                isPaused = true;
                timeOfTimerPauseStart = timerOperations.getCalendarInstance();
                binding.timerPauseStartView.setText(timerOperations.setTime());
            }
        } catch (NullPointerException exception) {
            showToastMessage("Timer isn't created");
        }
    }

    public void onTimerClosed() {
        replaceFragment(mainFragment, null);
    }

    private void createCommentOnServer(String text) {
        Call<CommentsModel> call = NetworkClient.getCommentAPI().createComment(commentsModel);
        call.enqueue(new Callback<CommentsModel>() {
            @Override
            public void onResponse(@NonNull Call<CommentsModel> call,
                                   @NonNull Response<CommentsModel> response) {
                if (response.isSuccessful()) {
                    commentsModel.setTime(timerOperations.setTime());
                    commentsModel.setWorkId(getArguments().getString("workId"));
                    commentsModel.setText(text);
                    showToastMessage("Comment created");
                } else {
                    try {
                        if (response.errorBody() != null) {
                            showToastMessage(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        showToastMessage("Shit happened");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentsModel> call, @NonNull Throwable t) {
                showToastMessage(t.getMessage());
            }
        });
    }

    private void replaceFragment(Fragment fragmentName, String backStackName) {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction()
                .add(R.id.navigation_content_frame, fragmentName);
        fragmentTransaction.addToBackStack(backStackName);
        fragmentTransaction.commit();
    }

    //TODO make fragmentManager static
    private void showToastMessage(String text) {
        Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void stopService() {
        try {
            getActivity().getApplicationContext().unbindService(MainFragment.serviceConnection);
        } catch (IllegalArgumentException exception) {
            showToastMessage("Work isn't existed");
        }
    }

   /* @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Fragment dialog1 = getSupportFragmentManager().findFragmentByTag("dialog");
        if (dialog1 instanceof CommentDialog){
            CommentDialog commentDialog = (CommentDialog) dialog1;
            commentDialog.getText()

        }
        //try {
            if (((EditText) dialog.getDialog().findViewById(R.id.inputDialogComment))
                    .getText().equals("")) {
          //      throw new NullPointerException();
          //  } else {
                createCommentOnServer(dialog);
            }
       // } catch (NullPointerException exception) {
       //     showToastMessage("Comment can't be empty");
       // }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Fragment dialog1 = getSupportFragmentManager().findFragmentByTag("dialog");
        if (dialog1 instanceof CommentDialog){
            CommentDialog commentDialog = (CommentDialog) dialog1;
            commentDialog.dismiss();
        }
    }

    @Override
    public void onPositiveClicked(String text) {
        CommentDialog commentDialog = new CommentDialog();
        createCommentOnServer(text);
        commentDialog.dismiss();
    }

    @Override
    public void onNegativeClicked() {
        Fragment dialog1 = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (dialog1 instanceof CommentDialog) {
            CommentDialog commentDialog = (CommentDialog) dialog1;
            commentDialog.dismiss();
        }
    }*/

    private void saveData() {
        if (isStarted) {
            setData(R.id.timerStartView, timeOfTimerStart);
            getActivity().findViewById(R.id.startTimer).setEnabled(false);
        }
        if (isPaused) {
            setData(R.id.timerPauseStartView, timeOfTimerPauseStart);
        }
        if (isResumed) {
            setData(R.id.timerEndPauseView, timeOfTimerPauseFinish);
        }
    }

    private void setData(Integer id, Date date) {
        ((TextView) getActivity().findViewById(id)).setText(simpleDateFormat.format(date));
    }
}