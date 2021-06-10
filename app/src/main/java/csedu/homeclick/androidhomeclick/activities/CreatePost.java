package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.createpost.CreatePostFragmentAdapter;
import csedu.homeclick.androidhomeclick.activities.sign_in_fragments.SignInFragmentAdapter;
import csedu.homeclick.androidhomeclick.handler.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.handler.TabLayoutHandler;
import csedu.homeclick.androidhomeclick.handler.TopAppBarHandler;

public class CreatePost extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CreatePostFragmentAdapter adapter;
    private FragmentManager fm;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        bindWidgets();
    }

    private void bindWidgets() {

        tabLayout = findViewById(R.id.create_post_TabLayout);
        viewPager = findViewById(R.id.create_post_view_pager);
        fm = getSupportFragmentManager();
        adapter = new CreatePostFragmentAdapter(fm, getLifecycle());
        viewPager.setAdapter(adapter);

        TabLayoutHandler.setInstance(tabLayout, viewPager);
        TabLayoutHandler.handle(this);

        BottomNavBarHandler.setInstance(findViewById(R.id.bottom_navigation_bar),R.id.add);
        BottomNavBarHandler.handle(this);

        toolbar = findViewById(R.id.app_toolbaar);
        TopAppBarHandler.getInstance(toolbar, this).handle();
    }
}