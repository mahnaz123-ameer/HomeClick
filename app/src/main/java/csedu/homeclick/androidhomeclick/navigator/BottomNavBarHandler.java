package csedu.homeclick.androidhomeclick.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.AdFeed;
import csedu.homeclick.androidhomeclick.activities.CreatePost;
import csedu.homeclick.androidhomeclick.activities.Profile;
import csedu.homeclick.androidhomeclick.activities.UserSignIn;
import csedu.homeclick.androidhomeclick.connector.UserService;

public class BottomNavBarHandler {
    private static BottomNavBarHandler bottomNavBarHandler;
    private static BottomNavigationView bottomNavigationView;
    private static int selectedItem;
    private static UserService userService;

    public static BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    private BottomNavBarHandler(View view, int selectIt) {
//        Log.i("bottom_view", view.toString());
//        Log.i("bottom_nav", (view.findViewById(R.id.bottom_navigation_bar)).toString());
        bottomNavigationView = (BottomNavigationView) view;
        selectedItem = selectIt;
        bottomNavigationView.setSelectedItemId(selectedItem);
        userService = new UserService();
    }

    public static void setInstance(View view, int selectIt) {
//        if(bottomNavBarHandler == null) {
        bottomNavBarHandler = new BottomNavBarHandler(view, selectIt);
//        }
    }

    public static void handle(final Activity activity) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(activity.getApplicationContext(), "home selected", Toast.LENGTH_SHORT).show();
                        activity.startActivity(new Intent(activity.getApplicationContext(), AdFeed.class));
                        break;
                    case R.id.create:
                        Toast.makeText(activity.getApplicationContext(), "add selected", Toast.LENGTH_SHORT).show();
                        Log.i("create", "ekhane ashchi");
                        if(userService.isSignedIn()) {
                            Log.i("create", "if er bhitore dhukena");
                            activity.startActivity(new Intent(activity.getApplicationContext(),CreatePost.class));
                        } else {
                            activity.startActivity(new Intent(activity.getApplicationContext(), UserSignIn.class));
                        }

                        break;
                    case R.id.account:
                        Toast.makeText(activity.getApplicationContext(), "account selected", Toast.LENGTH_SHORT).show();

                        if(userService.isSignedIn()) {
                            activity.startActivity(new Intent(activity.getApplicationContext(), Profile.class));
                        } else {
                            Log.i("signin", "is not signed in");
                            activity.startActivity(new Intent(activity.getApplicationContext(), UserSignIn.class));
                        }

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(activity.getApplicationContext(), "home reselected", Toast.LENGTH_SHORT).show();
                        activity.startActivity(new Intent(activity.getApplicationContext(), AdFeed.class));
                        break;
                    case R.id.create:
                        Toast.makeText(activity.getApplicationContext(), "add reselected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.account:
                        Toast.makeText(activity.getApplicationContext(), "account reselected", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public static void handle(final Context context) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(context, "home selected", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, AdFeed.class));
                        break;
                    case R.id.create:
                        Toast.makeText(context, "add selected", Toast.LENGTH_SHORT).show();
                        Log.i("create", "ekhane ashchi");
                        if(userService.isSignedIn()) {
                            Log.i("create", "if er bhitore dhukena");
                            context.startActivity(new Intent(context,CreatePost.class));
                        } else {
                            context.startActivity(new Intent(context, UserSignIn.class));
                        }

                        break;
                    case R.id.account:
                        Toast.makeText(context, "account selected", Toast.LENGTH_SHORT).show();

                        if(userService.isSignedIn()) {
                            context.startActivity(new Intent(context, Profile.class));
                        } else {
                            Log.i("signin", "is not signed in");
                            context.startActivity(new Intent(context, UserSignIn.class));
                        }

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(context, "home reselected", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.create:
                        Toast.makeText(context, "add reselected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.account:
                        Toast.makeText(context, "account reselected", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
