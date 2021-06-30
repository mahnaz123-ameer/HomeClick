package csedu.homeclick.androidhomeclick.structure;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class Leaflet implements Serializable {
    @ServerTimestamp
    private Date postDate;

    private String areaName;
    private String fullAddress;
    private String adType;
    private int numberOfBedrooms;
    private int numberOfBathrooms;
    private Boolean gasAvailability;
    private int paymentAmount;

    private String advertiserUID;


    private String advertisementID;

    public String getAdvertisementID() {
        return advertisementID;
    }

    public void setAdvertisementID(String advertisementID) {
        this.advertisementID = advertisementID;
    }


    public String getAdvertiserUID() {
        return advertiserUID;
    }

    public void setAdvertiserUID(String advertiserUID) {
        this.advertiserUID = advertiserUID;
    }


    public Leaflet() {

    }

    public Leaflet(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, String UID) {
        this.areaName = areaName;
        this.fullAddress = fullAddress;
        this.adType = adType;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.gasAvailability = gasAvailability;
        this.paymentAmount = paymentAmount;

        this.advertiserUID = UID;

    }

    public Leaflet(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount) {
        this.areaName = areaName;
        this.fullAddress = fullAddress;
        this.adType = adType;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.gasAvailability = gasAvailability;
        this.paymentAmount = paymentAmount;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public Boolean getGasAvailability() {
        return gasAvailability;
    }

    public void setGasAvailability(Boolean gasAvailability) {
        this.gasAvailability = gasAvailability;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public java.util.Date getPostDate() {
        return postDate;
    }

    public void setPostDate(java.util.Date postDate) {
        this.postDate = postDate;
    }


}
