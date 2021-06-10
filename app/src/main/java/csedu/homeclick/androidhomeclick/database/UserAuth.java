package csedu.homeclick.androidhomeclick.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.jetbrains.annotations.NotNull;

import csedu.homeclick.androidhomeclick.structure.User;


//TODO: all issues fixed, never touch this again

public class UserAuth {
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

    public static ActionCodeSettings getActionCodeSettings() {
        return actionCodeSettings;
    }

    public static String getPackageName() {
        return PACKAGE_NAME;
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

    //TODO: make this private later on == handled differently now
//    public static Boolean emailAlreadyExists(String email, Context context) {
//        setInstance();
//
//
//        firebaseAuth.fetchSignInMethodsForEmail(email)
//                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
//                        //TODO: Check later
//                        Log.i("sign in method size", "" + task.getResult().getSignInMethods().size());
//                        if (task.getResult().getSignInMethods().size() != 0) {
//
//                        } else {
//                            Log.i("fail", "couldn't fetch anything");
//                        }
//
//                    }
//                });
//    }


    public static void sendSignInLink(String email, Context context) {
        setInstance();
//        if (!emailAlreadyExists(email, context)) {
//            Toast.makeText(context, "Email does not exist. Please register", Toast.LENGTH_LONG).show();
//            return false;
//        }

        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        //TODO: Check later
                        Log.i("sign in method size", "" + task.getResult().getSignInMethods().size());
                        if (task.getResult().getSignInMethods().size() != 0) {
                            firebaseAuth.sendSignInLinkToEmail(email, UserAuth.getActionCodeSettings())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.i("email link", "link sent to email");
                                            SharedPreferences sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
                                            sharedPreferences.edit().putString("emailForSignIn", email).apply();
                                            sharedPreferences.edit().putBoolean("newUser", false).apply();
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "New user please sign up first.", Toast.LENGTH_SHORT).show();
                            Log.i("fail", "couldn't fetch anything");
                        }

                    }
                });

    }

    public static void sendSignInLink(User user, Context context) {
        setInstance();

//        if(emailAlreadyExists(email, context)) {
//            Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show();
//            return false;
//        }


        firebaseAuth.fetchSignInMethodsForEmail(user.getEmailAddress())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        //TODO: Check later
                        Log.i("sign in method size", "" + task.getResult().getSignInMethods().size());
                        if (task.getResult().getSignInMethods().size() != 0) {
                            Toast.makeText(context, "User already exists. Sign in instead.", Toast.LENGTH_SHORT).show();
                        } else {
                            firebaseAuth.sendSignInLinkToEmail(user.getEmailAddress(), UserAuth.getActionCodeSettings())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.i("email link", "link sent to email");
                                            Toast.makeText(context, "Link sent to email. Use it to sign in.", Toast.LENGTH_SHORT).show();
                                            SharedPreferences sharedPreferences = context.getSharedPreferences(UserAuth.getPackageName(), Context.MODE_PRIVATE);
                                            sharedPreferences.edit().putString("emailForSignIn", user.getEmailAddress()).apply();
                                            sharedPreferences.edit().putString("nameForSignUp", user.getName()).apply();
                                            sharedPreferences.edit().putString("phoneNumberForSignUp", user.getPhoneNumber()).apply();
                                            sharedPreferences.edit().putBoolean("newUser", true).apply();
                                            String signInUrl = UserAuth.getActionCodeSettings().getUrl();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(context, "Could not send link. Please try again.", Toast.LENGTH_SHORT).show();
                                            Log.i("signinlink", "sign in link send failure");
                                        }
                                    });
                        }

                    }
                });

//        firebaseAuth.sendSignInLinkToEmail(user.getEmailAddress(), actionCodeSettings)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.i("email link", "link sent to email");
//                        SharedPreferences sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
//                        sharedPreferences.edit().putString("emailForSignIn", user.getEmailAddress()).apply();
//                        sharedPreferences.edit().putString("nameForSignUp", user.getName()).apply();
//                        sharedPreferences.edit().putString("phoneNumberForSignUp", user.getPhoneNumber()).apply();
//                        sharedPreferences.edit().putBoolean("newUser", true).apply();
//                        String signInUrl = actionCodeSettings.getUrl();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Toast.makeText(context, "Could not send link. Please try again.", Toast.LENGTH_SHORT).show();
//                        Log.i("signinlink", "sign in link send failure");
//                    }
//                });
    }

    public static Boolean isSignedIn() {
        setInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            return false;
        }
        return true;
    }

    public static String getCurrentUserUID() {
        setInstance();
        if (isSignedIn()) {
            return firebaseAuth.getCurrentUser().getUid();
        }
        return null;
    }

    public static void completeSignIn(Intent intent, Context context) {
        setInstance();
        Uri link = intent.getData();

        if (link != null) {
            String emailLink = link.toString();
            String email = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("emailForSignIn", "");

            if (!email.isEmpty()) {
                firebaseAuth.signInWithEmailLink(email, emailLink)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(context, "Successfully signed in.", Toast.LENGTH_SHORT).show();

                                Boolean newUser = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getBoolean("newUser", false);
                                if (newUser) {
                                    addUserToDatabase(context);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(context, "Link has expired or already been used.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }

    }

    private static void addUserToDatabase(Context context) {
        String email = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("emailForSignIn", "");
        String name = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("nameForSignUp", "");
        String phoneNumber = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).getString("phoneNumberForSignUp", "");
        User newUserAdd = new User(name, email, phoneNumber, firebaseAuth.getCurrentUser().getUid());
        FirestoreDealer.getInstance().addUser(newUserAdd.getUserHashMap());

        clearSharedPreferences(context);
    }

    private static void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    public static void signOut(Context context) {
        setInstance();
        firebaseAuth.signOut();
        clearSharedPreferences(context);
    }
}


/*
 * shared preferences = emailForSignIn
 * newUser boolean
 * phoneNumberForSignUp
 * nameForSignUp
 *
 *
 *
 * */



