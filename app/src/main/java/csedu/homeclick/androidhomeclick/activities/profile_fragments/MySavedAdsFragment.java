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


public class MySavedAdsFragment extends Fragment implements AdvertisementRecyclerViewAdapter.OnAdCardClickListener{
    private UserService userService;
    private AdvertisementService adService;
    private RecyclerView myBookmarkedAdsRecView;

    private List<Advertisement> adArrayList = new ArrayList<>();
    private final AdvertisementRecyclerViewAdapter adRecViewAdapter = new AdvertisementRecyclerViewAdapter();

    public MySavedAdsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_saved_ads, container, false);

        bindWidgets(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMyBookmarkedAds();
    }

    private void getMyBookmarkedAds() {
        adService.fetchBookmarkedAds((ads, error) -> {
            adArrayList = ads;

            adRecViewAdapter.setAdvertisementArrayList(adArrayList);
            adRecViewAdapter.setAdCardListener(MySavedAdsFragment.this);
            adRecViewAdapter.notifyDataSetChanged();
        });

        myBookmarkedAdsRecView.setAdapter(adRecViewAdapter);
        myBookmarkedAdsRecView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    private void bindWidgets(View view) {
        userService = new UserService();
        adService = new AdvertisementService();

        myBookmarkedAdsRecView = view.findViewById(R.id.my_saved_ads_rec_view);
    }

    @Override
    public void onAdClick(int position) {
        final Advertisement clickedAdvert = adArrayList.get(position);
        String adID = clickedAdvert.getAdvertisementID();

        Intent targetIntent = new Intent(requireContext().getApplicationContext(), ShowAdvertisementDetails.class);

        targetIntent.putExtra("Ad", clickedAdvert);
        startActivity(targetIntent);
    }
}