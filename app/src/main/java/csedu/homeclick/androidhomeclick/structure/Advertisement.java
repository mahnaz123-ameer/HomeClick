package csedu.homeclick.androidhomeclick.structure;



import java.io.Serializable;
import java.util.Objects;


public class Advertisement extends Leaflet implements Serializable {
    private int numberOfBalconies;
    private int floor;
    private int floorSpace;
    private Boolean elevator;
    private Boolean generator;
    private Boolean garageSpace;

    public Advertisement() {
        super();
    }

    public Advertisement(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, User advertiser, int numberOfBalconies, int floor, int floorSpace, Boolean elevator, Boolean generator, Boolean garageSpace) {
        super(areaName, fullAddress, adType, numberOfBedrooms, numberOfBathrooms, gasAvailability, paymentAmount, advertiser);
        this.numberOfBalconies = numberOfBalconies;
        this.floor = floor;
        this.floorSpace = floorSpace;
        this.elevator = elevator;
        this.generator = generator;
        this.garageSpace = garageSpace;
    }

    public Advertisement(String areaName, String fullAddress, String adType, int numberOfBedrooms, int numberOfBathrooms, Boolean gasAvailability, int paymentAmount, int numberOfBalconies, int floor, int floorSpace, Boolean elevator, Boolean generator, Boolean garageSpace) {
        super(areaName, fullAddress, adType, numberOfBedrooms, numberOfBathrooms, gasAvailability, paymentAmount);
        this.numberOfBalconies = numberOfBalconies;
        this.floor = floor;
        this.floorSpace = floorSpace;
        this.elevator = elevator;
        this.generator = generator;
        this.garageSpace = garageSpace;
    }

    public int getNumberOfBalconies() {
        return numberOfBalconies;
    }

    public void setNumberOfBalconies(int numberOfBalconies) {
        this.numberOfBalconies = numberOfBalconies;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Boolean getElevator() {
        return elevator;
    }

    public void setElevator(Boolean elevator) {
        this.elevator = elevator;
    }

    public Boolean getGenerator() {
        return generator;
    }

    public void setGenerator(Boolean generator) {
        this.generator = generator;
    }

    public Boolean getGarageSpace() {
        return garageSpace;
    }

    public void setGarageSpace(Boolean garageSpace) {
        this.garageSpace = garageSpace;
    }

    public int getFloorSpace() {
        return floorSpace;
    }

    public void setFloorSpace(int floorSpace) {
        this.floorSpace = floorSpace;
    }
}
