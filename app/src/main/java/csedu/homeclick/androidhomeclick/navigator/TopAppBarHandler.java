package csedu.homeclick.androidhomeclick.navigator;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.AdFeed;
import csedu.homeclick.androidhomeclick.activities.CreatePost;
import csedu.homeclick.androidhomeclick.activities.UserSignIn;
//import csedu.homeclick.androidhomeclick.activities.create_post.AddPhotos;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.database.UserAuth;

public class TopAppBarHandler extends Activity implements MenuItem.OnMenuItemClickListener{
    private Toolbar toolbar;
    private View filter;
    private Activity activity;
    private UserService userService;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(UserAuth.isSignedIn()) {
            menu.getItem(2).setTitle("Sign out");
        } else {
            menu.getItem(2).setTitle("Sign in");
        }

        return super.onPrepareOptionsMenu(menu);
    }


    private TopAppBarHandler(Toolbar toolbar, Activity activity) {
        this.activity = activity;
        this.toolbar = toolbar;
        userService = new UserService();
        toolbar.inflateMenu(R.menu.top_app_bar);
        activity.invalidateOptionsMenu();
        onPrepareOptionsMenu(toolbar.getMenu());
    }

    public static TopAppBarHandler getInstance(Toolbar toolbar, Activity activity) {
        TopAppBarHandler topAppBarHandler = new TopAppBarHandler(toolbar, activity);

        return topAppBarHandler;
    }



    public void handle() {
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        UserAuth.setInstance();
        switch (item.getItemId()) {
            case R.id.sign_in_sign_out:
                if(userService.isSignedIn()) {
                    Log.i("signin", "user is signed in");
                    userService.signOut(activity.getApplicationContext());
                    toolbar.getMenu().getItem(2).setTitle("Sign in");
                    Intent targetIntent = new Intent(activity.getApplicationContext(), AdFeed.class);
                    activity.startActivity(targetIntent);
                } else {
                    Intent targetIntent = new Intent(activity.getApplicationContext(), UserSignIn.class);
                    Toast.makeText(activity.getApplicationContext(), "sign in button click", Toast.LENGTH_SHORT).show();
                    activity.startActivity(targetIntent);
                }
                break;
            case R.id.about_app:
                Toast.makeText(activity.getApplicationContext(), "Tapped about app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.filter:
                Toast.makeText(activity.getApplicationContext(), "sign out button click", Toast.LENGTH_SHORT).show();
                Intent targetIntent1 = new Intent(activity.getApplicationContext(), CreatePost.class);
                activity.startActivity(targetIntent1);

                break;
        }
        return true;
    }

}