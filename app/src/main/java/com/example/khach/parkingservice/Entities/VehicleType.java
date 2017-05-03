package com.example.khach.parkingservice.Entities;

/**
 * Created by khach on 13/04/2017.
 */

public class VehicleType {
    protected String vehiceCId;
    protected String vehicleName;
    protected String description;

    public VehicleType() {
    }

    public VehicleType(String vehiceCode, String vehicleName, String description) {
        this.vehiceCId = vehiceCode;
        this.vehicleName = vehicleName;
        this.description = description;
    }

    public String getVehiceCode() {
        return vehiceCId;
    }

    public void setVehiceCode(String vehiceCode) {
        this.vehiceCId = vehiceCode;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
