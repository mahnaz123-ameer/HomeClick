package csedu.homeclick.androidhomeclick.structure;

import java.util.Date;

public class RentAdvertisement extends Advertisement{
    private String tenantType;

    private int utilityCharge;
    private String description;
    private Boolean securityGuard;
    private Date availableFrom;

    private double latitude, longitude;


    public RentAdvertisement(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, int numberOfBalconies, int floor, Boolean elevator, Boolean generator, Boolean garageSpace, String tenantType, int utilityCharge, String description, Boolean securityGuard, Date availableFrom) {
        super(areaName, fullAddress, adType, numberOfBedrooms, numberOfBathrooms, gasAvailability, paymentAmount, numberOfBalconies, floor, elevator, generator, garageSpace);
        this.tenantType = tenantType;
        this.utilityCharge = utilityCharge;
        this.description = description;
        this.securityGuard = securityGuard;
        this.availableFrom = availableFrom;
    }

    public RentAdvertisement(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, User advertiser, int numberOfBalconies, int floor, Boolean elevator, Boolean generator, Boolean garageSpace, String tenantType, int utilityCharge, String description, Boolean securityGuard, Date availableFrom) {
        super(areaName, fullAddress, adType, numberOfBedrooms, numberOfBathrooms, gasAvailability, paymentAmount, advertiser, numberOfBalconies, floor, elevator, generator, garageSpace);
        this.tenantType = tenantType;
        this.utilityCharge = utilityCharge;
        this.description = description;
        this.securityGuard = securityGuard;
        this.availableFrom = availableFrom;
    }

    public RentAdvertisement(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, User advertiser, int numberOfBalconies, int floor, Boolean elevator, Boolean generator, Boolean garageSpace, String tenantType, int utilityCharge, String description, Boolean securityGuard, Date availableFrom, double latitude, double longitude) {
        super(areaName, fullAddress, adType, numberOfBedrooms, numberOfBathrooms, gasAvailability, paymentAmount, advertiser, numberOfBalconies, floor, elevator, generator, garageSpace);
        this.tenantType = tenantType;
        this.utilityCharge = utilityCharge;
        this.description = description;
        this.securityGuard = securityGuard;
        this.availableFrom = availableFrom;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTenantType() {
        return tenantType;
    }

    public void setTenantType(String tenantType) {
        this.tenantType = tenantType;
    }

    public int getUtilityCharge() {
        return utilityCharge;
    }

    public void setUtilityCharge(int utilityCharge) {
        this.utilityCharge = utilityCharge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSecurityGuard() {
        return securityGuard;
    }

    public void setSecurityGuard(Boolean securityGuard) {
        this.securityGuard = securityGuard;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.availableFrom = availableFrom;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

