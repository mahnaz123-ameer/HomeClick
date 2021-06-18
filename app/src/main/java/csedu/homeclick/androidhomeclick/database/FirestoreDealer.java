package csedu.homeclick.androidhomeclick.database;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.connector.UserInterface;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.RentAdvertisement;
import csedu.homeclick.androidhomeclick.structure.SaleAdvertisement;
import csedu.homeclick.androidhomeclick.structure.User;

public class FirestoreDealer implements AdInterface, UserInterface {
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
    public void updateUserInfo(OnUserInfoUpdateListener<User> onUserInfoUpdateListener, User updatedUser) {
        DocumentReference docRef = db.collection("Users").document(updatedUser.getUID());
        docRef.update(updatedUser.getUserHashMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()) {
                    onUserInfoUpdateListener.OnUserInfoUpdated();
                } else {
                    onUserInfoUpdateListener.OnUserInfoUpdateFailed();
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i("createpost", e.getMessage());
                    }
                });
    }

    @Override
    public void getAdsFromDatabase(final OnAdsFetchedListener<List<Advertisement>> onAdsFetchedListener) {
        db.collection("Advertisement")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Advertisement> adList = new ArrayList<>();

                        for(QueryDocumentSnapshot singleAd: queryDocumentSnapshots) {
                            adList.add(singleAd.toObject(Advertisement.class));
                        }

                        onAdsFetchedListener.OnAdsFetchedListener(adList);
                    }
                });
    }

    @Override
    public void getMyAds(OnPersonalAdsFetchedListener<List<Advertisement>> onPersonalAdsFetchedListener, final String UserUID) {
        db.collection("Advertisement").whereEqualTo("advertiserUID", UserUID).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Advertisement> adList = new ArrayList<>();

                        for(QueryDocumentSnapshot singleAd: queryDocumentSnapshots) {
                            adList.add(singleAd.toObject(Advertisement.class));
                        }

                        onPersonalAdsFetchedListener.OnPersonalAdsFetchedListener(adList);
                    }
                });
    }

    @Override
    public void getThisRentAd(final OnParticularAdFetchedListener<RentAdvertisement> onParticularAdFetchedListener, String advertID) {
        db.collection("Advertisement").document(advertID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            onParticularAdFetchedListener.OnParticularAdFetched(task.getResult().toObject(RentAdvertisement.class));
                        }
                    }
                });
    }

    @Override
    public void getThisSaleAd(OnParticularAdFetchedListener<SaleAdvertisement> onParticularAdFetchedListener, String advertID) {

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
