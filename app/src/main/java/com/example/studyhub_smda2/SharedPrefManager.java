package com.example.studyhub_smda2;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPrefManager {

    private static final String PREF_NAME = "studyhub_prefs";

    // Keys
    private static final String KEY_DARK_MODE      = "studyhub.darkmode";
    private static final String KEY_SHOW_PREVIEW   = "studyhub.showPreview";
    private static final String KEY_LAST_SUBJECT   = "studyhub.lastSubject";
    private static final String KEY_FOLDER_COUNT   = "studyhub.folderCount";
    private static final String KEY_IMAGE_PATHS_PREFIX  = "studyhub.images.";
    private static final String KEY_FOLDER_NAMES_PREFIX = "studyhub.folders.";

    private final SharedPreferences prefs;

    public SharedPrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ── Dark mode ─────────────────────────────────────────────────────────────
    public void setDarkMode(boolean value) {
        prefs.edit().putBoolean(KEY_DARK_MODE, value).apply();
    }
    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    // ── Show preview ──────────────────────────────────────────────────────────
    public void setShowPreview(boolean value) {
        prefs.edit().putBoolean(KEY_SHOW_PREVIEW, value).apply();
    }
    public boolean isShowPreview() {
        return prefs.getBoolean(KEY_SHOW_PREVIEW, true);
    }

    // ── Last opened subject ───────────────────────────────────────────────────
    public void setLastSubject(String subjectName) {
        prefs.edit().putString(KEY_LAST_SUBJECT, subjectName).apply();
    }
    public String getLastSubject() {
        return prefs.getString(KEY_LAST_SUBJECT, "None");
    }

    // ── Folder count (total across app) ──────────────────────────────────────
    public void setFolderCount(int count) {
        prefs.edit().putInt(KEY_FOLDER_COUNT, count).apply();
    }
    public int getFolderCount() {
        return prefs.getInt(KEY_FOLDER_COUNT, 0);
    }

    // ── Folder names per subject ──────────────────────────────────────────────
    // Key format:  studyhub.folders.<subjectName>
    public void setFolderNames(String subjectName, Set<String> names) {
        prefs.edit().putStringSet(KEY_FOLDER_NAMES_PREFIX + subjectName, names).apply();
    }
    public Set<String> getFolderNames(String subjectName) {
        // Return a mutable copy so callers can modify it safely
        Set<String> saved = prefs.getStringSet(KEY_FOLDER_NAMES_PREFIX + subjectName, new HashSet<>());
        return new HashSet<>(saved);
    }
    public void removeFolderNames(String subjectName) {
        prefs.edit().remove(KEY_FOLDER_NAMES_PREFIX + subjectName).apply();
    }

    // ── Image paths per folder ────────────────────────────────────────────────
    // Key format:  studyhub.images.<subjectName>_<folderName>
    public void saveImagePaths(String key, Set<String> paths) {
        prefs.edit().putStringSet(KEY_IMAGE_PATHS_PREFIX + key, paths).apply();
    }
    public Set<String> getImagePaths(String key) {
        Set<String> saved = prefs.getStringSet(KEY_IMAGE_PATHS_PREFIX + key, new HashSet<>());
        return new HashSet<>(saved);
    }
    public void removeImagePaths(String key) {
        prefs.edit().remove(KEY_IMAGE_PATHS_PREFIX + key).apply();
    }

    // ── Clear all notes (image paths only) ───────────────────────────────────
    public void clearAllNotes() {
        SharedPreferences.Editor editor = prefs.edit();
        for (String key : prefs.getAll().keySet()) {
            if (key.startsWith(KEY_IMAGE_PATHS_PREFIX)) {
                editor.remove(key);
            }
        }
        editor.apply();
    }

    // ── Reset everything ──────────────────────────────────────────────────────
    public void resetAll() {
        prefs.edit().clear().apply();
    }
}