package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;

public class Data_Policy extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_policy);
        BottomNavBarHandler.setInstance(findViewById(R.id.privacy_bottom_nav_bar),R.id.Privacy);
        BottomNavBarHandler.handle(this);

    }


}