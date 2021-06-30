package csedu.homeclick.androidhomeclick.activities;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.databinding.ActivityMapViewBinding;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.structure.Advertisement;

public class MapView extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, View.OnClickListener, GoogleMap.OnMapClickListener,
            View.OnLongClickListener{
    private static final String TAG = "MapView";
    private static final int REQUEST_CODE = 10;
    private GoogleMap mMap;
    private ActivityMapViewBinding binding;

    private List<Advertisement> adListForMarker = new ArrayList<>();
    private List<AdMarker> adMarkerPair = new ArrayList<>();
    private CardView adCard;
    private TextView bedroom, bathroom, gas, payment, fullAdd, areaName, adType;
    private Advertisement clickedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindWidgets();
        extractExtras();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    private void extractExtras() {
        Bundle extras = getIntent().getExtras();
        Advertisement[] arrayAd = (Advertisement[]) extras.get("adArrayList");

        for (int a = 0; a < arrayAd.length; a++) {
            this.adListForMarker.add(arrayAd[a]);
        }

        Log.i(TAG, "" + arrayAd.length);

    }

    private void bindWidgets() {
        bedroom = findViewById(R.id.card_bedroom);
        bathroom = findViewById(R.id.card_bathroom);
        gas = findViewById(R.id.card_gas);
        payment = findViewById(R.id.card_payment);
        adType = findViewById(R.id.card_ad_type);
        fullAdd = findViewById(R.id.card_full_address);
        areaName = findViewById(R.id.card_area_name);


        adCard = findViewById(R.id.map_view_card_details);
        BottomNavBarHandler.setInstance(findViewById(R.id.map_view_bottom_nav_bar), R.id.home);
        BottomNavBarHandler.handle(this);

        adCard.setOnClickListener(this);
        adCard.setOnLongClickListener(this);
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        this.mMap = googleMap;
        setMarkers();
    }

    private void setMarkers() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        } else {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }


            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.getUiSettings().setAllGesturesEnabled(true);

            int markerCount = 0;
            for (Advertisement ad : adListForMarker) {
                LatLng current = new LatLng(ad.getLatitude(), ad.getLongitude());

                int title = markerCount + 1;
                adMarkerPair.add(new AdMarker(new MarkerOptions().position(current).title(""+title), ad));

                Log.i(TAG, "count:" + markerCount + " " + adMarkerPair.get(markerCount).getMarker().getPosition().latitude );

                mMap.addMarker(adMarkerPair.get(markerCount).getMarker());
                markerCount++;
            }
            mMap.setOnMarkerClickListener(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        setMarkers();
                    }
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
        int position;
        String replacedId = marker.getId().replace("m", "0");
        position = Integer.parseInt(replacedId);
        this.clickedAd = adMarkerPair.get(position).getAd();

        fullAdd.setText(clickedAd.getFullAddress());
        areaName.setText(clickedAd.getAreaName());
        bedroom.setText(Integer.toString( clickedAd.getNumberOfBedrooms() ));
        bathroom.setText(Integer.toString( clickedAd.getNumberOfBathrooms() ));
        if(clickedAd.getGasAvailability()) {
            gas.setText(R.string._available);
        } else {
            gas.setText(R.string.not_available);
        }
        payment.setText(Integer.toString( clickedAd.getPaymentAmount() ));
        adType.setText(clickedAd.getAdType());

        adCard.setVisibility(View.VISIBLE);



        Log.i(TAG, "marker pos " + position);

        return false;
    }

    @Override
    public void onClick(View v) {
        adCard.setVisibility(View.GONE);

    }

    @Override
    public void onMapClick(@NonNull @NotNull LatLng latLng) {
        Log.i(TAG, "map clicked " + latLng.latitude + " " + latLng.longitude);
        adCard.setVisibility(View.GONE);
    }

    @Override
    public boolean onLongClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ShowAdvertisementDetails.class);
        intent.putExtra("Ad", clickedAd);
        startActivity(intent);
        return false;
    }


    private class AdMarker {
        MarkerOptions marker;
        Advertisement ad;

        public AdMarker(MarkerOptions marker, Advertisement ad) {
            this.marker = marker;
            this.ad = ad;
        }

        public MarkerOptions getMarker() {
            return marker;
        }

        public void setMarker(MarkerOptions marker) {
            this.marker = marker;
        }

        public Advertisement getAd() {
            return ad;
        }

        public void setAd(Advertisement ad) {
            this.ad = ad;
        }

        public void setClickListener(Marker m) {

        }
    }
}