package csedu.homeclick.androidhomeclick.activities.createpost;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.sign_in_fragments.SignUpFragment;


public class CreateSellPostFragment extends Fragment {

    public CreateSellPostFragment() {
        // Required empty public constructor
    }


    public static CreateSellPostFragment newInstance() {
        CreateSellPostFragment fragment = new CreateSellPostFragment();
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
        return inflater.inflate(R.layout.fragment_create_sell_post, container, false);
    }
}