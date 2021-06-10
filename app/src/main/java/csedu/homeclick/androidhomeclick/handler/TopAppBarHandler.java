package csedu.homeclick.androidhomeclick.handler;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.AdFeed;
import csedu.homeclick.androidhomeclick.activities.CreatePost;
import csedu.homeclick.androidhomeclick.activities.Filter;
import csedu.homeclick.androidhomeclick.activities.Profile;
import csedu.homeclick.androidhomeclick.activities.UserSignIn;
import csedu.homeclick.androidhomeclick.activities.createpost.AddPhotos;
import csedu.homeclick.androidhomeclick.database.UserAuth;

public class TopAppBarHandler extends Activity implements MenuItem.OnMenuItemClickListener{
    private Toolbar toolbar;
    private View filter;
    private Activity activity;

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
                if(UserAuth.isSignedIn()) {
                    UserAuth.signOut(activity.getApplicationContext());
                } else {
                    Intent targetIntent = new Intent(activity.getApplicationContext(), UserSignIn.class);
                    Toast.makeText(activity.getApplicationContext(), "sign out button click", Toast.LENGTH_SHORT).show();
                    activity.startActivity(targetIntent);
                }
                break;
            case R.id.about_app:
                Toast.makeText(activity.getApplicationContext(), "Tapped about app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.filter:
                Toast.makeText(activity.getApplicationContext(), "Filter pressed", Toast.LENGTH_SHORT).show();
                Intent targetIntent1 = new Intent(activity.getApplicationContext(), AddPhotos.class);
                Toast.makeText(activity.getApplicationContext(), "sign out button click", Toast.LENGTH_SHORT).show();
                activity.startActivity(targetIntent1);

                break;
        }
        return true;
    }

}
