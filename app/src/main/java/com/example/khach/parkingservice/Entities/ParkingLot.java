package com.example.khach.parkingservice.Entities;

import com.example.khach.parkingservice.DAO.DataAccess;
import com.google.firebase.database.DatabaseReference;

public class ParkingLot implements DataAccess {

    public String phoneNo;
    public String parkingId;
    public String name;
    public String size;
    public int reportNo;
    public String address;
    public int likeNo;

    public ParkingLot() {
    }

    public ParkingLot(String phoneNo, String parkingId, String name, String size, int reportNo, String address, int likeNo) {
        this.phoneNo = phoneNo;
        this.parkingId = parkingId;
        this.name = name;
        this.size = size;
        this.reportNo = reportNo;
        this.address = address;
        this.likeNo = likeNo;
    }
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getReportNo() {
        return reportNo;
    }

    public void setReportNo(int reportNo) {
        this.reportNo = reportNo;
    }

    public int getLikeNo() {
        return likeNo;
    }

    public void setLikeNo(int likeNo) {
        this.likeNo = likeNo;
    }

    public ParkingLot GetObject() {
        return new ParkingLot(name, parkingId, phoneNo, size, reportNo, address, likeNo);
    }

    @Override
    public void GetObjectById(String id, DatabaseReference ref) {

    }

    @Override
    public boolean Add(DatabaseReference ref) {
        try {
            ref.child("ParkingLot").child(getPhoneNo().toString()).setValue(GetObject());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean Update(DatabaseReference ref) {
        return false;
    }

    @Override
    public boolean Delete(DatabaseReference ref) {
        return false;
    }
}


