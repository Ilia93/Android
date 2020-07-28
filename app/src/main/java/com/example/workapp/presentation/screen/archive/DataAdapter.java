package com.example.workapp.presentation.screen.archive;

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

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<WorkModel> listOfWorks;

    DataAdapter(Context context, List<WorkModel> listOfWorks) {
        this.layoutInflater = LayoutInflater.from(context);
        this.listOfWorks = listOfWorks;
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {
        WorkModel works = listOfWorks.get(position);
        holder.workName.setText(works.getName());
    }

    @Override
    public int getItemCount() {
        return listOfWorks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView workName;

        ViewHolder(View view) {
            super(view);
            workName = view.findViewById(R.id.workInfo);
        }
    }
}
