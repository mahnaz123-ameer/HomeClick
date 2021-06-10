package csedu.homeclick.androidhomeclick.structure;

import java.util.Date;

public class SaleAdvertisement extends Advertisement{
    private String propertyCondition;

    public SaleAdvertisement(String type, int payment, int squareFootArea, String areaName, String fullAddress, int numOfBedrooms, int numOfBathrooms, Boolean gasAvailability, String floor, String advertiserName, String advertiserPhoneNumber, Date postCreationDate, String propertyCondition) {
        super(type, payment, squareFootArea, areaName, fullAddress, numOfBedrooms, numOfBathrooms, gasAvailability, floor, advertiserName, advertiserPhoneNumber, postCreationDate);
        this.propertyCondition = propertyCondition;
    }

    public String getPropertyCondition() {
        return propertyCondition;
    }

    public void setPropertyCondition(String propertyCondition) {
        this.propertyCondition = propertyCondition;
    }
}
