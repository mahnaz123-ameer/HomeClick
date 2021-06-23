package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.connector.AdvertisementService;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.recyclerviewadapters.AdvertisementRecyclerViewAdapter;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.navigator.TopAppBarHandler;
import csedu.homeclick.androidhomeclick.structure.Advertisement;

//TODO: pagination implementation

public class AdFeed extends AppCompatActivity implements AdvertisementRecyclerViewAdapter.OnAdCardClickListener {
    private RecyclerView adRecView;
    private List<Advertisement> adArrayList = new ArrayList<>();
    private final AdvertisementRecyclerViewAdapter adRecViewAdapter = new AdvertisementRecyclerViewAdapter();
    private AdvertisementService adService;

    private Toolbar toolbar;
    private UserService userService;

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

    @Override
    protected void onResume() {
        super.onResume();
        if(userService.isSignedIn()) {
            toolbar.getMenu().getItem(2).setTitle("Sign out");
        } else {
            toolbar.getMenu().getItem(2).setTitle("Sign in");
        }
    }

    private void getAdCards() {

        adService.fetchAdvertisements(new AdInterface.OnAdsFetchedListener<List<Advertisement>>() {
            @Override
            public void OnAdsFetchedListener(List<Advertisement> ads) {
                adArrayList = ads;

                adRecViewAdapter.setAdvertisementArrayList(adArrayList);
                adRecViewAdapter.setAdCardListener(AdFeed.this::onAdClick);
                adRecViewAdapter.notifyDataSetChanged();
            }
        });

        adRecView.setAdapter(adRecViewAdapter);
        adRecView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void bindWidgets() {
        userService = new UserService();
        adService = new AdvertisementService();
        adRecView = findViewById(R.id.adRecView);

        BottomNavBarHandler.setInstance(findViewById(R.id.ad_feed_bottom_bar), R.id.home);
        BottomNavBarHandler.handle(this);

        toolbar = findViewById(R.id.app_toolbaar);
        TopAppBarHandler.getInstance(toolbar, this).handle();
    }

    @Override
    public void onAdClick(int position) {
        final Advertisement clickedAdvert = adArrayList.get(position);
        String adID = clickedAdvert.getAdvertisementID();

//        Toast.makeText(this, adID + " clicked ad poster = " + clickedAdvert.getAdvertiserName(), Toast.LENGTH_SHORT).show();
        Intent targetIntent = new Intent(getApplicationContext(), ShowAdvertisementDetails.class);

        targetIntent.putExtra("Ad", clickedAdvert);
        startActivity(targetIntent);
    }
}

