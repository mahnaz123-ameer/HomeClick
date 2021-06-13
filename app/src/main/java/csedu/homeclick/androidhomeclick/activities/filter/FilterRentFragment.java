package csedu.homeclick.androidhomeclick.activities.filter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import csedu.homeclick.androidhomeclick.R;


public class FilterRentFragment extends Fragment {

    public FilterRentFragment() {
        // Required empty public constructor
    }


    public static FilterRentFragment newInstance() {
        FilterRentFragment fragment = new FilterRentFragment();
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
        return inflater.inflate(R.layout.fragment_filter_rent, container, false);
    }
}