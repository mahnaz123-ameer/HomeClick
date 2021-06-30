package csedu.homeclick.androidhomeclick.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;


public class Terms_Of_Use extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);
        BottomNavBarHandler.setInstance(findViewById(R.id.terms_bottom_nav_bar),R.id.Terms_Of_Use);
        BottomNavBarHandler.handle(this);
    }

}