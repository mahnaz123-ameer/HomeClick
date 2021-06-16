package csedu.homeclick.androidhomeclick.activities.create_post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import csedu.homeclick.androidhomeclick.R;


public class CreateSalePostFragment extends Fragment {

    public CreateSalePostFragment() {
        // Required empty public constructor
    }


    public static CreateSalePostFragment newInstance() {
        CreateSalePostFragment fragment = new CreateSalePostFragment();
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
        return inflater.inflate(R.layout.fragment_create_sale_post, container, false);
    }
}