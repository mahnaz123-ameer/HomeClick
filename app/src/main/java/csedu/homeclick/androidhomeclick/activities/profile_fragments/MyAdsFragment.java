package csedu.homeclick.androidhomeclick.activities.profile_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.ShowAdvertisementDetails;
import csedu.homeclick.androidhomeclick.connector.AdvertisementService;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.recyclerviewadapters.AdvertisementRecyclerViewAdapter;
import csedu.homeclick.androidhomeclick.structure.Advertisement;

public class MyAdsFragment extends Fragment implements AdvertisementRecyclerViewAdapter.OnAdCardClickListener{
    private UserService userService;
    private AdvertisementService adService;
    private RecyclerView myAdsRecView;

    private List<Advertisement> adArrayList = new ArrayList<>();
    private final AdvertisementRecyclerViewAdapter adRecViewAdapter = new AdvertisementRecyclerViewAdapter();

    public MyAdsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_ads, container, false);

        bindWidgets(view);

        return view;
    }

    private void bindWidgets(View view) {
        //services
        userService = new UserService();
        adService = new AdvertisementService();

        myAdsRecView = view.findViewById(R.id.my_ads_rec_view);
    }

    @Override
    public void onStart() {
        super.onStart();

        getMyAds();
    }

    private void getMyAds() {

        adService.fetchMyAds(ads -> {
            adArrayList = ads;

            adRecViewAdapter.setAdvertisementArrayList(adArrayList);
            adRecViewAdapter.setAdCardListener(MyAdsFragment.this);
            adRecViewAdapter.notifyDataSetChanged();
        });

        myAdsRecView.setAdapter(adRecViewAdapter);
        myAdsRecView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onAdClick(int position) {
        final Advertisement clickedAdvert = adArrayList.get(position);
        String adID = clickedAdvert.getAdvertisementID();

        Intent targetIntent = new Intent(this.requireContext().getApplicationContext(), ShowAdvertisementDetails.class);

        targetIntent.putExtra("Ad", clickedAdvert);
        startActivity(targetIntent);
    }
}