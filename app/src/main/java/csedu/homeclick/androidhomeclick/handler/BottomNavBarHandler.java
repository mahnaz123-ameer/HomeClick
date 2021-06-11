package csedu.homeclick.androidhomeclick.handler;

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
import csedu.homeclick.androidhomeclick.database.UserAuth;

public class BottomNavBarHandler {
    private static BottomNavBarHandler bottomNavBarHandler;
    private static BottomNavigationView bottomNavigationView;
    private static int selectedItem;
    private static UserService userService;

    public static BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    private BottomNavBarHandler(View view, int selectIt) {
        Log.i("bottom_nav", (view.findViewById(R.id.bottom_navigation_bar)).toString());
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_bar);
        selectedItem = selectIt;
        bottomNavigationView.setSelectedItemId(selectedItem);
        userService = new UserService();
    }

    public static void setInstance(View view, int selectIt) {
//        if(bottomNavBarHandler == null) {
        bottomNavBarHandler = new BottomNavBarHandler(view, selectIt);
//        }
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
