package csedu.homeclick.androidhomeclick.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class Terms_Of_Use extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);
        BottomNavBarHandler.setInstance(findViewById(R.id.terms_bottom_nav_bar),R.id.Terms_Of_Use);
        BottomNavBarHandler.handle(this);


    }

}