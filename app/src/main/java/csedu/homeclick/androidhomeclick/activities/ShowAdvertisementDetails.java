package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.connector.AdvertisementService;
import csedu.homeclick.androidhomeclick.connector.UserInterface;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.recyclerviewadapters.ImageRecyclerViewAdapter;
import csedu.homeclick.androidhomeclick.navigator.TopAppBarHandler;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.RentAdvertisement;
import csedu.homeclick.androidhomeclick.structure.SaleAdvertisement;
import csedu.homeclick.androidhomeclick.structure.User;

public class ShowAdvertisementDetails extends AppCompatActivity implements Serializable, View.OnClickListener,
        AdInterface.OnParticularAdFetchedListener, UserInterface.OnUserInfoListener,
        ImageRecyclerViewAdapter.OnPhotoClickListener, View.OnLongClickListener{
    public static final String TAG = "ShowAdDetails";

    private CoordinatorLayout coordinatorLayout;

    private UserService userService;
    private AdvertisementService adService;

    private RentAdvertisement rentAd;
    private SaleAdvertisement saleAd;
    final RentAdvertisement[] finalRentAd = new RentAdvertisement[1];
    final SaleAdvertisement[] finalSaleAd = new SaleAdvertisement[1];
    private final User advertiser = new User();


    private TextView areaNameTV, fullAddressTV, adTypeTV, bedroomTV, bathroomTV, balconyTV;
    private TextView gasTV, elevatorTV, generatorTV, garageTV, securityTV;
    private TextView floorTV, floorSpaceTV, paymentTV;
    private TextView tenantTypeTV, utilityTV, moveInTV;
    private TextView advertNameTV;

    private CardView callCard;

    private ImageButton edit, delete, bookmark, bookmarked;

    private  ExpandableTextView description;

    private ImageRecyclerViewAdapter adImagesVA;
    private RecyclerView imageRecView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_advertisement_details);

        Log.i(TAG, "in on create");
        
        bindWidgets();
        setClickListeners();
    }

    private void setClickListeners() {
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        callCard.setOnClickListener(this);
        adImagesVA.setOnPhotoClickListener(this);
        bookmarked.setOnClickListener(this);
        fullAddressTV.setOnLongClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void attachValues() {
        Log.i(TAG, "in attach values");

        Advertisement received = (Advertisement) getIntent().getExtras().get("Ad");

        if(userService.isSignedIn()) {
            if(userService.getUserUID().equals(received.getAdvertiserUID())) {
                Log.i(TAG, "my ad");
                bookmarked.setEnabled(false);
                bookmarked.setVisibility(View.GONE);

                bookmark.setEnabled(false);
                bookmark.setVisibility(View.GONE);

                edit.setVisibility(View.VISIBLE);
                edit.setEnabled(true);

                delete.setEnabled(true);
                delete.setVisibility(View.VISIBLE);

            } else if(adIsBookmarkedByUser(received)){
                Log.i(TAG, "ad i bookmarked");
                bookmarked.setEnabled(true);
                bookmarked.setVisibility(View.VISIBLE);

                bookmark.setEnabled(false);
                bookmark.setVisibility(View.GONE);

                edit.setVisibility(View.GONE);
                edit.setEnabled(false);

                delete.setEnabled(false);
                delete.setVisibility(View.GONE);
            } else {
                Log.i(TAG, "i haven't done anything with this yet");
                bookmark.setEnabled(true);
                bookmark.setVisibility(View.VISIBLE);

                bookmarked.setEnabled(false);
                bookmarked.setVisibility(View.GONE);

                edit.setVisibility(View.GONE);
                edit.setEnabled(false);

                delete.setEnabled(false);
                delete.setVisibility(View.GONE);
            }
        } else {
            bookmark.setVisibility(View.GONE);
            bookmark.setEnabled(false);

            bookmarked.setVisibility(View.GONE);
            bookmarked.setEnabled(false);

            edit.setVisibility(View.GONE);
            edit.setEnabled(false);

            delete.setVisibility(View.GONE);
            delete.setEnabled(false);
        }

        if(((Advertisement)this.getIntent().getExtras().get("Ad")).getAdType().equals("Rent")) {
            setRentAdDetails();
        } else {
            setSaleAdDetails();
        }


    }

    private boolean adIsBookmarkedByUser(Advertisement received) {
        for(String bookmakersId: received.getBookmarkedBy()) {
            if(userService.getUserUID().equals(bookmakersId)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    private void setSaleAdDetails() {
        Log.i(TAG, "in set sale ad details");

        adImagesVA.setUrlArrayList(saleAd.getUrlToImages());
        adImagesVA.notifyDataSetChanged();

        areaNameTV.setText(saleAd.getAreaName());
        fullAddressTV.setText(saleAd.getFullAddress());
        adTypeTV.setText(saleAd.getAdType());
        description.setText(saleAd.getDescription());

        bedroomTV.setText(Integer.toString(saleAd.getNumberOfBedrooms()));
        bathroomTV.setText(Integer.toString(saleAd.getNumberOfBathrooms()));
        balconyTV.setText(Integer.toString(saleAd.getNumberOfBalconies()));



        String gas, elevator, generator, garage, security;
        if(saleAd.getGasAvailability()) gas = getString( R.string._available );
        else gas = getString(R.string.not_available);

        if(saleAd.getElevator()) elevator = getString( R.string._available );
        else elevator = getString( R.string.not_available );

        if(saleAd.getGenerator()) generator = getString( R.string._available );
        else generator = getString( R.string.not_available );

        if(saleAd.getGarageSpace()) garage = getString( R.string._available );
        else garage = getString( R.string.not_available );

        gasTV.setText(gas);
        elevatorTV.setText(elevator);
        generatorTV.setText(generator);
        garageTV.setText(garage);

        floorTV.setText(Integer.toString(saleAd.getFloor()));
        floorSpaceTV.setText(saleAd.getFloorSpace() + " SQFT");

        paymentTV.setText(saleAd.getPaymentAmount() + " BDT");


        advertNameTV.setText(advertiser.getName());

        description.setText(description.getText());
    }

    @SuppressLint("SetTextI18n")
    private void setRentAdDetails() {
        Log.i(TAG, "in set rent ad details");

        adImagesVA.setUrlArrayList(rentAd.getUrlToImages());
        adImagesVA.notifyDataSetChanged();

        areaNameTV.setText(rentAd.getAreaName());
        Log.i(TAG, "in set rent ad details " + rentAd.getAreaName());

        fullAddressTV.setText(rentAd.getFullAddress());
        adTypeTV.setText(rentAd.getAdType());
        description.setText(rentAd.getDescription());

        
        bedroomTV.setText(Integer.toString(rentAd.getNumberOfBedrooms()));
        bathroomTV.setText(Integer.toString(rentAd.getNumberOfBathrooms()));
        balconyTV.setText(Integer.toString(rentAd.getNumberOfBalconies()));



        String gas, elevator, generator, garage, security;
        if(rentAd.getGasAvailability()) gas = getString( R.string._available );
        else gas = getString( R.string.not_available );

        if(rentAd.getElevator()) elevator = getString( R.string._available );
        else elevator = getString( R.string.not_available );

        if(rentAd.getGenerator()) generator = getString( R.string._available );
        else generator = getString( R.string.not_available );

        if(rentAd.getGarageSpace()) garage = getString( R.string._available );
        else garage = getString( R.string.not_available );

        if(rentAd.getSecurityGuard()) security = getString( R.string._available );
        else security = getString( R.string.not_available );

        gasTV.setText(gas);
        elevatorTV.setText(elevator);
        generatorTV.setText(generator);
        garageTV.setText(garage);
        securityTV.setText(security);

        floorTV.setText(Integer.toString(rentAd.getFloor()));
        Log.i(TAG, "floor =" + Integer.toString(rentAd.getFloor()));
        floorSpaceTV.setText(rentAd.getFloorSpace() + " SQFT");

        paymentTV.setText(rentAd.getPaymentAmount() + " BDT");
        utilityTV.setText(rentAd.getUtilityCharge()+ " BDT");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        String moveInDate = simpleDateFormat.format(rentAd.getAvailableFrom());
        moveInTV.setText(moveInDate);
        tenantTypeTV.setText(rentAd.getTenantType());

        advertNameTV.setText(advertiser.getName());

        description.setText(description.getText());
    }

    private void updateInfo(final RentAdvertisement advert) {
        Log.i(TAG, "in update info, para: rent ad");
        rentAd = advert;
        userService.findUserInfo(this, ((Advertisement) getIntent().getExtras().get("Ad")).getAdvertiserUID());
    }

    private void updateInfo(final SaleAdvertisement advert) {
        Log.i(TAG, "in update info, para: sale ad");
        saleAd = advert;
        userService.findUserInfo(this, ((Advertisement) getIntent().getExtras().get("Ad")).getAdvertiserUID());
    }

    private void bindWidgets() {
        Log.i(TAG, "in bind widgets");
        coordinatorLayout = findViewById(R.id.show_ad_layout);

        //top and bottom bars
        BottomNavBarHandler.setInstance(findViewById(R.id.show_ad_bottom_nav_bar), R.id.home);
        BottomNavBarHandler.handle(this);
        TopAppBarHandler.getInstance(findViewById(R.id.app_toolbaar), this).handle();

        //text views and card values
        areaNameTV = findViewById(R.id.home_area_name);
        fullAddressTV = findViewById(R.id.home_full_address);
        adTypeTV = findViewById(R.id.home_ad_type);
        description = findViewById(R.id.Description);

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
        bookmarked = findViewById(R.id.remove_ad);

        edit.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        bookmark.setVisibility(View.GONE);
        bookmarked.setVisibility(View.GONE);

        edit.setEnabled(false);
        delete.setEnabled(false);
        bookmarked.setEnabled(false);
        bookmark.setEnabled(false);

        imageRecView = findViewById(R.id.show_ad_images);

        userService = new UserService();
        adService = new AdvertisementService();
        adImagesVA = new ImageRecyclerViewAdapter();

        adImagesVA.setContext(this);
        imageRecView.setAdapter(adImagesVA);
        LinearLayoutManager llM = new LinearLayoutManager(this);
        llM.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageRecView.setLayoutManager(llM);

        Advertisement received = (Advertisement) getIntent().getExtras().get("Ad");

        if(received.getAdType().equals("Rent")) {
            adService.getRentAd(this, received.getAdvertisementID());
        } else {
            adService.getSaleAd(this, received.getAdvertisementID());
        }


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Log.i(TAG, "in on click");
        switch (v.getId()) {
            case R.id.edit_ad:

                Intent destination = new Intent(this, CreatePost.class);
                if(rentAd != null) {
                    destination.putExtra("Ad", rentAd);
                } else {
                    destination.putExtra("Ad", saleAd);
                }
                startActivity(destination);
                
                break;

            case R.id.delete_ad:
                confirmAndCompleteDeletion();

                break;

            case R.id.bookmark_ad:
                addToBookmarks();

                break;

            case R.id.remove_ad:
                removeFromBookmarks();

                break;

            case R.id.call_card:
                final String phone_number = advertiser.getPhoneNumber();

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

    private void confirmAndCompleteDeletion() {
        Advertisement received = (Advertisement) getIntent().getExtras().get("Ad");
        if(userService.isSignedIn() && userService.getUserUID().equals( received.getAdvertiserUID() )) {

            AlertDialog.Builder confirmDeletion = new AlertDialog.Builder(this);

            confirmDeletion
                    .setMessage("Are you sure you want to delete this post?")
                    .setPositiveButton("Yes", (dialog, which) -> proceedToDeletePost())
                    .setNegativeButton("No", (dialog, which) -> {
                        //do nothing
                    });

            AlertDialog confirmDeletionAlert = confirmDeletion.create();
            confirmDeletionAlert.show();
        }
    }

    private void removeFromBookmarks() {
        Log.i(TAG, "pressed remove from bookmark");
        Advertisement received = (Advertisement) getIntent().getExtras().get("Ad");
        List<String> bookmarkList = received.getBookmarkedBy();

        if(userService.isSignedIn()) {
            bookmarkList.remove(userService.getUserUID());
            received.setBookmarkedBy(bookmarkList);
            adService.editAd(received.getAdvertisementID(), received, new AdInterface.OnAdEditListener<Boolean>() {
                @Override
                public void OnAdEdited(Boolean edited, String error) {
                    Toast.makeText(ShowAdvertisementDetails.this, "Removed from bookmarks", Toast.LENGTH_SHORT).show();
                    bookmark.setEnabled(true);
                    bookmark.setVisibility(View.VISIBLE);

                    bookmarked.setEnabled(false);
                    bookmarked.setVisibility(View.GONE);
                }
            });
        }
    }

    private void addToBookmarks() {
        Advertisement received = (Advertisement) getIntent().getExtras().get("Ad");
        List<String> bookmarkList = received.getBookmarkedBy();

        bookmarkList.add(userService.getUserUID());
        received.setBookmarkedBy(bookmarkList);
        adService.editAd(received.getAdvertisementID(), received, new AdInterface.OnAdEditListener<Boolean>() {
            @Override
            public void OnAdEdited(Boolean edited, String error) {
                if(edited) {
                    Toast.makeText(ShowAdvertisementDetails.this, "Added to bookmarks", Toast.LENGTH_SHORT).show();
                    bookmarked.setEnabled(true);
                    bookmarked.setVisibility(View.VISIBLE);

                    bookmark.setEnabled(false);
                    bookmark.setVisibility(View.GONE);
                } else {
                    Log.i(TAG, error);
                }
            }
        });
    }

    private void proceedToDeletePost() {
        final Advertisement received = (Advertisement) getIntent().getExtras().get("Ad");

        final String adId = received.getAdvertisementID();
        final int totalToDelete = received.getNumberOfImages();
        final int[] alreadyDeleted = new int[1];
        alreadyDeleted[0] = 0;
        adService.deletePhotoFolder(adId, totalToDelete, (deleted, error) -> {
            startActivity(new Intent(ShowAdvertisementDetails.this.getApplicationContext(), AdFeed.class));
            if(deleted) {
                alreadyDeleted[0]++;
                Log.i(TAG, "number of the photo deleted = " + error);
                if(alreadyDeleted[0] == totalToDelete) {
                    adService.deleteAd(adId, new AdInterface.OnAdDeletedListener<Boolean>() {
                        @Override
                        public void OnAdDeleted(Boolean deleted, String error) {
                            if (deleted) {
                                Log.i(TAG, "Ad " + adId + " deleted successfully");
                            } else {
                                Log.i(TAG, error);
                            }
                        }
                    });
                }
            } else {
                Toast.makeText(ShowAdvertisementDetails.this.getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnParticularAdFetched(Object advert) {
        Log.i(TAG, "in on particular ad fetched");
        Advertisement received = (Advertisement) advert;
        if(received.getAdType().equals("Rent")) {
            updateInfo((RentAdvertisement)received);

        } else {
            updateInfo((SaleAdvertisement)received);
        }
    }

    @Override
    public void OnUserInfoFound(Object data) {
        Log.i(TAG, "in on user info found");
        User data1 = (User) data;
        advertiser.setName(data1.getName());
        advertiser.setPhoneNumber(data1.getPhoneNumber());
        advertiser.setEmailAddress(data1.getEmailAddress());
        advertiser.setUID(data1.getUID());
        attachValues();
    }

    @Override
    public void onPhotoClick(int position) {
        int total = 0;
        if(rentAd != null) {
            total = rentAd.getNumberOfImages();
        } else {
            total = saleAd.getNumberOfImages();
        }
        int photoNum = position + 1;
        Toast.makeText(this, "Image " + photoNum + " out of " + total, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.home_full_address) {
            final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            String fullAddress;
            if (rentAd != null) {
                fullAddress = rentAd.getFullAddress();
            } else {
                fullAddress = saleAd.getFullAddress();
            }
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Copy address to clipboard?", BaseTransientBottomBar.LENGTH_SHORT)
                    .setAction("Copy", v1 -> {
                        ClipData clip = ClipData.newPlainText("Full Address", fullAddress);
                        clipboard.setPrimaryClip(clip);
                    });
            snackbar.show();
        }
        return false;
    }
}