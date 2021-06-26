package csedu.homeclick.androidhomeclick.structure;

import java.io.Serializable;

public class FilterCriteria implements Serializable {
    private String adType;
    private int paymentAmount;
    private int numberOfBedrooms;
    private int numberOfBathrooms;
    private Boolean gasAvailability;

    public FilterCriteria() {
        this.adType = null;
        this.paymentAmount = -1;
        this.numberOfBedrooms = -1;
        this.numberOfBathrooms = -1;
        this.gasAvailability = false;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
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
}
