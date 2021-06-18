package csedu.homeclick.androidhomeclick.activities.sign_in_fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.UserService;


public class SignInFragment extends Fragment implements View.OnClickListener {
    private Button sendLink;
    private EditText editEmail;
    private UserService userService;

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

        bindWidgets(view);
        sendLink.setOnClickListener(this);

        return view;
    }

    private void bindWidgets(View view) {
        editEmail = view.findViewById(R.id.editEmail);
        sendLink = view.findViewById(R.id.sendSignInLink);

        userService = new UserService();
    }

    @Override
    public void onClick(View v) {
        if(checkData()) {
            String email = editEmail.getText().toString().trim();
            userService.signIn(email, v.getContext().getApplicationContext());
        }
    }

    private Boolean checkData() {
        String email;
        email = editEmail.getText().toString().trim();

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}