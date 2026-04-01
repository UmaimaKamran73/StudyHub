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

    public FolderAdapter(Context context, ArrayList<Folder> folderList) {
        this.context = context;
        this.folderList = folderList;
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

        TextView folderName = convertView.findViewById(R.id.folderName);
        TextView imageCount = convertView.findViewById(R.id.imageCount);
        Button btnOpen = convertView.findViewById(R.id.btnOpen);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        folderName.setText(folder.getName());
        imageCount.setText(folder.getImageCount() + " images");

        btnOpen.setOnClickListener(v -> {
            // navigate to NotesFragment, pass folder name
            NotesFragment notesFragment = new NotesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("folderName", folder.getName());
            notesFragment.setArguments(bundle);

            AppCompatActivity activity = (AppCompatActivity) context;
            ((MainActivity) activity).showFragmentContainer(); // add this line

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, notesFragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnDelete.setOnClickListener(v -> {
            folderList.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}