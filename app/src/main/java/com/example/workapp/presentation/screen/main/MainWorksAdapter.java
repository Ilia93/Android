package com.example.workapp.presentation.screen.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.work.WorkModel;

import java.util.List;

public class MainWorksAdapter extends RecyclerView.Adapter<MainWorksAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<WorkModel> listOfWorks;
    String activity_template = "Activity";

    MainWorksAdapter(Context context, List<WorkModel> listOfWorks) {
        this.layoutInflater = LayoutInflater.from(context);
        this.listOfWorks = listOfWorks;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.main_recycler_view_works, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkModel works = listOfWorks.get(position);
        holder.mainScreenWorkName.setText(works.getName() + " " + activity_template);
    }

    @Override
    public int getItemCount() {
        return listOfWorks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mainScreenWorkName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainScreenWorkName = itemView.findViewById(R.id.main_screen_work_name);
        }
    }
}
