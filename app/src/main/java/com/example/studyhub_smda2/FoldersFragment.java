package com.example.studyhub_smda2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoldersFragment extends Fragment {

    ListView listView;
    FolderAdapter adapter;

    ArrayList<Folder> folderList;
    TextView tvSubjectTitle;
    String subjectName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders, container, false);

        listView = view.findViewById(R.id.listView);
        tvSubjectTitle = view.findViewById(R.id.tvSubjectTitle);

        if (getArguments() != null) {
            subjectName = getArguments().getString("subjectName", "");
            tvSubjectTitle.setText(subjectName);
        }

        loadData();
        adapter = new FolderAdapter(getContext(), folderList);
        listView.setAdapter(adapter);

        return view;
    }

    private void loadData() {
        folderList = new ArrayList<>();
        folderList.add(new Folder("Lectures", 5));
        folderList.add(new Folder("Assignments", 3));
        folderList.add(new Folder("Quiz Preparation", 2));
        folderList.add(new Folder("Lab Work", 4));

        new SharedPrefManager(getContext()).setFolderCount(folderList.size());

    }
}