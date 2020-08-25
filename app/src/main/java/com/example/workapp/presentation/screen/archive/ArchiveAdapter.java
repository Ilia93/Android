package com.example.workapp.presentation.screen.archive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.work.WorkModel;

import java.util.List;

class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ViewHolder> {
    private List<WorkModel> listOfWorks;
    private OnUserClickListener onUserClickListener;

    ArchiveAdapter(List<WorkModel> listOfWorks, OnUserClickListener onUserClickListener) {
        this.listOfWorks = listOfWorks;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public ArchiveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.archive_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkModel works = listOfWorks.get(position);
        holder.workName.setText(works.getName());
        if (listOfWorks.get(position).isCompleted()) {
            holder.isCompleted.setChecked(true);
        } else {
            holder.isCompleted.setChecked(false);
        }
        holder.workShortInfo.setText(R.string.archive_work_short_information);
        holder.workCompletedInformation.setText(R.string.archive_work_completed_text);
    }

    @Override
    public int getItemCount() {
        return listOfWorks.size();
    }

    public interface OnUserClickListener {
        void onUserClick(WorkModel workModel);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView workName, workShortInfo, workCompletedInformation;
        final CheckBox isCompleted;

        ViewHolder(View view) {
            super(view);
            workName = view.findViewById(R.id.workInfo);
            isCompleted = view.findViewById(R.id.is_completed_flag);
            workShortInfo = view.findViewById(R.id.work_short_info);
            workCompletedInformation = view.findViewById(R.id.work_completed_info);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WorkModel workModel = listOfWorks.get(getLayoutPosition());
                    onUserClickListener.onUserClick(workModel);
                }
            });
        }
    }
}
