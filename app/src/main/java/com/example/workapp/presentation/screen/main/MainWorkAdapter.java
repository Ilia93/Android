package com.example.workapp.presentation.screen.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.presentation.screen.main.recyclerview.TextHolder;
import com.example.workapp.presentation.screen.main.recyclerview.WorkHolder;
import com.example.workapp.presentation.screen.main.recyclerview.WorkTemplatesHolder;
import com.example.workapp.presentation.screen.main.recyclerview.WorkTemplatesModel;

import java.util.List;

public class MainWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int WORK_TEMPLATE_TYPE = 0;
    private final int WORK_TYPE = 1;
    private final int HEADER_TYPE = 2;

    private List<Object> listOfWorks;
    private OnUserCardClickListener onUserCardClickListener;

    public MainWorkAdapter(List<Object> listOfWorks, OnUserCardClickListener onUserCardClickListener) {
        this.listOfWorks = listOfWorks;
        this.onUserCardClickListener = onUserCardClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case WORK_TYPE:
                view = layoutInflater.inflate(R.layout.main_recycler_view_works, parent,
                        false);
                viewHolder = new WorkHolder(view);
                break;
            case HEADER_TYPE:
                view = layoutInflater.inflate(R.layout.main_recycler_view_text, parent,
                        false);
                viewHolder = new TextHolder(view);
                break;
            default:
                view = layoutInflater.inflate(R.layout.main_recycler_view_work_templates, parent, false);
                viewHolder = new WorkTemplatesHolder(view, listOfWorks, onUserCardClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (listOfWorks.get(position) instanceof WorkModel) {
            return WORK_TYPE;
        } else if (listOfWorks.get(position) instanceof String) {
            return HEADER_TYPE;
        }
        return WORK_TEMPLATE_TYPE;
    }

    @Override
    public int getItemCount() {
        return listOfWorks.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case WORK_TYPE:
                WorkHolder workHolder = (WorkHolder) holder;
                WorkModel workModel = (WorkModel) listOfWorks.get(position);
                if (workModel != null) {
                    workHolder.getActivityDescription().setText(workModel.getName());
                }
                break;
            case HEADER_TYPE:
                TextHolder textHolder = (TextHolder) holder;
                textHolder.getTextHeader().setText((CharSequence) listOfWorks.get(position));
                break;
            default:
                WorkTemplatesHolder workTemplatesHolder = (WorkTemplatesHolder) holder;
                WorkTemplatesModel workTemplatesModel = (WorkTemplatesModel) listOfWorks.get(position);
                if (workTemplatesModel != null) {
                    workTemplatesHolder.getWorkImage().setImageResource(workTemplatesModel.getWorkImage());
                    workTemplatesHolder.getActivityDescription().setText(workTemplatesModel.getActivityDescription());
                }
                break;
        }
    }

    public interface OnUserCardClickListener {
        void onUserClick(WorkTemplatesModel mainTemplatesModel);
    }
}