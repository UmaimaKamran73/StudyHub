package com.example.studyhub_smda2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    Context context;
    ArrayList<Subject> subjectList;

    public SubjectAdapter(Context context, ArrayList<Subject> subjectList) {
        this.context     = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        boolean isDark  = new SharedPrefManager(context).isDarkMode();

        holder.subjectName.setText(subject.getName());
        holder.folderCount.setText(subject.getFolderCount() + " folders");
        holder.subjectIcon.setImageResource(subject.getIconResId());

        // Text color: white in dark mode, darkPurple in light
        int textColor = isDark
                ? context.getColor(R.color.white)
                : context.getColor(R.color.darkPurple);
        holder.subjectName.setTextColor(textColor);

        holder.btnOpenFolder.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) context;
            activity.openFoldersForSubject(subject.getName());
        });
    }

    @Override
    public int getItemCount() { return subjectList.size(); }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, folderCount;
        ImageView subjectIcon;
        Button btnOpenFolder;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName   = itemView.findViewById(R.id.subjectName);
            folderCount   = itemView.findViewById(R.id.folderCount);
            subjectIcon   = itemView.findViewById(R.id.subjectIcon);
            btnOpenFolder = itemView.findViewById(R.id.btnOpenFolder);
        }
    }
}