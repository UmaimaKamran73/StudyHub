package com.example.studyhub_smda2;

import android.net.Uri;

public class NoteImage {
    Uri imageUri;

    public NoteImage(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() { return imageUri; }
}
