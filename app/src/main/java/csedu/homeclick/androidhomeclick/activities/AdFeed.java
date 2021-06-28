package csedu.homeclick.androidhomeclick.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.jetbrains.annotations.NotNull;

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
import csedu.homeclick.androidhomeclick.structure.FilterCriteria;


public class AdFeed extends AppCompatActivity implements AdvertisementRecyclerViewAdapter.OnAdCardClickListener ,
                            View.OnClickListener{
    private static final String TAG = "AdFeed";
    private static final String PACKAGE_NAME = "csedu.homeclick.androidhomeclick";
    private static final String CLASS_NAME = "csedu.homeclick.androidhomeclick.activities.MapView";


    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if(!recyclerView.canScrollVertically(1)) {
                if(receivedCriteria == null) {
                    getAdCards();
                } else {
                    getFilteredAdCards();
                }
            }
        }
    };

    private RecyclerView adRecView;
    private final List<Advertisement> adArrayList = new ArrayList<>();
    private final AdvertisementRecyclerViewAdapter adRecViewAdapter = new AdvertisementRecyclerViewAdapter();
    private AdvertisementService adService;

    private Toolbar toolbar;
    private UserService userService;

    private LinearProgressIndicator loadAds;
    private FilterCriteria receivedCriteria = null;

    private FloatingActionButton mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_feed);

        bindWidgets();

        adRecView.addOnScrollListener(onScrollListener);
        mapView.setOnClickListener(this);

        if(getIntent().getData() != null) {
            userService.completeSignIn(getIntent(), getApplicationContext());
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(userService.isSignedIn()) {
            toolbar.getMenu().getItem(2).setTitle("Sign out");
        } else {
            toolbar.getMenu().getItem(2).setTitle("Sign in");
        }

        if(getIntent().getExtras() != null) {
            receivedCriteria = (FilterCriteria) getIntent().getExtras().get("FilterCriteria");
        } else {
            receivedCriteria = null;
        }

        if(receivedCriteria == null) {
            handleRegularAdLoading();
        } else {
            handleFilteredAdLoading();
        }
    }

    private void handleFilteredAdLoading() {
        Log.i(TAG, "in handle filter ad loading");
        adArrayList.clear();
        adService.refreshAds();

        getFilteredAdCards();
    }

    private void getFilteredAdCards() {
        Log.i(TAG, "in get filtered ad cards");
        adRecView.removeOnScrollListener(onScrollListener);

        loadAds.setVisibility(View.VISIBLE);

        int prevSize = adArrayList.size();
        adService.fetchFilteredAds(new AdInterface.OnAdsFetchedListener<List<Advertisement>>() {
            @Override
            public void OnAdsFetchedListener(List<Advertisement> ads) {
                if(!ads.isEmpty())
                    adArrayList.addAll(ads);

                Log.i(TAG, "ad array list size without hashset = " + adArrayList.size());

                int newSize = adArrayList.size();

                if(prevSize != newSize) {
                    adRecViewAdapter.setAdvertisementArrayList(adArrayList);
                    adRecViewAdapter.setAdCardListener(AdFeed.this);
                    adRecViewAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(AdFeed.this, "No more ads to load.", Toast.LENGTH_SHORT).show();
                }

                AdFeed.this.adRecView.addOnScrollListener(onScrollListener);
                AdFeed.this.loadAds.setVisibility(View.GONE);
            }

            @Override
            public void OnAdFetchingFailedListener(String error) {
                Log.i(TAG, error);
            }
        }, receivedCriteria);

    }

    private void handleRegularAdLoading() {
        Log.i(TAG, "in handle regular ad loading");
        adArrayList.clear();
        adService.refreshAds();


        getAdCards();
    }

    private void getAdCards() {
        adRecView.removeOnScrollListener(onScrollListener);
        int prevSize = adArrayList.size();

        loadAds.setVisibility(View.VISIBLE);

        adService.fetchAdvertisements(new AdInterface.OnAdsFetchedListener<List<Advertisement>>() {
            @Override
            public void OnAdsFetchedListener(List<Advertisement> ads) {
                if(!ads.isEmpty())
                    adArrayList.addAll(ads);

                Log.i(TAG, "ad array list size without hashset = " + adArrayList.size());

                int newSize = adArrayList.size();

                if(prevSize != newSize) {
                    adRecViewAdapter.setAdvertisementArrayList(adArrayList);
                    adRecViewAdapter.setAdCardListener(AdFeed.this);
                    adRecViewAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(AdFeed.this, "No more ads to load.", Toast.LENGTH_SHORT).show();
                }

                AdFeed.this.adRecView.addOnScrollListener(onScrollListener);
                AdFeed.this.loadAds.setVisibility(View.GONE);
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
        mapView = findViewById(R.id.map_view);
        loadAds = findViewById(R.id.loading_ads);
        loadAds.setVisibility(View.GONE);

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

        Intent targetIntent = new Intent(getApplicationContext(), ShowAdvertisementDetails.class);

        targetIntent.putExtra("Ad", clickedAdvert);
        startActivity(targetIntent);
    }

    @Override
    public void onClick(View v) {
        Advertisement[] toSend = new Advertisement[adArrayList.size()];
        for(int adNo = 0; adNo < adArrayList.size(); adNo++) {
            toSend[adNo] = adArrayList.get(adNo);
        }
        Intent intent = new Intent(getApplicationContext(), MapView.class);
        intent.putExtra("adArrayList", toSend);
        startActivity(intent);
    }
}
