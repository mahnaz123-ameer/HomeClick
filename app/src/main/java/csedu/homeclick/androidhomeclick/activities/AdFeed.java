package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.handler.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.handler.TopAppBarHandler;

public class AdFeed extends AppCompatActivity {

    private Toolbar toolbar;
    private UserService userService;

    @Override
    protected void onResume() {
        super.onResume();
        if(userService.isSignedIn()) {
            toolbar.getMenu().getItem(2).setTitle("Sign out");
        } else {
            toolbar.getMenu().getItem(2).setTitle("Sign in");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_feed);



        bindWidgets();

        if(getIntent().getData() != null) {
            userService.completeSignIn(getIntent(), getApplicationContext());
        }

    }

    private void bindWidgets() {
        BottomNavBarHandler.setInstance(findViewById(R.id.bottom_navigation_bar), R.id.home);
        BottomNavBarHandler.handle(this);

        toolbar = findViewById(R.id.app_toolbaar);
        TopAppBarHandler.getInstance(toolbar, this).handle();

        userService = new UserService();
    }
}

