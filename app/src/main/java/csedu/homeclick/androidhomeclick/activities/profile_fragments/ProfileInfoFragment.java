package csedu.homeclick.androidhomeclick.activities.profile_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.UserInterface;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.structure.User;


public class ProfileInfoFragment extends Fragment implements View.OnClickListener{
    private TextView profile_name, profile_email, profile_phone;
    private EditText name_edit, phone_edit;
    private ImageButton edit, save;

    private UserService userService;

    public ProfileInfoFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_profile_info, container, false);

        bindWidgets(view);

        edit.setOnClickListener(this);
        save.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        setInformation();
    }

    private void setInformation() {
        userService.findUserInfo(new UserInterface.OnUserInfoListener<User>() {
            @Override
            public void OnUserInfoFound(User data) {
                profile_name.setText(data.getName());
                profile_email.setText(data.getEmailAddress());
                profile_phone.setText(data.getPhoneNumber());
            }
        }, userService.getUserUID());
    }

    private void bindWidgets(View view) {
        userService = new UserService();

        profile_name = view.findViewById(R.id.profile_name);
        profile_email = view.findViewById(R.id.profile_email);
        profile_phone = view.findViewById(R.id.profile_phone);

        name_edit = view.findViewById(R.id.profile_name_edit);
        phone_edit = view.findViewById(R.id.profile_phone_edit);

        edit = view.findViewById(R.id.edit_info);
        save = view.findViewById(R.id.save_info);

        edit.setVisibility(View.VISIBLE);
        edit.setEnabled(true);

        save.setVisibility(View.GONE);
        save.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_info:
                activateEdit();

                break;
            case R.id.save_info:
                saveEdit(v);

                break;
            default:
                break;
        }
    }

    private void saveEdit(View view) {
        edit.setEnabled(true);
        edit.setVisibility(View.VISIBLE);

        save.setVisibility(View.GONE);
        save.setEnabled(false);

        String previous_name = profile_name.getText().toString();
        String previous_phone = profile_phone.getText().toString();

        String edited_name = name_edit.getText().toString();
        String edited_phone = phone_edit.getText().toString();

        profile_phone.setVisibility(View.VISIBLE);
        profile_name.setVisibility(View.VISIBLE);

        phone_edit.setVisibility(View.GONE);
        phone_edit.setEnabled(false);

        name_edit.setEnabled(false);
        name_edit.setVisibility(View.GONE);

        if(previous_name.equals(edited_name) && previous_phone.equals(edited_phone)) {
            Toast.makeText(view.getContext().getApplicationContext(), "No changes were made", Toast.LENGTH_SHORT).show();
        } else {
            User updatedUser = new User(edited_name, profile_email.getText().toString().trim(), edited_phone, userService.getUserUID());
            updateInfo(view, updatedUser);

        }
    }

    private void updateInfo(final View view, final User updatedUser) {
        final TextView finalName = profile_name;
        final TextView finalPhone = profile_phone;

        userService.updateUserInfo(new UserInterface.OnUserInfoUpdateListener<User>() {
            @Override
            public void OnUserInfoUpdated() {
                profile_name.setText(updatedUser.getName());
                profile_phone.setText(updatedUser.getPhoneNumber());
            }

            @Override
            public void OnUserInfoUpdateFailed() {
                Toast.makeText(view.getContext().getApplicationContext(), "User information could not be updated.", Toast.LENGTH_SHORT).show();
            }
        }, updatedUser);
    }



    private void activateEdit() {
        edit.setEnabled(false);
        edit.setVisibility(View.GONE);

        save.setVisibility(View.VISIBLE);
        save.setEnabled(true);

        profile_phone.setVisibility(View.GONE);
        profile_name.setVisibility(View.GONE);

        phone_edit.setVisibility(View.VISIBLE);
        phone_edit.setEnabled(true);
        phone_edit.setText(profile_phone.getText());

        name_edit.setEnabled(true);
        name_edit.setVisibility(View.VISIBLE);
        name_edit.setText(profile_name.getText());
    }


}