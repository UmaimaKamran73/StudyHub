package com.example.studyhub_smda2;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPrefManager {

    private static final String PREF_NAME = "studyhub_prefs";

    // Keys
    private static final String KEY_DARK_MODE = "studyhub.darkmode";
    private static final String KEY_SHOW_PREVIEW = "studyhub.showPreview";
    private static final String KEY_LAST_SUBJECT = "studyhub.lastSubject";
    private static final String KEY_FOLDER_COUNT = "studyhub.folderCount";
    private static final String KEY_IMAGE_PATHS_PREFIX = "studyhub.images.";

    private SharedPreferences prefs;

    public SharedPrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Dark mode
    public void setDarkMode(boolean value) {
        prefs.edit().putBoolean(KEY_DARK_MODE, value).apply();
    }
    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    // Show preview
    public void setShowPreview(boolean value) {
        prefs.edit().putBoolean(KEY_SHOW_PREVIEW, value).apply();
    }
    public boolean isShowPreview() {
        return prefs.getBoolean(KEY_SHOW_PREVIEW, true);
    }

    // Last opened subject
    public void setLastSubject(String subjectName) {
        prefs.edit().putString(KEY_LAST_SUBJECT, subjectName).apply();
    }
    public String getLastSubject() {
        return prefs.getString(KEY_LAST_SUBJECT, "None");
    }

    // Folder count
    public void setFolderCount(int count) {
        prefs.edit().putInt(KEY_FOLDER_COUNT, count).apply();
    }
    public int getFolderCount() {
        return prefs.getInt(KEY_FOLDER_COUNT, 0);
    }

    // Image paths per folder
    public void saveImagePaths(String folderName, Set<String> paths) {
        prefs.edit().putStringSet(KEY_IMAGE_PATHS_PREFIX + folderName, paths).apply();
    }
    public Set<String> getImagePaths(String folderName) {
        return prefs.getStringSet(KEY_IMAGE_PATHS_PREFIX + folderName, new HashSet<>());
    }
    public void removeImagePaths(String folderName) {
        prefs.edit().remove(KEY_IMAGE_PATHS_PREFIX + folderName).apply();
    }

    // Clear all notes (image paths only)
    public void clearAllNotes() {
        SharedPreferences.Editor editor = prefs.edit();
        for (String key : prefs.getAll().keySet()) {
            if (key.startsWith(KEY_IMAGE_PATHS_PREFIX)) {
                editor.remove(key);
            }
        }
        editor.apply();
    }

    // Reset everything
    public void resetAll() {
        prefs.edit().clear().apply();
    }
}