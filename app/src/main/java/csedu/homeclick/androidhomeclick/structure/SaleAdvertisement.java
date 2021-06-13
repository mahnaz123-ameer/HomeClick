package csedu.homeclick.androidhomeclick.structure;

import java.util.Date;

public class SaleAdvertisement extends Advertisement{
    private String propertyCondition;
    private String description;

    private int latitude, longitude;

    public SaleAdvertisement(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, User advertiser, int numberOfBalconies, int floor, Boolean elevator, Boolean generator, Boolean garageSpace, String propertyCondition, String description) {
        super(areaName, fullAddress, adType, numberOfBedrooms, numberOfBathrooms, gasAvailability, paymentAmount, advertiser, numberOfBalconies, floor, elevator, generator, garageSpace);
        this.propertyCondition = propertyCondition;
        this.description = description;
    }

    public SaleAdvertisement(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, User advertiser, int numberOfBalconies, int floor, Boolean elevator, Boolean generator, Boolean garageSpace, String propertyCondition, String description, int latitude, int longitude) {
        super(areaName, fullAddress, adType, numberOfBedrooms, numberOfBathrooms, gasAvailability, paymentAmount, advertiser, numberOfBalconies, floor, elevator, generator, garageSpace);
        this.propertyCondition = propertyCondition;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPropertyCondition() {
        return propertyCondition;
    }

    public void setPropertyCondition(String propertyCondition) {
        this.propertyCondition = propertyCondition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }
}
