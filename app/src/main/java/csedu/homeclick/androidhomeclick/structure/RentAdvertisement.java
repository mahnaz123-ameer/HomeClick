package csedu.homeclick.androidhomeclick.structure;

import java.util.Date;

public class RentAdvertisement extends Advertisement{
    private String tenantType;

    public RentAdvertisement(String type, int payment, int squareFootArea, String areaName, String fullAddress, int numOfBedrooms, int numOfBathrooms, Boolean gasAvailability, String floor, String advertiserName, String advertiserPhoneNumber, Date postCreationDate, String tenantType) {
        super(type, payment, squareFootArea, areaName, fullAddress, numOfBedrooms, numOfBathrooms, gasAvailability, floor, advertiserName, advertiserPhoneNumber, postCreationDate);
        this.tenantType = tenantType;
    }

    public String getTenantType() {
        return tenantType;
    }

    public void setTenantType(String tenantType) {
        this.tenantType = tenantType;
    }
}
