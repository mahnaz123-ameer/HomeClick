package csedu.homeclick.androidhomeclick.connector;

import java.util.List;

import csedu.homeclick.androidhomeclick.structure.Advertisement;
import csedu.homeclick.androidhomeclick.structure.RentAdvertisement;
import csedu.homeclick.androidhomeclick.structure.SaleAdvertisement;

public interface AdInterface {

    void addAdvertisement(OnAdPostSuccessListener<Advertisement> onAdPostSuccessListener, Advertisement advertisement);

    interface  OnAdPostSuccessListener<T> {
        void OnAdPostSuccessful(Boolean data);
    }

    void getAdsFromDatabase(OnAdsFetchedListener<List<Advertisement>> onAdsFetchedListener);

    interface OnAdsFetchedListener<T> {
        void OnAdsFetchedListener(T ads);
    }

    void getMyAds(OnPersonalAdsFetchedListener<List<Advertisement>> onPersonalAdsFetchedListener, String userUID);

    interface OnPersonalAdsFetchedListener<T> {
        void OnPersonalAdsFetchedListener(T ads);
    }

    void getThisRentAd(OnParticularAdFetchedListener<RentAdvertisement> onParticularAdFetchedListener, String advertID);
    void getThisSaleAd(OnParticularAdFetchedListener<SaleAdvertisement> onParticularAdFetchedListener, String advertID);

    interface OnParticularAdFetchedListener<T> {
        void OnParticularAdFetched(T advert);
    }
}
