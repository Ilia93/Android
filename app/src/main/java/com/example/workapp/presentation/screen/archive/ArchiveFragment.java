package com.example.workapp.presentation.screen.archive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.work.WorkActionResult;
import com.example.workapp.data.network.model.work.WorkCloudDataSource;
import com.example.workapp.data.network.model.work.WorkModel;

import java.util.List;

public class ArchiveFragment extends Fragment {

    private final String CLICKED_ID_ARG = "NOTE_CLICKED_ID_ARG";
    RecyclerView recyclerView;
    FragmentManager fragmentManager;
    Fragment completedWorksFragment = new CompletedWorksFragment();
    private WorkModel workModel = new WorkModel();

    @Override
    public void onResume() {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.archive_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        displayCompletedWorks(view);
        return view;
    }

    private void displayCompletedWorks(View view) {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWork(workModel.getName(), new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                setDataAdapter(works, view);
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("Error");
            }
        });
    }

    private void setDataAdapter(List<WorkModel> works, View view) {
        Bundle bundle = new Bundle();
        ArchiveAdapter.OnUserClickListener onUserClickListener = workModel -> {
            bundle.putString(CLICKED_ID_ARG, workModel.getId());
            completedWorksFragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.navigation_content_frame, completedWorksFragment);
            getFragmentManager().beginTransaction().addToBackStack(null);
            getFragmentManager().beginTransaction().commit();
        };
        ArchiveAdapter archiveAdapter = new ArchiveAdapter(works, onUserClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(archiveAdapter);
    }

    public void showToastMessage(String text) {
        Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}