package csedu.homeclick.androidhomeclick.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Objects;

import csedu.homeclick.androidhomeclick.connector.UserAuthInterface;
import csedu.homeclick.androidhomeclick.structure.User;




public class UserAuth implements UserAuthInterface {
    private static final String PACKAGE_NAME = "csedu.homeclick.androidhomeclick";
    private static UserAuth auth;
    private static FirebaseAuth firebaseAuth;
    private static ActionCodeSettings actionCodeSettings;


    private UserAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        if (actionCodeSettings == null) {
            setActionCodeSettings();
        }
    }

    public static void setInstance() {
        if (auth == null)
            auth = new UserAuth();
    }

    private static ActionCodeSettings getActionCodeSettings() {
        return actionCodeSettings;
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
                                PACKAGE_NAME,
                                true, /* installIfNotAvailable */
                                "1.0"    /* minimumVersion */)
                        .build();
    }

    public static Boolean isSignedIn() {
        setInstance();
        return firebaseAuth.getCurrentUser() != null;
    }

    public static String getCurrentUserUID() {
        setInstance();
        if (isSignedIn()) {
            return Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        }
        return null;
    }


    public static void completeSignIn(Intent intent, final Context context) {
        setInstance();
        Uri link = intent.getData();

        if (link != null) {
            String emailLink = link.toString();
            String email = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("emailForSignIn", "");

            if (!email.isEmpty()) {
                firebaseAuth.signInWithEmailLink(email, emailLink)
                        .addOnSuccessListener(authResult -> {
                            Toast.makeText(context, "Successfully signed in.", Toast.LENGTH_SHORT).show();

                            boolean newUser = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getBoolean("newUser", false);
                            if (newUser) {
                                addUserToDatabase(context);
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(context, "Link has expired or already been used.", Toast.LENGTH_SHORT).show());

            }
        }

    }

    private static void addUserToDatabase(final Context context) {
        String email = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("emailForSignIn", "");
        String name = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("nameForSignUp", "");
        String phoneNumber = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("phoneNumberForSignUp", "");
        User newUserAdd = new User(name, email, phoneNumber, Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
        FirestoreDealer.getInstance().addUser(newUserAdd);

        clearSharedPreferences(context);
    }

    @SuppressLint("ApplySharedPref")
    private static void clearSharedPreferences(final Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    public static void signOut(Context context) {
        setInstance();
        firebaseAuth.signOut();
        clearSharedPreferences(context);
    }

    public static UserAuth getInstance() {
        setInstance();
        return auth;
    }

    @Override
    public void signInNewUser(final User user, final Context context, final SendLinkToUserListener<User> sendLinkToUserListener) {
            setInstance();

            firebaseAuth.fetchSignInMethodsForEmail(user.getEmailAddress())
                    .addOnCompleteListener(task -> {

                        if(task.isSuccessful()) {
                            Log.i("sign in method size", "" + Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).size());

                            if (Objects.requireNonNull(task.getResult().getSignInMethods()).size() != 0) {
                                sendLinkToUserListener.OnSendLinkFailed("User already exists. Sign in instead.");
                            } else {
                                firebaseAuth.sendSignInLinkToEmail(user.getEmailAddress(), UserAuth.getActionCodeSettings())
                                        .addOnSuccessListener(unused -> {
                                            Log.i("email link", "link sent to email");
                                            SharedPreferences sharedPreferences = context.getSharedPreferences(UserAuth.PACKAGE_NAME, Context.MODE_PRIVATE);
                                            sharedPreferences.edit().putString("emailForSignIn", user.getEmailAddress()).apply();
                                            sharedPreferences.edit().putString("nameForSignUp", user.getName()).apply();
                                            sharedPreferences.edit().putString("phoneNumberForSignUp", user.getPhoneNumber()).apply();
                                            sharedPreferences.edit().putBoolean("newUser", true).apply();
                                            sendLinkToUserListener.OnSendLinkSuccessful("Sign in link sent to email. Use it to sign in.");
                                        })
                                        .addOnFailureListener(e -> sendLinkToUserListener.OnSendLinkFailed("Could not send link. Please try again."));
                            }
                        } else {
                            sendLinkToUserListener.OnSendLinkFailed(Objects.requireNonNull(task.getException()).getMessage());
                        }

                    });

    }

    @Override
    public void signInOldUser(final String emailAddress, final Context context, final SendLinkToUserListener<String> sendLinkToUserListener) {

            setInstance();

            firebaseAuth.fetchSignInMethodsForEmail(emailAddress)
                    .addOnCompleteListener(task -> {

                        Log.i("sign in method size", "" + Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).size());
                        if (Objects.requireNonNull(task.getResult().getSignInMethods()).size() != 0) {
                            firebaseAuth.sendSignInLinkToEmail(emailAddress, UserAuth.getActionCodeSettings())
                                    .addOnSuccessListener(unused -> {
                                        Log.i("email link", "link sent to email");
                                        SharedPreferences sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
                                        sharedPreferences.edit().putString("emailForSignIn", emailAddress).apply();
                                        sharedPreferences.edit().putBoolean("newUser", false).apply();
                                        sendLinkToUserListener.OnSendLinkSuccessful("Sign in link sent to email.");
                                    });
                        } else {
                            sendLinkToUserListener.OnSendLinkFailed("New user please sign up first.");
                            Log.i("fail", "couldn't fetch anything");
                        }

                    });


    }
}

