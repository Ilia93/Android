package com.example.workapp.data.database.activities;
/*
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workapp.R;

import java.util.List;

import com.example.workapp.database.App;
import com.example.workapp.database.AppDatabase;
import com.example.workapp.database.entity.Work;
import com.example.workapp.database.room_dao.WorkDao;

public class MainActivity extends AppCompatActivity {
    public static AppDatabase db = App.getInstance().getDatabase();
    WorkDao workDao = db.workDao();
    Work work = new Work();
    String workName = "Work name: ";
    String runActivity = "Run Activity";
    String sleepActivity = "Sleep Activity";
    String walkActivity = "Walk activity";
    String physicalActivity = "Physical activity";
    String emptyWorks = "Works are empty";
    Button archive, next, createWork, run, sleep, walk, physical_work;
    public TextView enterWork, latestWorkOne, latestWorkTwo, latestWorkThree, latestWorkFour,
            latestWorkFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVariables();
        setOnclickListeners();
        showWorks();
    }

    public void setVariables() {
        archive = findViewById(R.id.archive);
        next = findViewById(R.id.next);
        run = findViewById(R.id.run);
        walk = findViewById(R.id.walk);
        sleep = findViewById(R.id.sleep);
        physical_work = findViewById(R.id.physical_work);
        createWork = findViewById(R.id.createWork);
        enterWork = findViewById(R.id.inputWork);
        latestWorkOne = findViewById(R.id.first);
        latestWorkTwo = findViewById(R.id.second);
        latestWorkThree = findViewById(R.id.third);
        latestWorkFour = findViewById(R.id.fourth);
        latestWorkFive = findViewById(R.id.fifth);
    }

    public void setOnclickListeners() {
        archive.setOnClickListener(mainScreen);
        next.setOnClickListener(mainScreen);
        createWork.setOnClickListener(mainScreen);
        run.setOnClickListener(mainScreen);
        walk.setOnClickListener(mainScreen);
        sleep.setOnClickListener(mainScreen);
        physical_work.setOnClickListener(mainScreen);
    }


    OnClickListener mainScreen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.archive:
                    Intent archiveIntent = new Intent(MainActivity.this, CompletedWorks.class);
                    startActivity(archiveIntent);
                    break;
                case R.id.next:
                    Intent timerIntent = new Intent(MainActivity.this, TimerActivity.class);
                    timerIntent.putExtra("workId", work.getId());
                    timerIntent.putExtra("workName", work.getName());
                    startActivity(timerIntent);
                    break;
                case R.id.createWork:
                    //work.setName(enterWork.getText().toString());
                    //workDao.insert(work);
                    showMessage("Work created");
                    enterWork.setText(null);
                    break;
                case R.id.run:
                    enterWork.setText(runActivity);
                    break;
                case R.id.sleep:
                    enterWork.setText(sleepActivity);
                    break;
                case R.id.walk:
                    enterWork.setText(walkActivity);
                    break;
                case R.id.physical_work:
                    enterWork.setText(physicalActivity);
                    break;
            }
        }
    };

    public void showWorks() {
        //List<Work> works = workDao.getall();
        if (works.size() == 0) {
            latestWorkOne.setText(emptyWorks);
        } else if (works.size() == 1) {
            displayWorkName(latestWorkOne, 1, works);
        } else if (works.size() == 2) {
            displayWorkName(latestWorkOne, 1, works);
            displayWorkName(latestWorkTwo, 2, works);
        } else if (works.size() == 3) {
            displayWorkName(latestWorkOne, 1, works);
            displayWorkName(latestWorkTwo, 2, works);
            displayWorkName(latestWorkThree, 3, works);
        } else if (works.size() == 4) {
            displayWorkName(latestWorkOne, 1, works);
            displayWorkName(latestWorkTwo, 2, works);
            displayWorkName(latestWorkThree, 3, works);
            displayWorkName(latestWorkFour, 4, works);
        } else {
            displayWorkName(latestWorkOne, 1, works);
            displayWorkName(latestWorkTwo, 2, works);
            displayWorkName(latestWorkThree, 3, works);
            displayWorkName(latestWorkFour, 4, works);
            displayWorkName(latestWorkFive, 5, works);
        }
    }

    @SuppressLint("SetTextI18n")
    public void displayWorkName(TextView textView, Integer index, List<Work> works) {
        textView.setText(workName + works.get(works.size() - index).getName());
    }

    public void showMessage(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}*/