package com.example.studyhub_smda2;

import android.os.Bundle;
import android.view.View;

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

    TabLayout tabLayout;
    ViewPager2 viewPager;
    View fragmentContainer;
    View headerLayout;

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

        init();

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Subjects"); break;
                case 1: tab.setText("Folders");  break;
                case 2: tab.setText("Settings"); break;
            }
        }).attach();

        // Handle back press: if fragmentContainer is visible, pop the back stack.
        // If back stack becomes empty after popping, return to ViewPager.
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (fragmentContainer.getVisibility() == View.VISIBLE) {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                        // Still more fragments in back stack — just pop
                        getSupportFragmentManager().popBackStack();
                    } else {
                        // Last fragment — go back to ViewPager
                        getSupportFragmentManager().popBackStack();
                        showViewPager();
                    }
                } else {
                    // We are on the ViewPager — let the system handle (exits app)
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void init() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        headerLayout = findViewById(R.id.headerLayout);
    }

    /**
     * Call this before doing a fragment transaction to navigate
     * into a sub-screen (Folders or Notes).
     */
    public void showFragmentContainer() {
        fragmentContainer.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        // Disable tab switching while inside a sub-screen
        tabLayout.setEnabled(false);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.view != null) {
                tab.view.setClickable(false);
            }
        }
    }

    /**
     * Call this when returning to the main ViewPager.
     */
    public void showViewPager() {
        fragmentContainer.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        // Re-enable tabs
        tabLayout.setEnabled(true);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.view != null) {
                tab.view.setClickable(true);
            }
        }
    }
}