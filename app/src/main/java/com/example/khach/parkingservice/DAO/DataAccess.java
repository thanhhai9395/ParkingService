package com.example.khach.parkingservice.DAO;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by khach on 15/04/2017.
 */

public interface DataAccess {
    public void GetObjectById(String id,DatabaseReference ref);
    public boolean Add(DatabaseReference ref);
    public boolean Update(DatabaseReference ref);
    public boolean Delete(DatabaseReference ref);
}
