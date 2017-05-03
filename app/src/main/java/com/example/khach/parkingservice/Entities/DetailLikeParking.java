package com.example.khach.parkingservice.Entities;

/**
 * Created by khach on 13/04/2017.
 */

public class DetailLikeParking {
    protected String userId;
    protected String parkingId;

    public DetailLikeParking() {
    }

    public DetailLikeParking(String userId, String parkingId) {
        this.userId = userId;
        this.parkingId = parkingId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
