package csedu.homeclick.androidhomeclick.structure;

import java.io.Serializable;

public class SaleAdvertisement extends Advertisement implements Serializable {
    private String propertyCondition;
    private String description;

    private int latitude, longitude;

    public SaleAdvertisement(){

    }

    public SaleAdvertisement(String areaName, String fullAddress, String sale, int numOfBedrooms, int numOfBathrooms, int numOfBalconies, int floor, int floorSpace, Boolean gasAvail, Boolean elevatorAvail, Boolean generatorAvail, Boolean garageAvail, Boolean securityAvail, int payAmount, String saleDesc, String situation) {
    }

    public SaleAdvertisement(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, User advertiser, int numberOfBalconies, int floor, int floorSpace, Boolean elevator, Boolean generator, Boolean garageSpace, int numberOfImages, String propertyCondition, String description) {
        super(areaName, fullAddress, adType, numberOfBedrooms, numberOfBathrooms, gasAvailability, paymentAmount,  numberOfBalconies, floor, floorSpace, elevator, generator, garageSpace, numberOfImages);
        this.propertyCondition = propertyCondition;
        this.description = description;
    }

    public SaleAdvertisement(String name, String address, String sale, String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, int numberOfBalconies, int floor, int floorSpace, Boolean elevator, Boolean generator, Boolean garageSpace, int numberOfImages, String propertyCondition, String description, Boolean securityAvail) {
        super(areaName, fullAddress, adType, numberOfBedrooms, numberOfBathrooms, gasAvailability, paymentAmount, numberOfBalconies, floor, floorSpace, elevator, generator, garageSpace, numberOfImages);
        this.propertyCondition = propertyCondition;
        this.description = description;
    }

    public String getPropertyCondition() { return propertyCondition; }

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
