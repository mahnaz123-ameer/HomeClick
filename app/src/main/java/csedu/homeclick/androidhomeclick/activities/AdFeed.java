package csedu.homeclick.androidhomeclick.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.create_post.CreateRentPostFragment;
import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.connector.AdvertisementService;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.recyclerviewadapters.AdvertisementRecyclerViewAdapter;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.navigator.TopAppBarHandler;
import csedu.homeclick.androidhomeclick.structure.Advertisement;

//TODO: pagination implementation

public class AdFeed extends AppCompatActivity implements AdvertisementRecyclerViewAdapter.OnAdCardClickListener {
    public static final String TAG = "AdFeed";

    private RecyclerView adRecView;
    private final List<Advertisement> adArrayList = new ArrayList<>();
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
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(userService.isSignedIn()) {
            toolbar.getMenu().getItem(2).setTitle("Sign out");
        } else {
            toolbar.getMenu().getItem(2).setTitle("Sign in");
        }

        handleAdLoading();
    }

    private void handleAdLoading() {
        adService.refreshAds();

        getAdCards();

        adRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(1)) {
                    getAdCards();
                }
            }
        });
    }

    private void getAdCards() {
        int prevSize = adArrayList.size();

        adService.fetchAdvertisements(new AdInterface.OnAdsFetchedListener<List<Advertisement>>() {
            @Override
            public void OnAdsFetchedListener(List<Advertisement> ads) {
                adArrayList.addAll(ads);

                Log.i(TAG, "ad array list size = " + adArrayList.size());

                LinkedHashSet<Advertisement> hashSet = new LinkedHashSet<>(adArrayList);
                adArrayList.clear();
                adArrayList.addAll(hashSet);

                int newSize = adArrayList.size();

                if(prevSize != newSize) {
                    adRecViewAdapter.setAdvertisementArrayList(adArrayList);
                    adRecViewAdapter.setAdCardListener(AdFeed.this);
                    adRecViewAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void OnAdFetchingFailedListener(String error) {
                Log.i(TAG, error);
            }
        } );

    }

    private void bindWidgets() {
        userService = new UserService();
        adService = new AdvertisementService();
        adRecView = findViewById(R.id.adRecView);

        adRecView.setLayoutManager(new LinearLayoutManager(this));
        adRecView.setAdapter(adRecViewAdapter);

        BottomNavBarHandler.setInstance(findViewById(R.id.ad_feed_bottom_bar), R.id.home);
        BottomNavBarHandler.handle(this);

        toolbar = findViewById(R.id.app_toolbaar);
        TopAppBarHandler.getInstance(toolbar, this).handle();
    }

    @Override
    public void onAdClick(int position) {
        final Advertisement clickedAdvert = adArrayList.get(position);
//        String adID = clickedAdvert.getAdvertisementID();

//        Toast.makeText(this, adID + " clicked ad poster = " + clickedAdvert.getAdvertiserName(), Toast.LENGTH_SHORT).show();
        Intent targetIntent = new Intent(getApplicationContext(), ShowAdvertisementDetails.class);

        targetIntent.putExtra("Ad", clickedAdvert);
        startActivity(targetIntent);
    }
}

