package com.example.workapp.presentation.screen.archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workapp.R;
import com.example.workapp.data.network.model.work.WorkActionResult;
import com.example.workapp.data.network.model.work.WorkCloudDataSource;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.databinding.ActivityCompletedWorksBinding;
import com.example.workapp.presentation.screen.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CompletedWorks extends AppCompatActivity {

    private final String CLICKED_ID_ARG = "NOTE_CLICKED_ID_ARG";
    ActivityCompletedWorksBinding binding;
    //RecyclerView recyclerView;
    private String clickedWorkId;
    private WorkModel workModel = new WorkModel();
    private List<String> workIdArray = new ArrayList<>();
    private List<String> worksArrayCompleted = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedWorksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        worksServerCall();
    }

    private void worksServerCall() {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWork(workModel.getName(), new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                displayCompletedWorks(works);
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("Error");
            }
        });
    }

    private void displayCompletedWorks(@NonNull List<WorkModel> works) {
        getCompletedWorksIdAndName(works);
        //setDataAdapter(works); recyclerView
        switch (worksArrayCompleted.size()) {
            case 0:
                binding.firstArchiveNote.setText(R.string.main_empty_works);
                break;
            case 1:
                displayArchiveInformation(binding.firstArchiveNote, 1);
                break;
            case 2:
                displayArchiveInformation(binding.firstArchiveNote, 1);
                displayArchiveInformation(binding.secondArchiveNote, 2);
                break;
            case 3:
                displayArchiveInformation(binding.firstArchiveNote, 1);
                displayArchiveInformation(binding.secondArchiveNote, 2);
                displayArchiveInformation(binding.thirdArchiveNote, 3);
                break;
            case 4:
                displayArchiveInformation(binding.firstArchiveNote, 1);
                displayArchiveInformation(binding.secondArchiveNote, 2);
                displayArchiveInformation(binding.thirdArchiveNote, 3);
                displayArchiveInformation(binding.fourthArchiveNote, 4);
                break;
            default:
                displayArchiveInformation(binding.firstArchiveNote, 1);
                displayArchiveInformation(binding.secondArchiveNote, 2);
                displayArchiveInformation(binding.thirdArchiveNote, 3);
                displayArchiveInformation(binding.fourthArchiveNote, 4);
                displayArchiveInformation(binding.fifthtArchiveNote, 5);
                break;
        }
    }

    private void displayArchiveInformation(@NotNull TextView textView, Integer index) {
        textView.setText(worksArrayCompleted.get(worksArrayCompleted.size() - index));
    }

    /*private void setDataAdapter(List<WorkModel> works) {
        recyclerView = findViewById(R.id.recyclerView);
        DataAdapter dataAdapter = new DataAdapter(this, works);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dataAdapter);
    }*/
    //TODO recyclerView

    private void getWorkIdFromServer(Integer workPosition) {
        if (workIdArray.size() == 0){
            throw new ArrayIndexOutOfBoundsException();
        }
        clickedWorkId = workIdArray.get(workIdArray.size() - workPosition);
    }

    public void clickFirstNote(View view) {
        try {
            getWorkIdFromServer(1);
            moveToWorksArchive();
        } catch (ArrayIndexOutOfBoundsException exception) {
            showToastMessage("Don't click this text!");
        }
    }

    public void clickSecondNote(View view) {
        getWorkIdFromServer(2);
        moveToWorksArchive();
    }

    public void clickThirdNote(View view) {
        getWorkIdFromServer(3);
        moveToWorksArchive();
    }

    public void clickFourthNote(View view) {
        getWorkIdFromServer(4);
        moveToWorksArchive();
    }

    public void clickFifthNote(View view) {
        getWorkIdFromServer(5);
        moveToWorksArchive();
    }

    public void moveToWorksArchive() {
        Intent moveToArchive = new Intent(this, CompletedWorksInformation.class);
        moveToArchive.putExtra(CLICKED_ID_ARG, clickedWorkId);
        startActivity(moveToArchive);
    }

    public void moveFromArchive(View view) {
        Intent archiveClose = new Intent(this, MainActivity.class);
        startActivity(archiveClose);
    }

    private void getCompletedWorksIdAndName(@NotNull List<WorkModel> works) {
        for (int i = 0; i < works.size(); i++) {
            if (works.get(i).isCompleted()) {
                worksArrayCompleted.add(works.get(i).getName());
                workIdArray.add(works.get(i).getId());
            }
        }
    }

    public void showToastMessage(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}