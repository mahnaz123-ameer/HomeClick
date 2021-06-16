package csedu.homeclick.androidhomeclick.connector;

import java.util.List;

import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.User;

public interface AdInterface {

    void addUser(User user);

    void findUserInfo(OnUserInfoListener<User> onUserInfoListener, String UID);

    interface OnUserInfoListener<T> {

        void OnUserInfoFound(T data);
    }

    void addAdvertisement(OnAdPostSuccessListener<Advertisement> onAdPostSuccessListener, Advertisement advertisement);

    interface  OnAdPostSuccessListener<T> {
        void OnAdPostSuccessful(Boolean data);
    }

    void getAdsFromDatabase(OnAdsFetchedListener<List<Advertisement>> onAdsFetchedListener);

    interface OnAdsFetchedListener<T> {
        void OnAdsFetchedListener(T ads);
    }
}
