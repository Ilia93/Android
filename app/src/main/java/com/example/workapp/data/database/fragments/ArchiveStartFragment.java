package com.example.workapp.data.database.fragments;
/*
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workapp.R;
import com.example.workapp.database.entity.Work;
import com.example.workapp.database.room_dao.WorkDao;

import java.util.List;

import static com.example.workapp.presentation.screen.main.MainActivity.db;

public class ArchiveStartFragment extends Fragment {
    private WorkDao workDao = db.workDao();
    private List<Work> workList = workDao.getall();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickable = (Clickable) context;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_archive, container, false);
        if (workList.size() == 0) {
            ((TextView) view.findViewById(R.id.firstArchiveNote)).setText("There are no any works");
        } else if (workList.size() == 1) {
            displayArchiveInformation(view, R.id.firstArchiveNote, 1, workList, getArchivedFlag());
        } else if (workList.size() == 2) {
            displayArchiveInformation(view, R.id.firstArchiveNote, 1, workList, getArchivedFlag());
            displayArchiveInformation(view, R.id.secondArchiveNote, 2, workList, getArchivedFlag());
        } else if (workList.size() == 3) {
            displayArchiveInformation(view, R.id.firstArchiveNote, 1, workList, getArchivedFlag());
            displayArchiveInformation(view, R.id.secondArchiveNote, 2, workList, getArchivedFlag());
            displayArchiveInformation(view, R.id.thirdArchiveNote, 3, workList, getArchivedFlag());
        } else if (workList.size() == 4) {
            displayArchiveInformation(view, R.id.firstArchiveNote, 1, workList, getArchivedFlag());
            displayArchiveInformation(view, R.id.secondArchiveNote, 2, workList, getArchivedFlag());
            displayArchiveInformation(view, R.id.thirdArchiveNote, 3, workList, getArchivedFlag());
            displayArchiveInformation(view, R.id.fourthArchiveNote, 4, workList, getArchivedFlag());
        } else {
            displayArchiveInformation(view, R.id.firstArchiveNote, 1, workList, getArchivedFlag());
            displayArchiveInformation(view, R.id.secondArchiveNote, 2, workList, getArchivedFlag());
            displayArchiveInformation(view, R.id.thirdArchiveNote, 3, workList, getArchivedFlag());
            displayArchiveInformation(view, R.id.fourthArchiveNote, 4, workList, getArchivedFlag());
            displayArchiveInformation(view, R.id.fifthtArchiveNote, 5, workList, getArchivedFlag());
        }

        view.findViewById(R.id.firstArchiveNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.clickFirstNote();
            }
        });
        view.findViewById(R.id.secondArchiveNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.clickSecondNote();
            }
        });
        view.findViewById(R.id.thirdArchiveNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.clickThirdNote();
            }
        });
        view.findViewById(R.id.fourthArchiveNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.clickFourthNote();
            }
        });
        view.findViewById(R.id.fifthtArchiveNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.clickFifthNote();
            }
        });
        return view;
    }

    public interface Clickable {
        void clickFirstNote();

        void clickSecondNote();

        void clickThirdNote();

        void clickFourthNote();

        void clickFifthNote();
    }

    private Clickable clickable;

    @SuppressLint("SetTextI18n")
    private void displayArchiveInformation(View view, Integer viewId, Integer index,
                                           List<Work> workList, boolean archived) {
        if (archived) {
            String workName = "Work name: ";
            ((TextView) view.findViewById(viewId)).setText(workName + workList.get(workList.size() - index).getName());
        }
    }

    private boolean getArchivedFlag() {
        return workList.get(workList.size() - 1).getIsCompleted();
    }
}*/