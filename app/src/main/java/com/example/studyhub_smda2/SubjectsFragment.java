package com.example.studyhub_smda2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectsFragment extends Fragment implements ThemeAware {

    RecyclerView recyclerView;
    SubjectAdapter adapter;
    ArrayList<Subject> subjectList;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_subjects, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView );
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadData();
        adapter = new SubjectAdapter(getContext(), subjectList);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            onThemeChanged(((MainActivity) getActivity()).isDarkMode());
        }
    }

    private void loadData() {
        subjectList = new ArrayList<>();
        subjectList.add(new Subject("Mathematics",    4, R.drawable.app_logo2));
        subjectList.add(new Subject("Physics",        4, R.drawable.app_logo2));
        subjectList.add(new Subject("Computer Science", 4, R.drawable.app_logo2));
        subjectList.add(new Subject("English",        4, R.drawable.app_logo2));
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        if (rootView == null) return;
        int bg = isDark
                ? requireContext().getColor(R.color.darkPurple)
                : requireContext().getColor(R.color.white);
        rootView.setBackgroundColor(bg);
    }
}