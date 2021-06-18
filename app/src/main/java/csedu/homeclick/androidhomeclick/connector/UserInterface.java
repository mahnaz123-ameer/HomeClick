package csedu.homeclick.androidhomeclick.connector;

import csedu.homeclick.androidhomeclick.structure.User;

public interface UserInterface {

    void addUser(User user);

    void findUserInfo(OnUserInfoListener<User> onUserInfoListener, String UID);

    interface OnUserInfoListener<T> {

        void OnUserInfoFound(T data);
    }

    void updateUserInfo(OnUserInfoUpdateListener<User> onUserInfoUpdateListener, User updatedUser);

    interface OnUserInfoUpdateListener<T> {
        void OnUserInfoUpdated();
        void OnUserInfoUpdateFailed();
    }
}
