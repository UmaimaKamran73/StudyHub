package com.example.studyhub_smda2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FoldersFragment extends Fragment {

    ListView listView;
    FolderAdapter adapter;
    ArrayList<Folder> folderList;
    TextView tvSubjectTitle;
    Button btnAddFolder;
    String subjectName = "";
    SharedPrefManager prefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders, container, false);

        listView      = view.findViewById(R.id.listView);
        tvSubjectTitle = view.findViewById(R.id.tvSubjectTitle);
        btnAddFolder  = view.findViewById(R.id.btnAddFolder);

        prefManager = new SharedPrefManager(getContext());

        if (getArguments() != null) {
            subjectName = getArguments().getString("subjectName", "");
        }

        if (subjectName.isEmpty()) {
            // Opened from the Folders tab directly — no subject selected yet
            tvSubjectTitle.setText("Select a subject first");
            btnAddFolder.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
        } else {
            tvSubjectTitle.setText(subjectName);
            btnAddFolder.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            loadData();
            adapter = new FolderAdapter(getContext(), folderList, subjectName, prefManager);
            listView.setAdapter(adapter);
        }

        btnAddFolder.setOnClickListener(v -> showAddFolderDialog());

        return view;
    }

    private void loadData() {
        folderList = new ArrayList<>();

        // Load persisted folders for this subject
        Set<String> savedFolderNames = prefManager.getFolderNames(subjectName);

        if (savedFolderNames.isEmpty()) {
            // First time: add defaults and save them
            folderList.add(new Folder("Lectures", 0));
            folderList.add(new Folder("Assignments", 0));
            folderList.add(new Folder("Quiz Preparation", 0));
            folderList.add(new Folder("Lab Work", 0));
            persistFolderList();
        } else {
            for (String name : savedFolderNames) {
                int imageCount = prefManager.getImagePaths(subjectName + "_" + name).size();
                folderList.add(new Folder(name, imageCount));
            }
        }

        prefManager.setFolderCount(folderList.size());
    }

    private void persistFolderList() {
        Set<String> names = new HashSet<>();
        for (Folder f : folderList) {
            names.add(f.getName());
        }
        prefManager.setFolderNames(subjectName, names);
        prefManager.setFolderCount(names.size());
    }

    private void showAddFolderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Folder");

        final EditText input = new EditText(getContext());
        input.setHint("Folder name");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = input.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Folder name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            // Check duplicate
            for (Folder f : folderList) {
                if (f.getName().equalsIgnoreCase(name)) {
                    Toast.makeText(getContext(), "Folder already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            folderList.add(new Folder(name, 0));
            persistFolderList();
            adapter.notifyDataSetChanged();
            prefManager.setFolderCount(folderList.size());
            Toast.makeText(getContext(), "Folder added!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Called by FolderAdapter when a folder is deleted,
     * so we can persist the updated list.
     */
    public void onFolderDeleted() {
        persistFolderList();
        prefManager.setFolderCount(folderList.size());
    }
}