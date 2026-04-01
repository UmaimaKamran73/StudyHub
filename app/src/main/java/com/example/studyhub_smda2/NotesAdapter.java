package com.example.studyhub_smda2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context context;
    ArrayList<NoteImage> imageList;
    OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDelete(int position, NoteImage noteImage);
    }

    public NotesAdapter(Context context, ArrayList<NoteImage> imageList, OnDeleteClickListener deleteListener) {
        this.context = context;
        this.imageList = imageList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note_image, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        NoteImage noteImage = imageList.get(position);
        holder.noteImage.setImageURI(noteImage.getImageUri());

        holder.btnDeleteImage.setOnClickListener(v -> {
            deleteListener.onDelete(position, noteImage);
        });
    }

    @Override
    public int getItemCount() { return imageList.size(); }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        ImageView noteImage;
        Button btnDeleteImage;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            noteImage = itemView.findViewById(R.id.noteImage);
            btnDeleteImage = itemView.findViewById(R.id.btnDeleteImage);
        }
    }
}