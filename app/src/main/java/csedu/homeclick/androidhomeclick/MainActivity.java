package csedu.homeclick.androidhomeclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import csedu.homeclick.androidhomeclick.database.FirestoreDealer;
import csedu.homeclick.androidhomeclick.database.UserAuth;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.User;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ActionCodeSettings actionCodeSettings;
    private Button button;

    private String TAG = "MainActivity";

    CollectionReference cr = FirebaseFirestore.getInstance().collection("Advertisement");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Query q = cr.whereEqualTo("adType", "Rent").orderBy("paymentAmount", Query.Direction.ASCENDING).whereLessThanOrEqualTo("paymentAmount", 10000);
        q = q.whereEqualTo("numberOfBathrooms", 1).whereEqualTo("numberOfBedrooms", 1).whereEqualTo("gasAvailability", true);
        q.orderBy("postDate", Query.Direction.DESCENDING).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i(TAG, e.getMessage());
            }
        })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Advertisement> ads = new ArrayList<>();

                        for(QueryDocumentSnapshot qs: queryDocumentSnapshots) {
                            ads.add(qs.toObject(Advertisement.class));
                            Log.i(TAG, qs.toObject(Advertisement.class).getAdType());
                            Log.i(TAG, "" + qs.toObject(Advertisement.class).getPaymentAmount());
                            Log.i(TAG, "" + qs.toObject(Advertisement.class).getNumberOfBedrooms());
                            Log.i(TAG, "" + qs.toObject(Advertisement.class).getNumberOfBathrooms());
                            Log.i(TAG, "" + qs.toObject(Advertisement.class).getGasAvailability());
                        }

                        Log.i(TAG, "" + ads.size());

                    }
                });
//        Query q2 = cr.whereEqualTo("adType", "rent").orderBy("paymentAmount", Query.Direction.ASCENDING).whereLessThanOrEqualTo("paymentAmount", 10000);
//        q2 = q2.orderBy("numberOfBedrooms").whereGreaterThanOrEqualTo("numberOfBedrooms", 2);
//        q2 = q2.orderBy("numberOfBathrooms").whereGreaterThanOrEqualTo("numberOfBathrooms", 1);
//        q2 = q2.whereEqualTo("gasAvailability", true);
//        q2.get().addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                Log.i(TAG, e.getMessage());
//            }
//        })
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        List<Advertisement> ads = new ArrayList<>();
//
//                        for(QueryDocumentSnapshot qs: queryDocumentSnapshots) {
//                            ads.add(qs.toObject(Advertisement.class));
//                            Log.i(TAG, qs.toObject(Advertisement.class).getAdType());
//                            Log.i(TAG, "" + qs.toObject(Advertisement.class).getPaymentAmount());
//                            Log.i(TAG, "" + qs.toObject(Advertisement.class).getNumberOfBedrooms());
//                            Log.i(TAG, "" + qs.toObject(Advertisement.class).getNumberOfBathrooms());
//                            Log.i(TAG, "" + qs.toObject(Advertisement.class).getGasAvailability());
//                        }
//
//                        Log.i(TAG, "" + ads.size());
//
//                    }
//                });

    }
}