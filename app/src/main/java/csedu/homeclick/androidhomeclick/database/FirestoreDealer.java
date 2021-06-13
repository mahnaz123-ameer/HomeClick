package csedu.homeclick.androidhomeclick.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.User;

public class FirestoreDealer implements AdInterface {
    private static FirestoreDealer fd;
    private final FirebaseFirestore db;

    private FirestoreDealer() {
        db = FirebaseFirestore.getInstance();
    }

    public static FirestoreDealer getInstance() {
        if(fd == null) {
            fd = new FirestoreDealer();
        }
        return fd;
    }

    public void addUser(User userMap) {
        if(UserAuth.isSignedIn()) {
            db.collection("Users").document(UserAuth.getCurrentUserUID()).set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.i("add user", "successful");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.i("add user", "failed");
                        }
                    });
        }
    }

    @Override
    public void findUserInfo(OnUserInfoListener<User> onUserInfoListener, String UID) {
        DocumentReference docRef = db.collection("Users").document(UID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    onUserInfoListener.OnUserInfoFound(task.getResult().toObject(User.class));
                } else {

                }

            }
        });
    }

    @Override
    public void addAdvertisement(final OnAdPostSuccessListener<Advertisement> onAdPostSuccessListener, final Advertisement advertisement) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("UserUID", UserAuth.getCurrentUserUID());
        db.collection("AdID").add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String advertisementID = documentReference.getId();
                        advertisement.setAdvertisementID(advertisementID);

                        db.collection("Advertisement").document(advertisementID).set(advertisement)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onAdPostSuccessListener.OnAdPostSuccessful(true);
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        onAdPostSuccessListener.OnAdPostSuccessful(false);
                                    }
                                });
                    }
                });
    }


    //    public void getUser(String UID, final MainActivity activity) {
//        DocumentReference docRef = db.collection("Users").document(UID);
//        final User[] user = new User[1];
//
//                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        user[0] = documentSnapshot.toObject(User.class);
//                        activity.foundUser(user[0]);
//                    }
//                });
//    }

//    public void completeAdPost(final Advertisement rent, final String UID) {
//        DocumentReference docRef = db.collection("Users").document(UID);
//
//        final User[] user = new User[1];
//
//                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
//                        if(task.isSuccessful()) {
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                            user[0] = documentSnapshot.toObject(User.class);
//                            rent.setAdvertiserUID(UID);
//                            rent.setAdvertiserName(user[0].getName());
//                            rent.setAdvertiserPhoneNumber(user[0].getPhoneNumber());
//                            Log.i("user", "found user" + user[0].getName());
//
//                        } else {
//                            Log.i("user info", "failed for some reaosn");
//                        }
//                    }
//                });
//    }


}
