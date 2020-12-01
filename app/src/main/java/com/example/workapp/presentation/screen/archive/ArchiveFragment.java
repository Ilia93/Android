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
import com.example.workapp.databinding.ArchiveFragmentBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArchiveFragment extends Fragment {

    public static final String CLICKED_ID_ARG = "NOTE_CLICKED_ID_ARG";
    private ArchiveFragmentBinding binding;
    private WorkModel workModel = new WorkModel();

    public static ArchiveFragment newInstance() {
        return new ArchiveFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ArchiveFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
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
                showToastMessage("Failed to load server data");
            }
        });
    }

    private void setDataAdapter(List<WorkModel> works) {
        RecyclerViewAdapter.OnUserClickListener onUserClickListener = workModel -> {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                replaceFragment(activity);
            }
        };
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(works, onUserClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity(), RecyclerView.VERTICAL, false));
        binding.recyclerView.setAdapter(recyclerViewAdapter);
    }

    private Bundle setFragmentArgs() {
        Bundle bundle = new Bundle();
        bundle.putString(CLICKED_ID_ARG, workModel.getId());
        return bundle;
    }

    private void replaceFragment(@NotNull FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .replace(
                        R.id.navigation_content_frame,
                        CompletedWorksFragment.newInstance(setFragmentArgs())
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