package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.connector.AdvertisementService;
import csedu.homeclick.androidhomeclick.connector.UserInterface;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.navigator.TopAppBarHandler;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.RentAdvertisement;
import csedu.homeclick.androidhomeclick.structure.User;

public class ShowAdvertisementDetails extends AppCompatActivity implements Serializable, View.OnClickListener, AdInterface.OnParticularAdFetchedListener, UserInterface.OnUserInfoListener {
    public static final String TAG = "ShowAdDetails";

    private UserService userService;
    private AdvertisementService adService;

    private RentAdvertisement ad;
    final RentAdvertisement[] advertisement = new RentAdvertisement[1];


    private TextView areaNameTV, fullAddressTV, adTypeTV, bedroomTV, bathroomTV, balconyTV;
    private TextView gasTV, elevatorTV, generatorTV, garageTV, securityTV;
    private TextView floorTV, floorSpaceTV, paymentTV;
    private TextView tenantTypeTV, utilityTV, moveInTV;
    private TextView advertNameTV;

    private CardView callCard;

    private ImageButton edit, delete, bookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_advertisement_details);
        
        bindWidgets();
        adService.getRentAd(this, ((Advertisement)getIntent().getExtras().get("Ad")).getAdvertisementID());


        edit.setOnClickListener(this::onClick);
        delete.setOnClickListener(this::onClick);
        bookmark.setOnClickListener(this::onClick);
        callCard.setOnClickListener(this::onClick);
    }

    @SuppressLint("SetTextI18n")
    private void attachValues() {
        if(userService.isSignedIn()) {
            if(userService.getUserUID().equals(((Advertisement)getIntent().getExtras().get("Ad")).getAdvertiserUID())) {
                bookmark.setEnabled(false);
                bookmark.setVisibility(View.GONE);

                edit.setVisibility(View.VISIBLE);
                edit.setEnabled(true);

                delete.setEnabled(true);
                delete.setVisibility(View.VISIBLE);
            } else {
                bookmark.setEnabled(true);
                bookmark.setVisibility(View.VISIBLE);

                edit.setVisibility(View.GONE);
                edit.setEnabled(false);

                delete.setEnabled(false);
                delete.setVisibility(View.GONE);
            }
        }
        areaNameTV.setText(ad.getAreaName());
        fullAddressTV.setText(ad.getFullAddress());
        adTypeTV.setText(ad.getAdType());

        bedroomTV.setText(Integer.toString(ad.getNumberOfBedrooms()));
        bathroomTV.setText(Integer.toString(ad.getNumberOfBathrooms()));
        balconyTV.setText(Integer.toString(ad.getNumberOfBalconies()));

        String gas, elevator, generator, garage, security;
        if(ad.getGasAvailability()) gas = getString( R.string.available );
        else gas = getString( R.string.not_available );

        if(ad.getElevator()) elevator = getString( R.string.available );
        else elevator = getString( R.string.not_available );

        if(ad.getGenerator()) generator = getString( R.string.available );
        else generator = getString( R.string.not_available );

        if(ad.getGarageSpace()) garage = getString( R.string.available );
        else garage = getString( R.string.not_available );

        if(ad.getSecurityGuard()) security = getString( R.string.available );
        else security = getString( R.string.not_available );

        gasTV.setText(gas);
        elevatorTV.setText(elevator);
        generatorTV.setText(generator);
        garageTV.setText(garage);
        securityTV.setText(security);

        floorTV.setText(Integer.toString(ad.getFloor()));
        floorSpaceTV.setText(Integer.toString(ad.getFloorSpace()) + " SQFT");

        paymentTV.setText(Integer.toString(ad.getPaymentAmount()) + " BDT");
        utilityTV.setText(Integer.toString(ad.getUtilityCharge()) + " BDT");

        moveInTV.setText(ad.getAvailableFrom().toString());
        tenantTypeTV.setText(ad.getTenantType());

        advertNameTV.setText(ad.getAdvertiserName());
    }

    private void updateInfo(final RentAdvertisement advert) {
        ad = advert;
        userService.findUserInfo(this, ((Advertisement)getIntent().getExtras().get("Ad")).getAdvertiserUID());
    }

    private void bindWidgets() {
        BottomNavBarHandler.setInstance(findViewById(R.id.show_ad_bottom_nav_bar), R.id.home);
        BottomNavBarHandler.handle(this);
        TopAppBarHandler.getInstance(findViewById(R.id.app_toolbaar), this).handle();

        areaNameTV = findViewById(R.id.home_area_name);
        fullAddressTV = findViewById(R.id.home_full_address);
        adTypeTV = findViewById(R.id.home_ad_type);

        bedroomTV = findViewById(R.id.num_of_bedrooms);
        bathroomTV = findViewById(R.id.num_of_bathrooms);
        balconyTV = findViewById(R.id.num_of_balconies);

        gasTV = findViewById(R.id.home_gas_availability);
        elevatorTV = findViewById(R.id.home_elevator);
        generatorTV = findViewById(R.id.home_generator);
        garageTV = findViewById(R.id.home_garage_space);
        securityTV = findViewById(R.id.home_security_guard);

        floorTV = findViewById(R.id.home_floor);
        floorSpaceTV = findViewById(R.id.home_floor_space);
        paymentTV = findViewById(R.id.home_payment);
        tenantTypeTV = findViewById(R.id.home_tenant_type);
        utilityTV = findViewById(R.id.home_utility);
        moveInTV = findViewById(R.id.move_in_date);

        advertNameTV = findViewById(R.id.advertiser_name);
        callCard = findViewById(R.id.call_card);

        edit = findViewById(R.id.edit_ad);
        delete = findViewById(R.id.delete_ad);
        bookmark = findViewById(R.id.bookmark_ad);

        userService = new UserService();
        adService = new AdvertisementService();
        Advertisement received = (Advertisement) getIntent().getExtras().get("Ad");

        adService.getRentAd(new AdInterface.OnParticularAdFetchedListener<RentAdvertisement>() {
            @Override
            public void OnParticularAdFetched(RentAdvertisement advert) {
                advertisement[0] = advert;

            }
        }, received.getAdvertisementID());



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_ad:
                Toast.makeText(this.getApplicationContext(), "Edit options not programmed yet.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete_ad:
                if(userService.isSignedIn() && userService.getUserUID().equals( ad.getAdvertiserUID() )) {
                    final String adId = ad.getAdvertisementID();
                    final int totalToDelete = ad.getNumberOfImages();
                    final int[] alreadyDeleted = new int[1];
                    alreadyDeleted[0] = 0;
                    adService.deletePhotoFolder(adId, totalToDelete, new AdInterface.OnPhotoFolderDeletedListener<Boolean>() {
                        @Override
                        public void OnPhotoFolderDeleted(Boolean deleted, String error) {
                            startActivity(new Intent(ShowAdvertisementDetails.this.getApplicationContext(), AdFeed.class));
                            if(deleted) {
                                alreadyDeleted[0]++;
                                Log.i(TAG, "number of the photo deleted = " + error);
                                if(alreadyDeleted[0] == totalToDelete) {
                                    adService.deleteAd(adId, new AdInterface.OnAdDeletedListener<Boolean>() {
                                        @Override
                                        public void OnAdDeleted(Boolean deleted, String error) {
                                            if (deleted) {

                                            } else {
                                                Toast.makeText(ShowAdvertisementDetails.this, error, Toast.LENGTH_SHORT).show();
                                                Log.i(TAG, error);
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(ShowAdvertisementDetails.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;

            case R.id.bookmark_ad:
                Toast.makeText(this.getApplicationContext(), "Bookmarks not programmed yet.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.call_card:
                final String phone_number = ad.getAdvertiserPhoneNumber();

                if(phone_number == null) return;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Log.i("show ad","tel:" + (String) phone_number);
                    intent.setData(Uri.parse("tel:" + (String)phone_number));
                    startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void OnParticularAdFetched(Object advert) {
        updateInfo((RentAdvertisement)advert);
    }

    @Override
    public void OnUserInfoFound(Object data) {
        User newInfo = (User) data;
        ad.setAdvertiserName(newInfo.getName());
        Log.i("aaaaaaaaaaaaa", ad.getAdvertiserName());
        ad.setAdvertiserPhoneNumber(newInfo.getPhoneNumber());
        attachValues();
    }
}