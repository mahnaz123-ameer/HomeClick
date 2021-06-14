package csedu.homeclick.androidhomeclick.connector;

import android.util.Log;

import java.util.List;

import csedu.homeclick.androidhomeclick.database.FirestoreDealer;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.User;

public class AdvertisementService {
    private UserService userService;
    private AdInterface adDealer;

    public AdvertisementService() {
        userService = new UserService();
        adDealer = FirestoreDealer.getInstance();
    }

    public void findUserInfo(AdInterface.OnUserInfoListener<User> onUserInfoListener, String UID) {
        adDealer.findUserInfo(onUserInfoListener, UID);
    }

    public void addAdvertisement(AdInterface.OnAdPostSuccessListener<Advertisement> onAdPostSuccessListener, Advertisement advertisement) {
        adDealer.addAdvertisement(onAdPostSuccessListener, advertisement);
    }

    public void fetchAdvertisements(AdInterface.OnAdsFetchedListener<List<Advertisement>> onAdsFetchedListener) {
        adDealer.getAdsFromDatabase(onAdsFetchedListener);
    }
}
