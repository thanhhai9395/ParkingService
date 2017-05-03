package com.example.khach.parkingservice.Entities;

import com.example.khach.parkingservice.DAO.DataAccess;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by khach on 13/04/2017.
 */

public class AccountType implements DataAccess{
    protected String accountTypeCode;
    protected String nameAccountCode;

    public AccountType() {
    }

    public AccountType(String accountTypeCode, String nameAccountCode) {
        this.accountTypeCode = accountTypeCode;
        this.nameAccountCode = nameAccountCode;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public String getNameAccountCode() {
        return nameAccountCode;
    }

    public void setNameAccountCode(String nameAccountCode) {
        this.nameAccountCode = nameAccountCode;
    }

    @Override
    public void GetObjectById(String id, DatabaseReference ref) {

    }

    @Override
    public boolean Add(DatabaseReference ref) {
        return false;
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
