package csedu.homeclick.androidhomeclick.activities.create_post;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;



import java.util.ArrayList;
import java.util.List;

import csedu.homeclick.androidhomeclick.R;

import csedu.homeclick.androidhomeclick.navigator.BottomNavBarHandler;
import csedu.homeclick.androidhomeclick.navigator.TopAppBarHandler;

public class AddPhotos extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener  {

    private Toolbar toolbar;

    Button select, previous, next, goBack;
    ImageSwitcher imageView;

    TextView total;
    final ArrayList<Uri>  mArrayUri = new ArrayList<>();
    final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), new ActivityResultCallback<List<Uri>>() {
        @Override
        public void onActivityResult(List<Uri> result) {
            AddPhotos.this.mArrayUri.addAll(result);
            if (!AddPhotos.this.mArrayUri.isEmpty())
                Glide.with(AddPhotos.this).load(mArrayUri.get(0)).into((ImageView) imageView.getCurrentView());
        }
    });
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);

        bindWidgets();


        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        select.setOnClickListener(this);
        next.setOnLongClickListener(this);
    }

    private void bindWidgets() {
        BottomNavBarHandler.setInstance(findViewById(R.id.add_photos_bottom_nav_bar), R.id.add);
        BottomNavBarHandler.handle(this);

        toolbar = findViewById(R.id.app_toolbaar);
        TopAppBarHandler.getInstance(toolbar, this).handle();

        select = findViewById(R.id.select);
        total = findViewById(R.id.text);
        imageView = findViewById(R.id.image);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        goBack = findViewById(R.id.GoBack);

        imageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                return imageView1;
            }
        });


    }




    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(getApplicationContext(), "Long click", Toast.LENGTH_SHORT).show();
        return false;
    }




    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.previous:
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    Glide.with(AddPhotos.this).load(mArrayUri.get(position)).into((ImageView) imageView.getCurrentView());
                }
                break;
            case R.id.next:
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    Glide.with(AddPhotos.this).load(mArrayUri.get(position)).into((ImageView) imageView.getCurrentView());
                } else {
                    Toast.makeText(AddPhotos.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.select:
                mGetContent.launch("image/*");
                break;
            default:
                break;
        }
    }

}
