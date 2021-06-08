package csedu.homeclick.androidhomeclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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

        firebaseAuth = FirebaseAuth.getInstance();
        Log.i("firebaseauth", firebaseAuth.toString());
        firebaseFirestore = FirebaseFirestore.getInstance();

        actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("http://homeclick-296f6.firebaseapp.com/")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                "csedu.homeclick.androidhomeclick",
                                true, /* installIfNotAvailable */
                                "1.0"    /* minimumVersion */)
                        .build();

        String email = "mahnazameer123@gmail.com";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("send link", "successful");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                                Log.i("send link", "failure " + e.getMessage());
                            }
                        });
            }
        });

        Uri link = getIntent().getData();
        if(link != null) {
            firebaseAuth.signInWithEmailLink(email, link.toString())
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.i("sign in", "sign in failed" + e.getMessage());
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.i("sign in", "sign in successfull");
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("name", "Mahnaz");
                            userMap.put("email", email);
                            firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.i("database", "successful set");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Log.i("database", "failure");
                                        }
                                    });
                            userMap.put("uid", firebaseAuth.getCurrentUser().getUid());
                            firebaseFirestore.collection("Users").add(userMap)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Log.i("database add", "fail korse");
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.i("database add", "add hoise, set na");
                                        }
                                    });

                        }
                    });
        }

    }
}