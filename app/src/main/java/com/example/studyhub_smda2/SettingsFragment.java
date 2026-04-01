package com.example.studyhub_smda2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment implements ThemeAware {

    Switch switchDarkMode, switchImagePreview;
    Button btnClearNotes, btnResetData;
    TextView tvLastSubject, tvFolderCount;
    SharedPrefManager prefManager;
    ScrollView rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        prefManager = new SharedPrefManager(getContext());

        rootLayout = view.findViewById(R.id.settingsRoot);
        switchDarkMode     = view.findViewById(R.id.switchDarkMode);
        switchImagePreview = view.findViewById(R.id.switchImagePreview);
        btnClearNotes      = view.findViewById(R.id.btnClearNotes);
        btnResetData       = view.findViewById(R.id.btnResetData);
        tvLastSubject      = view.findViewById(R.id.tvLastSubject);
        tvFolderCount      = view.findViewById(R.id.tvFolderCount);

        // Restore saved states
        switchDarkMode.setChecked(prefManager.isDarkMode());
        switchImagePreview.setChecked(prefManager.isShowPreview());
        tvLastSubject.setText("Last opened subject: " + prefManager.getLastSubject());
        tvFolderCount.setText("Total folders: " + prefManager.getFolderCount());

        // Apply saved dark mode on load
        onThemeChanged(prefManager.isDarkMode());

        switchDarkMode.setOnCheckedChangeListener((btn, isChecked) -> {
            prefManager.setDarkMode(isChecked);
            // Push to MainActivity which broadcasts to all fragments
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).applyDarkMode(isChecked);
            }
            Toast.makeText(getContext(),
                    "Dark mode " + (isChecked ? "enabled" : "disabled"),
                    Toast.LENGTH_SHORT).show();
        });

        switchImagePreview.setOnCheckedChangeListener((btn, isChecked) -> {
            prefManager.setShowPreview(isChecked);
            Toast.makeText(getContext(),
                    "Image preview " + (isChecked ? "enabled" : "disabled"),
                    Toast.LENGTH_SHORT).show();
        });

        btnClearNotes.setOnClickListener(v -> {
            prefManager.clearAllNotes();
            Toast.makeText(getContext(), "All notes cleared!", Toast.LENGTH_SHORT).show();
        });

        btnResetData.setOnClickListener(v -> {
            prefManager.resetAll();
            switchDarkMode.setChecked(false);
            switchImagePreview.setChecked(true);
            tvLastSubject.setText("Last opened subject: None");
            tvFolderCount.setText("Total folders: 0");
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).applyDarkMode(false);
            }
            Toast.makeText(getContext(), "App data reset!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onThemeChanged(prefManager.isDarkMode());
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        if (rootLayout == null) return;
        int bg = isDark
                ? requireContext().getColor(R.color.darkPurple)
                : requireContext().getColor(R.color.white);
        rootLayout.setBackgroundColor(bg);
    }
}