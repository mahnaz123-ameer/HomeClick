package csedu.homeclick.androidhomeclick.activities.sign_in_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import csedu.homeclick.androidhomeclick.R;


public class SignInFragment extends Fragment {

    private Button send_link_login;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        send_link_login = view.findViewById(R.id.sendSignInLink);
        send_link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Sign In Link send to email successfully", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }
}