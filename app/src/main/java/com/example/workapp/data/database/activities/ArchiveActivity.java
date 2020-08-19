/*package com.example.workapp.data.room_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.workapp.R;
import com.example.workapp.data.room_app.fragments.CompletedWorksInformation;
import com.example.workapp.data.room_app.fragments.ArchiveStartFragment;

public class ArchiveActivity extends AppCompatActivity implements ArchiveStartFragment.Clickable {
    Button archiveExit;
    FragmentManager myFragmentManager;
    FragmentTransaction transaction;
    Fragment archiveStartFragment, archiveSelectedNoteFragment;
    public static boolean isIsClickedFirstNote = false;
    public static boolean isClickedSecondNote = false;
    public static boolean isClickedThirdNote = false;
    public static boolean isClickedFourthNote = false;
    public static boolean isClickedFifthNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        if (savedInstanceState == null) {
            myFragmentManager = getSupportFragmentManager();
            archiveStartFragment = new ArchiveStartFragment();
            transaction = myFragmentManager.beginTransaction();
            transaction.add(R.id.ArchiveFragmentFrame, archiveStartFragment);
            transaction.commit();
        }
        setVariables();
        setOnclickListeners();
    }

    public void setVariables() {
        archiveExit = findViewById(R.id.archiveExit);
    }

    public void setOnclickListeners() {
        archiveExit.setOnClickListener(archiveListener);
    }

    OnClickListener archiveListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent archiveClose = new Intent(ArchiveActivity.this, MainActivity.class);
            startActivity(archiveClose);
        }
    };

    @Override
    public void clickFirstNote() {
        isIsClickedFirstNote = true;
        setSelectedNote();

    }

    @Override
    public void clickSecondNote() {
        isClickedSecondNote = true;
        setSelectedNote();
    }

    @Override
    public void clickThirdNote() {
        isClickedThirdNote = true;
        setSelectedNote();
    }

    @Override
    public void clickFourthNote() {
        isClickedFourthNote = true;
        setSelectedNote();
    }

    @Override
    public void clickFifthNote() {
        isClickedFifthNote = true;
        setSelectedNote();
    }

    public void setSelectedNote() {
        archiveSelectedNoteFragment = new CompletedWorksInformation();
        transaction = myFragmentManager.beginTransaction();
        transaction.replace(R.id.ArchiveFragmentFrame, archiveSelectedNoteFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
*/