package com.example.khach.parkingservice.Entities;

import com.example.khach.parkingservice.DAO.DataAccess;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by khach on 13/04/2017.
 */

public  class Account implements DataAccess {
    public String nameAccount;
    public String passWord;
    public String phoneNo;
    public String accountCode;
    public String imageAvatar;

    public Account() {
    }
    public Account(String nameAccount, String passWord, String phoneNo, String accountCode, String imageAvatar) {
        this.nameAccount = nameAccount;
        this.passWord = passWord;
        this.phoneNo = phoneNo;
        this.accountCode = accountCode;
        this.imageAvatar = imageAvatar;
    }

    public String getNameAccount() {
        return nameAccount;
    }
    public void setNameAccount(String nameAccount) {
        this.nameAccount = nameAccount;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        accountCode = accountCode;
    }

    public String getImageAvatar() {
        return imageAvatar;
    }

    public void setImageAvatar(String imageAvatar) {
        this.imageAvatar = imageAvatar;
    }
    public Account GetAccount(){
        return new Account(nameAccount,passWord,phoneNo,accountCode,imageAvatar);
    }
    @Override
    public void GetObjectById(String id,DatabaseReference ref) {
        ref.child("Account").child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean Delete(DatabaseReference ref) {
        try{
            ref.child("Account").child(getPhoneNo()).removeValue();
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    @Override
    public boolean Update(DatabaseReference ref) {
        try{
            ref.child("Account").child(getPhoneNo()).setValue(GetAccount());
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    @Override
    public boolean Add(DatabaseReference ref) {
      /*  DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();*/
        try{
            ref.child("Account").child(getNameAccount()).setValue(GetAccount());
            return true;
        }catch (Exception ex){
            return false;
        }

    }
}
