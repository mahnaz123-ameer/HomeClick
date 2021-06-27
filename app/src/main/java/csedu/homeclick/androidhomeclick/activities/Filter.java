package csedu.homeclick.androidhomeclick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.navigator.TopAppBarHandler;
import csedu.homeclick.androidhomeclick.structure.FilterCriteria;

public class Filter extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Filter";
    private Toolbar toolbar;

    private TextInputEditText paymentAmount;
    private EditText filter_no_of_bedrooms, filter_no_of_bathrooms;
    private CheckBox gasAvailability;
    private RadioGroup adType;
    private RadioButton rent, sale, any;
    private Button applyFilter;

    private FilterCriteria filterCriteria;


    private Button increase_bedrooms, decrease_bedrooms;
    int count_bedrooms;

    private Button increase_bathrooms, decrease_bathrooms;
    int count_bathrooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        bindWidgets();
        setClickOnListeners();
    }

    private void setClickOnListeners() {
        Log.i(TAG, "in set click listeners");
        applyFilter.setOnClickListener(this);
        increase_bedrooms.setOnClickListener(this);
        decrease_bedrooms.setOnClickListener(this);
        increase_bathrooms.setOnClickListener(this);
        decrease_bathrooms.setOnClickListener(this);
    }

    private void bindWidgets() {
        paymentAmount = findViewById(R.id.filter_max_pay_amount);
        gasAvailability = findViewById(R.id.filter_gas_availability);
        adType = findViewById(R.id.choose_ad_type_radio_group);
        rent = findViewById(R.id.choose_rent);
        sale = findViewById(R.id.choose_sale);
        any = findViewById(R.id.choose_any);
        applyFilter = findViewById(R.id.apply_filter);

        increase_bedrooms = findViewById(R.id.increase_bedrooms);
        decrease_bedrooms = findViewById(R.id.decrease_bedrooms);
        increase_bathrooms = findViewById(R.id.increase_bathrooms);
        decrease_bathrooms = findViewById(R.id.decrease_bathrooms);


        filter_no_of_bedrooms = findViewById(R.id.filter_num_of_bedrooms);
        filter_no_of_bathrooms = findViewById(R.id.filter_num_of_bathrooms);


        filterCriteria = new FilterCriteria();

        BottomNavBarHandler.setInstance(findViewById(R.id.filter_bottom_nav_bar), R.id.filter);
        BottomNavBarHandler.handle(this);

        toolbar = findViewById(R.id.app_toolbaar);
        TopAppBarHandler.getInstance(toolbar, this).handle();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_filter:
                if (noneSelected()) {
                    Log.i(TAG, "none selected");
                    startActivity(new Intent(getApplicationContext(), AdFeed.class));
                } else {
                    createFilterCriteria();
                    Intent targetIntent = new Intent(getApplicationContext(), AdFeed.class);
                    targetIntent.putExtra("FilterCriteria", this.filterCriteria);
                    startActivity(targetIntent);
                }
                break;
            case R.id.increase_bedrooms:
                count_bedrooms++;
                filter_no_of_bedrooms.setText(Integer.toString(count_bedrooms));
                break;
            case R.id.decrease_bedrooms:
                count_bedrooms--;
                filter_no_of_bedrooms.setText(Integer.toString(count_bedrooms));
                break;
            case R.id.increase_bathrooms:
                count_bathrooms++;
                filter_no_of_bathrooms.setText(Integer.toString(count_bathrooms));
                break;
            case R.id.decrease_bathrooms:
                count_bathrooms--;
                filter_no_of_bathrooms.setText(Integer.toString(count_bathrooms));
                break;
            default:
                break;
        }
    }

    private void createFilterCriteria() {
        int checkedId = adType.getCheckedRadioButtonId();

        switch (checkedId) {
            case R.id.choose_rent:
                filterCriteria.setAdType("Rent");
                break;
            case R.id.choose_sale:
                filterCriteria.setAdType("Sale");
                break;
        }

        if (!paymentAmount.getText().toString().isEmpty()) {
            int amount = Integer.parseInt(paymentAmount.getText().toString());
            filterCriteria.setPaymentAmount(amount);
        }

        if (!filter_no_of_bedrooms.getText().toString().isEmpty()) {
            int bedroomNum = Integer.parseInt(filter_no_of_bedrooms.getText().toString());
            filterCriteria.setNumberOfBedrooms(bedroomNum);
        }

        if (!filter_no_of_bathrooms.getText().toString().isEmpty()) {
            int bathroomNum = Integer.parseInt(filter_no_of_bathrooms.getText().toString());
            filterCriteria.setNumberOfBathrooms(bathroomNum);
        }

        if (gasAvailability.isChecked()) {
            filterCriteria.setGasAvailability(true);
        }
    }

    private Boolean noneSelected() {
        Boolean noneSelect = true;
        EditText[] editTexts = {paymentAmount, filter_no_of_bedrooms, filter_no_of_bathrooms};
        for (EditText t : editTexts) {
            if (!t.getText().toString().isEmpty()) {
                Log.i(TAG, "edit tex selected");
                noneSelect = false;
            }
        }
        int checkId = adType.getCheckedRadioButtonId();
        if (checkId == rent.getId() || checkId == sale.getId()) {
            Log.i(TAG, "radio selected");
            noneSelect = false;
        }
        if (gasAvailability.isChecked()) {
            Log.i(TAG, "gas selected");
            noneSelect = false;
        }
        return noneSelect;
    }
}