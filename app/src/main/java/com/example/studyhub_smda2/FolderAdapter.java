package com.example.studyhub_smda2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FolderAdapter extends BaseAdapter {

    Context context;
    ArrayList<Folder> folderList;
    String subjectName;
    SharedPrefManager prefManager;
    FoldersFragment foldersFragment;

    public FolderAdapter(Context context, ArrayList<Folder> folderList,
                         String subjectName, SharedPrefManager prefManager,
                         FoldersFragment foldersFragment) {
        this.context         = context;
        this.folderList      = folderList;
        this.subjectName     = subjectName;
        this.prefManager     = prefManager;
        this.foldersFragment = foldersFragment;
    }

    @Override public int getCount()              { return folderList.size(); }
    @Override public Object getItem(int pos)     { return folderList.get(pos); }
    @Override public long getItemId(int pos)     { return pos; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false);
        }

        Folder folder = folderList.get(position);
        boolean isDark = prefManager.isDarkMode();

        TextView tvFolderName = convertView.findViewById(R.id.folderName);
        TextView tvImageCount = convertView.findViewById(R.id.imageCount);
        Button btnOpen        = convertView.findViewById(R.id.btnOpen);
        Button btnDelete      = convertView.findViewById(R.id.btnDelete);

        tvFolderName.setText(folder.getName());

        // Fresh image count from SharedPrefs
        String prefKey = subjectName + "_" + folder.getName();
        int count = prefManager.getImagePaths(prefKey).size();
        folder.setImageCount(count);
        tvImageCount.setText(count + " images");

        // Text colors for dark mode
        int textColor = isDark
                ? context.getColor(R.color.white)
                : context.getColor(R.color.darkPurple);
        tvFolderName.setTextColor(textColor);
        tvImageCount.setTextColor(isDark ? context.getColor(R.color.white) : 0xFF888888);

        // Card background
        convertView.setBackgroundColor(isDark
                ? context.getColor(R.color.darkPurple)
                : context.getColor(R.color.white));

        btnOpen.setOnClickListener(v -> foldersFragment.openNotes(folder.getName()));

        btnDelete.setOnClickListener(v -> {
            prefManager.removeImagePaths(subjectName + "_" + folder.getName());
            folderList.remove(position);
            notifyDataSetChanged();
            foldersFragment.onFolderDeleted();
        });

        return convertView;
    }
}