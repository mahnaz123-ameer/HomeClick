package csedu.homeclick.androidhomeclick.connector;

import android.content.Context;
import android.content.Intent;

import csedu.homeclick.androidhomeclick.database.FirestoreDealer;
import csedu.homeclick.androidhomeclick.database.UserAuth;
import csedu.homeclick.androidhomeclick.structure.User;

public class UserService {
    private UserInterface userDB;

    public UserService() {
        UserAuth.setInstance();
        userDB = FirestoreDealer.getInstance();
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

    public void findUserInfo(UserInterface.OnUserInfoListener<User> onUserInfoListener, String UID) {
        userDB.findUserInfo(onUserInfoListener, UID);
    }

    public void updateUserInfo(UserInterface.OnUserInfoUpdateListener<User> onUserInfoUpdateListener, User updatedUser) {
        userDB.updateUserInfo(onUserInfoUpdateListener, updatedUser);
    }
}
