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
import java.util.LinkedHashSet;
import java.util.Set;

public class NotesFragment extends Fragment {

    RecyclerView recyclerView;
    NotesAdapter adapter;
    ArrayList<NoteImage> imageList;
    TextView tvFolderTitle;
    Button btnAddImage, btnShareImage;
    Uri lastSelectedUri;

    String folderName  = "";
    String subjectName = "";
    String prefKey     = "";

    SharedPrefManager prefManager;

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri == null) return;

                    requireActivity().getContentResolver()
                            .takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    lastSelectedUri = uri;
                    imageList.add(new NoteImage(uri));
                    adapter.notifyDataSetChanged();

                    Set<String> paths = new LinkedHashSet<>(prefManager.getImagePaths(prefKey));
                    paths.add(uri.toString());
                    prefManager.saveImagePaths(prefKey, paths);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        tvFolderTitle = view.findViewById(R.id.tvFolderTitle);
        btnAddImage   = view.findViewById(R.id.btnAddImage);
        btnShareImage = view.findViewById(R.id.btnShareImage);
        recyclerView  = view.findViewById(R.id.notesRecyclerView);

        prefManager = new SharedPrefManager(getContext());

        if (getArguments() != null) {
            folderName  = getArguments().getString("folderName",  "");
            subjectName = getArguments().getString("subjectName", "");
        }

        prefKey = subjectName + "_" + folderName;
        tvFolderTitle.setText(folderName);

        Set<String> savedPaths = prefManager.getImagePaths(prefKey);
        imageList = new ArrayList<>();

        if (savedPaths.isEmpty()) {
            // Pre-load 3 copies of app_logo2 as default images
            String logoUri = "android.resource://" + requireContext().getPackageName() + "/" + R.drawable.app_logo2;
            imageList.add(new NoteImage(Uri.parse(logoUri)));
            imageList.add(new NoteImage(Uri.parse(logoUri)));
            imageList.add(new NoteImage(Uri.parse(logoUri)));

            Set<String> defaultPaths = new LinkedHashSet<>();
            defaultPaths.add(logoUri + "#1");
            defaultPaths.add(logoUri + "#2");
            defaultPaths.add(logoUri + "#3");
            prefManager.saveImagePaths(prefKey, defaultPaths);
        } else {
            for (String path : savedPaths) {
                // Strip the #1 #2 #3 suffix we added for uniqueness in the set
                String cleanPath = path.contains("#") ? path.substring(0, path.lastIndexOf("#")) : path;
                imageList.add(new NoteImage(Uri.parse(cleanPath)));
            }
        }

        boolean showPreview = prefManager.isShowPreview();

        adapter = new NotesAdapter(getContext(), imageList, showPreview, (position, noteImage) -> {
            imageList.remove(position);
            adapter.notifyDataSetChanged();

            Set<String> paths = new LinkedHashSet<>(prefManager.getImagePaths(prefKey));
            paths.remove(noteImage.getImageUri().toString());
            // Also try removing with suffixes
            paths.removeIf(p -> p.startsWith(noteImage.getImageUri().toString()));
            prefManager.saveImagePaths(prefKey, paths);
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        btnAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            galleryLauncher.launch(intent);
        });

        btnShareImage.setOnClickListener(v -> {
            if (imageList.isEmpty()) {
                Toast.makeText(getContext(), "No image to share!", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uriToShare = lastSelectedUri != null ? lastSelectedUri : imageList.get(0).getImageUri();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToShare);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        return view;
    }
}