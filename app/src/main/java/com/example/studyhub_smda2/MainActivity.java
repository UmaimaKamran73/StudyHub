package com.example.studyhub_smda2;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
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

        ViewPagerAdapter adapter=new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
        {
            switch (position)
            {
                case 0:
                    tab.setText("Subjects");
                            break;
                case 1:
                    tab.setText("Folders");
                            break;
                case 2:
                    tab.setText("Settings");
                            break;
            }
        }).attach();
    }

    private void init()
    {
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
    }

    public void showFragmentContainer() {
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
    }

    public void showViewPager() {
        findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
    }

   /* @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                showViewPager();
            }
        } else {
            super.onBackPressed();
        }
    }*/

}
