package com.example.studyhub_smda2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FoldersFragment extends Fragment implements ThemeAware {

    ListView listView;
    FolderAdapter adapter;
    ArrayList<Folder> folderList;
    TextView tvSubjectTitle;
    Button btnAddFolder;
    FrameLayout notesContainer;
    LinearLayout rootLayout;

    String subjectName = "";
    SharedPrefManager prefManager;
    boolean notesOpen = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders, container, false);

        rootLayout     = view.findViewById(R.id.foldersRoot);
        listView       = view.findViewById(R.id.listView);
        tvSubjectTitle = view.findViewById(R.id.tvSubjectTitle);
        btnAddFolder   = view.findViewById(R.id.btnAddFolder);
        notesContainer = view.findViewById(R.id.notesContainer);

        prefManager = new SharedPrefManager(getContext());

        showNoSubjectState();

        btnAddFolder.setOnClickListener(v -> showAddFolderDialog());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Pick up subject name set by MainActivity before switching tabs
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity.pendingSubjectName != null) {
                loadSubject(activity.pendingSubjectName);
                activity.pendingSubjectName = null; // clear it
            }
        }
        // Apply saved dark mode
        if (getActivity() instanceof MainActivity) {
            boolean isDark = ((MainActivity) getActivity()).isDarkMode();
            onThemeChanged(isDark);
        }
    }

    public void loadSubject(String name) {
        subjectName = name;
        tvSubjectTitle.setText(name);
        btnAddFolder.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        notesContainer.setVisibility(View.GONE);
        notesOpen = false;

        prefManager.setLastSubject(name);
        loadData();
        adapter = new FolderAdapter(getContext(), folderList, subjectName, prefManager, this);
        listView.setAdapter(adapter);
    }

    private void showNoSubjectState() {
        tvSubjectTitle.setText("Select a subject from the Subjects tab");
        btnAddFolder.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        notesContainer.setVisibility(View.GONE);
    }

    public void openNotes(String folderName) {
        notesOpen = true;
        notesContainer.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        btnAddFolder.setVisibility(View.GONE);
        tvSubjectTitle.setText(folderName);

        NotesFragment notesFragment = new NotesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("folderName", folderName);
        bundle.putString("subjectName", subjectName);
        notesFragment.setArguments(bundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.notesContainer, notesFragment)
                .commit();
    }

    public boolean handleBackPress() {
        if (notesOpen) {
            notesOpen = false;
            notesContainer.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            btnAddFolder.setVisibility(View.VISIBLE);
            tvSubjectTitle.setText(subjectName);
            if (adapter != null) adapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private void loadData() {
        folderList = new ArrayList<>();
        Set<String> savedFolderNames = prefManager.getFolderNames(subjectName);

        if (savedFolderNames.isEmpty()) {
            folderList.add(new Folder("Lectures", 0));
            folderList.add(new Folder("Assignments", 0));
            folderList.add(new Folder("Quiz Preparation", 0));
            folderList.add(new Folder("Lab Work", 0));
            persistFolderList();
        } else {
            for (String name : savedFolderNames) {
                int count = prefManager.getImagePaths(subjectName + "_" + name).size();
                folderList.add(new Folder(name, count));
            }
        }
        prefManager.setFolderCount(folderList.size());
    }

    public void persistFolderList() {
        Set<String> names = new HashSet<>();
        for (Folder f : folderList) names.add(f.getName());
        prefManager.setFolderNames(subjectName, names);
        prefManager.setFolderCount(names.size());
    }

    public void onFolderDeleted() {
        persistFolderList();
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
            for (Folder f : folderList) {
                if (f.getName().equalsIgnoreCase(name)) {
                    Toast.makeText(getContext(), "Folder already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            folderList.add(new Folder(name, 0));
            persistFolderList();
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Folder added!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        if (rootLayout == null) return;
        int bg = isDark
                ? requireContext().getColor(R.color.darkPurple)
                : requireContext().getColor(R.color.white);
        rootLayout.setBackgroundColor(bg);
    }
}