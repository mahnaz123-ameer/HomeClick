package csedu.homeclick.androidhomeclick.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;


import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;



import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.create_post.CreatePostFragmentAdapter;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.navigator.TabLayoutHandler;
import csedu.homeclick.androidhomeclick.navigator.TopAppBarHandler;
import csedu.homeclick.androidhomeclick.structure.Advertisement;

public class CreatePost extends AppCompatActivity {
    private static final String TAG = "CreatePost";

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CreatePostFragmentAdapter adapter;
    private FragmentManager fm;
    private Toolbar toolbar;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double longitude, latitude;

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        bindWidgets();
        getPermissionAndLocation();
    }

    private void getPermissionAndLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            System.out.println("In here, permission granted, requesting location updates");
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                this.latitude = location.getLatitude();
                this.longitude = location.getLongitude();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                if(location != null) {
                    this.latitude = location.getLatitude();
                    this.longitude = location.getLongitude();
                }


            }
        }
    }


    private void bindWidgets() {
        tabLayout = findViewById(R.id.create_post_TabLayout);
        viewPager = findViewById(R.id.create_post_view_pager);
        fm = getSupportFragmentManager();
        adapter = new CreatePostFragmentAdapter(fm, getLifecycle());
        viewPager.setAdapter(adapter);


        Advertisement received = null;
        if(getIntent().getExtras() != null) {
            received = (Advertisement) getIntent().getExtras().get("Ad");
        }

        int pageIndex = 0;

        if(received != null) {
            if (received.getAdType().equals("Sale")) {
                Log.i(TAG, "in page index 1");
                pageIndex = 1;
            }
        }

        TabLayoutHandler.setInstance(tabLayout, viewPager, pageIndex);
        TabLayoutHandler.handle(this);

        BottomNavBarHandler.setInstance(findViewById(R.id.create_post_bottom_nav_bar),R.id.create);
        BottomNavBarHandler.handle(this);

        toolbar = findViewById(R.id.app_toolbaar);
        TopAppBarHandler.getInstance(toolbar, this).handle();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                CreatePost.this.latitude = location.getLatitude();
                CreatePost.this.longitude = location.getLongitude();

                Log.i(TAG, "latitude = " + latitude);
                Log.i(TAG, "longitude = " + longitude);

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Log.i(TAG, provider);
                goToEnableProvider();
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                //do nothing
            }
        };
    }

    private void goToEnableProvider() {
        Log.i(TAG, "in go to enable provider");
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
    }
}