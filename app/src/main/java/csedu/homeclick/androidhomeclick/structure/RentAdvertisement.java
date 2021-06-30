package csedu.homeclick.androidhomeclick.structure;

import java.io.Serializable;
import java.util.Date;

public class RentAdvertisement extends Advertisement implements Serializable {
    private String tenantType;

    private int utilityCharge;
    private String description;
    private Boolean securityGuard;
    private Date availableFrom;



    public RentAdvertisement() {
        super();
    }

    public RentAdvertisement(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms,
                             Boolean gasAvailability, int paymentAmount, int numberOfBalconies, int floor, int floorSpace,
                             Boolean elevator, Boolean generator, Boolean garageSpace, int numberOfImages, String tenantType, int utilityCharge, String description, Boolean securityGuard, Date availableFrom) {
        super(areaName, fullAddress, adType, numberOfBedrooms, numberOfBathrooms, gasAvailability, paymentAmount, numberOfBalconies, floor, floorSpace, elevator, generator, garageSpace, numberOfImages);
        this.tenantType = tenantType;
        this.utilityCharge = utilityCharge;
        this.description = description;
        this.securityGuard = securityGuard;
        this.availableFrom = availableFrom;
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

}

