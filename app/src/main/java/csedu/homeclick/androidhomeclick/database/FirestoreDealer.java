package csedu.homeclick.androidhomeclick.database;

import android.util.Log;

import androidx.annotation.NonNull;

import csedu.homeclick.androidhomeclick.structure.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class FirestoreDealer {
    private static FirestoreDealer fd;
    private final FirebaseFirestore db;
    private static Boolean emailExists;
    private static Boolean addUserSuccess;

    private FirestoreDealer() {
        db = FirebaseFirestore.getInstance();
    }

    public static FirestoreDealer getInstance() {
        if(fd == null) {
            fd = new FirestoreDealer();
        }
        return fd;
    }

    public Boolean addUser(HashMap<String, Object> userMap) {
        addUserSuccess = true;
        if(UserAuth.isSignedIn()) {
            db.collection("Users").document(UserAuth.getCurrentUserUID()).set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            addUserSuccess = false;
                        }
                    });
        }
        return addUserSuccess;
    }
}
