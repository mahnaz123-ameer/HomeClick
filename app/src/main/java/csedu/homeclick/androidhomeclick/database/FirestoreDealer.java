package csedu.homeclick.androidhomeclick.database;


import android.net.Uri;
import android.util.Log;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import csedu.homeclick.androidhomeclick.connector.AdInterface;
import csedu.homeclick.androidhomeclick.connector.UserInterface;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.RentAdvertisement;
import csedu.homeclick.androidhomeclick.structure.SaleAdvertisement;
import csedu.homeclick.androidhomeclick.structure.User;

public class FirestoreDealer implements AdInterface, UserInterface {
    public static final String TAG = "FirestoreDealer";
    public static final String AD_TABLE = "Advertisement";
    public static final String USER_TABLE = "Users";
    public static final String UPLOADS_FOLDER = "uploads";
    public static final String SERVER_ENTRY_TIME = "postDate";

    private static FirestoreDealer fd;
    private final FirebaseFirestore db;

    private QueryDocumentSnapshot lastLoadedAdSnapshot;


    private FirestoreDealer() {
        db = FirebaseFirestore.getInstance();
        lastLoadedAdSnapshot = null;
    }

    public static FirestoreDealer getInstance() {
        if(fd == null) {
            fd = new FirestoreDealer();
        }
        return fd;
    }

    public void addUser(User userMap) {
        if(UserAuth.isSignedIn()) {
            db.collection(USER_TABLE).document(Objects.requireNonNull(UserAuth.getCurrentUserUID())).set(userMap)
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
        DocumentReference docRef = db.collection(USER_TABLE).document(UID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    try {
                        onUserInfoListener.OnUserInfoFound(task.getResult().toObject(User.class));
                    } catch(Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                } else {
                    if(task.getException() != null)
                        Log.i(TAG,  task.getException().getMessage());
                }

            }
        });
    }

    @Override
    public void updateUserInfo(OnUserInfoUpdateListener<User> onUserInfoUpdateListener, User updatedUser) {
        DocumentReference docRef = db.collection(USER_TABLE).document(updatedUser.getUID());
        docRef.set(updatedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public void getAdId(final OnAdIdListener<Advertisement> onAdIdListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("UserUID", UserAuth.getCurrentUserUID());

        db.collection("AdID").add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String adID = documentReference.getId();
                        onAdIdListener.onAdIdObtained(adID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                });
    }

    @Override
    public void setLastFetchedQuerySnapshot(QueryDocumentSnapshot querySnapshot) {
        this.lastLoadedAdSnapshot = querySnapshot;
    }

    @Override
    public void uploadPhoto(Uri uri, String fileExtension, String pathId, OnPhotoUploadListener<String> onPhotoUploadListener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String fileName = System.currentTimeMillis() + "." + fileExtension;
        StorageReference ref = storage.getReference(UPLOADS_FOLDER).child(pathId).child(fileName);

        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                onPhotoUploadListener.onCompleteNotify(uri.toString());
                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                        double percent = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        onPhotoUploadListener.ongoingProgress((int) percent);
                    }
                });
    }

    @Override
    public void pushAdIntoDatabase(final Advertisement advert, final OnAdPostSuccessListener<Boolean> onAdPostSuccessListener) {
        db.collection(AD_TABLE).document(advert.getAdvertisementID()).set(advert)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onAdPostSuccessListener.OnAdPostSuccessful(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        onAdPostSuccessListener.OnAdPostFailed(e.getMessage());
                    }
                });
    }

    @Override
    public void getAdsFromDatabase(final OnAdsFetchedListener<List<Advertisement>> onAdsFetchedListener) {
        Task<QuerySnapshot> getTenAds;

        if(this.lastLoadedAdSnapshot == null) {
            getTenAds = db.collection(AD_TABLE).orderBy(SERVER_ENTRY_TIME, Query.Direction.DESCENDING).limit(10)
                    .get();

            getTenAds
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<Advertisement> adList = new ArrayList<>();
                    int total = queryDocumentSnapshots.size();
                    int count = 0;
                    for(QueryDocumentSnapshot singleAd: queryDocumentSnapshots) {
                        count++;
                        adList.add(singleAd.toObject(Advertisement.class));
                        if(count == total) {
                            FirestoreDealer.this.setLastFetchedQuerySnapshot(singleAd);
                            Log.i(TAG, "snapshot change detected, size = " + count);
                        }
                    }

                    onAdsFetchedListener.OnAdsFetchedListener(adList);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            onAdsFetchedListener.OnAdFetchingFailedListener(e.getMessage());
                        }
                    });
        } else {
            getTenAds = db.collection(AD_TABLE)
                    .orderBy(SERVER_ENTRY_TIME, Query.Direction.DESCENDING).startAfter(lastLoadedAdSnapshot).limit(10)
                    .get();

            getTenAds
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<Advertisement> adList = new ArrayList<>();

                    int total = queryDocumentSnapshots.size();
                    int count = 0;

                    for(QueryDocumentSnapshot singleAd: queryDocumentSnapshots) {
                        count++;
                        adList.add(singleAd.toObject(Advertisement.class));
                        if(count == total) {
                            Log.i(TAG, "snapshot change detected, size = " + count);
                            FirestoreDealer.this.setLastFetchedQuerySnapshot(singleAd);
                        }
                    }

                    onAdsFetchedListener.OnAdsFetchedListener(adList);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            onAdsFetchedListener.OnAdFetchingFailedListener(e.getMessage());
                        }
                    });

        }
    }

    @Override
    public void getMyAds(OnPersonalAdsFetchedListener<List<Advertisement>> onPersonalAdsFetchedListener, final String UserUID) {
        Task<QuerySnapshot> fetchingPersonalAds = db.collection(AD_TABLE).orderBy(SERVER_ENTRY_TIME, Query.Direction.DESCENDING).whereEqualTo("advertiserUID", UserUID).get();

        fetchingPersonalAds
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Advertisement> adList = new ArrayList<>();

                        for(QueryDocumentSnapshot singleAd: queryDocumentSnapshots) {
                            adList.add(singleAd.toObject(Advertisement.class));
                        }

                        onPersonalAdsFetchedListener.OnPersonalAdsFetched(adList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                });
    }

    @Override
    public void getThisRentAd(final OnParticularAdFetchedListener<RentAdvertisement> onParticularAdFetchedListener, String advertID) {
        db.collection(AD_TABLE).document(advertID).get()
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
        db.collection(AD_TABLE).document(advertID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            onParticularAdFetchedListener.OnParticularAdFetched(task.getResult().toObject(SaleAdvertisement.class));
                        }
                    }
                });
    }

    @Override
    public void deletePhotoFolder(final String folderName, final int toDelete, final OnPhotoFolderDeletedListener<Boolean> onPhotoFolderDeletedListener) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference ref = firebaseStorage.getReference(UPLOADS_FOLDER).child(folderName);
        Log.i(TAG, "folderName = " + folderName);
//        final List<StorageReference> listToDelete = new ArrayList<>();
        ref.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                final int[] count = new int[1];
                count[0] = -1;
                for( StorageReference item: listResult.getItems() ) {
                    count[0]++;
                    item.delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    onPhotoFolderDeletedListener.OnPhotoFolderDeleted(true, Integer.toString( count[0] ));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    onPhotoFolderDeletedListener.OnPhotoFolderDeleted(false, e.getMessage() );
                                }
                            });
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        onPhotoFolderDeletedListener.OnPhotoFolderDeleted(false, e.getMessage());
                    }
                });
    }

    @Override
    public void deleteParticularAd(String id, OnAdDeletedListener<Boolean> onAdDeletedListener) {
        DocumentReference docRef = db.collection(AD_TABLE).document(id);
        final DocumentReference adIdRef = db.collection("AdID").document(id);
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        adIdRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        onAdDeletedListener.OnAdDeleted(true, "");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        onAdDeletedListener.OnAdDeleted(false, e.getMessage());
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        onAdDeletedListener.OnAdDeleted(false, e.getMessage());
                    }
                });
    }

    @Override
    public void editParticularAd(final String id, final Advertisement advertisement, final OnAdEditListener<Boolean> onAdEditListener) {
        DocumentReference docRef = db.collection(AD_TABLE).document(id);

        docRef.set(advertisement)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onAdEditListener.OnAdEdited(true, "");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        onAdEditListener.OnAdEdited(false, e.getMessage());
                    }
                });
    }

}
