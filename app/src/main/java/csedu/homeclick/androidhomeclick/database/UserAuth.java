package csedu.homeclick.androidhomeclick.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import csedu.homeclick.androidhomeclick.structure.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class UserAuth {
    private static final String PACKAGE_NAME = "csedu.homeclick.androidhomeclick";
    private static UserAuth auth;
    private static FirebaseAuth firebaseAuth;
    private static ActionCodeSettings actionCodeSettings;

    private UserAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        setActionCodeSettings();
    }

    public static void setInstance() {
        if(auth == null)
            auth = new UserAuth();
    }

    private static void setActionCodeSettings() {
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
    }


    public static Boolean sendSignInLink(String email, Context context) {
        setInstance();
        if (!FirestoreDealer.getInstance().emailAlreadyExists(email)) {
            return false;
        }

        firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("email link", "link sent to email");
                        SharedPreferences sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("emailForSignIn", email).apply();
                    }
                });
        return true;
    }

    public static Boolean sendSignInLink(String email, String name, String phoneNumber, Context context) {
        setInstance();
        if(FirestoreDealer.getInstance().emailAlreadyExists(email)) {
            return false;
        }

        firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("email link", "link sent to email");
                        SharedPreferences sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("emailForSignIn", email).apply();
                        sharedPreferences.edit().putString("nameForSignUp", name).apply();
                        sharedPreferences.edit().putString("phoneNumberForSignUp", phoneNumber).apply();
                        sharedPreferences.edit().putBoolean("newUser", true).apply();
                    }
                });
        return true;
    }

    public static Boolean isSignedIn() {
        setInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            return false;
        }
        return true;
    }

    public static String getCurrentUserId() {
        setInstance();
        if(isSignedIn()) {
            return firebaseAuth.getCurrentUser().getUid();
        }
        return null;
    }

    public static void completeSignIn(Intent intent, Context context) {
        setInstance();
        Uri link = intent.getData();
        if(link != null) {
            String emailLink = link.toString();
            String email = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("emailForSignIn", "");

            if(!email.isEmpty()) {
                firebaseAuth.signInWithEmailLink(email, emailLink);
            }

            Boolean newUser = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getBoolean("newUser", false);
            if(newUser) {
                String name = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("nameForSignUp", "");
                String phoneNumber = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("phoneNumberForSignUp", "");
                User newUserAdd = new User(name, email, phoneNumber);
                FirestoreDealer.getInstance().addUser(newUserAdd.getUserHashMap());
            }

        }
    }

    public static void signOut() {
        setInstance();
        firebaseAuth.signOut();
    }
}



