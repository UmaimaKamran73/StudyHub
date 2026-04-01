package com.example.studyhub_smda2;

/**
 * Implemented by any Fragment that wants to react to dark mode changes.
 */
public interface ThemeAware {
    void onThemeChanged(boolean isDark);
}