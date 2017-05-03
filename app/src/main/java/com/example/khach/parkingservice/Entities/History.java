package com.example.khach.parkingservice.Entities;

/**
 * Created by khach on 13/04/2017.
 */

public class History {
    protected String userId;
    protected String parkingId;
    protected String time;

    public History() {
    }

    public History(String userId, String parkingId, String time) {
        this.userId = userId;
        this.parkingId = parkingId;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
