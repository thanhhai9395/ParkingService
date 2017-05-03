package com.example.khach.parkingservice.Entities;

/**
 * Created by khach on 13/04/2017.
 */

public class DetailFeeParking {
    protected String parkingId;
    protected String vehiceCId;
    protected double fee;
    protected int spaceNo;

    public DetailFeeParking() {
    }

    public DetailFeeParking(String parkingId, String vehiceCId, double fee, int spaceNo) {
        this.parkingId = parkingId;
        this.vehiceCId = vehiceCId;
        this.fee = fee;
        this.spaceNo = spaceNo;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getVehiceCId() {
        return vehiceCId;
    }

    public void setVehiceCId(String vehiceCId) {
        this.vehiceCId = vehiceCId;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getSpaceNo() {
        return spaceNo;
    }

    public void setSpaceNo(int spaceNo) {
        this.spaceNo = spaceNo;
    }
}
