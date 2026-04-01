package com.example.studyhub_smda2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class SubjectsFragment extends Fragment {

    RecyclerView recyclerView;
    SubjectAdapter adapter;
    ArrayList<Subject> subjectList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subjects, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        loadData();
        setupRecyclerView();

        return view;
    }

    private void loadData() {
        subjectList = new ArrayList<>();
        subjectList.add(new Subject("Data Structures", 3, R.drawable.app_logo2));
        subjectList.add(new Subject("Operating Systems", 4, R.drawable.app_logo2));
        subjectList.add(new Subject("Artificial Intelligence", 2, R.drawable.app_logo2));
        subjectList.add(new Subject("Software for Mobile Devices", 5, R.drawable.app_logo2));
    }

    private void setupRecyclerView() {
        adapter = new SubjectAdapter(getContext(), subjectList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }
}