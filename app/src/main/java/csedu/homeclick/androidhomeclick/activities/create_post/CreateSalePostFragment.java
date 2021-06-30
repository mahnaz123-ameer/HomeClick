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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.AdFeed;
import csedu.homeclick.androidhomeclick.activities.CreatePost;
import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.connector.AdvertisementService;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.recyclerviewadapters.ImageRecyclerViewAdapter;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.SaleAdvertisement;


public class CreateSalePostFragment extends Fragment implements View.OnClickListener, ImageRecyclerViewAdapter.OnPhotoClickListener {
    private static final String TAG = "CreateSalePostFragment";
    private static final String PACKAGE_NAME = "csedu.homeclick.androidhomeclick";
    private static final String CLASS_NAME = "csedu.homeclick.androidhomeclick.activities.MapsActivity";


    private Boolean EDIT_MODE = false;
    private EditText saleAreaName, saleFullAddress, saleBedrooms, saleBathrooms, saleBalconies;
    private EditText saleFloor,saleFloorSpace,salePayment,saleDescription;
    private CheckBox saleGas, saleElevator, saleGenerator, saleGarage,saleSecurity;


    private RadioGroup saleSituation;
    private RadioButton New,established;
    private Button postAd, selectPhotos;
    private Button addSaleLocation;
    private TextView saleLocation;


    private  Button increase_sale_bedrooms,decrease_sale_bedrooms;
    int count_sale_bedrooms;

    private  Button increase_sale_bathrooms,decrease_sale_bathrooms;
    int count_sale_bathrooms;


    private  Button increase_sale_balconies,decrease_sale_balconies;
    int count_sale_balconies;

    private int adapterPosition;
    private int prevPhotoAdapterPosition;

    private RecyclerView imageRecView,prevPhotoRecView;
    private TextView prevPhoto;
    private ImageRecyclerViewAdapter imageRecVA = new ImageRecyclerViewAdapter();
    private ImageRecyclerViewAdapter prevPhotoRecVA = new ImageRecyclerViewAdapter();


    private SaleAdvertisement saleAd = null;

    private UserService userService;
    private final AdvertisementService advertisementService = new AdvertisementService();

    private double longitude, latitude;

    List<Uri> imageUri = new ArrayList<>();

    final ActivityResultLauncher<String> mapLauncher
            = registerForActivityResult(new ActivityResultContract<String, List<Double>>() {

        @NonNull
        @NotNull
        @Override
        public Intent createIntent(@NonNull @NotNull Context context, String input) {
            Intent intent = new Intent();
            intent.setClassName(PACKAGE_NAME,CLASS_NAME);
            double latitude = ( (CreatePost)(CreateSalePostFragment.this.requireActivity()) ).getLatitude();
            double longitude = ( (CreatePost)(CreateSalePostFragment.this.requireActivity()) ).getLongitude();
            if(EDIT_MODE) {
                SaleAdvertisement received = (SaleAdvertisement) requireActivity().getIntent().getExtras().get("Ad");
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
            if(intent == null) return list;
            if(intent.getExtras() != null) {
                list.add((Double) intent.getExtras().get("latitude"));
                list.add((Double) intent.getExtras().get("longitude"));
            }
            return list;
        }
    }, new ActivityResultCallback<List<Double>>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onActivityResult(List<Double> result) {
            if(!result.isEmpty()) {
                CreateSalePostFragment.this.latitude = result.get(0);
                CreateSalePostFragment.this.longitude = result.get(1);
                saleLocation.setText("Latitude = " + result.get(0) + "\nLongitude = "+ result.get(1));
            }
        }
    });

    final ActivityResultLauncher<String> imageSelectorLauncher = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), result -> {
        CreateSalePostFragment.this.imageUri.addAll(result);

        //making sure a photo hasn't been added twice to the list
        LinkedHashSet<Uri> hashSet = new LinkedHashSet<>(CreateSalePostFragment.this.imageUri);
        CreateSalePostFragment.this.imageUri = new ArrayList<>(hashSet);


        CreateSalePostFragment.this.imageRecVA.setOnPhotoClickListener(CreateSalePostFragment.this);
        CreateSalePostFragment.this.imageRecVA.setUrlArrayList(CreateSalePostFragment.this.imageUri);
        CreateSalePostFragment.this.imageRecVA.notifyDataSetChanged();

    });

    public CreateSalePostFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_sale_post, container, false);
        bindWidgets(view);

        if( requireActivity().getIntent().getExtras() != null) {
            Advertisement received = (Advertisement) requireActivity().getIntent().getExtras().get("Ad");
            if(received.getAdType().equals("Sale")) {
                saleAd = (SaleAdvertisement) received;
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
        postAd.setOnClickListener(this);
        selectPhotos.setOnClickListener(this);
        increase_sale_bedrooms.setOnClickListener(this);
        decrease_sale_bedrooms.setOnClickListener(this);
        increase_sale_bathrooms.setOnClickListener(this);
        decrease_sale_bathrooms.setOnClickListener(this);
        increase_sale_balconies.setOnClickListener(this);
        decrease_sale_balconies.setOnClickListener(this);
        addSaleLocation.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setWidgets() {
        decrease_sale_bedrooms.setEnabled(true);
        decrease_sale_bathrooms.setEnabled(true);
        decrease_sale_balconies.setEnabled(true);

        prevPhotoRecView.setVisibility(View.VISIBLE);
        prevPhoto.setVisibility(View.VISIBLE);

        prevPhotoRecVA.setContext(this.getContext());
        prevPhotoRecVA.setUrlArrayList(saleAd.getUrlToImages());
        prevPhotoRecVA.notifyDataSetChanged();
        prevPhotoRecVA.setOnPhotoClickListener(position -> CreateSalePostFragment.this.prevPhotoAdapterPosition = position);

        registerForContextMenu(prevPhotoRecView);

        saleAreaName.setText(saleAd.getAreaName());
        saleFullAddress.setText(saleAd.getFullAddress());
        saleBedrooms.setText(Integer.toString(saleAd.getNumberOfBedrooms()));
        saleBathrooms.setText(Integer.toString(saleAd.getNumberOfBathrooms()));
        saleBalconies.setText(Integer.toString(saleAd.getNumberOfBalconies()));

        count_sale_bedrooms = saleAd.getNumberOfBedrooms();
        count_sale_bathrooms = saleAd.getNumberOfBathrooms();
        count_sale_balconies = saleAd.getNumberOfBalconies();

        saleFloor.setText(Integer.toString(saleAd.getFloor()));
        saleFloorSpace.setText(Integer.toString( saleAd.getFloorSpace()));
        saleGas.setChecked(saleAd.getGasAvailability());
        saleElevator.setChecked(saleAd.getElevator());
        saleGenerator.setChecked(saleAd.getGenerator());
        saleGarage.setChecked(saleAd.getGarageSpace());

        salePayment.setText(Integer.toString( saleAd.getPaymentAmount() ));

        saleDescription.setText(saleAd.getDescription());

        String saleCoOrdinates = "Latitude = " + saleAd.getLatitude() + "\nLongitude = " + saleAd.getLongitude();
        this.latitude = saleAd.getLatitude();
        this.longitude = saleAd.getLongitude();
        saleLocation.setText(saleCoOrdinates);

        if( saleAd.getPropertyCondition().equals("New") ) {
            New.setChecked(true);
        } else {
            established.setChecked(true);
        }

    }

    private void bindWidgets(View view) {
        prevPhotoAdapterPosition = -1;

        Log.i(TAG, "in bind widgets");
        userService = new UserService();

        imageRecView = view.findViewById(R.id.saleImageRecView);
        imageRecVA.setContext(CreateSalePostFragment.this.getContext());
        imageRecView.setAdapter(imageRecVA);
        LinearLayoutManager llM = new LinearLayoutManager(this.getContext());
        llM.setOrientation(LinearLayoutManager.VERTICAL);
        imageRecView.setLayoutManager(llM);

        registerForContextMenu(imageRecView);



        prevPhotoRecView = view.findViewById(R.id.previouslyAddedSaleImageRecView);
        prevPhoto = view.findViewById(R.id.prevPhotoTextView);
        prevPhotoRecVA.setContext(this.getContext());
        prevPhotoRecView.setAdapter(prevPhotoRecVA);

        LinearLayoutManager llM2 = new LinearLayoutManager(this.getContext());
        llM2.setOrientation(LinearLayoutManager.VERTICAL);
        prevPhotoRecView.setLayoutManager(llM2);

        saleAreaName = view.findViewById(R.id.saleAreaName);
        saleFullAddress = view.findViewById(R.id.saleFullAddress);
        saleBedrooms = view.findViewById(R.id.saleBedrooms);
        saleBathrooms = view.findViewById(R.id.saleBathrooms);
        saleBalconies = view.findViewById(R.id.saleBalconies);
        saleFloor = view.findViewById(R.id.saleFloor);
        saleFloorSpace = view.findViewById(R.id.saleFloorSpace);
        saleGas = view.findViewById(R.id.saleGas);
        saleElevator = view.findViewById(R.id.saleElevator);
        saleGenerator = view.findViewById(R.id.saleGenerator);
        saleGarage = view.findViewById(R.id.saleGarage);
        saleSecurity = view.findViewById(R.id.saleSecurity);
        salePayment = view.findViewById(R.id.salePayment);
        saleDescription = view.findViewById(R.id.saleDescription);
        saleSituation = view.findViewById(R.id.saleSituation);
        New = view.findViewById(R.id.rbNew);
        established = view.findViewById(R.id.rbEstablished);
        postAd = view.findViewById(R.id.buttonSalePostAd);

        selectPhotos = view.findViewById(R.id.select_sale);
        saleLocation = view.findViewById(R.id.sale_location);
        addSaleLocation = view.findViewById(R.id.sale_add_location);


        increase_sale_bedrooms = view.findViewById(R.id.increase_bedrooms);
        decrease_sale_bedrooms = view.findViewById(R.id.decrease_bedrooms);
        increase_sale_bathrooms = view.findViewById(R.id.increase_bathrooms);
        decrease_sale_bathrooms = view.findViewById(R.id.decrease_bathrooms);
        increase_sale_balconies = view.findViewById(R.id.increase_balconies);
        decrease_sale_balconies = view.findViewById(R.id.decrease_balconies);


        this.latitude = ( (CreatePost)(CreateSalePostFragment.this.requireActivity()) ).getLatitude();
        this.longitude = ( (CreatePost)(CreateSalePostFragment.this.requireActivity()) ).getLongitude();
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.select_sale:
                imageSelectorLauncher.launch("image/*");
                break;

            case R.id.buttonSalePostAd:
                Toast.makeText(requireContext().getApplicationContext(), "post ad clicked", Toast.LENGTH_SHORT).show();
                if(EDIT_MODE) {
                    editPost(v);
                } else {
                    createPost(v);
                }
            case R.id.increase_bedrooms:
                decrease_sale_bedrooms.setEnabled(true);
                count_sale_bedrooms++;
                saleBedrooms.setText(Integer.toString(count_sale_bedrooms));
                break;
            case R.id.decrease_bedrooms:
                if(count_sale_bedrooms > 0) {
                    count_sale_bedrooms--;
                    if(count_sale_bedrooms == 0) decrease_sale_bedrooms.setEnabled(false);
                } else {
                    decrease_sale_bedrooms.setEnabled(false);
                    count_sale_bedrooms = 0;
                }
                saleBedrooms.setText(Integer.toString(count_sale_bedrooms));
                break;
            case R.id.increase_bathrooms:
                count_sale_bathrooms++;
                decrease_sale_bathrooms.setEnabled(true);
                saleBathrooms.setText(Integer.toString(count_sale_bathrooms));
                break;
            case R.id.decrease_bathrooms:
                if(count_sale_bathrooms > 0){
                    count_sale_bathrooms--;
                    if(count_sale_bathrooms == 0) decrease_sale_bathrooms.setEnabled(false);
                } else {
                    decrease_sale_bathrooms.setEnabled(false);
                    count_sale_bathrooms = 0;
                }
                saleBathrooms.setText(Integer.toString(count_sale_bathrooms));
                break;
            case R.id.increase_balconies:
                count_sale_balconies++;
                decrease_sale_balconies.setEnabled(true);
                saleBalconies.setText(Integer.toString(count_sale_balconies));
                break;
            case R.id.decrease_balconies:
                if(count_sale_balconies > 0){
                    count_sale_balconies--;
                    if(count_sale_balconies == 0) decrease_sale_balconies.setEnabled(false);
                } else {
                    decrease_sale_balconies.setEnabled(false);
                    count_sale_balconies = 0;
                }
                saleBalconies.setText(Integer.toString(count_sale_balconies));
                break;
            case R.id.sale_add_location:
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

            SaleAdvertisement adWithNewInfo = makeAd();
            adWithNewInfo.setAdvertisementID(saleAd.getAdvertisementID());
            adWithNewInfo.setAdvertiserUID(saleAd.getAdvertiserUID());
            adWithNewInfo.setUrlToImages(saleAd.getUrlToImages());
            adWithNewInfo.setNumberOfImages(saleAd.getUrlToImages().size());
            adWithNewInfo.setLatitude(latitude);
            adWithNewInfo.setLongitude(longitude);
            saleAd = adWithNewInfo;

            if(!imageUri.isEmpty()) {
                fileExtensions = getFileExtensions(imageUri);
                processUploads(fileExtensions, imageUri);
            } else {
                advertisementService.editAd(saleAd.getAdvertisementID(), saleAd, (edited, error) -> {
                    if(edited) {
                        Log.i(TAG, "edited successfully");
                        CreateSalePostFragment.this.postAd.setEnabled(true);
                        startActivity(new Intent(CreateSalePostFragment.this.requireContext().getApplicationContext(), AdFeed.class));
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
        final int newTotal = saleAd.getNumberOfImages() + imageUri.size();
        final String adId = saleAd.getAdvertisementID();
        final SaleAdvertisement toEdit = saleAd;
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
                        finalUrls.addAll(downloadLinks);
                        toEdit.setUrlToImages( finalUrls );
                        toEdit.setNumberOfImages( finalUrls.size() );

                        advertisementService.editAd(adId, toEdit, (edited, error) -> {
                            if(!edited) {
                                Log.i(TAG, error);
                            } else {
                                Log.i(TAG, "Ad edited successfully");
                                startActivity(new Intent(CreateSalePostFragment.this.requireContext().getApplicationContext(), AdFeed.class));
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
            final SaleAdvertisement saleAd = makeAd();

            saleAd.setAdvertiserUID(userService.getUserUID());

            final List<String> fileExtensions = getFileExtensions(imageUri);
            Log.i(TAG, fileExtensions.toString());

            processUploads(fileExtensions, imageUri, saleAd);
        } else {
            Log.i(TAG, "data checking failed");
        }
    }

    private void processUploads(final List<String> fileExtensions, final List<Uri> uriList, final SaleAdvertisement saleAd) {
        Log.i(TAG, "in process uploads");
        advertisementService.getAdId(adId -> {
            saleAd.setAdvertisementID(adId);
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
                            saleAd.setUrlToImages(downloadLinks);
                            CreateSalePostFragment.this.completeAdPost(saleAd);
                        }
                    }
                });
            }

        });
    }

    void completeAdPost(final SaleAdvertisement saleAd) {
        advertisementService.completeAdPost(saleAd, new AdInterface.OnAdPostSuccessListener<Boolean>() {
            @Override
            public void OnAdPostSuccessful(Boolean data) {
                Toast.makeText(requireContext().getApplicationContext(), "Ad posted successfully.", Toast.LENGTH_SHORT).show();
                CreateSalePostFragment.this.postAd.setEnabled(true);
                CreateSalePostFragment.this.startActivity(new Intent(requireContext().getApplicationContext(), AdFeed.class));
            }

            @Override
            public void OnAdPostFailed(String error) {
                Toast.makeText(requireContext().getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                CreateSalePostFragment.this.postAd.setEnabled(true);
                CreateSalePostFragment.this.startActivity(new Intent(requireContext().getApplicationContext(), AdFeed.class));
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

    public SaleAdvertisement makeAd() {
        String areaName = saleAreaName.getText().toString().trim();
        String fullAddress = saleFullAddress.getText().toString().trim();
        int numOfBedrooms = Integer.parseInt(saleBedrooms.getText().toString().trim());
        int numOfBathrooms = Integer.parseInt(saleBathrooms.getText().toString().trim());
        int numOfBalconies = Integer.parseInt(saleBalconies.getText().toString().trim());
        int floor = Integer.parseInt(saleFloor.getText().toString().trim());
        int floorSpace = Integer.parseInt(saleFloorSpace.getText().toString().trim());

        Boolean gasAvail = saleGas.isChecked();
        Boolean elevatorAvail = saleElevator.isChecked();
        Boolean generatorAvail = saleGenerator.isChecked();
        Boolean garageAvail = saleGarage.isChecked();
        Boolean securityAvail = saleSecurity.isChecked();

        int payAmount = Integer.parseInt(salePayment.getText().toString().trim());

        String saleDesc = saleDescription.getText().toString().trim();

        String situation;
        int checked = saleSituation.getCheckedRadioButtonId();

        if(checked == R.id.rbNew) {
            situation = "New";
        } else {
            situation = "Established";
        }

        SaleAdvertisement sale = new SaleAdvertisement(areaName, fullAddress, "Sale",
                numOfBedrooms, numOfBathrooms, gasAvail, payAmount,
                numOfBalconies, floor, floorSpace, elevatorAvail, generatorAvail,
                garageAvail,  imageUri.size(),situation, saleDesc, securityAvail);


        sale.setAdvertiserUID(userService.getUserUID());
        sale.setLatitude(CreateSalePostFragment.this.latitude);
        sale.setLongitude(CreateSalePostFragment.this.longitude);

        return sale;
    }
    private Boolean checkData() {
        Log.i(TAG, "in check data");
        boolean dataOkay = true;
        EditText[] allEditTexts = {saleAreaName, saleFullAddress, saleBedrooms, saleBathrooms, saleBalconies,
                saleFloor, saleFloorSpace,salePayment, saleDescription};

        for(EditText e: allEditTexts) {
            if(e.getText().toString().trim().isEmpty()) {
                e.setError("Field cannot be empty");
                dataOkay = false;
            }
        }

        int picked = saleSituation.getCheckedRadioButtonId();

        if(picked != R.id.rbNew && picked != R.id.rbEstablished) {
            Toast.makeText(this.requireContext().getApplicationContext(), "You must pick situation of your property.", Toast.LENGTH_SHORT).show();
            dataOkay = false;
        }

        if(this.imageUri.isEmpty() && !EDIT_MODE) {
            dataOkay = false;
            Toast.makeText(this.requireContext().getApplicationContext(), "You must add photos to your post.", Toast.LENGTH_SHORT).show();
        }

        if(EDIT_MODE && this.imageUri.isEmpty() && saleAd.getUrlToImages().isEmpty()) {
            dataOkay = false;
            Toast.makeText(this.requireContext().getApplicationContext(), "You must add photos to your post.", Toast.LENGTH_SHORT).show();
        }

        return dataOkay;
    }

    @Override
    public void onCreateContextMenu(@NonNull @NotNull ContextMenu menu, @NonNull @NotNull View v, @Nullable @org.jetbrains.annotations.Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        requireActivity().getMenuInflater().inflate(R.menu.remove_photo_2, menu);
    }

    @Override
    public void onPhotoClick(int position) {
        setAdapterPosition(position);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.remove_photo_2) {
            if (prevPhotoAdapterPosition == -1) {
                Log.i(TAG, "nwe photo adapter postition = " + getAdapterPosition() + "image uri size = " + imageUri.size());
                imageUri.remove(getAdapterPosition());
                imageRecVA.setUrlArrayList(imageUri);
                imageRecVA.notifyDataSetChanged();
            } else {
                Log.i(TAG, "prev photo adapter postition = " + prevPhotoAdapterPosition);
                saleAd.getUrlToImages().remove(prevPhotoAdapterPosition);
                saleAd.setNumberOfImages(saleAd.getNumberOfImages() - 1);
                prevPhotoRecVA.setUrlArrayList(saleAd.getUrlToImages());
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