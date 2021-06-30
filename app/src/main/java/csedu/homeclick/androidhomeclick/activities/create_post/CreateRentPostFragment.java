package csedu.homeclick.androidhomeclick.activities.create_post;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;


import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.AdFeed;
import csedu.homeclick.androidhomeclick.activities.CreatePost;
import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.connector.AdvertisementService;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.recyclerviewadapters.ImageRecyclerViewAdapter;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.RentAdvertisement;


public class CreateRentPostFragment extends Fragment implements View.OnClickListener, CalendarView.OnDateChangeListener, ImageRecyclerViewAdapter.OnPhotoClickListener{
    private static final String TAG = "CreateRentPostFragment";
    private static final String PACKAGE_NAME = "csedu.homeclick.androidhomeclick";
    private static final String CLASS_NAME = "csedu.homeclick.androidhomeclick.activities.MapsActivity";

    private Boolean EDIT_MODE = false;
    private EditText rentAreaName, rentFullAddress,rentBedrooms, rentBathrooms, rentBalconies;
    private EditText rentFloor, rentFloorSpace, rentPayment, rentUtilityCharge, rentDescription;
    private CheckBox rentGas, rentElevator, rentGenerator, rentGarage, rentSecurity;

    private CalendarView rentAvailableFrom;

    private RadioGroup rentTenant;
    private RadioButton family, single;
    private Button postAd, selectPhotos;

    private TextView rentLocation;
    private Button addRentLocation;


    private  Button increase_bedrooms,decrease_bedrooms;
    int count_bedrooms;

    private  Button increase_bathrooms,decrease_bathrooms;
    int count_bathrooms;


    private  Button increase_balconies,decrease_balconies;
    int count_balconies;


    private int adapterPosition;
    private int prevPhotoAdapterPosition;

    private double longitude, latitude;

    private RecyclerView imageRecView, prevPhotoRecView;
    private TextView prevPhoto;
    private final ImageRecyclerViewAdapter imageRecVA = new ImageRecyclerViewAdapter();
    private final ImageRecyclerViewAdapter prevPhotoRecVA = new ImageRecyclerViewAdapter();

    private RentAdvertisement rentAd = null;


    private UserService userService;
    private final AdvertisementService advertisementService = new AdvertisementService();

    List<Uri> imageUri = new ArrayList<>();


    final ActivityResultLauncher<String> mapLauncher
            = registerForActivityResult(new ActivityResultContract<String, List<Double>>() {

        @NonNull
        @NotNull
        @Override
        public Intent createIntent(@NonNull @NotNull Context context, String input) {
            Intent intent = new Intent();
            intent.setClassName(PACKAGE_NAME,CLASS_NAME);
            double latitude = ( (CreatePost)(CreateRentPostFragment.this.requireActivity()) ).getLatitude();
            double longitude = ( (CreatePost)(CreateRentPostFragment.this.requireActivity()) ).getLongitude();
            if(EDIT_MODE) {
                RentAdvertisement received = (RentAdvertisement) requireActivity().getIntent().getExtras().get("Ad");
                latitude = received.getLatitude();
                longitude = received.getLongitude();
            }


            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            Log.i(TAG, "latitude = " + latitude + " longitude = " + longitude);
            return intent;
        }

        @Override
        public List<Double> parseResult(int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent intent) {
            List<Double> list = new ArrayList<>();
            try{
                if(intent == null) return list;
                else if(intent.getExtras() != null) {
                    list.add((Double) intent.getExtras().get("latitude"));
                    list.add((Double) intent.getExtras().get("longitude"));
                }
            }
            catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }
            return list;
        }
    }, new ActivityResultCallback<List<Double>>() {
        @Override
        public void onActivityResult(List<Double> result) {
            if(!result.isEmpty()) {
                rentLocation.setText("Latitude = " + result.get(0) + "\nLongitude = "+ result.get(1));
                CreateRentPostFragment.this.latitude = result.get(0);
                CreateRentPostFragment.this.longitude = result.get(1);
            }
        }
    });


    final ActivityResultLauncher<String> imageSelectorLauncher = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), result -> {
        CreateRentPostFragment.this.imageUri.addAll(result);

        //making sure a photo hasn't been added twice to the list
        LinkedHashSet<Uri> hashSet = new LinkedHashSet<>(CreateRentPostFragment.this.imageUri);
        CreateRentPostFragment.this.imageUri = new ArrayList<>(hashSet);

        CreateRentPostFragment.this.imageRecVA.setOnPhotoClickListener(CreateRentPostFragment.this);
        CreateRentPostFragment.this.imageRecVA.setUrlArrayList(CreateRentPostFragment.this.imageUri);
        CreateRentPostFragment.this.imageRecVA.notifyDataSetChanged();
    });

    private final Date[] rentAvailFrom = new Date[1];

    public CreateRentPostFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_rent_post, container, false);

        bindWidgets(view);

        if( requireActivity().getIntent().getExtras() != null) {
            Advertisement received = (Advertisement) requireActivity().getIntent().getExtras().get("Ad");
            if(received.getAdType().equals("Rent")) {
                rentAd = (RentAdvertisement) received;
                EDIT_MODE = true;
            }
        }

        if(EDIT_MODE) {
            setWidgets();
        }
        setClickListeners();


        return view;



    }

    private void setClickListeners() {
        Log.i(TAG, "in set click listeners");
        rentAvailableFrom.setOnDateChangeListener(this);
        postAd.setOnClickListener(this);

        addRentLocation.setOnClickListener(this);

        selectPhotos.setOnClickListener(this);
        increase_bedrooms.setOnClickListener(this);
        decrease_bedrooms.setOnClickListener(this);
        increase_bathrooms.setOnClickListener(this);
        decrease_bathrooms.setOnClickListener(this);
        increase_balconies.setOnClickListener(this);
        decrease_balconies.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setWidgets() {
        prevPhotoRecView.setVisibility(View.VISIBLE);
        prevPhoto.setVisibility(View.VISIBLE);

        prevPhotoRecVA.setContext(this.getContext());
        prevPhotoRecVA.setUrlArrayList(rentAd.getUrlToImages());
        prevPhotoRecVA.notifyDataSetChanged();
        prevPhotoRecVA.setOnPhotoClickListener(position -> CreateRentPostFragment.this.prevPhotoAdapterPosition = position);
        registerForContextMenu(prevPhotoRecView);

        rentAreaName.setText(rentAd.getAreaName());
        rentFullAddress.setText(rentAd.getFullAddress());
        rentBedrooms.setText(Integer.toString( rentAd.getNumberOfBedrooms() ));
        rentBathrooms.setText(Integer.toString(rentAd.getNumberOfBathrooms()));
        rentBalconies.setText(Integer.toString(rentAd.getNumberOfBalconies()));
        rentFloor.setText(Integer.toString( rentAd.getFloor() ));
        rentFloorSpace.setText(Integer.toString( rentAd.getFloorSpace() ));
        rentGas.setChecked(rentAd.getGasAvailability());
        rentElevator.setChecked(rentAd.getElevator());
        rentGenerator.setChecked(rentAd.getGenerator());
        rentGarage.setChecked(rentAd.getGarageSpace());
        rentSecurity.setChecked(rentAd.getSecurityGuard());
        rentPayment.setText(Integer.toString( rentAd.getPaymentAmount() ));
        rentUtilityCharge.setText(Integer.toString(rentAd.getUtilityCharge()));

        rentDescription.setText(rentAd.getDescription());
        String rentCoOrdinates = "Latitude = " + rentAd.getLatitude() + "\nLongitude = " + rentAd.getLongitude();
        this.latitude = rentAd.getLatitude();
        this.longitude = rentAd.getLongitude();
        rentLocation.setText(rentCoOrdinates);
        if( rentAd.getTenantType().equals("Family") ) {
            family.setChecked(true);
        } else {
            single.setChecked(true);
        }

        rentAvailableFrom.setDate(rentAd.getAvailableFrom().getTime());
    }

    private void bindWidgets(View view) {
        prevPhotoAdapterPosition = -1;

        Log.i(TAG, "in bind widgets");
        userService = new UserService();


        imageRecView = view.findViewById(R.id.imageRecView);
        imageRecVA.setContext(CreateRentPostFragment.this.getContext());
        imageRecView.setAdapter(imageRecVA);
        LinearLayoutManager llM = new LinearLayoutManager(this.getContext());
        llM.setOrientation(LinearLayoutManager.VERTICAL);
        imageRecView.setLayoutManager(llM);

        registerForContextMenu(imageRecView);

        prevPhotoRecView = view.findViewById(R.id.previouslyAddedImageRecView);
        prevPhoto = view.findViewById(R.id.prevPhotoTextView);
        prevPhotoRecVA.setContext(this.getContext());
        prevPhotoRecView.setAdapter(prevPhotoRecVA);

        LinearLayoutManager llM2 = new LinearLayoutManager(this.getContext());
        llM2.setOrientation(LinearLayoutManager.VERTICAL);
        prevPhotoRecView.setLayoutManager(llM2);

        rentAreaName = view.findViewById(R.id.rentAreaName);
        rentFullAddress = view.findViewById(R.id.rentFullAddress);
        rentBedrooms = view.findViewById(R.id.rentBedrooms);
        rentBathrooms = view.findViewById(R.id.rentBathrooms);
        rentBalconies = view.findViewById(R.id.rentBalconies);
        rentFloor = view.findViewById(R.id.rentFloor);
        rentFloorSpace = view.findViewById(R.id.rentFloorSpace);
        rentGas = view.findViewById(R.id.rentGas);
        rentElevator = view.findViewById(R.id.rentElevator);
        rentGenerator = view.findViewById(R.id.rentGenerator);
        rentGarage = view.findViewById(R.id.rentGarage);
        rentSecurity = view.findViewById(R.id.rentSecurity);

        if(rentSecurity == null) {
            Log.i(TAG, "rent security still null somehow");
        }
        rentPayment = view.findViewById(R.id.rentPayment);
        rentUtilityCharge = view.findViewById(R.id.rentUtilityCharge);
        rentAvailableFrom = view.findViewById(R.id.rentAvailableFrom);
        rentDescription = view.findViewById(R.id.rentDescription);
        rentTenant = view.findViewById(R.id.rdTenant);
        family = view.findViewById(R.id.rbFamily);
        single = view.findViewById(R.id.rbSinglePerson);
        postAd = view.findViewById(R.id.buttonRentPostAd);

        selectPhotos = view.findViewById(R.id.select_rent);

        rentLocation = view.findViewById(R.id.rent_location);
        addRentLocation = view.findViewById(R.id.rent_add_location);


        increase_bedrooms = view.findViewById(R.id.increase_bedrooms);
        decrease_bedrooms = view.findViewById(R.id.decrease_bedrooms);
        increase_bathrooms = view.findViewById(R.id.increase_bathrooms);
        decrease_bathrooms = view.findViewById(R.id.decrease_bathrooms);
        increase_balconies = view.findViewById(R.id.increase_balconies);
        decrease_balconies = view.findViewById(R.id.decrease_balconies);

        this.latitude = ( (CreatePost)(CreateRentPostFragment.this.requireActivity()) ).getLatitude();
        this.longitude = ( (CreatePost)(CreateRentPostFragment.this.requireActivity()) ).getLongitude();

    }




    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_rent:
                imageSelectorLauncher.launch("image/*");
                break;

            case R.id.buttonRentPostAd:
                Toast.makeText(requireContext().getApplicationContext(), "post ad clicked", Toast.LENGTH_SHORT).show();
                if(EDIT_MODE) {
                    editPost(v);
                } else {
                    createPost(v);
                }

                break;

            case R.id.increase_bedrooms:
                count_bedrooms++;
                decrease_bedrooms.setEnabled(true);
                rentBedrooms.setText(Integer.toString(count_bedrooms));
                break;
            case R.id.decrease_bedrooms:
                if(count_bedrooms > 0) {
                    count_bedrooms--;
                    if(count_bedrooms == 0) {
                        decrease_bedrooms.setEnabled(false);
                    }
                } else {
                    count_bedrooms = 0;
                    decrease_bedrooms.setEnabled(false);
                }
                rentBedrooms.setText(Integer.toString(count_bedrooms));
                break;
            case R.id.increase_bathrooms:
                count_bathrooms++;
                decrease_bathrooms.setEnabled(true);
                rentBathrooms.setText(Integer.toString(count_bathrooms));
                break;
            case R.id.decrease_bathrooms:
                if(count_bathrooms > 0){
                    count_bathrooms--;
                    if(count_bathrooms == 0) {
                        decrease_bathrooms.setEnabled(false);
                    }
                } else {
                    count_bathrooms = 0;
                    decrease_bathrooms.setEnabled(false);
                }
                rentBathrooms.setText(Integer.toString(count_bathrooms));
                break;
            case R.id.increase_balconies:
                count_balconies++;
                decrease_balconies.setEnabled(true);
                rentBalconies.setText(Integer.toString(count_balconies));
                break;
            case R.id.decrease_balconies:
                if(count_balconies > 0){
                    count_balconies--;
                    if(count_balconies == 0) {
                        decrease_balconies.setEnabled(false);
                    }
                } else {
                    count_balconies = 0;
                    decrease_balconies.setEnabled(false);
                }
                rentBalconies.setText(Integer.toString(count_balconies));
                break;

            case R.id.rent_add_location:
                mapLauncher.launch(CLASS_NAME);
                break;
            default:
                break;
        }

    }

    private void editPost(View v) {
        Log.i(TAG, "in edit post");
        if(checkData()) {
            postAd.setEnabled(false);

            final List<String> fileExtensions;

            RentAdvertisement adWithNewInfo = makeAd();
            adWithNewInfo.setAdvertisementID(rentAd.getAdvertisementID());
            adWithNewInfo.setAdvertiserUID(rentAd.getAdvertiserUID());
            adWithNewInfo.setUrlToImages(rentAd.getUrlToImages());
            adWithNewInfo.setNumberOfImages(rentAd.getUrlToImages().size());
            adWithNewInfo.setLatitude(latitude);
            adWithNewInfo.setLongitude(longitude);
            rentAd = adWithNewInfo;

            if(!imageUri.isEmpty()) {
                fileExtensions = getFileExtensions(imageUri);
                processUploads(fileExtensions, imageUri);
            } else {
                advertisementService.editAd(rentAd.getAdvertisementID(), rentAd, (edited, error) -> {
                    if(edited) {
                        Log.i(TAG, "edited successfully");
                        startActivity(new Intent(CreateRentPostFragment.this.requireContext().getApplicationContext(), AdFeed.class));
                    } else {
                        Log.i(TAG, error);
                    }
                });
            }

        } else {
            Log.i(TAG, "check data failed for editing");
        }
    }

    private void processUploads(List<String> fileExtensions, List<Uri> imageUri) {
        final int newTotal = rentAd.getNumberOfImages() + imageUri.size();
        final String adId = rentAd.getAdvertisementID();
        final RentAdvertisement toEdit = rentAd;
        Log.i(TAG, "rentAd size = " + rentAd.getUrlToImages().size());
        final List<String> downloadLinks = new ArrayList<>();
        final int[] uploadCount = new int[1];
        for(int imageCount = 0; imageCount < imageUri.size(); imageCount++) {
            final int total = imageUri.size();
            advertisementService.uploadPhoto(imageUri.get(imageCount), fileExtensions.get(imageCount), adId, new AdInterface.OnPhotoUploadListener<String>() {
                @Override
                public void ongoingProgress(int percentage) {

                }

                @Override
                public void onCompleteNotify(String downloadUrl) {
                    uploadCount[0]++;
                    downloadLinks.add(downloadUrl);
                    if(uploadCount[0] == total) {
                        List<String> finalUrls = toEdit.getUrlToImages();
                        Log.i(TAG, "before adding = " + finalUrls.size());
                        finalUrls.addAll(downloadLinks);
                        Log.i(TAG, "after adding = " + finalUrls.size());
                        toEdit.setUrlToImages( finalUrls );
                        toEdit.setNumberOfImages( finalUrls.size() );

                        advertisementService.editAd(adId, toEdit, (edited, error) -> {
                            if(!edited) {
                                Log.i(TAG, error);
                            } else {
                                Log.i(TAG, "Ad edited successfully uri wasn't empty");
                                startActivity(new Intent(CreateRentPostFragment.this.requireContext().getApplicationContext(), AdFeed.class));
                            }
                        });
                    }
                }
            });
        }

    }

    private void createPost(View v) {
        Log.i(TAG, "in create post");
        if(checkData()) {
            postAd.setEnabled(false);
            Log.i(TAG, "cleared check data?");
            final RentAdvertisement rentAd = makeAd();

            rentAd.setAdvertiserUID(userService.getUserUID());

            final List<String> fileExtensions = getFileExtensions(imageUri);
            Log.i(TAG, fileExtensions.toString());

            processUploads(fileExtensions, imageUri, rentAd);
        } else {
            Log.i(TAG, "data checking failed");
        }
    }

    private void processUploads(final List<String> fileExtensions, final List<Uri> uriList, final RentAdvertisement rentAd) {
        Log.i(TAG, "in process uploads");
        advertisementService.getAdId(adId -> {
            rentAd.setAdvertisementID(adId);
            final int[] uploadCount = new int[1];
            final List<String> downloadLinks = new ArrayList<>();

            for(int imageCount = 0; imageCount < uriList.size(); imageCount++) {
                final int total = uriList.size();
                advertisementService.uploadPhoto(uriList.get(imageCount), fileExtensions.get(imageCount), adId, new AdInterface.OnPhotoUploadListener<String>() {
                    @Override
                    public void ongoingProgress(int percentage) {

                    }

                    @Override
                    public void onCompleteNotify(String downloadUrl) {
                        uploadCount[0]++;
                        downloadLinks.add(downloadUrl);
                        if(uploadCount[0] == total) {
                            rentAd.setUrlToImages(downloadLinks);
                            CreateRentPostFragment.this.completeAdPost(rentAd);
                        }
                    }
                });
            }

        });
    }

    void completeAdPost(final RentAdvertisement rentAd) {
        advertisementService.completeAdPost(rentAd, new AdInterface.OnAdPostSuccessListener<Boolean>() {
            @Override
            public void OnAdPostSuccessful(Boolean data) {
                Toast.makeText(requireContext().getApplicationContext(), "Ad posted successfully.", Toast.LENGTH_SHORT).show();
                CreateRentPostFragment.this.postAd.setEnabled(true);
                CreateRentPostFragment.this.startActivity(new Intent(requireContext().getApplicationContext(), AdFeed.class));
            }

            @Override
            public void OnAdPostFailed(String error) {
                Toast.makeText(requireContext().getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                CreateRentPostFragment.this.postAd.setEnabled(true);
                CreateRentPostFragment.this.startActivity(new Intent(requireContext().getApplicationContext(), AdFeed.class));
            }
        });
    }

    private List<String> getFileExtensions(List<Uri> listUri) {
        List<String> extensions = new ArrayList<>();
        ContentResolver cr = this.requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        for(Uri uri: listUri) {
            extensions.add(mime.getExtensionFromMimeType(cr.getType(uri)));
        }

        return extensions;
    }

    public RentAdvertisement makeAd() {
        String areaName = rentAreaName.getText().toString().trim();
        String fullAddress = rentFullAddress.getText().toString().trim();
        int numOfBedrooms = Integer.parseInt(rentBedrooms.getText().toString().trim());
        int numOfBathrooms = Integer.parseInt(rentBathrooms.getText().toString().trim());
        int numOfBalconies = Integer.parseInt(rentBalconies.getText().toString().trim());
        int floor = Integer.parseInt(rentFloor.getText().toString().trim());
        int floorSpace = Integer.parseInt(rentFloorSpace.getText().toString().trim());

        Boolean gasAvail = rentGas.isChecked();
        Boolean elevatorAvail = rentElevator.isChecked();
        Boolean generatorAvail = rentGenerator.isChecked();
        Boolean garageAvail = rentGarage.isChecked();
        Boolean securityAvail = rentSecurity.isChecked();

        int payAmount = Integer.parseInt(rentPayment.getText().toString().trim());
        int utilities = Integer.parseInt(rentUtilityCharge.getText().toString().trim());

        String rentDesc = rentDescription.getText().toString().trim();

        String tenantType;
        int checked = rentTenant.getCheckedRadioButtonId();

        if(checked == R.id.rbFamily) {
            tenantType = "Family";
        } else {
            tenantType = "Student/Working Person";
        }

        if(rentAvailFrom[0] == null) {
            rentAvailFrom[0] = new Date(System.currentTimeMillis());
        }

        RentAdvertisement rent = new RentAdvertisement(areaName, fullAddress, "Rent",
                numOfBedrooms, numOfBathrooms, gasAvail, payAmount,
                numOfBalconies, floor, floorSpace, elevatorAvail, generatorAvail,
                garageAvail,  imageUri.size(), tenantType, utilities, rentDesc, securityAvail, rentAvailFrom[0]);

        rent.setAdvertiserUID(userService.getUserUID());
        rent.setLatitude(this.latitude);
        rent.setLongitude(this.longitude);

        Log.i("date", rent.getAvailableFrom().toString());
        return rent;
    }

    private boolean checkData() {
        Log.i(TAG, "in check data");
        boolean dataOkay = true;
        EditText[] allEditTexts = {rentAreaName,rentBedrooms, rentFullAddress, rentBathrooms, rentBalconies,
                rentFloor, rentFloorSpace, rentPayment, rentUtilityCharge, rentDescription};


        for(EditText e: allEditTexts) {
            if(e.getText().toString().trim().isEmpty()) {
                e.setError("Field cannot be empty");
                dataOkay = false;
            }
        }


        int picked = rentTenant.getCheckedRadioButtonId();

        if(picked != R.id.rbFamily && picked != R.id.rbSinglePerson) {
            Toast.makeText(this.requireContext().getApplicationContext(), "You must pick a tenant type.", Toast.LENGTH_SHORT).show();
            dataOkay = false;
        }

        if(this.imageUri.isEmpty() && !EDIT_MODE) {
            dataOkay = false;
            Toast.makeText(this.requireContext().getApplicationContext(), "You must add photos to your post.", Toast.LENGTH_SHORT).show();
        }

        if(EDIT_MODE && this.imageUri.isEmpty() && rentAd.getUrlToImages().isEmpty()) {
            dataOkay = false;
            Toast.makeText(this.requireContext().getApplicationContext(), "You must add photos to your post.", Toast.LENGTH_SHORT).show();
        }

        return dataOkay;
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);
        rentAvailFrom[0] = new Date(cal.getTimeInMillis());
        Log.i("date", rentAvailFrom[0].toString());
    }


    @Override
    public void onCreateContextMenu(@NonNull @NotNull ContextMenu menu, @NonNull @NotNull View v, @Nullable @org.jetbrains.annotations.Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        requireActivity().getMenuInflater().inflate(R.menu.remove_photo, menu);
    }

    @Override
    public void onPhotoClick(int position) {
        setAdapterPosition(position);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.remove_photo) {
            if (prevPhotoAdapterPosition == -1) {
                Log.i(TAG, "nwe photo adapter position = " + getAdapterPosition());
                imageUri.remove(getAdapterPosition());
                imageRecVA.setUrlArrayList(imageUri);
                imageRecVA.notifyDataSetChanged();
            } else {
                Log.i(TAG, "prev photo adapter position = " + prevPhotoAdapterPosition);
                rentAd.getUrlToImages().remove(prevPhotoAdapterPosition);
                rentAd.setNumberOfImages(rentAd.getNumberOfImages() - 1);
                prevPhotoRecVA.setUrlArrayList(rentAd.getUrlToImages());
                prevPhotoRecVA.notifyDataSetChanged();
                prevPhotoAdapterPosition = -1;
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }
}
