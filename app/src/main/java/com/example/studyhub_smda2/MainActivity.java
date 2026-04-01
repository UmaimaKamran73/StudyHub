package com.example.studyhub_smda2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    public TabLayout tabLayout;
    public ViewPager2 viewPager;
    public String pendingSubjectName = null;
    private TextView tvAppTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabLayout  = findViewById(R.id.tabLayout);
        viewPager  = findViewById(R.id.viewPager);
        tvAppTitle = findViewById(R.id.tvAppTitle);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Subjects"); break;
                case 1: tab.setText("Folders");  break;
                case 2: tab.setText("Settings"); break;
            }
        }).attach();

        // Apply saved dark mode on startup
        applyDarkMode(new SharedPrefManager(this).isDarkMode());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (viewPager.getCurrentItem() == 1) {
                    FoldersFragment foldersFragment = getFoldersFragment();
                    if (foldersFragment != null && foldersFragment.handleBackPress()) {
                        return;
                    }
                    viewPager.setCurrentItem(0, true);
                    return;
                }
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    public void openFoldersForSubject(String subjectName) {
        pendingSubjectName = subjectName;
        viewPager.setCurrentItem(1, true);
    }

    public FoldersFragment getFoldersFragment() {
        String tag = "f" + viewPager.getId() + ":1";
        return (FoldersFragment) getSupportFragmentManager().findFragmentByTag(tag);
    }

    public void applyDarkMode(boolean isDark) {
        int bgColor   = isDark ? getColor(R.color.darkPurple)  : getColor(R.color.white);
        int headerBg  = isDark ? getColor(R.color.darkPurple)  : getColor(R.color.lightPurple);
        int titleColor = isDark ? getColor(R.color.white)      : getColor(R.color.darkPurple);
        int tabTextColor = isDark ? getColor(R.color.white)    : getColor(R.color.darkPurple);

        findViewById(R.id.main).setBackgroundColor(bgColor);
        findViewById(R.id.headerLayout).setBackgroundColor(headerBg);

        // StudyHub title text color
        if (tvAppTitle != null) tvAppTitle.setTextColor(titleColor);

        // Tab text colors
        tabLayout.setTabTextColors(tabTextColor, tabTextColor);

        // Push theme to all live fragments
        for (androidx.fragment.app.Fragment f :
                getSupportFragmentManager().getFragments()) {
            if (f instanceof ThemeAware && f.getView() != null) {
                ((ThemeAware) f).onThemeChanged(isDark);
            }
        }
    }

    public boolean isDarkMode() {
        return new SharedPrefManager(this).isDarkMode();
    }
}