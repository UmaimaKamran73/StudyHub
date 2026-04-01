package com.example.studyhub_smda2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FolderAdapter extends BaseAdapter {

    Context context;
    ArrayList<Folder> folderList;
    String subjectName;
    SharedPrefManager prefManager;

    public FolderAdapter(Context context, ArrayList<Folder> folderList,
                         String subjectName, SharedPrefManager prefManager) {
        this.context = context;
        this.folderList = folderList;
        this.subjectName = subjectName;
        this.prefManager = prefManager;
    }

    @Override
    public int getCount() { return folderList.size(); }

    @Override
    public Object getItem(int position) { return folderList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false);
        }

        Folder folder = folderList.get(position);

        TextView tvFolderName = convertView.findViewById(R.id.folderName);
        TextView tvImageCount = convertView.findViewById(R.id.imageCount);
        Button btnOpen        = convertView.findViewById(R.id.btnOpen);
        Button btnDelete      = convertView.findViewById(R.id.btnDelete);

        tvFolderName.setText(folder.getName());

        // Refresh image count from SharedPreferences each time view is drawn
        String prefKey = subjectName + "_" + folder.getName();
        int count = prefManager.getImagePaths(prefKey).size();
        folder.setImageCount(count);
        tvImageCount.setText(count + " images");

        btnOpen.setOnClickListener(v -> {
            NotesFragment notesFragment = new NotesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("folderName", folder.getName());
            bundle.putString("subjectName", subjectName);
            notesFragment.setArguments(bundle);

            AppCompatActivity activity = (AppCompatActivity) context;
            // fragmentContainer is already visible (we are inside it),
            // so just replace and add to back stack.
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, notesFragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnDelete.setOnClickListener(v -> {
            String key = subjectName + "_" + folder.getName();
            // Remove all images stored under this folder
            prefManager.removeImagePaths(key);
            folderList.remove(position);
            notifyDataSetChanged();

            // Tell the fragment to persist the updated list
            if (context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                FoldersFragment frag = (FoldersFragment) activity
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainer);
                if (frag != null) frag.onFolderDeleted();
            }
        });

        return convertView;
    }
}