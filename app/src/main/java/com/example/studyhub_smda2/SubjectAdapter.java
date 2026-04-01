package com.example.studyhub_smda2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    Context context;
    ArrayList<Subject> subjectList;

    public SubjectAdapter(Context context, ArrayList<Subject> subjectList) {
        this.context = context;
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

        holder.subjectName.setText(subject.getName());
        holder.folderCount.setText(subject.getFolderCount() + " folders");
        holder.subjectIcon.setImageResource(subject.getIconResId());

        holder.btnOpenFolder.setOnClickListener(v -> {
            // Save last opened subject
            new SharedPrefManager(context).setLastSubject(subject.getName());

            // Build FoldersFragment with subject name
            FoldersFragment foldersFragment = new FoldersFragment();
            Bundle bundle = new Bundle();
            bundle.putString("subjectName", subject.getName());
            foldersFragment.setArguments(bundle);

            AppCompatActivity activity = (AppCompatActivity) context;
            // Show the fragment container (hides ViewPager, disables tabs)
            ((MainActivity) activity).showFragmentContainer();

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, foldersFragment)
                    .addToBackStack(null)
                    .commit();
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
            subjectName  = itemView.findViewById(R.id.subjectName);
            folderCount  = itemView.findViewById(R.id.folderCount);
            subjectIcon  = itemView.findViewById(R.id.subjectIcon);
            btnOpenFolder = itemView.findViewById(R.id.btnOpenFolder);
        }
    }
}