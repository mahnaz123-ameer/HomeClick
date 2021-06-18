package csedu.homeclick.androidhomeclick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.sign_in_fragments.SignInFragmentAdapter;
import csedu.homeclick.androidhomeclick.handler.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.handler.TabLayoutHandler;
import csedu.homeclick.androidhomeclick.handler.TopAppBarHandler;
import csedu.homeclick.androidhomeclick.structure.Advertisement;

public class ShowAdvertisementDetails extends AppCompatActivity implements Serializable {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_advertisement_details);

        final Advertisement ad = (Advertisement) getIntent().getExtras().get("Ad");



            TextView name = findViewById(R.id.show_ad_post_by_name);
            name.setText(ad.getAdvertiserName());
            final String phone_number = ad.getAdvertiserPhoneNumber();


            CardView cardView = findViewById(R.id.call_card);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if(phone_number == null) return;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Log.i("show ad","tel:" + (String) phone_number);
                    intent.setData(Uri.parse("tel:" + (String)phone_number));
                    startActivity(intent);
                }
            });

        Log.i("Show Ad Details", getIntent().getExtras().get("Ad").toString());

        bindWidgets();

    }

    private void bindWidgets() {

        toolbar = findViewById(R.id.app_toolbaar);

        BottomNavBarHandler.setInstance(findViewById(R.id.bottom_navigation_bar), R.id.account);
        BottomNavBarHandler.handle(this);

        TopAppBarHandler.getInstance(toolbar, this).handle();
    }
}