package com.example.workapp.presentation.screen.archive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.work.WorkActionResult;
import com.example.workapp.data.network.model.work.WorkCloudDataSource;
import com.example.workapp.data.network.model.work.WorkModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArchiveFragment extends Fragment {

    private final String CLICKED_ID_ARG = "NOTE_CLICKED_ID_ARG";

    private RecyclerView recyclerView;
    private WorkModel workModel = new WorkModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.archive_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        displayCompletedWorks();
        return view;
    }

    private void displayCompletedWorks() {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWork(workModel.getName(), new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                setDataAdapter(works);
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("Error");
            }
        });
    }

    private void setDataAdapter(List<WorkModel> works) {
        Fragment completedWorksFragment = new CompletedWorksFragment();
        ArchiveAdapter.OnUserClickListener onUserClickListener = workModel -> {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                setFragmentArgs(completedWorksFragment);
                replaceFragment(activity, completedWorksFragment);
            }
        };
        ArchiveAdapter archiveAdapter = new ArchiveAdapter(works, onUserClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(archiveAdapter);
    }

    private void setFragmentArgs(@NotNull Fragment completedWorksFragment) {
        Bundle bundle = new Bundle();
        bundle.putString(CLICKED_ID_ARG, workModel.getId());
        completedWorksFragment.setArguments(bundle);
    }

    private void replaceFragment(@NotNull FragmentActivity activity,
                                 Fragment completedWorksFragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .replace(
                        R.id.navigation_content_frame,
                        completedWorksFragment
                );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public void showToastMessage(String text) {
        Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}