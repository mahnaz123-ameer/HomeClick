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
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import csedu.homeclick.androidhomeclick.database.FirestoreDealer;
import csedu.homeclick.androidhomeclick.database.UserAuth;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.User;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ActionCodeSettings actionCodeSettings;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //amake sign in korte dey kina
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "nnnawar@gmail.com";
                UserAuth.sendSignInLink(email, getApplicationContext());
            }
        });


            UserAuth.completeSignIn(getIntent(), getApplicationContext());


        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "nnnawar@gmail.com";
                UserAuth.sendSignInLink(new User("nashmin", email, "01715048076"), getApplicationContext());
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "nawar.nashmin@gmail.com";
                UserAuth.sendSignInLink(email, getApplicationContext());
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "nawar.nashmin@gmail.com";
                UserAuth.sendSignInLink(new User("etao nashmin", email, "01852967435"), getApplicationContext());
            }
        });


        UserAuth.completeSignIn(getIntent(), getApplicationContext());
    }
}

/*
* email already exists does this work
*
*
*
*
*
* */

//
//        firebaseAuth = FirebaseAuth.getInstance();
//                Log.i("firebaseauth", firebaseAuth.toString());
//                firebaseFirestore = FirebaseFirestore.getInstance();
//
//                actionCodeSettings =
//                ActionCodeSettings.newBuilder()
//                // URL you want to redirect back to. The domain (www.example.com) for this
//                // URL must be whitelisted in the Firebase Console.
//                .setUrl("http://homeclick-296f6.firebaseapp.com/")
//                // This must be true
//                .setHandleCodeInApp(true)
//                .setAndroidPackageName(
//                "csedu.homeclick.androidhomeclick",
//                true, /* installIfNotAvailable */
//                "1.0"    /* minimumVersion */)
//                .build();
//
//                String email = "nnnawar@gmail.com";
//
//                button.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings)
//        .addOnSuccessListener(new OnSuccessListener<Void>() {
//@Override
//public void onSuccess(Void unused) {
//        Log.i("send link", "successful");
//        }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
//        Log.i("send link", "failure " + e.getMessage());
//        }
//        });
//        }
//        });
//
//        Uri link = getIntent().getData();
//        if(link != null) {
//        firebaseAuth.signInWithEmailLink(email, link.toString())
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull @NotNull Exception e) {
//        Log.i("sign in", "sign in failed" + e.getMessage());
//        }
//        })
//        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//@Override
//public void onSuccess(AuthResult authResult) {
//        Log.i("sign in", "sign in successfull");
//        HashMap<String, Object> userMap = new HashMap<>();
//        userMap.put("name", "Mahnaz");
//        userMap.put("email", email);
//        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(userMap)
//        .addOnSuccessListener(new OnSuccessListener<Void>() {
//@Override
//public void onSuccess(Void unused) {
//        Log.i("database", "successful set");
//        }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull @NotNull Exception e) {
//        Log.i("database", "failure");
//        }
//        });
//        userMap.put("uid", firebaseAuth.getCurrentUser().getUid());
//        firebaseFirestore.collection("Users").add(userMap)
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull @NotNull Exception e) {
//        Log.i("database add", "fail korse");
//        }
//        })
//        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//@Override
//public void onSuccess(DocumentReference documentReference) {
//        Log.i("database add", "add hoise, set na");
//        }
//        });
//
//        }
//        });
//        }
