package com.example.khach.parkingservice.Entities;

import com.example.khach.parkingservice.DAO.DataAccess;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by khach on 13/04/2017.
 */

public class User implements DataAccess {
    protected String userId;
    protected String userName;
    protected int year;
    protected String phoneNo;

    public User() {
    }

    public User(String userId, String userName, int year, String phoneNo) {
        this.userId = userId;
        this.userName = userName;
        this.year = year;
        this.phoneNo = phoneNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
public User GetObject(){
     return new User(userId,userName,year,phoneNo);
}
    @Override
    public void GetObjectById(String id, DatabaseReference ref) {

    }

    @Override
    public boolean Add(DatabaseReference ref) {
       /* DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();*/
        try{
            ref.child("User").child(getPhoneNo().toString()).setValue(GetObject());
            return true;
        }catch (Exception ex){
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
