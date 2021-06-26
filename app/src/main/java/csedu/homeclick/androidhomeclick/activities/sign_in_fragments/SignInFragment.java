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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.structure.User;


public class SignInFragment extends Fragment implements View.OnClickListener {
    private Button sendLink;
    private TextInputEditText editEmail;
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
        Boolean allOkay = true;
        String  email = editEmail.getText().toString().trim();


        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter valid email");
            allOkay = false;
        }
        return allOkay;



    }
}