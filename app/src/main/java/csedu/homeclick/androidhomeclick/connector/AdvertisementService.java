package csedu.homeclick.androidhomeclick.connector;

import android.net.Uri;

import java.util.List;

import csedu.homeclick.androidhomeclick.database.FirestoreDealer;
import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.RentAdvertisement;
import csedu.homeclick.androidhomeclick.structure.SaleAdvertisement;

public class AdvertisementService {
    private UserService userService;
    private AdInterface adDealer;

    public AdvertisementService() {
        userService = new UserService();
        adDealer = FirestoreDealer.getInstance();
    }

    public void getAdId(AdInterface.OnAdIdListener<Advertisement> onAdIdListener) {
        adDealer.getAdId(onAdIdListener);
    }

    public void uploadPhoto(Uri uri, String fileExtension, String pathID, AdInterface.OnPhotoUploadListener<String> onPhotoUploadListener) {
        adDealer.uploadPhoto(uri, fileExtension, pathID, onPhotoUploadListener);
    }

    public void completeAdPost(Advertisement advert, AdInterface.OnAdPostSuccessListener<Boolean> onAdPostSuccessListener) {
        adDealer.pushAdIntoDatabase(advert, onAdPostSuccessListener);
    }

//    public void addAdvertisement(AdInterface.OnAdPostSuccessListener<Advertisement> onAdPostSuccessListener, AdInterface.OnPhotoUploadProgressListener<List<Uri>> onPhotoUploadProgressListener, Advertisement advertisement, List<Uri> toUpload, List<String> fileExtensions) {
//        adDealer.getAdIdAndUploadPhotos(onAdPostSuccessListener, onPhotoUploadProgressListener, advertisement, toUpload, fileExtensions);
//    }

    public void fetchAdvertisements(AdInterface.OnAdsFetchedListener<List<Advertisement>> onAdsFetchedListener) {
        adDealer.getAdsFromDatabase(onAdsFetchedListener);
    }

    public void fetchMyAds(AdInterface.OnPersonalAdsFetchedListener<List<Advertisement>> onPersonalAdsFetchedListener) {
        adDealer.getMyAds(onPersonalAdsFetchedListener, userService.getUserUID());
    }


    //get ads for ad details showing
    public void getRentAd(AdInterface.OnParticularAdFetchedListener<RentAdvertisement> onParticularAdFetchedListener, String adID) {
        adDealer.getThisRentAd(onParticularAdFetchedListener, adID);
    }
    public void getSaleAd(AdInterface.OnParticularAdFetchedListener<SaleAdvertisement> onParticularAdFetchedListener, String advertisementID) {
        adDealer.getThisSaleAd(onParticularAdFetchedListener, advertisementID);
    }

    public void deletePhotoFolder(String folder, int toDelete, AdInterface.OnPhotoFolderDeletedListener<Boolean> onPhotoFolderDeletedListener) {
        adDealer.deletePhotoFolder(folder, toDelete, onPhotoFolderDeletedListener);
    }

    public void deleteAd(String id, AdInterface.OnAdDeletedListener<Boolean> onAdDeletedListener) {
        adDealer.deleteParticularAd(id, onAdDeletedListener);
    }

    public void editAd(String id, Advertisement advertisement, AdInterface.OnAdEditListener<Boolean> onAdEditListener) {
        adDealer.editParticularAd(id, advertisement, onAdEditListener);
    }
}
