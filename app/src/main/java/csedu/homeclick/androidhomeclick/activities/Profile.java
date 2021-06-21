package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.profile_fragments.ProfileFragmentAdapter;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.navigator.TabLayoutHandler;
import csedu.homeclick.androidhomeclick.navigator.TopAppBarHandler;

import com.google.android.material.tabs.TabLayout;

public class Profile extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ProfileFragmentAdapter profileFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bindWidgets();


    }

    private void bindWidgets() {
        BottomNavBarHandler.setInstance(findViewById(R.id.profile_bottom_nav_bar), R.id.account);
        BottomNavBarHandler.handle(this);

        TopAppBarHandler.getInstance(findViewById(R.id.app_toolbaar), this).handle();

        tabLayout = findViewById(R.id.profile_tab_layout);
        viewPager = findViewById(R.id.profile_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        profileFragmentAdapter = new ProfileFragmentAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(profileFragmentAdapter);

        TabLayoutHandler.setInstance(tabLayout, viewPager);
        TabLayoutHandler.handle(this);
    }
}