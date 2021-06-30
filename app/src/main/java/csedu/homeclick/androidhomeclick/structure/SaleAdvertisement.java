package csedu.homeclick.androidhomeclick.structure;

import java.io.Serializable;

public class SaleAdvertisement extends Advertisement implements Serializable {
    private String propertyCondition;
    private String description;



    public SaleAdvertisement(){
        super();
    }


    public SaleAdvertisement(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, int numberOfBalconies, int floor, int floorSpace, Boolean elevator, Boolean generator, Boolean garageSpace, int numberOfImages, String propertyCondition, String description, Boolean securityAvail) {
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

}
