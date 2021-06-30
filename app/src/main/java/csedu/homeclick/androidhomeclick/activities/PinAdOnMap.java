package csedu.homeclick.androidhomeclick.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.databinding.ActivityMapsBinding;
import csedu.homeclick.androidhomeclick.databinding.ActivityPinAdOnMapBinding;

public class PinAdOnMap extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final int REQUEST_CODE = 15;
    private static final String TAG = "PinAdOnMap" ;

    private GoogleMap mMap;
    private ActivityPinAdOnMapBinding binding;
    private Button goBack;

    private LatLng adPos;

    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPinAdOnMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        goBack = findViewById(R.id.go_back);
        goBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.go_back) {
            finish();
        }
    }

    private void openInMaps() {
        String uri = "http://maps.google.com/maps?daddr=" + adPos.latitude + "," + adPos.longitude + " (" + "Location of the house" + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;
        setMarker();
    }

    private void setMarker() {
        latitude = (double) getIntent().getExtras().get("latitude");
        longitude = (double) getIntent().getExtras().get("longitude");

        adPos = new LatLng(latitude, longitude);

        MarkerOptions adMarker = new MarkerOptions().position(adPos);
        mMap.addMarker(adMarker);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(adPos));
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setOnMarkerClickListener(this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setMarker();
            }
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
        Log.i(TAG, "on marker click");
        AlertDialog.Builder openMaps = new AlertDialog.Builder(this);

        openMaps
                .setMessage("Open in Google Maps?")
                .setPositiveButton("Yes", (dialog, which) -> openInMaps())
                .setNegativeButton("Cancel", (dialog, which) -> {
                    //do nothing
                });

        AlertDialog openMapsAlert = openMaps.create();
        openMapsAlert.show();
        return false;
    }
}