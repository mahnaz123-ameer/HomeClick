package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.sign_in_fragments.SignInFragmentAdapter;
import csedu.homeclick.androidhomeclick.handler.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.handler.TabLayoutHandler;
import csedu.homeclick.androidhomeclick.handler.TopAppBarHandler;

import com.google.android.material.tabs.TabLayout;

public class UserSignIn extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private SignInFragmentAdapter adapter;
    private FragmentManager fm;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);

        bindWidgets();



    }

    private void bindWidgets() {
        toolbar = findViewById(R.id.app_toolbaar);
        tabLayout = findViewById(R.id.signInTabLayout);
        viewPager = findViewById(R.id.user_sign_in_view_pager);
        fm = getSupportFragmentManager();
        adapter = new SignInFragmentAdapter(fm, getLifecycle());
        viewPager.setAdapter(adapter);

        TabLayoutHandler.setInstance(tabLayout, viewPager);
        TabLayoutHandler.handle(this);

        BottomNavBarHandler.setInstance(findViewById(R.id.bottom_navigation_bar), R.id.account);
        BottomNavBarHandler.handle(this);

        TopAppBarHandler.getInstance(toolbar, this).handle();
    }
}