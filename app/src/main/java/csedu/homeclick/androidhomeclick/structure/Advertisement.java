package csedu.homeclick.androidhomeclick.structure;

import org.w3c.dom.Text;

import java.util.Date;

public class Advertisement {
    private String type; //rent sale

    private int payment;
    private int squareFootArea;
    private String areaName;
    private String fullAddress;
    private int numOfBedrooms;
    private int numOfBathrooms;
    private Boolean gasAvailability;
    private String floor;

    private String advertiserName;
    private String advertiserPhoneNumber;

    private Date postCreationDate;

    //rent
//    private String tenantType;
//
//    //sale
//    private String propertyCondition;


    public Advertisement(String type, int payment, int squareFootArea, String areaName, String fullAddress, int numOfBedrooms, int numOfBathrooms, Boolean gasAvailability, String floor, String advertiserName, String advertiserPhoneNumber, Date postCreationDate) {
        this.type = type;
        this.payment = payment;
        this.squareFootArea = squareFootArea;
        this.areaName = areaName;
        this.fullAddress = fullAddress;
        this.numOfBedrooms = numOfBedrooms;
        this.numOfBathrooms = numOfBathrooms;
        this.gasAvailability = gasAvailability;
        this.floor = floor;
        this.advertiserName = advertiserName;
        this.advertiserPhoneNumber = advertiserPhoneNumber;
        this.postCreationDate = postCreationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getSquareFootArea() {
        return squareFootArea;
    }

    public void setSquareFootArea(int squareFootArea) {
        this.squareFootArea = squareFootArea;
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

    public int getNumOfBedrooms() {
        return numOfBedrooms;
    }

    public void setNumOfBedrooms(int numOfBedrooms) {
        this.numOfBedrooms = numOfBedrooms;
    }

    public int getNumOfBathrooms() {
        return numOfBathrooms;
    }

    public void setNumOfBathrooms(int numOfBathrooms) {
        this.numOfBathrooms = numOfBathrooms;
    }

    public Boolean getGasAvailability() {
        return gasAvailability;
    }

    public void setGasAvailability(Boolean gasAvailability) {
        this.gasAvailability = gasAvailability;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    public String getAdvertiserPhoneNumber() {
        return advertiserPhoneNumber;
    }

    public void setAdvertiserPhoneNumber(String advertiserPhoneNumber) {
        this.advertiserPhoneNumber = advertiserPhoneNumber;
    }

    public Date getPostCreationDate() {
        return postCreationDate;
    }

    public void setPostCreationDate(Date postCreationDate) {
        this.postCreationDate = postCreationDate;
    }
}
