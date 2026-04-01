package com.example.studyhub_smda2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NotesFragment extends Fragment {

    RecyclerView recyclerView;
    NotesAdapter adapter;
    ArrayList<NoteImage> imageList;
    TextView tvFolderTitle;
    Button btnAddImage, btnShareImage;
    Uri selectedImageUri;
    String folderName = "";
    SharedPrefManager prefManager;

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    imageList.add(new NoteImage(selectedImageUri));
                    adapter.notifyDataSetChanged();

                    // save image path to SharedPreferences
                    Set<String> paths = new HashSet<>(prefManager.getImagePaths(folderName));
                    paths.add(selectedImageUri.toString());
                    prefManager.saveImagePaths(folderName, paths);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        // init views first
        tvFolderTitle = view.findViewById(R.id.tvFolderTitle);
        btnAddImage = view.findViewById(R.id.btnAddImage);
        btnShareImage = view.findViewById(R.id.btnShareImage);
        recyclerView = view.findViewById(R.id.notesRecyclerView);

        prefManager = new SharedPrefManager(getContext());

        // get folder name from arguments
        if (getArguments() != null) {
            folderName = getArguments().getString("folderName", "");
            tvFolderTitle.setText(folderName);
        }

        // load saved images from SharedPreferences
        imageList = new ArrayList<>();
        Set<String> savedPaths = prefManager.getImagePaths(folderName);
        for (String path : savedPaths) {
            imageList.add(new NoteImage(Uri.parse(path)));
        }

        adapter = new NotesAdapter(getContext(), imageList, (position, noteImage) -> {
            // delete callback - remove from list and SharedPreferences
            imageList.remove(position);
            adapter.notifyDataSetChanged();

            Set<String> paths = new HashSet<>(prefManager.getImagePaths(folderName));
            paths.remove(noteImage.getImageUri().toString());
            prefManager.saveImagePaths(folderName, paths);
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        btnAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            galleryLauncher.launch(intent);
        });

        btnShareImage.setOnClickListener(v -> {
            if (selectedImageUri == null) {
                Toast.makeText(getContext(), "No image selected to share!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, selectedImageUri);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        return view;
    }
}