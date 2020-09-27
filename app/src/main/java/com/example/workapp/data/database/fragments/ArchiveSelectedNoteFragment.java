package com.example.workapp.data.database.fragments;
/*
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workapp.R;

import java.util.List;

import static com.example.workapp.presentation.screen.archive.ArchiveFragment.isClickedFifthNote;
import static com.example.workapp.presentation.screen.archive.ArchiveFragment.isClickedFourthNote;
import static com.example.workapp.presentation.screen.archive.ArchiveFragment.isClickedSecondNote;
import static com.example.workapp.presentation.screen.archive.ArchiveFragment.isClickedThirdNote;
import static com.example.workapp.presentation.screen.archive.ArchiveFragment.isIsClickedFirstNote;

import com.example.workapp.database.entity.AppTimer;
import com.example.workapp.database.querries.WorkWithComments;
import com.example.workapp.database.querries.WorkWithTimer;
import com.example.workapp.database.room_dao.WorkDao;
import static com.example.workapp.presentation.screen.main.MainActivity.db;

public class CompletedWorksFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.archive_completed_works_fragment, container, false);
        //WorkDao workDao = db.workDao();
        //List<WorkWithComments> works = workDao.getWorkComments();
        //List<WorkWithTimer> worksAndTimer = workDao.getWorkAndTimer();
        if (isIsClickedFirstNote)
            displayCommonWorkInformation(view, works, worksAndTimer, 1);
        isIsClickedFirstNote = false;
        if (isClickedSecondNote) {
            displayCommonWorkInformation(view, works, worksAndTimer, 2);
            isClickedSecondNote = false;
        } else if (isClickedThirdNote) {
            displayCommonWorkInformation(view, works, worksAndTimer, 3);
            isClickedThirdNote = false;
        } else if (isClickedFourthNote) {
            displayCommonWorkInformation(view, works, worksAndTimer, 4);
            isClickedFourthNote = false;
        } else if (isClickedFifthNote) {
            displayCommonWorkInformation(view, works, worksAndTimer, 5);
            isClickedFifthNote = false;
        }
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void displayCommonWorkInformation(View view, List<WorkWithComments> works,
                                              List<WorkWithTimer> worksAndTimer, Integer index) {
        String workName = "Work name: ";
        String workStarted = "Work started at: ";
        String workFinished = "Work finished at: ";
        String latestComment = "Latest comments is:";
        String elapsedTime = "Time elapsed by Activity: ";
        String commonPauseTime = "Time spent by pause: ";
        int position = works.size() - index;
        //AppTimer appTimer = worksAndTimer.get(worksAndTimer.size() - index).
               // appTimers.get(worksAndTimer.get(worksAndTimer.size() - 1).appTimers.size() - 1);
        String all_comments = "";
        for (int i = 0; i < works.get(position).comments.size(); i++) {
            do {
                all_comments = all_comments.concat(works.get(position).comments.get(i).getText() + " at " +
                        works.get(position).comments.get(i).getTime() + "\n");
            } while (i == works.get(position).comments.size());
        }

        ((TextView) view.findViewById(R.id.firstInformationNote)).setText(workName +
                works.get(position).getWorkName() + "\n" +
                workStarted + appTimer.getStartTime() + "\n" +
                workFinished + appTimer.getFinishTime() + "\n" +
                elapsedTime + appTimer.getElapsedTime() + "\n" +
                commonPauseTime + appTimer.getTimeInPause() + "\n" +
                latestComment + "\n" + all_comments);
    }
}*/