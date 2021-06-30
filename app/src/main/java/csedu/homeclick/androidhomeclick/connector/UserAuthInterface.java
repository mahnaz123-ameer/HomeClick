package csedu.homeclick.androidhomeclick.connector;

import android.content.Context;

import csedu.homeclick.androidhomeclick.structure.User;

public interface UserAuthInterface {
    void signInNewUser(User user, Context context, SendLinkToUserListener<User> sendLinkToUserListener);

    interface SendLinkToUserListener<E> {
        void OnSendLinkSuccessful(String toastMessage);
        void OnSendLinkFailed(String error);
    }

    void signInOldUser(String emailAddress, Context context, SendLinkToUserListener<String> sendLinkToUserListener);
}
