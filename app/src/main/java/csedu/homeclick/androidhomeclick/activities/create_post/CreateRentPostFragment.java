package csedu.homeclick.androidhomeclick.activities.create_post;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.connector.AdvertisementService;
import csedu.homeclick.androidhomeclick.connector.UserInterface;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.RentAdvertisement;
import csedu.homeclick.androidhomeclick.structure.User;


public class CreateRentPostFragment extends Fragment implements View.OnClickListener, CalendarView.OnDateChangeListener{
    private EditText rentAreaName, rentFullAddress, rentBedrooms, rentBathrooms, rentBalconies;
    private EditText rentFloor, rentFloorSpace, rentPayment, rentUtilityCharge, rentDescription;
    private CheckBox rentGas, rentElevator, rentGenerator, rentGarage, rentSecurity;

    private CalendarView rentAvailableFrom;

    private RadioGroup rentTenant;
    private RadioButton family, single;
    private Button postAd;

    private UserService userService;
    private AdvertisementService advertisementService;

    private final Date[] rentAvailFrom = new Date[1];

    public CreateRentPostFragment() {
        // Required empty public constructor
    }


    public static CreateRentPostFragment newInstance() {
        CreateRentPostFragment fragment = new CreateRentPostFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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

        rentAvailableFrom.setOnDateChangeListener(this::onSelectedDayChange);
        postAd.setOnClickListener(this);

        return view;
    }

    private void bindWidgets(View view) {
        userService = new UserService();
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
        rentPayment = view.findViewById(R.id.rentPayment);
        rentUtilityCharge = view.findViewById(R.id.rentUtilityCharge);
        rentAvailableFrom = view.findViewById(R.id.rentAvailableFrom);
        rentDescription = view.findViewById(R.id.rentDescription);
        rentTenant = view.findViewById(R.id.rdTenant);
        family = view.findViewById(R.id.rbFamily);
        single = view.findViewById(R.id.rbSinglePerson);
        postAd = view.findViewById(R.id.buttonRentPostAd);


        advertisementService = new AdvertisementService();
    }

    @Override
    public void onClick(View v) {
        if(checkData()) {
            final RentAdvertisement rentAd = makeAd();

            userService.findUserInfo(new UserInterface.OnUserInfoListener<User>() {
                @Override
                public void OnUserInfoFound(User data) {
                    rentAd.setAdvertiserName(data.getName());
                    rentAd.setAdvertiserPhoneNumber(data.getPhoneNumber());
                    rentAd.setAdvertiserUID(userService.getUserUID());

                    advertisementService.addAdvertisement(new AdInterface.OnAdPostSuccessListener<Advertisement>() {
                        @Override
                        public void OnAdPostSuccessful(Boolean data) {
                            if(data) {
                                Toast.makeText(v.getContext().getApplicationContext(), "Ad posted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(v.getContext().getApplicationContext(), "Error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, rentAd);
                }
            }, userService.getUserUID());
        }
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

        RentAdvertisement rent = new RentAdvertisement(areaName, fullAddress, "Rent", numOfBedrooms, numOfBathrooms, gasAvail, payAmount, numOfBalconies, floor, floorSpace, elevatorAvail, generatorAvail, garageAvail, tenantType, utilities, rentDesc, securityAvail, rentAvailFrom[0]);
        Log.i("date", rent.getAvailableFrom().toString());
        return rent;
    }

    private Boolean checkData() {
        Boolean dataOkay = true;
        EditText[] allEditTexts = {rentAreaName, rentFullAddress, rentBedrooms, rentBathrooms, rentBalconies, rentFloor, rentFloorSpace, rentPayment, rentUtilityCharge, rentDescription};

        for(EditText e: allEditTexts) {
            if(e.getText().toString().trim().isEmpty()) {
                e.setError("Field cannot be empty");
                dataOkay = false;
            }
        }

        int picked = rentTenant.getCheckedRadioButtonId();

        if(picked != R.id.rbFamily && picked != R.id.rbSinglePerson) {
            dataOkay = false;
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
}