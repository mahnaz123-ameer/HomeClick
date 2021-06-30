package csedu.homeclick.androidhomeclick.navigator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationView;


import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.AdFeed;
import csedu.homeclick.androidhomeclick.activities.CreatePost;
import csedu.homeclick.androidhomeclick.activities.Profile;
import csedu.homeclick.androidhomeclick.activities.UserSignIn;
import csedu.homeclick.androidhomeclick.connector.UserService;

public class BottomNavBarHandler {
    private static final String TAG = "BottomNavBarHandler";
    private static BottomNavBarHandler bottomNavBarHandler;
    private static BottomNavigationView bottomNavigationView;
    private static int selectedItem;
    private static UserService userService;

    private BottomNavBarHandler(View view, int selectIt) {
        bottomNavigationView = (BottomNavigationView) view;
        selectedItem = selectIt;
        bottomNavigationView.setSelectedItemId(selectedItem);
        userService = new UserService();
    }

    public static void setInstance(View view, int selectIt) {
        bottomNavBarHandler = new BottomNavBarHandler(view, selectIt);
    }

    @SuppressLint("NonConstantResourceId")
    public static void handle(final Activity activity) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    activity.startActivity(new Intent(activity.getApplicationContext(), AdFeed.class));
                    break;
                case R.id.create:
                    Log.i(TAG, "create from bottom nav selected");
                    if(userService.isSignedIn()) {
                        activity.startActivity(new Intent(activity.getApplicationContext(),CreatePost.class));
                    } else {
                        activity.startActivity(new Intent(activity.getApplicationContext(), UserSignIn.class));
                    }

                    break;
                case R.id.account:

                    if(userService.isSignedIn()) {
                        activity.startActivity(new Intent(activity.getApplicationContext(), Profile.class));
                    } else {
                        Log.i("TAG", "is not signed in");
                        activity.startActivity(new Intent(activity.getApplicationContext(), UserSignIn.class));
                    }

                    break;
                default:
                    break;
            }
            return true;
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    activity.startActivity(new Intent(activity.getApplicationContext(), AdFeed.class));

                    break;
                case R.id.create:
                    Log.i(TAG, "create reselected");

                    break;
                case R.id.account:
                    Log.i(TAG, "account reselected");
                    activity.startActivity(new Intent(activity.getApplicationContext(), Profile.class));
                    break;
                default:
                    break;
            }
        });
    }

}
