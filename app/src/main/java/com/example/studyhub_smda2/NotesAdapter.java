package com.example.studyhub_smda2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    public interface OnDeleteListener {
        void onDelete(int position, NoteImage noteImage);
    }

    Context context;
    ArrayList<NoteImage> imageList;
    boolean showPreview;
    OnDeleteListener deleteListener;

    public NotesAdapter(Context context, ArrayList<NoteImage> imageList,
                        boolean showPreview, OnDeleteListener deleteListener) {
        this.context        = context;
        this.imageList      = imageList;
        this.showPreview    = showPreview;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteImage noteImage = imageList.get(position);

        if (showPreview) {
            // Show the actual image
            holder.imageView.setVisibility(View.VISIBLE);
            holder.tvNoPreview.setVisibility(View.GONE);
            holder.imageView.setImageURI(noteImage.getImageUri());
        } else {
            // Hide image, show placeholder text instead
            holder.imageView.setVisibility(View.GONE);
            holder.tvNoPreview.setVisibility(View.VISIBLE);
            holder.tvNoPreview.setText("Image " + (position + 1));
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(holder.getAdapterPosition(), noteImage);
            }
        });
    }

    @Override
    public int getItemCount() { return imageList.size(); }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvNoPreview;
        View btnDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView    = itemView.findViewById(R.id.noteImage);
            tvNoPreview  = itemView.findViewById(R.id.tvNoPreview);
            btnDelete    = itemView.findViewById(R.id.btnDeleteNote);
        }
    }
}