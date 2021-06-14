package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.connector.AdvertisementService;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.handler.AdvertisementRecyclerViewAdapter;
import csedu.homeclick.androidhomeclick.handler.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.handler.TopAppBarHandler;
import csedu.homeclick.androidhomeclick.structure.Advertisement;

public class AdFeed extends AppCompatActivity {
    private RecyclerView adRecView;
    private List<Advertisement> advertisementArrayList = new ArrayList<>();
    private AdvertisementRecyclerViewAdapter advertisementRecyclerViewAdapter = new AdvertisementRecyclerViewAdapter();
    private AdvertisementService advertisementService;

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

    @Override
    protected void onStart() {
        super.onStart();

        getAdCards();
    }

    private void getAdCards() {

        advertisementService.fetchAdvertisements(new AdInterface.OnAdsFetchedListener<List<Advertisement>>() {
            @Override
            public void OnAdsFetchedListener(List<Advertisement> ads) {
                advertisementArrayList = ads;

                advertisementRecyclerViewAdapter.setAdvertisementArrayList(advertisementArrayList);
                advertisementRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        adRecView.setAdapter(advertisementRecyclerViewAdapter);
        adRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void bindWidgets() {
        advertisementService = new AdvertisementService();
        adRecView = findViewById(R.id.adRecView);

        BottomNavBarHandler.setInstance(findViewById(R.id.bottom_navigation_bar), R.id.home);
        BottomNavBarHandler.handle(this);

        toolbar = findViewById(R.id.app_toolbaar);
        TopAppBarHandler.getInstance(toolbar, this).handle();

        userService = new UserService();
    }
}

