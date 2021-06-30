package csedu.homeclick.androidhomeclick.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private static final int REQUEST_CODE = 4;
    private static final String TAG = "MapsActivity" ;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button sendLocation;
    private MarkerOptions selfMarker;

    private Location selfLocation;
    private LatLng selfLatLng;

    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSelfLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }




        sendLocation = findViewById(R.id.send_location_map);
        sendLocation.setOnClickListener(this);
    }

    private void setSelfLocation() {
        double latitude = (double) getIntent().getExtras().get("latitude");
        double longitude = (double) getIntent().getExtras().get("longitude");
        this.latitude = latitude;
        this.longitude = longitude;
        this.selfLocation = new Location("gps");
        this.selfLocation.setLatitude(latitude);
        this.selfLocation.setLongitude(longitude);
        this.selfLatLng = new LatLng(latitude, longitude);

        Log.i(TAG, "" + latitude);
        Log.i(TAG, "" + longitude);
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        mMap = googleMap;
        setInitialMarker();

    }

    private void setInitialMarker() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        } else {
            selfMarker = new MarkerOptions().position(selfLatLng).draggable(true).title("Me");
            mMap.addMarker(selfMarker);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(selfLatLng));
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(@NonNull @NotNull Marker marker) {

                }

                @Override
                public void onMarkerDrag(@NonNull @NotNull Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(@NonNull @NotNull Marker marker) {
                    double lat = marker.getPosition().latitude;
                    double lon = marker.getPosition().longitude;
                    MapsActivity.this.latitude = lat;
                    MapsActivity.this.longitude = lon;
                    Log.i("pos", "lat " + lat + " lon " + lon );
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("longitude", MapsActivity.this.longitude);
        intent.putExtra("latitude", MapsActivity.this.latitude);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE) {
            if(grantResults.length > 0) {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                        setInitialMarker();
                    }
                }
            }
        }
    }
}
