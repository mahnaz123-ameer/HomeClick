package csedu.homeclick.androidhomeclick.activities.profile_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import csedu.homeclick.androidhomeclick.R;


public class MySavedAdsFragment extends Fragment {


    public MySavedAdsFragment() {
        // Required empty public constructor
    }

    public static MySavedAdsFragment newInstance(String param1, String param2) {
        MySavedAdsFragment fragment = new MySavedAdsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_saved_ads, container, false);
    }
}