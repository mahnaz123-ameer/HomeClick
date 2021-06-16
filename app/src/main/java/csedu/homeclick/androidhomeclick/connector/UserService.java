package csedu.homeclick.androidhomeclick.connector;

import android.content.Context;
import android.content.Intent;

import csedu.homeclick.androidhomeclick.database.UserAuth;
import csedu.homeclick.androidhomeclick.structure.User;

public class UserService {

    public UserService() {
        UserAuth.setInstance();
    }

    public Boolean isSignedIn() {
        return UserAuth.isSignedIn();
    }

    public void signOut(Context context) {
        UserAuth.signOut(context);
    }

    public void signInNewUser(User user, Context context) {
        UserAuth.sendSignInLink(user, context);
    }

    public void signIn(String emailAddress, Context context) {
        UserAuth.sendSignInLink(emailAddress, context);
    }

    public void completeSignIn(Intent intent, Context context) {
        UserAuth.completeSignIn(intent, context);
    }

    public String getUserUID() {
        return UserAuth.getCurrentUserUID();
    }
}
