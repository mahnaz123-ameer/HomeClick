package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.navigator.TopAppBarHandler;
import csedu.homeclick.androidhomeclick.structure.FilterCriteria;

public class Filter extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Filter";
    private Toolbar toolbar;

    private EditText paymentAmount, bedroom, bathroom;
    private CheckBox gasAvailability;
    private RadioGroup adType;
    private RadioButton rent, sale, any;
    private Button applyFilter;

    private FilterCriteria filterCriteria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        bindWidgets();
        applyFilter.setOnClickListener(this);
    }

    private void bindWidgets() {
        paymentAmount = findViewById(R.id.filter_max_pay_amount);
        bedroom = findViewById(R.id.filter_num_of_bedrooms);
        bathroom = findViewById(R.id.filter_num_of_bathrooms);
        gasAvailability = findViewById(R.id.filter_gas_availability);
        adType = findViewById(R.id.choose_ad_type_radio_group);
        rent = findViewById(R.id.choose_rent);
        sale = findViewById(R.id.choose_sale);
        any = findViewById(R.id.choose_any);
        applyFilter = findViewById(R.id.apply_filter);

        filterCriteria = new FilterCriteria();

        BottomNavBarHandler.setInstance(findViewById(R.id.filter_bottom_nav_bar), R.id.filter);
        BottomNavBarHandler.handle(this);

        toolbar = findViewById(R.id.app_toolbaar);
        TopAppBarHandler.getInstance(toolbar, this).handle();
    }

    @Override
    public void onClick(View v) {
        if(noneSelected()) {
            Log.i(TAG, "none selected");
            startActivity(new Intent(getApplicationContext(), AdFeed.class));
        } else {
            createFilterCriteria();
            Intent targetIntent = new Intent(getApplicationContext(), AdFeed.class);
            targetIntent.putExtra("FilterCriteria", this.filterCriteria);
            startActivity(targetIntent);
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
            default:
                break;
        }

        if(!paymentAmount.getText().toString().isEmpty()) {
            int amount = Integer.parseInt( paymentAmount.getText().toString() );
            filterCriteria.setPaymentAmount(amount);
        }

        if(!bedroom.getText().toString().isEmpty()) {
            int bedroomNum = Integer.parseInt( bedroom.getText().toString() );
            filterCriteria.setNumberOfBedrooms(bedroomNum);
        }

        if(!bathroom.getText().toString().isEmpty()) {
            int bathroomNum = Integer.parseInt( bathroom.getText().toString() );
            filterCriteria.setNumberOfBathrooms(bathroomNum);
        }

        if(gasAvailability.isChecked()) {
            filterCriteria.setGasAvailability(true);
        }
    }

    private Boolean noneSelected() {
        Boolean noneSelect = true;
        EditText[] editTexts = {paymentAmount, bedroom, bathroom};
        for(EditText t: editTexts) {
            if(!t.getText().toString().isEmpty()) {
                Log.i(TAG, "edit tex selected");
                noneSelect = false;
            }
        }
        int checkId = adType.getCheckedRadioButtonId();
        if( checkId == rent.getId() || checkId == sale.getId()) {
            Log.i(TAG, "radio selected");
            noneSelect = false;
        }
        if(gasAvailability.isChecked()) {
            Log.i(TAG, "gas selected");
            noneSelect = false;
        }
        return noneSelect;
    }
}